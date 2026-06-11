#include "SensorPacket.h"

#include <chrono>
#include <ctime>
#include <iostream>
#include <random>
#include <string>
#include <thread>

#define WIN32_LEAN_AND_MEAN
#include <windows.h>
#include <winhttp.h>

#pragma comment(lib, "winhttp.lib")

#ifdef USE_ASM_CHECKSUM
extern "C" unsigned int calculate_checksum(const unsigned char* data, size_t length);
#endif

namespace
{
    constexpr wchar_t hostName[] = L"localhost";
    constexpr int hostPort = 8080;
    constexpr wchar_t apiPath[] = L"/api/sensors";

    unsigned int fallbackChecksum(const std::string& payload)
    {
        unsigned int value = 0;
        for (unsigned char character : payload)
        {
            value = (value + character) & 0xFF;
        }

        return value;
    }

    unsigned int calculatePacketChecksum(const std::string& payload)
    {
#ifdef USE_ASM_CHECKSUM
        return calculate_checksum(reinterpret_cast<const unsigned char*>(payload.data()), payload.size());
#else
        return fallbackChecksum(payload);
#endif
    }

    std::string currentTimestamp()
    {
        const auto now = std::chrono::system_clock::now();
        const std::time_t timeValue = std::chrono::system_clock::to_time_t(now);
        std::tm utcTime {};
        gmtime_s(&utcTime, &timeValue);

        char buffer[32] = {};
        std::strftime(buffer, sizeof(buffer), "%Y-%m-%dT%H:%M:%SZ", &utcTime);
        return buffer;
    }

    SensorPacket generatePacket(std::mt19937& engine)
    {
        std::uniform_real_distribution<double> temperature(62.0, 88.0);
        std::uniform_real_distribution<double> pressure(96.0, 106.0);
        std::uniform_real_distribution<double> vibration(10.0, 82.0);
        std::uniform_real_distribution<double> oxygenLevel(85.0, 99.0);
        std::uniform_int_distribution<int> heartRate(60, 125);
        std::uniform_real_distribution<double> batteryLevel(12.0, 100.0);

        SensorPacket packet {
            temperature(engine),
            pressure(engine),
            vibration(engine),
            oxygenLevel(engine),
            heartRate(engine),
            batteryLevel(engine),
            currentTimestamp(),
            0
        };

        packet.checksum = calculatePacketChecksum(packet.canonicalPayload());
        return packet;
    }

    bool sendPacketToBackend(const SensorPacket& packet)
    {
        const std::wstring headers = L"Content-Type: application/json\r\n";
        const std::string json = packet.toJson();

        HINTERNET session = WinHttpOpen(L"SensorSimulator/1.0", WINHTTP_ACCESS_TYPE_DEFAULT_PROXY, WINHTTP_NO_PROXY_NAME, WINHTTP_NO_PROXY_BYPASS, 0);
        if (session == nullptr)
        {
            return false;
        }

        HINTERNET connect = WinHttpConnect(session, hostName, hostPort, 0);
        if (connect == nullptr)
        {
            WinHttpCloseHandle(session);
            return false;
        }

        HINTERNET request = WinHttpOpenRequest(connect, L"POST", apiPath, nullptr, WINHTTP_NO_REFERER, WINHTTP_DEFAULT_ACCEPT_TYPES, 0);
        if (request == nullptr)
        {
            WinHttpCloseHandle(connect);
            WinHttpCloseHandle(session);
            return false;
        }

        const BOOL sendResult = WinHttpSendRequest(
            request,
            headers.c_str(),
            static_cast<DWORD>(headers.size()),
            const_cast<char*>(json.data()),
            static_cast<DWORD>(json.size()),
            static_cast<DWORD>(json.size()),
            0);

        bool success = false;
        if (sendResult == TRUE && WinHttpReceiveResponse(request, nullptr) == TRUE)
        {
            DWORD statusCode = 0;
            DWORD statusCodeSize = sizeof(statusCode);
            success = WinHttpQueryHeaders(request, WINHTTP_QUERY_STATUS_CODE | WINHTTP_QUERY_FLAG_NUMBER, WINHTTP_HEADER_NAME_BY_INDEX, &statusCode, &statusCodeSize, WINHTTP_NO_HEADER_INDEX) == TRUE
                && statusCode >= 200
                && statusCode < 300;
        }

        WinHttpCloseHandle(request);
        WinHttpCloseHandle(connect);
        WinHttpCloseHandle(session);
        return success;
    }
}

int main()
{
    std::random_device device;
    std::mt19937 engine(device());

    std::cout << "Starting sensor simulator. Sending data to http://localhost:8080/api/sensors" << std::endl;

    while (true)
    {
        const SensorPacket packet = generatePacket(engine);
        const bool sent = sendPacketToBackend(packet);

        std::cout
            << packet.timestamp
            << " temperature=" << packet.temperature
            << " oxygen=" << packet.oxygenLevel
            << " battery=" << packet.batteryLevel
            << " checksum=" << packet.checksum
            << " status=" << (sent ? "sent" : "failed")
            << std::endl;

        std::this_thread::sleep_for(std::chrono::seconds(1));
    }
}
#pragma once

#include <iomanip>
#include <sstream>
#include <string>

struct SensorPacket
{
    double temperature;
    double pressure;
    double vibration;
    double oxygenLevel;
    int heartRate;
    double batteryLevel;
    std::string timestamp;
    unsigned int checksum;

    std::string canonicalPayload() const
    {
        std::ostringstream stream;
        stream << timestamp << '|'
               << std::fixed << std::setprecision(2)
               << temperature << '|'
               << pressure << '|'
               << vibration << '|'
               << oxygenLevel << '|'
               << heartRate << '|'
               << batteryLevel;
        return stream.str();
    }

    std::string toJson() const
    {
        std::ostringstream stream;
        stream << std::fixed << std::setprecision(2);
        stream << "{";
        stream << "\"temperature\":" << temperature << ',';
        stream << "\"pressure\":" << pressure << ',';
        stream << "\"vibration\":" << vibration << ',';
        stream << "\"oxygenLevel\":" << oxygenLevel << ',';
        stream << "\"heartRate\":" << heartRate << ',';
        stream << "\"batteryLevel\":" << batteryLevel << ',';
        stream << "\"timestamp\":\"" << timestamp << "\",";
        stream << "\"checksum\":" << checksum;
        stream << "}";
        return stream.str();
    }
};
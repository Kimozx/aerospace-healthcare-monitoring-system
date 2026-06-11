# C++ Sensor Simulator

This component simulates a live telemetry source.

Responsibilities:

1. Generate fake sensor data every second.
2. Build the canonical payload string.
3. Calculate checksum.
4. Send JSON packets to the Java backend.

Core files:

1. `include/SensorPacket.h`
2. `src/main.cpp`
3. `../assembly-checksum/checksum.asm`

Example build flow:

```powershell
cmake -S . -B build
cmake --build build
.
\build\sensor-simulator.exe
```
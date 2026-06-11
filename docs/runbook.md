# Runbook

This runbook explains how to start, validate, and demo the Aerospace and Healthcare Monitoring System.

## Recommended Startup Order

1. Start the Java Spring Boot backend.
2. Start the Python analysis service.
3. Build and run the C++ simulator.
4. Launch the C# control panel.

## Java Backend

Working directory: `java-backend`

PowerShell:

```powershell
mvn spring-boot:run
```

Expected API base URL:

```text
http://localhost:8080/api
```

Quick checks:

```text
GET /api/health
GET /api/sensors/latest
GET /api/alerts
```

## Python Analysis Service

Working directory: `python-analysis`

PowerShell:

```powershell
pip install -r requirements.txt
python analyzer.py
```

Expected URL:

```text
http://localhost:8000
```

Quick checks:

```text
GET /health
POST /api/analysis/run
GET /api/analysis/report
```

## C++ Simulator

Working directory: `cpp-simulator`

Example CMake flow:

```powershell
cmake -S . -B build
cmake --build build
.
\build\sensor-simulator.exe
```

Behavior:

1. Generate a sensor packet every second.
2. Compute checksum.
3. POST packet to `http://localhost:8080/api/sensors`.

## C# Control Panel

Working directory: `csharp-control-panel`

PowerShell:

```powershell
dotnet run
```

Behavior:

1. Start monitoring.
2. Stop monitoring.
3. View latest values.
4. View alerts.
5. Export report.

## Assembly Checksum

The checksum routine is consumed by the C++ simulator when built with the expected Windows/MSVC-compatible toolchain.

Canonical checksum input format:

```text
timestamp|temperature|pressure|vibration|oxygenLevel|heartRate|batteryLevel
```

## VHDL Alert Circuit

Use a VHDL simulator such as ModelSim or GHDL to run the testbench.

Files:

1. `alert_circuit.vhd`
2. `alert_circuit_tb.vhd`

Expected behavior:

If any of the inputs are `1`, `alert_signal` should be `1`.

## Demo Checklist

1. Backend health endpoint returns `ok`.
2. Python health endpoint returns `ok` and backend reachability state.
3. Simulator logs successful sends.
4. Backend stores records.
5. Python report contains current alerts.
6. Control panel shows latest values and alert history.

## Troubleshooting

1. If the Python service cannot analyze data, check that the backend is running on port `8080`.
2. If the control panel shows no values, check `GET /api/sensors/latest` in the backend.
3. If the simulator fails to send, confirm that the backend is listening on `localhost:8080`.
4. If alerts are missing, call `POST /api/analysis/run` manually.
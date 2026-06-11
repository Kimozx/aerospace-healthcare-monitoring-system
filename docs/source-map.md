# Source Map

This file explains what each top-level folder owns.

## `assembly-checksum`

Low-level checksum logic used by the simulator.

## `cpp-simulator`

Sensor packet generation and HTTP submission to the backend.

Key files:

1. `include/SensorPacket.h`
2. `src/main.cpp`
3. `CMakeLists.txt`

## `csharp-control-panel`

Desktop operator interface for monitoring workflow.

Key files:

1. `MainForm.cs`
2. `Program.cs`
3. `AerospaceHealthcareControlPanel.csproj`

## `docs`

Architecture, API contract, runbook, and source guide.

## `java-backend`

Spring Boot application for ingestion, storage, query, and alert persistence.

Key packages:

1. `controller`
2. `dto`
3. `model`
4. `repository`
5. `service`
6. `util`

## `python-analysis`

Rule-based anomaly detection and report generation.

Key files:

1. `analyzer.py`
2. `report_writer.py`
3. `thresholds.py`

## `shared`

Shared sample payloads and cross-component examples.

## `vhdl-alert-circuit`

Hardware-oriented alert logic model and testbench.
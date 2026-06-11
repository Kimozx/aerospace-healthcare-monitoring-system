# Java Backend

This Spring Boot service is the central API for the project.

Responsibilities:

1. Receive sensor packets from the C++ simulator.
2. Verify checksum integrity.
3. Store telemetry in H2.
4. Store alerts from the Python analysis service.
5. Serve records and alerts to the C# control panel.

Main endpoints:

1. `GET /api/health`
2. `POST /api/sensors`
3. `GET /api/sensors`
4. `GET /api/sensors/latest`
5. `POST /api/alerts`
6. `GET /api/alerts`

Run with:

```powershell
mvn spring-boot:run
```
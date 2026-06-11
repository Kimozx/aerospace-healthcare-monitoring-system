# Architecture Notes

## Data Contract

All services use the same packet shape:

```json
{
  "temperature": 72.4,
  "pressure": 101.2,
  "vibration": 22.5,
  "oxygenLevel": 97.0,
  "heartRate": 76,
  "batteryLevel": 88.0,
  "timestamp": "2026-06-10T14:30:05Z",
  "checksum": 173
}
```

## Integration Rules

1. The C++ simulator sends JSON to the Java backend every second.
2. The backend verifies the checksum before saving a record.
3. The Python service fetches sensor records from the backend.
4. The Python service posts alerts back to the backend.
5. The C# control panel reads the latest record and recent alerts.
6. The control panel exports the latest Python report to a local text file.

## Suggested Improvements

1. Add authentication between services.
2. Replace H2 with PostgreSQL.
3. Schedule Python analysis automatically with Celery or APScheduler.
4. Add charts in the C# desktop application.
5. Move checksum code into a shared native library.
# Python Analysis Service

This Flask service performs rule-based analysis over telemetry stored in the Java backend.

Responsibilities:

1. Fetch sensor records from the backend.
2. Detect abnormal values.
3. Publish alerts back to the backend.
4. Generate a plain-text report.
5. Expose analysis and health endpoints.

Main endpoints:

1. `GET /health`
2. `POST /api/analysis/run`
3. `GET /api/analysis/report`

Run with:

```powershell
pip install -r requirements.txt
python analyzer.py
```
from pathlib import Path
import os

import requests
from flask import Flask, jsonify, Response

from report_writer import build_report
from thresholds import THRESHOLDS


BACKEND_URL = os.getenv("BACKEND_URL", "http://localhost:8080")
REPORT_PATH = Path(__file__).resolve().parent / "reports" / "latest_report.txt"
app = Flask(__name__)
published_alerts = set()


def fetch_sensor_records():
    response = requests.get(f"{BACKEND_URL}/api/sensors", timeout=10)
    response.raise_for_status()
    return response.json()


def detect_alerts(record):
    alerts = []

    if record["temperature"] > THRESHOLDS["temperature"]:
        alerts.append({
            "alertType": "HIGH_TEMPERATURE",
            "message": f"Temperature reached {record['temperature']}",
            "severity": "HIGH",
            "sensorTimestamp": record["timestamp"],
        })

    if record["oxygenLevel"] < THRESHOLDS["oxygenLevel"]:
        alerts.append({
            "alertType": "LOW_OXYGEN",
            "message": f"Oxygen dropped to {record['oxygenLevel']}",
            "severity": "CRITICAL",
            "sensorTimestamp": record["timestamp"],
        })

    if record["vibration"] > THRESHOLDS["vibration"]:
        alerts.append({
            "alertType": "VIBRATION_WARNING",
            "message": f"Vibration reached {record['vibration']}",
            "severity": "MEDIUM",
            "sensorTimestamp": record["timestamp"],
        })

    if record["batteryLevel"] < THRESHOLDS["batteryLevel"]:
        alerts.append({
            "alertType": "LOW_BATTERY",
            "message": f"Battery dropped to {record['batteryLevel']}",
            "severity": "HIGH",
            "sensorTimestamp": record["timestamp"],
        })

    return alerts


def publish_alerts(alerts):
    created = 0
    for alert in alerts:
        key = (alert["alertType"], alert["sensorTimestamp"])
        if key in published_alerts:
            continue

        response = requests.post(f"{BACKEND_URL}/api/alerts", json=alert, timeout=10)
        response.raise_for_status()
        published_alerts.add(key)
        created += 1

    return created


def analyze_all_records():
    records = fetch_sensor_records()
    alerts = []
    for record in records:
        alerts.extend(detect_alerts(record))

    created_count = publish_alerts(alerts)
    report = build_report(records, alerts)
    REPORT_PATH.parent.mkdir(parents=True, exist_ok=True)
    REPORT_PATH.write_text(report, encoding="utf-8")
    return {
        "recordCount": len(records),
        "alertCount": len(alerts),
        "publishedAlertCount": created_count,
        "reportPath": str(REPORT_PATH),
    }, report


def backend_health_status():
    try:
        response = requests.get(f"{BACKEND_URL}/api/health", timeout=5)
        response.raise_for_status()
        return {
            "reachable": True,
            "backend": response.json(),
        }
    except requests.RequestException as exception:
        return {
            "reachable": False,
            "error": str(exception),
        }


@app.get("/health")
def health_check():
    return jsonify({
        "status": "ok",
        "service": "python-analysis",
        "backendUrl": BACKEND_URL,
        "backendStatus": backend_health_status(),
    })


@app.post("/api/analysis/run")
def run_analysis():
    try:
        summary, _ = analyze_all_records()
        return jsonify(summary)
    except requests.RequestException as exception:
        return jsonify({
            "status": "error",
            "message": str(exception),
        }), 502


@app.get("/api/analysis/report")
def get_report():
    try:
        _, report = analyze_all_records()
        return Response(report, mimetype="text/plain")
    except requests.RequestException as exception:
        return jsonify({
            "status": "error",
            "message": str(exception),
        }), 502


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8000, debug=True)
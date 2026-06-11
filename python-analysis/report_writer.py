from datetime import datetime


def build_report(records, alerts):
    lines = [
        "Aerospace and Healthcare Monitoring Report",
        f"Generated at: {datetime.utcnow().isoformat()}Z",
        f"Total records analyzed: {len(records)}",
        f"Total alerts found: {len(alerts)}",
        "",
        "Recent alerts:",
    ]

    if not alerts:
        lines.append("No abnormal values detected.")
    else:
        for alert in alerts[-10:]:
            lines.append(
                f"- [{alert['severity']}] {alert['alertType']} at {alert['sensorTimestamp']}: {alert['message']}"
            )

    return "\n".join(lines)
package com.aeromonitoring.dto;

import jakarta.validation.constraints.NotBlank;

public class AlertRequest {

    @NotBlank
    private String alertType;

    @NotBlank
    private String message;

    @NotBlank
    private String severity;

    @NotBlank
    private String sensorTimestamp;

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getSensorTimestamp() {
        return sensorTimestamp;
    }

    public void setSensorTimestamp(String sensorTimestamp) {
        this.sensorTimestamp = sensorTimestamp;
    }
}
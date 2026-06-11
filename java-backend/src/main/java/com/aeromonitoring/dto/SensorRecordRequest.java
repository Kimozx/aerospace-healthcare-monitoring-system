package com.aeromonitoring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SensorRecordRequest {

    @NotNull
    private Double temperature;

    @NotNull
    private Double pressure;

    @NotNull
    private Double vibration;

    @NotNull
    private Double oxygenLevel;

    @NotNull
    private Integer heartRate;

    @NotNull
    private Double batteryLevel;

    @NotBlank
    private String timestamp;

    @NotNull
    private Integer checksum;

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getVibration() {
        return vibration;
    }

    public void setVibration(Double vibration) {
        this.vibration = vibration;
    }

    public Double getOxygenLevel() {
        return oxygenLevel;
    }

    public void setOxygenLevel(Double oxygenLevel) {
        this.oxygenLevel = oxygenLevel;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public Double getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getChecksum() {
        return checksum;
    }

    public void setChecksum(Integer checksum) {
        this.checksum = checksum;
    }
}
package com.aeromonitoring.controller;

import com.aeromonitoring.dto.AlertRequest;
import com.aeromonitoring.dto.SensorRecordRequest;
import com.aeromonitoring.model.AlertRecord;
import com.aeromonitoring.model.SensorRecord;
import com.aeromonitoring.service.AlertService;
import com.aeromonitoring.service.SensorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class SensorController {

    private final SensorService sensorService;
    private final AlertService alertService;

    public SensorController(SensorService sensorService, AlertService alertService) {
        this.sensorService = sensorService;
        this.alertService = alertService;
    }

    @PostMapping("/sensors")
    public ResponseEntity<?> saveSensorRecord(@Valid @RequestBody SensorRecordRequest request) {
        try {
            SensorRecord saved = sensorService.save(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
        }
    }

    @GetMapping("/sensors")
    public List<SensorRecord> getAllSensorRecords() {
        return sensorService.findAll();
    }

    @GetMapping("/sensors/latest")
    public ResponseEntity<SensorRecord> getLatestSensorRecord() {
        SensorRecord latest = sensorService.findLatest();
        return latest == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(latest);
    }

    @PostMapping("/alerts")
    public ResponseEntity<AlertRecord> saveAlert(@Valid @RequestBody AlertRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alertService.save(request));
    }

    @GetMapping("/alerts")
    public List<AlertRecord> getAlerts() {
        return alertService.findAll();
    }

    @GetMapping("/health")
    public Map<String, Object> getHealth() {
        return Map.of(
            "status", "ok",
            "service", "java-backend",
            "sensorRecordCount", sensorService.countRecords(),
            "alertCount", alertService.countAlerts());
    }
}
package com.aeromonitoring.service;

import com.aeromonitoring.dto.AlertRequest;
import com.aeromonitoring.model.AlertRecord;
import com.aeromonitoring.repository.AlertRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class AlertService {

    private final AlertRecordRepository repository;

    public AlertService(AlertRecordRepository repository) {
        this.repository = repository;
    }

    public AlertRecord save(AlertRequest request) {
        AlertRecord alertRecord = new AlertRecord();
        alertRecord.setAlertType(request.getAlertType());
        alertRecord.setMessage(request.getMessage());
        alertRecord.setSeverity(request.getSeverity());
        alertRecord.setSensorTimestamp(request.getSensorTimestamp());
        alertRecord.setCreatedAt(LocalDateTime.now());
        return repository.save(alertRecord);
    }

    public List<AlertRecord> findAll() {
        return repository.findAll()
            .stream()
            .sorted(Comparator.comparing(AlertRecord::getCreatedAt).reversed())
            .toList();
    }

    public long countAlerts() {
        return repository.count();
    }
}
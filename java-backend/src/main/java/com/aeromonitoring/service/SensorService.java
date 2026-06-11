package com.aeromonitoring.service;

import com.aeromonitoring.dto.SensorRecordRequest;
import com.aeromonitoring.model.SensorRecord;
import com.aeromonitoring.repository.SensorRecordRepository;
import com.aeromonitoring.util.ChecksumUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SensorService {

    private final SensorRecordRepository repository;

    public SensorService(SensorRecordRepository repository) {
        this.repository = repository;
    }

    public SensorRecord save(SensorRecordRequest request) {
        String payload = ChecksumUtils.canonicalPayload(
            request.getTimestamp(),
            request.getTemperature(),
            request.getPressure(),
            request.getVibration(),
            request.getOxygenLevel(),
            request.getHeartRate(),
            request.getBatteryLevel());

        int expectedChecksum = ChecksumUtils.calculate(payload);
        if (expectedChecksum != request.getChecksum()) {
            throw new IllegalArgumentException("Checksum mismatch. Packet may be corrupted.");
        }

        SensorRecord record = new SensorRecord();
        record.setTemperature(request.getTemperature());
        record.setPressure(request.getPressure());
        record.setVibration(request.getVibration());
        record.setOxygenLevel(request.getOxygenLevel());
        record.setHeartRate(request.getHeartRate());
        record.setBatteryLevel(request.getBatteryLevel());
        record.setTimestamp(request.getTimestamp());
        record.setChecksum(request.getChecksum());
        record.setReceivedAt(LocalDateTime.now());
        return repository.save(record);
    }

    public List<SensorRecord> findAll() {
        return repository.findAll();
    }

    public SensorRecord findLatest() {
        return repository.findTopByOrderByIdDesc().orElse(null);
    }

    public long countRecords() {
        return repository.count();
    }
}
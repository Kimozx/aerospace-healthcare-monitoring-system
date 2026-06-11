package com.aeromonitoring.repository;

import com.aeromonitoring.model.SensorRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensorRecordRepository extends JpaRepository<SensorRecord, Long> {
    Optional<SensorRecord> findTopByOrderByIdDesc();
}
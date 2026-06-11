package com.aeromonitoring.repository;

import com.aeromonitoring.model.AlertRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRecordRepository extends JpaRepository<AlertRecord, Long> {
}
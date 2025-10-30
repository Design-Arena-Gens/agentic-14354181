package com.farm.repository;

import com.farm.entity.Goat;
import com.farm.entity.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, UUID> {
    List<HealthRecord> findByGoat(Goat goat);
    List<HealthRecord> findByRecordDateBetween(LocalDate start, LocalDate end);
}

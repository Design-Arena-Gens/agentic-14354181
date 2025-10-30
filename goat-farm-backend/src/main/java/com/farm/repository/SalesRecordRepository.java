package com.farm.repository;

import com.farm.entity.Goat;
import com.farm.entity.SalesRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SalesRecordRepository extends JpaRepository<SalesRecord, UUID> {
    List<SalesRecord> findByGoat(Goat goat);
    List<SalesRecord> findBySaleDateBetween(LocalDate start, LocalDate end);
}

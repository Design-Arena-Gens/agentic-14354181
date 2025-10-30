package com.farm.repository;

import com.farm.entity.BreedingRecord;
import com.farm.entity.Goat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BreedingRecordRepository extends JpaRepository<BreedingRecord, UUID> {
    List<BreedingRecord> findByDoe(Goat doe);
    List<BreedingRecord> findByBuck(Goat buck);
}

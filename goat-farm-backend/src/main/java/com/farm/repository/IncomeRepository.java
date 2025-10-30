package com.farm.repository;

import com.farm.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IncomeRepository extends JpaRepository<Income, UUID> {
    List<Income> findByIncomeDateBetween(LocalDate start, LocalDate end);
}

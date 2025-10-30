package com.farm.repository;

import com.farm.entity.ReportLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReportLogRepository extends JpaRepository<ReportLog, UUID> {
}

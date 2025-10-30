package com.farm.controller;

import com.farm.service.ReportService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/herd-summary")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER','VIEWER')")
    public Map<String, Object> herdSummary() {
        return reportService.herdSummary();
    }

    @GetMapping("/financial-summary")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER','ACCOUNTANT','VIEWER')")
    public Map<String, Object> financialSummary() {
        return reportService.financialSummary();
    }

    @GetMapping("/health-summary")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER','VETERINARIAN','VIEWER')")
    public Map<String, Object> healthSummary() {
        return reportService.healthSummary();
    }
}

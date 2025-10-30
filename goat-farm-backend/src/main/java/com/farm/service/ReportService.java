package com.farm.service;

import java.util.Map;

public interface ReportService {
    Map<String, Object> herdSummary();
    Map<String, Object> financialSummary();
    Map<String, Object> healthSummary();
}

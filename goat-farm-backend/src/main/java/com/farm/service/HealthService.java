package com.farm.service;

import com.farm.dto.HealthRecordDto;

import java.util.List;

public interface HealthService {
    HealthRecordDto create(HealthRecordDto dto, String username);
    HealthRecordDto update(String id, HealthRecordDto dto, String username);
    HealthRecordDto get(String id);
    List<HealthRecordDto> list();
    void delete(String id);
}

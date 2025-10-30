package com.farm.service;

import com.farm.dto.SalesRecordDto;

import java.util.List;

public interface SalesService {
    SalesRecordDto create(SalesRecordDto dto, String username);
    SalesRecordDto update(String id, SalesRecordDto dto, String username);
    SalesRecordDto get(String id);
    List<SalesRecordDto> list();
    void delete(String id);
}

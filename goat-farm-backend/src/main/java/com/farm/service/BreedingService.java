package com.farm.service;

import com.farm.dto.BreedingRecordDto;

import java.util.List;

public interface BreedingService {
    BreedingRecordDto create(BreedingRecordDto dto, String username);
    BreedingRecordDto update(String id, BreedingRecordDto dto, String username);
    BreedingRecordDto get(String id);
    List<BreedingRecordDto> list();
    void delete(String id);
}

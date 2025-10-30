package com.farm.service;

import com.farm.dto.IncomeDto;

import java.util.List;

public interface IncomeService {
    IncomeDto create(IncomeDto dto, String username);
    IncomeDto update(String id, IncomeDto dto, String username);
    IncomeDto get(String id);
    List<IncomeDto> list();
    void delete(String id);
}

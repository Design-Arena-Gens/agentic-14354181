package com.farm.service;

import com.farm.dto.ExpenseDto;

import java.util.List;

public interface ExpenseService {
    ExpenseDto create(ExpenseDto dto, String username);
    ExpenseDto update(String id, ExpenseDto dto, String username);
    ExpenseDto get(String id);
    List<ExpenseDto> list();
    void delete(String id);
}

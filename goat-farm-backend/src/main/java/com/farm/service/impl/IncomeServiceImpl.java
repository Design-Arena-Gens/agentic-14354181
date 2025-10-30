package com.farm.service.impl;

import com.farm.dto.IncomeDto;
import com.farm.entity.Income;
import com.farm.exception.ResourceNotFoundException;
import com.farm.repository.IncomeRepository;
import com.farm.service.IncomeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepository;

    public IncomeServiceImpl(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    @Override
    public IncomeDto create(IncomeDto dto, String username) {
        Income income = new Income();
        mapToEntity(dto, income);
        income.setCreatedBy(username);
        income.setUpdatedBy(username);
        return toDto(incomeRepository.save(income));
    }

    @Override
    public IncomeDto update(String id, IncomeDto dto, String username) {
        Income income = getEntity(id);
        mapToEntity(dto, income);
        income.setUpdatedBy(username);
        return toDto(incomeRepository.save(income));
    }

    @Override
    @Transactional(readOnly = true)
    public IncomeDto get(String id) {
        return toDto(getEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncomeDto> list() {
        return incomeRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public void delete(String id) {
        incomeRepository.delete(getEntity(id));
    }

    private Income getEntity(String id) {
        return incomeRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Income not found"));
    }

    private void mapToEntity(IncomeDto dto, Income income) {
        income.setIncomeDate(dto.incomeDate());
        income.setSource(dto.source());
        income.setAmount(dto.amount());
        income.setDescription(dto.description());
    }

    private IncomeDto toDto(Income income) {
        return new IncomeDto(
                income.getId().toString(),
                income.getIncomeDate(),
                income.getSource(),
                income.getAmount(),
                income.getDescription()
        );
    }
}

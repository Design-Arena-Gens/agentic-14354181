package com.farm.service.impl;

import com.farm.dto.ExpenseDto;
import com.farm.entity.Expense;
import com.farm.exception.ResourceNotFoundException;
import com.farm.repository.ExpenseRepository;
import com.farm.service.ExpenseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public ExpenseDto create(ExpenseDto dto, String username) {
        Expense expense = new Expense();
        mapToEntity(dto, expense);
        expense.setCreatedBy(username);
        expense.setUpdatedBy(username);
        return toDto(expenseRepository.save(expense));
    }

    @Override
    public ExpenseDto update(String id, ExpenseDto dto, String username) {
        Expense expense = getEntity(id);
        mapToEntity(dto, expense);
        expense.setUpdatedBy(username);
        return toDto(expenseRepository.save(expense));
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseDto get(String id) {
        return toDto(getEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseDto> list() {
        return expenseRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public void delete(String id) {
        expenseRepository.delete(getEntity(id));
    }

    private Expense getEntity(String id) {
        return expenseRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
    }

    private void mapToEntity(ExpenseDto dto, Expense expense) {
        expense.setExpenseDate(dto.expenseDate());
        expense.setCategory(dto.category());
        expense.setAmount(dto.amount());
        expense.setDescription(dto.description());
        expense.setVendor(dto.vendor());
    }

    private ExpenseDto toDto(Expense expense) {
        return new ExpenseDto(
                expense.getId().toString(),
                expense.getExpenseDate(),
                expense.getCategory(),
                expense.getAmount(),
                expense.getDescription(),
                expense.getVendor()
        );
    }
}

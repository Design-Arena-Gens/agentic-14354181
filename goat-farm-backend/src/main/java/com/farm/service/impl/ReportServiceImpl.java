package com.farm.service.impl;

import com.farm.repository.ExpenseRepository;
import com.farm.repository.GoatRepository;
import com.farm.repository.HealthRecordRepository;
import com.farm.repository.IncomeRepository;
import com.farm.repository.SalesRecordRepository;
import com.farm.service.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final GoatRepository goatRepository;
    private final SalesRecordRepository salesRecordRepository;
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final HealthRecordRepository healthRecordRepository;

    public ReportServiceImpl(GoatRepository goatRepository,
                             SalesRecordRepository salesRecordRepository,
                             ExpenseRepository expenseRepository,
                             IncomeRepository incomeRepository,
                             HealthRecordRepository healthRecordRepository) {
        this.goatRepository = goatRepository;
        this.salesRecordRepository = salesRecordRepository;
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
        this.healthRecordRepository = healthRecordRepository;
    }

    @Override
    public Map<String, Object> herdSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalGoats", goatRepository.count());
        summary.put("totalDoes", goatRepository.findAll().stream().filter(goat -> goat.getGender() == com.farm.entity.Goat.Gender.DOE).count());
        summary.put("totalBucks", goatRepository.findAll().stream().filter(goat -> goat.getGender() == com.farm.entity.Goat.Gender.BUCK).count());
        summary.put("averageWeight", goatRepository.findAll().stream()
                .filter(goat -> goat.getWeightKg() != null)
                .mapToDouble(com.farm.entity.Goat::getWeightKg)
                .average().orElse(0.0));
        return summary;
    }

    @Override
    public Map<String, Object> financialSummary() {
        Map<String, Object> summary = new HashMap<>();
        double totalSales = salesRecordRepository.findAll().stream()
                .mapToDouble(record -> record.getSalePrice() != null ? record.getSalePrice() : 0.0)
                .sum();
        double totalExpense = expenseRepository.findAll().stream()
                .mapToDouble(expense -> expense.getAmount() != null ? expense.getAmount() : 0.0)
                .sum();
        double totalIncome = incomeRepository.findAll().stream()
                .mapToDouble(income -> income.getAmount() != null ? income.getAmount() : 0.0)
                .sum();
        summary.put("totalSales", BigDecimal.valueOf(totalSales));
        summary.put("totalExpenses", BigDecimal.valueOf(totalExpense));
        summary.put("totalIncome", BigDecimal.valueOf(totalIncome));
        summary.put("netProfit", BigDecimal.valueOf(totalIncome + totalSales - totalExpense));
        return summary;
    }

    @Override
    public Map<String, Object> healthSummary() {
        Map<String, Object> summary = new HashMap<>();
        LocalDate now = LocalDate.now();
        long vaccinationsThisMonth = healthRecordRepository.findByRecordDateBetween(now.withDayOfMonth(1), now)
                .stream().filter(record -> record.getRecordType() == com.farm.entity.HealthRecord.HealthRecordType.VACCINATION)
                .count();
        summary.put("vaccinationsThisMonth", vaccinationsThisMonth);
        summary.put("totalHealthRecords", healthRecordRepository.count());
        return summary;
    }
}

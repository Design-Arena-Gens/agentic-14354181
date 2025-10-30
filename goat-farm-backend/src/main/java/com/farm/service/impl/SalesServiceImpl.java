package com.farm.service.impl;

import com.farm.dto.SalesRecordDto;
import com.farm.entity.Goat;
import com.farm.entity.SalesRecord;
import com.farm.exception.ResourceNotFoundException;
import com.farm.repository.GoatRepository;
import com.farm.repository.SalesRecordRepository;
import com.farm.service.SalesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SalesServiceImpl implements SalesService {

    private final SalesRecordRepository salesRecordRepository;
    private final GoatRepository goatRepository;

    public SalesServiceImpl(SalesRecordRepository salesRecordRepository, GoatRepository goatRepository) {
        this.salesRecordRepository = salesRecordRepository;
        this.goatRepository = goatRepository;
    }

    @Override
    public SalesRecordDto create(SalesRecordDto dto, String username) {
        SalesRecord record = new SalesRecord();
        mapToEntity(dto, record);
        record.setCreatedBy(username);
        record.setUpdatedBy(username);
        return toDto(salesRecordRepository.save(record));
    }

    @Override
    public SalesRecordDto update(String id, SalesRecordDto dto, String username) {
        SalesRecord record = getEntity(id);
        mapToEntity(dto, record);
        record.setUpdatedBy(username);
        return toDto(salesRecordRepository.save(record));
    }

    @Override
    @Transactional(readOnly = true)
    public SalesRecordDto get(String id) {
        return toDto(getEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesRecordDto> list() {
        return salesRecordRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public void delete(String id) {
        salesRecordRepository.delete(getEntity(id));
    }

    private SalesRecord getEntity(String id) {
        return salesRecordRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Sales record not found"));
    }

    private void mapToEntity(SalesRecordDto dto, SalesRecord record) {
        Goat goat = goatRepository.findById(UUID.fromString(dto.goatId()))
                .orElseThrow(() -> new ResourceNotFoundException("Goat not found"));
        record.setGoat(goat);
        record.setBuyerName(dto.buyerName());
        record.setSaleDate(dto.saleDate());
        record.setSalePrice(dto.salePrice());
        record.setPaymentStatus(dto.paymentStatus());
        record.setRemarks(dto.remarks());
    }

    private SalesRecordDto toDto(SalesRecord record) {
        return new SalesRecordDto(
                record.getId().toString(),
                record.getGoat().getId().toString(),
                record.getBuyerName(),
                record.getSaleDate(),
                record.getSalePrice(),
                record.getPaymentStatus(),
                record.getRemarks()
        );
    }
}

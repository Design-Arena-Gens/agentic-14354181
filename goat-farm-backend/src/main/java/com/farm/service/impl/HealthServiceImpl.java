package com.farm.service.impl;

import com.farm.dto.HealthRecordDto;
import com.farm.entity.Goat;
import com.farm.entity.HealthRecord;
import com.farm.exception.ResourceNotFoundException;
import com.farm.repository.GoatRepository;
import com.farm.repository.HealthRecordRepository;
import com.farm.service.HealthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class HealthServiceImpl implements HealthService {

    private final HealthRecordRepository healthRecordRepository;
    private final GoatRepository goatRepository;

    public HealthServiceImpl(HealthRecordRepository healthRecordRepository, GoatRepository goatRepository) {
        this.healthRecordRepository = healthRecordRepository;
        this.goatRepository = goatRepository;
    }

    @Override
    public HealthRecordDto create(HealthRecordDto dto, String username) {
        HealthRecord record = new HealthRecord();
        mapToEntity(dto, record);
        record.setCreatedBy(username);
        record.setUpdatedBy(username);
        return toDto(healthRecordRepository.save(record));
    }

    @Override
    public HealthRecordDto update(String id, HealthRecordDto dto, String username) {
        HealthRecord record = getEntity(id);
        mapToEntity(dto, record);
        record.setUpdatedBy(username);
        return toDto(healthRecordRepository.save(record));
    }

    @Override
    @Transactional(readOnly = true)
    public HealthRecordDto get(String id) {
        return toDto(getEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HealthRecordDto> list() {
        return healthRecordRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public void delete(String id) {
        healthRecordRepository.delete(getEntity(id));
    }

    private HealthRecord getEntity(String id) {
        return healthRecordRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Health record not found"));
    }

    private void mapToEntity(HealthRecordDto dto, HealthRecord record) {
        Goat goat = goatRepository.findById(UUID.fromString(dto.goatId()))
                .orElseThrow(() -> new ResourceNotFoundException("Goat not found"));
        record.setGoat(goat);
        record.setRecordType(dto.recordType());
        record.setRecordDate(dto.recordDate());
        record.setVaccineOrMedicine(dto.vaccineOrMedicine());
        record.setDosage(dto.dosage());
        record.setVeterinarian(dto.veterinarian());
        record.setNotes(dto.notes());
    }

    private HealthRecordDto toDto(HealthRecord record) {
        return new HealthRecordDto(
                record.getId().toString(),
                record.getGoat().getId().toString(),
                record.getRecordType(),
                record.getRecordDate(),
                record.getVaccineOrMedicine(),
                record.getDosage(),
                record.getVeterinarian(),
                record.getNotes()
        );
    }
}

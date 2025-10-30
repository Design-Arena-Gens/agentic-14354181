package com.farm.service.impl;

import com.farm.dto.BreedingRecordDto;
import com.farm.entity.BreedingRecord;
import com.farm.entity.Goat;
import com.farm.exception.ResourceNotFoundException;
import com.farm.repository.BreedingRecordRepository;
import com.farm.repository.GoatRepository;
import com.farm.service.BreedingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BreedingServiceImpl implements BreedingService {

    private final BreedingRecordRepository breedingRecordRepository;
    private final GoatRepository goatRepository;

    public BreedingServiceImpl(BreedingRecordRepository breedingRecordRepository, GoatRepository goatRepository) {
        this.breedingRecordRepository = breedingRecordRepository;
        this.goatRepository = goatRepository;
    }

    @Override
    public BreedingRecordDto create(BreedingRecordDto dto, String username) {
        BreedingRecord record = new BreedingRecord();
        mapToEntity(dto, record);
        record.setCreatedBy(username);
        record.setUpdatedBy(username);
        return toDto(breedingRecordRepository.save(record));
    }

    @Override
    public BreedingRecordDto update(String id, BreedingRecordDto dto, String username) {
        BreedingRecord record = getEntity(id);
        mapToEntity(dto, record);
        record.setUpdatedBy(username);
        return toDto(breedingRecordRepository.save(record));
    }

    @Override
    @Transactional(readOnly = true)
    public BreedingRecordDto get(String id) {
        return toDto(getEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BreedingRecordDto> list() {
        return breedingRecordRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public void delete(String id) {
        breedingRecordRepository.delete(getEntity(id));
    }

    private BreedingRecord getEntity(String id) {
        return breedingRecordRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Breeding record not found"));
    }

    private void mapToEntity(BreedingRecordDto dto, BreedingRecord record) {
        Goat doe = goatRepository.findById(UUID.fromString(dto.doeId()))
                .orElseThrow(() -> new ResourceNotFoundException("Doe not found"));
        Goat buck = goatRepository.findById(UUID.fromString(dto.buckId()))
                .orElseThrow(() -> new ResourceNotFoundException("Buck not found"));
        record.setDoe(doe);
        record.setBuck(buck);
        record.setBreedingDate(dto.breedingDate());
        record.setExpectedKiddingDate(dto.expectedKiddingDate());
        record.setActualKiddingDate(dto.actualKiddingDate());
        record.setKidsBorn(dto.kidsBorn());
        record.setRemarks(dto.remarks());
    }

    private BreedingRecordDto toDto(BreedingRecord record) {
        return new BreedingRecordDto(
                record.getId().toString(),
                record.getDoe().getId().toString(),
                record.getBuck().getId().toString(),
                record.getBreedingDate(),
                record.getExpectedKiddingDate(),
                record.getActualKiddingDate(),
                record.getKidsBorn(),
                record.getRemarks()
        );
    }
}

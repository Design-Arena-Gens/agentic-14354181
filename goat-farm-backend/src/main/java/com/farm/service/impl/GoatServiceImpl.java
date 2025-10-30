package com.farm.service.impl;

import com.farm.dto.GoatDto;
import com.farm.entity.Goat;
import com.farm.exception.BadRequestException;
import com.farm.exception.ResourceNotFoundException;
import com.farm.repository.GoatRepository;
import com.farm.service.GoatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class GoatServiceImpl implements GoatService {

    private final GoatRepository goatRepository;

    public GoatServiceImpl(GoatRepository goatRepository) {
        this.goatRepository = goatRepository;
    }

    @Override
    public GoatDto create(GoatDto dto, String username) {
        goatRepository.findByTagId(dto.tagId()).ifPresent(g -> {
            throw new BadRequestException("Tag ID already exists");
        });
        Goat goat = new Goat();
        mapToEntity(dto, goat);
        goat.setCreatedBy(username);
        goat.setUpdatedBy(username);
        return toDto(goatRepository.save(goat));
    }

    @Override
    public GoatDto update(String id, GoatDto dto, String username) {
        Goat goat = getEntity(id);
        if (!goat.getTagId().equals(dto.tagId())) {
            goatRepository.findByTagId(dto.tagId()).ifPresent(existing -> {
                throw new BadRequestException("Tag ID already exists");
            });
        }
        mapToEntity(dto, goat);
        goat.setUpdatedBy(username);
        return toDto(goatRepository.save(goat));
    }

    @Override
    @Transactional(readOnly = true)
    public GoatDto get(String id) {
        return toDto(getEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoatDto> list() {
        return goatRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public void delete(String id) {
        goatRepository.delete(getEntity(id));
    }

    private Goat getEntity(String id) {
        return goatRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Goat not found"));
    }

    private GoatDto toDto(Goat goat) {
        return new GoatDto(
                goat.getId().toString(),
                goat.getTagId(),
                goat.getName(),
                goat.getGender(),
                goat.getBreed(),
                goat.getDateOfBirth(),
                goat.getWeightKg(),
                goat.getBodyConditionScore(),
                goat.getMother() != null ? goat.getMother().getId().toString() : null,
                goat.getFather() != null ? goat.getFather().getId().toString() : null,
                goat.getStatus()
        );
    }

    private void mapToEntity(GoatDto dto, Goat goat) {
        goat.setTagId(dto.tagId());
        goat.setName(dto.name());
        goat.setGender(dto.gender());
        goat.setBreed(dto.breed());
        goat.setDateOfBirth(dto.dateOfBirth());
        goat.setWeightKg(dto.weightKg());
        goat.setBodyConditionScore(dto.bodyConditionScore());
        goat.setStatus(dto.status());
        if (dto.motherId() != null) {
            goat.setMother(goatRepository.findById(UUID.fromString(dto.motherId()))
                    .orElseThrow(() -> new ResourceNotFoundException("Mother goat not found")));
        } else {
            goat.setMother(null);
        }
        if (dto.fatherId() != null) {
            goat.setFather(goatRepository.findById(UUID.fromString(dto.fatherId()))
                    .orElseThrow(() -> new ResourceNotFoundException("Father goat not found")));
        } else {
            goat.setFather(null);
        }
    }
}

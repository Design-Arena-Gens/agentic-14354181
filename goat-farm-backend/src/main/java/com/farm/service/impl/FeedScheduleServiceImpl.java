package com.farm.service.impl;

import com.farm.dto.FeedScheduleDto;
import com.farm.entity.FeedSchedule;
import com.farm.entity.Goat;
import com.farm.exception.ResourceNotFoundException;
import com.farm.repository.FeedScheduleRepository;
import com.farm.repository.GoatRepository;
import com.farm.service.FeedScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class FeedScheduleServiceImpl implements FeedScheduleService {

    private final FeedScheduleRepository feedScheduleRepository;
    private final GoatRepository goatRepository;

    public FeedScheduleServiceImpl(FeedScheduleRepository feedScheduleRepository, GoatRepository goatRepository) {
        this.feedScheduleRepository = feedScheduleRepository;
        this.goatRepository = goatRepository;
    }

    @Override
    public FeedScheduleDto create(FeedScheduleDto dto, String username) {
        FeedSchedule schedule = new FeedSchedule();
        mapToEntity(dto, schedule);
        schedule.setCreatedBy(username);
        schedule.setUpdatedBy(username);
        return toDto(feedScheduleRepository.save(schedule));
    }

    @Override
    public FeedScheduleDto update(String id, FeedScheduleDto dto, String username) {
        FeedSchedule schedule = getEntity(id);
        mapToEntity(dto, schedule);
        schedule.setUpdatedBy(username);
        return toDto(feedScheduleRepository.save(schedule));
    }

    @Override
    @Transactional(readOnly = true)
    public FeedScheduleDto get(String id) {
        return toDto(getEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedScheduleDto> list() {
        return feedScheduleRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public void delete(String id) {
        feedScheduleRepository.delete(getEntity(id));
    }

    private FeedSchedule getEntity(String id) {
        return feedScheduleRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Feed schedule not found"));
    }

    private void mapToEntity(FeedScheduleDto dto, FeedSchedule schedule) {
        Goat goat = goatRepository.findById(UUID.fromString(dto.goatId()))
                .orElseThrow(() -> new ResourceNotFoundException("Goat not found"));
        schedule.setGoat(goat);
        schedule.setFeedName(dto.feedName());
        schedule.setQuantityKg(dto.quantityKg());
        schedule.setScheduledDate(dto.scheduledDate());
        schedule.setScheduledTime(dto.scheduledTime());
        if (dto.status() != null) {
            schedule.setStatus(dto.status());
        }
    }

    private FeedScheduleDto toDto(FeedSchedule schedule) {
        return new FeedScheduleDto(
                schedule.getId().toString(),
                schedule.getGoat().getId().toString(),
                schedule.getFeedName(),
                schedule.getQuantityKg(),
                schedule.getScheduledDate(),
                schedule.getScheduledTime(),
                schedule.getStatus()
        );
    }
}

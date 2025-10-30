package com.farm.service;

import com.farm.dto.FeedScheduleDto;

import java.util.List;

public interface FeedScheduleService {
    FeedScheduleDto create(FeedScheduleDto dto, String username);
    FeedScheduleDto update(String id, FeedScheduleDto dto, String username);
    FeedScheduleDto get(String id);
    List<FeedScheduleDto> list();
    void delete(String id);
}

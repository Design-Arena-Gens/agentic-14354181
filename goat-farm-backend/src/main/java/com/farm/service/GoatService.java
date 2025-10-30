package com.farm.service;

import com.farm.dto.GoatDto;

import java.util.List;

public interface GoatService {
    GoatDto create(GoatDto dto, String username);
    GoatDto update(String id, GoatDto dto, String username);
    GoatDto get(String id);
    List<GoatDto> list();
    void delete(String id);
}

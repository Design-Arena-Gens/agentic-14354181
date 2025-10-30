package com.farm.service;

import com.farm.dto.InventoryItemDto;

import java.util.List;

public interface InventoryService {
    InventoryItemDto create(InventoryItemDto dto, String username);
    InventoryItemDto update(String id, InventoryItemDto dto, String username);
    InventoryItemDto get(String id);
    List<InventoryItemDto> list();
    void delete(String id);
}

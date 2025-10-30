package com.farm.service.impl;

import com.farm.dto.InventoryItemDto;
import com.farm.entity.InventoryItem;
import com.farm.exception.ResourceNotFoundException;
import com.farm.repository.InventoryItemRepository;
import com.farm.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryItemRepository inventoryItemRepository;

    public InventoryServiceImpl(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @Override
    public InventoryItemDto create(InventoryItemDto dto, String username) {
        InventoryItem item = new InventoryItem();
        mapToEntity(dto, item);
        item.setCreatedBy(username);
        item.setUpdatedBy(username);
        return toDto(inventoryItemRepository.save(item));
    }

    @Override
    public InventoryItemDto update(String id, InventoryItemDto dto, String username) {
        InventoryItem item = getEntity(id);
        mapToEntity(dto, item);
        item.setUpdatedBy(username);
        return toDto(inventoryItemRepository.save(item));
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryItemDto get(String id) {
        return toDto(getEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryItemDto> list() {
        return inventoryItemRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public void delete(String id) {
        inventoryItemRepository.delete(getEntity(id));
    }

    private InventoryItem getEntity(String id) {
        return inventoryItemRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found"));
    }

    private void mapToEntity(InventoryItemDto dto, InventoryItem item) {
        item.setItemName(dto.itemName());
        item.setCategory(dto.category());
        item.setQuantity(dto.quantity());
        item.setUnit(dto.unit());
        item.setPurchaseDate(dto.purchaseDate());
        item.setExpiryDate(dto.expiryDate());
        item.setUnitCost(dto.unitCost());
        item.setSupplier(dto.supplier());
    }

    private InventoryItemDto toDto(InventoryItem item) {
        return new InventoryItemDto(
                item.getId().toString(),
                item.getItemName(),
                item.getCategory(),
                item.getQuantity(),
                item.getUnit(),
                item.getPurchaseDate(),
                item.getExpiryDate(),
                item.getUnitCost(),
                item.getSupplier()
        );
    }
}

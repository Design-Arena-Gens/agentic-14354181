package com.farm.controller;

import com.farm.dto.FeedScheduleDto;
import com.farm.service.FeedScheduleService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    private final FeedScheduleService feedScheduleService;

    public FeedController(FeedScheduleService feedScheduleService) {
        this.feedScheduleService = feedScheduleService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER','WORKER')")
    public FeedScheduleDto create(@Valid @RequestBody FeedScheduleDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        return feedScheduleService.create(dto, userDetails.getUsername());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER','WORKER')")
    public FeedScheduleDto update(@PathVariable String id, @Valid @RequestBody FeedScheduleDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        return feedScheduleService.update(id, dto, userDetails.getUsername());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER','WORKER','VIEWER')")
    public List<FeedScheduleDto> list() {
        return feedScheduleService.list();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER','WORKER','VIEWER')")
    public FeedScheduleDto get(@PathVariable String id) {
        return feedScheduleService.get(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER')")
    public void delete(@PathVariable String id) {
        feedScheduleService.delete(id);
    }
}

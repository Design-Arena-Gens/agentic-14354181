package com.farm.repository;

import com.farm.entity.FeedSchedule;
import com.farm.entity.Goat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface FeedScheduleRepository extends JpaRepository<FeedSchedule, UUID> {
    List<FeedSchedule> findByGoat(Goat goat);
    List<FeedSchedule> findByScheduledDateBetween(LocalDate start, LocalDate end);
}

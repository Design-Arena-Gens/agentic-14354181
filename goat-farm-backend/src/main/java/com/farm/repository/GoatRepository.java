package com.farm.repository;

import com.farm.entity.Goat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GoatRepository extends JpaRepository<Goat, UUID> {
    Optional<Goat> findByTagId(String tagId);
}

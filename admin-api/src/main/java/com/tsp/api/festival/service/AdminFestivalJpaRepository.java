package com.tsp.api.festival.service;

import com.tsp.api.festival.domain.AdminFestivalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminFestivalJpaRepository extends JpaRepository<AdminFestivalEntity, Long> {
}

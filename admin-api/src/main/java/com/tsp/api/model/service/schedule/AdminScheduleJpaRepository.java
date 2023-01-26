package com.tsp.api.model.service.schedule;

import com.tsp.api.domain.model.schedule.AdminScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminScheduleJpaRepository extends JpaRepository<AdminScheduleEntity, Long> {
}

package com.tsp.api.model.service.schedule;

import com.tsp.api.model.domain.schedule.AdminScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminScheduleJpaRepository extends JpaRepository<AdminScheduleEntity, Long> {

    @Query("select s from AdminScheduleEntity s where s.adminModelEntity.idx = :modelIdx order by s.idx desc")
    List<AdminScheduleEntity> findAllById(@Param("modelIdx") Long modelIdx);
}

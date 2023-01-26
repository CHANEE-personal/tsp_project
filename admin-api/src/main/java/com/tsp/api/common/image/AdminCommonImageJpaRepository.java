package com.tsp.api.common.image;

import com.tsp.api.common.domain.CommonImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminCommonImageJpaRepository extends JpaRepository<CommonImageEntity, Long> {
}

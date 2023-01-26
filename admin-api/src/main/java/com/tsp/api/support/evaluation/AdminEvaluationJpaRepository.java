package com.tsp.api.support.evaluation;

import com.tsp.api.domain.support.evaluation.EvaluationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminEvaluationJpaRepository extends JpaRepository<EvaluationEntity, Long> {
}

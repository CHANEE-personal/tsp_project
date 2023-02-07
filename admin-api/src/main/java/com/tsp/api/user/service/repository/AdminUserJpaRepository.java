package com.tsp.api.user.service.repository;

import com.tsp.api.user.domain.AdminUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface AdminUserJpaRepository extends JpaRepository<AdminUserEntity, Long> {
    Optional<AdminUserEntity> findByUserId(String id);
    Optional<AdminUserEntity> findByUserToken(String token);
    Optional<AdminUserEntity> findByUserRefreshToken(String refreshToken);
}

package com.tsp.api.model.service.schedule;

import com.tsp.api.model.domain.AdminModelEntity;
import com.tsp.api.model.domain.schedule.AdminScheduleDTO;
import com.tsp.api.model.domain.schedule.AdminScheduleEntity;
import com.tsp.api.model.service.AdminModelJpaRepository;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

import static com.tsp.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class AdminScheduleJpaServiceImpl implements AdminScheduleJpaService {
    private final AdminScheduleJpaQueryRepository adminScheduleJpaQueryRepository;
    private final AdminScheduleJpaRepository adminScheduleJpaRepository;
    private final AdminModelJpaRepository adminModelJpaRepository;

    private AdminModelEntity oneModel(Long idx) {
        return adminModelJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_MODEL));
    }

    private AdminScheduleEntity oneSchedule(Long idx) {
        return adminScheduleJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_MODEL_SCHEDULE));
    }

    /**
     * <pre>
     * 1. MethodName : findScheduleList
     * 2. ClassName  : AdminScheduleJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 스케줄 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AdminScheduleDTO> findScheduleList(Map<String, Object> scheduleMap, PageRequest pageRequest) {
        return adminScheduleJpaQueryRepository.findScheduleList(scheduleMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findOneSchedule
     * 2. ClassName  : AdminScheduleJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 스케줄 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminScheduleDTO findOneSchedule(Long idx) {
        return AdminScheduleEntity.toDto(oneSchedule(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneSchedule
     * 2. ClassName  : AdminScheduleJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 이전 스케줄 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 22.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminScheduleDTO findPrevOneSchedule(Long idx) {
        return adminScheduleJpaQueryRepository.findPrevOneSchedule(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneSchedule
     * 2. ClassName  : AdminScheduleJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 다음 스케줄 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 22.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminScheduleDTO findNextOneSchedule(Long idx) {
        return adminScheduleJpaQueryRepository.findNextOneSchedule(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertSchedule
     * 2. ClassName  : AdminScheduleJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 스케줄 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    @Override
    @Transactional
    public AdminScheduleDTO insertSchedule(Long idx, AdminScheduleEntity adminScheduleEntity) {
        try {
            oneModel(idx).addSchedule(adminScheduleEntity);
            return AdminScheduleEntity.toDto(adminScheduleJpaRepository.save(adminScheduleEntity));
        } catch (Exception e) {
            throw new TspException(ERROR_MODEL_SCHEDULE);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateSchedule
     * 2. ClassName  : AdminScheduleJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 스케줄 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    @Override
    @Transactional
    public AdminScheduleDTO updateSchedule(AdminScheduleEntity adminScheduleEntity) {
        try {
            Optional<AdminScheduleEntity> oneSchedule = Optional.ofNullable(adminScheduleJpaRepository.findById(adminScheduleEntity.getIdx())
                    .orElseThrow(() -> new TspException(NOT_FOUND_MODEL_SCHEDULE)));
            oneSchedule.ifPresent(adminSchedule -> adminSchedule.update(adminScheduleEntity));
            return AdminScheduleEntity.toDto(adminScheduleEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_MODEL_SCHEDULE);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteSchedule
     * 2. ClassName  : AdminScheduleJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 스케줄 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    @Override
    @Transactional
    public Long deleteSchedule(Long idx) {
        try {
            adminScheduleJpaRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_MODEL_SCHEDULE);
        }
    }
}

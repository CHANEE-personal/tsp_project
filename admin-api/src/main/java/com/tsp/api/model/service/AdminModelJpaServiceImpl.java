package com.tsp.api.model.service;

import com.tsp.api.comment.service.AdminCommentJpaRepository;
import com.tsp.api.common.SaveImage;
import com.tsp.api.common.image.AdminCommonImageJpaRepository;
import com.tsp.api.comment.domain.AdminCommentDTO;
import com.tsp.api.comment.domain.AdminCommentEntity;
import com.tsp.api.common.domain.CommonImageDTO;
import com.tsp.api.common.domain.CommonImageEntity;
import com.tsp.api.model.domain.AdminModelDTO;
import com.tsp.api.model.domain.AdminModelEntity;
import com.tsp.api.model.domain.agency.AdminAgencyEntity;
import com.tsp.api.model.domain.recommend.AdminRecommendDTO;
import com.tsp.api.model.domain.recommend.AdminRecommendEntity;
import com.tsp.api.model.domain.schedule.AdminScheduleDTO;
import com.tsp.api.model.domain.schedule.AdminScheduleEntity;
import com.tsp.api.model.service.agency.AdminAgencyJpaRepository;
import com.tsp.api.model.service.recommend.AdminRecommendJpaRepository;
import com.tsp.api.model.service.schedule.AdminScheduleJpaRepository;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tsp.api.model.domain.AdminModelEntity.toDto;
import static com.tsp.exception.ApiExceptionType.*;


@Service
@Transactional
@RequiredArgsConstructor
public class AdminModelJpaServiceImpl implements AdminModelJpaService {
    private final AdminModelJpaQueryRepository adminModelJpaQueryRepository;
    private final AdminModelJpaRepository adminModelJpaRepository;
    private final AdminAgencyJpaRepository adminAgencyJpaRepository;
    private final AdminRecommendJpaRepository adminRecommendJpaRepository;
    private final AdminCommentJpaRepository adminCommentJpaRepository;
    private final AdminCommonImageJpaRepository adminCommonImageJpaRepository;
    private final AdminScheduleJpaRepository adminScheduleJpaRepository;
    private final SaveImage saveImage;

    private AdminModelEntity oneModel(Long idx) {
        return adminModelJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_MODEL));
    }

    private AdminAgencyEntity oneAgency(Long idx) {
        return adminAgencyJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_AGENCY));
    }

    private AdminRecommendEntity oneRecommend(Long idx) {
        return adminRecommendJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_RECOMMEND));
    }

    /**
     * <pre>
     * 1. MethodName : findModelList
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AdminModelDTO> findModelList(Map<String, Object> modelMap, PageRequest pageRequest) {
        return adminModelJpaQueryRepository.findModelList(modelMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findOneModel
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminModelDTO findOneModel(Long idx) {
        return AdminModelEntity.toDto(adminModelJpaRepository.findByIdx(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_MODEL)));
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneModel
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 이전 모델 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 12.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminModelDTO findPrevOneModel(AdminModelEntity adminModelEntity) {
        return adminModelJpaQueryRepository.findPrevOneModel(adminModelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneModel
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 다음 모델 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 12.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminModelDTO findNextOneModel(AdminModelEntity adminModelEntity) {
        return adminModelJpaQueryRepository.findNextOneModel(adminModelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : insertModel
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 07.
     * </pre>
     */
    @Override
    public AdminModelDTO insertModel(AdminModelEntity adminModelEntity) {
        try {
            return toDto(adminModelJpaRepository.save(adminModelEntity));
        } catch (Exception e) {
            throw new TspException(ERROR_MODEL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateModel
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 07.
     * </pre>
     */
    @Override
    public AdminModelDTO updateModel(Long idx, AdminModelEntity adminModelEntity) {
        try {
            Optional.ofNullable(oneModel(idx))
                    .ifPresent(adminModel -> adminModel.update(adminModelEntity));
            return toDto(adminModelJpaRepository.save(adminModelEntity));
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_MODEL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteModel
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 17.
     * </pre>
     */
    @Override
    public void deleteModel(Long idx) {
        try {
            adminModelJpaRepository.deleteById(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_MODEL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertModelImage
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 이미지 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 07.
     * </pre>
     */
    @Override
    public List<CommonImageDTO> insertModelImage(CommonImageEntity commonImageEntity, List<MultipartFile> fileName) {
        try {
            return saveImage.saveFile(fileName, commonImageEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_MODEL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteModelImage
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 이미지 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 07.
     * </pre>
     */
    @Override
    public void deleteImage(CommonImageEntity commonImageEntity) {
        try {
            adminCommonImageJpaRepository.delete(commonImageEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_IMAGE);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateModelAgency
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 소속사 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 14.
     * </pre>
     */
    @Override
    public AdminModelDTO updateModelAgency(Long agencyIdx, AdminModelEntity adminModelEntity) {
        // 기존 소속사 존재 여부 판단
        Optional.ofNullable(oneAgency(agencyIdx))
                .ifPresent(adminAgencyEntity -> adminAgencyEntity.addAgency(adminModelEntity));
        return toDto(adminModelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : insertModelAdminComment
     * 2. ClassName  : AdminCommentJpaServiceImpl.java
     * 3. Comment    : 관리자 코멘트 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @Override
    @Transactional
    public AdminCommentDTO insertModelAdminComment(Long idx, AdminCommentEntity adminCommentEntity) {
        try {
            oneModel(idx).addComment(adminCommentEntity);
            return AdminCommentEntity.toDto(adminCommentJpaRepository.save(adminCommentEntity));
        } catch (Exception e) {
            throw new TspException(ERROR_COMMENT);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findModelAdminComment
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 어드민 코멘트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 26.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public List<AdminCommentDTO> findModelAdminComment(Long idx) {
        return AdminCommentEntity.toDtoList(adminCommentJpaRepository.findByAdminModelEntityIdxAndCommentType(idx, "model"));
    }

    /**
     * <pre>
     * 1. MethodName : toggleModelNewYn
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 새로운 모델 설정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 29.
     * </pre>
     */
    @Override
    public AdminModelDTO toggleModelNewYn(Long idx) {
        try {
            AdminModelEntity oneModelEntity = oneModel(idx);
            Optional.ofNullable(oneModelEntity)
                    .ifPresent(adminModelEntity -> adminModelEntity.toggleNewYn(adminModelEntity.getNewYn()));
            return toDto(oneModelEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_MODEL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOneModelSchedule
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 스케줄 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 03.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public List<AdminScheduleDTO> findOneModelSchedule(Long idx) {
        return adminScheduleJpaRepository.findAllById(idx)
                .stream().map(AdminScheduleEntity::toDto)
                .collect(Collectors.toList());
    }

    /**
     * <pre>
     * 1. MethodName : findRecommendList
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 추천 검색어 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 05.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public List<AdminRecommendDTO> findRecommendList(Map<String, Object> recommendMap) {
        return adminRecommendJpaRepository.findAll(PageRequest.of(0, 10))
                .stream().map(AdminRecommendEntity::toDto)
                .collect(Collectors.toList());
    }

    /**
     * <pre>
     * 1. MethodName : findOneRecommend
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 추천 검색어 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 05.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminRecommendDTO findOneRecommend(Long idx) {
        AdminRecommendEntity oneRecommend = oneRecommend(idx);
        return AdminRecommendEntity.toDto(oneRecommend);
    }

    /**
     * <pre>
     * 1. MethodName : insertRecommend
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 추천 검색어 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 05.
     * </pre>
     */
    @Override
    public AdminRecommendDTO insertRecommend(AdminRecommendEntity adminRecommendEntity) {
        try {
            return AdminRecommendEntity.toDto(adminRecommendJpaRepository.save(adminRecommendEntity));
        } catch (Exception e) {
            throw new TspException(ERROR_RECOMMEND);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateRecommend
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 추천 검색어 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 05.
     * </pre>
     */
    @Override
    public AdminRecommendDTO updateRecommend(AdminRecommendEntity adminRecommendEntity) {
        try {
            Optional.ofNullable(oneRecommend(adminRecommendEntity.getIdx()))
                    .ifPresent(adminRecommend -> adminRecommend.update(adminRecommendEntity));
            return AdminRecommendEntity.toDto(adminRecommendEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_RECOMMEND);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteRecommend
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 추천 검색어 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 05.
     * </pre>
     */
    @Override
    public Long deleteRecommend(Long idx) {
        try {
            adminRecommendJpaRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_RECOMMEND);
        }
    }
}

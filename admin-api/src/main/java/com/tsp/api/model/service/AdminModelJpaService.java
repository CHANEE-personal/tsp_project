package com.tsp.api.model.service;

import com.tsp.api.domain.comment.AdminCommentDTO;
import com.tsp.api.domain.comment.AdminCommentEntity;
import com.tsp.api.domain.common.CommonImageDTO;
import com.tsp.api.domain.common.CommonImageEntity;
import com.tsp.api.domain.model.AdminModelDTO;
import com.tsp.api.domain.model.AdminModelEntity;
import com.tsp.api.domain.model.recommend.AdminRecommendDTO;
import com.tsp.api.domain.model.recommend.AdminRecommendEntity;
import com.tsp.api.domain.model.schedule.AdminScheduleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface AdminModelJpaService {

    /**
     * <pre>
     * 1. MethodName : findModelList
     * 2. ClassName  : AdminModelJpaService.java
     * 3. Comment    : 관리자 모델 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    Page<AdminModelDTO> findModelList(Map<String, Object> modelMap, PageRequest pageRequest);

    /**
     * <pre>
     * 1. MethodName : findOneModel
     * 2. ClassName  : AdminModelJpaService.java
     * 3. Comment    : 관리자 모델 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    AdminModelDTO findOneModel(Long idx);

    /**
     * <pre>
     * 1. MethodName : findPrevOneModel
     * 2. ClassName  : AdminModelJpaService.java
     * 3. Comment    : 관리자 이전 모델 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 12.
     * </pre>
     */
    AdminModelDTO findPrevOneModel(AdminModelEntity adminModelEntity);

    /**
     * <pre>
     * 1. MethodName : findNextOneModel
     * 2. ClassName  : AdminModelJpaService.java
     * 3. Comment    : 관리자 다음 모델 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 12.
     * </pre>
     */
    AdminModelDTO findNextOneModel(AdminModelEntity adminModelEntity);

    /**
     * <pre>
     * 1. MethodName : insertModel
     * 2. ClassName  : AdminModelJpaService.java
     * 3. Comment    : 관리자 모델 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 07.
     * </pre>
     */
    AdminModelDTO insertModel(AdminModelEntity adminModelEntity);

    /**
     * <pre>
     * 1. MethodName : updateModel
     * 2. ClassName  : AdminModelJpaService.java
     * 3. Comment    : 관리자 모델 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 07.
     * </pre>
     */
    AdminModelDTO updateModel(Long idx, AdminModelEntity adminModelEntity);

    /**
     * <pre>
     * 1. MethodName : deleteModel
     * 2. ClassName  : AdminModelJpaService.java
     * 3. Comment    : 관리자 모델 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 17.
     * </pre>
     */
    void deleteModel(Long idx);

    /**
     * <pre>
     * 1. MethodName : insertModelImage
     * 2. ClassName  : AdminModelJpaService.java
     * 3. Comment    : 관리자 모델 이미지 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 07.
     * </pre>
     */
    List<CommonImageDTO> insertModelImage(CommonImageEntity commonImageEntity, List<MultipartFile> fileName);

    /**
     * <pre>
     * 1. MethodName : deleteModelImage
     * 2. ClassName  : AdminModelJpaService.java
     * 3. Comment    : 관리자 모델 이미지 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 07.
     * </pre>
     */
    void deleteImage(CommonImageEntity commonImageEntity);

    /**
     * <pre>
     * 1. MethodName : updateModelAgency
     * 2. ClassName  : AdminModelJpaService.java
     * 3. Comment    : 관리자 모델 소속사 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 14.
     * </pre>
     */
    AdminModelDTO updateModelAgency(Long idx, AdminModelEntity adminModelEntity);

    /**
     * <pre>
     * 1. MethodName : insertModelAdminComment
     * 2. ClassName  : AdminModelJpaService.java
     * 3. Comment    : 관리자 모델 코멘트 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    AdminCommentDTO insertModelAdminComment(Long idx, AdminCommentEntity adminCommentEntity);

    /**
     * <pre>
     * 1. MethodName : findModelAdminComment
     * 2. ClassName  : AdminModelJpaService.java
     * 3. Comment    : 관리자 모델 어드민 코멘트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 26.
     * </pre>
     */
    List<AdminCommentDTO> findModelAdminComment(Long idx);

    /**
     * <pre>
     * 1. MethodName : toggleModelNewYn
     * 2. ClassName  : AdminModelJpaService.java
     * 3. Comment    : 관리자 새로운 모델 설정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 29.
     * </pre>
     */
    AdminModelDTO toggleModelNewYn(Long idx);

    /**
     * <pre>
     * 1. MethodName : findOneModelSchedule
     * 2. ClassName  : AdminModelJpaService.java
     * 3. Comment    : 관리자 모델 스케줄 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 03.
     * </pre>
     */
    List<AdminScheduleDTO> findOneModelSchedule(Long idx);

    /**
     * <pre>
     * 1. MethodName : findRecommendList
     * 2. ClassName  : AdminModelJpaService.java
     * 3. Comment    : 관리자 추천 검색어 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 05.
     * </pre>
     */
    List<AdminRecommendDTO> findRecommendList(Map<String, Object> recommendMap);

    /**
     * <pre>
     * 1. MethodName : findOneRecommend
     * 2. ClassName  : AdminModelJpaService.java
     * 3. Comment    : 관리자 추천 검색어 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 05.
     * </pre>
     */
    AdminRecommendDTO findOneRecommend(Long idx);

    /**
     * <pre>
     * 1. MethodName : insertRecommend
     * 2. ClassName  : AdminModelJpaService.java
     * 3. Comment    : 관리자 추천 검색어 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 05.
     * </pre>
     */
    AdminRecommendDTO insertRecommend(AdminRecommendEntity adminRecommendEntity);

    /**
     * <pre>
     * 1. MethodName : updateRecommend
     * 2. ClassName  : AdminModelJpaService.java
     * 3. Comment    : 관리자 추천 검색어 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 05.
     * </pre>
     */
    AdminRecommendDTO updateRecommend(AdminRecommendEntity adminRecommendEntity);

    /**
     * <pre>
     * 1. MethodName : deleteRecommend
     * 2. ClassName  : AdminModelJpaService.java
     * 3. Comment    : 관리자 추천 검색어 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 05.
     * </pre>
     */
    Long deleteRecommend(Long idx);
}

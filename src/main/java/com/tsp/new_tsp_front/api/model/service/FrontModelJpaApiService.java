package com.tsp.new_tsp_front.api.model.service;

import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import com.tsp.new_tsp_front.api.model.service.impl.FrontModelJpaRepository;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class FrontModelJpaApiService {
    private final FrontModelJpaRepository frontModelJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findModelCount
     * 2. ClassName  : FrontModelJpaApiService.java
     * 3. Comment    : 프론트 > 모델 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 02.
     * </pre>
     */
    @Transactional(readOnly = true)
    public int findModelCount(Map<String, Object> modelMap) {
        return frontModelJpaRepository.findModelCount(modelMap);
    }

    /**
     * <pre>
     * 1. MethodName : findModelList
     * 2. ClassName  : FrontModelJpaApiService.java
     * 3. Comment    : 프론트 > 모델 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 02.
     * </pre>
     */
    @Cacheable(value = "model", key = "#modelMap")
    @Transactional(readOnly = true)
    public List<FrontModelDTO> findModelList(Map<String, Object> modelMap) {
        return frontModelJpaRepository.findModelList(modelMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneModel
     * 2. ClassName  : FrontModelJpaApiService.java
     * 3. Comment    : 프론트 > 모델 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 09.
     * </pre>
     */
    @CachePut(value = "model", key = "#idx")
    @Transactional
    public FrontModelDTO findOneModel(Long idx) {
        return frontModelJpaRepository.findOneModel(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneModel
     * 2. ClassName  : FrontModelJpaServiceImpl.java
     * 3. Comment    : 이전 모델 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    @CachePut(value = "model", key = "#frontModelEntity.idx")
    @Transactional
    public FrontModelDTO findPrevOneModel(FrontModelEntity frontModelEntity) {
        return frontModelJpaRepository.findPrevOneModel(frontModelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneModel
     * 2. ClassName  : FrontModelJpaServiceImpl.java
     * 3. Comment    : 다음 모델 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    @CachePut(value = "model", key = "#frontModelEntity.idx")
    @Transactional
    public FrontModelDTO findNextOneModel(FrontModelEntity frontModelEntity) {
        return frontModelJpaRepository.findNextOneModel(frontModelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : findMainModelList
     * 2. ClassName  : FrontModelJpaApiService.java
     * 3. Comment    : 프론트 > 메인 모델 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 03. 27.
     * </pre>
     */
    @Cacheable("mainModel")
    @Transactional(readOnly = true)
    public List<FrontModelDTO> findMainModelList() {
        return this.frontModelJpaRepository.findMainModelList();
    }

    /**
     * <pre>
     * 1. MethodName : favoriteModel
     * 2. ClassName  : FrontModelJpaApiService.java
     * 3. Comment    : 프론트 > 메인 모델 좋아요
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 03. 27.
     * </pre>
     */
    @CachePut(value = "model", key = "#idx")
    @Transactional
    public int favoriteModel(Long idx) {
        try {
            return frontModelJpaRepository.favoriteModel(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_MODEL_LIKE, e);
        }
    }
}

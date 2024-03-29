package com.tsp.api.model.service;

import com.tsp.api.model.domain.FrontModelDTO;
import com.tsp.api.model.domain.FrontModelEntity;
import com.tsp.api.model.domain.recommend.FrontRecommendDTO;
import com.tsp.api.model.domain.recommend.FrontRecommendEntity;
import com.tsp.api.model.domain.search.FrontSearchDTO;
import com.tsp.api.model.recommend.service.FrontRecommendJpaRepository;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tsp.exception.ApiExceptionType.ERROR_MODEL_LIKE;
import static com.tsp.exception.ApiExceptionType.NOT_FOUND_MODEL;

@Service
@RequiredArgsConstructor
public class FrontModelJpaApiService {
    private final FrontModelJpaQueryRepository frontModelJpaQueryRepository;
    private final FrontModelJpaRepository frontModelJpaRepository;
    private final FrontRecommendJpaRepository frontRecommendJpaRepository;

    private FrontModelEntity oneModel(Long idx) {
        return frontModelJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_MODEL));
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
    @Transactional(readOnly = true)
    public Page<FrontModelDTO> findModelList(Map<String, Object> modelMap, PageRequest pageRequest) {
        return frontModelJpaQueryRepository.findModelList(modelMap, pageRequest);
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
    @Transactional
    public FrontModelDTO findOneModel(Long idx) {
        FrontModelEntity oneModel = frontModelJpaRepository.findByIdx(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_MODEL));

        // 조회 수 증가
        oneModel.updateViewCount();
        return FrontModelEntity.toDto(oneModel);
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
    @Transactional
    public FrontModelDTO findPrevOneModel(Long idx, Integer categoryCd) {
        return frontModelJpaQueryRepository.findPrevOneModel(idx, categoryCd);
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
    @Transactional
    public FrontModelDTO findNextOneModel(Long idx, Integer categoryCd) {
        return frontModelJpaQueryRepository.findNextOneModel(idx, categoryCd);
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
    @Transactional(readOnly = true)
    public List<FrontModelDTO> findMainModelList() {
        return this.frontModelJpaRepository.findMainModelList()
                .stream().map(FrontModelEntity::toDto)
                .collect(Collectors.toList());
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
    @Transactional
    public int favoriteModel(Long idx) {
        try {
            oneModel(idx).updateFavoriteCount();
            return oneModel(idx).getModelFavoriteCount();
        } catch (Exception e) {
            throw new TspException(ERROR_MODEL_LIKE);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findRecommendList
     * 2. ClassName  : FrontModelJpaApiService.java
     * 3. Comment    : 프론트 > 추천 검색어 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 05.
     * </pre>
     */
    @Transactional(readOnly = true)
    public List<FrontRecommendDTO> findRecommendList(PageRequest pageRequest) {
        return frontRecommendJpaRepository.findAll(pageRequest)
                .stream().map(FrontRecommendEntity::toDto)
                .collect(Collectors.toList());
    }

    /**
     * <pre>
     * 1. MethodName : rankingKeywordList
     * 2. ClassName  : FrontModelJpaService.java
     * 3. Comment    : 프론트 모델 검색어 랭킹 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 07.
     * </pre>
     */
    @Transactional(readOnly = true)
    public Page<FrontSearchDTO> rankingKeywordList(PageRequest pageRequest) {
        return frontModelJpaQueryRepository.rankingKeywordList(pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findModelKeyword
     * 2. ClassName  : FrontModelJpaRepository.java
     * 3. Comment    : 추천 검색어 or 검색어 랭킹을 통한 모델 검색
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 07.
     * </pre>
     */
    @Transactional(readOnly = true)
    public List<FrontModelDTO> findModelKeyword(String keyword) {
        return frontModelJpaQueryRepository.findModelKeyword(keyword);
    }
}

package com.tsp.new_tsp_front.api.model.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.api.agency.domain.QFrontAgencyEntity.frontAgencyEntity;
import static com.tsp.new_tsp_front.api.common.domain.QCommonImageEntity.commonImageEntity;
import static com.tsp.new_tsp_front.api.model.domain.QFrontModelEntity.frontModelEntity;
import static com.tsp.new_tsp_front.api.model.service.impl.ModelMapper.INSTANCE;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getInt;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getString;
import static java.util.Objects.requireNonNull;

@Slf4j
@RequiredArgsConstructor
@Repository
public class FrontModelJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchCategory(Map<String, Object> modelMap) {
        int categoryCd = getInt(modelMap.get("categoryCd"), 1);

        return frontModelEntity.categoryCd.eq(categoryCd);
    }

    private BooleanExpression searchModelInfo(Map<String, Object> modelMap) {
        String searchType = getString(modelMap.get("searchType"), "");
        String searchKeyword = getString(modelMap.get("searchKeyword"), "");

        if ("0".equals(searchType)) {
            return frontModelEntity.modelKorName.contains(searchKeyword)
                    .or(frontModelEntity.modelEngName.contains(searchKeyword)
                            .or(frontModelEntity.modelDescription.contains(searchKeyword)));
        } else if ("1".equals(searchType)) {
            return frontModelEntity.modelKorName.contains(searchKeyword)
                    .or(frontModelEntity.modelEngName.contains(searchKeyword));
        } else {
            return frontModelEntity.modelDescription.contains(searchKeyword);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findMainModelList
     * 2. ClassName  : FrontModelJpaRepository.java
     * 3. Comment    : 프론트 메인 모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 03. 27.
     * </pre>
     */
    public List<FrontModelDTO> findMainModelList() {
        List<FrontModelEntity> modelList = queryFactory
                .selectFrom(frontModelEntity)
                .orderBy(frontModelEntity.idx.desc())
                .leftJoin(frontModelEntity.commonImageEntityList, commonImageEntity)
                .fetchJoin()
                .where(frontModelEntity.modelMainYn.eq("Y")
                        .and(frontModelEntity.visible.eq("Y")
                                .and(commonImageEntity.typeName.eq("model")
                                        .and(commonImageEntity.imageType.eq("main"))
                                        .and(commonImageEntity.visible.eq("Y")))))
                .fetch();

        modelList.forEach(list -> modelList.get(modelList.indexOf(list)).setRnum(modelList.indexOf(list)));

        return INSTANCE.toDtoList(modelList);
    }

    /**
     * <pre>
     * 1. MethodName : findModelCount
     * 2. ClassName  : FrontModelJpaRepository.java
     * 3. Comment    : 프론트 모델 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 03. 27.
     * </pre>
     */
    public int findModelCount(Map<String, Object> modelMap) {
        return queryFactory
                .selectFrom(frontModelEntity)
                .where((searchCategory(modelMap).or(searchModelInfo(modelMap)))
                        .and(frontModelEntity.visible.eq("Y")))
                .fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findModelList
     * 2. ClassName  : FrontModelJpaRepository.java
     * 3. Comment    : 프론트 모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 02.
     * </pre>
     */
    public List<FrontModelDTO> findModelList(Map<String, Object> modelMap) {
        List<FrontModelEntity> modelList = queryFactory
                .selectFrom(frontModelEntity)
                .orderBy(frontModelEntity.idx.desc())
                .innerJoin(frontModelEntity.frontAgencyEntity, frontAgencyEntity)
                .fetchJoin()
                .leftJoin(frontModelEntity.commonImageEntityList, commonImageEntity)
                .fetchJoin()
                .where((searchCategory(modelMap).or(searchModelInfo(modelMap)))
                        .and(frontModelEntity.visible.eq("Y")))
                .offset(getInt(modelMap.get("jpaStartPage"), 0))
                .limit(getInt(modelMap.get("size"), 0))
                .distinct()
                .fetch();

        modelList.forEach(list -> modelList.get(modelList.indexOf(list))
                .setRnum(getInt(modelMap.get("startPage"), 1) * (getInt(modelMap.get("size"), 1)) - (2 - modelList.indexOf(list))));

        return INSTANCE.toDtoList(modelList);
    }

    /**
     * <pre>
     * 1. MethodName : findOneModel
     * 2. ClassName  : FrontModelJpaRepository.java
     * 3. Comment    : 프론트 모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 09.
     * </pre>
     */
    public FrontModelDTO findOneModel(Long idx) {
        // 모델 조회 수 증가
        updateModelViewCount(idx);

        //모델 상세 조회
        FrontModelEntity findOneModel = queryFactory
                .selectFrom(frontModelEntity)
                .innerJoin(frontModelEntity.frontAgencyEntity, frontAgencyEntity)
                .fetchJoin()
                .leftJoin(frontModelEntity.commonImageEntityList, commonImageEntity)
                .fetchJoin()
                .where(frontModelEntity.idx.eq(idx)
                        .and(frontModelEntity.visible.eq("Y"))
                        .and(commonImageEntity.typeName.eq("model")))
                .fetchOne();

        return INSTANCE.toDto(findOneModel);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneModel
     * 2. ClassName  : FrontModelJpaRepository.java
     * 3. Comment    : 이전 모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 17.
     * </pre>
     */
    public FrontModelDTO findPrevOneModel(FrontModelEntity existFrontModelEntity) {
        // 이전 모델 조회
        FrontModelEntity findPrevOneModel = queryFactory
                .selectFrom(frontModelEntity)
                .orderBy(frontModelEntity.idx.desc())
                .where(frontModelEntity.idx.lt(existFrontModelEntity.getIdx())
                        .and(frontModelEntity.categoryCd.eq(existFrontModelEntity.getCategoryCd()))
                        .and(frontModelEntity.visible.eq("Y")))
                .fetchFirst();

        return INSTANCE.toDto(findPrevOneModel);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneModel
     * 2. ClassName  : FrontModelJpaRepository.java
     * 3. Comment    : 다음 모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 17.
     * </pre>
     */
    public FrontModelDTO findNextOneModel(FrontModelEntity existFrontModelEntity) {
        // 다음 모델 조회
        FrontModelEntity findNextOneModel = queryFactory
                .selectFrom(frontModelEntity)
                .orderBy(frontModelEntity.idx.asc())
                .where(frontModelEntity.idx.gt(existFrontModelEntity.getIdx())
                        .and(frontModelEntity.categoryCd.eq(existFrontModelEntity.getCategoryCd()))
                        .and(frontModelEntity.visible.eq("Y")))
                .fetchFirst();

        return INSTANCE.toDto(findNextOneModel);
    }

    /**
     * <pre>
     * 1. MethodName : viewModelCount
     * 2. ClassName  : FrontModelJpaRepository.java
     * 3. Comment    : 프론트 모델 조회 수
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 09.
     * </pre>
     */
    public Integer viewModelCount(Long idx) {
        return requireNonNull(queryFactory
                .selectFrom(frontModelEntity)
                .where(frontModelEntity.idx.eq(idx)).fetchOne()).getModelViewCount();
    }

    /**
     * <pre>
     * 1. MethodName : updateModelViewCount
     * 2. ClassName  : FrontModelJpaRepository.java
     * 3. Comment    : 프론트 모델 조회 수 증가
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 09.
     * </pre>
     */
    public Integer updateModelViewCount(Long idx) {
        // 모델 조회 수 증가
        queryFactory
                .update(frontModelEntity)
                //add , minus , multiple 다 가능하다.
                .set(frontModelEntity.modelViewCount, frontModelEntity.modelViewCount.add(1))
                .where(frontModelEntity.idx.eq(idx))
                .execute();

        em.flush();
        em.clear();

        return viewModelCount(idx);
    }

    /**
     * <pre>
     * 1. MethodName : favoriteModelCount
     * 2. ClassName  : FrontModelJpaRepository.java
     * 3. Comment    : 프론트 모델 좋아요 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 09.
     * </pre>
     */
    public Integer favoriteModelCount(Long idx) {
        return requireNonNull(queryFactory
                .selectFrom(frontModelEntity)
                .where(frontModelEntity.idx.eq(idx)).fetchOne()).getModelFavoriteCount();
    }

    /**
     * <pre>
     * 1. MethodName : favoriteModel
     * 2. ClassName  : FrontModelJpaRepository.java
     * 3. Comment    : 프론트 모델 좋아요
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 09.
     * </pre>
     */
    public Integer favoriteModel(Long idx) {
        queryFactory
                .update(frontModelEntity)
                //add , minus , multiple 다 가능하다.
                .set(frontModelEntity.modelFavoriteCount, frontModelEntity.modelFavoriteCount.add(1))
                .where(frontModelEntity.idx.eq(idx))
                .execute();

        em.flush();
        em.clear();

        return favoriteModelCount(idx);
    }

    /**
     * <pre>
     * 1. MethodName : getNewModelCount
     * 2. ClassName  : FrontModelJpaRepository.java
     * 3. Comment    : 프론트 새로운 모델 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 29.
     * </pre>
     */
    public int getNewModelCount(Map<String, Object> modelMap) {
        return queryFactory
                .selectFrom(frontModelEntity)
                .where((searchCategory(modelMap).or(searchModelInfo(modelMap)))
                        .and(frontModelEntity.visible.eq("Y"))
                        .and(frontModelEntity.newYn.eq("Y")))
                .fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : getNewModelList
     * 2. ClassName  : FrontModelJpaRepository.java
     * 3. Comment    : 프론트 새로운 모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 29.
     * </pre>
     */
    public List<FrontModelDTO> getNewModelList(Map<String, Object> modelMap) {
        List<FrontModelEntity> newModelList = queryFactory
                .selectFrom(frontModelEntity)
                .orderBy(frontModelEntity.idx.desc())
                .innerJoin(frontModelEntity.frontAgencyEntity, frontAgencyEntity)
                .fetchJoin()
                .leftJoin(frontModelEntity.commonImageEntityList, commonImageEntity)
                .fetchJoin()
                .where((searchCategory(modelMap).or(searchModelInfo(modelMap)))
                        .and(frontModelEntity.visible.eq("Y"))
                        .and(frontModelEntity.newYn.eq("Y")))
                .offset(getInt(modelMap.get("jpaStartPage"), 0))
                .limit(getInt(modelMap.get("size"), 0))
                .distinct()
                .fetch();

        newModelList.forEach(list -> newModelList.get(newModelList.indexOf(list))
                .setRnum(getInt(modelMap.get("startPage"), 1) * (getInt(modelMap.get("size"), 1)) - (2 - newModelList.indexOf(list))));

        return INSTANCE.toDtoList(newModelList);
    }
}

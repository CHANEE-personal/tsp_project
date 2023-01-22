package com.tsp.api.support.service;

import com.tsp.api.support.domain.FrontSupportDTO;
import com.tsp.api.support.domain.FrontSupportEntity;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tsp.exception.ApiExceptionType.ERROR_SUPPORT;

@Service
@RequiredArgsConstructor
public class FrontSupportJpaApiService {

    private final FrontSupportJpaQueryRepository frontSupportJpaQueryRepository;
    private final FrontSupportJpaRepository frontSupportJpaRepository;

    /**
     * <pre>
     * 1. MethodName : insertSupportModel
     * 2. ClassName  : FrontSupportJpaApiService.java
     * 3. Comment    : 모델 지원하기
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 07.
     * </pre>
     */
    @Transactional
    public FrontSupportDTO insertSupportModel(FrontSupportEntity frontSupportEntity) {
        try {
            return FrontSupportEntity.toDto(frontSupportJpaRepository.save(frontSupportEntity));
        } catch (Exception e) {
            throw new TspException(ERROR_SUPPORT);
        }
    }
}

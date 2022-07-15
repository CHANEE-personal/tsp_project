package com.tsp.new_tsp_front.api.support.service;

import com.tsp.new_tsp_front.api.support.domain.FrontSupportDTO;
import com.tsp.new_tsp_front.api.support.domain.FrontSupportEntity;
import com.tsp.new_tsp_front.api.support.service.impl.FrontSupportJpaRepository;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.tsp.new_tsp_front.exception.ApiExceptionType.ERROR_SUPPORT;

@Service
@RequiredArgsConstructor
public class FrontSupportJpaApiService {
    private final FrontSupportJpaRepository frontSupportJpaRepository;

    /**
     * <pre>
     * 1. MethodName : insertSupportModel
     * 2. ClassName  : FrontSupportJpaApiService.java
     * 3. Comment    : 모델 지원하기
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     */
    public FrontSupportDTO insertSupportModel(FrontSupportEntity frontSupportEntity) throws TspException {
        try {
            return this.frontSupportJpaRepository.insertSupportModel(frontSupportEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_SUPPORT, e);
        }
    }
}

package com.tsp.api.common.service;

import com.tsp.api.common.domain.NewCodeDto;
import com.tsp.api.common.domain.NewCodeEntity;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.tsp.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class AdminCommonJpaServiceImpl implements AdminCommonJpaService {

    private final AdminCommonJpaQueryRepository adminCommonJpaQueryRepository;
    private final AdminCommonJpaRepository adminCommonJpaRepository;

    private NewCodeEntity oneCommon(Long idx) {
        return adminCommonJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_COMMON));
    }

    /**
     * <pre>
     * 1. MethodName : findCommonCodeList
     * 2. ClassName  : AdminCommonJpaServiceImpl.java
     * 3. Comment    : 관리자 공통 코드 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NewCodeDto> findCommonCodeList(Map<String, Object> commonMap, PageRequest pageRequest) {
        return adminCommonJpaQueryRepository.findCommonCodeList(commonMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findOneCommonCode
     * 2. ClassName  : AdminCommonJpaServiceImpl.java
     * 3. Comment    : 관리자 공통 코드 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public NewCodeDto findOneCommonCode(Long idx) {
        return NewCodeEntity.toDto(oneCommon(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertCommonCode
     * 2. ClassName  : AdminCommonJpaServiceImpl.java
     * 3. Comment    : 관리자 공통 코드 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Transactional
    public NewCodeDto insertCommonCode(NewCodeEntity newCodeEntity) {
        try {
            return NewCodeEntity.toDto(adminCommonJpaRepository.save(newCodeEntity));
        } catch (Exception e) {
            throw new TspException(ERROR_COMMON);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateCommonCode
     * 2. ClassName  : AdminCommonJpaServiceImpl.java
     * 3. Comment    : 관리자 공통 코드 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Transactional
    public NewCodeDto updateCommonCode(Long idx, NewCodeEntity newCodeEntity) {
        try {
            oneCommon(idx).update(newCodeEntity);
            return NewCodeEntity.toDto(newCodeEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_COMMON);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteCommonCode
     * 2. ClassName  : AdminCommonJpaServiceImpl.java
     * 3. Comment    : 관리자 공통 코드 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Transactional
    public Long deleteCommonCode(Long idx) {
        try {
            adminCommonJpaRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_COMMON);
        }
    }
}

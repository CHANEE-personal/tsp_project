package com.tsp.api.common.service;

import com.tsp.api.common.domain.NewCodeDTO;
import com.tsp.api.common.domain.NewCodeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

public interface AdminCommonJpaService {

    /**
     * <pre>
     * 1. MethodName : findCommonCodeList
     * 2. ClassName  : AdminCommonJpaService.java
     * 3. Comment    : 관리자 공통 코드 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    Page<NewCodeDTO> findCommonCodeList(Map<String, Object> commonMap, PageRequest pageRequest);

    /**
     * <pre>
     * 1. MethodName : findOneCommonCode
     * 2. ClassName  : AdminCommonJpaService.java
     * 3. Comment    : 관리자 공통 코드 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    NewCodeDTO findOneCommonCode(Long idx);

    /**
     * <pre>
     * 1. MethodName : insertCommonCode
     * 2. ClassName  : AdminCommonJpaService.java
     * 3. Comment    : 관리자 공통 코드 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    NewCodeDTO insertCommonCode(NewCodeEntity newCodeEntity);

    /**
     * <pre>
     * 1. MethodName : updateCommonCode
     * 2. ClassName  : AdminCommonJpaService.java
     * 3. Comment    : 관리자 공통 코드 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    NewCodeDTO updateCommonCode(Long idx, NewCodeEntity newCodeEntity);

    /**
     * <pre>
     * 1. MethodName : deleteCommonCode
     * 2. ClassName  : AdminModelJpaService.java
     * 3. Comment    : 관리자 공통 코드 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    Long deleteCommonCode(Long idx);
}

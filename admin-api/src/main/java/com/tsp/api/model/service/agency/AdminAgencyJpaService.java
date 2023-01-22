package com.tsp.api.model.service.agency;

import com.tsp.api.domain.common.CommonImageDTO;
import com.tsp.api.domain.common.CommonImageEntity;
import com.tsp.api.domain.model.agency.AdminAgencyDTO;
import com.tsp.api.domain.model.agency.AdminAgencyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface AdminAgencyJpaService {

    /**
     * <pre>
     * 1. MethodName : findAgencyList
     * 2. ClassName  : AdminAgencyJpaService.java
     * 3. Comment    : 관리자 소속사 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 14.
     * </pre>
     */
    Page<AdminAgencyDTO> findAgencyList(Map<String, Object> agencyMap, PageRequest pageRequest);

    /**
     * <pre>
     * 1. MethodName : findOneAgency
     * 2. ClassName  : AdminAgencyJpaService.java
     * 3. Comment    : 관리자 소속사 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 14.
     * </pre>
     */
    AdminAgencyDTO findOneAgency(Long idx);

    /**
     * <pre>
     * 1. MethodName : insertAgency
     * 2. ClassName  : AdminAgencyJpaService.java
     * 3. Comment    : 관리자 소속사 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 14.
     * </pre>
     */
    AdminAgencyDTO insertAgency(AdminAgencyEntity adminAgencyEntity);

    /**
     * <pre>
     * 1. MethodName : updateAgency
     * 2. ClassName  : AdminAgencyJpaService.java
     * 3. Comment    : 관리자 소속사 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 14.
     * </pre>
     */
    AdminAgencyDTO updateAgency(Long idx, AdminAgencyEntity adminAgencyEntity);

    /**
     * <pre>
     * 1. MethodName : deleteAgency
     * 2. ClassName  : AdminAgencyJpaService.java
     * 3. Comment    : 관리자 소속사 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 14.
     * </pre>
     */
    Long deleteAgency(Long idx);

    /**
     * <pre>
     * 1. MethodName : insertAgencyImage
     * 2. ClassName  : AdminAgencyJpaService.java
     * 3. Comment    : 관리자 소속사 이미지 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 14.
     * </pre>
     */
    List<CommonImageDTO> insertAgencyImage(CommonImageEntity commonImageEntity, List<MultipartFile> fileName);

    /**
     * <pre>
     * 1. MethodName : deleteAgencyImage
     * 2. ClassName  : AdminAgencyJpaService.java
     * 3. Comment    : 관리자 소속사 이미지 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 14.
     * </pre>
     */
    void deleteAgencyImage(CommonImageEntity commonImageEntity);
}

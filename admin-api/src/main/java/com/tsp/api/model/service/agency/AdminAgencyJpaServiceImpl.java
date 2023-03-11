package com.tsp.api.model.service.agency;

import com.tsp.api.common.SaveImage;
import com.tsp.api.common.image.AdminCommonImageJpaRepository;
import com.tsp.api.common.domain.CommonImageDto;
import com.tsp.api.common.domain.CommonImageEntity;
import com.tsp.api.model.domain.agency.AdminAgencyDto;
import com.tsp.api.model.domain.agency.AdminAgencyEntity;
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

import static com.tsp.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class AdminAgencyJpaServiceImpl implements AdminAgencyJpaService {
    private final AdminAgencyJpaQueryRepository adminAgencyJpaQueryRepository;
    private final AdminAgencyJpaRepository adminAgencyJpaRepository;
    private final SaveImage saveImage;
    private final AdminCommonImageJpaRepository adminCommonImageJpaRepository;

    private AdminAgencyEntity oneAgency(Long idx) {
        return adminAgencyJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_AGENCY));
    }

    /**
     * <pre>
     * 1. MethodName : findAgencyList
     * 2. ClassName  : AdminAgencyJpaServiceImpl.java
     * 3. Comment    : 관리자 소속사 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 14.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AdminAgencyDto> findAgencyList(Map<String, Object> agencyMap, PageRequest pageRequest) {
        return adminAgencyJpaQueryRepository.findAgencyList(agencyMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findOneAgency
     * 2. ClassName  : AdminAgencyJpaServiceImpl.java
     * 3. Comment    : 관리자 소속사 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 14.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminAgencyDto findOneAgency(Long idx) {
        return adminAgencyJpaQueryRepository.findOneAgency(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertAgency
     * 2. ClassName  : AdminAgencyJpaServiceImpl.java
     * 3. Comment    : 관리자 소속사 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 14.
     * </pre>
     */
    @Override
    @Transactional
    public AdminAgencyDto insertAgency(AdminAgencyEntity adminAgencyEntity) {
        try {
            return AdminAgencyEntity.toDto(adminAgencyJpaRepository.save(adminAgencyEntity));
        } catch (Exception e) {
            throw new TspException(ERROR_AGENCY);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateAgency
     * 2. ClassName  : AdminAgencyJpaServiceImpl.java
     * 3. Comment    : 관리자 소속사 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 14.
     * </pre>
     */
    @Override
    @Transactional
    public AdminAgencyDto updateAgency(Long idx, AdminAgencyEntity adminAgencyEntity) {
        try {
            Optional.ofNullable(oneAgency(idx))
                    .ifPresent(adminAgency -> adminAgency.update(adminAgencyEntity));
            return AdminAgencyEntity.toDto(adminAgencyEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_AGENCY);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteAgency
     * 2. ClassName  : AdminAgencyJpaServiceImpl.java
     * 3. Comment    : 관리자 소속사 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    @Override
    @Transactional
    public Long deleteAgency(Long idx) {
        try {
            adminAgencyJpaRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_AGENCY);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertAgencyImage
     * 2. ClassName  : AdminAgencyJpaServiceImpl.java
     * 3. Comment    : 관리자 소속사 이미지 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    @Override
    public List<CommonImageDto> insertAgencyImage(CommonImageEntity commonImageEntity, List<MultipartFile> fileName) {
        try {
            return saveImage.saveFile(fileName, commonImageEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_AGENCY);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteAgencyImage
     * 2. ClassName  : AdminAgencyJpaServiceImpl.java
     * 3. Comment    : 관리자 소속사 이미지 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    @Override
    public void deleteAgencyImage(CommonImageEntity commonImageEntity) {
        try {
            adminCommonImageJpaRepository.delete(commonImageEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_IMAGE);
        }
    }
}

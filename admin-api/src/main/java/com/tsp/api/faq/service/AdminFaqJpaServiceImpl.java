package com.tsp.api.faq.service;

import com.tsp.api.domain.faq.AdminFaqDTO;
import com.tsp.api.domain.faq.AdminFaqEntity;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

import static com.tsp.exception.ApiExceptionType.*;


@Service
@RequiredArgsConstructor
public class AdminFaqJpaServiceImpl implements AdminFaqJpaService {
    private final AdminFaqJpaQueryRepository adminFaqJpaQueryRepository;
    private final AdminFaqJpaRepository adminFaqJpaRepository;

    private AdminFaqEntity oneFaq(Long idx) {
        return adminFaqJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_FAQ));
    }

    /**
     * <pre>
     * 1. MethodName : findFaqList
     * 2. ClassName  : AdminFaqJpaServiceImpl.java
     * 3. Comment    : 관리자 FAQ 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 22.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AdminFaqDTO> findFaqList(Map<String, Object> faqMap, PageRequest pageRequest) {
        return adminFaqJpaQueryRepository.findFaqList(faqMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findOneFaq
     * 2. ClassName  : AdminFaqJpaServiceImpl.java
     * 3. Comment    : 관리자 FAQ 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 22.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminFaqDTO findOneFaq(Long idx) {
        return AdminFaqEntity.toDto(oneFaq(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneFaq
     * 2. ClassName  : AdminFaqJpaServiceImpl.java
     * 3. Comment    : 관리자 이전 FAQ 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 18.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminFaqDTO findPrevOneFaq(Long idx) {
        return adminFaqJpaQueryRepository.findPrevOneFaq(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneFaq
     * 2. ClassName  : AdminFaqJpaServiceImpl.java
     * 3. Comment    : 관리자 다음 FAQ 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 18.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminFaqDTO findNextOneFaq(Long idx) {
        return adminFaqJpaQueryRepository.findNextOneFaq(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertFaq
     * 2. ClassName  : AdminFaqServiceImpl.java
     * 3. Comment    : 관리자 FAQ 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 22.
     * </pre>
     */
    @Override
    @Transactional
    public AdminFaqDTO insertFaq(AdminFaqEntity adminFaqEntity) {
        try {
            return AdminFaqEntity.toDto(adminFaqJpaRepository.save(adminFaqEntity));
        } catch (Exception e) {
            throw new TspException(ERROR_FAQ);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateFaq
     * 2. ClassName  : AdminFaqServiceImpl.java
     * 3. Comment    : 관리자 FAQ 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 22.
     * </pre>
     */
    @Override
    @Transactional
    public AdminFaqDTO updateFaq(Long idx, AdminFaqEntity adminFaqEntity) {
        try {
            Optional.ofNullable(oneFaq(idx))
                    .ifPresent(adminFaq -> adminFaq.update(adminFaqEntity));
            return AdminFaqEntity.toDto(adminFaqEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_FAQ);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteFaq
     * 2. ClassName  : AdminFaqServiceImpl.java
     * 3. Comment    : 관리자 FAQ 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 22.
     * </pre>
     */
    @Override
    @Transactional
    public Long deleteFaq(Long idx) {
        try {
            adminFaqJpaRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_FAQ);
        }
    }
}

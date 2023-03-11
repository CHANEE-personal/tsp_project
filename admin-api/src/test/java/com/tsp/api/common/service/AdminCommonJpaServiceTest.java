package com.tsp.api.common.service;

import com.tsp.api.common.domain.NewCodeDto;
import com.tsp.api.common.domain.NewCodeEntity;
import com.tsp.api.model.service.AdminModelCommonServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("공통코드 Service Test")
class AdminCommonJpaServiceTest extends AdminModelCommonServiceTest {
    @Mock private AdminCommonJpaRepository adminCommonJpaRepository;
    @Mock private AdminCommonJpaQueryRepository adminCommonJpaQueryRepository;
    @InjectMocks private AdminCommonJpaServiceImpl mockAdminCommonJpaService;
    private final AdminCommonJpaService adminCommonJpaService;

    @Test
    @DisplayName("공통코드 리스트 조회 테스트")
    void 공통코드리스트조회테스트() {
        // given
        Map<String, Object> commonMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 100);

        // then
        assertThat(adminCommonJpaService.findCommonCodeList(commonMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("공통코드 리스트 조회 Mockito 테스트")
    void 공통코드리스트조회Mockito테스트() {
        Map<String, Object> commonMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 100);

        List<NewCodeDto> commonCodeList = new ArrayList<>();
        commonCodeList.add(NewCodeDto.builder().idx(1L).categoryCd(1).categoryNm("men").cmmType("model").visible("Y").build());
        Page<NewCodeDto> resultCommon = new PageImpl<>(commonCodeList, pageRequest, commonCodeList.size());
        // when
        when(adminCommonJpaQueryRepository.findCommonCodeList(commonMap, pageRequest)).thenReturn(resultCommon);
        Page<NewCodeDto> newCommonCodeList = mockAdminCommonJpaService.findCommonCodeList(commonMap, pageRequest);
        List<NewCodeDto> findCommonCodeList = newCommonCodeList.stream().collect(Collectors.toList());

        // then
        assertThat(findCommonCodeList.get(0).getIdx()).isEqualTo(commonCodeList.get(0).getIdx());
        assertThat(findCommonCodeList.get(0).getCategoryCd()).isEqualTo(commonCodeList.get(0).getCategoryCd());
        assertThat(findCommonCodeList.get(0).getCategoryNm()).isEqualTo(commonCodeList.get(0).getCategoryNm());
        assertThat(findCommonCodeList.get(0).getCmmType()).isEqualTo(commonCodeList.get(0).getCmmType());

        // verify
        verify(adminCommonJpaQueryRepository, times(1)).findCommonCodeList(commonMap, pageRequest);
        verify(adminCommonJpaQueryRepository, atLeastOnce()).findCommonCodeList(commonMap, pageRequest);
        verifyNoMoreInteractions(adminCommonJpaQueryRepository);

        InOrder inOrder = inOrder(adminCommonJpaQueryRepository);
        inOrder.verify(adminCommonJpaQueryRepository).findCommonCodeList(commonMap, pageRequest);
    }

    @Test
    @DisplayName("공통코드 리스트 조회 BDD 테스트")
    void 공통코드리스트조회BDD테스트() {
        Map<String, Object> commonMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 100);

        List<NewCodeDto> commonCodeList = new ArrayList<>();
        commonCodeList.add(NewCodeDto.builder().idx(1L).categoryCd(1).categoryNm("men").cmmType("model").visible("Y").build());
        Page<NewCodeDto> resultCommon = new PageImpl<>(commonCodeList, pageRequest, commonCodeList.size());
        // when
        when(adminCommonJpaQueryRepository.findCommonCodeList(commonMap, pageRequest)).thenReturn(resultCommon);
        Page<NewCodeDto> newCommonCodeList = mockAdminCommonJpaService.findCommonCodeList(commonMap, pageRequest);
        List<NewCodeDto> findCommonCodeList = newCommonCodeList.stream().collect(Collectors.toList());

        // then
        assertThat(findCommonCodeList.get(0).getIdx()).isEqualTo(commonCodeList.get(0).getIdx());
        assertThat(findCommonCodeList.get(0).getCategoryCd()).isEqualTo(commonCodeList.get(0).getCategoryCd());
        assertThat(findCommonCodeList.get(0).getCategoryNm()).isEqualTo(commonCodeList.get(0).getCategoryNm());
        assertThat(findCommonCodeList.get(0).getCmmType()).isEqualTo(commonCodeList.get(0).getCmmType());

        // verify
        then(adminCommonJpaQueryRepository).should(times(1)).findCommonCodeList(commonMap, pageRequest);
        then(adminCommonJpaQueryRepository).should(atLeastOnce()).findCommonCodeList(commonMap, pageRequest);
        then(adminCommonJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공통코드 상세 조회 Mockito 테스트")
    void 공통코드상세조회Mockito테스트() {
        // when
        when(adminCommonJpaRepository.findById(newCodeEntity.getIdx())).thenReturn(Optional.ofNullable(newCodeEntity));
        NewCodeDto commonCodeInfo = mockAdminCommonJpaService.findOneCommonCode(newCodeEntity.getIdx());

        // then
        assertThat(commonCodeInfo.getIdx()).isEqualTo(newCodeEntity.getIdx());
        assertThat(commonCodeInfo.getCategoryCd()).isEqualTo(newCodeEntity.getCategoryCd());
        assertThat(commonCodeInfo.getCategoryNm()).isEqualTo(newCodeEntity.getCategoryNm());
        assertThat(commonCodeInfo.getCmmType()).isEqualTo(newCodeEntity.getCmmType());

        // verify
        verify(adminCommonJpaRepository, times(1)).findById(newCodeEntity.getIdx());
        verify(adminCommonJpaRepository, atLeastOnce()).findById(newCodeEntity.getIdx());
        verifyNoMoreInteractions(adminCommonJpaRepository);

        InOrder inOrder = inOrder(adminCommonJpaRepository);
        inOrder.verify(adminCommonJpaRepository).findById(newCodeEntity.getIdx());
    }

    @Test
    @DisplayName("공통코드 상세 조회 BDD 테스트")
    void 공통코드상세조회BDD테스트() {
        // when
        given(adminCommonJpaRepository.findById(newCodeEntity.getIdx())).willReturn(Optional.ofNullable(newCodeEntity));
        NewCodeDto commonCodeInfo = mockAdminCommonJpaService.findOneCommonCode(newCodeEntity.getIdx());

        // then
        assertThat(commonCodeInfo.getIdx()).isEqualTo(newCodeEntity.getIdx());
        assertThat(commonCodeInfo.getCategoryCd()).isEqualTo(newCodeEntity.getCategoryCd());
        assertThat(commonCodeInfo.getCategoryNm()).isEqualTo(newCodeEntity.getCategoryNm());
        assertThat(commonCodeInfo.getCmmType()).isEqualTo(newCodeEntity.getCmmType());

        // verify
        then(adminCommonJpaRepository).should(times(1)).findById(newCodeEntity.getIdx());
        then(adminCommonJpaRepository).should(atLeastOnce()).findById(newCodeEntity.getIdx());
        then(adminCommonJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공통코드 등록 Mockito 테스트")
    void 공통코드등록Mockito테스트() {
        // when
        when(adminCommonJpaRepository.save(newCodeEntity)).thenReturn(newCodeEntity);
        NewCodeDto commonInfo = mockAdminCommonJpaService.insertCommonCode(newCodeEntity);

        // then
        assertThat(commonInfo.getCategoryCd()).isEqualTo(newCodeEntity.getCategoryCd());
        assertThat(commonInfo.getCategoryNm()).isEqualTo(newCodeEntity.getCategoryNm());
        assertThat(commonInfo.getCmmType()).isEqualTo(newCodeEntity.getCmmType());

        // verify
        verify(adminCommonJpaRepository, times(1)).save(newCodeEntity);
        verify(adminCommonJpaRepository, atLeastOnce()).save(newCodeEntity);
        verifyNoMoreInteractions(adminCommonJpaRepository);

        InOrder inOrder = inOrder(adminCommonJpaRepository);
        inOrder.verify(adminCommonJpaRepository).save(newCodeEntity);
    }

    @Test
    @DisplayName("공통코드 등록 BDD 테스트")
    void 공통코드등록BDD테스트() {
        // when
        given(adminCommonJpaRepository.save(newCodeEntity)).willReturn(newCodeEntity);
        NewCodeDto commonInfo = mockAdminCommonJpaService.insertCommonCode(newCodeEntity);

        // then
        assertThat(commonInfo.getCategoryCd()).isEqualTo(newCodeEntity.getCategoryCd());
        assertThat(commonInfo.getCategoryNm()).isEqualTo(newCodeEntity.getCategoryNm());
        assertThat(commonInfo.getCmmType()).isEqualTo(newCodeEntity.getCmmType());

        // verify
        then(adminCommonJpaRepository).should(times(1)).save(newCodeEntity);
        then(adminCommonJpaRepository).should(atLeastOnce()).save(newCodeEntity);
        then(adminCommonJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공통코드 수정 Mockito 테스트")
    void 공통코드수정Mockito테스트() {
        // given
        NewCodeEntity updateCodeEntity = NewCodeEntity.builder()
                .idx(newCodeEntity.getIdx())
                .categoryCd(2)
                .categoryNm("new men")
                .cmmType("model")
                .visible("Y")
                .build();

        // when
        when(adminCommonJpaRepository.findById(updateCodeEntity.getIdx())).thenReturn(Optional.of(updateCodeEntity));
        when(adminCommonJpaRepository.save(updateCodeEntity)).thenReturn(updateCodeEntity);
        NewCodeDto commonCodeInfo = mockAdminCommonJpaService.updateCommonCode(updateCodeEntity.getIdx(), updateCodeEntity);

        // then
        assertThat(commonCodeInfo.getCategoryNm()).isEqualTo(updateCodeEntity.getCategoryNm());
        assertThat(commonCodeInfo.getCmmType()).isEqualTo(updateCodeEntity.getCmmType());

        // verify
        verify(adminCommonJpaRepository, times(1)).findById(updateCodeEntity.getIdx());
        verify(adminCommonJpaRepository, atLeastOnce()).findById(updateCodeEntity.getIdx());
        verifyNoMoreInteractions(adminCommonJpaRepository);

        InOrder inOrder = inOrder(adminCommonJpaRepository);
        inOrder.verify(adminCommonJpaRepository).findById(updateCodeEntity.getIdx());
    }

    @Test
    @DisplayName("공통코드 수정 BDD 테스트")
    void 공통코드수정BDD테스트() {
        // given
        NewCodeEntity updateCodeEntity = NewCodeEntity.builder()
                .idx(newCodeEntity.getIdx())
                .categoryCd(2)
                .categoryNm("new men")
                .cmmType("model")
                .visible("Y")
                .build();

        // when
        given(adminCommonJpaRepository.findById(updateCodeEntity.getIdx())).willReturn(Optional.of(updateCodeEntity));
        given(adminCommonJpaRepository.save(updateCodeEntity)).willReturn(updateCodeEntity);
        NewCodeDto commonCodeInfo = mockAdminCommonJpaService.updateCommonCode(updateCodeEntity.getIdx(), updateCodeEntity);

        // then
        assertThat(commonCodeInfo.getCategoryNm()).isEqualTo(updateCodeEntity.getCategoryNm());
        assertThat(commonCodeInfo.getCmmType()).isEqualTo(updateCodeEntity.getCmmType());

        // verify
        then(adminCommonJpaRepository).should(times(1)).findById(updateCodeEntity.getIdx());
        then(adminCommonJpaRepository).should(atLeastOnce()).findById(updateCodeEntity.getIdx());
        then(adminCommonJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공통코드 삭제 테스트")
    void 공통코드삭제테스트() {
        // then
        assertThat(adminCommonJpaService.deleteCommonCode(newCodeDTO.getIdx())).isNotNull();
    }
}

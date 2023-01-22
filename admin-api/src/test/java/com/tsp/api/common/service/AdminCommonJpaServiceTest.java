package com.tsp.api.common.service;

import com.tsp.api.domain.common.NewCodeDTO;
import com.tsp.api.domain.common.NewCodeEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
class AdminCommonJpaServiceTest {
    @Mock private AdminCommonJpaService mockAdminCommonJpaService;
    private final AdminCommonJpaService adminCommonJpaService;
    private NewCodeEntity commonCodeEntity;
    private NewCodeDTO commonCodeDTO;

    public void createCommonCode() {

        // 공통코드 생성
        commonCodeEntity = NewCodeEntity.builder()
                .categoryCd(1)
                .categoryNm("남성")
                .cmmType("model")
                .visible("Y")
                .build();

        commonCodeDTO = NewCodeEntity.toDto(commonCodeEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createCommonCode();
    }

    @Test
    @DisplayName("공통코드 리스트 조회 테스트")
    void 공통코드리스트조회테스트() {
        // given
        Map<String, Object> commonMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 100);

        // then
        assertThat(adminCommonJpaService.findCommonCodeList(commonMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("공통코드 리스트 조회 Mockito 테스트")
    void 공통코드리스트조회Mockito테스트() {
        Map<String, Object> commonMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 100);

        List<NewCodeDTO> commonCodeList = new ArrayList<>();
        commonCodeList.add(NewCodeDTO.builder().idx(1L).categoryCd(1).categoryNm("men").cmmType("model").visible("Y").build());
        Page<NewCodeDTO> resultCommon = new PageImpl<>(commonCodeList, pageRequest, commonCodeList.size());
        // when
        when(mockAdminCommonJpaService.findCommonCodeList(commonMap, pageRequest)).thenReturn(resultCommon);
        Page<NewCodeDTO> newCommonCodeList = mockAdminCommonJpaService.findCommonCodeList(commonMap, pageRequest);
        List<NewCodeDTO> findCommonCodeList = newCommonCodeList.stream().collect(Collectors.toList());

        // then
        assertThat(findCommonCodeList.get(0).getIdx()).isEqualTo(commonCodeList.get(0).getIdx());
        assertThat(findCommonCodeList.get(0).getCategoryCd()).isEqualTo(commonCodeList.get(0).getCategoryCd());
        assertThat(findCommonCodeList.get(0).getCategoryNm()).isEqualTo(commonCodeList.get(0).getCategoryNm());
        assertThat(findCommonCodeList.get(0).getCmmType()).isEqualTo(commonCodeList.get(0).getCmmType());

        // verify
        verify(mockAdminCommonJpaService, times(1)).findCommonCodeList(commonMap, pageRequest);
        verify(mockAdminCommonJpaService, atLeastOnce()).findCommonCodeList(commonMap, pageRequest);
        verifyNoMoreInteractions(mockAdminCommonJpaService);

        InOrder inOrder = inOrder(mockAdminCommonJpaService);
        inOrder.verify(mockAdminCommonJpaService).findCommonCodeList(commonMap, pageRequest);
    }

    @Test
    @DisplayName("공통코드 리스트 조회 BDD 테스트")
    void 공통코드리스트조회BDD테스트() {
        Map<String, Object> commonMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 100);

        List<NewCodeDTO> commonCodeList = new ArrayList<>();
        commonCodeList.add(NewCodeDTO.builder().idx(1L).categoryCd(1).categoryNm("men").cmmType("model").visible("Y").build());
        Page<NewCodeDTO> resultCommon = new PageImpl<>(commonCodeList, pageRequest, commonCodeList.size());
        // when
        when(mockAdminCommonJpaService.findCommonCodeList(commonMap, pageRequest)).thenReturn(resultCommon);
        Page<NewCodeDTO> newCommonCodeList = mockAdminCommonJpaService.findCommonCodeList(commonMap, pageRequest);
        List<NewCodeDTO> findCommonCodeList = newCommonCodeList.stream().collect(Collectors.toList());

        // then
        assertThat(findCommonCodeList.get(0).getIdx()).isEqualTo(commonCodeList.get(0).getIdx());
        assertThat(findCommonCodeList.get(0).getCategoryCd()).isEqualTo(commonCodeList.get(0).getCategoryCd());
        assertThat(findCommonCodeList.get(0).getCategoryNm()).isEqualTo(commonCodeList.get(0).getCategoryNm());
        assertThat(findCommonCodeList.get(0).getCmmType()).isEqualTo(commonCodeList.get(0).getCmmType());

        // verify
        then(mockAdminCommonJpaService).should(times(1)).findCommonCodeList(commonMap, pageRequest);
        then(mockAdminCommonJpaService).should(atLeastOnce()).findCommonCodeList(commonMap, pageRequest);
        then(mockAdminCommonJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공통코드 상세 조회 Mockito 테스트")
    void 공통코드상세조회Mockito테스트() {
        // given
        commonCodeEntity = NewCodeEntity.builder().idx(1L).categoryCd(1).categoryNm("men").cmmType("model").visible("Y").build();

        commonCodeDTO = NewCodeDTO.builder()
                .idx(1L)
                .categoryCd(1)
                .categoryNm("men")
                .cmmType("model")
                .visible("Y")
                .build();

        // when
        when(mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity.getIdx())).thenReturn(commonCodeDTO);
        NewCodeDTO commonCodeInfo = mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity.getIdx());

        // then
        assertThat(commonCodeInfo.getIdx()).isEqualTo(1);
        assertThat(commonCodeInfo.getCategoryCd()).isEqualTo(1);
        assertThat(commonCodeInfo.getCategoryNm()).isEqualTo("men");
        assertThat(commonCodeInfo.getCmmType()).isEqualTo("model");

        // verify
        verify(mockAdminCommonJpaService, times(1)).findOneCommonCode(commonCodeEntity.getIdx());
        verify(mockAdminCommonJpaService, atLeastOnce()).findOneCommonCode(commonCodeEntity.getIdx());
        verifyNoMoreInteractions(mockAdminCommonJpaService);

        InOrder inOrder = inOrder(mockAdminCommonJpaService);
        inOrder.verify(mockAdminCommonJpaService).findOneCommonCode(commonCodeEntity.getIdx());
    }

    @Test
    @DisplayName("공통코드 상세 조회 BDD 테스트")
    void 공통코드상세조회BDD테스트() {
        // given
        commonCodeEntity = NewCodeEntity.builder().idx(1L).categoryCd(1).categoryNm("men").cmmType("model").visible("Y").build();

        commonCodeDTO = NewCodeDTO.builder()
                .idx(1L)
                .categoryCd(1)
                .categoryNm("men")
                .cmmType("model")
                .visible("Y")
                .build();

        // when
        when(mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity.getIdx())).thenReturn(commonCodeDTO);
        NewCodeDTO commonCodeInfo = mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity.getIdx());

        // then
        assertThat(commonCodeInfo.getIdx()).isEqualTo(1);
        assertThat(commonCodeInfo.getCategoryCd()).isEqualTo(1);
        assertThat(commonCodeInfo.getCategoryNm()).isEqualTo("men");
        assertThat(commonCodeInfo.getCmmType()).isEqualTo("model");

        // verify
        then(mockAdminCommonJpaService).should(times(1)).findOneCommonCode(commonCodeEntity.getIdx());
        then(mockAdminCommonJpaService).should(atLeastOnce()).findOneCommonCode(commonCodeEntity.getIdx());
        then(mockAdminCommonJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공통코드 등록 Mockito 테스트")
    void 공통코드등록Mockito테스트() {
        // given
        adminCommonJpaService.insertCommonCode(commonCodeEntity);

        // when
        when(mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity.getIdx())).thenReturn(commonCodeDTO);
        NewCodeDTO commonInfo = mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity.getIdx());

        // then
        assertThat(commonInfo.getCategoryCd()).isEqualTo(commonCodeDTO.getCategoryCd());
        assertThat(commonInfo.getCategoryNm()).isEqualTo(commonCodeDTO.getCategoryNm());
        assertThat(commonInfo.getCmmType()).isEqualTo(commonCodeDTO.getCmmType());

        // verify
        verify(mockAdminCommonJpaService, times(1)).findOneCommonCode(commonCodeEntity.getIdx());
        verify(mockAdminCommonJpaService, atLeastOnce()).findOneCommonCode(commonCodeEntity.getIdx());
        verifyNoMoreInteractions(mockAdminCommonJpaService);

        InOrder inOrder = inOrder(mockAdminCommonJpaService);
        inOrder.verify(mockAdminCommonJpaService).findOneCommonCode(commonCodeEntity.getIdx());
    }

    @Test
    @DisplayName("공통코드 등록 BDD 테스트")
    void 공통코드등록BDD테스트() {
        // given
        adminCommonJpaService.insertCommonCode(commonCodeEntity);

        // when
        given(mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity.getIdx())).willReturn(commonCodeDTO);
        NewCodeDTO commonInfo = mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity.getIdx());

        // then
        assertThat(commonInfo.getCategoryCd()).isEqualTo(commonCodeDTO.getCategoryCd());
        assertThat(commonInfo.getCategoryNm()).isEqualTo(commonCodeDTO.getCategoryNm());
        assertThat(commonInfo.getCmmType()).isEqualTo(commonCodeDTO.getCmmType());

        // verify
        then(mockAdminCommonJpaService).should(times(1)).findOneCommonCode(commonCodeEntity.getIdx());
        then(mockAdminCommonJpaService).should(atLeastOnce()).findOneCommonCode(commonCodeEntity.getIdx());
        then(mockAdminCommonJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공통코드 수정 Mockito 테스트")
    void 공통코드수정Mockito테스트() {
        // given
        Long idx = adminCommonJpaService.insertCommonCode(commonCodeEntity).getIdx();

        commonCodeEntity = NewCodeEntity.builder()
                .idx(idx)
                .categoryCd(2)
                .categoryNm("new men")
                .cmmType("model")
                .visible("Y")
                .build();

        adminCommonJpaService.updateCommonCode(idx, commonCodeEntity);

        commonCodeDTO = NewCodeEntity.toDto(commonCodeEntity);

        // when
        when(mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity.getIdx())).thenReturn(commonCodeDTO);
        NewCodeDTO commonCodeInfo = mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity.getIdx());

        // then
        assertThat(commonCodeInfo.getCategoryNm()).isEqualTo("new men");
        assertThat(commonCodeInfo.getCmmType()).isEqualTo("model");

        // verify
        verify(mockAdminCommonJpaService, times(1)).findOneCommonCode(commonCodeEntity.getIdx());
        verify(mockAdminCommonJpaService, atLeastOnce()).findOneCommonCode(commonCodeEntity.getIdx());
        verifyNoMoreInteractions(mockAdminCommonJpaService);

        InOrder inOrder = inOrder(mockAdminCommonJpaService);
        inOrder.verify(mockAdminCommonJpaService).findOneCommonCode(commonCodeEntity.getIdx());
    }

    @Test
    @DisplayName("공통코드 수정 BDD 테스트")
    void 공통코드수정BDD테스트() {
        // given
        Long idx = adminCommonJpaService.insertCommonCode(commonCodeEntity).getIdx();

        commonCodeEntity = NewCodeEntity.builder()
                .idx(idx)
                .categoryCd(2)
                .categoryNm("new men")
                .cmmType("model")
                .visible("Y")
                .build();

        adminCommonJpaService.updateCommonCode(idx, commonCodeEntity);

        commonCodeDTO = NewCodeEntity.toDto(commonCodeEntity);

        // when
        given(mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity.getIdx())).willReturn(commonCodeDTO);
        NewCodeDTO commonCodeInfo = mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity.getIdx());

        // then
        assertThat(commonCodeInfo.getCategoryNm()).isEqualTo("new men");
        assertThat(commonCodeInfo.getCmmType()).isEqualTo("model");

        // verify
        then(mockAdminCommonJpaService).should(times(1)).findOneCommonCode(commonCodeEntity.getIdx());
        then(mockAdminCommonJpaService).should(atLeastOnce()).findOneCommonCode(commonCodeEntity.getIdx());
        then(mockAdminCommonJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공통코드 삭제 테스트")
    void 공통코드삭제테스트() {
        // given
        Long idx = adminCommonJpaService.insertCommonCode(commonCodeEntity).getIdx();

        // then
        assertThat(adminCommonJpaService.deleteCommonCode(idx)).isNotNull();
    }
}

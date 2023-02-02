package com.tsp.api.common.service;

import com.tsp.api.common.domain.NewCodeDTO;
import com.tsp.api.common.domain.NewCodeEntity;
import com.tsp.api.model.service.AdminModelCommonServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
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
class AdminCommonJpaServiceTest extends AdminModelCommonServiceTest {
    @Mock private AdminCommonJpaService mockAdminCommonJpaService;
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
        // when
        when(mockAdminCommonJpaService.findOneCommonCode(newCodeDTO.getIdx())).thenReturn(newCodeDTO);
        NewCodeDTO commonCodeInfo = mockAdminCommonJpaService.findOneCommonCode(newCodeDTO.getIdx());

        // then
        assertThat(commonCodeInfo.getIdx()).isEqualTo(newCodeDTO.getIdx());
        assertThat(commonCodeInfo.getCategoryCd()).isEqualTo(1);
        assertThat(commonCodeInfo.getCategoryNm()).isEqualTo("men");
        assertThat(commonCodeInfo.getCmmType()).isEqualTo("model");

        // verify
        verify(mockAdminCommonJpaService, times(1)).findOneCommonCode(newCodeDTO.getIdx());
        verify(mockAdminCommonJpaService, atLeastOnce()).findOneCommonCode(newCodeDTO.getIdx());
        verifyNoMoreInteractions(mockAdminCommonJpaService);

        InOrder inOrder = inOrder(mockAdminCommonJpaService);
        inOrder.verify(mockAdminCommonJpaService).findOneCommonCode(newCodeDTO.getIdx());
    }

    @Test
    @DisplayName("공통코드 상세 조회 BDD 테스트")
    void 공통코드상세조회BDD테스트() {
        // when
        given(mockAdminCommonJpaService.findOneCommonCode(newCodeDTO.getIdx())).willReturn(newCodeDTO);
        NewCodeDTO commonCodeInfo = mockAdminCommonJpaService.findOneCommonCode(newCodeDTO.getIdx());

        // then
        assertThat(commonCodeInfo.getIdx()).isEqualTo(newCodeDTO.getIdx());
        assertThat(commonCodeInfo.getCategoryCd()).isEqualTo(1);
        assertThat(commonCodeInfo.getCategoryNm()).isEqualTo("men");
        assertThat(commonCodeInfo.getCmmType()).isEqualTo("model");

        // verify
        then(mockAdminCommonJpaService).should(times(1)).findOneCommonCode(newCodeDTO.getIdx());
        then(mockAdminCommonJpaService).should(atLeastOnce()).findOneCommonCode(newCodeDTO.getIdx());
        then(mockAdminCommonJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공통코드 등록 Mockito 테스트")
    void 공통코드등록Mockito테스트() {
        // when
        when(mockAdminCommonJpaService.findOneCommonCode(newCodeDTO.getIdx())).thenReturn(newCodeDTO);
        NewCodeDTO commonInfo = mockAdminCommonJpaService.findOneCommonCode(newCodeDTO.getIdx());

        // then
        assertThat(commonInfo.getCategoryCd()).isEqualTo(newCodeDTO.getCategoryCd());
        assertThat(commonInfo.getCategoryNm()).isEqualTo(newCodeDTO.getCategoryNm());
        assertThat(commonInfo.getCmmType()).isEqualTo(newCodeDTO.getCmmType());

        // verify
        verify(mockAdminCommonJpaService, times(1)).findOneCommonCode(newCodeDTO.getIdx());
        verify(mockAdminCommonJpaService, atLeastOnce()).findOneCommonCode(newCodeDTO.getIdx());
        verifyNoMoreInteractions(mockAdminCommonJpaService);

        InOrder inOrder = inOrder(mockAdminCommonJpaService);
        inOrder.verify(mockAdminCommonJpaService).findOneCommonCode(newCodeDTO.getIdx());
    }

    @Test
    @DisplayName("공통코드 등록 BDD 테스트")
    void 공통코드등록BDD테스트() {
        // when
        given(mockAdminCommonJpaService.findOneCommonCode(newCodeDTO.getIdx())).willReturn(newCodeDTO);
        NewCodeDTO commonInfo = mockAdminCommonJpaService.findOneCommonCode(newCodeDTO.getIdx());

        // then
        assertThat(commonInfo.getCategoryCd()).isEqualTo(newCodeDTO.getCategoryCd());
        assertThat(commonInfo.getCategoryNm()).isEqualTo(newCodeDTO.getCategoryNm());
        assertThat(commonInfo.getCmmType()).isEqualTo(newCodeDTO.getCmmType());

        // verify
        then(mockAdminCommonJpaService).should(times(1)).findOneCommonCode(newCodeDTO.getIdx());
        then(mockAdminCommonJpaService).should(atLeastOnce()).findOneCommonCode(newCodeDTO.getIdx());
        then(mockAdminCommonJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공통코드 수정 Mockito 테스트")
    void 공통코드수정Mockito테스트() {
        // given
        newCodeEntity = NewCodeEntity.builder()
                .idx(newCodeDTO.getIdx())
                .categoryCd(2)
                .categoryNm("new men")
                .cmmType("model")
                .visible("Y")
                .build();

        adminCommonJpaService.updateCommonCode(newCodeDTO.getIdx(), newCodeEntity);

        newCodeDTO = NewCodeEntity.toDto(newCodeEntity);

        // when
        when(mockAdminCommonJpaService.findOneCommonCode(newCodeDTO.getIdx())).thenReturn(newCodeDTO);
        NewCodeDTO commonCodeInfo = mockAdminCommonJpaService.findOneCommonCode(newCodeDTO.getIdx());

        // then
        assertThat(commonCodeInfo.getCategoryNm()).isEqualTo("new men");
        assertThat(commonCodeInfo.getCmmType()).isEqualTo("model");

        // verify
        verify(mockAdminCommonJpaService, times(1)).findOneCommonCode(newCodeDTO.getIdx());
        verify(mockAdminCommonJpaService, atLeastOnce()).findOneCommonCode(newCodeDTO.getIdx());
        verifyNoMoreInteractions(mockAdminCommonJpaService);

        InOrder inOrder = inOrder(mockAdminCommonJpaService);
        inOrder.verify(mockAdminCommonJpaService).findOneCommonCode(newCodeDTO.getIdx());
    }

    @Test
    @DisplayName("공통코드 수정 BDD 테스트")
    void 공통코드수정BDD테스트() {
        // given
        newCodeEntity = NewCodeEntity.builder()
                .idx(newCodeDTO.getIdx())
                .categoryCd(2)
                .categoryNm("new men")
                .cmmType("model")
                .visible("Y")
                .build();

        adminCommonJpaService.updateCommonCode(newCodeDTO.getIdx(), newCodeEntity);

        newCodeDTO = NewCodeEntity.toDto(newCodeEntity);

        // when
        given(mockAdminCommonJpaService.findOneCommonCode(newCodeDTO.getIdx())).willReturn(newCodeDTO);
        NewCodeDTO commonCodeInfo = mockAdminCommonJpaService.findOneCommonCode(newCodeDTO.getIdx());

        // then
        assertThat(commonCodeInfo.getCategoryNm()).isEqualTo("new men");
        assertThat(commonCodeInfo.getCmmType()).isEqualTo("model");

        // verify
        then(mockAdminCommonJpaService).should(times(1)).findOneCommonCode(newCodeDTO.getIdx());
        then(mockAdminCommonJpaService).should(atLeastOnce()).findOneCommonCode(newCodeDTO.getIdx());
        then(mockAdminCommonJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공통코드 삭제 테스트")
    void 공통코드삭제테스트() {
        // then
        assertThat(adminCommonJpaService.deleteCommonCode(newCodeDTO.getIdx())).isNotNull();
    }
}

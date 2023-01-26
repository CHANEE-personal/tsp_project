package com.tsp.api.support.service;

import com.tsp.api.comment.domain.AdminCommentDTO;
import com.tsp.api.comment.domain.AdminCommentEntity;
import com.tsp.api.support.domain.AdminSupportDTO;
import com.tsp.api.support.domain.AdminSupportEntity;
import com.tsp.api.support.domain.evaluation.EvaluationDTO;
import com.tsp.api.support.domain.evaluation.EvaluationEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("지원모델 Service Test")
class AdminSupportJpaServiceTest {
    @Mock private AdminSupportJpaService mockAdminSupportJpaService;
    private final AdminSupportJpaService adminSupportJpaService;

    private AdminSupportEntity adminSupportEntity;
    private AdminSupportDTO adminSupportDTO;

    private EvaluationEntity evaluationEntity;
    private EvaluationDTO evaluationDTO;
    private AdminCommentEntity adminCommentEntity;
    private AdminCommentDTO adminCommentDTO;

    void createSupport() {
        adminSupportEntity = AdminSupportEntity.builder()
                .supportName("조찬희")
                .supportHeight(170)
                .supportMessage("조찬희")
                .supportPhone("010-9466-2702")
                .supportSize3("31-24-31")
                .supportInstagram("https://instagram.com")
                .supportTime(LocalDateTime.now())
                .visible("Y")
                .build();

        adminSupportDTO = AdminSupportEntity.toDto(adminSupportEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createSupport();
    }

    @Test
    @DisplayName("지원모델 리스트 조회 테스트")
    void 지원모델리스트조회테스트() {
        // given
        Map<String, Object> supportMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        // then
        assertThat(adminSupportJpaService.findSupportList(supportMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("지원모델 리스트 조회 Mockito 테스트")
    void 지원모델리스트조회Mockito테스트() {
        // given
        Map<String, Object> supportMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminSupportDTO> returnSupportList = new ArrayList<>();
        returnSupportList.add(AdminSupportDTO.builder()
                .idx(1L).supportName("조찬희").supportHeight(170).supportMessage("조찬희")
                .supportPhone("010-1234-5678").supportSize3("31-24-31").supportInstagram("https://instagram.com").visible("Y").build());
        Page<AdminSupportDTO> resultSupport = new PageImpl<>(returnSupportList, pageRequest, returnSupportList.size());

        // when
        when(mockAdminSupportJpaService.findSupportList(supportMap, pageRequest)).thenReturn(resultSupport);
        Page<AdminSupportDTO> supportsList = mockAdminSupportJpaService.findSupportList(supportMap, pageRequest);
        List<AdminSupportDTO> findSupportList = supportsList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findSupportList).isNotEmpty(),
                () -> assertThat(findSupportList).hasSize(1)
        );

        assertThat(findSupportList.get(0).getIdx()).isEqualTo(returnSupportList.get(0).getIdx());
        assertThat(findSupportList.get(0).getSupportName()).isEqualTo(returnSupportList.get(0).getSupportName());
        assertThat(findSupportList.get(0).getSupportHeight()).isEqualTo(returnSupportList.get(0).getSupportHeight());
        assertThat(findSupportList.get(0).getSupportMessage()).isEqualTo(returnSupportList.get(0).getSupportMessage());
        assertThat(findSupportList.get(0).getVisible()).isEqualTo(returnSupportList.get(0).getVisible());

        // verify
        verify(mockAdminSupportJpaService, times(1)).findSupportList(supportMap, pageRequest);
        verify(mockAdminSupportJpaService, atLeastOnce()).findSupportList(supportMap, pageRequest);
        verifyNoMoreInteractions(mockAdminSupportJpaService);

        InOrder inOrder = inOrder(mockAdminSupportJpaService);
        inOrder.verify(mockAdminSupportJpaService).findSupportList(supportMap, pageRequest);
    }

    @Test
    @DisplayName("지원모델 리스트 조회 BDD 테스트")
    void 지원모델리스트조회BDD테스트() {
        // given
        Map<String, Object> supportMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminSupportDTO> returnSupportList = new ArrayList<>();
        returnSupportList.add(AdminSupportDTO.builder()
                .idx(1L).supportName("조찬희").supportHeight(170).supportMessage("조찬희")
                .supportPhone("010-1234-5678").supportSize3("31-24-31").supportInstagram("https://instagram.com").visible("Y").build());
        Page<AdminSupportDTO> resultSupport = new PageImpl<>(returnSupportList, pageRequest, returnSupportList.size());

        // when
        given(mockAdminSupportJpaService.findSupportList(supportMap, pageRequest)).willReturn(resultSupport);
        Page<AdminSupportDTO> supportsList = mockAdminSupportJpaService.findSupportList(supportMap, pageRequest);
        List<AdminSupportDTO> findSupportList = supportsList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findSupportList).isNotEmpty(),
                () -> assertThat(findSupportList).hasSize(1)
        );

        assertThat(findSupportList.get(0).getIdx()).isEqualTo(returnSupportList.get(0).getIdx());
        assertThat(findSupportList.get(0).getSupportName()).isEqualTo(returnSupportList.get(0).getSupportName());
        assertThat(findSupportList.get(0).getSupportHeight()).isEqualTo(returnSupportList.get(0).getSupportHeight());
        assertThat(findSupportList.get(0).getSupportMessage()).isEqualTo(returnSupportList.get(0).getSupportMessage());
        assertThat(findSupportList.get(0).getVisible()).isEqualTo(returnSupportList.get(0).getVisible());

        // verify
        then(mockAdminSupportJpaService).should(times(1)).findSupportList(supportMap, pageRequest);
        then(mockAdminSupportJpaService).should(atLeastOnce()).findSupportList(supportMap, pageRequest);
        then(mockAdminSupportJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @Disabled
    @DisplayName("지원모델 상세 조회 테스트")
    void 지원모델상세조회테스트() {
        // given
        adminSupportEntity = AdminSupportEntity.builder().idx(1L).build();

        // when
        adminSupportDTO = adminSupportJpaService.findOneSupportModel(adminSupportEntity.getIdx());

        // then
        assertThat(adminSupportDTO.getIdx()).isEqualTo(1);
    }

    @Test
    @DisplayName("지원모델 상세 조회 Mockito 테스트")
    void 지원모델상세조회Mockito테스트() {
        // when
        when(mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity.getIdx())).thenReturn(adminSupportDTO);
        AdminSupportDTO supportInfo = mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity.getIdx());

        // then
        assertThat(supportInfo.getIdx()).isEqualTo(adminSupportDTO.getIdx());
        assertThat(supportInfo.getSupportName()).isEqualTo(adminSupportDTO.getSupportName());
        assertThat(supportInfo.getSupportMessage()).isEqualTo(adminSupportDTO.getSupportMessage());
        assertThat(supportInfo.getVisible()).isEqualTo(adminSupportDTO.getVisible());

        // verify
        verify(mockAdminSupportJpaService, times(1)).findOneSupportModel(adminSupportEntity.getIdx());
        verify(mockAdminSupportJpaService, atLeastOnce()).findOneSupportModel(adminSupportEntity.getIdx());
        verifyNoMoreInteractions(mockAdminSupportJpaService);

        InOrder inOrder = inOrder(mockAdminSupportJpaService);
        inOrder.verify(mockAdminSupportJpaService).findOneSupportModel(adminSupportEntity.getIdx());
    }

    @Test
    @DisplayName("지원모델 상세 조회 BDD 테스트")
    void 지원모델상세조회BDD테스트() {
        // when
        given(mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity.getIdx())).willReturn(adminSupportDTO);
        AdminSupportDTO supportInfo = mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity.getIdx());

        // then
        assertThat(supportInfo.getIdx()).isEqualTo(adminSupportDTO.getIdx());
        assertThat(supportInfo.getSupportName()).isEqualTo(adminSupportDTO.getSupportName());
        assertThat(supportInfo.getSupportMessage()).isEqualTo(adminSupportDTO.getSupportMessage());
        assertThat(supportInfo.getVisible()).isEqualTo(adminSupportDTO.getVisible());

        // verify
        then(mockAdminSupportJpaService).should(times(1)).findOneSupportModel(adminSupportEntity.getIdx());
        then(mockAdminSupportJpaService).should(atLeastOnce()).findOneSupportModel(adminSupportEntity.getIdx());
        then(mockAdminSupportJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원모델 수정 Mockito 테스트")
    void 지원모델수정Mockito테스트() {
        // given
        Long idx = adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx();
        adminSupportEntity = AdminSupportEntity.builder()
                .idx(idx)
                .supportName("test")
                .supportPhone("010-9466-2702")
                .supportHeight(170)
                .supportSize3("31-24-31")
                .supportMessage("test")
                .supportInstagram("https://instagram.com")
                .visible("Y")
                .build();

        adminSupportJpaService.updateSupportModel(idx, adminSupportEntity);
        adminSupportDTO = AdminSupportEntity.toDto(adminSupportEntity);

        // when
        when(mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity.getIdx())).thenReturn(adminSupportDTO);
        AdminSupportDTO supportInfo = mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity.getIdx());

        // then
        assertThat(supportInfo.getSupportName()).isEqualTo("test");
        assertThat(supportInfo.getSupportPhone()).isEqualTo("010-9466-2702");

        // verify
        verify(mockAdminSupportJpaService, times(1)).findOneSupportModel(adminSupportEntity.getIdx());
        verify(mockAdminSupportJpaService, atLeastOnce()).findOneSupportModel(adminSupportEntity.getIdx());
        verifyNoMoreInteractions(mockAdminSupportJpaService);

        InOrder inOrder = inOrder(mockAdminSupportJpaService);
        inOrder.verify(mockAdminSupportJpaService).findOneSupportModel(adminSupportEntity.getIdx());
    }

    @Test
    @DisplayName("지원모델 수정 BDD 테스트")
    void 지원모델수정BDD테스트() {
        // given
        Long idx = adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx();
        adminSupportEntity = AdminSupportEntity.builder()
                .idx(idx)
                .supportName("test")
                .supportPhone("010-9466-2702")
                .supportHeight(170)
                .supportSize3("31-24-31")
                .supportMessage("test")
                .supportInstagram("https://instagram.com")
                .visible("Y")
                .build();

        adminSupportJpaService.updateSupportModel(idx, adminSupportEntity);
        adminSupportDTO = AdminSupportEntity.toDto(adminSupportEntity);

        // when
        given(mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity.getIdx())).willReturn(adminSupportDTO);
        AdminSupportDTO supportInfo = mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity.getIdx());

        // then
        assertThat(supportInfo.getSupportName()).isEqualTo("test");
        assertThat(supportInfo.getSupportPhone()).isEqualTo("010-9466-2702");

        // verify
        then(mockAdminSupportJpaService).should(times(1)).findOneSupportModel(adminSupportEntity.getIdx());
        then(mockAdminSupportJpaService).should(atLeastOnce()).findOneSupportModel(adminSupportEntity.getIdx());
        then(mockAdminSupportJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원모델 삭제 테스트")
    void 지원모델삭제테스트() {
        // given
        Long idx = adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx();

        // then
        assertThat(adminSupportJpaService.deleteSupportModel(idx)).isNotNull();
    }

    @Test
    @DisplayName("지원 모델 평가 리스트 조회 Mockito 테스트")
    void 지원모델평가리스트조회Mockito테스트() {
        // given
        Map<String, Object> evaluationMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<EvaluationDTO> evaluationList = new ArrayList<>();
//        evaluationList.add(EvaluationDTO.builder().idx(1L)
//                .supportIdx(adminSupportEntity.getIdx()).evaluateComment("합격").visible("Y").build());
        Page<EvaluationDTO> resultEvaluate = new PageImpl<>(evaluationList, pageRequest, evaluationList.size());

        // when
        when(mockAdminSupportJpaService.findEvaluationList(evaluationMap, pageRequest)).thenReturn(resultEvaluate);
        Page<EvaluationDTO> evaluationInfo = mockAdminSupportJpaService.findEvaluationList(evaluationMap, pageRequest);
        List<EvaluationDTO> findEvaluateList = evaluationInfo.stream().collect(Collectors.toList());

        // then
        assertThat(findEvaluateList.get(0).getIdx()).isEqualTo(evaluationList.get(0).getIdx());
//        assertThat(findEvaluateList.get(0).getSupportIdx()).isEqualTo(evaluationList.get(0).getSupportIdx());
        assertThat(findEvaluateList.get(0).getEvaluateComment()).isEqualTo(evaluationList.get(0).getEvaluateComment());

        // verify
        verify(mockAdminSupportJpaService, times(1)).findEvaluationList(evaluationMap, pageRequest);
        verify(mockAdminSupportJpaService, atLeastOnce()).findEvaluationList(evaluationMap, pageRequest);
        verifyNoMoreInteractions(mockAdminSupportJpaService);

        InOrder inOrder = inOrder(mockAdminSupportJpaService);
        inOrder.verify(mockAdminSupportJpaService).findEvaluationList(evaluationMap, pageRequest);
    }

    @Test
    @DisplayName("지원 모델 평가 리스트 조회 BDD 테스트")
    void 지원모델평가리스트조회BDD테스트() {
        // given
        Map<String, Object> evaluationMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<EvaluationDTO> evaluationList = new ArrayList<>();
//        evaluationList.add(EvaluationDTO.builder().idx(1L)
//                .supportIdx(adminSupportEntity.getIdx()).evaluateComment("합격").visible("Y").build());
        Page<EvaluationDTO> resultEvaluate = new PageImpl<>(evaluationList, pageRequest, evaluationList.size());

        // when
        given(mockAdminSupportJpaService.findEvaluationList(evaluationMap, pageRequest)).willReturn(resultEvaluate);
        Page<EvaluationDTO> evaluationInfo = mockAdminSupportJpaService.findEvaluationList(evaluationMap, pageRequest);
        List<EvaluationDTO> findEvaluateList = evaluationInfo.stream().collect(Collectors.toList());

        // then
        assertThat(findEvaluateList.get(0).getIdx()).isEqualTo(evaluationList.get(0).getIdx());
//        assertThat(findEvaluateList.get(0).getSupportIdx()).isEqualTo(evaluationList.get(0).getSupportIdx());
        assertThat(findEvaluateList.get(0).getEvaluateComment()).isEqualTo(evaluationList.get(0).getEvaluateComment());

        // verify
        then(mockAdminSupportJpaService).should(times(1)).findEvaluationList(evaluationMap, pageRequest);
        then(mockAdminSupportJpaService).should(atLeastOnce()).findEvaluationList(evaluationMap, pageRequest);
        then(mockAdminSupportJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원 모델 평가 상세 조회 Mockito 테스트")
    void 지원모델평가상세조회Mockito테스트() {
        // given
        evaluationEntity = EvaluationEntity.builder()
                .idx(1L).adminSupportEntity(adminSupportEntity)
                .evaluateComment("합격").visible("Y").build();

        evaluationDTO = EvaluationEntity.toDto(evaluationEntity);

        // when
        when(mockAdminSupportJpaService.findOneEvaluation(evaluationEntity.getIdx())).thenReturn(evaluationDTO);
        EvaluationDTO evaluationInfo = mockAdminSupportJpaService.findOneEvaluation(evaluationEntity.getIdx());

        // then
        assertThat(evaluationInfo.getIdx()).isEqualTo(1);
//        assertThat(evaluationInfo.getSupportIdx()).isEqualTo(adminSupportEntity.getIdx());
        assertThat(evaluationInfo.getEvaluateComment()).isEqualTo("합격");

        // verify
        verify(mockAdminSupportJpaService, times(1)).findOneEvaluation(evaluationEntity.getIdx());
        verify(mockAdminSupportJpaService, atLeastOnce()).findOneEvaluation(evaluationEntity.getIdx());
        verifyNoMoreInteractions(mockAdminSupportJpaService);

        InOrder inOrder = inOrder(mockAdminSupportJpaService);
        inOrder.verify(mockAdminSupportJpaService).findOneEvaluation(evaluationEntity.getIdx());
    }

    @Test
    @DisplayName("지원 모델 평가 상세 조회 BDD 테스트")
    void 지원모델평가상세조회BDD테스트() {
        // given
        evaluationEntity = EvaluationEntity.builder()
                .idx(1L).adminSupportEntity(adminSupportEntity)
                .evaluateComment("합격").visible("Y").build();

        evaluationDTO = EvaluationEntity.toDto(evaluationEntity);

        // when
        given(mockAdminSupportJpaService.findOneEvaluation(evaluationEntity.getIdx())).willReturn(evaluationDTO);
        EvaluationDTO evaluationInfo = mockAdminSupportJpaService.findOneEvaluation(evaluationEntity.getIdx());

        // then
        assertThat(evaluationInfo.getIdx()).isEqualTo(1);
//        assertThat(evaluationInfo.getSupportIdx()).isEqualTo(adminSupportEntity.getIdx());
        assertThat(evaluationInfo.getEvaluateComment()).isEqualTo("합격");

        // verify
        then(mockAdminSupportJpaService).should(times(1)).findOneEvaluation(evaluationEntity.getIdx());
        then(mockAdminSupportJpaService).should(atLeastOnce()).findOneEvaluation(evaluationEntity.getIdx());
        then(mockAdminSupportJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원 모델 평가 Mockito 테스트")
    void 지원모델평가Mockito테스트() {
        // given
        Long supportIdx = adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx();

        evaluationEntity = EvaluationEntity.builder()
                .evaluateComment("합격")
                .visible("Y")
                .build();

        adminSupportJpaService.evaluationSupportModel(supportIdx, evaluationEntity);

        evaluationDTO = EvaluationEntity.toDto(evaluationEntity);

        // when
        when(mockAdminSupportJpaService.findOneEvaluation(evaluationEntity.getIdx())).thenReturn(evaluationDTO);
        EvaluationDTO evaluationInfo = mockAdminSupportJpaService.findOneEvaluation(evaluationEntity.getIdx());

        // then
//        assertThat(evaluationInfo.getSupportIdx()).isEqualTo(supportIdx);
        assertThat(evaluationInfo.getEvaluateComment()).isEqualTo("합격");

        // verify
        verify(mockAdminSupportJpaService, times(1)).findOneEvaluation(evaluationEntity.getIdx());
        verify(mockAdminSupportJpaService, atLeastOnce()).findOneEvaluation(evaluationEntity.getIdx());
        verifyNoMoreInteractions(mockAdminSupportJpaService);

        InOrder inOrder = inOrder(mockAdminSupportJpaService);
        inOrder.verify(mockAdminSupportJpaService).findOneEvaluation(evaluationEntity.getIdx());
    }

    @Test
    @DisplayName("지원 모델 평가 BDD 테스트")
    void 지원모델평가BDD테스트() {
        // given
        Long supportIdx = adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx();

        evaluationEntity = EvaluationEntity.builder()
                .evaluateComment("합격")
                .visible("Y")
                .build();

        adminSupportJpaService.evaluationSupportModel(supportIdx, evaluationEntity);

        evaluationDTO = EvaluationEntity.toDto(evaluationEntity);

        // when
        given(mockAdminSupportJpaService.findOneEvaluation(evaluationEntity.getIdx())).willReturn(evaluationDTO);
        EvaluationDTO evaluationInfo = mockAdminSupportJpaService.findOneEvaluation(evaluationEntity.getIdx());

        // then
//        assertThat(evaluationInfo.getSupportIdx()).isEqualTo(supportIdx);
        assertThat(evaluationInfo.getEvaluateComment()).isEqualTo("합격");

        // verify
        then(mockAdminSupportJpaService).should(times(1)).findOneEvaluation(evaluationEntity.getIdx());
        then(mockAdminSupportJpaService).should(atLeastOnce()).findOneEvaluation(evaluationEntity.getIdx());
        then(mockAdminSupportJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원 모델 평가 삭제 Mockito 테스트")
    void 지원모델평가삭제Mockito테스트() {
        // given
        Long supportIdx = adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx();

        evaluationEntity = EvaluationEntity.builder()
                .evaluateComment("합격").visible("Y").build();

        // 지원모델 평가 저장
        adminSupportJpaService.evaluationSupportModel(supportIdx, evaluationEntity);
        evaluationDTO = EvaluationEntity.toDto(evaluationEntity);

        given(mockAdminSupportJpaService.findOneEvaluation(evaluationEntity.getIdx())).willReturn(evaluationDTO);
        Long deleteIdx = adminSupportJpaService.deleteEvaluation(evaluationEntity.getIdx());

        // then
        assertThat(mockAdminSupportJpaService.findOneEvaluation(evaluationEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminSupportJpaService, times(1)).findOneEvaluation(evaluationEntity.getIdx());
        verify(mockAdminSupportJpaService, atLeastOnce()).findOneEvaluation(evaluationEntity.getIdx());
        verifyNoMoreInteractions(mockAdminSupportJpaService);

        InOrder inOrder = inOrder(mockAdminSupportJpaService);
        inOrder.verify(mockAdminSupportJpaService).findOneEvaluation(evaluationEntity.getIdx());
    }

    @Test
    @DisplayName("지원 모델 평가 삭제 BDD 테스트")
    void 지원모델평가삭제BDD테스트() {
        // given
        Long supportIdx = adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx();

        evaluationEntity = EvaluationEntity.builder()
                .evaluateComment("합격").visible("Y").build();

        // 지원모델 평가 저장
        adminSupportJpaService.evaluationSupportModel(supportIdx, evaluationEntity);
        evaluationDTO = EvaluationEntity.toDto(evaluationEntity);

        given(mockAdminSupportJpaService.findOneEvaluation(evaluationEntity.getIdx())).willReturn(evaluationDTO);
        Long deleteIdx = adminSupportJpaService.deleteEvaluation(evaluationEntity.getIdx());

        // then
        assertThat(mockAdminSupportJpaService.findOneEvaluation(evaluationEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminSupportJpaService).should(times(1)).findOneEvaluation(evaluationEntity.getIdx());
        then(mockAdminSupportJpaService).should(atLeastOnce()).findOneEvaluation(evaluationEntity.getIdx());
        then(mockAdminSupportJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원 모델 합격 Mockito 테스트")
    void 지원모델합격Mockito테스트() {
        // given
        Long supportIdx = adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx();

        AdminSupportDTO supportDTO = adminSupportJpaService.updatePass(supportIdx);

        // when
        when(mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity.getIdx())).thenReturn(supportDTO);
        AdminSupportDTO supportInfo = mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity.getIdx());

        // then
        assertThat(supportInfo.getIdx()).isEqualTo(adminSupportEntity.getIdx());
        assertThat(supportInfo.getPassYn()).isEqualTo("Y");
        assertThat(supportInfo.getPassTime()).isNotNull();

        verify(mockAdminSupportJpaService, times(1)).findOneSupportModel(adminSupportEntity.getIdx());
        verify(mockAdminSupportJpaService, atLeastOnce()).findOneSupportModel(adminSupportEntity.getIdx());
        verifyNoMoreInteractions(mockAdminSupportJpaService);

        InOrder inOrder = inOrder(mockAdminSupportJpaService);
        inOrder.verify(mockAdminSupportJpaService).findOneSupportModel(adminSupportEntity.getIdx());
    }

    @Test
    @DisplayName("지원 모델 합격 BDD 테스트")
    void 지원모델합격BDD테스트() {
        // given
        Long supportIdx = adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx();

        AdminSupportDTO supportDTO = adminSupportJpaService.updatePass(supportIdx);

        // when
        when(mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity.getIdx())).thenReturn(supportDTO);
        AdminSupportDTO supportInfo = mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity.getIdx());

        // then
        assertThat(supportInfo.getIdx()).isEqualTo(adminSupportEntity.getIdx());
        assertThat(supportInfo.getPassYn()).isEqualTo("Y");
        assertThat(supportInfo.getPassTime()).isNotNull();

        then(mockAdminSupportJpaService).should(times(1)).findOneSupportModel(adminSupportEntity.getIdx());
        then(mockAdminSupportJpaService).should(atLeastOnce()).findOneSupportModel(adminSupportEntity.getIdx());
        then(mockAdminSupportJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원모델 어드민 코멘트 조회 Mockito 테스트")
    void 지원모델어드민코멘트조회Mockito테스트() {
        adminSupportEntity = AdminSupportEntity.builder()
                .supportName("조찬희")
                .supportHeight(170)
                .supportMessage("조찬희")
                .supportPhone("010-9466-2702")
                .supportSize3("31-24-31")
                .supportTime(LocalDateTime.now())
                .passYn("Y")
                .passTime(now())
                .visible("Y")
                .build();

        Long supportIdx = adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx();

        adminCommentEntity = AdminCommentEntity.builder()
                .comment("코멘트 테스트")
                .visible("Y")
                .build();

        List<AdminCommentDTO> adminCommentList = new ArrayList<>();
        adminCommentList.add(AdminCommentDTO.builder()
                .comment("코멘트 테스트")
                .commentType("support")
                .commentTypeIdx(supportIdx)
                .visible("Y")
                .build());

        when(mockAdminSupportJpaService.findSupportAdminComment(adminSupportEntity.getIdx())).thenReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminSupportJpaService.findSupportAdminComment(adminSupportEntity.getIdx());

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("support");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminSupportEntity.getIdx());

        verify(mockAdminSupportJpaService, times(1)).findSupportAdminComment(adminSupportEntity.getIdx());
        verify(mockAdminSupportJpaService, atLeastOnce()).findSupportAdminComment(adminSupportEntity.getIdx());
        verifyNoMoreInteractions(mockAdminSupportJpaService);

        InOrder inOrder = inOrder(mockAdminSupportJpaService);
        inOrder.verify(mockAdminSupportJpaService).findSupportAdminComment(adminSupportEntity.getIdx());
    }

    @Test
    @DisplayName("지원모델 어드민 코멘트 조회 BDD 테스트")
    void 지원모델어드민코멘트조회BDD테스트() {
        adminSupportEntity = AdminSupportEntity.builder()
                .supportName("조찬희")
                .supportHeight(170)
                .supportMessage("조찬희")
                .supportPhone("010-9466-2702")
                .supportSize3("31-24-31")
                .passYn("Y")
                .passTime(now())
                .visible("Y")
                .build();

        Long supportIdx = adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx();

        adminCommentEntity = AdminCommentEntity.builder()
                .comment("코멘트 테스트")
                .visible("Y")
                .build();

        List<AdminCommentDTO> adminCommentList = new ArrayList<>();
        adminCommentList.add(AdminCommentDTO.builder()
                .comment("코멘트 테스트")
                .commentType("support")
                .commentTypeIdx(supportIdx)
                .visible("Y")
                .build());

        given(mockAdminSupportJpaService.findSupportAdminComment(adminSupportEntity.getIdx())).willReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminSupportJpaService.findSupportAdminComment(adminSupportEntity.getIdx());

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("support");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminSupportEntity.getIdx());

        // verify
        then(mockAdminSupportJpaService).should(times(1)).findSupportAdminComment(adminSupportEntity.getIdx());
        then(mockAdminSupportJpaService).should(atLeastOnce()).findSupportAdminComment(adminSupportEntity.getIdx());
        then(mockAdminSupportJpaService).shouldHaveNoMoreInteractions();
    }
}

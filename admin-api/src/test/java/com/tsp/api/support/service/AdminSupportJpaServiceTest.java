package com.tsp.api.support.service;

import com.tsp.api.comment.domain.AdminCommentDTO;
import com.tsp.api.comment.domain.AdminCommentEntity;
import com.tsp.api.model.service.AdminModelCommonServiceTest;
import com.tsp.api.support.domain.AdminSupportDTO;
import com.tsp.api.support.domain.AdminSupportEntity;
import com.tsp.api.support.domain.evaluation.EvaluationDTO;
import com.tsp.api.support.domain.evaluation.EvaluationEntity;
import com.tsp.api.support.evaluation.AdminEvaluationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
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
class AdminSupportJpaServiceTest extends AdminModelCommonServiceTest {
    @Mock
    private AdminSupportJpaRepository adminSupportJpaRepository;
    @Mock
    private AdminSupportJpaQueryRepository adminSupportJpaQueryRepository;
    @InjectMocks
    private AdminSupportJpaServiceImpl mockAdminSupportJpaService;

    @Mock
    private AdminEvaluationJpaRepository adminEvaluationJpaRepository;
    private final AdminSupportJpaService adminSupportJpaService;

    @Test
    @DisplayName("지원모델 리스트 조회 테스트")
    void 지원모델리스트조회테스트() {
        // given
        Map<String, Object> supportMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        // then
        assertThat(adminSupportJpaService.findSupportList(supportMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("지원모델 리스트 조회 Mockito 테스트")
    void 지원모델리스트조회Mockito테스트() {
        // given
        Map<String, Object> supportMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<AdminSupportDTO> returnSupportList = new ArrayList<>();
        returnSupportList.add(AdminSupportDTO.builder()
                .idx(1L).supportName("조찬희").supportHeight(170).supportMessage("조찬희")
                .supportPhone("010-1234-5678").supportSize3("31-24-31").supportInstagram("https://instagram.com").visible("Y").build());
        Page<AdminSupportDTO> resultSupport = new PageImpl<>(returnSupportList, pageRequest, returnSupportList.size());

        // when
        when(adminSupportJpaQueryRepository.findSupportList(supportMap, pageRequest)).thenReturn(resultSupport);
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
        verify(adminSupportJpaQueryRepository, times(1)).findSupportList(supportMap, pageRequest);
        verify(adminSupportJpaQueryRepository, atLeastOnce()).findSupportList(supportMap, pageRequest);
        verifyNoMoreInteractions(adminSupportJpaQueryRepository);

        InOrder inOrder = inOrder(adminSupportJpaQueryRepository);
        inOrder.verify(adminSupportJpaQueryRepository).findSupportList(supportMap, pageRequest);
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
        given(adminSupportJpaQueryRepository.findSupportList(supportMap, pageRequest)).willReturn(resultSupport);
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
        then(adminSupportJpaQueryRepository).should(times(1)).findSupportList(supportMap, pageRequest);
        then(adminSupportJpaQueryRepository).should(atLeastOnce()).findSupportList(supportMap, pageRequest);
        then(adminSupportJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원모델 상세 조회 테스트")
    void 지원모델상세조회테스트() {
        // then
        assertThat(adminSupportJpaService.findOneSupportModel(adminSupportEntity.getIdx())).isNotNull();
    }

    @Test
    @DisplayName("지원모델 상세 조회 Mockito 테스트")
    void 지원모델상세조회Mockito테스트() {
        // when
        when(adminSupportJpaRepository.findById(adminSupportEntity.getIdx())).thenReturn(Optional.ofNullable(adminSupportEntity));
        AdminSupportDTO supportInfo = mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity.getIdx());

        // then
        assertThat(supportInfo.getIdx()).isEqualTo(adminSupportEntity.getIdx());
        assertThat(supportInfo.getSupportName()).isEqualTo(adminSupportEntity.getSupportName());
        assertThat(supportInfo.getSupportMessage()).isEqualTo(adminSupportEntity.getSupportMessage());
        assertThat(supportInfo.getVisible()).isEqualTo(adminSupportEntity.getVisible());

        // verify
        verify(adminSupportJpaRepository, times(1)).findById(adminSupportEntity.getIdx());
        verify(adminSupportJpaRepository, atLeastOnce()).findById(adminSupportEntity.getIdx());
        verifyNoMoreInteractions(adminSupportJpaRepository);

        InOrder inOrder = inOrder(adminSupportJpaRepository);
        inOrder.verify(adminSupportJpaRepository).findById(adminSupportEntity.getIdx());
    }

    @Test
    @DisplayName("지원모델 상세 조회 BDD 테스트")
    void 지원모델상세조회BDD테스트() {
        // when
        given(adminSupportJpaRepository.findById(adminSupportEntity.getIdx())).willReturn(Optional.ofNullable(adminSupportEntity));
        AdminSupportDTO supportInfo = mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity.getIdx());

        // then
        assertThat(supportInfo.getIdx()).isEqualTo(adminSupportEntity.getIdx());
        assertThat(supportInfo.getSupportName()).isEqualTo(adminSupportEntity.getSupportName());
        assertThat(supportInfo.getSupportMessage()).isEqualTo(adminSupportEntity.getSupportMessage());
        assertThat(supportInfo.getVisible()).isEqualTo(adminSupportEntity.getVisible());

        // verify
        then(adminSupportJpaRepository).should(times(1)).findById(adminSupportEntity.getIdx());
        then(adminSupportJpaRepository).should(atLeastOnce()).findById(adminSupportEntity.getIdx());
        then(adminSupportJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원모델 수정 Mockito 테스트")
    void 지원모델수정Mockito테스트() {
        // given
        AdminSupportEntity updateSupport = AdminSupportEntity.builder()
                .idx(adminSupportEntity.getIdx())
                .supportName("test")
                .supportPhone("010-9466-2702")
                .supportHeight(170)
                .supportSize3("31-24-31")
                .supportMessage("test")
                .supportInstagram("https://instagram.com")
                .visible("Y")
                .build();

        // when
        when(adminSupportJpaRepository.findById(updateSupport.getIdx())).thenReturn(Optional.of(updateSupport));
        when(adminSupportJpaRepository.save(updateSupport)).thenReturn(updateSupport);
        AdminSupportDTO supportInfo = mockAdminSupportJpaService.updateSupportModel(updateSupport.getIdx(), updateSupport);

        // then
        assertThat(supportInfo.getSupportName()).isEqualTo(updateSupport.getSupportName());
        assertThat(supportInfo.getSupportPhone()).isEqualTo(updateSupport.getSupportPhone());

        // verify
        verify(adminSupportJpaRepository, times(1)).findById(adminSupportEntity.getIdx());
        verify(adminSupportJpaRepository, atLeastOnce()).findById(adminSupportEntity.getIdx());
        verifyNoMoreInteractions(adminSupportJpaRepository);

        InOrder inOrder = inOrder(adminSupportJpaRepository);
        inOrder.verify(adminSupportJpaRepository).findById(adminSupportEntity.getIdx());
    }

    @Test
    @DisplayName("지원모델 수정 BDD 테스트")
    void 지원모델수정BDD테스트() {
        // given
        AdminSupportEntity updateSupport = AdminSupportEntity.builder()
                .idx(adminSupportEntity.getIdx())
                .supportName("test")
                .supportPhone("010-9466-2702")
                .supportHeight(170)
                .supportSize3("31-24-31")
                .supportMessage("test")
                .supportInstagram("https://instagram.com")
                .visible("Y")
                .build();

        // when
        given(adminSupportJpaRepository.findById(updateSupport.getIdx())).willReturn(Optional.of(updateSupport));
        given(adminSupportJpaRepository.save(updateSupport)).willReturn(updateSupport);
        AdminSupportDTO supportInfo = mockAdminSupportJpaService.updateSupportModel(updateSupport.getIdx(), updateSupport);

        // then
        assertThat(supportInfo.getSupportName()).isEqualTo(updateSupport.getSupportName());
        assertThat(supportInfo.getSupportPhone()).isEqualTo(updateSupport.getSupportPhone());

        // verify
        then(adminSupportJpaRepository).should(times(1)).findById(adminSupportEntity.getIdx());
        then(adminSupportJpaRepository).should(atLeastOnce()).findById(adminSupportEntity.getIdx());
        then(adminSupportJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원모델 삭제 테스트")
    void 지원모델삭제테스트() {
        // then
        assertThat(adminSupportJpaService.deleteSupportModel(adminSupportEntity.getIdx())).isNotNull();
    }

    @Test
    @DisplayName("지원 모델 평가 리스트 조회 Mockito 테스트")
    void 지원모델평가리스트조회Mockito테스트() {
        // given
        Map<String, Object> evaluationMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<EvaluationEntity> evaluationList = new ArrayList<>();
        evaluationList.add(EvaluationEntity.builder().idx(1L)
                .adminSupportEntity(adminSupportEntity).evaluateComment("합격").visible("Y").build());

        Page<EvaluationEntity> resultPage = new PageImpl<>(evaluationList, pageRequest, evaluationList.size());

        // when
        when(adminEvaluationJpaRepository.findAll(pageRequest)).thenReturn(resultPage);
        List<EvaluationDTO> findEvaluateList = mockAdminSupportJpaService.findEvaluationList(evaluationMap, pageRequest);

        // then
        assertThat(findEvaluateList.get(0).getIdx()).isEqualTo(evaluationList.get(0).getIdx());
        assertThat(findEvaluateList.get(0).getAdminSupportDTO().getIdx()).isEqualTo(evaluationList.get(0).getAdminSupportEntity().getIdx());
        assertThat(findEvaluateList.get(0).getEvaluateComment()).isEqualTo(evaluationList.get(0).getEvaluateComment());

        // verify
        verify(adminEvaluationJpaRepository, times(1)).findAll(pageRequest);
        verify(adminEvaluationJpaRepository, atLeastOnce()).findAll(pageRequest);
        verifyNoMoreInteractions(adminEvaluationJpaRepository);

        InOrder inOrder = inOrder(adminEvaluationJpaRepository);
        inOrder.verify(adminEvaluationJpaRepository).findAll(pageRequest);
    }

    @Test
    @DisplayName("지원 모델 평가 리스트 조회 BDD 테스트")
    void 지원모델평가리스트조회BDD테스트() {
        // given
        Map<String, Object> evaluationMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<EvaluationEntity> evaluationList = new ArrayList<>();
        evaluationList.add(EvaluationEntity.builder().idx(1L)
                .adminSupportEntity(adminSupportEntity).evaluateComment("합격").visible("Y").build());

        Page<EvaluationEntity> resultPage = new PageImpl<>(evaluationList, pageRequest, evaluationList.size());

        // when
        given(adminEvaluationJpaRepository.findAll(pageRequest)).willReturn(resultPage);
        List<EvaluationDTO> findEvaluateList = mockAdminSupportJpaService.findEvaluationList(evaluationMap, pageRequest);

        // then
        assertThat(findEvaluateList.get(0).getIdx()).isEqualTo(evaluationList.get(0).getIdx());
        assertThat(findEvaluateList.get(0).getAdminSupportDTO().getIdx()).isEqualTo(evaluationList.get(0).getAdminSupportEntity().getIdx());
        assertThat(findEvaluateList.get(0).getEvaluateComment()).isEqualTo(evaluationList.get(0).getEvaluateComment());

        // verify
        then(mockAdminSupportJpaService).should(times(1)).findEvaluationList(evaluationMap, pageRequest);
        then(mockAdminSupportJpaService).should(atLeastOnce()).findEvaluationList(evaluationMap, pageRequest);
        then(mockAdminSupportJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원 모델 평가 상세 조회 Mockito 테스트")
    void 지원모델평가상세조회Mockito테스트() {
        // when
        when(adminEvaluationJpaRepository.findById(evaluationEntity.getIdx())).thenReturn(Optional.ofNullable(evaluationEntity));
        EvaluationDTO evaluationInfo = mockAdminSupportJpaService.findOneEvaluation(evaluationEntity.getIdx());

        // then
        assertThat(evaluationInfo.getIdx()).isEqualTo(evaluationEntity.getIdx());
        assertThat(evaluationInfo.getAdminSupportDTO().getIdx()).isEqualTo(adminSupportEntity.getIdx());
        assertThat(evaluationInfo.getEvaluateComment()).isEqualTo(evaluationEntity.getEvaluateComment());

        // verify
        verify(adminEvaluationJpaRepository, times(1)).findById(evaluationEntity.getIdx());
        verify(adminEvaluationJpaRepository, atLeastOnce()).findById(evaluationEntity.getIdx());
        verifyNoMoreInteractions(adminEvaluationJpaRepository);

        InOrder inOrder = inOrder(adminEvaluationJpaRepository);
        inOrder.verify(adminEvaluationJpaRepository).findById(evaluationEntity.getIdx());
    }

    @Test
    @DisplayName("지원 모델 평가 상세 조회 BDD 테스트")
    void 지원모델평가상세조회BDD테스트() {
        // when
        given(adminEvaluationJpaRepository.findById(evaluationEntity.getIdx())).willReturn(Optional.ofNullable(evaluationEntity));
        EvaluationDTO evaluationInfo = mockAdminSupportJpaService.findOneEvaluation(evaluationEntity.getIdx());

        // then
        assertThat(evaluationInfo.getIdx()).isEqualTo(evaluationEntity.getIdx());
        assertThat(evaluationInfo.getAdminSupportDTO().getIdx()).isEqualTo(adminSupportEntity.getIdx());
        assertThat(evaluationInfo.getEvaluateComment()).isEqualTo(evaluationEntity.getEvaluateComment());

        // verify
        then(adminEvaluationJpaRepository).should(times(1)).findById(evaluationEntity.getIdx());
        then(adminEvaluationJpaRepository).should(atLeastOnce()).findById(evaluationEntity.getIdx());
        then(adminEvaluationJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원 모델 평가 Mockito 테스트")
    void 지원모델평가Mockito테스트() {
        // when
        when(adminSupportJpaRepository.findById(adminSupportEntity.getIdx())).thenReturn(Optional.ofNullable(adminSupportEntity));
        when(adminEvaluationJpaRepository.save(evaluationEntity)).thenReturn(evaluationEntity);
        EvaluationDTO evaluationInfo = mockAdminSupportJpaService.evaluationSupportModel(adminSupportEntity.getIdx(), evaluationEntity);

        // then
        assertThat(evaluationInfo.getAdminSupportDTO().getIdx()).isEqualTo(adminSupportEntity.getIdx());
        assertThat(evaluationInfo.getEvaluateComment()).isEqualTo(evaluationEntity.getEvaluateComment());

        // verify
        verify(adminSupportJpaRepository, times(1)).findById(adminSupportEntity.getIdx());
        verify(adminSupportJpaRepository, atLeastOnce()).findById(adminSupportEntity.getIdx());
        verifyNoMoreInteractions(adminSupportJpaRepository);

        InOrder inOrder = inOrder(adminSupportJpaRepository);
        inOrder.verify(adminSupportJpaRepository).findById(adminSupportEntity.getIdx());
    }

    @Test
    @DisplayName("지원 모델 평가 BDD 테스트")
    void 지원모델평가BDD테스트() {
        // when
        given(adminSupportJpaRepository.findById(adminSupportEntity.getIdx())).willReturn(Optional.ofNullable(adminSupportEntity));
        given(adminEvaluationJpaRepository.save(evaluationEntity)).willReturn(evaluationEntity);
        EvaluationDTO evaluationInfo = mockAdminSupportJpaService.evaluationSupportModel(adminSupportEntity.getIdx(), evaluationEntity);

        // then
        assertThat(evaluationInfo.getAdminSupportDTO().getIdx()).isEqualTo(adminSupportEntity.getIdx());
        assertThat(evaluationInfo.getEvaluateComment()).isEqualTo(evaluationEntity.getEvaluateComment());

        // verify
        then(adminSupportJpaRepository).should(times(1)).findById(adminSupportEntity.getIdx());
        then(adminSupportJpaRepository).should(atLeastOnce()).findById(adminSupportEntity.getIdx());
        then(adminSupportJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원 모델 평가 삭제 Mockito 테스트")
    void 지원모델평가삭제Mockito테스트() {
        Long deleteIdx = adminSupportJpaService.deleteEvaluation(evaluationEntity.getIdx());

        // then
        assertThat(evaluationEntity.getIdx()).isEqualTo(deleteIdx);
    }

    @Test
    @DisplayName("지원 모델 합격 Mockito 테스트")
    void 지원모델합격Mockito테스트() {
        // given
        AdminSupportEntity updateSupport = AdminSupportEntity.builder()
                .idx(adminSupportEntity.getIdx())
                .supportName("조찬희")
                .supportHeight(170)
                .supportMessage("조찬희")
                .supportPhone("010-9466-2702")
                .supportSize3("31-24-31")
                .supportInstagram("https://instagram.com")
                .supportTime(LocalDateTime.now())
                .passYn("N")
                .visible("Y")
                .build();
        // when
        when(adminSupportJpaRepository.findById(adminSupportEntity.getIdx())).thenReturn(Optional.ofNullable(adminSupportEntity));
        when(adminSupportJpaRepository.save(updateSupport)).thenReturn(updateSupport);
        AdminSupportDTO supportInfo = mockAdminSupportJpaService.updatePass(adminSupportEntity.getIdx());

        // then
        assertThat(supportInfo.getIdx()).isEqualTo(adminSupportEntity.getIdx());
        assertThat(supportInfo.getPassYn()).isEqualTo("Y");
        assertThat(supportInfo.getPassTime()).isNotNull();

        verify(adminSupportJpaRepository, times(1)).findById(adminSupportEntity.getIdx());
        verify(adminSupportJpaRepository, atLeastOnce()).findById(adminSupportEntity.getIdx());
        verifyNoMoreInteractions(adminSupportJpaRepository);

        InOrder inOrder = inOrder(adminSupportJpaRepository);
        inOrder.verify(adminSupportJpaRepository).findById(adminSupportEntity.getIdx());
    }

    @Test
    @DisplayName("지원 모델 합격 BDD 테스트")
    void 지원모델합격BDD테스트() {
        // given
        AdminSupportEntity updateSupport = AdminSupportEntity.builder()
                .idx(adminSupportEntity.getIdx())
                .supportName("조찬희")
                .supportHeight(170)
                .supportMessage("조찬희")
                .supportPhone("010-9466-2702")
                .supportSize3("31-24-31")
                .supportInstagram("https://instagram.com")
                .supportTime(LocalDateTime.now())
                .passYn("N")
                .visible("Y")
                .build();
        // when
        given(adminSupportJpaRepository.findById(adminSupportEntity.getIdx())).willReturn(Optional.ofNullable(adminSupportEntity));
        given(adminSupportJpaRepository.save(updateSupport)).willReturn(updateSupport);
        AdminSupportDTO supportInfo = mockAdminSupportJpaService.updatePass(adminSupportEntity.getIdx());

        // then
        assertThat(supportInfo.getIdx()).isEqualTo(adminSupportEntity.getIdx());
        assertThat(supportInfo.getPassYn()).isEqualTo("Y");
        assertThat(supportInfo.getPassTime()).isNotNull();

        then(adminSupportJpaRepository).should(times(1)).findById(adminSupportEntity.getIdx());
        then(adminSupportJpaRepository).should(atLeastOnce()).findById(adminSupportEntity.getIdx());
        then(adminSupportJpaRepository).shouldHaveNoMoreInteractions();
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

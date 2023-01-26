package com.tsp.api.support.service;

import com.tsp.api.comment.domain.AdminCommentDTO;
import com.tsp.api.comment.domain.AdminCommentEntity;
import com.tsp.api.support.domain.AdminSupportDTO;
import com.tsp.api.support.domain.AdminSupportEntity;
import com.tsp.api.support.domain.evaluation.EvaluationDTO;
import com.tsp.api.support.domain.evaluation.EvaluationEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("지원모델 Repository Test")
class AdminSupportJpaQueryRepositoryTest {
    @Mock
    AdminSupportJpaQueryRepository mockAdminSupportJpaQueryRepository;
    private final AdminSupportJpaQueryRepository adminSupportJpaQueryRepository;
    private final AdminSupportJpaRepository adminSupportJpaRepository;

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
        PageRequest pageRequest = PageRequest.of(1, 100);

        // then
        assertThat(adminSupportJpaQueryRepository.findSupportList(supportMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("지원모델 Mockito 조회 테스트")
    void 지원모델Mockito조회테스트() {
        // given
        Map<String, Object> supportMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminSupportDTO> supportList = new ArrayList<>();
        supportList.add(AdminSupportDTO.builder().idx(1L).supportName("조찬희").supportPhone("010-9466-2702").build());
        Page<AdminSupportDTO> resultSupport = new PageImpl<>(supportList, pageRequest, supportList.size());

        // when
        when(mockAdminSupportJpaQueryRepository.findSupportList(supportMap, pageRequest)).thenReturn(resultSupport);
        Page<AdminSupportDTO> supportInfo = mockAdminSupportJpaQueryRepository.findSupportList(supportMap, pageRequest);
        List<AdminSupportDTO> findSupportList = supportInfo.stream().collect(Collectors.toList());

        // then
        assertThat(findSupportList.get(0).getIdx()).isEqualTo(supportList.get(0).getIdx());
        assertThat(findSupportList.get(0).getSupportName()).isEqualTo(supportList.get(0).getSupportName());

        // verify
        verify(mockAdminSupportJpaQueryRepository, times(1)).findSupportList(supportMap, pageRequest);
        verify(mockAdminSupportJpaQueryRepository, atLeastOnce()).findSupportList(supportMap, pageRequest);
        verifyNoMoreInteractions(mockAdminSupportJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminSupportJpaQueryRepository);
        inOrder.verify(mockAdminSupportJpaQueryRepository).findSupportList(supportMap, pageRequest);
    }

    @Test
    @DisplayName("지원모델 BDD 조회 테스트")
    void 지원모델BDD조회테스트() {
        // given
        Map<String, Object> supportMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminSupportDTO> supportList = new ArrayList<>();
        supportList.add(AdminSupportDTO.builder().idx(1L).supportName("조찬희").supportPhone("010-9466-2702").build());
        Page<AdminSupportDTO> resultSupport = new PageImpl<>(supportList, pageRequest, supportList.size());

        // when
        given(mockAdminSupportJpaQueryRepository.findSupportList(supportMap, pageRequest)).willReturn(resultSupport);
        Page<AdminSupportDTO> supportInfo = mockAdminSupportJpaQueryRepository.findSupportList(supportMap, pageRequest);
        List<AdminSupportDTO> findSupportList = supportInfo.stream().collect(Collectors.toList());

        // then
        assertThat(findSupportList.get(0).getIdx()).isEqualTo(supportList.get(0).getIdx());
        assertThat(findSupportList.get(0).getSupportName()).isEqualTo(supportList.get(0).getSupportName());

        // verify
        then(mockAdminSupportJpaQueryRepository).should(times(1)).findSupportList(supportMap, pageRequest);
        then(mockAdminSupportJpaQueryRepository).should(atLeastOnce()).findSupportList(supportMap, pageRequest);
        then(mockAdminSupportJpaQueryRepository).shouldHaveNoMoreInteractions();
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
        when(mockAdminSupportJpaQueryRepository.findEvaluationList(evaluationMap, pageRequest)).thenReturn(resultEvaluate);
        Page<EvaluationDTO> evaluationInfo = mockAdminSupportJpaQueryRepository.findEvaluationList(evaluationMap, pageRequest);
        List<EvaluationDTO> findEvaluationList = evaluationInfo.stream().collect(Collectors.toList());

        // then
        assertThat(findEvaluationList.get(0).getIdx()).isEqualTo(evaluationList.get(0).getIdx());
//        assertThat(findEvaluationList.get(0).getSupportIdx()).isEqualTo(evaluationList.get(0).getSupportIdx());
        assertThat(findEvaluationList.get(0).getEvaluateComment()).isEqualTo(evaluationList.get(0).getEvaluateComment());

        // verify
        verify(mockAdminSupportJpaQueryRepository, times(1)).findEvaluationList(evaluationMap, pageRequest);
        verify(mockAdminSupportJpaQueryRepository, atLeastOnce()).findEvaluationList(evaluationMap, pageRequest);
        verifyNoMoreInteractions(mockAdminSupportJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminSupportJpaQueryRepository);
        inOrder.verify(mockAdminSupportJpaQueryRepository).findEvaluationList(evaluationMap, pageRequest);
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
        given(mockAdminSupportJpaQueryRepository.findEvaluationList(evaluationMap, pageRequest)).willReturn(resultEvaluate);
        Page<EvaluationDTO> evaluationInfo = mockAdminSupportJpaQueryRepository.findEvaluationList(evaluationMap, pageRequest);
        List<EvaluationDTO> findEvaluationList = evaluationInfo.stream().collect(Collectors.toList());

        // then
        assertThat(findEvaluationList.get(0).getIdx()).isEqualTo(evaluationList.get(0).getIdx());
//        assertThat(findEvaluationList.get(0).getSupportIdx()).isEqualTo(evaluationList.get(0).getSupportIdx());
        assertThat(findEvaluationList.get(0).getEvaluateComment()).isEqualTo(evaluationList.get(0).getEvaluateComment());

        // verify
        then(mockAdminSupportJpaQueryRepository).should(times(1)).findEvaluationList(evaluationMap, pageRequest);
        then(mockAdminSupportJpaQueryRepository).should(atLeastOnce()).findEvaluationList(evaluationMap, pageRequest);
        then(mockAdminSupportJpaQueryRepository).shouldHaveNoMoreInteractions();
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

        Long supportIdx = adminSupportJpaRepository.save(adminSupportEntity).getIdx();

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

        when(mockAdminSupportJpaQueryRepository.findSupportAdminComment(adminSupportEntity.getIdx())).thenReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminSupportJpaQueryRepository.findSupportAdminComment(adminSupportEntity.getIdx());

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("support");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminSupportEntity.getIdx());

        verify(mockAdminSupportJpaQueryRepository, times(1)).findSupportAdminComment(adminSupportEntity.getIdx());
        verify(mockAdminSupportJpaQueryRepository, atLeastOnce()).findSupportAdminComment(adminSupportEntity.getIdx());
        verifyNoMoreInteractions(mockAdminSupportJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminSupportJpaQueryRepository);
        inOrder.verify(mockAdminSupportJpaQueryRepository).findSupportAdminComment(adminSupportEntity.getIdx());
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

        Long supportIdx = adminSupportJpaRepository.save(adminSupportEntity).getIdx();

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

        given(mockAdminSupportJpaQueryRepository.findSupportAdminComment(adminSupportEntity.getIdx())).willReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminSupportJpaQueryRepository.findSupportAdminComment(adminSupportEntity.getIdx());

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("support");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminSupportEntity.getIdx());

        // verify
        then(mockAdminSupportJpaQueryRepository).should(times(1)).findSupportAdminComment(adminSupportEntity.getIdx());
        then(mockAdminSupportJpaQueryRepository).should(atLeastOnce()).findSupportAdminComment(adminSupportEntity.getIdx());
        then(mockAdminSupportJpaQueryRepository).shouldHaveNoMoreInteractions();
    }
}

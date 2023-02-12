package com.tsp.api.model.service.negotiation;

import com.tsp.api.model.domain.negotiation.AdminNegotiationDTO;
import com.tsp.api.model.domain.negotiation.AdminNegotiationEntity;
import com.tsp.api.model.service.AdminModelCommonServiceTest;
import com.tsp.api.model.service.AdminModelJpaRepository;
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

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("모델 섭외 Service Test")
class AdminNegotiationJpaServiceTest extends AdminModelCommonServiceTest {

    @Mock private AdminModelJpaRepository adminModelJpaRepository;
    @Mock private AdminNegotiationJpaRepository adminNegotiationJpaRepository;
    @Mock private AdminNegotiationJpaQueryRepository adminNegotiationJpaQueryRepository;
    @InjectMocks private AdminNegotiationJpaServiceImpl mockAdminNegotiationJpaService;
    private final AdminNegotiationJpaService adminNegotiationJpaService;
    private final EntityManager em;

    @Test
    @DisplayName("모델 섭외 리스트 조회 테스트")
    void 모델섭외리스트조회테스트() {
        // given
        Map<String, Object> negotiationMap = new HashMap<>();
        negotiationMap.put("searchKeyword", "김예영");
        PageRequest pageRequest = PageRequest.of(0, 100);

        // then
        assertThat(adminNegotiationJpaService.findNegotiationList(negotiationMap, pageRequest)).isNotEmpty();

        Map<String, Object> lastMonthNegotiationMap = new HashMap<>();
        lastMonthNegotiationMap.put("searchStartTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 1, 0, 0, 0, 0));
        lastMonthNegotiationMap.put("searchEndTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 30, 23, 59, 59));
        PageRequest pageRequest2 = PageRequest.of(1, 100);

        // then
        assertThat(adminNegotiationJpaService.findNegotiationList(negotiationMap, pageRequest2)).isNotEmpty();

        Map<String, Object> currentNegotiationMap = new HashMap<>();
        currentNegotiationMap.put("searchStartTime", of(now().getYear(), LocalDate.now().getMonth(), 1, 0, 0, 0, 0));
        currentNegotiationMap.put("searchEndTime", of(now().getYear(), LocalDate.now().getMonth(), 30, 23, 59, 59));
        PageRequest pageRequest3 = PageRequest.of(1, 100);

        // then
        assertThat(adminNegotiationJpaService.findNegotiationList(negotiationMap, pageRequest3)).isNotEmpty();
    }

    @Test
    @DisplayName("모델 섭외 Mockito 조회 테스트")
    void 모델섭외Mockito조회테스트() {
        // given
        Map<String, Object> negotiationMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminNegotiationDTO> negotiationList = new ArrayList<>();
        negotiationList.add(AdminNegotiationDTO.builder().adminModelDTO(adminModelDTO)
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 첫번째").modelNegotiationDate(now()).build());
        negotiationList.add(AdminNegotiationDTO.builder().adminModelDTO(adminModelDTO)
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 두번째").modelNegotiationDate(now()).build());

        Page<AdminNegotiationDTO> resultNegotiation = new PageImpl<>(negotiationList, pageRequest, negotiationList.size());

        // when
        when(adminNegotiationJpaQueryRepository.findNegotiationList(negotiationMap, pageRequest)).thenReturn(resultNegotiation);
        Page<AdminNegotiationDTO> newModelNegotiationList = mockAdminNegotiationJpaService.findNegotiationList(negotiationMap, pageRequest);
        List<AdminNegotiationDTO> findNegotiationList = newModelNegotiationList.stream().collect(Collectors.toList());

        // then
        assertThat(findNegotiationList.get(0).getIdx()).isEqualTo(negotiationList.get(0).getIdx());
        assertThat(findNegotiationList.get(0).getModelNegotiationDesc()).isEqualTo(negotiationList.get(0).getModelNegotiationDesc());

        // verify
        verify(adminNegotiationJpaQueryRepository, times(1)).findNegotiationList(negotiationMap, pageRequest);
        verify(adminNegotiationJpaQueryRepository, atLeastOnce()).findNegotiationList(negotiationMap, pageRequest);
        verifyNoMoreInteractions(adminNegotiationJpaQueryRepository);

        InOrder inOrder = inOrder(adminNegotiationJpaQueryRepository);
        inOrder.verify(adminNegotiationJpaQueryRepository).findNegotiationList(negotiationMap, pageRequest);
    }

    @Test
    @DisplayName("모델 섭외 BDD 조회 테스트")
    void 모델섭외BDD조회테스트() {
        // given
        Map<String, Object> negotiationMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminNegotiationDTO> negotiationList = new ArrayList<>();
        negotiationList.add(AdminNegotiationDTO.builder().adminModelDTO(adminModelDTO)
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 첫번째").modelNegotiationDate(now()).build());
        negotiationList.add(AdminNegotiationDTO.builder().adminModelDTO(adminModelDTO)
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 두번째").modelNegotiationDate(now()).build());

        Page<AdminNegotiationDTO> resultNegotiation = new PageImpl<>(negotiationList, pageRequest, negotiationList.size());

        // when
        given(adminNegotiationJpaQueryRepository.findNegotiationList(negotiationMap, pageRequest)).willReturn(resultNegotiation);
        Page<AdminNegotiationDTO> newModelNegotiationList = mockAdminNegotiationJpaService.findNegotiationList(negotiationMap, pageRequest);
        List<AdminNegotiationDTO> findNegotiationList = newModelNegotiationList.stream().collect(Collectors.toList());

        // then
        assertThat(findNegotiationList.get(0).getIdx()).isEqualTo(negotiationList.get(0).getIdx());
        assertThat(findNegotiationList.get(0).getModelNegotiationDesc()).isEqualTo(negotiationList.get(0).getModelNegotiationDesc());

        // verify
        then(adminNegotiationJpaQueryRepository).should(times(1)).findNegotiationList(negotiationMap, pageRequest);
        then(adminNegotiationJpaQueryRepository).should(atLeastOnce()).findNegotiationList(negotiationMap, pageRequest);
        then(adminNegotiationJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 모델 섭외 상세 조회 테스트")
    void 이전or다음모델섭외상세조회테스트() {
        // given
        adminNegotiationEntity = AdminNegotiationEntity.builder().idx(2L).build();

        // when
        adminNegotiationDTO = adminNegotiationJpaService.findOneNegotiation(adminNegotiationEntity.getIdx());

        // 이전 모델 섭외
        assertThat(adminNegotiationJpaService.findPrevOneNegotiation(adminNegotiationEntity.getIdx()).getIdx()).isEqualTo(1);
        // 다음 모델 섭외
        assertThat(adminNegotiationJpaService.findNextOneNegotiation(adminNegotiationEntity.getIdx()).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 모델 섭외 상세 조회 Mockito 테스트")
    void 이전모델섭외상세조회Mockito테스트() {
        // given
        adminNegotiationEntity = AdminNegotiationEntity.builder().idx(2L).build();

        // when
        adminNegotiationDTO = adminNegotiationJpaService.findOneNegotiation(adminNegotiationEntity.getIdx());

        when(mockAdminNegotiationJpaService.findPrevOneNegotiation(adminNegotiationEntity.getIdx())).thenReturn(adminNegotiationDTO);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaService.findPrevOneNegotiation(adminNegotiationEntity.getIdx());

        // then
        assertThat(negotiationInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockAdminNegotiationJpaService, times(1)).findPrevOneNegotiation(adminNegotiationEntity.getIdx());
        verify(mockAdminNegotiationJpaService, atLeastOnce()).findPrevOneNegotiation(adminNegotiationEntity.getIdx());
        verifyNoMoreInteractions(mockAdminNegotiationJpaService);

        InOrder inOrder = inOrder(mockAdminNegotiationJpaService);
        inOrder.verify(mockAdminNegotiationJpaService).findPrevOneNegotiation(adminNegotiationEntity.getIdx());
    }

    @Test
    @DisplayName("이전 모델 섭외 상세 조회 BDD 테스트")
    void 이전모델섭외상세조회BDD테스트() {
        // given
        adminNegotiationEntity = AdminNegotiationEntity.builder().idx(2L).build();

        // when
        adminNegotiationDTO = adminNegotiationJpaService.findOneNegotiation(adminNegotiationEntity.getIdx());

        given(mockAdminNegotiationJpaService.findPrevOneNegotiation(adminNegotiationEntity.getIdx())).willReturn(adminNegotiationDTO);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaService.findPrevOneNegotiation(adminNegotiationEntity.getIdx());

        // then
        assertThat(negotiationInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockAdminNegotiationJpaService).should(times(1)).findPrevOneNegotiation(adminNegotiationEntity.getIdx());
        then(mockAdminNegotiationJpaService).should(atLeastOnce()).findPrevOneNegotiation(adminNegotiationEntity.getIdx());
        then(mockAdminNegotiationJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 모델 섭외 상세 조회 Mockito 테스트")
    void 다음모델섭외상세조회Mockito테스트() {
        // given
        adminNegotiationEntity = AdminNegotiationEntity.builder().idx(2L).build();

        // when
        adminNegotiationDTO = adminNegotiationJpaService.findOneNegotiation(adminNegotiationEntity.getIdx());

        when(mockAdminNegotiationJpaService.findNextOneNegotiation(adminNegotiationEntity.getIdx())).thenReturn(adminNegotiationDTO);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaService.findNextOneNegotiation(adminNegotiationEntity.getIdx());

        // then
        assertThat(negotiationInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockAdminNegotiationJpaService, times(1)).findNextOneNegotiation(adminNegotiationEntity.getIdx());
        verify(mockAdminNegotiationJpaService, atLeastOnce()).findNextOneNegotiation(adminNegotiationEntity.getIdx());
        verifyNoMoreInteractions(mockAdminNegotiationJpaService);

        InOrder inOrder = inOrder(mockAdminNegotiationJpaService);
        inOrder.verify(mockAdminNegotiationJpaService).findNextOneNegotiation(adminNegotiationEntity.getIdx());
    }

    @Test
    @DisplayName("다음 모델 섭외 상세 조회 BDD 테스트")
    void 다음모델섭외상세조회BDD테스트() {
        // given
        AdminNegotiationDTO oneNegotiation = adminNegotiationJpaService.insertModelNegotiation(adminModelEntity.getIdx(), adminNegotiationEntity);

        // when
        adminNegotiationDTO = adminNegotiationJpaService.findOneNegotiation(oneNegotiation.getIdx());

        given(mockAdminNegotiationJpaService.findNextOneNegotiation(adminNegotiationEntity.getIdx())).willReturn(adminNegotiationDTO);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaService.findNextOneNegotiation(adminNegotiationEntity.getIdx());

        // then
        assertThat(negotiationInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockAdminNegotiationJpaService).should(times(1)).findNextOneNegotiation(adminNegotiationEntity.getIdx());
        then(mockAdminNegotiationJpaService).should(atLeastOnce()).findNextOneNegotiation(adminNegotiationEntity.getIdx());
        then(mockAdminNegotiationJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델섭외등록Mockito테스트")
    void 모델섭외등록Mockito테스트() {
        // when
        when(adminModelJpaRepository.findById(adminModelEntity.getIdx())).thenReturn(Optional.ofNullable(adminModelEntity));
        when(adminNegotiationJpaRepository.save(adminNegotiationEntity)).thenReturn(adminNegotiationEntity);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaService.insertModelNegotiation(adminModelEntity.getIdx(), adminNegotiationEntity);

        // then
        assertThat(negotiationInfo.getAdminModelDTO().getIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(negotiationInfo.getModelKorName()).isEqualTo("조찬희");
        assertThat(negotiationInfo.getModelNegotiationDesc()).isNotNull();

        // verify
        verify(adminNegotiationJpaRepository, times(1)).save(adminNegotiationEntity);
        verify(adminNegotiationJpaRepository, atLeastOnce()).save(adminNegotiationEntity);
        verifyNoMoreInteractions(adminNegotiationJpaRepository);

        InOrder inOrder = inOrder(adminNegotiationJpaRepository);
        inOrder.verify(adminNegotiationJpaRepository).save(adminNegotiationEntity);
    }

    @Test
    @DisplayName("모델섭외등록BDD테스트")
    void 모델섭외등록BDD테스트() {
        // when
        given(adminModelJpaRepository.findById(adminModelEntity.getIdx())).willReturn(Optional.ofNullable(adminModelEntity));
        given(adminNegotiationJpaRepository.save(adminNegotiationEntity)).willReturn(adminNegotiationEntity);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaService.insertModelNegotiation(adminModelEntity.getIdx(), adminNegotiationEntity);

        // then
        assertThat(negotiationInfo.getAdminModelDTO().getIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(negotiationInfo.getModelKorName()).isEqualTo("조찬희");
        assertThat(negotiationInfo.getModelNegotiationDesc()).isNotNull();

        // verify
        then(adminNegotiationJpaRepository).should(times(1)).save(adminNegotiationEntity);
        then(adminNegotiationJpaRepository).should(atLeastOnce()).save(adminNegotiationEntity);
        then(adminNegotiationJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델섭외수정Mockito테스트")
    void 모델섭외수정Mockito테스트() {
        // given
        AdminNegotiationEntity updateNegotiationEntity = AdminNegotiationEntity.builder()
                .idx(adminNegotiationDTO.getIdx())
                .modelKorName(adminModelEntity.getModelKorName())
                .adminModelEntity(adminModelEntity)
                .modelNegotiationDesc("섭외 수정 테스트")
                .modelNegotiationDate(now())
                .name("조찬희")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        // when
        when(adminNegotiationJpaRepository.findById(updateNegotiationEntity.getIdx())).thenReturn(Optional.of(updateNegotiationEntity));
        when(adminNegotiationJpaRepository.save(updateNegotiationEntity)).thenReturn(updateNegotiationEntity);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaService.updateModelNegotiation(updateNegotiationEntity.getIdx(), updateNegotiationEntity);

        // then
        assertThat(negotiationInfo.getAdminModelDTO().getIdx()).isEqualTo(adminModelDTO.getIdx());
        assertThat(negotiationInfo.getModelNegotiationDesc()).isEqualTo("섭외 수정 테스트");

        // verify
        verify(adminNegotiationJpaRepository, times(1)).findById(updateNegotiationEntity.getIdx());
        verify(adminNegotiationJpaRepository, atLeastOnce()).findById(updateNegotiationEntity.getIdx());
        verifyNoMoreInteractions(adminNegotiationJpaRepository);

        InOrder inOrder = inOrder(adminNegotiationJpaRepository);
        inOrder.verify(adminNegotiationJpaRepository).findById(updateNegotiationEntity.getIdx());
    }

    @Test
    @DisplayName("모델섭외수정BDD테스트")
    void 모델섭외수정BDD테스트() {
        // given
        AdminNegotiationEntity updateNegotiationEntity = AdminNegotiationEntity.builder()
                .idx(adminNegotiationDTO.getIdx())
                .modelKorName(adminModelEntity.getModelKorName())
                .adminModelEntity(adminModelEntity)
                .modelNegotiationDesc("섭외 수정 테스트")
                .modelNegotiationDate(now())
                .name("조찬희")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        // when
        given(adminNegotiationJpaRepository.findById(updateNegotiationEntity.getIdx())).willReturn(Optional.of(updateNegotiationEntity));
        given(adminNegotiationJpaRepository.save(updateNegotiationEntity)).willReturn(updateNegotiationEntity);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaService.updateModelNegotiation(updateNegotiationEntity.getIdx(), updateNegotiationEntity);

        // then
        assertThat(negotiationInfo.getAdminModelDTO().getIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(negotiationInfo.getModelNegotiationDesc()).isEqualTo("섭외 수정 테스트");

        // verify
        then(adminNegotiationJpaRepository).should(times(1)).findById(updateNegotiationEntity.getIdx());
        then(adminNegotiationJpaRepository).should(atLeastOnce()).findById(updateNegotiationEntity.getIdx());
        then(adminNegotiationJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델섭외삭제Mockito테스트")
    void 모델섭외삭제Mockito테스트() {
        // when
        Long deleteIdx = adminNegotiationJpaService.deleteModelNegotiation(adminNegotiationEntity.getIdx());

        // then
        assertThat(adminNegotiationEntity.getIdx()).isEqualTo(deleteIdx);
    }
}

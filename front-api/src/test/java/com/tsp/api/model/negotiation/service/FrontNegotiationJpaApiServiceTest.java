package com.tsp.api.model.negotiation.service;

import com.tsp.api.FrontCommonServiceTest;
import com.tsp.api.model.domain.negotiation.FrontNegotiationDTO;
import com.tsp.api.model.domain.negotiation.FrontNegotiationEntity;
import com.tsp.api.model.service.FrontModelJpaRepository;
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
class FrontNegotiationApiServiceTest extends FrontCommonServiceTest {
    @Mock private FrontModelJpaRepository frontModelJpaRepository;
    @Mock private FrontNegotiationJpaRepository frontNegotiationJpaRepository;
    @Mock private FrontNegotiationJpaQueryRepository frontNegotiationJpaQueryRepository;
    @InjectMocks private FrontNegotiationJpaApiService mockFrontNegotiationJpaApiService;
    private final FrontNegotiationJpaApiService frontNegotiationJpaApiService;
    private final EntityManager em;

    @Test
    @DisplayName("모델 섭외 리스트 조회 테스트")
    void 모델섭외리스트조회테스트() {
        // given
        Map<String, Object> negotiationMap = new HashMap<>();
        negotiationMap.put("searchKeyword", "조찬희");
        PageRequest pageRequest = PageRequest.of(0, 100);

        // then
        assertThat(frontNegotiationJpaApiService.findModelNegotiationList(negotiationMap, pageRequest)).isNotEmpty();

        Map<String, Object> lastMonthNegotiationMap = new HashMap<>();
        lastMonthNegotiationMap.put("searchStartTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 1, 0, 0, 0, 0));
        lastMonthNegotiationMap.put("searchEndTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 30, 23, 59, 59));
        PageRequest pageRequest2 = PageRequest.of(0, 100);

        // then
        assertThat(frontNegotiationJpaApiService.findModelNegotiationList(negotiationMap, pageRequest2)).isNotEmpty();

        Map<String, Object> currentNegotiationMap = new HashMap<>();
        currentNegotiationMap.put("searchStartTime", of(now().getYear(), LocalDate.now().getMonth(), 1, 0, 0, 0, 0));
        currentNegotiationMap.put("searchEndTime", of(now().getYear(), LocalDate.now().getMonth(), 30, 23, 59, 59));
        PageRequest pageRequest3 = PageRequest.of(0, 100);

        // then
        assertThat(frontNegotiationJpaApiService.findModelNegotiationList(negotiationMap, pageRequest3)).isNotEmpty();
    }

    @Test
    @DisplayName("모델 섭외 Mockito 조회 테스트")
    void 모델섭외Mockito조회테스트() {
        // given
        Map<String, Object> negotiationMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<FrontNegotiationDTO> negotiationList = new ArrayList<>();
        negotiationList.add(FrontNegotiationDTO.builder().frontModelDTO(frontModelDTO)
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 첫번째").modelNegotiationDate(now()).build());
        negotiationList.add(FrontNegotiationDTO.builder().frontModelDTO(frontModelDTO)
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 두번째").modelNegotiationDate(now()).build());

        Page<FrontNegotiationDTO> resultNegotiation = new PageImpl<>(negotiationList, pageRequest, negotiationList.size());
        // when
        when(frontNegotiationJpaQueryRepository.findModelNegotiationList(negotiationMap, pageRequest)).thenReturn(resultNegotiation);
        Page<FrontNegotiationDTO> newModelNegotiationList = mockFrontNegotiationJpaApiService.findModelNegotiationList(negotiationMap, pageRequest);
        List<FrontNegotiationDTO> findNegotiationList = newModelNegotiationList.stream().collect(Collectors.toList());

        // then
        assertThat(findNegotiationList.get(0).getIdx()).isEqualTo(negotiationList.get(0).getIdx());
        assertThat(findNegotiationList.get(0).getModelNegotiationDesc()).isEqualTo(negotiationList.get(0).getModelNegotiationDesc());

        // verify
        verify(frontNegotiationJpaQueryRepository, times(1)).findModelNegotiationList(negotiationMap, pageRequest);
        verify(frontNegotiationJpaQueryRepository, atLeastOnce()).findModelNegotiationList(negotiationMap, pageRequest);
        verifyNoMoreInteractions(frontNegotiationJpaQueryRepository);

        InOrder inOrder = inOrder(frontNegotiationJpaQueryRepository);
        inOrder.verify(frontNegotiationJpaQueryRepository).findModelNegotiationList(negotiationMap, pageRequest);
    }

    @Test
    @DisplayName("모델 섭외 BDD 조회 테스트")
    void 모델섭외BDD조회테스트() {
        // given
        Map<String, Object> negotiationMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<FrontNegotiationDTO> negotiationList = new ArrayList<>();
        negotiationList.add(FrontNegotiationDTO.builder().frontModelDTO(frontModelDTO)
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 첫번째").modelNegotiationDate(now()).build());
        negotiationList.add(FrontNegotiationDTO.builder().frontModelDTO(frontModelDTO)
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 두번째").modelNegotiationDate(now()).build());

        Page<FrontNegotiationDTO> resultNegotiation = new PageImpl<>(negotiationList, pageRequest, negotiationList.size());
        // when
        given(frontNegotiationJpaQueryRepository.findModelNegotiationList(negotiationMap, pageRequest)).willReturn(resultNegotiation);
        Page<FrontNegotiationDTO> newModelNegotiationList = mockFrontNegotiationJpaApiService.findModelNegotiationList(negotiationMap, pageRequest);
        List<FrontNegotiationDTO> findNegotiationList = newModelNegotiationList.stream().collect(Collectors.toList());

        // then
        assertThat(findNegotiationList.get(0).getIdx()).isEqualTo(negotiationList.get(0).getIdx());
        assertThat(findNegotiationList.get(0).getModelNegotiationDesc()).isEqualTo(negotiationList.get(0).getModelNegotiationDesc());

        // verify
        then(frontNegotiationJpaQueryRepository).should(times(1)).findModelNegotiationList(negotiationMap, pageRequest);
        then(frontNegotiationJpaQueryRepository).should(atLeastOnce()).findModelNegotiationList(negotiationMap, pageRequest);
        then(frontNegotiationJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델섭외등록Mockito테스트")
    void 모델섭외등록Mockito테스트() {
        // when
        when(frontModelJpaRepository.findById(frontModelEntity.getIdx())).thenReturn(Optional.ofNullable(frontModelEntity));
        when(frontNegotiationJpaRepository.save(frontNegotiationEntity)).thenReturn(frontNegotiationEntity);
        FrontNegotiationDTO negotiationInfo = mockFrontNegotiationJpaApiService.insertModelNegotiation(frontModelEntity.getIdx(), frontNegotiationEntity);

        // then
        assertThat(negotiationInfo.getFrontModelDTO().getIdx()).isEqualTo(frontModelEntity.getIdx());
        assertThat(negotiationInfo.getModelKorName()).isEqualTo(frontModelEntity.getModelKorName());
        assertThat(negotiationInfo.getModelNegotiationDesc()).isNotNull();

        // verify
        verify(frontModelJpaRepository, times(1)).findById(frontModelEntity.getIdx());
        verify(frontModelJpaRepository, atLeastOnce()).findById(frontModelEntity.getIdx());
        verifyNoMoreInteractions(frontModelJpaRepository);

        InOrder inOrder = inOrder(frontModelJpaRepository);
        inOrder.verify(frontModelJpaRepository).findById(frontModelEntity.getIdx());
    }

    @Test
    @DisplayName("모델섭외등록BDD테스트")
    void 모델섭외등록BDD테스트() {
        // when
        given(frontModelJpaRepository.findById(frontModelEntity.getIdx())).willReturn(Optional.ofNullable(frontModelEntity));
        given(frontNegotiationJpaRepository.save(frontNegotiationEntity)).willReturn(frontNegotiationEntity);
        FrontNegotiationDTO negotiationInfo = mockFrontNegotiationJpaApiService.insertModelNegotiation(frontModelEntity.getIdx(), frontNegotiationEntity);

        // then
        assertThat(negotiationInfo.getFrontModelDTO().getIdx()).isEqualTo(frontModelEntity.getIdx());
        assertThat(negotiationInfo.getModelKorName()).isEqualTo(frontModelEntity.getModelKorName());
        assertThat(negotiationInfo.getModelNegotiationDesc()).isNotNull();

        // verify
        then(frontModelJpaRepository).should(times(1)).findById(frontModelEntity.getIdx());
        then(frontModelJpaRepository).should(atLeastOnce()).findById(frontModelEntity.getIdx());
        then(frontModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델섭외수정Mockito테스트")
    void 모델섭외수정Mockito테스트() {
        // given
        FrontNegotiationEntity updateNegotiationEntity = FrontNegotiationEntity.builder()
                .idx(frontNegotiationEntity.getIdx())
                .frontModelEntity(frontModelEntity)
                .modelKorName(frontModelEntity.getModelKorName())
                .modelNegotiationDesc("섭외 수정 테스트")
                .modelNegotiationDate(now())
                .name("조찬희")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        // when
        when(frontNegotiationJpaRepository.findById(updateNegotiationEntity.getIdx())).thenReturn(Optional.of(updateNegotiationEntity));
        when(frontNegotiationJpaRepository.save(updateNegotiationEntity)).thenReturn(updateNegotiationEntity);
        FrontNegotiationDTO negotiationInfo = mockFrontNegotiationJpaApiService.updateModelNegotiation(updateNegotiationEntity.getIdx(), updateNegotiationEntity);

        // then
        assertThat(negotiationInfo.getFrontModelDTO().getIdx()).isEqualTo(frontModelEntity.getIdx());
        assertThat(negotiationInfo.getModelNegotiationDesc()).isEqualTo(updateNegotiationEntity.getModelNegotiationDesc());

        // verify
        verify(frontNegotiationJpaRepository, times(1)).findById(updateNegotiationEntity.getIdx());
        verify(frontNegotiationJpaRepository, atLeastOnce()).findById(updateNegotiationEntity.getIdx());

        InOrder inOrder = inOrder(frontNegotiationJpaRepository);
        inOrder.verify(frontNegotiationJpaRepository).findById(updateNegotiationEntity.getIdx());
    }

    @Test
    @DisplayName("모델섭외수정BDD테스트")
    void 모델섭외수정BDD테스트() {
        // given
        FrontNegotiationEntity updateNegotiationEntity = FrontNegotiationEntity.builder()
                .idx(frontNegotiationEntity.getIdx())
                .frontModelEntity(frontModelEntity)
                .modelKorName(frontModelEntity.getModelKorName())
                .modelNegotiationDesc("섭외 수정 테스트")
                .modelNegotiationDate(now())
                .name("조찬희")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        // when
        given(frontNegotiationJpaRepository.findById(updateNegotiationEntity.getIdx())).willReturn(Optional.of(updateNegotiationEntity));
        given(frontNegotiationJpaRepository.save(updateNegotiationEntity)).willReturn(updateNegotiationEntity);
        FrontNegotiationDTO negotiationInfo = mockFrontNegotiationJpaApiService.updateModelNegotiation(updateNegotiationEntity.getIdx(), updateNegotiationEntity);

        // then
        assertThat(negotiationInfo.getFrontModelDTO().getIdx()).isEqualTo(frontModelEntity.getIdx());
        assertThat(negotiationInfo.getModelNegotiationDesc()).isEqualTo(updateNegotiationEntity.getModelNegotiationDesc());

        // verify
        then(frontNegotiationJpaRepository).should(times(1)).findById(updateNegotiationEntity.getIdx());
        then(frontNegotiationJpaRepository).should(atLeastOnce()).findById(updateNegotiationEntity.getIdx());
    }

    @Test
    @DisplayName("모델섭외삭제테스트")
    void 모델섭외삭제테스트() {
        // given
        Long deleteIdx = frontNegotiationJpaApiService.deleteModelNegotiation(frontNegotiationEntity.getIdx());

        // then
        assertThat(deleteIdx).isNotNull();
    }
}

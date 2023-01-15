package com.tsp.new_tsp_front.api.model.negotiation.service.impl;

import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyDTO;
import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyEntity;
import com.tsp.new_tsp_front.api.model.domain.CareerJson;
import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import com.tsp.new_tsp_front.api.model.domain.negotiation.FrontNegotiationDTO;
import com.tsp.new_tsp_front.api.model.domain.negotiation.FrontNegotiationEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

@Slf4j
@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("모델 섭외 Repository Test")
class FrontNegotiationJpaQueryRepositoryTest {
    @Mock
    private FrontNegotiationJpaQueryRepository mockFrontNegotiationJpaQueryRepository;
    private final FrontNegotiationJpaQueryRepository frontNegotiationJpaQueryRepository;
    private final EntityManager em;

    private FrontModelEntity frontModelEntity;
    private FrontModelDTO frontModelDTO;
    private FrontNegotiationEntity frontNegotiationEntity;
    private FrontNegotiationDTO frontNegotiationDTO;
    private FrontAgencyEntity frontAgencyEntity;
    private FrontAgencyDTO frontAgencyDTO;

    void createModelAndNegotiation() {
        frontAgencyEntity = FrontAgencyEntity.builder()
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

        frontAgencyDTO = FrontAgencyEntity.toDto(frontAgencyEntity);

        ArrayList<CareerJson> careerList = new ArrayList<>();
        careerList.add(new CareerJson("title","txt"));

        frontModelEntity = FrontModelEntity.builder()
                .categoryCd(1)
                .categoryAge(2)
                .agencyIdx(frontAgencyEntity.getIdx())
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .modelFavoriteCount(1)
                .newYn("N")
                .frontAgencyEntity(frontAgencyEntity)
                .modelViewCount(1)
                .visible("Y")
                .build();

        em.persist(frontModelEntity);

        frontModelDTO = FrontModelEntity.toDto(frontModelEntity);

        frontNegotiationEntity = FrontNegotiationEntity.builder()
                .frontModelEntity(frontModelEntity)
                .modelKorName(frontModelEntity.getModelKorName())
                .modelNegotiationDesc("영화 프로젝트 참여")
                .modelNegotiationDate(now())
                .name("조찬희")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        frontNegotiationDTO = FrontNegotiationEntity.toDto(frontNegotiationEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createModelAndNegotiation();
    }

    @Test
    @DisplayName("모델 섭외 리스트 조회 테스트")
    void 모델섭외리스트조회테스트() {
        // given
        Map<String, Object> negotiationMap = new HashMap<>();
        negotiationMap.put("searchKeyword", "김예영");
        PageRequest pageRequest = PageRequest.of(1, 100);

        // then
        assertThat(frontNegotiationJpaQueryRepository.findModelNegotiationList(negotiationMap, pageRequest)).isNotEmpty();

        Map<String, Object> lastMonthNegotiationMap = new HashMap<>();
        lastMonthNegotiationMap.put("searchStartTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 1, 0, 0, 0, 0));
        lastMonthNegotiationMap.put("searchEndTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 30, 23, 59, 59));
        PageRequest pageRequest2 = PageRequest.of(1, 100);

        // then
        assertThat(frontNegotiationJpaQueryRepository.findModelNegotiationList(negotiationMap, pageRequest2)).isNotEmpty();

        Map<String, Object> currentNegotiationMap = new HashMap<>();
        currentNegotiationMap.put("searchStartTime", of(now().getYear(), LocalDate.now().getMonth(), 1, 0, 0, 0, 0));
        currentNegotiationMap.put("searchEndTime", of(now().getYear(), LocalDate.now().getMonth(), 30, 23, 59, 59));
        PageRequest pageRequest3 = PageRequest.of(1, 100);

        // then
        assertThat(frontNegotiationJpaQueryRepository.findModelNegotiationList(negotiationMap, pageRequest3)).isNotEmpty();
    }

    @Test
    @DisplayName("모델 섭외 Mockito 조회 테스트")
    void 모델섭외Mockito조회테스트() {
        // given
        Map<String, Object> negotiationMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontNegotiationDTO> negotiationList = new ArrayList<>();
        negotiationList.add(FrontNegotiationDTO.builder().modelIdx(frontModelEntity.getIdx())
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 첫번째").modelNegotiationDate(now()).build());
        negotiationList.add(FrontNegotiationDTO.builder().modelIdx(frontModelEntity.getIdx())
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 두번째").modelNegotiationDate(now()).build());

        Page<FrontNegotiationDTO> resultNegotiation = new PageImpl<>(negotiationList, pageRequest, negotiationList.size());
        // when
        when(mockFrontNegotiationJpaQueryRepository.findModelNegotiationList(negotiationMap, pageRequest)).thenReturn(resultNegotiation);
        Page<FrontNegotiationDTO> newModelNegotiationList = mockFrontNegotiationJpaQueryRepository.findModelNegotiationList(negotiationMap, pageRequest);
        List<FrontNegotiationDTO> findNegotiationList = newModelNegotiationList.stream().collect(Collectors.toList());

        // then
        assertThat(findNegotiationList.get(0).getIdx()).isEqualTo(negotiationList.get(0).getIdx());
        assertThat(findNegotiationList.get(0).getModelNegotiationDesc()).isEqualTo(negotiationList.get(0).getModelNegotiationDesc());

        // verify
        verify(mockFrontNegotiationJpaQueryRepository, times(1)).findModelNegotiationList(negotiationMap, pageRequest);
        verify(mockFrontNegotiationJpaQueryRepository, atLeastOnce()).findModelNegotiationList(negotiationMap, pageRequest);
        verifyNoMoreInteractions(mockFrontNegotiationJpaQueryRepository);

        InOrder inOrder = inOrder(mockFrontNegotiationJpaQueryRepository);
        inOrder.verify(mockFrontNegotiationJpaQueryRepository).findModelNegotiationList(negotiationMap, pageRequest);
    }

    @Test
    @DisplayName("모델 섭외 BDD 조회 테스트")
    void 모델섭외BDD조회테스트() {
        // given
        Map<String, Object> negotiationMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontNegotiationDTO> negotiationList = new ArrayList<>();
        negotiationList.add(FrontNegotiationDTO.builder().modelIdx(frontModelEntity.getIdx())
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 첫번째").modelNegotiationDate(now()).build());
        negotiationList.add(FrontNegotiationDTO.builder().modelIdx(frontModelEntity.getIdx())
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 두번째").modelNegotiationDate(now()).build());

        Page<FrontNegotiationDTO> resultNegotiation = new PageImpl<>(negotiationList, pageRequest, negotiationList.size());
        // when
        given(mockFrontNegotiationJpaQueryRepository.findModelNegotiationList(negotiationMap, pageRequest)).willReturn(resultNegotiation);
        Page<FrontNegotiationDTO> newModelNegotiationList = mockFrontNegotiationJpaQueryRepository.findModelNegotiationList(negotiationMap, pageRequest);
        List<FrontNegotiationDTO> findNegotiationList = newModelNegotiationList.stream().collect(Collectors.toList());

        // then
        assertThat(findNegotiationList.get(0).getIdx()).isEqualTo(negotiationList.get(0).getIdx());
        assertThat(findNegotiationList.get(0).getModelNegotiationDesc()).isEqualTo(negotiationList.get(0).getModelNegotiationDesc());

        // verify
        then(mockFrontNegotiationJpaQueryRepository).should(times(1)).findModelNegotiationList(negotiationMap, pageRequest);
        then(mockFrontNegotiationJpaQueryRepository).should(atLeastOnce()).findModelNegotiationList(negotiationMap, pageRequest);
        then(mockFrontNegotiationJpaQueryRepository).shouldHaveNoMoreInteractions();
    }
}
package com.tsp.new_tsp_front.api.agency.service.impl;

import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyDTO;
import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyEntity;
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
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.api.agency.service.impl.AgencyMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-local.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("Agency Repository Test")
class FrontAgencyJpaRepositoryTest {
    @Mock private final FrontAgencyJpaRepository mockFrontAgencyJpaRepository;
    private final FrontAgencyJpaRepository frontAgencyJpaRepository;
    private FrontAgencyEntity frontAgencyEntity;
    private FrontAgencyDTO frontAgencyDTO;
    private final EntityManager em;

    private void createAgency() {
        frontAgencyEntity = FrontAgencyEntity.builder()
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

        frontAgencyDTO = INSTANCE.toDto(frontAgencyEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createAgency();
    }

    @Test
    @DisplayName("Agency 리스트 조회 테스트")
    void Agency리스트조회테스트() {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        agencyMap.put("jpaStartPage", 0);
        agencyMap.put("size", 3);
        agencyMap.put("searchType", 0);
        agencyMap.put("searchKeyword", "agency");

        // then
        assertThat(frontAgencyJpaRepository.findAgencyList(agencyMap)).isNotEmpty();
    }

    @Test
    @DisplayName("Agency 리스트 조회 Mockito 테스트")
    void Agency리스트조회Mockito테스트() {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        agencyMap.put("jpaStartPage", 1);
        agencyMap.put("size", 3);

        List<FrontAgencyDTO> agencyList = new ArrayList<>();
        agencyList.add(FrontAgencyDTO.builder().idx(1).agencyName("agency").agencyDescription("agency").visible("Y").build());

        // when
        when(mockFrontAgencyJpaRepository.findAgencyList(agencyMap)).thenReturn(agencyList);
        List<FrontAgencyDTO> newAgencyList = mockFrontAgencyJpaRepository.findAgencyList(agencyMap);

        // then
        assertThat(newAgencyList.get(0).getIdx()).isEqualTo(agencyList.get(0).getIdx());
        assertThat(newAgencyList.get(0).getAgencyName()).isEqualTo(agencyList.get(0).getAgencyName());
        assertThat(newAgencyList.get(0).getAgencyDescription()).isEqualTo(agencyList.get(0).getAgencyDescription());

        // verify
        verify(mockFrontAgencyJpaRepository, times(1)).findAgencyList(agencyMap);
        verify(mockFrontAgencyJpaRepository, atLeastOnce()).findAgencyList(agencyMap);
        verifyNoMoreInteractions(mockFrontAgencyJpaRepository);

        InOrder inOrder = inOrder(mockFrontAgencyJpaRepository);
        inOrder.verify(mockFrontAgencyJpaRepository).findAgencyList(agencyMap);
    }

    @Test
    @DisplayName("Agency 리스트 조회 BDD 테스트")
    void Agency리스트조회BDD테스트() {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        agencyMap.put("jpaStartPage", 1);
        agencyMap.put("size", 3);

        List<FrontAgencyDTO> agencyList = new ArrayList<>();
        agencyList.add(FrontAgencyDTO.builder().idx(1).agencyName("agency").agencyDescription("agency").visible("Y").build());

        // when
        given(mockFrontAgencyJpaRepository.findAgencyList(agencyMap)).willReturn(agencyList);
        List<FrontAgencyDTO> newAgencyList = mockFrontAgencyJpaRepository.findAgencyList(agencyMap);

        // then
        assertThat(newAgencyList.get(0).getIdx()).isEqualTo(agencyList.get(0).getIdx());
        assertThat(newAgencyList.get(0).getAgencyName()).isEqualTo(agencyList.get(0).getAgencyName());
        assertThat(newAgencyList.get(0).getAgencyDescription()).isEqualTo(agencyList.get(0).getAgencyDescription());

        // verify
        then(mockFrontAgencyJpaRepository).should(times(1)).findAgencyList(agencyMap);
        then(mockFrontAgencyJpaRepository).should(atLeastOnce()).findAgencyList(agencyMap);
        then(mockFrontAgencyJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("Agency상세Mockito조회테스트")
    void Agency상세Mockito조회테스트() {
        // given
        FrontAgencyEntity frontAgencyEntity = FrontAgencyEntity.builder()
                .idx(1)
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

        frontAgencyDTO = AgencyMapper.INSTANCE.toDto(frontAgencyEntity);

        // when
        when(mockFrontAgencyJpaRepository.findOneAgency(frontAgencyEntity)).thenReturn(frontAgencyDTO);
        FrontAgencyDTO agencyInfo = mockFrontAgencyJpaRepository.findOneAgency(frontAgencyEntity);

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(1);
        assertThat(agencyInfo.getAgencyName()).isEqualTo("agency");
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo("agency");
        assertThat(agencyInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockFrontAgencyJpaRepository, times(1)).findOneAgency(frontAgencyEntity);
        verify(mockFrontAgencyJpaRepository, atLeastOnce()).findOneAgency(frontAgencyEntity);
        verifyNoMoreInteractions(mockFrontAgencyJpaRepository);

        InOrder inOrder = inOrder(mockFrontAgencyJpaRepository);
        inOrder.verify(mockFrontAgencyJpaRepository).findOneAgency(frontAgencyEntity);
    }

    @Test
    @DisplayName("Agency상세BDD조회테스트")
    void Agency상세BDD조회테스트() {
        // given
        FrontAgencyEntity frontAgencyEntity = FrontAgencyEntity.builder()
                .idx(1)
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

        frontAgencyDTO = AgencyMapper.INSTANCE.toDto(frontAgencyEntity);

        // when
        given(mockFrontAgencyJpaRepository.findOneAgency(frontAgencyEntity)).willReturn(frontAgencyDTO);
        FrontAgencyDTO agencyInfo = mockFrontAgencyJpaRepository.findOneAgency(frontAgencyEntity);

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(1);
        assertThat(agencyInfo.getAgencyName()).isEqualTo("agency");
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo("agency");
        assertThat(agencyInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockFrontAgencyJpaRepository).should(times(1)).findOneAgency(frontAgencyEntity);
        then(mockFrontAgencyJpaRepository).should(atLeastOnce()).findOneAgency(frontAgencyEntity);
        then(mockFrontAgencyJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("Agency 좋아요 갯수 조회 Mockito 테스트")
    void Agency좋아요갯수조회Mockito테스트() {
        // given
        em.persist(frontAgencyEntity);
        frontAgencyDTO = AgencyMapper.INSTANCE.toDto(frontAgencyEntity);

        // when
        when(mockFrontAgencyJpaRepository.findOneAgency(frontAgencyEntity)).thenReturn(frontAgencyDTO);
        FrontAgencyDTO agencyInfo = mockFrontAgencyJpaRepository.findOneAgency(frontAgencyEntity);

        // then
        assertThat(agencyInfo.getFavoriteCount()).isEqualTo(frontAgencyEntity.getFavoriteCount());

        // verify
        verify(mockFrontAgencyJpaRepository, times(1)).findOneAgency(frontAgencyEntity);
        verify(mockFrontAgencyJpaRepository, atLeastOnce()).findOneAgency(frontAgencyEntity);
        verifyNoMoreInteractions(mockFrontAgencyJpaRepository);
    }

    @Test
    @DisplayName("모델 좋아요 갯수 조회 BDD 테스트")
    void 모델좋아요갯수조회BDD테스트() {
        em.persist(frontAgencyEntity);
        frontAgencyDTO = AgencyMapper.INSTANCE.toDto(frontAgencyEntity);

        // when
        given(mockFrontAgencyJpaRepository.findOneAgency(frontAgencyEntity)).willReturn(frontAgencyDTO);
        FrontAgencyDTO agencyInfo = mockFrontAgencyJpaRepository.findOneAgency(frontAgencyEntity);

        // then
        assertThat(agencyInfo.getFavoriteCount()).isEqualTo(frontAgencyEntity.getFavoriteCount());

        // verify
        then(mockFrontAgencyJpaRepository).should(times(1)).findOneAgency(frontAgencyEntity);
        then(mockFrontAgencyJpaRepository).should(atLeastOnce()).findOneAgency(frontAgencyEntity);
        then(mockFrontAgencyJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 좋아요 Mockito 테스트")
    void 모델좋아요Mockito테스트() {
        // given
        em.persist(frontAgencyEntity);
        frontAgencyDTO = AgencyMapper.INSTANCE.toDto(frontAgencyEntity);

        Integer favoriteCount = frontAgencyJpaRepository.favoriteAgency(frontAgencyEntity);

        // when
        when(mockFrontAgencyJpaRepository.favoriteAgency(frontAgencyEntity)).thenReturn(favoriteCount);
        Integer newFavoriteCount = mockFrontAgencyJpaRepository.favoriteAgencyCount(frontAgencyEntity.getIdx());

        // then
        assertThat(newFavoriteCount).isEqualTo(favoriteCount);

        // verify
        verify(mockFrontAgencyJpaRepository, times(1)).favoriteAgency(frontAgencyEntity);
        verify(mockFrontAgencyJpaRepository, atLeastOnce()).favoriteAgency(frontAgencyEntity);
        verifyNoMoreInteractions(mockFrontAgencyJpaRepository);

        InOrder inOrder = inOrder(mockFrontAgencyJpaRepository);
        inOrder.verify(mockFrontAgencyJpaRepository).favoriteAgency(frontAgencyEntity);
    }

    @Test
    @DisplayName("모델 좋아요 BDD 테스트")
    void 모델좋아요BDD테스트() {
        // given
        em.persist(frontAgencyEntity);
        frontAgencyDTO = AgencyMapper.INSTANCE.toDto(frontAgencyEntity);

        Integer favoriteCount = frontAgencyJpaRepository.favoriteAgency(frontAgencyEntity);

        // when
        given(mockFrontAgencyJpaRepository.favoriteAgency(frontAgencyEntity)).willReturn(favoriteCount);
        Integer newFavoriteCount = mockFrontAgencyJpaRepository.favoriteAgencyCount(frontAgencyEntity.getIdx());

        // then
        assertThat(newFavoriteCount).isEqualTo(favoriteCount);

        // verify
        then(mockFrontAgencyJpaRepository).should(times(1)).favoriteAgency(frontAgencyEntity);
        then(mockFrontAgencyJpaRepository).should(atLeastOnce()).favoriteAgency(frontAgencyEntity);
        then(mockFrontAgencyJpaRepository).shouldHaveNoMoreInteractions();
    }
}
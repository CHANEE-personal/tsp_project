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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
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
class FrontAgencyJpaQueryRepositoryTest {
    @Mock private final FrontAgencyJpaQueryRepository mockFrontAgencyJpaQueryRepository;
    private final FrontAgencyJpaQueryRepository frontAgencyJpaQueryRepository;
    private FrontAgencyEntity frontAgencyEntity;
    private FrontAgencyDTO frontAgencyDTO;
    private final EntityManager em;

    private void createAgency() {
        frontAgencyEntity = FrontAgencyEntity.builder()
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

        frontAgencyDTO = FrontAgencyEntity.toDto(frontAgencyEntity);
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
        agencyMap.put("searchType", 0);
        agencyMap.put("searchKeyword", "agency");
        PageRequest pageRequest = PageRequest.of(1, 3);

        // then
        assertThat(frontAgencyJpaQueryRepository.findAgencyList(agencyMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("Agency 리스트 조회 Mockito 테스트")
    void Agency리스트조회Mockito테스트() {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontAgencyDTO> agencyList = new ArrayList<>();
        agencyList.add(FrontAgencyDTO.builder().idx(1L).agencyName("agency").agencyDescription("agency").visible("Y").build());

        Page<FrontAgencyDTO> resultAgency = new PageImpl<>(agencyList, pageRequest, agencyList.size());
        // when
        when(mockFrontAgencyJpaQueryRepository.findAgencyList(agencyMap, pageRequest)).thenReturn(resultAgency);
        Page<FrontAgencyDTO> newAgencyList = mockFrontAgencyJpaQueryRepository.findAgencyList(agencyMap, pageRequest);
        List<FrontAgencyDTO> findAgencyList = newAgencyList.stream().collect(Collectors.toList());

        // then
        assertThat(findAgencyList.get(0).getIdx()).isEqualTo(agencyList.get(0).getIdx());
        assertThat(findAgencyList.get(0).getAgencyName()).isEqualTo(agencyList.get(0).getAgencyName());
        assertThat(findAgencyList.get(0).getAgencyDescription()).isEqualTo(agencyList.get(0).getAgencyDescription());

        // verify
        verify(mockFrontAgencyJpaQueryRepository, times(1)).findAgencyList(agencyMap, pageRequest);
        verify(mockFrontAgencyJpaQueryRepository, atLeastOnce()).findAgencyList(agencyMap, pageRequest);
        verifyNoMoreInteractions(mockFrontAgencyJpaQueryRepository);

        InOrder inOrder = inOrder(mockFrontAgencyJpaQueryRepository);
        inOrder.verify(mockFrontAgencyJpaQueryRepository).findAgencyList(agencyMap, pageRequest);
    }

    @Test
    @DisplayName("Agency 리스트 조회 BDD 테스트")
    void Agency리스트조회BDD테스트() {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontAgencyDTO> agencyList = new ArrayList<>();
        agencyList.add(FrontAgencyDTO.builder().idx(1L).agencyName("agency").agencyDescription("agency").visible("Y").build());

        Page<FrontAgencyDTO> resultAgency = new PageImpl<>(agencyList, pageRequest, agencyList.size());
        // when
        given(mockFrontAgencyJpaQueryRepository.findAgencyList(agencyMap, pageRequest)).willReturn(resultAgency);
        Page<FrontAgencyDTO> newAgencyList = mockFrontAgencyJpaQueryRepository.findAgencyList(agencyMap, pageRequest);
        List<FrontAgencyDTO> findAgencyList = newAgencyList.stream().collect(Collectors.toList());

        // then
        assertThat(findAgencyList.get(0).getIdx()).isEqualTo(agencyList.get(0).getIdx());
        assertThat(findAgencyList.get(0).getAgencyName()).isEqualTo(agencyList.get(0).getAgencyName());
        assertThat(findAgencyList.get(0).getAgencyDescription()).isEqualTo(agencyList.get(0).getAgencyDescription());

        // verify
        then(mockFrontAgencyJpaQueryRepository).should(times(1)).findAgencyList(agencyMap, pageRequest);
        then(mockFrontAgencyJpaQueryRepository).should(atLeastOnce()).findAgencyList(agencyMap, pageRequest);
        then(mockFrontAgencyJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("소속사 좋아요 Mockito 테스트")
    void 소속사좋아요Mockito테스트() {
        // given
        em.persist(frontAgencyEntity);
        frontAgencyDTO = FrontAgencyEntity.toDto(frontAgencyEntity);

        Integer favoriteCount = frontAgencyJpaQueryRepository.favoriteAgency(frontAgencyDTO.getIdx());

        // when
        when(mockFrontAgencyJpaQueryRepository.favoriteAgency(frontAgencyDTO.getIdx())).thenReturn(favoriteCount);
        Integer newFavoriteCount = mockFrontAgencyJpaQueryRepository.favoriteAgency(frontAgencyDTO.getIdx());

        // then
        assertThat(newFavoriteCount).isEqualTo(favoriteCount);

        // verify
        verify(mockFrontAgencyJpaQueryRepository, times(1)).favoriteAgency(frontAgencyDTO.getIdx());
        verify(mockFrontAgencyJpaQueryRepository, atLeastOnce()).favoriteAgency(frontAgencyDTO.getIdx());
        verifyNoMoreInteractions(mockFrontAgencyJpaQueryRepository);

        InOrder inOrder = inOrder(mockFrontAgencyJpaQueryRepository);
        inOrder.verify(mockFrontAgencyJpaQueryRepository).favoriteAgency(frontAgencyDTO.getIdx());
    }

    @Test
    @DisplayName("소속사 좋아요 BDD 테스트")
    void 소속사좋아요BDD테스트() {
        // given
        em.persist(frontAgencyEntity);
        frontAgencyDTO = FrontAgencyEntity.toDto(frontAgencyEntity);

        Integer favoriteCount = frontAgencyJpaQueryRepository.favoriteAgency(frontAgencyDTO.getIdx());

        // when
        given(mockFrontAgencyJpaQueryRepository.favoriteAgency(frontAgencyDTO.getIdx())).willReturn(favoriteCount);
        Integer newFavoriteCount = mockFrontAgencyJpaQueryRepository.favoriteAgency(frontAgencyDTO.getIdx());

        // then
        assertThat(newFavoriteCount).isEqualTo(favoriteCount);

        // verify
        then(mockFrontAgencyJpaQueryRepository).should(times(1)).favoriteAgency(frontAgencyDTO.getIdx());
        then(mockFrontAgencyJpaQueryRepository).should(atLeastOnce()).favoriteAgency(frontAgencyDTO.getIdx());
        then(mockFrontAgencyJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 소속사 상세 조회 테스트")
    void 이전or다음소속사상세조회테스트() {
        // given
        frontAgencyEntity = FrontAgencyEntity.builder().idx(2L).build();

        // when

        // 이전 소속사
        assertThat(frontAgencyJpaQueryRepository.findPrevOneAgency(frontAgencyEntity.getIdx()).getIdx()).isEqualTo(1);
        // 다음 소속사
        assertThat(frontAgencyJpaQueryRepository.findNextOneAgency(frontAgencyEntity.getIdx()).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 소속사 상세 조회 Mockito 테스트")
    void 이전소속사상세조회Mockito테스트() {
        // given
        frontAgencyEntity = FrontAgencyEntity.builder().idx(2L).build();

        // when
        frontAgencyDTO = frontAgencyJpaQueryRepository.findPrevOneAgency(frontAgencyEntity.getIdx());

        when(mockFrontAgencyJpaQueryRepository.findPrevOneAgency(frontAgencyEntity.getIdx())).thenReturn(frontAgencyDTO);
        FrontAgencyDTO agencyInfo = mockFrontAgencyJpaQueryRepository.findPrevOneAgency(frontAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockFrontAgencyJpaQueryRepository, times(1)).findPrevOneAgency(frontAgencyEntity.getIdx());
        verify(mockFrontAgencyJpaQueryRepository, atLeastOnce()).findPrevOneAgency(frontAgencyEntity.getIdx());
        verifyNoMoreInteractions(mockFrontAgencyJpaQueryRepository);

        InOrder inOrder = inOrder(mockFrontAgencyJpaQueryRepository);
        inOrder.verify(mockFrontAgencyJpaQueryRepository).findPrevOneAgency(frontAgencyEntity.getIdx());
    }

    @Test
    @DisplayName("이전 소속사 상세 조회 BDD 테스트")
    void 이전소속사상세조회BDD테스트() {
        // given
        frontAgencyEntity = FrontAgencyEntity.builder().idx(2L).build();

        // when
        frontAgencyDTO = frontAgencyJpaQueryRepository.findPrevOneAgency(frontAgencyEntity.getIdx());

        given(mockFrontAgencyJpaQueryRepository.findPrevOneAgency(frontAgencyEntity.getIdx())).willReturn(frontAgencyDTO);
        FrontAgencyDTO agencyInfo = mockFrontAgencyJpaQueryRepository.findPrevOneAgency(frontAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockFrontAgencyJpaQueryRepository).should(times(1)).findPrevOneAgency(frontAgencyEntity.getIdx());
        then(mockFrontAgencyJpaQueryRepository).should(atLeastOnce()).findPrevOneAgency(frontAgencyEntity.getIdx());
        then(mockFrontAgencyJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 소속사 상세 조회 Mockito 테스트")
    void 다음소속사상세조회Mockito테스트() {
        // given
        frontAgencyEntity = FrontAgencyEntity.builder().idx(2L).build();

        // when
        frontAgencyDTO = frontAgencyJpaQueryRepository.findNextOneAgency(frontAgencyEntity.getIdx());

        when(mockFrontAgencyJpaQueryRepository.findNextOneAgency(frontAgencyEntity.getIdx())).thenReturn(frontAgencyDTO);
        FrontAgencyDTO agencyInfo = mockFrontAgencyJpaQueryRepository.findNextOneAgency(frontAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockFrontAgencyJpaQueryRepository, times(1)).findNextOneAgency(frontAgencyEntity.getIdx());
        verify(mockFrontAgencyJpaQueryRepository, atLeastOnce()).findNextOneAgency(frontAgencyEntity.getIdx());
        verifyNoMoreInteractions(mockFrontAgencyJpaQueryRepository);

        InOrder inOrder = inOrder(mockFrontAgencyJpaQueryRepository);
        inOrder.verify(mockFrontAgencyJpaQueryRepository).findNextOneAgency(frontAgencyEntity.getIdx());
    }

    @Test
    @DisplayName("다음 소속사 상세 조회 BDD 테스트")
    void 다음소속사상세조회BDD테스트() {
        // given
        frontAgencyEntity = FrontAgencyEntity.builder().idx(2L).build();

        // when
        frontAgencyDTO = frontAgencyJpaQueryRepository.findNextOneAgency(frontAgencyEntity.getIdx());

        given(mockFrontAgencyJpaQueryRepository.findNextOneAgency(frontAgencyEntity.getIdx())).willReturn(frontAgencyDTO);
        FrontAgencyDTO agencyInfo = mockFrontAgencyJpaQueryRepository.findNextOneAgency(frontAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockFrontAgencyJpaQueryRepository).should(times(1)).findNextOneAgency(frontAgencyEntity.getIdx());
        then(mockFrontAgencyJpaQueryRepository).should(atLeastOnce()).findNextOneAgency(frontAgencyEntity.getIdx());
        then(mockFrontAgencyJpaQueryRepository).shouldHaveNoMoreInteractions();
    }
}
package com.tsp.api.model.agency.service;

import com.tsp.api.FrontCommonServiceTest;
import com.tsp.api.model.domain.agency.FrontAgencyDTO;
import com.tsp.api.model.domain.agency.FrontAgencyEntity;
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

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
@DisplayName("Agency Service Test")
class FrontAgencyJpaServiceTest extends FrontCommonServiceTest {
    @Mock private FrontAgencyJpaRepository frontAgencyJpaRepository;
    @Mock private FrontAgencyJpaQueryRepository frontAgencyJpaQueryRepository;
    @InjectMocks private FrontAgencyJpaService mockFrontAgencyJpaService;
    private final FrontAgencyJpaService frontAgencyJpaService;
    private final EntityManager em;

    @Test
    @DisplayName("Agency리스트조회Mockito테스트")
    void Agency리스트조회Mockito테스트() {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<FrontAgencyDTO> agencyList = new ArrayList<>();
        agencyList.add(frontAgencyDTO);

        Page<FrontAgencyDTO> resultAgency = new PageImpl<>(agencyList, pageRequest, agencyList.size());
        // when
        when(frontAgencyJpaQueryRepository.findAgencyList(agencyMap, pageRequest)).thenReturn(resultAgency);
        Page<FrontAgencyDTO> newAgencyList = mockFrontAgencyJpaService.findAgencyList(agencyMap, pageRequest);
        List<FrontAgencyDTO> findAgencyList = newAgencyList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findAgencyList).isNotEmpty(),
                () -> assertThat(findAgencyList).hasSize(1)
        );

        assertThat(findAgencyList.get(0).getIdx()).isEqualTo(agencyList.get(0).getIdx());
        assertThat(findAgencyList.get(0).getAgencyName()).isEqualTo(agencyList.get(0).getAgencyName());
        assertThat(findAgencyList.get(0).getAgencyDescription()).isEqualTo(agencyList.get(0).getAgencyDescription());
        assertThat(findAgencyList.get(0).getVisible()).isEqualTo(agencyList.get(0).getVisible());

        // verify
        verify(frontAgencyJpaQueryRepository, times(1)).findAgencyList(agencyMap, pageRequest);
        verify(frontAgencyJpaQueryRepository, atLeastOnce()).findAgencyList(agencyMap, pageRequest);
        verifyNoMoreInteractions(frontAgencyJpaQueryRepository);

        InOrder inOrder = inOrder(frontAgencyJpaQueryRepository);
        inOrder.verify(frontAgencyJpaQueryRepository).findAgencyList(agencyMap, pageRequest);
    }

    @Test
    @DisplayName("Agency리스트조회BDD테스트")
    void Agency리스트조회BDD테스트() {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<FrontAgencyDTO> agencyList = new ArrayList<>();
        agencyList.add(frontAgencyDTO);

        Page<FrontAgencyDTO> resultAgency = new PageImpl<>(agencyList, pageRequest, agencyList.size());
        // when
        given(frontAgencyJpaQueryRepository.findAgencyList(agencyMap, pageRequest)).willReturn(resultAgency);
        Page<FrontAgencyDTO> newAgencyList = mockFrontAgencyJpaService.findAgencyList(agencyMap, pageRequest);
        List<FrontAgencyDTO> findAgencyList = newAgencyList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(agencyList).isNotEmpty(),
                () -> assertThat(agencyList).hasSize(1)
        );

        assertThat(findAgencyList.get(0).getIdx()).isEqualTo(agencyList.get(0).getIdx());
        assertThat(findAgencyList.get(0).getAgencyName()).isEqualTo(agencyList.get(0).getAgencyName());
        assertThat(findAgencyList.get(0).getAgencyDescription()).isEqualTo(agencyList.get(0).getAgencyDescription());
        assertThat(findAgencyList.get(0).getVisible()).isEqualTo(agencyList.get(0).getVisible());

        // verify
        then(frontAgencyJpaQueryRepository).should(times(1)).findAgencyList(agencyMap, pageRequest);
        then(frontAgencyJpaQueryRepository).should(atLeastOnce()).findAgencyList(agencyMap, pageRequest);
        then(frontAgencyJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("Agency상세조회Mockito테스트")
    void Agency상세조회Mockito테스트() {
        // when
        when(frontAgencyJpaRepository.findById(frontAgencyEntity.getIdx())).thenReturn(Optional.ofNullable(frontAgencyEntity));
        FrontAgencyDTO agencyInfo = mockFrontAgencyJpaService.findOneAgency(frontAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(frontAgencyEntity.getIdx());
        assertThat(agencyInfo.getAgencyName()).isEqualTo(frontAgencyEntity.getAgencyName());
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo(frontAgencyEntity.getAgencyDescription());
        assertThat(agencyInfo.getVisible()).isEqualTo(frontAgencyEntity.getVisible());

        // verify
        verify(frontAgencyJpaRepository, times(1)).findById(frontAgencyEntity.getIdx());
        verify(frontAgencyJpaRepository, atLeastOnce()).findById(frontAgencyEntity.getIdx());
        verifyNoMoreInteractions(frontAgencyJpaRepository);

        InOrder inOrder = inOrder(frontAgencyJpaRepository);
        inOrder.verify(frontAgencyJpaRepository).findById(frontAgencyEntity.getIdx());
    }

    @Test
    @DisplayName("Agency상세조회BDD테스트")
    void Agency상세조회BDD테스트() {
        // when
        given(frontAgencyJpaRepository.findById(frontAgencyEntity.getIdx())).willReturn(Optional.ofNullable(frontAgencyEntity));
        FrontAgencyDTO agencyInfo = mockFrontAgencyJpaService.findOneAgency(frontAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(frontAgencyEntity.getIdx());
        assertThat(agencyInfo.getAgencyName()).isEqualTo(frontAgencyEntity.getAgencyName());
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo(frontAgencyEntity.getAgencyDescription());
        assertThat(agencyInfo.getVisible()).isEqualTo(frontAgencyEntity.getVisible());

        // verify
        then(frontAgencyJpaRepository).should(times(1)).findById(frontAgencyEntity.getIdx());
        then(frontAgencyJpaRepository).should(atLeastOnce()).findById(frontAgencyEntity.getIdx());
        then(frontAgencyJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("Agency 좋아요 Mockito 테스트")
    void Agency좋아요Mockito테스트() {
        // when
        when(frontAgencyJpaQueryRepository.favoriteAgency(frontAgencyEntity.getIdx())).thenReturn(frontAgencyEntity.getFavoriteCount() + 1);
        Integer newFavoriteCount = mockFrontAgencyJpaService.favoriteAgency(frontAgencyEntity.getIdx());

        // then
        assertThat(newFavoriteCount).isEqualTo(frontAgencyEntity.getFavoriteCount() + 1);

        // verify
        verify(frontAgencyJpaQueryRepository, times(1)).favoriteAgency(frontAgencyEntity.getIdx());
        verify(frontAgencyJpaQueryRepository, atLeastOnce()).favoriteAgency(frontAgencyEntity.getIdx());
        verifyNoMoreInteractions(frontAgencyJpaQueryRepository);

        InOrder inOrder = inOrder(frontAgencyJpaQueryRepository);
        inOrder.verify(frontAgencyJpaQueryRepository).favoriteAgency(frontAgencyEntity.getIdx());
    }

    @Test
    @DisplayName("Agency 좋아요 BDD 테스트")
    void Agency좋아요BDD테스트() {
        // when
        given(frontAgencyJpaQueryRepository.favoriteAgency(frontAgencyEntity.getIdx())).willReturn(frontAgencyEntity.getFavoriteCount() + 1);
        Integer newFavoriteCount = mockFrontAgencyJpaService.favoriteAgency(frontAgencyEntity.getIdx());

        // then
        assertThat(newFavoriteCount).isEqualTo(frontAgencyEntity.getFavoriteCount() + 1);

        // verify
        then(frontAgencyJpaQueryRepository).should(times(1)).favoriteAgency(frontAgencyEntity.getIdx());
        then(frontAgencyJpaQueryRepository).should(atLeastOnce()).favoriteAgency(frontAgencyEntity.getIdx());
        then(frontAgencyJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 소속사 상세 조회 테스트")
    void 이전or다음소속사상세조회테스트() {
        // given
        frontAgencyEntity = FrontAgencyEntity.builder().idx(2L).build();

        // when
        frontAgencyDTO = frontAgencyJpaService.findOneAgency(frontAgencyEntity.getIdx());

        // 이전 소속사
        assertThat(frontAgencyJpaService.findPrevOneAgency(frontAgencyEntity.getIdx()).getIdx()).isEqualTo(1);
        // 다음 소속사
        assertThat(frontAgencyJpaService.findNextOneAgency(frontAgencyEntity.getIdx()).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 소속사 상세 조회 Mockito 테스트")
    void 이전소속사상세조회Mockito테스트() {
        // given
        frontAgencyEntity = FrontAgencyEntity.builder().idx(2L).build();

        // when
        frontAgencyDTO = frontAgencyJpaService.findPrevOneAgency(frontAgencyEntity.getIdx());

        when(mockFrontAgencyJpaService.findPrevOneAgency(frontAgencyEntity.getIdx())).thenReturn(frontAgencyDTO);
        FrontAgencyDTO agencyInfo = mockFrontAgencyJpaService.findPrevOneAgency(frontAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockFrontAgencyJpaService, times(1)).findPrevOneAgency(frontAgencyEntity.getIdx());
        verify(mockFrontAgencyJpaService, atLeastOnce()).findPrevOneAgency(frontAgencyEntity.getIdx());
        verifyNoMoreInteractions(mockFrontAgencyJpaService);

        InOrder inOrder = inOrder(mockFrontAgencyJpaService);
        inOrder.verify(mockFrontAgencyJpaService).findPrevOneAgency(frontAgencyEntity.getIdx());
    }

    @Test
    @DisplayName("이전 소속사 상세 조회 BDD 테스트")
    void 이전소속사상세조회BDD테스트() {
        // given
        frontAgencyEntity = FrontAgencyEntity.builder().idx(2L).build();

        // when
        frontAgencyDTO = frontAgencyJpaService.findPrevOneAgency(frontAgencyEntity.getIdx());

        given(mockFrontAgencyJpaService.findPrevOneAgency(frontAgencyEntity.getIdx())).willReturn(frontAgencyDTO);
        FrontAgencyDTO agencyInfo = mockFrontAgencyJpaService.findPrevOneAgency(frontAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockFrontAgencyJpaService).should(times(1)).findPrevOneAgency(frontAgencyEntity.getIdx());
        then(mockFrontAgencyJpaService).should(atLeastOnce()).findPrevOneAgency(frontAgencyEntity.getIdx());
        then(mockFrontAgencyJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 소속사 상세 조회 Mockito 테스트")
    void 다음소속사상세조회Mockito테스트() {
        // given
        frontAgencyEntity = FrontAgencyEntity.builder().idx(2L).build();

        // when
        frontAgencyDTO = frontAgencyJpaService.findNextOneAgency(frontAgencyEntity.getIdx());

        when(mockFrontAgencyJpaService.findNextOneAgency(frontAgencyEntity.getIdx())).thenReturn(frontAgencyDTO);
        FrontAgencyDTO agencyInfo = mockFrontAgencyJpaService.findNextOneAgency(frontAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockFrontAgencyJpaService, times(1)).findNextOneAgency(frontAgencyEntity.getIdx());
        verify(mockFrontAgencyJpaService, atLeastOnce()).findNextOneAgency(frontAgencyEntity.getIdx());
        verifyNoMoreInteractions(mockFrontAgencyJpaService);

        InOrder inOrder = inOrder(mockFrontAgencyJpaService);
        inOrder.verify(mockFrontAgencyJpaService).findNextOneAgency(frontAgencyEntity.getIdx());
    }

    @Test
    @DisplayName("다음 소속사 상세 조회 BDD 테스트")
    void 다음소속사상세조회BDD테스트() {
        // given
        frontAgencyEntity = FrontAgencyEntity.builder().idx(2L).build();

        // when
        frontAgencyDTO = frontAgencyJpaService.findNextOneAgency(frontAgencyEntity.getIdx());

        given(mockFrontAgencyJpaService.findNextOneAgency(frontAgencyEntity.getIdx())).willReturn(frontAgencyDTO);
        FrontAgencyDTO agencyInfo = mockFrontAgencyJpaService.findNextOneAgency(frontAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockFrontAgencyJpaService).should(times(1)).findNextOneAgency(frontAgencyEntity.getIdx());
        then(mockFrontAgencyJpaService).should(atLeastOnce()).findNextOneAgency(frontAgencyEntity.getIdx());
        then(mockFrontAgencyJpaService).shouldHaveNoMoreInteractions();
    }
}

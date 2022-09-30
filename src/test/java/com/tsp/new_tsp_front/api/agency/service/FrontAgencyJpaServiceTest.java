package com.tsp.new_tsp_front.api.agency.service;

import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyDTO;
import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyEntity;
import com.tsp.new_tsp_front.api.agency.service.impl.AgencyMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.api.model.service.impl.ModelMapper.INSTANCE;
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
class FrontAgencyJpaServiceTest {
    @Mock private FrontAgencyJpaService mockFrontAgencyJpaService;
    private final FrontAgencyJpaService frontAgencyJpaService;
    private FrontAgencyEntity frontAgencyEntity;
    private FrontAgencyDTO frontAgencyDTO;
    private final EntityManager em;

    @Test
    @DisplayName("Agency리스트조회Mockito테스트")
    void Agency리스트조회Mockito테스트() {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        agencyMap.put("jpaStartPage", 1);
        agencyMap.put("size", 3);

        List<FrontAgencyDTO> returnAgencyList = new ArrayList<>();

        returnAgencyList.add(FrontAgencyDTO.builder().idx(1L).agencyName("Agency테스트").agencyDescription("Agency테스트").visible("Y").build());
        returnAgencyList.add(FrontAgencyDTO.builder().idx(2L).agencyName("agencyTest").agencyDescription("agencyTest").visible("Y").build());

        // when
        when(mockFrontAgencyJpaService.findAgencyList(agencyMap)).thenReturn(returnAgencyList);
        List<FrontAgencyDTO> agencyList = mockFrontAgencyJpaService.findAgencyList(agencyMap);

        // then
        assertAll(
                () -> assertThat(agencyList).isNotEmpty(),
                () -> assertThat(agencyList).hasSize(2)
        );

        assertThat(agencyList.get(0).getIdx()).isEqualTo(returnAgencyList.get(0).getIdx());
        assertThat(agencyList.get(0).getAgencyName()).isEqualTo(returnAgencyList.get(0).getAgencyName());
        assertThat(agencyList.get(0).getAgencyDescription()).isEqualTo(returnAgencyList.get(0).getAgencyDescription());
        assertThat(agencyList.get(0).getVisible()).isEqualTo(returnAgencyList.get(0).getVisible());

        // verify
        verify(mockFrontAgencyJpaService, times(1)).findAgencyList(agencyMap);
        verify(mockFrontAgencyJpaService, atLeastOnce()).findAgencyList(agencyMap);
        verifyNoMoreInteractions(mockFrontAgencyJpaService);

        InOrder inOrder = inOrder(mockFrontAgencyJpaService);
        inOrder.verify(mockFrontAgencyJpaService).findAgencyList(agencyMap);
    }

    @Test
    @DisplayName("Agency리스트조회BDD테스트")
    void Agency리스트조회BDD테스트() {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        agencyMap.put("jpaStartPage", 1);
        agencyMap.put("size", 3);

        List<FrontAgencyDTO> returnAgencyList = new ArrayList<>();

        returnAgencyList.add(FrontAgencyDTO.builder().idx(1L).agencyName("Agency테스트").agencyDescription("Agency테스트").visible("Y").build());
        returnAgencyList.add(FrontAgencyDTO.builder().idx(2L).agencyName("agencyTest").agencyDescription("agencyTest").visible("Y").build());

        // when
        given(mockFrontAgencyJpaService.findAgencyList(agencyMap)).willReturn(returnAgencyList);
        List<FrontAgencyDTO> agencyList = mockFrontAgencyJpaService.findAgencyList(agencyMap);

        // then
        assertAll(
                () -> assertThat(agencyList).isNotEmpty(),
                () -> assertThat(agencyList).hasSize(2)
        );

        assertThat(agencyList.get(0).getIdx()).isEqualTo(returnAgencyList.get(0).getIdx());
        assertThat(agencyList.get(0).getAgencyName()).isEqualTo(returnAgencyList.get(0).getAgencyName());
        assertThat(agencyList.get(0).getAgencyDescription()).isEqualTo(returnAgencyList.get(0).getAgencyDescription());
        assertThat(agencyList.get(0).getVisible()).isEqualTo(returnAgencyList.get(0).getVisible());

        // verify
        then(mockFrontAgencyJpaService).should(times(1)).findAgencyList(agencyMap);
        then(mockFrontAgencyJpaService).should(atLeastOnce()).findAgencyList(agencyMap);
        then(mockFrontAgencyJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("Agency상세조회Mockito테스트")
    void Agency상세조회Mockito테스트() {
        // given
        FrontAgencyEntity frontAgencyEntity = FrontAgencyEntity.builder().idx(1L).agencyName("agencyTest").agencyDescription("agencyTest").build();
        FrontAgencyDTO frontAgencyDTO = AgencyMapper.INSTANCE.toDto(frontAgencyEntity);

        // when
        when(mockFrontAgencyJpaService.findOneAgency(frontAgencyEntity)).thenReturn(frontAgencyDTO);
        FrontAgencyDTO agencyInfo = mockFrontAgencyJpaService.findOneAgency(frontAgencyEntity);

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(frontAgencyDTO.getIdx());
        assertThat(agencyInfo.getAgencyName()).isEqualTo(frontAgencyDTO.getAgencyName());
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo(frontAgencyDTO.getAgencyDescription());
        assertThat(agencyInfo.getVisible()).isEqualTo(frontAgencyDTO.getVisible());

        // verify
        verify(mockFrontAgencyJpaService, times(1)).findOneAgency(frontAgencyEntity);
        verify(mockFrontAgencyJpaService, atLeastOnce()).findOneAgency(frontAgencyEntity);
        verifyNoMoreInteractions(mockFrontAgencyJpaService);

        InOrder inOrder = inOrder(mockFrontAgencyJpaService);
        inOrder.verify(mockFrontAgencyJpaService).findOneAgency(frontAgencyEntity);
    }

    @Test
    @DisplayName("Agency상세조회BDD테스트")
    void Agency상세조회BDD테스트() {
        // given
        FrontAgencyEntity frontAgencyEntity = FrontAgencyEntity.builder().idx(1L).agencyName("agencyTest").agencyDescription("agencyTest").build();
        FrontAgencyDTO frontAgencyDTO = AgencyMapper.INSTANCE.toDto(frontAgencyEntity);

        // when
        given(mockFrontAgencyJpaService.findOneAgency(frontAgencyEntity)).willReturn(frontAgencyDTO);
        FrontAgencyDTO agencyInfo = mockFrontAgencyJpaService.findOneAgency(frontAgencyEntity);

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(frontAgencyDTO.getIdx());
        assertThat(agencyInfo.getAgencyName()).isEqualTo(frontAgencyDTO.getAgencyName());
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo(frontAgencyDTO.getAgencyDescription());
        assertThat(agencyInfo.getVisible()).isEqualTo(frontAgencyDTO.getVisible());

        // verify
        then(mockFrontAgencyJpaService).should(times(1)).findOneAgency(frontAgencyEntity);
        then(mockFrontAgencyJpaService).should(atLeastOnce()).findOneAgency(frontAgencyEntity);
        then(mockFrontAgencyJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("Agency 좋아요 Mockito 테스트")
    void Agency좋아요Mockito테스트() {
        frontAgencyEntity = FrontAgencyEntity.builder()
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

        frontAgencyDTO = AgencyMapper.INSTANCE.toDto(frontAgencyEntity);
        // given
        em.persist(frontAgencyEntity);

        Integer favoriteCount = frontAgencyJpaService.favoriteAgency(frontAgencyEntity);

        // when
        when(mockFrontAgencyJpaService.favoriteAgency(frontAgencyEntity)).thenReturn(favoriteCount);
        Integer newFavoriteCount = mockFrontAgencyJpaService.favoriteAgency(frontAgencyEntity);

        // then
        assertThat(newFavoriteCount).isEqualTo(favoriteCount);

        // verify
        verify(mockFrontAgencyJpaService, times(1)).favoriteAgency(frontAgencyEntity);
        verify(mockFrontAgencyJpaService, atLeastOnce()).favoriteAgency(frontAgencyEntity);
        verifyNoMoreInteractions(mockFrontAgencyJpaService);

        InOrder inOrder = inOrder(mockFrontAgencyJpaService);
        inOrder.verify(mockFrontAgencyJpaService).favoriteAgency(frontAgencyEntity);
    }

    @Test
    @DisplayName("Agency 좋아요 BDD 테스트")
    void Agency좋아요BDD테스트() {
        frontAgencyEntity = FrontAgencyEntity.builder()
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

        frontAgencyDTO = AgencyMapper.INSTANCE.toDto(frontAgencyEntity);
        // given
        em.persist(frontAgencyEntity);

        Integer favoriteCount = frontAgencyJpaService.favoriteAgency(frontAgencyEntity);

        // when
        given(mockFrontAgencyJpaService.favoriteAgency(frontAgencyEntity)).willReturn(favoriteCount);
        Integer newFavoriteCount = mockFrontAgencyJpaService.favoriteAgency(frontAgencyEntity);

        // then
        assertThat(newFavoriteCount).isEqualTo(favoriteCount);

        // verify
        then(mockFrontAgencyJpaService).should(times(1)).favoriteAgency(frontAgencyEntity);
        then(mockFrontAgencyJpaService).should(atLeastOnce()).favoriteAgency(frontAgencyEntity);
        then(mockFrontAgencyJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 소속사 상세 조회 테스트")
    void 이전or다음소속사상세조회테스트() {
        // given
        frontAgencyEntity = FrontAgencyEntity.builder().idx(2L).build();

        // when
        frontAgencyDTO = frontAgencyJpaService.findOneAgency(frontAgencyEntity);

        // 이전 소속사
        assertThat(frontAgencyJpaService.findPrevOneAgency(frontAgencyEntity).getIdx()).isEqualTo(1);
        // 다음 소속사
        assertThat(frontAgencyJpaService.findNextOneAgency(frontAgencyEntity).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 소속사 상세 조회 Mockito 테스트")
    void 이전소속사상세조회Mockito테스트() {
        // given
        frontAgencyEntity = FrontAgencyEntity.builder().idx(2L).build();

        // when
        frontAgencyDTO = frontAgencyJpaService.findPrevOneAgency(frontAgencyEntity);

        when(mockFrontAgencyJpaService.findPrevOneAgency(frontAgencyEntity)).thenReturn(frontAgencyDTO);
        FrontAgencyDTO agencyInfo = mockFrontAgencyJpaService.findPrevOneAgency(frontAgencyEntity);

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockFrontAgencyJpaService, times(1)).findPrevOneAgency(frontAgencyEntity);
        verify(mockFrontAgencyJpaService, atLeastOnce()).findPrevOneAgency(frontAgencyEntity);
        verifyNoMoreInteractions(mockFrontAgencyJpaService);

        InOrder inOrder = inOrder(mockFrontAgencyJpaService);
        inOrder.verify(mockFrontAgencyJpaService).findPrevOneAgency(frontAgencyEntity);
    }

    @Test
    @DisplayName("이전 소속사 상세 조회 BDD 테스트")
    void 이전소속사상세조회BDD테스트() {
        // given
        frontAgencyEntity = FrontAgencyEntity.builder().idx(2L).build();

        // when
        frontAgencyDTO = frontAgencyJpaService.findPrevOneAgency(frontAgencyEntity);

        given(mockFrontAgencyJpaService.findPrevOneAgency(frontAgencyEntity)).willReturn(frontAgencyDTO);
        FrontAgencyDTO agencyInfo = mockFrontAgencyJpaService.findPrevOneAgency(frontAgencyEntity);

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockFrontAgencyJpaService).should(times(1)).findPrevOneAgency(frontAgencyEntity);
        then(mockFrontAgencyJpaService).should(atLeastOnce()).findPrevOneAgency(frontAgencyEntity);
        then(mockFrontAgencyJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 소속사 상세 조회 Mockito 테스트")
    void 다음소속사상세조회Mockito테스트() {
        // given
        frontAgencyEntity = FrontAgencyEntity.builder().idx(2L).build();

        // when
        frontAgencyDTO = frontAgencyJpaService.findNextOneAgency(frontAgencyEntity);

        when(mockFrontAgencyJpaService.findNextOneAgency(frontAgencyEntity)).thenReturn(frontAgencyDTO);
        FrontAgencyDTO agencyInfo = mockFrontAgencyJpaService.findNextOneAgency(frontAgencyEntity);

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockFrontAgencyJpaService, times(1)).findNextOneAgency(frontAgencyEntity);
        verify(mockFrontAgencyJpaService, atLeastOnce()).findNextOneAgency(frontAgencyEntity);
        verifyNoMoreInteractions(mockFrontAgencyJpaService);

        InOrder inOrder = inOrder(mockFrontAgencyJpaService);
        inOrder.verify(mockFrontAgencyJpaService).findNextOneAgency(frontAgencyEntity);
    }

    @Test
    @DisplayName("다음 소속사 상세 조회 BDD 테스트")
    void 다음소속사상세조회BDD테스트() {
        // given
        frontAgencyEntity = FrontAgencyEntity.builder().idx(2L).build();

        // when
        frontAgencyDTO = frontAgencyJpaService.findNextOneAgency(frontAgencyEntity);

        given(mockFrontAgencyJpaService.findNextOneAgency(frontAgencyEntity)).willReturn(frontAgencyDTO);
        FrontAgencyDTO agencyInfo = mockFrontAgencyJpaService.findNextOneAgency(frontAgencyEntity);

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockFrontAgencyJpaService).should(times(1)).findNextOneAgency(frontAgencyEntity);
        then(mockFrontAgencyJpaService).should(atLeastOnce()).findNextOneAgency(frontAgencyEntity);
        then(mockFrontAgencyJpaService).shouldHaveNoMoreInteractions();
    }
}
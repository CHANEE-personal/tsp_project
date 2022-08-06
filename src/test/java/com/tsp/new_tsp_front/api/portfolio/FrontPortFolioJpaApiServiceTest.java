package com.tsp.new_tsp_front.api.portfolio;

import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioDTO;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioEntity;
import com.tsp.new_tsp_front.api.portfolio.service.impl.FrontPortFolioJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioEntity.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("포트폴리오 Service Test")
class FrontPortFolioJpaApiServiceTest {
    @Mock private FrontPortFolioJpaRepository frontPortFolioJpaRepository;
    @InjectMocks private FrontPortFolioJpaApiService frontPortFolioJpaApiService;

    @Test
    @DisplayName("포트폴리오리스트조회Mockito테스트")
    void 포트폴리오리스트조회Mockito테스트() {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        portfolioMap.put("jpaStartPage", 1);
        portfolioMap.put("size", 3);

        List<FrontPortFolioDTO> returnPortfolioList = new ArrayList<>();
        returnPortfolioList.add(FrontPortFolioDTO.builder()
                .idx(1).title("portfolioTest").description("portfolioTest").hashTag("portfolio").videoUrl("test").visible("Y").build());

        // when
        when(frontPortFolioJpaRepository.getPortFolioList(portfolioMap)).thenReturn(returnPortfolioList);
        List<FrontPortFolioDTO> portfolioList = frontPortFolioJpaApiService.getPortFolioList(portfolioMap);

        // then
        assertAll(
                () -> assertThat(portfolioList).isNotEmpty(),
                () -> assertThat(portfolioList).hasSize(1)
        );

        assertThat(portfolioList.get(0).getIdx()).isEqualTo(returnPortfolioList.get(0).getIdx());
        assertThat(portfolioList.get(0).getTitle()).isEqualTo(returnPortfolioList.get(0).getTitle());
        assertThat(portfolioList.get(0).getDescription()).isEqualTo(returnPortfolioList.get(0).getDescription());
        assertThat(portfolioList.get(0).getHashTag()).isEqualTo(returnPortfolioList.get(0).getHashTag());
        assertThat(portfolioList.get(0).getVideoUrl()).isEqualTo(returnPortfolioList.get(0).getVideoUrl());
        assertThat(portfolioList.get(0).getVisible()).isEqualTo(returnPortfolioList.get(0).getVisible());

        // verify
        verify(frontPortFolioJpaRepository, times(1)).getPortFolioList(portfolioMap);
        verify(frontPortFolioJpaRepository, atLeastOnce()).getPortFolioList(portfolioMap);
        verifyNoMoreInteractions(frontPortFolioJpaRepository);
    }

    @Test
    @DisplayName("포트폴리오리스트조회BDD테스트")
    void 포트폴리오리스트조회BDD테스트() {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        portfolioMap.put("jpaStartPage", 1);
        portfolioMap.put("size", 3);

        List<FrontPortFolioDTO> returnPortfolioList = new ArrayList<>();
        returnPortfolioList.add(FrontPortFolioDTO.builder()
                .idx(1).title("portfolioTest").description("portfolioTest").hashTag("portfolio").videoUrl("test").visible("Y").build());

        // when
        given(frontPortFolioJpaRepository.getPortFolioList(portfolioMap)).willReturn(returnPortfolioList);
        List<FrontPortFolioDTO> portfolioList = frontPortFolioJpaApiService.getPortFolioList(portfolioMap);

        // then
        assertAll(
                () -> assertThat(portfolioList).isNotEmpty(),
                () -> assertThat(portfolioList).hasSize(1)
        );

        assertThat(portfolioList.get(0).getIdx()).isEqualTo(returnPortfolioList.get(0).getIdx());
        assertThat(portfolioList.get(0).getTitle()).isEqualTo(returnPortfolioList.get(0).getTitle());
        assertThat(portfolioList.get(0).getDescription()).isEqualTo(returnPortfolioList.get(0).getDescription());
        assertThat(portfolioList.get(0).getHashTag()).isEqualTo(returnPortfolioList.get(0).getHashTag());
        assertThat(portfolioList.get(0).getVideoUrl()).isEqualTo(returnPortfolioList.get(0).getVideoUrl());
        assertThat(portfolioList.get(0).getVisible()).isEqualTo(returnPortfolioList.get(0).getVisible());

        // verify
        then(frontPortFolioJpaRepository).should(times(1)).getPortFolioList(portfolioMap);
        then(frontPortFolioJpaRepository).should(atLeastOnce()).getPortFolioList(portfolioMap);
        then(frontPortFolioJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("포트폴리오상세조회Mockito테스트")
    void 포트폴리오상세조회Mockito테스트() {
        // given
        FrontPortFolioEntity frontPortFolioEntity = builder().idx(1).build();
        FrontPortFolioDTO frontPortFolioDTO = FrontPortFolioDTO.builder().title("portfolioTest").description("portfolioTest").hashTag("portfolio").videoUrl("test").visible("Y").build();

        // when
        when(frontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity)).thenReturn(frontPortFolioDTO);
        FrontPortFolioDTO portfolioInfo = frontPortFolioJpaApiService.getPortFolioInfo(frontPortFolioEntity);

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(frontPortFolioDTO.getIdx());
        assertThat(portfolioInfo.getTitle()).isEqualTo(frontPortFolioDTO.getTitle());
        assertThat(portfolioInfo.getDescription()).isEqualTo(frontPortFolioDTO.getDescription());
        assertThat(portfolioInfo.getHashTag()).isEqualTo(frontPortFolioDTO.getHashTag());
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo(frontPortFolioDTO.getVideoUrl());
        assertThat(portfolioInfo.getVisible()).isEqualTo(frontPortFolioDTO.getVisible());

        // verify
        verify(frontPortFolioJpaRepository, times(1)).getPortFolioInfo(frontPortFolioEntity);
        verify(frontPortFolioJpaRepository, atLeastOnce()).getPortFolioInfo(frontPortFolioEntity);
        verifyNoMoreInteractions(frontPortFolioJpaRepository);
    }

    @Test
    @DisplayName("포트폴리오상세조회BDD테스트")
    void 포트폴리오상세조회BDD테스트() {
        // given
        FrontPortFolioEntity frontPortFolioEntity = builder().idx(1).build();
        FrontPortFolioDTO frontPortFolioDTO = FrontPortFolioDTO.builder().title("portfolioTest").description("portfolioTest").hashTag("portfolio").videoUrl("test").visible("Y").build();

        // when
        given(frontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity)).willReturn(frontPortFolioDTO);
        FrontPortFolioDTO portfolioInfo = frontPortFolioJpaApiService.getPortFolioInfo(frontPortFolioEntity);

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(frontPortFolioDTO.getIdx());
        assertThat(portfolioInfo.getTitle()).isEqualTo(frontPortFolioDTO.getTitle());
        assertThat(portfolioInfo.getDescription()).isEqualTo(frontPortFolioDTO.getDescription());
        assertThat(portfolioInfo.getHashTag()).isEqualTo(frontPortFolioDTO.getHashTag());
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo(frontPortFolioDTO.getVideoUrl());
        assertThat(portfolioInfo.getVisible()).isEqualTo(frontPortFolioDTO.getVisible());

        // verify
        then(frontPortFolioJpaRepository).should(times(1)).getPortFolioInfo(frontPortFolioEntity);
        then(frontPortFolioJpaRepository).should(atLeastOnce()).getPortFolioInfo(frontPortFolioEntity);
        then(frontPortFolioJpaRepository).shouldHaveNoMoreInteractions();
    }
}
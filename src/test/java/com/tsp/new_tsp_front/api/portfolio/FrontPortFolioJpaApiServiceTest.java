package com.tsp.new_tsp_front.api.portfolio;

import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioDTO;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioEntity;
import com.tsp.new_tsp_front.api.portfolio.service.impl.FrontPortFolioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("포트폴리오 Service Test")
class FrontPortFolioJpaApiServiceTest {
    @Mock private FrontPortFolioJpaApiService mockFrontPortFolioJpaApiService;
    private final FrontPortFolioJpaApiService frontPortFolioJpaApiService;

    @Test
    @DisplayName("포트폴리오리스트조회Mockito테스트")
    void 포트폴리오리스트조회Mockito테스트() {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        portfolioMap.put("jpaStartPage", 1);
        portfolioMap.put("size", 3);

        List<FrontPortFolioDTO> returnPortfolioList = new ArrayList<>();
        returnPortfolioList.add(FrontPortFolioDTO.builder()
                .idx(1L).title("portfolioTest").description("portfolioTest").hashTag("portfolio").videoUrl("test").visible("Y").build());

        // when
        when(mockFrontPortFolioJpaApiService.getPortFolioList(portfolioMap)).thenReturn(returnPortfolioList);
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
        verify(mockFrontPortFolioJpaApiService, times(1)).getPortFolioList(portfolioMap);
        verify(mockFrontPortFolioJpaApiService, atLeastOnce()).getPortFolioList(portfolioMap);
        verifyNoMoreInteractions(mockFrontPortFolioJpaApiService);

        InOrder inOrder = inOrder(mockFrontPortFolioJpaApiService);
        inOrder.verify(mockFrontPortFolioJpaApiService).getPortFolioList(portfolioMap);
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
                .idx(1L).title("portfolioTest").description("portfolioTest").hashTag("portfolio").videoUrl("test").visible("Y").build());

        // when
        given(mockFrontPortFolioJpaApiService.getPortFolioList(portfolioMap)).willReturn(returnPortfolioList);
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
        then(mockFrontPortFolioJpaApiService).should(times(1)).getPortFolioList(portfolioMap);
        then(mockFrontPortFolioJpaApiService).should(atLeastOnce()).getPortFolioList(portfolioMap);
        then(mockFrontPortFolioJpaApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("포트폴리오상세조회Mockito테스트")
    void 포트폴리오상세조회Mockito테스트() {
        // given
        FrontPortFolioEntity frontPortFolioEntity = FrontPortFolioEntity.builder().idx(1L).build();
        FrontPortFolioDTO frontPortFolioDTO = FrontPortFolioDTO.builder().title("portfolioTest").description("portfolioTest").hashTag("portfolio").videoUrl("test").visible("Y").build();

        // when
        when(mockFrontPortFolioJpaApiService.getPortFolioInfo(frontPortFolioEntity)).thenReturn(frontPortFolioDTO);
        FrontPortFolioDTO portfolioInfo = frontPortFolioJpaApiService.getPortFolioInfo(frontPortFolioEntity);

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(frontPortFolioDTO.getIdx());
        assertThat(portfolioInfo.getTitle()).isEqualTo(frontPortFolioDTO.getTitle());
        assertThat(portfolioInfo.getDescription()).isEqualTo(frontPortFolioDTO.getDescription());
        assertThat(portfolioInfo.getHashTag()).isEqualTo(frontPortFolioDTO.getHashTag());
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo(frontPortFolioDTO.getVideoUrl());
        assertThat(portfolioInfo.getVisible()).isEqualTo(frontPortFolioDTO.getVisible());

        // verify
        verify(mockFrontPortFolioJpaApiService, times(1)).getPortFolioInfo(frontPortFolioEntity);
        verify(mockFrontPortFolioJpaApiService, atLeastOnce()).getPortFolioInfo(frontPortFolioEntity);
        verifyNoMoreInteractions(mockFrontPortFolioJpaApiService);

        InOrder inOrder = inOrder(mockFrontPortFolioJpaApiService);
        inOrder.verify(mockFrontPortFolioJpaApiService).getPortFolioInfo(frontPortFolioEntity);
    }

    @Test
    @DisplayName("포트폴리오상세조회BDD테스트")
    void 포트폴리오상세조회BDD테스트() {
        // given
        FrontPortFolioEntity frontPortFolioEntity = FrontPortFolioEntity.builder().idx(1L).build();
        FrontPortFolioDTO frontPortFolioDTO = FrontPortFolioDTO.builder().title("portfolioTest").description("portfolioTest").hashTag("portfolio").videoUrl("test").visible("Y").build();

        // when
        given(mockFrontPortFolioJpaApiService.getPortFolioInfo(frontPortFolioEntity)).willReturn(frontPortFolioDTO);
        FrontPortFolioDTO portfolioInfo = frontPortFolioJpaApiService.getPortFolioInfo(frontPortFolioEntity);

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(frontPortFolioDTO.getIdx());
        assertThat(portfolioInfo.getTitle()).isEqualTo(frontPortFolioDTO.getTitle());
        assertThat(portfolioInfo.getDescription()).isEqualTo(frontPortFolioDTO.getDescription());
        assertThat(portfolioInfo.getHashTag()).isEqualTo(frontPortFolioDTO.getHashTag());
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo(frontPortFolioDTO.getVideoUrl());
        assertThat(portfolioInfo.getVisible()).isEqualTo(frontPortFolioDTO.getVisible());

        // verify
        then(mockFrontPortFolioJpaApiService).should(times(1)).getPortFolioInfo(frontPortFolioEntity);
        then(mockFrontPortFolioJpaApiService).should(atLeastOnce()).getPortFolioInfo(frontPortFolioEntity);
        then(mockFrontPortFolioJpaApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 포트폴리오 상세 조회 테스트")
    void 이전or다음포트폴리오상세조회테스트() {
        // given
        FrontPortFolioEntity frontPortFolioEntity = FrontPortFolioEntity.builder()
                .idx(2L)
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        FrontPortFolioDTO frontPortFolioDTO = frontPortFolioJpaApiService.getPortFolioInfo(frontPortFolioEntity);

        // 이전 프로덕션
        assertThat(frontPortFolioJpaApiService.findPrevOnePortfolio(frontPortFolioEntity).getIdx()).isEqualTo(1);
        // 다음 프로덕션
        assertThat(frontPortFolioJpaApiService.findNextOnePortfolio(frontPortFolioEntity).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 포트폴리오 상세 조회 Mockito 테스트")
    void 이전포트폴리오상세조회Mockito테스트() {
        // given
        FrontPortFolioEntity frontPortFolioEntity = FrontPortFolioEntity.builder()
                .idx(2L)
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        FrontPortFolioDTO frontPortFolioDTO = frontPortFolioJpaApiService.getPortFolioInfo(frontPortFolioEntity);

        when(mockFrontPortFolioJpaApiService.findPrevOnePortfolio(frontPortFolioEntity)).thenReturn(frontPortFolioDTO);
        FrontPortFolioDTO portfolioInfo = mockFrontPortFolioJpaApiService.findPrevOnePortfolio(frontPortFolioEntity);

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockFrontPortFolioJpaApiService, times(1)).findPrevOnePortfolio(frontPortFolioEntity);
        verify(mockFrontPortFolioJpaApiService, atLeastOnce()).findPrevOnePortfolio(frontPortFolioEntity);
        verifyNoMoreInteractions(mockFrontPortFolioJpaApiService);

        InOrder inOrder = inOrder(mockFrontPortFolioJpaApiService);
        inOrder.verify(mockFrontPortFolioJpaApiService).findPrevOnePortfolio(frontPortFolioEntity);
    }

    @Test
    @DisplayName("이전 포트폴리오 상세 조회 BDD 테스트")
    void 이전포트폴리오상세조회BDD테스트() {
        // given
        FrontPortFolioEntity frontPortFolioEntity = FrontPortFolioEntity.builder()
                .idx(2L)
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        FrontPortFolioDTO frontPortFolioDTO = frontPortFolioJpaApiService.getPortFolioInfo(frontPortFolioEntity);

        given(mockFrontPortFolioJpaApiService.findPrevOnePortfolio(frontPortFolioEntity)).willReturn(frontPortFolioDTO);
        FrontPortFolioDTO portfolioInfo = mockFrontPortFolioJpaApiService.findPrevOnePortfolio(frontPortFolioEntity);

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockFrontPortFolioJpaApiService).should(times(1)).findPrevOnePortfolio(frontPortFolioEntity);
        then(mockFrontPortFolioJpaApiService).should(atLeastOnce()).findPrevOnePortfolio(frontPortFolioEntity);
        then(mockFrontPortFolioJpaApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 포트폴리오 상세 조회 Mockito 테스트")
    void 다음포트폴리오상세조회Mockito테스트() {
        // given
        FrontPortFolioEntity frontPortFolioEntity = FrontPortFolioEntity.builder()
                .idx(2L)
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        FrontPortFolioDTO frontPortFolioDTO = frontPortFolioJpaApiService.getPortFolioInfo(frontPortFolioEntity);

        when(mockFrontPortFolioJpaApiService.findPrevOnePortfolio(frontPortFolioEntity)).thenReturn(frontPortFolioDTO);
        FrontPortFolioDTO portfolioInfo = mockFrontPortFolioJpaApiService.findPrevOnePortfolio(frontPortFolioEntity);

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockFrontPortFolioJpaApiService, times(1)).findNextOnePortfolio(frontPortFolioEntity);
        verify(mockFrontPortFolioJpaApiService, atLeastOnce()).findNextOnePortfolio(frontPortFolioEntity);
        verifyNoMoreInteractions(mockFrontPortFolioJpaApiService);

        InOrder inOrder = inOrder(mockFrontPortFolioJpaApiService);
        inOrder.verify(mockFrontPortFolioJpaApiService).findNextOnePortfolio(frontPortFolioEntity);
    }

    @Test
    @DisplayName("다음 포트폴리오 상세 조회 BDD 테스트")
    void 다음포트폴리오상세조회BDD테스트() {
        // given
        FrontPortFolioEntity frontPortFolioEntity = FrontPortFolioEntity.builder()
                .idx(2L)
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        FrontPortFolioDTO frontPortFolioDTO = frontPortFolioJpaApiService.getPortFolioInfo(frontPortFolioEntity);

        given(mockFrontPortFolioJpaApiService.findPrevOnePortfolio(frontPortFolioEntity)).willReturn(frontPortFolioDTO);
        FrontPortFolioDTO portfolioInfo = mockFrontPortFolioJpaApiService.findPrevOnePortfolio(frontPortFolioEntity);

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockFrontPortFolioJpaApiService).should(times(1)).findNextOnePortfolio(frontPortFolioEntity);
        then(mockFrontPortFolioJpaApiService).should(atLeastOnce()).findNextOnePortfolio(frontPortFolioEntity);
        then(mockFrontPortFolioJpaApiService).shouldHaveNoMoreInteractions();
    }
}
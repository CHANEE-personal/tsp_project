package com.tsp.new_tsp_front.api.portfolio;

import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioDTO;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontPortFolioDTO> returnPortfolioList = new ArrayList<>();
        returnPortfolioList.add(FrontPortFolioDTO.builder()
                .idx(1L).title("portfolioTest").description("portfolioTest").hashTag("portfolio").videoUrl("test").visible("Y").build());
        Page<FrontPortFolioDTO> resultPortfolio = new PageImpl<>(returnPortfolioList, pageRequest, returnPortfolioList.size());

        // when
        when(mockFrontPortFolioJpaApiService.findPortfolioList(portfolioMap, pageRequest)).thenReturn(resultPortfolio);
        Page<FrontPortFolioDTO> portfolioList = frontPortFolioJpaApiService.findPortfolioList(portfolioMap, pageRequest);
        List<FrontPortFolioDTO> findPortfolioList = portfolioList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findPortfolioList).isNotEmpty(),
                () -> assertThat(findPortfolioList).hasSize(1)
        );

        assertThat(findPortfolioList.get(0).getIdx()).isEqualTo(returnPortfolioList.get(0).getIdx());
        assertThat(findPortfolioList.get(0).getTitle()).isEqualTo(returnPortfolioList.get(0).getTitle());
        assertThat(findPortfolioList.get(0).getDescription()).isEqualTo(returnPortfolioList.get(0).getDescription());
        assertThat(findPortfolioList.get(0).getHashTag()).isEqualTo(returnPortfolioList.get(0).getHashTag());
        assertThat(findPortfolioList.get(0).getVideoUrl()).isEqualTo(returnPortfolioList.get(0).getVideoUrl());
        assertThat(findPortfolioList.get(0).getVisible()).isEqualTo(returnPortfolioList.get(0).getVisible());

        // verify
        verify(mockFrontPortFolioJpaApiService, times(1)).findPortfolioList(portfolioMap, pageRequest);
        verify(mockFrontPortFolioJpaApiService, atLeastOnce()).findPortfolioList(portfolioMap, pageRequest);
        verifyNoMoreInteractions(mockFrontPortFolioJpaApiService);

        InOrder inOrder = inOrder(mockFrontPortFolioJpaApiService);
        inOrder.verify(mockFrontPortFolioJpaApiService).findPortfolioList(portfolioMap, pageRequest);
    }

    @Test
    @DisplayName("포트폴리오리스트조회BDD테스트")
    void 포트폴리오리스트조회BDD테스트() {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontPortFolioDTO> returnPortfolioList = new ArrayList<>();
        returnPortfolioList.add(FrontPortFolioDTO.builder()
                .idx(1L).title("portfolioTest").description("portfolioTest").hashTag("portfolio").videoUrl("test").visible("Y").build());
        Page<FrontPortFolioDTO> resultPortfolio = new PageImpl<>(returnPortfolioList, pageRequest, returnPortfolioList.size());

        // when
        given(mockFrontPortFolioJpaApiService.findPortfolioList(portfolioMap, pageRequest)).willReturn(resultPortfolio);
        Page<FrontPortFolioDTO> portfolioList = frontPortFolioJpaApiService.findPortfolioList(portfolioMap, pageRequest);
        List<FrontPortFolioDTO> findPortfolioList = portfolioList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findPortfolioList).isNotEmpty(),
                () -> assertThat(findPortfolioList).hasSize(1)
        );

        assertThat(findPortfolioList.get(0).getIdx()).isEqualTo(returnPortfolioList.get(0).getIdx());
        assertThat(findPortfolioList.get(0).getTitle()).isEqualTo(returnPortfolioList.get(0).getTitle());
        assertThat(findPortfolioList.get(0).getDescription()).isEqualTo(returnPortfolioList.get(0).getDescription());
        assertThat(findPortfolioList.get(0).getHashTag()).isEqualTo(returnPortfolioList.get(0).getHashTag());
        assertThat(findPortfolioList.get(0).getVideoUrl()).isEqualTo(returnPortfolioList.get(0).getVideoUrl());
        assertThat(findPortfolioList.get(0).getVisible()).isEqualTo(returnPortfolioList.get(0).getVisible());

        // verify
        then(mockFrontPortFolioJpaApiService).should(times(1)).findPortfolioList(portfolioMap, pageRequest);
        then(mockFrontPortFolioJpaApiService).should(atLeastOnce()).findPortfolioList(portfolioMap, pageRequest);
        then(mockFrontPortFolioJpaApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("포트폴리오상세조회Mockito테스트")
    void 포트폴리오상세조회Mockito테스트() {
        // given
        FrontPortFolioEntity frontPortFolioEntity = FrontPortFolioEntity.builder().idx(1L).build();
        FrontPortFolioDTO frontPortFolioDTO = FrontPortFolioDTO.builder().title("portfolioTest").description("portfolioTest").hashTag("portfolio").videoUrl("test").visible("Y").build();

        // when
        when(mockFrontPortFolioJpaApiService.findOnePortfolio(frontPortFolioEntity.getIdx())).thenReturn(frontPortFolioDTO);
        FrontPortFolioDTO portfolioInfo = frontPortFolioJpaApiService.findOnePortfolio(frontPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(frontPortFolioDTO.getIdx());
        assertThat(portfolioInfo.getTitle()).isEqualTo(frontPortFolioDTO.getTitle());
        assertThat(portfolioInfo.getDescription()).isEqualTo(frontPortFolioDTO.getDescription());
        assertThat(portfolioInfo.getHashTag()).isEqualTo(frontPortFolioDTO.getHashTag());
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo(frontPortFolioDTO.getVideoUrl());
        assertThat(portfolioInfo.getVisible()).isEqualTo(frontPortFolioDTO.getVisible());

        // verify
        verify(mockFrontPortFolioJpaApiService, times(1)).findOnePortfolio(frontPortFolioEntity.getIdx());
        verify(mockFrontPortFolioJpaApiService, atLeastOnce()).findOnePortfolio(frontPortFolioEntity.getIdx());
        verifyNoMoreInteractions(mockFrontPortFolioJpaApiService);

        InOrder inOrder = inOrder(mockFrontPortFolioJpaApiService);
        inOrder.verify(mockFrontPortFolioJpaApiService).findOnePortfolio(frontPortFolioEntity.getIdx());
    }

    @Test
    @DisplayName("포트폴리오상세조회BDD테스트")
    void 포트폴리오상세조회BDD테스트() {
        // given
        FrontPortFolioEntity frontPortFolioEntity = FrontPortFolioEntity.builder().idx(1L).build();
        FrontPortFolioDTO frontPortFolioDTO = FrontPortFolioDTO.builder().title("portfolioTest").description("portfolioTest").hashTag("portfolio").videoUrl("test").visible("Y").build();

        // when
        given(mockFrontPortFolioJpaApiService.findOnePortfolio(frontPortFolioEntity.getIdx())).willReturn(frontPortFolioDTO);
        FrontPortFolioDTO portfolioInfo = frontPortFolioJpaApiService.findOnePortfolio(frontPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(frontPortFolioDTO.getIdx());
        assertThat(portfolioInfo.getTitle()).isEqualTo(frontPortFolioDTO.getTitle());
        assertThat(portfolioInfo.getDescription()).isEqualTo(frontPortFolioDTO.getDescription());
        assertThat(portfolioInfo.getHashTag()).isEqualTo(frontPortFolioDTO.getHashTag());
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo(frontPortFolioDTO.getVideoUrl());
        assertThat(portfolioInfo.getVisible()).isEqualTo(frontPortFolioDTO.getVisible());

        // verify
        then(mockFrontPortFolioJpaApiService).should(times(1)).findOnePortfolio(frontPortFolioEntity.getIdx());
        then(mockFrontPortFolioJpaApiService).should(atLeastOnce()).findOnePortfolio(frontPortFolioEntity.getIdx());
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
        FrontPortFolioDTO frontPortFolioDTO = frontPortFolioJpaApiService.findOnePortfolio(frontPortFolioEntity.getIdx());

        // 이전 프로덕션
        assertThat(frontPortFolioJpaApiService.findPrevOnePortfolio(frontPortFolioEntity.getIdx())).isEqualTo(1);
        // 다음 프로덕션
        assertThat(frontPortFolioJpaApiService.findNextOnePortfolio(frontPortFolioEntity.getIdx())).isEqualTo(3);
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
        FrontPortFolioDTO frontPortFolioDTO = frontPortFolioJpaApiService.findOnePortfolio(frontPortFolioEntity.getIdx());

        when(mockFrontPortFolioJpaApiService.findPrevOnePortfolio(frontPortFolioEntity.getIdx())).thenReturn(frontPortFolioDTO);
        FrontPortFolioDTO portfolioInfo = mockFrontPortFolioJpaApiService.findPrevOnePortfolio(frontPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockFrontPortFolioJpaApiService, times(1)).findPrevOnePortfolio(frontPortFolioEntity.getIdx());
        verify(mockFrontPortFolioJpaApiService, atLeastOnce()).findPrevOnePortfolio(frontPortFolioEntity.getIdx());
        verifyNoMoreInteractions(mockFrontPortFolioJpaApiService);

        InOrder inOrder = inOrder(mockFrontPortFolioJpaApiService);
        inOrder.verify(mockFrontPortFolioJpaApiService).findPrevOnePortfolio(frontPortFolioEntity.getIdx());
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
        FrontPortFolioDTO frontPortFolioDTO = frontPortFolioJpaApiService.findOnePortfolio(frontPortFolioEntity.getIdx());

        given(mockFrontPortFolioJpaApiService.findPrevOnePortfolio(frontPortFolioEntity.getIdx())).willReturn(frontPortFolioDTO);
        FrontPortFolioDTO portfolioInfo = mockFrontPortFolioJpaApiService.findPrevOnePortfolio(frontPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockFrontPortFolioJpaApiService).should(times(1)).findPrevOnePortfolio(frontPortFolioEntity.getIdx());
        then(mockFrontPortFolioJpaApiService).should(atLeastOnce()).findPrevOnePortfolio(frontPortFolioEntity.getIdx());
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
        FrontPortFolioDTO frontPortFolioDTO = frontPortFolioJpaApiService.findOnePortfolio(frontPortFolioEntity.getIdx());

        when(mockFrontPortFolioJpaApiService.findPrevOnePortfolio(frontPortFolioEntity.getIdx())).thenReturn(frontPortFolioDTO);
        FrontPortFolioDTO portfolioInfo = mockFrontPortFolioJpaApiService.findPrevOnePortfolio(frontPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockFrontPortFolioJpaApiService, times(1)).findNextOnePortfolio(frontPortFolioEntity.getIdx());
        verify(mockFrontPortFolioJpaApiService, atLeastOnce()).findNextOnePortfolio(frontPortFolioEntity.getIdx());
        verifyNoMoreInteractions(mockFrontPortFolioJpaApiService);

        InOrder inOrder = inOrder(mockFrontPortFolioJpaApiService);
        inOrder.verify(mockFrontPortFolioJpaApiService).findNextOnePortfolio(frontPortFolioEntity.getIdx());
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
        FrontPortFolioDTO frontPortFolioDTO = frontPortFolioJpaApiService.findOnePortfolio(frontPortFolioEntity.getIdx());

        given(mockFrontPortFolioJpaApiService.findPrevOnePortfolio(frontPortFolioEntity.getIdx())).willReturn(frontPortFolioDTO);
        FrontPortFolioDTO portfolioInfo = mockFrontPortFolioJpaApiService.findPrevOnePortfolio(frontPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockFrontPortFolioJpaApiService).should(times(1)).findNextOnePortfolio(frontPortFolioEntity.getIdx());
        then(mockFrontPortFolioJpaApiService).should(atLeastOnce()).findNextOnePortfolio(frontPortFolioEntity.getIdx());
        then(mockFrontPortFolioJpaApiService).shouldHaveNoMoreInteractions();
    }
}
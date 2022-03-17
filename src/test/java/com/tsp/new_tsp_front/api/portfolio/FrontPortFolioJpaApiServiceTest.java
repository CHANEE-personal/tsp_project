package com.tsp.new_tsp_front.api.portfolio;

import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioDTO;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioEntity;
import com.tsp.new_tsp_front.api.portfolio.service.impl.FrontPortFolioJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioDTO.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@Transactional
@ExtendWith(MockitoExtension.class)
@DisplayName("포트폴리오 Service Test")
class FrontPortFolioJpaApiServiceTest {

    @Mock
    private FrontPortFolioJpaRepository frontPortFolioJpaRepository;

    @InjectMocks
    private FrontPortFolioJpaApiService frontPortFolioJpaApiService;

    @Test
    public void 포트폴리오리스트조회테스트() throws Exception {
        // given
        ConcurrentHashMap<String, Object> portfolioMap = new ConcurrentHashMap<>();
        portfolioMap.put("jpaStartPage", 1);
        portfolioMap.put("size", 3);

        List<FrontPortFolioDTO> returnPortfolioList = new ArrayList<>();
        returnPortfolioList.add(builder()
                .idx(1).title("portfolioTest").description("portfolioTest").hashTag("portfolio").videoUrl("test").visible("Y").build());

        given(frontPortFolioJpaRepository.getPortFolioList(portfolioMap)).willReturn(returnPortfolioList);

        // when
        List<FrontPortFolioDTO> portfolioList = frontPortFolioJpaApiService.getPortFolioList(portfolioMap);

        assertThat(portfolioList.size()).isGreaterThan(0);
        assertThat(portfolioList.size()).isEqualTo(1);

        assertThat(portfolioList.get(0).getIdx()).isEqualTo(returnPortfolioList.get(0).getIdx());
        assertThat(portfolioList.get(0).getTitle()).isEqualTo(returnPortfolioList.get(0).getTitle());
        assertThat(portfolioList.get(0).getDescription()).isEqualTo(returnPortfolioList.get(0).getDescription());
        assertThat(portfolioList.get(0).getHashTag()).isEqualTo(returnPortfolioList.get(0).getHashTag());
        assertThat(portfolioList.get(0).getVideoUrl()).isEqualTo(returnPortfolioList.get(0).getVideoUrl());
        assertThat(portfolioList.get(0).getVisible()).isEqualTo(returnPortfolioList.get(0).getVisible());
    }

    @Test
    public void 포트폴리오상세조회테스트() throws Exception {
        // given
        FrontPortFolioEntity frontPortFolioEntity = FrontPortFolioEntity.builder().idx(1).build();
        FrontPortFolioDTO frontPortFolioDTO = builder().title("portfolioTest").description("portfolioTest").hashTag("portfolio").videoUrl("test").visible("Y").build();
        given(frontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity)).willReturn(frontPortFolioDTO);

        // when
        FrontPortFolioDTO portfolioInfo = frontPortFolioJpaApiService.getPortFolioInfo(frontPortFolioEntity);

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(frontPortFolioDTO.getIdx());
        assertThat(portfolioInfo.getTitle()).isEqualTo(frontPortFolioDTO.getTitle());
        assertThat(portfolioInfo.getDescription()).isEqualTo(frontPortFolioDTO.getDescription());
        assertThat(portfolioInfo.getHashTag()).isEqualTo(frontPortFolioDTO.getHashTag());
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo(frontPortFolioDTO.getVideoUrl());
        assertThat(portfolioInfo.getVisible()).isEqualTo(frontPortFolioDTO.getVisible());
    }
}
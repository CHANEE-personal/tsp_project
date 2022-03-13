package com.tsp.new_tsp_front.api.portfolio.service.impl;

import com.tsp.new_tsp_front.api.portfolio.FrontPortFolioJpaApiService;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioDTO;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioEntity.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("포트폴리오 Repository Test")
class FrontPortFolioJpaRepositoryTest {

    @Autowired
    private FrontPortFolioJpaRepository frontPortFolioJpaRepository;

    @InjectMocks
    private FrontPortFolioJpaApiService frontPortFolioJpaApiService;

    @Test
    public void 포트폴리오조회테스트() throws Exception {
        // given
        ConcurrentHashMap<String, Object> portfolioMap = new ConcurrentHashMap<>();
        portfolioMap.put("jpaStartPage", 1);
        portfolioMap.put("size", 3);

        // when
        List<FrontPortFolioDTO> portfolioList = frontPortFolioJpaRepository.getPortFolioList(portfolioMap);

        // then
        assertThat(portfolioList.size()).isGreaterThan(0);
    }

    @Test
    public void 포트폴리오상세조회테스트() throws Exception {
        // given
        FrontPortFolioEntity frontPortFolioEntity = builder().idx(1).build();

        // when
        FrontPortFolioDTO portfolioInfo = frontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity);

        // then
        assertAll(() -> assertThat(portfolioInfo.getIdx()).isEqualTo(1),
                () -> {
                    assertThat(portfolioInfo.getTitle()).isEqualTo("포트폴리오 테스트");
                    assertNotNull(portfolioInfo.getTitle());
                },
                () -> {
                    assertThat(portfolioInfo.getDescription()).isEqualTo("포트폴리오 테스트");
                    assertNotNull(portfolioInfo.getDescription());
                },
                () -> {
                    assertThat(portfolioInfo.getHashTag()).isEqualTo("TEST");
                    assertNotNull(portfolioInfo.getHashTag());
                },
                () -> {
                    assertThat(portfolioInfo.getVideoUrl()).isEqualTo("http://youtube.com");
                    assertNotNull(portfolioInfo.getVideoUrl());
                },
                () -> {
                    assertThat(portfolioInfo.getVisible()).isEqualTo("Y");
                    assertNotNull(portfolioInfo.getVisible());
                });

        assertThat(portfolioInfo.getPortfolioImage().get(0).getTypeName()).isEqualTo("portfolio");
        assertThat(portfolioInfo.getPortfolioImage().get(0).getFileName()).isEqualTo("52d4fdc8-f109-408e-b243-85cc1be207c5.jpg");
        assertThat(portfolioInfo.getPortfolioImage().get(0).getFilePath()).isEqualTo("/var/www/dist/upload/1223043918525.jpg");

    }
}
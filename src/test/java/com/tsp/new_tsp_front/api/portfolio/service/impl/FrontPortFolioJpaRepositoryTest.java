package com.tsp.new_tsp_front.api.portfolio.service.impl;

import com.tsp.new_tsp_front.api.common.domain.CommonImageEntity;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioDTO;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioEntity.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("포트폴리오 Repository Test")
class FrontPortFolioJpaRepositoryTest {

    @Autowired
    FrontPortFolioJpaRepository frontPortFolioJpaRepository;

    @Mock
    FrontPortFolioJpaRepository mockFrontPortFolioJpaRepository;

    private FrontPortFolioDTO portFolioDTO;
    private CommonImageEntity commonImageEntity;
    private FrontPortFolioEntity frontPortFolioEntity;

    @BeforeEach
    public void setup() {
        commonImageEntity = CommonImageEntity.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("portfolio")
                .build();

        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        frontPortFolioEntity = builder().idx(1).commonImageEntityList(commonImageEntityList).build();

        portFolioDTO = FrontPortFolioDTO.builder()
                .idx(1)
                .title("포트폴리오 Test")
                .description("포트폴리오 Test")
                .categoryCd(2)
                .hashTag("포트폴리오 Test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .portfolioImage(PortFolioImageMapper.INSTANCE.toDtoList(commonImageEntityList))
                .build();
    }

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
        assertThat(portfolioInfo.getPortfolioImage().get(0).getImageType()).isEqualTo("main");
        assertThat(portfolioInfo.getPortfolioImage().get(0).getFileName()).isEqualTo("52d4fdc8-f109-408e-b243-85cc1be207c5.jpg");
        assertThat(portfolioInfo.getPortfolioImage().get(0).getFilePath()).isEqualTo("/var/www/dist/upload/1223043918525.jpg");

        assertThat(portfolioInfo.getPortfolioImage().get(1).getTypeName()).isEqualTo("portfolio");
        assertThat(portfolioInfo.getPortfolioImage().get(1).getImageType()).isEqualTo("sub1");
        assertThat(portfolioInfo.getPortfolioImage().get(1).getFileName()).isEqualTo("e13f6930-17a5-407c-96ed-fd625b720d21.jpg");
        assertThat(portfolioInfo.getPortfolioImage().get(1).getFilePath()).isEqualTo("/var/www/dist/upload/1223043918557.jpg");
    }

    @Test
    public void 포트폴리오상세BDD조회테스트() throws Exception {

        // given
        given(mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity)).willReturn(portFolioDTO);

        // when
        Integer idx = mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getIdx();
        String title = mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getTitle();
        String description = mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getDescription();
        Integer categoryCd = mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getCategoryCd();
        String hashTag = mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getHashTag();
        String videoUrl = mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getVideoUrl();
        String visible = mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getVisible();
        String fileName = mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getPortfolioImage().get(0).getFileName();
        String fileMask = mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getPortfolioImage().get(0).getFileMask();
        String filePath = mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getPortfolioImage().get(0).getFilePath();
        String imageType = mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getPortfolioImage().get(0).getImageType();
        String typeName = mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getPortfolioImage().get(0).getTypeName();

        // then
        assertThat(idx).isEqualTo(1);
        assertThat(title).isEqualTo("포트폴리오 Test");
        assertThat(description).isEqualTo("포트폴리오 Test");
        assertThat(categoryCd).isEqualTo(2);
        assertThat(hashTag).isEqualTo("포트폴리오 Test");
        assertThat(videoUrl).isEqualTo("https://youtube.com");
        assertThat(visible).isEqualTo("Y");
        assertThat(fileName).isEqualTo("test.jpg");
        assertThat(fileMask).isEqualTo("test.jpg");
        assertThat(filePath).isEqualTo("/test/test.jpg");
        assertThat(imageType).isEqualTo("main");
        assertThat(typeName).isEqualTo("portfolio");
    }
}
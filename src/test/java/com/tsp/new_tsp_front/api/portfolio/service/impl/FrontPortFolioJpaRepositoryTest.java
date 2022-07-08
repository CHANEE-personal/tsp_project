package com.tsp.new_tsp_front.api.portfolio.service.impl;

import com.tsp.new_tsp_front.api.common.domain.CommonImageDTO;
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

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioEntity.*;
import static com.tsp.new_tsp_front.api.portfolio.service.impl.PortFolioImageMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("포트폴리오 Repository Test")
class FrontPortFolioJpaRepositoryTest {
    private FrontPortFolioEntity frontPortFolioEntity;
    private FrontPortFolioDTO frontPortFolioDTO;
    private CommonImageEntity commonImageEntity;
    private CommonImageDTO commonImageDTO;

    List<CommonImageEntity> commonImageEntityList = new ArrayList<>();

    @Autowired
    FrontPortFolioJpaRepository frontPortFolioJpaRepository;

    @Mock
    FrontPortFolioJpaRepository mockFrontPortFolioJpaRepository;

    private void createPortfolio() {
        commonImageEntity = CommonImageEntity.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("portfolio")
                .build();

        commonImageDTO = INSTANCE.toDto(commonImageEntity);

        commonImageEntityList.add(commonImageEntity);

        frontPortFolioDTO = FrontPortFolioDTO.builder()
                .idx(1)
                .title("포트폴리오 Test")
                .description("포트폴리오 Test")
                .categoryCd(2)
                .hashTag("포트폴리오 Test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .portfolioImage(INSTANCE.toDtoList(commonImageEntityList))
                .build();

        frontPortFolioEntity = builder().idx(1).commonImageEntityList(commonImageEntityList).build();
    }

    @BeforeEach
    void setup() {
        createPortfolio();
    }

    @Test
    @DisplayName("포트폴리오 조회 테스트")
    void 포트폴리오조회테스트() {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        portfolioMap.put("jpaStartPage", 1);
        portfolioMap.put("size", 3);

        // then
        assertThat(frontPortFolioJpaRepository.getPortFolioList(portfolioMap)).isNotEmpty();
    }

    @Test
    @DisplayName("포트폴리오 상세 조회 테스트")
    void 포트폴리오상세조회테스트() {
        frontPortFolioDTO = frontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity);

        // then
        assertAll(() -> assertThat(frontPortFolioDTO.getIdx()).isEqualTo(1),
                () -> {
                    assertThat(frontPortFolioDTO.getTitle()).isEqualTo("포트폴리오 테스트");
                    assertNotNull(frontPortFolioDTO.getTitle());
                },
                () -> {
                    assertThat(frontPortFolioDTO.getDescription()).isEqualTo("포트폴리오 테스트");
                    assertNotNull(frontPortFolioDTO.getDescription());
                },
                () -> {
                    assertThat(frontPortFolioDTO.getHashTag()).isEqualTo("TEST");
                    assertNotNull(frontPortFolioDTO.getHashTag());
                },
                () -> {
                    assertThat(frontPortFolioDTO.getVideoUrl()).isEqualTo("http://youtube.com");
                    assertNotNull(frontPortFolioDTO.getVideoUrl());
                },
                () -> {
                    assertThat(frontPortFolioDTO.getVisible()).isEqualTo("Y");
                    assertNotNull(frontPortFolioDTO.getVisible());
                });

        assertThat(frontPortFolioDTO.getPortfolioImage().get(0).getTypeName()).isEqualTo("portfolio");
        assertThat(frontPortFolioDTO.getPortfolioImage().get(0).getImageType()).isEqualTo("main");
        assertThat(frontPortFolioDTO.getPortfolioImage().get(0).getFileName()).isEqualTo("52d4fdc8-f109-408e-b243-85cc1be207c5.jpg");
        assertThat(frontPortFolioDTO.getPortfolioImage().get(0).getFilePath()).isEqualTo("/var/www/dist/upload/1223043918525.jpg");

        assertThat(frontPortFolioDTO.getPortfolioImage().get(1).getTypeName()).isEqualTo("portfolio");
        assertThat(frontPortFolioDTO.getPortfolioImage().get(1).getImageType()).isEqualTo("sub1");
        assertThat(frontPortFolioDTO.getPortfolioImage().get(1).getFileName()).isEqualTo("e13f6930-17a5-407c-96ed-fd625b720d21.jpg");
        assertThat(frontPortFolioDTO.getPortfolioImage().get(1).getFilePath()).isEqualTo("/var/www/dist/upload/1223043918557.jpg");
    }

    @Test
    @DisplayName("포트폴리오 BDD 조회 테스트")
    void 포트폴리오BDD조회테스트() {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        portfolioMap.put("jpaStartPage", 1);
        portfolioMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<FrontPortFolioDTO> portfolioList = new ArrayList<>();
        portfolioList.add(FrontPortFolioDTO.builder().idx(1).title("포트폴리오").description("포트폴리오")
                .hashTag("#test").portfolioImage(commonImageDtoList).visible("Y").build());

        // when
//        given(mockFrontPortFolioJpaRepository.getPortFolioList(portfolioMap)).willReturn(portfolioList);
        when(mockFrontPortFolioJpaRepository.getPortFolioList(portfolioMap)).thenReturn(portfolioList);

        assertThat(mockFrontPortFolioJpaRepository.getPortFolioList(portfolioMap).get(0).getIdx()).isEqualTo(portfolioList.get(0).getIdx());
        assertThat(mockFrontPortFolioJpaRepository.getPortFolioList(portfolioMap).get(0).getTitle()).isEqualTo(portfolioList.get(0).getTitle());
        assertThat(mockFrontPortFolioJpaRepository.getPortFolioList(portfolioMap).get(0).getDescription()).isEqualTo(portfolioList.get(0).getDescription());
        assertThat(mockFrontPortFolioJpaRepository.getPortFolioList(portfolioMap).get(0).getHashTag()).isEqualTo(portfolioList.get(0).getHashTag());
        assertThat(mockFrontPortFolioJpaRepository.getPortFolioList(portfolioMap).get(0).getPortfolioImage().get(0).getFileName()).isEqualTo(portfolioList.get(0).getPortfolioImage().get(0).getFileName());
        assertThat(mockFrontPortFolioJpaRepository.getPortFolioList(portfolioMap).get(0).getPortfolioImage().get(0).getTypeName()).isEqualTo(portfolioList.get(0).getPortfolioImage().get(0).getTypeName());

        // verify
        verify(mockFrontPortFolioJpaRepository, times(6)).getPortFolioList(portfolioMap);
        verify(mockFrontPortFolioJpaRepository, atLeastOnce()).getPortFolioList(portfolioMap);
    }

    @Test
    @DisplayName("포트폴리오 상세 BDD 조회 테스트")
    void 포트폴리오상세BDD조회테스트() {

        // when
//        given(mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity)).willReturn(frontPortFolioDTO);
        when(mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity)).thenReturn(frontPortFolioDTO);

        // then
        assertThat(mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getIdx()).isEqualTo(1);
        assertThat(mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getTitle()).isEqualTo("포트폴리오 Test");
        assertThat(mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getDescription()).isEqualTo("포트폴리오 Test");
        assertThat(mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getCategoryCd()).isEqualTo(2);
        assertThat(mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getHashTag()).isEqualTo("포트폴리오 Test");
        assertThat(mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getVideoUrl()).isEqualTo("https://youtube.com");
        assertThat(mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getVisible()).isEqualTo("Y");
        assertThat(mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getPortfolioImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getPortfolioImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getPortfolioImage().get(0).getFilePath()).isEqualTo("/test/test.jpg");
        assertThat(mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getPortfolioImage().get(0).getImageType()).isEqualTo("main");
        assertThat(mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity).getPortfolioImage().get(0).getTypeName()).isEqualTo("portfolio");

        // verify
        verify(mockFrontPortFolioJpaRepository, times(12)).getPortFolioInfo(frontPortFolioEntity);
        verify(mockFrontPortFolioJpaRepository, atLeastOnce()).getPortFolioInfo(frontPortFolioEntity);
    }
}
package com.tsp.new_tsp_front.api.portfolio.service.impl;

import com.tsp.new_tsp_front.api.common.domain.CommonImageDTO;
import com.tsp.new_tsp_front.api.common.domain.CommonImageEntity;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioDTO;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-local.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("포트폴리오 Repository Test")
class FrontPortFolioJpaRepositoryTest {
    @Mock FrontPortFolioJpaRepository mockFrontPortFolioJpaRepository;
    private final FrontPortFolioJpaRepository frontPortFolioJpaRepository;

    private FrontPortFolioEntity frontPortFolioEntity;
    private FrontPortFolioDTO frontPortFolioDTO;
    private CommonImageEntity commonImageEntity;
    private CommonImageDTO commonImageDTO;
    List<CommonImageEntity> commonImageEntityList = new ArrayList<>();

    private final EntityManager em;

    private void createPortfolio() {
        commonImageEntity = CommonImageEntity.builder()
                .idx(1L)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1L)
                .typeName("portfolio")
                .build();

        commonImageDTO = CommonImageEntity.toDto(commonImageEntity);

        commonImageEntityList.add(commonImageEntity);

        frontPortFolioEntity = FrontPortFolioEntity.builder()
                .title("포트폴리오 Test")
                .description("포트폴리오 Test")
                .categoryCd(2)
                .hashTag("포트폴리오 Test")
                .viewCount(1)
                .videoUrl("https://youtube.com")
                .visible("Y")
                .commonImageEntityList(commonImageEntityList)
                .build();

        frontPortFolioDTO = FrontPortFolioEntity.toDto(frontPortFolioEntity);
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
    @DisplayName("포트폴리오 리스트 조회 Mockito 테스트")
    void 포트폴리오리스트조회Mockito테스트() {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        portfolioMap.put("jpaStartPage", 1);
        portfolioMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<FrontPortFolioDTO> portfolioList = new ArrayList<>();
        portfolioList.add(FrontPortFolioDTO.builder().idx(1L).title("포트폴리오").description("포트폴리오")
                .hashTag("#test").portfolioImage(commonImageDtoList).visible("Y").build());

        // when
        when(mockFrontPortFolioJpaRepository.getPortFolioList(portfolioMap)).thenReturn(portfolioList);
        List<FrontPortFolioDTO> newPortfolioList = mockFrontPortFolioJpaRepository.getPortFolioList(portfolioMap);

        // then
        assertThat(newPortfolioList.get(0).getIdx()).isEqualTo(portfolioList.get(0).getIdx());
        assertThat(newPortfolioList.get(0).getTitle()).isEqualTo(portfolioList.get(0).getTitle());
        assertThat(newPortfolioList.get(0).getDescription()).isEqualTo(portfolioList.get(0).getDescription());
        assertThat(newPortfolioList.get(0).getHashTag()).isEqualTo(portfolioList.get(0).getHashTag());
        assertThat(newPortfolioList.get(0).getPortfolioImage().get(0).getFileName()).isEqualTo(portfolioList.get(0).getPortfolioImage().get(0).getFileName());
        assertThat(newPortfolioList.get(0).getPortfolioImage().get(0).getTypeName()).isEqualTo(portfolioList.get(0).getPortfolioImage().get(0).getTypeName());

        // verify
        verify(mockFrontPortFolioJpaRepository, times(1)).getPortFolioList(portfolioMap);
        verify(mockFrontPortFolioJpaRepository, atLeastOnce()).getPortFolioList(portfolioMap);
        verifyNoMoreInteractions(mockFrontPortFolioJpaRepository);

        InOrder inOrder = inOrder(mockFrontPortFolioJpaRepository);
        inOrder.verify(mockFrontPortFolioJpaRepository).getPortFolioList(portfolioMap);
    }

    @Test
    @DisplayName("포트폴리오 리스트 조회 BDD 테스트")
    void 포트폴리오리스트조회BDD테스트() {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        portfolioMap.put("jpaStartPage", 1);
        portfolioMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<FrontPortFolioDTO> portfolioList = new ArrayList<>();
        portfolioList.add(FrontPortFolioDTO.builder().idx(1L).title("포트폴리오").description("포트폴리오")
                .hashTag("#test").portfolioImage(commonImageDtoList).visible("Y").build());

        // when
        given(mockFrontPortFolioJpaRepository.getPortFolioList(portfolioMap)).willReturn(portfolioList);
        List<FrontPortFolioDTO> newPortfolioList = mockFrontPortFolioJpaRepository.getPortFolioList(portfolioMap);

        // then
        assertThat(newPortfolioList.get(0).getIdx()).isEqualTo(portfolioList.get(0).getIdx());
        assertThat(newPortfolioList.get(0).getTitle()).isEqualTo(portfolioList.get(0).getTitle());
        assertThat(newPortfolioList.get(0).getDescription()).isEqualTo(portfolioList.get(0).getDescription());
        assertThat(newPortfolioList.get(0).getHashTag()).isEqualTo(portfolioList.get(0).getHashTag());
        assertThat(newPortfolioList.get(0).getPortfolioImage().get(0).getFileName()).isEqualTo(portfolioList.get(0).getPortfolioImage().get(0).getFileName());
        assertThat(newPortfolioList.get(0).getPortfolioImage().get(0).getTypeName()).isEqualTo(portfolioList.get(0).getPortfolioImage().get(0).getTypeName());

        // verify
        then(mockFrontPortFolioJpaRepository).should(times(1)).getPortFolioList(portfolioMap);
        then(mockFrontPortFolioJpaRepository).should(atLeastOnce()).getPortFolioList(portfolioMap);
        then(mockFrontPortFolioJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("포트폴리오 상세 조회 테스트")
    void 포트폴리오상세조회테스트() {
        frontPortFolioDTO = frontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity.getIdx());

        // then
        assertAll(() -> {
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

        // then
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
    @DisplayName("포트폴리오 상세 조회 Mockito 테스트")
    void 포트폴리오상세조회Mockito테스트() {
        // when
        when(mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity.getIdx())).thenReturn(frontPortFolioDTO);
        FrontPortFolioDTO portfolioInfo = mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getTitle()).isEqualTo("포트폴리오 Test");
        assertThat(portfolioInfo.getDescription()).isEqualTo("포트폴리오 Test");
        assertThat(portfolioInfo.getCategoryCd()).isEqualTo(2);
        assertThat(portfolioInfo.getHashTag()).isEqualTo("포트폴리오 Test");
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo("https://youtube.com");
        assertThat(portfolioInfo.getVisible()).isEqualTo("Y");
        assertThat(portfolioInfo.getPortfolioImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(portfolioInfo.getPortfolioImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(portfolioInfo.getPortfolioImage().get(0).getFilePath()).isEqualTo("/test/test.jpg");
        assertThat(portfolioInfo.getPortfolioImage().get(0).getImageType()).isEqualTo("main");
        assertThat(portfolioInfo.getPortfolioImage().get(0).getTypeName()).isEqualTo("portfolio");

        // verify
        verify(mockFrontPortFolioJpaRepository, times(1)).getPortFolioInfo(frontPortFolioEntity.getIdx());
        verify(mockFrontPortFolioJpaRepository, atLeastOnce()).getPortFolioInfo(frontPortFolioEntity.getIdx());
        verifyNoMoreInteractions(mockFrontPortFolioJpaRepository);

        InOrder inOrder = inOrder(mockFrontPortFolioJpaRepository);
        inOrder.verify(mockFrontPortFolioJpaRepository).getPortFolioInfo(frontPortFolioEntity.getIdx());
    }

    @Test
    @DisplayName("포트폴리오 상세 조회 BDD 테스트")
    void 포트폴리오상세조회BDD테스트() {
        // when
        given(mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity.getIdx())).willReturn(frontPortFolioDTO);
        FrontPortFolioDTO portFolioInfo = mockFrontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity.getIdx());

        // then
        assertThat(portFolioInfo.getTitle()).isEqualTo("포트폴리오 Test");
        assertThat(portFolioInfo.getDescription()).isEqualTo("포트폴리오 Test");
        assertThat(portFolioInfo.getCategoryCd()).isEqualTo(2);
        assertThat(portFolioInfo.getHashTag()).isEqualTo("포트폴리오 Test");
        assertThat(portFolioInfo.getVideoUrl()).isEqualTo("https://youtube.com");
        assertThat(portFolioInfo.getVisible()).isEqualTo("Y");
        assertThat(portFolioInfo.getPortfolioImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(portFolioInfo.getPortfolioImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(portFolioInfo.getPortfolioImage().get(0).getFilePath()).isEqualTo("/test/test.jpg");
        assertThat(portFolioInfo.getPortfolioImage().get(0).getImageType()).isEqualTo("main");
        assertThat(portFolioInfo.getPortfolioImage().get(0).getTypeName()).isEqualTo("portfolio");

        // verify
        then(mockFrontPortFolioJpaRepository).should(times(1)).getPortFolioInfo(frontPortFolioEntity.getIdx());
        then(mockFrontPortFolioJpaRepository).should(atLeastOnce()).getPortFolioInfo(frontPortFolioEntity.getIdx());
        then(mockFrontPortFolioJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 포트폴리오 상세 조회 테스트")
    void 이전or다음포트폴리오상세조회테스트() {
        // given
        frontPortFolioEntity = FrontPortFolioEntity.builder()
                .idx(2L)
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        frontPortFolioDTO = frontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity.getIdx());

        // 이전 프로덕션
        assertThat(frontPortFolioJpaRepository.findPrevOnePortfolio(frontPortFolioEntity.getIdx())).isEqualTo(1);
        // 다음 프로덕션
        assertThat(frontPortFolioJpaRepository.findNextOnePortfolio(frontPortFolioEntity.getIdx())).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 포트폴리오 상세 조회 Mockito 테스트")
    void 이전포트폴리오상세조회Mockito테스트() {
        // given
        frontPortFolioEntity = FrontPortFolioEntity.builder()
                .idx(2L)
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        frontPortFolioDTO = frontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity.getIdx());

        when(mockFrontPortFolioJpaRepository.findPrevOnePortfolio(frontPortFolioEntity.getIdx())).thenReturn(frontPortFolioDTO);
        FrontPortFolioDTO portfolioInfo = mockFrontPortFolioJpaRepository.findPrevOnePortfolio(frontPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockFrontPortFolioJpaRepository, times(1)).findPrevOnePortfolio(frontPortFolioEntity.getIdx());
        verify(mockFrontPortFolioJpaRepository, atLeastOnce()).findPrevOnePortfolio(frontPortFolioEntity.getIdx());
        verifyNoMoreInteractions(mockFrontPortFolioJpaRepository);

        InOrder inOrder = inOrder(mockFrontPortFolioJpaRepository);
        inOrder.verify(mockFrontPortFolioJpaRepository).findPrevOnePortfolio(frontPortFolioEntity.getIdx());
    }

    @Test
    @DisplayName("이전 포트폴리오 상세 조회 BDD 테스트")
    void 이전포트폴리오상세조회BDD테스트() {
        // given
        frontPortFolioEntity = FrontPortFolioEntity.builder()
                .idx(2L)
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        frontPortFolioDTO = frontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity.getIdx());

        given(mockFrontPortFolioJpaRepository.findPrevOnePortfolio(frontPortFolioEntity.getIdx())).willReturn(frontPortFolioDTO);
        FrontPortFolioDTO portfolioInfo = mockFrontPortFolioJpaRepository.findPrevOnePortfolio(frontPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockFrontPortFolioJpaRepository).should(times(1)).findPrevOnePortfolio(frontPortFolioEntity.getIdx());
        then(mockFrontPortFolioJpaRepository).should(atLeastOnce()).findPrevOnePortfolio(frontPortFolioEntity.getIdx());
        then(mockFrontPortFolioJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 포트폴리오 상세 조회 Mockito 테스트")
    void 다음포트폴리오상세조회Mockito테스트() {
        // given
        frontPortFolioEntity = FrontPortFolioEntity.builder()
                .idx(2L)
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        frontPortFolioDTO = frontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity.getIdx());

        when(mockFrontPortFolioJpaRepository.findPrevOnePortfolio(frontPortFolioEntity.getIdx())).thenReturn(frontPortFolioDTO);
        FrontPortFolioDTO portfolioInfo = mockFrontPortFolioJpaRepository.findPrevOnePortfolio(frontPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockFrontPortFolioJpaRepository, times(1)).findNextOnePortfolio(frontPortFolioEntity.getIdx());
        verify(mockFrontPortFolioJpaRepository, atLeastOnce()).findNextOnePortfolio(frontPortFolioEntity.getIdx());
        verifyNoMoreInteractions(mockFrontPortFolioJpaRepository);

        InOrder inOrder = inOrder(mockFrontPortFolioJpaRepository);
        inOrder.verify(mockFrontPortFolioJpaRepository).findNextOnePortfolio(frontPortFolioEntity.getIdx());
    }

    @Test
    @DisplayName("다음 포트폴리오 상세 조회 BDD 테스트")
    void 다음포트폴리오상세조회BDD테스트() {
        // given
        frontPortFolioEntity = FrontPortFolioEntity.builder()
                .idx(2L)
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        frontPortFolioDTO = frontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity.getIdx());

        given(mockFrontPortFolioJpaRepository.findPrevOnePortfolio(frontPortFolioEntity.getIdx())).willReturn(frontPortFolioDTO);
        FrontPortFolioDTO portfolioInfo = mockFrontPortFolioJpaRepository.findPrevOnePortfolio(frontPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockFrontPortFolioJpaRepository).should(times(1)).findNextOnePortfolio(frontPortFolioEntity.getIdx());
        then(mockFrontPortFolioJpaRepository).should(atLeastOnce()).findNextOnePortfolio(frontPortFolioEntity.getIdx());
        then(mockFrontPortFolioJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션 조회 수 Mockito 테스트")
    void 프로덕션조회수Mockito테스트() {
        // given
        em.persist(frontPortFolioEntity);
        frontPortFolioDTO = FrontPortFolioEntity.toDto(frontPortFolioEntity);

        Integer viewCount = frontPortFolioJpaRepository.updatePortfolioViewCount(frontPortFolioEntity.getIdx());

        // when
        when(mockFrontPortFolioJpaRepository.updatePortfolioViewCount(frontPortFolioEntity.getIdx())).thenReturn(viewCount);
        Integer newViewCount = mockFrontPortFolioJpaRepository.updatePortfolioViewCount(frontPortFolioEntity.getIdx());

        // then
        assertThat(newViewCount).isEqualTo(viewCount);

        // verify
        verify(mockFrontPortFolioJpaRepository, times(1)).updatePortfolioViewCount(frontPortFolioEntity.getIdx());
        verify(mockFrontPortFolioJpaRepository, atLeastOnce()).updatePortfolioViewCount(frontPortFolioEntity.getIdx());
        verifyNoMoreInteractions(mockFrontPortFolioJpaRepository);

        InOrder inOrder = inOrder(mockFrontPortFolioJpaRepository);
        inOrder.verify(mockFrontPortFolioJpaRepository).updatePortfolioViewCount(frontPortFolioEntity.getIdx());
    }

    @Test
    @DisplayName("프로덕션 조회 수 BDD 테스트")
    void 프로덕션조회수BDD테스트() {
        // given
        em.persist(frontPortFolioEntity);
        frontPortFolioDTO = FrontPortFolioEntity.toDto(frontPortFolioEntity);

        Integer viewCount = frontPortFolioJpaRepository.updatePortfolioViewCount(frontPortFolioEntity.getIdx());

        // when
        when(mockFrontPortFolioJpaRepository.updatePortfolioViewCount(frontPortFolioEntity.getIdx())).thenReturn(viewCount);
        Integer newViewCount = mockFrontPortFolioJpaRepository.updatePortfolioViewCount(frontPortFolioEntity.getIdx());

        // then
        assertThat(newViewCount).isEqualTo(viewCount);

        // verify
        then(mockFrontPortFolioJpaRepository).should(times(1)).updatePortfolioViewCount(frontPortFolioEntity.getIdx());
        then(mockFrontPortFolioJpaRepository).should(atLeastOnce()).updatePortfolioViewCount(frontPortFolioEntity.getIdx());
        then(mockFrontPortFolioJpaRepository).shouldHaveNoMoreInteractions();
    }
}
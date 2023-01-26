package com.tsp.api.portfolio.service;

import com.tsp.api.common.EntityType;
import com.tsp.api.domain.comment.AdminCommentDTO;
import com.tsp.api.domain.comment.AdminCommentEntity;
import com.tsp.api.domain.common.CommonImageDTO;
import com.tsp.api.domain.common.CommonImageEntity;
import com.tsp.api.domain.portfolio.AdminPortFolioDTO;
import com.tsp.api.domain.portfolio.AdminPortFolioEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.event.EventListener;
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
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("포트폴리오 Repository Test")
class AdminPortfolioJpaQueryRepositoryTest {
    @Mock private AdminPortfolioJpaQueryRepository mockAdminPortfolioJpaQueryRepository;
    private final AdminPortfolioJpaQueryRepository adminPortfolioJpaQueryRepository;
    private final AdminPortfolioJpaRepository adminPortfolioJpaRepository;

    private AdminPortFolioEntity adminPortFolioEntity;
    private AdminPortFolioDTO adminPortFolioDTO;
    private CommonImageEntity commonImageEntity;
    private CommonImageDTO commonImageDTO;
    private AdminCommentEntity adminCommentEntity;
    private AdminCommentDTO adminCommentDTO;

    void createPortfolioAndImage() {
        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        adminPortFolioDTO = AdminPortFolioEntity.toDto(adminPortFolioEntity);

        commonImageEntity = CommonImageEntity.builder()
                .idx(1L)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1L)
                .typeName(EntityType.PORTFOLIO)
                .build();

        commonImageDTO = CommonImageEntity.toDto(commonImageEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createPortfolioAndImage();
    }

    @Test
    @DisplayName("포트폴리오조회테스트")
    void 포트폴리오조회테스트() {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 100);

        // then
        assertThat(adminPortfolioJpaQueryRepository.findPortfolioList(portfolioMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("포트폴리오상세조회테스트")
    void 포트폴리오상세조회테스트() {
        // given
        adminPortFolioEntity = AdminPortFolioEntity.builder().idx(1L).build();

        // when
        adminPortFolioDTO = adminPortfolioJpaQueryRepository.findOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertAll(() -> {
                    assertThat(adminPortFolioDTO.getIdx()).isEqualTo(1);
                },
                () -> {
                    assertThat(adminPortFolioDTO.getTitle()).isEqualTo("포트폴리오 테스트");
                },
                () -> {
                    assertThat(adminPortFolioDTO.getDescription()).isEqualTo("포트폴리오 테스트");
                },
                () -> {
                    assertThat(adminPortFolioDTO.getHashTag()).isEqualTo("#test");
                },
                () -> {
                    assertThat(adminPortFolioDTO.getVideoUrl()).isEqualTo("https://youtube.com");
                });
    }

    @Test
    @DisplayName("포트폴리오Mockito조회테스트")
    void 포트폴리오Mockito조회테스트() {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminPortFolioDTO> portfolioList = new ArrayList<>();
        portfolioList.add(AdminPortFolioDTO.builder().idx(1L)
                .title("포트폴리오 테스트").description("포트폴리오 테스트")
                .portfolioImage(commonImageDtoList).build());

        Page<AdminPortFolioDTO> resultPortfolio = new PageImpl<>(portfolioList, pageRequest, portfolioList.size());

        // when
        when(mockAdminPortfolioJpaQueryRepository.findPortfolioList(portfolioMap, pageRequest)).thenReturn(resultPortfolio);
        Page<AdminPortFolioDTO> newPortfolioList = mockAdminPortfolioJpaQueryRepository.findPortfolioList(portfolioMap, pageRequest);
        List<AdminPortFolioDTO> findPortfolioList = newPortfolioList.stream().collect(Collectors.toList());

        // then
        assertThat(findPortfolioList.get(0).getIdx()).isEqualTo(portfolioList.get(0).getIdx());
        assertThat(findPortfolioList.get(0).getTitle()).isEqualTo(portfolioList.get(0).getTitle());
        assertThat(findPortfolioList.get(0).getDescription()).isEqualTo(portfolioList.get(0).getDescription());

        // verify
        verify(mockAdminPortfolioJpaQueryRepository, times(1)).findPortfolioList(portfolioMap, pageRequest);
        verify(mockAdminPortfolioJpaQueryRepository, atLeastOnce()).findPortfolioList(portfolioMap, pageRequest);
        verifyNoMoreInteractions(mockAdminPortfolioJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaQueryRepository);
        inOrder.verify(mockAdminPortfolioJpaQueryRepository).findPortfolioList(portfolioMap, pageRequest);
    }

    @Test
    @DisplayName("포트폴리오BDD조회테스트")
    void 포트폴리오BDD조회테스트() {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminPortFolioDTO> portfolioList = new ArrayList<>();
        portfolioList.add(AdminPortFolioDTO.builder().idx(1L)
                .title("포트폴리오 테스트").description("포트폴리오 테스트")
                .portfolioImage(commonImageDtoList).build());

        Page<AdminPortFolioDTO> resultPortfolio = new PageImpl<>(portfolioList, pageRequest, portfolioList.size());

        // when
        given(mockAdminPortfolioJpaQueryRepository.findPortfolioList(portfolioMap, pageRequest)).willReturn(resultPortfolio);
        Page<AdminPortFolioDTO> newPortfolioList = mockAdminPortfolioJpaQueryRepository.findPortfolioList(portfolioMap, pageRequest);
        List<AdminPortFolioDTO> findPortfolioList = newPortfolioList.stream().collect(Collectors.toList());

        // then
        assertThat(findPortfolioList.get(0).getIdx()).isEqualTo(portfolioList.get(0).getIdx());
        assertThat(findPortfolioList.get(0).getTitle()).isEqualTo(portfolioList.get(0).getTitle());
        assertThat(findPortfolioList.get(0).getDescription()).isEqualTo(portfolioList.get(0).getDescription());

        // verify
        then(mockAdminPortfolioJpaQueryRepository).should(times(1)).findPortfolioList(portfolioMap, pageRequest);
        then(mockAdminPortfolioJpaQueryRepository).should(atLeastOnce()).findPortfolioList(portfolioMap, pageRequest);
        then(mockAdminPortfolioJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("포트폴리오상세Mockito조회테스트")
    void 포트폴리오상세Mockito조회테스트() {
        // given
        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        adminPortFolioEntity = AdminPortFolioEntity.builder().idx(1L).commonImageEntityList(commonImageEntityList).build();

        adminPortFolioDTO = AdminPortFolioDTO.builder()
                .idx(1L)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .portfolioImage(CommonImageEntity.toDtoList(commonImageEntityList))
                .build();

        // when
        when(mockAdminPortfolioJpaQueryRepository.findOnePortfolio(adminPortFolioEntity.getIdx())).thenReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaQueryRepository.findOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(1);
        assertThat(portfolioInfo.getTitle()).isEqualTo("포트폴리오 테스트");
        assertThat(portfolioInfo.getDescription()).isEqualTo("포트폴리오 테스트");
        assertThat(portfolioInfo.getHashTag()).isEqualTo("#test");
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo("https://youtube.com");

        // verify
        verify(mockAdminPortfolioJpaQueryRepository, times(1)).findOnePortfolio(adminPortFolioEntity.getIdx());
        verify(mockAdminPortfolioJpaQueryRepository, atLeastOnce()).findOnePortfolio(adminPortFolioEntity.getIdx());
        verifyNoMoreInteractions(mockAdminPortfolioJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaQueryRepository);
        inOrder.verify(mockAdminPortfolioJpaQueryRepository).findOnePortfolio(adminPortFolioEntity.getIdx());
    }

    @Test
    @DisplayName("포트폴리오상세BDD조회테스트")
    void 포트폴리오상세BDD조회테스트() {
        // given
        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        adminPortFolioEntity = AdminPortFolioEntity.builder().idx(1L).commonImageEntityList(commonImageEntityList).build();

        adminPortFolioDTO = AdminPortFolioDTO.builder()
                .idx(1L)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .portfolioImage(CommonImageEntity.toDtoList(commonImageEntityList))
                .build();

        // when
        given(mockAdminPortfolioJpaQueryRepository.findOnePortfolio(adminPortFolioEntity.getIdx())).willReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaQueryRepository.findOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(1);
        assertThat(portfolioInfo.getTitle()).isEqualTo("포트폴리오 테스트");
        assertThat(portfolioInfo.getDescription()).isEqualTo("포트폴리오 테스트");
        assertThat(portfolioInfo.getHashTag()).isEqualTo("#test");
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo("https://youtube.com");

        // verify
        then(mockAdminPortfolioJpaQueryRepository).should(times(1)).findOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaQueryRepository).should(atLeastOnce()).findOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 포트폴리오 상세 조회 테스트")
    void 이전or다음포트폴리오상세조회테스트() {
        // given
        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(2L)
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        adminPortFolioDTO = adminPortfolioJpaQueryRepository.findOnePortfolio(adminPortFolioEntity.getIdx());

        // 이전 포트폴리오
        assertThat(adminPortfolioJpaQueryRepository.findPrevOnePortfolio(adminPortFolioEntity.getIdx()).getIdx()).isEqualTo(1);
        // 다음 포트폴리오
        assertThat(adminPortfolioJpaQueryRepository.findNextOnePortfolio(adminPortFolioEntity.getIdx()).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 포트폴리오 상세 조회 Mockito 테스트")
    void 이전포트폴리오상세조회Mockito테스트() {
        // given
        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(2L)
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        adminPortFolioDTO = adminPortfolioJpaQueryRepository.findOnePortfolio(adminPortFolioEntity.getIdx());

        when(mockAdminPortfolioJpaQueryRepository.findPrevOnePortfolio(adminPortFolioEntity.getIdx())).thenReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaQueryRepository.findPrevOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockAdminPortfolioJpaQueryRepository, times(1)).findPrevOnePortfolio(adminPortFolioEntity.getIdx());
        verify(mockAdminPortfolioJpaQueryRepository, atLeastOnce()).findPrevOnePortfolio(adminPortFolioEntity.getIdx());
        verifyNoMoreInteractions(mockAdminPortfolioJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaQueryRepository);
        inOrder.verify(mockAdminPortfolioJpaQueryRepository).findPrevOnePortfolio(adminPortFolioEntity.getIdx());
    }

    @Test
    @DisplayName("이전 포트폴리오 상세 조회 BDD 테스트")
    void 이전포트폴리오상세조회BDD테스트() {
        // given
        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(2L)
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        adminPortFolioDTO = adminPortfolioJpaQueryRepository.findOnePortfolio(adminPortFolioEntity.getIdx());

        given(mockAdminPortfolioJpaQueryRepository.findPrevOnePortfolio(adminPortFolioEntity.getIdx())).willReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaQueryRepository.findPrevOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockAdminPortfolioJpaQueryRepository).should(times(1)).findPrevOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaQueryRepository).should(atLeastOnce()).findPrevOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 포트폴리오 상세 조회 Mockito 테스트")
    void 다음포트폴리오상세조회Mockito테스트() {
        // given
        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(2L)
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        adminPortFolioDTO = adminPortfolioJpaQueryRepository.findOnePortfolio(adminPortFolioEntity.getIdx());

        when(mockAdminPortfolioJpaQueryRepository.findPrevOnePortfolio(adminPortFolioEntity.getIdx())).thenReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaQueryRepository.findPrevOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockAdminPortfolioJpaQueryRepository, times(1)).findNextOnePortfolio(adminPortFolioEntity.getIdx());
        verify(mockAdminPortfolioJpaQueryRepository, atLeastOnce()).findNextOnePortfolio(adminPortFolioEntity.getIdx());
        verifyNoMoreInteractions(mockAdminPortfolioJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaQueryRepository);
        inOrder.verify(mockAdminPortfolioJpaQueryRepository).findNextOnePortfolio(adminPortFolioEntity.getIdx());
    }

    @Test
    @DisplayName("다음 포트폴리오 상세 조회 BDD 테스트")
    void 다음포트폴리오상세조회BDD테스트() {
        // given
        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(2L)
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        adminPortFolioDTO = adminPortfolioJpaQueryRepository.findOnePortfolio(adminPortFolioEntity.getIdx());

        given(mockAdminPortfolioJpaQueryRepository.findPrevOnePortfolio(adminPortFolioEntity.getIdx())).willReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaQueryRepository.findPrevOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockAdminPortfolioJpaQueryRepository).should(times(1)).findNextOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaQueryRepository).should(atLeastOnce()).findNextOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaQueryRepository).shouldHaveNoMoreInteractions();
    }
}

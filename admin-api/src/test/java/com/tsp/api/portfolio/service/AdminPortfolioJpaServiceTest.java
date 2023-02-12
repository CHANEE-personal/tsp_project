package com.tsp.api.portfolio.service;

import com.tsp.api.common.service.AdminCommonJpaRepository;
import com.tsp.api.model.service.AdminModelCommonServiceTest;
import com.tsp.api.portfolio.domain.AdminPortFolioDTO;
import com.tsp.api.portfolio.domain.AdminPortFolioEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
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

import java.util.*;
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
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("포트폴리오 Service Test")
class AdminPortfolioJpaServiceTest extends AdminModelCommonServiceTest {
    @Mock private AdminCommonJpaRepository adminCommonJpaRepository;
    @Mock private AdminPortfolioJpaQueryRepository adminPortfolioJpaQueryRepository;
    @Mock private AdminPortfolioJpaRepository adminPortfolioJpaRepository;
    @InjectMocks private AdminPortfolioJpaServiceImpl mockAdminPortfolioJpaService;
    private final AdminPortfolioJpaService adminPortfolioJpaService;

    @Test
    @DisplayName("포트폴리오조회테스트")
    void 포트폴리오조회테스트() {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 100);

        // then
        assertThat(adminPortfolioJpaService.findPortfolioList(portfolioMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("포트폴리오 리스트 조회 Mockito 테스트")
    void 포트폴리오리스트조회Mockito테스트() {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<AdminPortFolioDTO> returnPortfolioList = new ArrayList<>();
        returnPortfolioList.add(AdminPortFolioDTO.builder()
                .idx(1L).title("portfolioTest").description("portfolioTest").hashTag("portfolio").videoUrl("test").visible("Y").build());
        Page<AdminPortFolioDTO> resultPortfolio = new PageImpl<>(returnPortfolioList, pageRequest, returnPortfolioList.size());

        // when
        when(adminPortfolioJpaQueryRepository.findPortfolioList(portfolioMap, pageRequest)).thenReturn(resultPortfolio);
        Page<AdminPortFolioDTO> portfolioList = mockAdminPortfolioJpaService.findPortfolioList(portfolioMap, pageRequest);
        List<AdminPortFolioDTO> findPortfolioList = portfolioList.stream().collect(Collectors.toList());

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
        verify(adminPortfolioJpaQueryRepository, times(1)).findPortfolioList(portfolioMap, pageRequest);
        verify(adminPortfolioJpaQueryRepository, atLeastOnce()).findPortfolioList(portfolioMap, pageRequest);
        verifyNoMoreInteractions(adminPortfolioJpaQueryRepository);

        InOrder inOrder = inOrder(adminPortfolioJpaQueryRepository);
        inOrder.verify(adminPortfolioJpaQueryRepository).findPortfolioList(portfolioMap, pageRequest);
    }

    @Test
    @DisplayName("포트폴리오 리스트 조회 BDD 테스트")
    void 포트폴리오리스트조회BDD테스트() {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<AdminPortFolioDTO> returnPortfolioList = new ArrayList<>();
        returnPortfolioList.add(AdminPortFolioDTO.builder()
                .idx(1L).title("portfolioTest").description("portfolioTest").hashTag("portfolio").videoUrl("test").visible("Y").build());
        Page<AdminPortFolioDTO> resultPortfolio = new PageImpl<>(returnPortfolioList, pageRequest, returnPortfolioList.size());

        // when
        given(adminPortfolioJpaQueryRepository.findPortfolioList(portfolioMap, pageRequest)).willReturn(resultPortfolio);
        Page<AdminPortFolioDTO> portfolioList = mockAdminPortfolioJpaService.findPortfolioList(portfolioMap, pageRequest);
        List<AdminPortFolioDTO> findPortfolioList = portfolioList.stream().collect(Collectors.toList());

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
        then(adminPortfolioJpaQueryRepository).should(times(1)).findPortfolioList(portfolioMap, pageRequest);
        then(adminPortfolioJpaQueryRepository).should(atLeastOnce()).findPortfolioList(portfolioMap, pageRequest);
        then(adminPortfolioJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("포트폴리오 상세 조회 테스트")
    void 포트폴리오상세조회테스트() {
        // then
        assertThat(adminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity.getIdx()).getTitle()).isNotEmpty();
    }

    @Test
    @DisplayName("포트폴리오 상세 조회 Mockito 테스트")
    void 포트폴리오상세조회Mockito테스트() {
        // when
        when(adminPortfolioJpaRepository.findByIdx(adminPortFolioEntity.getIdx())).thenReturn(Optional.ofNullable(adminPortFolioEntity));
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(adminPortFolioEntity.getIdx());
        assertThat(portfolioInfo.getTitle()).isEqualTo(adminPortFolioEntity.getTitle());
        assertThat(portfolioInfo.getDescription()).isEqualTo(adminPortFolioEntity.getDescription());
        assertThat(portfolioInfo.getHashTag()).isEqualTo(adminPortFolioEntity.getHashTag());
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo(adminPortFolioEntity.getVideoUrl());
        assertThat(portfolioInfo.getVisible()).isEqualTo(adminPortFolioEntity.getVisible());

        // verify
        verify(adminPortfolioJpaRepository, times(1)).findByIdx(adminPortFolioEntity.getIdx());
        verify(adminPortfolioJpaRepository, atLeastOnce()).findByIdx(adminPortFolioEntity.getIdx());
        verifyNoMoreInteractions(adminPortfolioJpaRepository);

        InOrder inOrder = inOrder(adminPortfolioJpaRepository);
        inOrder.verify(adminPortfolioJpaRepository).findByIdx(adminPortFolioEntity.getIdx());
    }

    @Test
    @DisplayName("포트폴리오 상세 조회 BDD 테스트")
    void 포트폴리오상세조회BDD테스트() {
        // when
        given(adminPortfolioJpaRepository.findByIdx(adminPortFolioEntity.getIdx())).willReturn(Optional.ofNullable(adminPortFolioEntity));
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(adminPortFolioEntity.getIdx());
        assertThat(portfolioInfo.getTitle()).isEqualTo(adminPortFolioEntity.getTitle());
        assertThat(portfolioInfo.getDescription()).isEqualTo(adminPortFolioEntity.getDescription());
        assertThat(portfolioInfo.getHashTag()).isEqualTo(adminPortFolioEntity.getHashTag());
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo(adminPortFolioEntity.getVideoUrl());
        assertThat(portfolioInfo.getVisible()).isEqualTo(adminPortFolioEntity.getVisible());

        // verify
        then(adminPortfolioJpaRepository).should(times(1)).findByIdx(adminPortFolioEntity.getIdx());
        then(adminPortfolioJpaRepository).should(atLeastOnce()).findByIdx(adminPortFolioEntity.getIdx());
        then(adminPortfolioJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 포트폴리오 상세 조회 Mockito 테스트")
    void 이전포트폴리오상세조회Mockito테스트() {
        // given
        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(2L)
                .newPortFolioJpaDTO(newCodeEntity)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        adminPortFolioDTO = adminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity.getIdx());

        when(mockAdminPortfolioJpaService.findPrevOnePortfolio(adminPortFolioEntity.getIdx())).thenReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.findPrevOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockAdminPortfolioJpaService, times(1)).findPrevOnePortfolio(adminPortFolioEntity.getIdx());
        verify(mockAdminPortfolioJpaService, atLeastOnce()).findPrevOnePortfolio(adminPortFolioEntity.getIdx());
        verifyNoMoreInteractions(mockAdminPortfolioJpaService);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaService);
        inOrder.verify(mockAdminPortfolioJpaService).findPrevOnePortfolio(adminPortFolioEntity.getIdx());
    }

    @Test
    @DisplayName("이전 포트폴리오 상세 조회 BDD 테스트")
    void 이전포트폴리오상세조회BDD테스트() {
        // given
        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(2L)
                .newPortFolioJpaDTO(newCodeEntity)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        adminPortFolioDTO = adminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity.getIdx());

        given(mockAdminPortfolioJpaService.findPrevOnePortfolio(adminPortFolioEntity.getIdx())).willReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.findPrevOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockAdminPortfolioJpaService).should(times(1)).findPrevOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaService).should(atLeastOnce()).findPrevOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 포트폴리오 상세 조회 Mockito 테스트")
    void 다음포트폴리오상세조회Mockito테스트() {
        // given
        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(2L)
                .newPortFolioJpaDTO(newCodeEntity)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        adminPortFolioDTO = adminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity.getIdx());

        when(mockAdminPortfolioJpaService.findPrevOnePortfolio(adminPortFolioEntity.getIdx())).thenReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.findPrevOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockAdminPortfolioJpaService, times(1)).findNextOnePortfolio(adminPortFolioEntity.getIdx());
        verify(mockAdminPortfolioJpaService, atLeastOnce()).findNextOnePortfolio(adminPortFolioEntity.getIdx());
        verifyNoMoreInteractions(mockAdminPortfolioJpaService);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaService);
        inOrder.verify(mockAdminPortfolioJpaService).findNextOnePortfolio(adminPortFolioEntity.getIdx());
    }

    @Test
    @DisplayName("다음 포트폴리오 상세 조회 BDD 테스트")
    void 다음포트폴리오상세조회BDD테스트() {
        // given
        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(2L)
                .newPortFolioJpaDTO(newCodeEntity)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        adminPortFolioDTO = adminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity.getIdx());

        given(mockAdminPortfolioJpaService.findPrevOnePortfolio(adminPortFolioEntity.getIdx())).willReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.findPrevOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockAdminPortfolioJpaService).should(times(1)).findNextOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaService).should(atLeastOnce()).findNextOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("포트폴리오 등록 Mockito 테스트")
    void 포트폴리오등록Mockito테스트() {
        // when
        when(adminCommonJpaRepository.findByCategoryCd(adminPortFolioEntity.getNewPortFolioJpaDTO().getCategoryCd())).thenReturn(Optional.ofNullable(newCodeEntity));
        when(adminPortfolioJpaRepository.save(adminPortFolioEntity)).thenReturn(adminPortFolioEntity);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.insertPortfolio(adminPortFolioEntity);

        // then
        assertThat(portfolioInfo.getTitle()).isEqualTo(adminPortFolioEntity.getTitle());
        assertThat(portfolioInfo.getDescription()).isEqualTo(adminPortFolioEntity.getDescription());
        assertThat(portfolioInfo.getHashTag()).isEqualTo(adminPortFolioEntity.getHashTag());
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo(adminPortFolioEntity.getVideoUrl());

        // verify
        verify(adminPortfolioJpaRepository, times(1)).save(adminPortFolioEntity);
        verify(adminPortfolioJpaRepository, atLeastOnce()).save(adminPortFolioEntity);
        verifyNoMoreInteractions(adminPortfolioJpaRepository);

        InOrder inOrder = inOrder(adminPortfolioJpaRepository);
        inOrder.verify(adminPortfolioJpaRepository).save(adminPortFolioEntity);
    }

    @Test
    @DisplayName("포트폴리오 등록 BDD 테스트")
    void 포트폴리오등록BDD테스트() {
        // when
        given(adminCommonJpaRepository.findByCategoryCd(adminPortFolioEntity.getNewPortFolioJpaDTO().getCategoryCd())).willReturn(Optional.ofNullable(newCodeEntity));
        given(adminPortfolioJpaRepository.save(adminPortFolioEntity)).willReturn(adminPortFolioEntity);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.insertPortfolio(adminPortFolioEntity);

        // then
        assertThat(portfolioInfo.getTitle()).isEqualTo(adminPortFolioEntity.getTitle());
        assertThat(portfolioInfo.getDescription()).isEqualTo(adminPortFolioEntity.getDescription());
        assertThat(portfolioInfo.getHashTag()).isEqualTo(adminPortFolioEntity.getHashTag());
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo(adminPortFolioEntity.getVideoUrl());

        // verify
        then(adminPortfolioJpaRepository).should(times(1)).save(adminPortFolioEntity);
        then(adminPortfolioJpaRepository).should(atLeastOnce()).save(adminPortFolioEntity);
        then(adminPortfolioJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("포트폴리오 수정 Mockito 테스트")
    void 포트폴리오수정Mockito테스트() {
        // given
        AdminPortFolioEntity updatePortfolio = AdminPortFolioEntity.builder()
                .idx(adminPortFolioEntity.getIdx())
                .newPortFolioJpaDTO(newCodeEntity)
                .title("포트폴리오 테스트1")
                .description("포트폴리오 테스트1")
                .hashTag("#test1")
                .videoUrl("https://test.com")
                .visible("Y")
                .build();

        // when
        when(adminPortfolioJpaRepository.findById(adminPortFolioEntity.getIdx())).thenReturn(Optional.ofNullable(adminPortFolioEntity));
        when(adminPortfolioJpaRepository.save(adminPortFolioEntity)).thenReturn(adminPortFolioEntity);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.updatePortfolio(updatePortfolio.getIdx(), updatePortfolio);

        // then
        assertThat(portfolioInfo.getTitle()).isEqualTo(updatePortfolio.getTitle());

        // verify
        verify(adminPortfolioJpaRepository, times(1)).findById(adminPortFolioEntity.getIdx());
        verify(adminPortfolioJpaRepository, atLeastOnce()).findById(adminPortFolioEntity.getIdx());
        verifyNoMoreInteractions(adminPortfolioJpaRepository);

        InOrder inOrder = inOrder(adminPortfolioJpaRepository);
        inOrder.verify(adminPortfolioJpaRepository).findById(adminPortFolioEntity.getIdx());
    }

    @Test
    @DisplayName("포트폴리오 수정 BDD 테스트")
    void 포트폴리오수정BDD테스트() {
        // given
        AdminPortFolioEntity updatePortfolio = AdminPortFolioEntity.builder()
                .idx(adminPortFolioEntity.getIdx())
                .newPortFolioJpaDTO(newCodeEntity)
                .title("포트폴리오 테스트1")
                .description("포트폴리오 테스트1")
                .hashTag("#test1")
                .videoUrl("https://test.com")
                .visible("Y")
                .build();

        // when
        given(adminPortfolioJpaRepository.findById(adminPortFolioEntity.getIdx())).willReturn(Optional.ofNullable(adminPortFolioEntity));
        given(adminPortfolioJpaRepository.save(adminPortFolioEntity)).willReturn(adminPortFolioEntity);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.updatePortfolio(updatePortfolio.getIdx(), updatePortfolio);

        // then
        assertThat(portfolioInfo.getTitle()).isEqualTo(updatePortfolio.getTitle());

        // verify
        then(adminPortfolioJpaRepository).should(times(1)).findById(adminPortFolioEntity.getIdx());
        then(adminPortfolioJpaRepository).should(atLeastOnce()).findById(adminPortFolioEntity.getIdx());
        then(adminPortfolioJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("포트폴리오 삭제 테스트")
    void 포트폴리오삭제테스트() {
        // given
        Long idx = adminPortfolioJpaService.insertPortfolio(adminPortFolioEntity).getIdx();

        // then
        assertThat(adminPortfolioJpaService.deletePortfolio(idx)).isNotNull();
    }
}

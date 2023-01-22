package com.tsp.api.portfolio.service;

import com.tsp.api.domain.comment.AdminCommentDTO;
import com.tsp.api.domain.comment.AdminCommentEntity;
import com.tsp.api.domain.common.NewCodeDTO;
import com.tsp.api.domain.common.NewCodeEntity;
import com.tsp.api.domain.portfolio.AdminPortFolioDTO;
import com.tsp.api.domain.portfolio.AdminPortFolioEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
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
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("포트폴리오 Service Test")
class AdminPortfolioJpaServiceTest {
    @Mock private AdminPortfolioJpaService mockAdminPortfolioJpaService;
    private final AdminPortfolioJpaService adminPortfolioJpaService;

    private AdminPortFolioEntity adminPortFolioEntity;
    private AdminPortFolioDTO adminPortFolioDTO;
    private AdminCommentEntity adminCommentEntity;
    private AdminCommentDTO adminCommentDTO;
    private NewCodeEntity newCodeEntity;
    private NewCodeDTO newCodeDTO;
    private final EntityManager em;

    void createPortfolio() {
        newCodeEntity = NewCodeEntity.builder()
                .categoryCd(10)
                .categoryNm("테스트")
                .cmmType("test")
                .visible("Y")
                .build();

        em.persist(newCodeEntity);
        newCodeDTO = NewCodeEntity.toDto(newCodeEntity);

        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .categoryCd(newCodeDTO.getCategoryCd())
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        adminPortFolioDTO = AdminPortFolioEntity.toDto(adminPortFolioEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createPortfolio();
    }

    @Test
    @DisplayName("포트폴리오조회테스트")
    void 포트폴리오조회테스트() {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 100);

        // then
        assertThat(adminPortfolioJpaService.findPortfolioList(portfolioMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("포트폴리오 리스트 조회 Mockito 테스트")
    void 포트폴리오리스트조회Mockito테스트() {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminPortFolioDTO> returnPortfolioList = new ArrayList<>();
        returnPortfolioList.add(AdminPortFolioDTO.builder()
                .idx(1L).title("portfolioTest").description("portfolioTest").hashTag("portfolio").videoUrl("test").visible("Y").build());
        Page<AdminPortFolioDTO> resultPortfolio = new PageImpl<>(returnPortfolioList, pageRequest, returnPortfolioList.size());

        // when
        when(mockAdminPortfolioJpaService.findPortfolioList(portfolioMap, pageRequest)).thenReturn(resultPortfolio);
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
        verify(mockAdminPortfolioJpaService, times(1)).findPortfolioList(portfolioMap, pageRequest);
        verify(mockAdminPortfolioJpaService, atLeastOnce()).findPortfolioList(portfolioMap, pageRequest);
        verifyNoMoreInteractions(mockAdminPortfolioJpaService);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaService);
        inOrder.verify(mockAdminPortfolioJpaService).findPortfolioList(portfolioMap, pageRequest);
    }

    @Test
    @DisplayName("포트폴리오 리스트 조회 BDD 테스트")
    void 포트폴리오리스트조회BDD테스트() {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminPortFolioDTO> returnPortfolioList = new ArrayList<>();
        returnPortfolioList.add(AdminPortFolioDTO.builder()
                .idx(1L).title("portfolioTest").description("portfolioTest").hashTag("portfolio").videoUrl("test").visible("Y").build());
        Page<AdminPortFolioDTO> resultPortfolio = new PageImpl<>(returnPortfolioList, pageRequest, returnPortfolioList.size());

        // when
        given(mockAdminPortfolioJpaService.findPortfolioList(portfolioMap, pageRequest)).willReturn(resultPortfolio);
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
        then(mockAdminPortfolioJpaService).should(times(1)).findPortfolioList(portfolioMap, pageRequest);
        then(mockAdminPortfolioJpaService).should(atLeastOnce()).findPortfolioList(portfolioMap, pageRequest);
        then(mockAdminPortfolioJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @Disabled
    @DisplayName("포트폴리오 상세 조회 테스트")
    void 포트폴리오상세조회테스트() {
        // given
        adminPortFolioEntity = AdminPortFolioEntity.builder().idx(1L).build();

        // then
        assertThat(adminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity.getIdx()).getTitle()).isNotEmpty();
    }

    @Test
    @DisplayName("포트폴리오 상세 조회 Mockito 테스트")
    void 포트폴리오상세조회Mockito테스트() {
        // when
        when(mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity.getIdx())).thenReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(adminPortFolioDTO.getIdx());
        assertThat(portfolioInfo.getTitle()).isEqualTo(adminPortFolioDTO.getTitle());
        assertThat(portfolioInfo.getDescription()).isEqualTo(adminPortFolioDTO.getDescription());
        assertThat(portfolioInfo.getHashTag()).isEqualTo(adminPortFolioDTO.getHashTag());
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo(adminPortFolioDTO.getVideoUrl());
        assertThat(portfolioInfo.getVisible()).isEqualTo(adminPortFolioDTO.getVisible());

        // verify
        verify(mockAdminPortfolioJpaService, times(1)).findOnePortfolio(adminPortFolioEntity.getIdx());
        verify(mockAdminPortfolioJpaService, atLeastOnce()).findOnePortfolio(adminPortFolioEntity.getIdx());
        verifyNoMoreInteractions(mockAdminPortfolioJpaService);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaService);
        inOrder.verify(mockAdminPortfolioJpaService).findOnePortfolio(adminPortFolioEntity.getIdx());
    }

    @Test
    @DisplayName("포트폴리오 상세 조회 BDD 테스트")
    void 포트폴리오상세조회BDD테스트() {
        // when
        given(mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity.getIdx())).willReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(adminPortFolioDTO.getIdx());
        assertThat(portfolioInfo.getTitle()).isEqualTo(adminPortFolioDTO.getTitle());
        assertThat(portfolioInfo.getDescription()).isEqualTo(adminPortFolioDTO.getDescription());
        assertThat(portfolioInfo.getHashTag()).isEqualTo(adminPortFolioDTO.getHashTag());
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo(adminPortFolioDTO.getVideoUrl());
        assertThat(portfolioInfo.getVisible()).isEqualTo(adminPortFolioDTO.getVisible());

        // verify
        then(mockAdminPortfolioJpaService).should(times(1)).findOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaService).should(atLeastOnce()).findOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaService).shouldHaveNoMoreInteractions();
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
                .categoryCd(1)
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
                .categoryCd(1)
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
                .categoryCd(1)
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
        // given
        adminPortfolioJpaService.insertPortfolio(adminPortFolioEntity);

        // when
        when(mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity.getIdx())).thenReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getTitle()).isEqualTo("포트폴리오 테스트");
        assertThat(portfolioInfo.getDescription()).isEqualTo("포트폴리오 테스트");
        assertThat(portfolioInfo.getHashTag()).isEqualTo("#test");
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo("https://youtube.com");

        // verify
        verify(mockAdminPortfolioJpaService, times(1)).findOnePortfolio(adminPortFolioEntity.getIdx());
        verify(mockAdminPortfolioJpaService, atLeastOnce()).findOnePortfolio(adminPortFolioEntity.getIdx());
        verifyNoMoreInteractions(mockAdminPortfolioJpaService);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaService);
        inOrder.verify(mockAdminPortfolioJpaService).findOnePortfolio(adminPortFolioEntity.getIdx());
    }

    @Test
    @DisplayName("포트폴리오 등록 BDD 테스트")
    void 포트폴리오등록BDD테스트() {
        // given
        adminPortfolioJpaService.insertPortfolio(adminPortFolioEntity);

        // when
        given(mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity.getIdx())).willReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getTitle()).isEqualTo("포트폴리오 테스트");
        assertThat(portfolioInfo.getDescription()).isEqualTo("포트폴리오 테스트");
        assertThat(portfolioInfo.getHashTag()).isEqualTo("#test");
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo("https://youtube.com");

        // verify
        then(mockAdminPortfolioJpaService).should(times(1)).findOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaService).should(atLeastOnce()).findOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("포트폴리오 수정 Mockito 테스트")
    void 포트폴리오수정Mockito테스트() {
        // given
        Long idx = adminPortfolioJpaService.insertPortfolio(adminPortFolioEntity).getIdx();

        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(idx)
                .categoryCd(1)
                .title("포트폴리오 테스트1")
                .description("포트폴리오 테스트1")
                .hashTag("#test1")
                .videoUrl("https://test.com")
                .visible("Y")
                .build();

        adminPortFolioDTO = AdminPortFolioEntity.toDto(adminPortFolioEntity);

        adminPortfolioJpaService.updatePortfolio(idx, adminPortFolioEntity);

        // when
        when(mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity.getIdx())).thenReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getTitle()).isEqualTo("포트폴리오 테스트1");

        // verify
        verify(mockAdminPortfolioJpaService, times(1)).findOnePortfolio(adminPortFolioEntity.getIdx());
        verify(mockAdminPortfolioJpaService, atLeastOnce()).findOnePortfolio(adminPortFolioEntity.getIdx());
        verifyNoMoreInteractions(mockAdminPortfolioJpaService);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaService);
        inOrder.verify(mockAdminPortfolioJpaService).findOnePortfolio(adminPortFolioEntity.getIdx());
    }

    @Test
    @DisplayName("포트폴리오 수정 BDD 테스트")
    void 포트폴리오수정BDD테스트() {
        // given
        Long idx = adminPortfolioJpaService.insertPortfolio(adminPortFolioEntity).getIdx();

        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(idx)
                .categoryCd(1)
                .title("포트폴리오 테스트1")
                .description("포트폴리오 테스트1")
                .hashTag("#test1")
                .videoUrl("https://test.com")
                .visible("Y")
                .build();

        adminPortFolioDTO = AdminPortFolioEntity.toDto(adminPortFolioEntity);

        adminPortfolioJpaService.updatePortfolio(idx, adminPortFolioEntity);

        // when
        given(mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity.getIdx())).willReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getTitle()).isEqualTo("포트폴리오 테스트1");

        // verify
        then(mockAdminPortfolioJpaService).should(times(1)).findOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaService).should(atLeastOnce()).findOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaService).shouldHaveNoMoreInteractions();
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

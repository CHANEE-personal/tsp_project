package com.tsp.new_tsp_front.api.production.service;

import com.tsp.new_tsp_front.api.production.domain.FrontProductionDTO;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
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
@DisplayName("프로덕션 Service Test")
class FrontProductionJpaApiServiceTest {
    @Mock private FrontProductionJpaApiService mockFrontProductionJpaApiService;
    private final FrontProductionJpaApiService frontProductionJpaApiService;

    @Test
    @DisplayName("프로덕션리스트조회Mockito테스트")
    void 프로덕션리스트조회Mockito테스트() {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        productionMap.put("jpaStartPage", 1);
        productionMap.put("size", 3);

        List<FrontProductionDTO> returnProductionList = new ArrayList<>();

        returnProductionList.add(FrontProductionDTO.builder().idx(1L).title("프로덕션테스트").description("프로덕션테스트").visible("Y").build());
        returnProductionList.add(FrontProductionDTO.builder().idx(2L).title("productionTest").description("productionTest").visible("Y").build());

        // when
        when(mockFrontProductionJpaApiService.getProductionList(productionMap)).thenReturn(returnProductionList);
        List<FrontProductionDTO> productionList = mockFrontProductionJpaApiService.getProductionList(productionMap);

        // then
        assertAll(
                () -> assertThat(productionList).isNotEmpty(),
                () -> assertThat(productionList).hasSize(2)
        );

        assertThat(productionList.get(0).getIdx()).isEqualTo(returnProductionList.get(0).getIdx());
        assertThat(productionList.get(0).getTitle()).isEqualTo(returnProductionList.get(0).getTitle());
        assertThat(productionList.get(0).getDescription()).isEqualTo(returnProductionList.get(0).getDescription());
        assertThat(productionList.get(0).getVisible()).isEqualTo(returnProductionList.get(0).getVisible());

        // verify
        verify(mockFrontProductionJpaApiService, times(1)).getProductionList(productionMap);
        verify(mockFrontProductionJpaApiService, atLeastOnce()).getProductionList(productionMap);
        verifyNoMoreInteractions(mockFrontProductionJpaApiService);

        InOrder inOrder = inOrder(mockFrontProductionJpaApiService);
        inOrder.verify(mockFrontProductionJpaApiService).getProductionList(productionMap);
    }

    @Test
    @DisplayName("프로덕션리스트조회BDD테스트")
    void 프로덕션리스트조회BDD테스트() {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        productionMap.put("jpaStartPage", 1);
        productionMap.put("size", 3);

        List<FrontProductionDTO> returnProductionList = new ArrayList<>();

        returnProductionList.add(FrontProductionDTO.builder().idx(1L).title("프로덕션테스트").description("프로덕션테스트").visible("Y").build());
        returnProductionList.add(FrontProductionDTO.builder().idx(2L).title("productionTest").description("productionTest").visible("Y").build());

        // when
        given(mockFrontProductionJpaApiService.getProductionList(productionMap)).willReturn(returnProductionList);
        List<FrontProductionDTO> productionList = mockFrontProductionJpaApiService.getProductionList(productionMap);

        // then
        assertAll(
                () -> assertThat(productionList).isNotEmpty(),
                () -> assertThat(productionList).hasSize(2)
        );

        assertThat(productionList.get(0).getIdx()).isEqualTo(returnProductionList.get(0).getIdx());
        assertThat(productionList.get(0).getTitle()).isEqualTo(returnProductionList.get(0).getTitle());
        assertThat(productionList.get(0).getDescription()).isEqualTo(returnProductionList.get(0).getDescription());
        assertThat(productionList.get(0).getVisible()).isEqualTo(returnProductionList.get(0).getVisible());

        // verify
        then(mockFrontProductionJpaApiService).should(times(1)).getProductionList(productionMap);
        then(mockFrontProductionJpaApiService).should(atLeastOnce()).getProductionList(productionMap);
        then(mockFrontProductionJpaApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션상세조회Mockito테스트")
    void 프로덕션상세조회Mockito테스트() {
        // given
        FrontProductionEntity frontProductionEntity = FrontProductionEntity.builder().idx(1L).build();
        FrontProductionDTO frontProductionDTO = FrontProductionDTO.builder().idx(1L).title("productionTest").description("productionTest").build();

        // when
        when(mockFrontProductionJpaApiService.getProductionInfo(frontProductionEntity)).thenReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaApiService.getProductionInfo(frontProductionEntity);

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(frontProductionDTO.getIdx());
        assertThat(productionInfo.getTitle()).isEqualTo(frontProductionDTO.getTitle());
        assertThat(productionInfo.getDescription()).isEqualTo(frontProductionDTO.getDescription());
        assertThat(productionInfo.getVisible()).isEqualTo(frontProductionDTO.getVisible());

        // verify
        verify(mockFrontProductionJpaApiService, times(1)).getProductionInfo(frontProductionEntity);
        verify(mockFrontProductionJpaApiService, atLeastOnce()).getProductionInfo(frontProductionEntity);
        verifyNoMoreInteractions(mockFrontProductionJpaApiService);

        InOrder inOrder = inOrder(mockFrontProductionJpaApiService);
        inOrder.verify(mockFrontProductionJpaApiService).getProductionInfo(frontProductionEntity);
    }

    @Test
    @DisplayName("프로덕션상세조회BDD테스트")
    void 프로덕션상세조회BDD테스트() {
        // given
        FrontProductionEntity frontProductionEntity = FrontProductionEntity.builder().idx(1L).build();
        FrontProductionDTO frontProductionDTO = FrontProductionDTO.builder().idx(1L).title("productionTest").description("productionTest").build();

        // when
        given(mockFrontProductionJpaApiService.getProductionInfo(frontProductionEntity)).willReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaApiService.getProductionInfo(frontProductionEntity);

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(frontProductionDTO.getIdx());
        assertThat(productionInfo.getTitle()).isEqualTo(frontProductionDTO.getTitle());
        assertThat(productionInfo.getDescription()).isEqualTo(frontProductionDTO.getDescription());
        assertThat(productionInfo.getVisible()).isEqualTo(frontProductionDTO.getVisible());

        // verify
        then(mockFrontProductionJpaApiService).should(times(1)).getProductionInfo(frontProductionEntity);
        then(mockFrontProductionJpaApiService).should(atLeastOnce()).getProductionInfo(frontProductionEntity);
        then(mockFrontProductionJpaApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 프로덕션 상세 조회 테스트")
    void 이전or다음프로덕션상세조회테스트() {
        // given
        FrontProductionEntity frontProductionEntity = FrontProductionEntity.builder().idx(118L).build();

        // when
        FrontProductionDTO frontProductionDTO = frontProductionJpaApiService.getProductionInfo(frontProductionEntity);

        // 이전 프로덕션
        assertThat(frontProductionJpaApiService.findPrevOneProduction(frontProductionEntity).getIdx()).isEqualTo(117);
        // 다음 프로덕션
        assertThat(frontProductionJpaApiService.findNextOneProduction(frontProductionEntity).getIdx()).isEqualTo(119);
    }

    @Test
    @DisplayName("이전 프로덕션 상세 조회 Mockito 테스트")
    void 이전프로덕션상세조회Mockito테스트() {
        // given
        FrontProductionEntity frontProductionEntity = FrontProductionEntity.builder().idx(118L).build();

        // when
        FrontProductionDTO frontProductionDTO = frontProductionJpaApiService.findPrevOneProduction(frontProductionEntity);

        when(mockFrontProductionJpaApiService.findPrevOneProduction(frontProductionEntity)).thenReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaApiService.findPrevOneProduction(frontProductionEntity);

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(117);

        // verify
        verify(mockFrontProductionJpaApiService, times(1)).findPrevOneProduction(frontProductionEntity);
        verify(mockFrontProductionJpaApiService, atLeastOnce()).findPrevOneProduction(frontProductionEntity);
        verifyNoMoreInteractions(mockFrontProductionJpaApiService);

        InOrder inOrder = inOrder(mockFrontProductionJpaApiService);
        inOrder.verify(mockFrontProductionJpaApiService).findPrevOneProduction(frontProductionEntity);
    }

    @Test
    @DisplayName("이전 프로덕션 상세 조회 BDD 테스트")
    void 이전프로덕션상세조회BDD테스트() {
        // given
        FrontProductionEntity frontProductionEntity = FrontProductionEntity.builder().idx(118L).build();

        // when
        FrontProductionDTO frontProductionDTO = frontProductionJpaApiService.findPrevOneProduction(frontProductionEntity);

        given(mockFrontProductionJpaApiService.findPrevOneProduction(frontProductionEntity)).willReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaApiService.findPrevOneProduction(frontProductionEntity);

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(117);

        // verify
        then(mockFrontProductionJpaApiService).should(times(1)).findPrevOneProduction(frontProductionEntity);
        then(mockFrontProductionJpaApiService).should(atLeastOnce()).findPrevOneProduction(frontProductionEntity);
        then(mockFrontProductionJpaApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 프로덕션 상세 조회 Mockito 테스트")
    void 다음프로덕션상세조회Mockito테스트() {
        // given
        FrontProductionEntity frontProductionEntity = FrontProductionEntity.builder().idx(118L).build();

        // when
        FrontProductionDTO frontProductionDTO = frontProductionJpaApiService.findNextOneProduction(frontProductionEntity);

        when(mockFrontProductionJpaApiService.findNextOneProduction(frontProductionEntity)).thenReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaApiService.findNextOneProduction(frontProductionEntity);

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(119);

        // verify
        verify(mockFrontProductionJpaApiService, times(1)).findNextOneProduction(frontProductionEntity);
        verify(mockFrontProductionJpaApiService, atLeastOnce()).findNextOneProduction(frontProductionEntity);
        verifyNoMoreInteractions(mockFrontProductionJpaApiService);

        InOrder inOrder = inOrder(mockFrontProductionJpaApiService);
        inOrder.verify(mockFrontProductionJpaApiService).findNextOneProduction(frontProductionEntity);
    }

    @Test
    @DisplayName("다음 프로덕션 상세 조회 BDD 테스트")
    void 다음프로덕션상세조회BDD테스트() {
        // given
        FrontProductionEntity frontProductionEntity = FrontProductionEntity.builder().idx(118L).build();

        // when
        FrontProductionDTO frontProductionDTO = frontProductionJpaApiService.findNextOneProduction(frontProductionEntity);

        given(mockFrontProductionJpaApiService.findNextOneProduction(frontProductionEntity)).willReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaApiService.findNextOneProduction(frontProductionEntity);

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(119);

        // verify
        then(mockFrontProductionJpaApiService).should(times(1)).findNextOneProduction(frontProductionEntity);
        then(mockFrontProductionJpaApiService).should(atLeastOnce()).findNextOneProduction(frontProductionEntity);
        then(mockFrontProductionJpaApiService).shouldHaveNoMoreInteractions();
    }
}
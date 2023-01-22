package com.tsp.api.production.service;

import com.tsp.api.production.domain.FrontProductionDTO;
import com.tsp.api.production.domain.FrontProductionEntity;
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
@DisplayName("프로덕션 Service Test")
class FrontProductionJpaApiServiceTest {
    @Mock private FrontProductionJpaApiService mockFrontProductionJpaApiService;
    private final FrontProductionJpaApiService frontProductionJpaApiService;

    @Test
    @DisplayName("프로덕션리스트조회Mockito테스트")
    void 프로덕션리스트조회Mockito테스트() {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontProductionDTO> returnProductionList = new ArrayList<>();

        returnProductionList.add(FrontProductionDTO.builder().idx(1L).title("프로덕션테스트").description("프로덕션테스트").visible("Y").build());
        returnProductionList.add(FrontProductionDTO.builder().idx(2L).title("productionTest").description("productionTest").visible("Y").build());

        Page<FrontProductionDTO> resultProduction = new PageImpl<>(returnProductionList, pageRequest, returnProductionList.size());
        // when
        when(mockFrontProductionJpaApiService.findProductionList(productionMap, pageRequest)).thenReturn(resultProduction);
        Page<FrontProductionDTO> productionList = mockFrontProductionJpaApiService.findProductionList(productionMap, pageRequest);
        List<FrontProductionDTO> findProductionList = productionList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findProductionList).isNotEmpty(),
                () -> assertThat(findProductionList).hasSize(2)
        );

        assertThat(findProductionList.get(0).getIdx()).isEqualTo(returnProductionList.get(0).getIdx());
        assertThat(findProductionList.get(0).getTitle()).isEqualTo(returnProductionList.get(0).getTitle());
        assertThat(findProductionList.get(0).getDescription()).isEqualTo(returnProductionList.get(0).getDescription());
        assertThat(findProductionList.get(0).getVisible()).isEqualTo(returnProductionList.get(0).getVisible());

        // verify
        verify(mockFrontProductionJpaApiService, times(1)).findProductionList(productionMap, pageRequest);
        verify(mockFrontProductionJpaApiService, atLeastOnce()).findProductionList(productionMap, pageRequest);
        verifyNoMoreInteractions(mockFrontProductionJpaApiService);

        InOrder inOrder = inOrder(mockFrontProductionJpaApiService);
        inOrder.verify(mockFrontProductionJpaApiService).findProductionList(productionMap, pageRequest);
    }

    @Test
    @DisplayName("프로덕션리스트조회BDD테스트")
    void 프로덕션리스트조회BDD테스트() {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontProductionDTO> returnProductionList = new ArrayList<>();

        returnProductionList.add(FrontProductionDTO.builder().idx(1L).title("프로덕션테스트").description("프로덕션테스트").visible("Y").build());
        returnProductionList.add(FrontProductionDTO.builder().idx(2L).title("productionTest").description("productionTest").visible("Y").build());

        Page<FrontProductionDTO> resultProduction = new PageImpl<>(returnProductionList, pageRequest, returnProductionList.size());
        // when
        given(mockFrontProductionJpaApiService.findProductionList(productionMap, pageRequest)).willReturn(resultProduction);
        Page<FrontProductionDTO> productionList = mockFrontProductionJpaApiService.findProductionList(productionMap, pageRequest);
        List<FrontProductionDTO> findProductionList = productionList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findProductionList).isNotEmpty(),
                () -> assertThat(findProductionList).hasSize(2)
        );

        assertThat(findProductionList.get(0).getIdx()).isEqualTo(returnProductionList.get(0).getIdx());
        assertThat(findProductionList.get(0).getTitle()).isEqualTo(returnProductionList.get(0).getTitle());
        assertThat(findProductionList.get(0).getDescription()).isEqualTo(returnProductionList.get(0).getDescription());
        assertThat(findProductionList.get(0).getVisible()).isEqualTo(returnProductionList.get(0).getVisible());

        // verify
        then(mockFrontProductionJpaApiService).should(times(1)).findProductionList(productionMap, pageRequest);
        then(mockFrontProductionJpaApiService).should(atLeastOnce()).findProductionList(productionMap, pageRequest);
        then(mockFrontProductionJpaApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션상세조회Mockito테스트")
    void 프로덕션상세조회Mockito테스트() {
        // given
        FrontProductionEntity frontProductionEntity = FrontProductionEntity.builder().idx(1L).build();
        FrontProductionDTO frontProductionDTO = FrontProductionDTO.builder().idx(1L).title("productionTest").description("productionTest").build();

        // when
        when(mockFrontProductionJpaApiService.findOneProduction(frontProductionEntity.getIdx())).thenReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaApiService.findOneProduction(frontProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(frontProductionDTO.getIdx());
        assertThat(productionInfo.getTitle()).isEqualTo(frontProductionDTO.getTitle());
        assertThat(productionInfo.getDescription()).isEqualTo(frontProductionDTO.getDescription());
        assertThat(productionInfo.getVisible()).isEqualTo(frontProductionDTO.getVisible());

        // verify
        verify(mockFrontProductionJpaApiService, times(1)).findOneProduction(frontProductionEntity.getIdx());
        verify(mockFrontProductionJpaApiService, atLeastOnce()).findOneProduction(frontProductionEntity.getIdx());
        verifyNoMoreInteractions(mockFrontProductionJpaApiService);

        InOrder inOrder = inOrder(mockFrontProductionJpaApiService);
        inOrder.verify(mockFrontProductionJpaApiService).findOneProduction(frontProductionEntity.getIdx());
    }

    @Test
    @DisplayName("프로덕션상세조회BDD테스트")
    void 프로덕션상세조회BDD테스트() {
        // given
        FrontProductionEntity frontProductionEntity = FrontProductionEntity.builder().idx(1L).build();
        FrontProductionDTO frontProductionDTO = FrontProductionDTO.builder().idx(1L).title("productionTest").description("productionTest").build();

        // when
        given(mockFrontProductionJpaApiService.findOneProduction(frontProductionEntity.getIdx())).willReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaApiService.findOneProduction(frontProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(frontProductionDTO.getIdx());
        assertThat(productionInfo.getTitle()).isEqualTo(frontProductionDTO.getTitle());
        assertThat(productionInfo.getDescription()).isEqualTo(frontProductionDTO.getDescription());
        assertThat(productionInfo.getVisible()).isEqualTo(frontProductionDTO.getVisible());

        // verify
        then(mockFrontProductionJpaApiService).should(times(1)).findOneProduction(frontProductionEntity.getIdx());
        then(mockFrontProductionJpaApiService).should(atLeastOnce()).findOneProduction(frontProductionEntity.getIdx());
        then(mockFrontProductionJpaApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 프로덕션 상세 조회 테스트")
    void 이전or다음프로덕션상세조회테스트() {
        // given
        FrontProductionEntity frontProductionEntity = FrontProductionEntity.builder().idx(118L).build();

        // when
        FrontProductionDTO frontProductionDTO = frontProductionJpaApiService.findOneProduction(frontProductionEntity.getIdx());

        // 이전 프로덕션
        assertThat(frontProductionJpaApiService.findPrevOneProduction(frontProductionEntity.getIdx()).getIdx()).isEqualTo(117);
        // 다음 프로덕션
        assertThat(frontProductionJpaApiService.findNextOneProduction(frontProductionEntity.getIdx()).getIdx()).isEqualTo(119);
    }

    @Test
    @DisplayName("이전 프로덕션 상세 조회 Mockito 테스트")
    void 이전프로덕션상세조회Mockito테스트() {
        // given
        FrontProductionEntity frontProductionEntity = FrontProductionEntity.builder().idx(118L).build();

        // when
        FrontProductionDTO frontProductionDTO = frontProductionJpaApiService.findPrevOneProduction(frontProductionEntity.getIdx());

        when(mockFrontProductionJpaApiService.findPrevOneProduction(frontProductionEntity.getIdx())).thenReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaApiService.findPrevOneProduction(frontProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(117);

        // verify
        verify(mockFrontProductionJpaApiService, times(1)).findPrevOneProduction(frontProductionEntity.getIdx());
        verify(mockFrontProductionJpaApiService, atLeastOnce()).findPrevOneProduction(frontProductionEntity.getIdx());
        verifyNoMoreInteractions(mockFrontProductionJpaApiService);

        InOrder inOrder = inOrder(mockFrontProductionJpaApiService);
        inOrder.verify(mockFrontProductionJpaApiService).findPrevOneProduction(frontProductionEntity.getIdx());
    }

    @Test
    @DisplayName("이전 프로덕션 상세 조회 BDD 테스트")
    void 이전프로덕션상세조회BDD테스트() {
        // given
        FrontProductionEntity frontProductionEntity = FrontProductionEntity.builder().idx(118L).build();

        // when
        FrontProductionDTO frontProductionDTO = frontProductionJpaApiService.findPrevOneProduction(frontProductionEntity.getIdx());

        given(mockFrontProductionJpaApiService.findPrevOneProduction(frontProductionEntity.getIdx())).willReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaApiService.findPrevOneProduction(frontProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(117);

        // verify
        then(mockFrontProductionJpaApiService).should(times(1)).findPrevOneProduction(frontProductionEntity.getIdx());
        then(mockFrontProductionJpaApiService).should(atLeastOnce()).findPrevOneProduction(frontProductionEntity.getIdx());
        then(mockFrontProductionJpaApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 프로덕션 상세 조회 Mockito 테스트")
    void 다음프로덕션상세조회Mockito테스트() {
        // given
        FrontProductionEntity frontProductionEntity = FrontProductionEntity.builder().idx(118L).build();

        // when
        FrontProductionDTO frontProductionDTO = frontProductionJpaApiService.findNextOneProduction(frontProductionEntity.getIdx());

        when(mockFrontProductionJpaApiService.findNextOneProduction(frontProductionEntity.getIdx())).thenReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaApiService.findNextOneProduction(frontProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(119);

        // verify
        verify(mockFrontProductionJpaApiService, times(1)).findNextOneProduction(frontProductionEntity.getIdx());
        verify(mockFrontProductionJpaApiService, atLeastOnce()).findNextOneProduction(frontProductionEntity.getIdx());
        verifyNoMoreInteractions(mockFrontProductionJpaApiService);

        InOrder inOrder = inOrder(mockFrontProductionJpaApiService);
        inOrder.verify(mockFrontProductionJpaApiService).findNextOneProduction(frontProductionEntity.getIdx());
    }

    @Test
    @DisplayName("다음 프로덕션 상세 조회 BDD 테스트")
    void 다음프로덕션상세조회BDD테스트() {
        // given
        FrontProductionEntity frontProductionEntity = FrontProductionEntity.builder().idx(118L).build();

        // when
        FrontProductionDTO frontProductionDTO = frontProductionJpaApiService.findNextOneProduction(frontProductionEntity.getIdx());

        given(mockFrontProductionJpaApiService.findNextOneProduction(frontProductionEntity.getIdx())).willReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaApiService.findNextOneProduction(frontProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(119);

        // verify
        then(mockFrontProductionJpaApiService).should(times(1)).findNextOneProduction(frontProductionEntity.getIdx());
        then(mockFrontProductionJpaApiService).should(atLeastOnce()).findNextOneProduction(frontProductionEntity.getIdx());
        then(mockFrontProductionJpaApiService).shouldHaveNoMoreInteractions();
    }
}

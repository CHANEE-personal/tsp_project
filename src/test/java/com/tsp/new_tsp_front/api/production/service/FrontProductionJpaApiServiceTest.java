package com.tsp.new_tsp_front.api.production.service;

import com.tsp.new_tsp_front.api.production.domain.FrontProductionDTO;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity;
import com.tsp.new_tsp_front.api.production.service.impl.FrontProductionJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("프로덕션 Service Test")
class FrontProductionJpaApiServiceTest {
    @Mock private FrontProductionJpaRepository frontProductionJpaRepository;
    @InjectMocks private FrontProductionJpaApiService frontProductionJpaApiService;

    @Test
    @DisplayName("프로덕션리스트조회Mockito테스트")
    void 프로덕션리스트조회Mockito테스트() {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        productionMap.put("jpaStartPage", 1);
        productionMap.put("size", 3);

        List<FrontProductionDTO> returnProductionList = new ArrayList<>();

        returnProductionList.add(FrontProductionDTO.builder().idx(1).title("프로덕션테스트").description("프로덕션테스트").visible("Y").build());
        returnProductionList.add(FrontProductionDTO.builder().idx(2).title("productionTest").description("productionTest").visible("Y").build());

        // when
        when(frontProductionJpaRepository.getProductionList(productionMap)).thenReturn(returnProductionList);
        List<FrontProductionDTO> productionList = frontProductionJpaApiService.getProductionList(productionMap);

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
        verify(frontProductionJpaRepository, times(1)).getProductionList(productionMap);
        verify(frontProductionJpaRepository, atLeastOnce()).getProductionList(productionMap);
        verifyNoMoreInteractions(frontProductionJpaRepository);

        InOrder inOrder = inOrder(frontProductionJpaRepository);
        inOrder.verify(frontProductionJpaRepository).getProductionList(productionMap);
    }

    @Test
    @DisplayName("프로덕션리스트조회BDD테스트")
    void 프로덕션리스트조회BDD테스트() {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        productionMap.put("jpaStartPage", 1);
        productionMap.put("size", 3);

        List<FrontProductionDTO> returnProductionList = new ArrayList<>();

        returnProductionList.add(FrontProductionDTO.builder().idx(1).title("프로덕션테스트").description("프로덕션테스트").visible("Y").build());
        returnProductionList.add(FrontProductionDTO.builder().idx(2).title("productionTest").description("productionTest").visible("Y").build());

        // when
        given(frontProductionJpaRepository.getProductionList(productionMap)).willReturn(returnProductionList);
        List<FrontProductionDTO> productionList = frontProductionJpaApiService.getProductionList(productionMap);

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
        then(frontProductionJpaRepository).should(times(1)).getProductionList(productionMap);
        then(frontProductionJpaRepository).should(atLeastOnce()).getProductionList(productionMap);
        then(frontProductionJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션상세조회Mockito테스트")
    void 프로덕션상세조회Mockito테스트() {
        // given
        FrontProductionEntity frontProductionEntity = FrontProductionEntity.builder().idx(1).build();
        FrontProductionDTO frontProductionDTO = FrontProductionDTO.builder().idx(1).title("productionTest").description("productionTest").build();

        // when
        when(frontProductionJpaRepository.getProductionInfo(frontProductionEntity)).thenReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = frontProductionJpaApiService.getProductionInfo(frontProductionEntity);

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(frontProductionDTO.getIdx());
        assertThat(productionInfo.getTitle()).isEqualTo(frontProductionDTO.getTitle());
        assertThat(productionInfo.getDescription()).isEqualTo(frontProductionDTO.getDescription());
        assertThat(productionInfo.getVisible()).isEqualTo(frontProductionDTO.getVisible());

        // verify
        verify(frontProductionJpaRepository, times(1)).getProductionInfo(frontProductionEntity);
        verify(frontProductionJpaRepository, atLeastOnce()).getProductionInfo(frontProductionEntity);
        verifyNoMoreInteractions(frontProductionJpaRepository);

        InOrder inOrder = inOrder(frontProductionJpaRepository);
        inOrder.verify(frontProductionJpaRepository).getProductionInfo(frontProductionEntity);
    }

    @Test
    @DisplayName("프로덕션상세조회BDD테스트")
    void 프로덕션상세조회BDD테스트() {
        // given
        FrontProductionEntity frontProductionEntity = FrontProductionEntity.builder().idx(1).build();
        FrontProductionDTO frontProductionDTO = FrontProductionDTO.builder().idx(1).title("productionTest").description("productionTest").build();

        // when
        given(frontProductionJpaRepository.getProductionInfo(frontProductionEntity)).willReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = frontProductionJpaApiService.getProductionInfo(frontProductionEntity);

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(frontProductionDTO.getIdx());
        assertThat(productionInfo.getTitle()).isEqualTo(frontProductionDTO.getTitle());
        assertThat(productionInfo.getDescription()).isEqualTo(frontProductionDTO.getDescription());
        assertThat(productionInfo.getVisible()).isEqualTo(frontProductionDTO.getVisible());

        // verify
        then(frontProductionJpaRepository).should(times(1)).getProductionInfo(frontProductionEntity);
        then(frontProductionJpaRepository).should(atLeastOnce()).getProductionInfo(frontProductionEntity);
        then(frontProductionJpaRepository).shouldHaveNoMoreInteractions();
    }
}
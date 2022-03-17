package com.tsp.new_tsp_front.api.production.service;

import com.tsp.new_tsp_front.api.production.domain.FrontProductionDTO;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity;
import com.tsp.new_tsp_front.api.production.service.impl.FrontProductionJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@Transactional
@ExtendWith(MockitoExtension.class)
@DisplayName("프로덕션 Service Test")
class FrontProductionJpaApiServiceTest {

    @Mock
    private FrontProductionJpaRepository frontProductionJpaRepository;

    @InjectMocks
    private FrontProductionJpaApiService frontProductionJpaApiService;

    @Test
    public void 프로덕션리스트조회테스트() throws Exception {
        // given
        ConcurrentHashMap<String, Object> productionMap = new ConcurrentHashMap<>();
        productionMap.put("jpaStartPage", 1);
        productionMap.put("size", 3);

        List<FrontProductionDTO> returnProductionList = new ArrayList<>();

        returnProductionList.add(FrontProductionDTO.builder().idx(1).title("프로덕션테스트").description("프로덕션테스트").visible("Y").build());
        returnProductionList.add(FrontProductionDTO.builder().idx(2).title("productionTest").description("productionTest").visible("Y").build());

        given(frontProductionJpaRepository.getProductionList(productionMap)).willReturn(returnProductionList);

        // when
        List<FrontProductionDTO> productionList = frontProductionJpaApiService.getProductionList(productionMap);

        // then
        assertThat(productionList.size()).isGreaterThan(0);
        assertThat(productionList.size()).isEqualTo(2);

        assertThat(productionList.get(0).getIdx()).isEqualTo(returnProductionList.get(0).getIdx());
        assertThat(productionList.get(0).getTitle()).isEqualTo(returnProductionList.get(0).getTitle());
        assertThat(productionList.get(0).getDescription()).isEqualTo(returnProductionList.get(0).getDescription());
        assertThat(productionList.get(0).getVisible()).isEqualTo(returnProductionList.get(0).getVisible());
    }

    @Test
    public void 프로덕션상세조회테스트() throws Exception {
        // given
        FrontProductionEntity frontProductionEntity = FrontProductionEntity.builder().idx(1).build();
        FrontProductionDTO frontProductionDTO = FrontProductionDTO.builder().idx(1).title("productionTest").description("productionTest").build();
        given(frontProductionJpaRepository.getProductionInfo(frontProductionEntity)).willReturn(frontProductionDTO);

        // when
        FrontProductionDTO productionInfo = frontProductionJpaApiService.getProductionInfo(frontProductionEntity);

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(frontProductionDTO.getIdx());
        assertThat(productionInfo.getTitle()).isEqualTo(frontProductionDTO.getTitle());
        assertThat(productionInfo.getDescription()).isEqualTo(frontProductionDTO.getDescription());
        assertThat(productionInfo.getVisible()).isEqualTo(frontProductionDTO.getVisible());
    }

}
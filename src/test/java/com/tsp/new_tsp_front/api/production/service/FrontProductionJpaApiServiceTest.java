package com.tsp.new_tsp_front.api.production.service;

import com.tsp.new_tsp_front.api.production.domain.FrontProductionDTO;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity;
import com.tsp.new_tsp_front.api.production.service.impl.FrontProductionJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import static com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity.builder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
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
    void 프로덕션리스트조회테스트() {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        productionMap.put("jpaStartPage", 1);
        productionMap.put("size", 3);

        List<FrontProductionDTO> returnProductionList = new ArrayList<>();

        returnProductionList.add(FrontProductionDTO.builder().idx(1).title("프로덕션테스트").description("프로덕션테스트").visible("Y").build());
        returnProductionList.add(FrontProductionDTO.builder().idx(2).title("productionTest").description("productionTest").visible("Y").build());

        given(frontProductionJpaRepository.getProductionList(productionMap)).willReturn(returnProductionList);

        // when
        List<FrontProductionDTO> productionList = frontProductionJpaApiService.getProductionList(productionMap);

        // then
        assertAll(
                () -> assertThat(productionList).isNotEmpty(),
                () -> assertThat(productionList).hasSize(3)
        );

        assertThat(productionList.get(0).getIdx()).isEqualTo(returnProductionList.get(0).getIdx());
        assertThat(productionList.get(0).getTitle()).isEqualTo(returnProductionList.get(0).getTitle());
        assertThat(productionList.get(0).getDescription()).isEqualTo(returnProductionList.get(0).getDescription());
        assertThat(productionList.get(0).getVisible()).isEqualTo(returnProductionList.get(0).getVisible());
    }

    @Test
    void 프로덕션상세조회테스트() {
        // given
        FrontProductionEntity frontProductionEntity = builder().idx(1).build();
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
package com.tsp.new_tsp_front.api.production.service.impl;

import com.tsp.new_tsp_front.api.common.domain.CommonImageDTO;
import com.tsp.new_tsp_front.api.common.domain.CommonImageEntity;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionDTO;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity;
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
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("프로덕션 Repository Test")
class FrontProductionJpaRepositoryTest {
    private FrontProductionEntity frontProductionEntity;
    private FrontProductionDTO frontProductionDTO;
    private CommonImageEntity commonImageEntity;
    private CommonImageDTO commonImageDTO;

    List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
    @Autowired
    private FrontProductionJpaRepository frontProductionJpaRepository;

    @Mock
    private FrontProductionJpaRepository mockFrontProductionJpaRepository;

    private void createProduction() {
        commonImageEntity = CommonImageEntity.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("production")
                .build();

        commonImageDTO = ProductionImageMapperImpl.INSTANCE.toDto(commonImageEntity);

        commonImageEntityList.add(commonImageEntity);

        frontProductionDTO = FrontProductionDTO.builder()
                .idx(1)
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .productionImage(ProductionImageMapper.INSTANCE.toDtoList(commonImageEntityList))
                .build();

        frontProductionEntity = builder().idx(1).commonImageEntityList(commonImageEntityList).build();
    }

    @BeforeEach
    void init() {
        createProduction();
    }

    @Test
    @DisplayName("프로덕션 리스트 조회 테스트")
    void 프로덕션리스트조회테스트() {

        // given
        Map<String, Object> productionMap = new HashMap<>();
        productionMap.put("jpaStartPage", 1);
        productionMap.put("size", 3);
        productionMap.put("searchType", 0);
        productionMap.put("searchKeyword", "하하");

        // then
        assertThat(frontProductionJpaRepository.getProductionList(productionMap).size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("프로덕션 BDD 조회 테스트")
    void 프로덕션BDD조회테스트() {
        // given
        ConcurrentHashMap<String, Object> productionMap = new ConcurrentHashMap<>();
        productionMap.put("jpaStartPage", 1);
        productionMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<FrontProductionDTO> productionList = new ArrayList<>();
        productionList.add(FrontProductionDTO.builder().idx(1).title("프로덕션").description("프로덕션").visible("Y")
                .productionImage(commonImageDtoList).build());

        // when
//        given(mockFrontProductionJpaRepository.getProductionList(productionMap)).willReturn(productionList);
        when(mockFrontProductionJpaRepository.getProductionList(productionMap)).thenReturn(productionList);

        // then
        assertThat(mockFrontProductionJpaRepository.getProductionList(productionMap).get(0).getIdx()).isEqualTo(productionList.get(0).getIdx());
        assertThat(mockFrontProductionJpaRepository.getProductionList(productionMap).get(0).getTitle()).isEqualTo(productionList.get(0).getTitle());
        assertThat(mockFrontProductionJpaRepository.getProductionList(productionMap).get(0).getDescription()).isEqualTo(productionList.get(0).getDescription());
        assertThat(mockFrontProductionJpaRepository.getProductionList(productionMap).get(0).getProductionImage().get(0).getFileName()).isEqualTo(productionList.get(0).getProductionImage().get(0).getFileName());
        assertThat(mockFrontProductionJpaRepository.getProductionList(productionMap).get(0).getProductionImage().get(0).getTypeName()).isEqualTo(productionList.get(0).getProductionImage().get(0).getTypeName());

        // verify
        verify(mockFrontProductionJpaRepository, times(5)).getProductionList(productionMap);
        verify(mockFrontProductionJpaRepository, atLeastOnce()).getProductionList(productionMap);
    }

    @Test
    @DisplayName("프로덕션 상세 BDD 조회 테스트")
    void 프로덕션상세BDD조회테스트() {

        // given
//        given(mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity)).willReturn(frontProductionDTO);
        when(mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity)).thenReturn(frontProductionDTO);

        // then
        assertThat(mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity).getIdx()).isEqualTo(1);
        assertThat(mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity).getTitle()).isEqualTo("프로덕션 테스트");
        assertThat(mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity).getDescription()).isEqualTo("프로덕션 테스트");
        assertThat(mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity).getVisible()).isEqualTo("Y");
        assertThat(mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity).getProductionImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity).getProductionImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity).getProductionImage().get(0).getImageType()).isEqualTo("main");
        assertThat(mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity).getProductionImage().get(0).getTypeName()).isEqualTo("production");

        // verify
        verify(mockFrontProductionJpaRepository, times(8)).getProductionInfo(frontProductionEntity);
        verify(mockFrontProductionJpaRepository, atLeastOnce()).getProductionInfo(frontProductionEntity);
    }
}
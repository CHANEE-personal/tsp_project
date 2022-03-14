package com.tsp.new_tsp_front.api.production.service.impl;

import com.tsp.new_tsp_front.api.common.domain.CommonImageEntity;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionDTO;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity;
import com.tsp.new_tsp_front.api.production.service.FrontProductionJpaApiService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("프로덕션 Repository Test")
class FrontProductionJpaRepositoryTest {
    @Autowired
    private FrontProductionJpaRepository frontProductionJpaRepository;

    @Mock
    private FrontProductionJpaRepository mockFrontProductionJpaRepository;

    @InjectMocks
    private FrontProductionJpaApiService frontProductionJpaApiService;

    @Test
    public void 프로덕션리스트조회테스트() throws Exception {

        // given
        ConcurrentHashMap<String, Object> productionMap = new ConcurrentHashMap<>();
        productionMap.put("jpaStartPage", 1);
        productionMap.put("size", 3);

        // when
        List<FrontProductionDTO> productionList = frontProductionJpaRepository.getProductionList(productionMap);

        // then
        assertThat(productionList.size()).isGreaterThan(0);
    }

    @Test
    public void 프로덕션상세조회테스트() throws Exception {
        // given
        FrontProductionEntity frontProductionEntity = builder().idx(1).build();

        // when
        FrontProductionDTO productionInfo = frontProductionJpaRepository.getProductionInfo(frontProductionEntity);

        // then
        assertAll(() -> assertThat(productionInfo.getIdx()).isEqualTo(1),
                () -> {
                    assertThat(productionInfo.getTitle()).isEqualTo("프로덕션 테스트");
                    assertNotNull(productionInfo.getTitle());
                },
                () -> {
                    assertThat(productionInfo.getDescription()).isEqualTo("프로덕션 테스트");
                    assertNotNull(productionInfo.getDescription());
                },
                () -> {
                    assertThat(productionInfo.getVisible()).isEqualTo("Y");
                    assertNotNull(productionInfo.getVisible());
                });

        assertThat(productionInfo.getProductionImage().get(0).getTypeName()).isEqualTo("production");
        assertThat(productionInfo.getProductionImage().get(0).getFileName()).isEqualTo("52d4fdc8-f109-408e-b243-85cc1be207c5.jpg");
        assertThat(productionInfo.getProductionImage().get(0).getFilePath()).isEqualTo("/var/www/dist/upload/1223024921206.jpg");
    }

    @Test
    public void 프로덕션상세BDD조회테스트() throws Exception {

        // given
        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("production")
                .build();

        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        FrontProductionEntity frontProductionEntity = builder().idx(1).commonImageEntityList(commonImageEntityList).build();

        FrontProductionDTO productionDTO = FrontProductionDTO.builder()
                .idx(1)
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .productionImage(ProductionImageMapper.INSTANCE.toDtoList(commonImageEntityList))
                .build();

        given(mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity)).willReturn(productionDTO);

        // when
        Integer idx = mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity).getIdx();
        String title = mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity).getTitle();
        String description = mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity).getDescription();
        String visible = mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity).getVisible();
        String fileName = mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity).getProductionImage().get(0).getFileName();
        String fileMask = mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity).getProductionImage().get(0).getFileMask();
        String imageType = mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity).getProductionImage().get(0).getImageType();
        String typeName = mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity).getProductionImage().get(0).getTypeName();

        // then
        assertThat(idx).isEqualTo(1);
        assertThat(title).isEqualTo("프로덕션 테스트");
        assertThat(description).isEqualTo("프로덕션 테스트");
        assertThat(visible).isEqualTo("Y");
        assertThat(fileName).isEqualTo("test.jpg");
        assertThat(fileMask).isEqualTo("test.jpg");
        assertThat(imageType).isEqualTo("main");
        assertThat(typeName).isEqualTo("production");
    }
}
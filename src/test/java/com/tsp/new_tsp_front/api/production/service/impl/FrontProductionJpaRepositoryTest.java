package com.tsp.new_tsp_front.api.production.service.impl;

import com.tsp.new_tsp_front.api.common.domain.CommonImageDTO;
import com.tsp.new_tsp_front.api.common.domain.CommonImageEntity;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionDTO;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity;
import com.tsp.new_tsp_front.api.production.service.FrontProductionJpaApiService;
import org.junit.jupiter.api.BeforeEach;
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
    private FrontProductionEntity frontProductionEntity;
    private FrontProductionDTO frontProductionDTO;
    private CommonImageEntity commonImageEntity;
    private CommonImageDTO commonImageDTO;

    List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
    @Autowired
    private FrontProductionJpaRepository frontProductionJpaRepository;

    @Mock
    private FrontProductionJpaRepository mockFrontProductionJpaRepository;

    @InjectMocks
    private FrontProductionJpaApiService frontProductionJpaApiService;

    @BeforeEach
    public void init() {
        frontProductionEntity = builder().idx(1).commonImageEntityList(commonImageEntityList).build();

        commonImageEntity = CommonImageEntity.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("production")
                .build();

        commonImageDTO = CommonImageDTO.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("production")
                .build();

        commonImageEntityList.add(commonImageEntity);

        frontProductionDTO = FrontProductionDTO.builder()
                .idx(1)
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .productionImage(ProductionImageMapper.INSTANCE.toDtoList(commonImageEntityList))
                .build();
    }

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
    public void 프로덕션BDD조회테스트() throws Exception {
        // given
        ConcurrentHashMap<String, Object> productionMap = new ConcurrentHashMap<>();
        productionMap.put("jpaStartPage", 1);
        productionMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<FrontProductionDTO> productionList = new ArrayList<>();
        productionList.add(FrontProductionDTO.builder().idx(1).title("프로덕션").description("프로덕션").visible("Y")
                .productionImage(commonImageDtoList).build());

        given(mockFrontProductionJpaRepository.getProductionList(productionMap)).willReturn(productionList);

        // when
        Integer idx = mockFrontProductionJpaRepository.getProductionList(productionMap).get(0).getIdx();
        String title = mockFrontProductionJpaRepository.getProductionList(productionMap).get(0).getTitle();
        String description = mockFrontProductionJpaRepository.getProductionList(productionMap).get(0).getDescription();
        String fileName = mockFrontProductionJpaRepository.getProductionList(productionMap).get(0).getProductionImage().get(0).getFileName();
        String typeName = mockFrontProductionJpaRepository.getProductionList(productionMap).get(0).getProductionImage().get(0).getTypeName();

        assertThat(idx).isEqualTo(productionList.get(0).getIdx());
        assertThat(title).isEqualTo(productionList.get(0).getTitle());
        assertThat(description).isEqualTo(productionList.get(0).getDescription());
        assertThat(fileName).isEqualTo(productionList.get(0).getProductionImage().get(0).getFileName());
        assertThat(typeName).isEqualTo(productionList.get(0).getProductionImage().get(0).getTypeName());
    }

    @Test
    public void 프로덕션상세BDD조회테스트() throws Exception {

        // given
        given(mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity)).willReturn(frontProductionDTO);

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
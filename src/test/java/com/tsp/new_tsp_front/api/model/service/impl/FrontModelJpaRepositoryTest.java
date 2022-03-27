package com.tsp.new_tsp_front.api.model.service.impl;

import com.tsp.new_tsp_front.api.common.domain.CommonImageEntity;
import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
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
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_front.api.model.domain.FrontModelEntity.builder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("모델 Repository Test")
class FrontModelJpaRepositoryTest {

    @Autowired
    private FrontModelJpaRepository frontModelJpaRepository;

    @Mock
    private FrontModelJpaRepository mockFrontModelJpaRepository;

    @Test
    public void 모델리스트조회테스트() throws Exception {

        // given
        ConcurrentHashMap<String, Object> modelMap = new ConcurrentHashMap<>();
        modelMap.put("categoryCd", "1");
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        // when
        List<FrontModelDTO> modelList = frontModelJpaRepository.getModelList(modelMap);

        // then
        assertThat(modelList.size()).isGreaterThan(0);
    }

    @Test
    public void 모델상세조회테스트() {
        // given
        FrontModelEntity frontModelEntity = builder().categoryCd(1).idx(3).build();

        // when
        FrontModelDTO modelInfo = frontModelJpaRepository.getModelInfo(frontModelEntity);

        // then
        assertAll(() -> assertThat(modelInfo.getIdx()).isEqualTo(3),
                () -> {
                    assertThat(modelInfo.getCategoryCd()).isEqualTo(1);
                    assertNotNull(modelInfo.getCategoryCd());
                },
                () -> {
                    assertThat(modelInfo.getCategoryAge()).isEqualTo("2");
                    assertNotNull(modelInfo.getCategoryAge());
                },
                () -> {
                    assertThat(modelInfo.getModelKorName()).isEqualTo("조찬희");
                    assertNotNull(modelInfo.getModelKorName());
                },
                () -> {
                    assertThat(modelInfo.getModelEngName()).isEqualTo("CHOCHANHEE");
                    assertNotNull(modelInfo.getModelEngName());
                },
                () -> {
                    assertThat(modelInfo.getModelDescription()).isEqualTo("chaneeCho");
                    assertNotNull(modelInfo.getModelDescription());
                },
                () -> {
                    assertThat(modelInfo.getHeight()).isEqualTo("170");
                    assertNotNull(modelInfo.getHeight());
                },
                () -> {
                    assertThat(modelInfo.getSize3()).isEqualTo("34-24-34");
                    assertNotNull(modelInfo.getSize3());
                },
                () -> {
                    assertThat(modelInfo.getShoes()).isEqualTo("270");
                    assertNotNull(modelInfo.getShoes());
                },
                () -> {
                    assertThat(modelInfo.getVisible()).isEqualTo("Y");
                    assertNotNull(modelInfo.getVisible());
                });

        assertThat(modelInfo.getModelImage().get(0).getTypeName()).isEqualTo("model");
        assertThat(modelInfo.getModelImage().get(0).getImageType()).isEqualTo("main");
        assertThat(modelInfo.getModelImage().get(0).getFileName()).isEqualTo("52d4fdc8-f109-408e-b243-85cc1be207c5.jpg");
        assertThat(modelInfo.getModelImage().get(0).getFilePath()).isEqualTo("/var/www/dist/upload/1223023959779.jpg");

        assertThat(modelInfo.getModelImage().get(1).getTypeName()).isEqualTo("model");
        assertThat(modelInfo.getModelImage().get(1).getImageType()).isEqualTo("sub1");
        assertThat(modelInfo.getModelImage().get(1).getFileName()).isEqualTo("e13f6930-17a5-407c-96ed-fd625b720d21.jpg");
        assertThat(modelInfo.getModelImage().get(1).getFilePath()).isEqualTo("/var/www/dist/upload/1223023959823.jpg");
    }

    @Test
    public void 모델상세BDD조회테스트() throws Exception {

        // given
        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("model")
                .build();

        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        FrontModelEntity frontModelEntity = builder().idx(1).commonImageEntityList(commonImageEntityList).build();

        FrontModelDTO frontModelDTO = FrontModelDTO.builder()
                .idx(1)
                .categoryCd(1)
                .categoryAge("2")
                .modelKorName("조찬희")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .height("170")
                .size3("34-24-34")
                .shoes("270")
                .visible("Y")
                .modelImage(ModelImageMapper.INSTANCE.toDtoList(commonImageEntityList))
                .build();

        given(mockFrontModelJpaRepository.getModelInfo(frontModelEntity)).willReturn(frontModelDTO);

        // when
        Integer idx = mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getIdx();
        Integer categoryCd = mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getCategoryCd();
        String categoryAge = mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getCategoryAge();
        String modelKorName = mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getModelKorName();
        String modelEngName = mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getModelEngName();
        String modelDescription = mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getModelDescription();
        String height = mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getHeight();
        String size3 = mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getSize3();
        String shoes = mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getShoes();
        String visible = mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getVisible();
        String fileName = mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getModelImage().get(0).getFileName();
        String fileMask = mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getModelImage().get(0).getFileMask();
        String filePath = mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getModelImage().get(0).getFilePath();
        String imageType = mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getModelImage().get(0).getImageType();
        String typeName = mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getModelImage().get(0).getTypeName();

        assertThat(idx).isEqualTo(1);
        assertThat(categoryCd).isEqualTo(1);
        assertThat(categoryAge).isEqualTo("2");
        assertThat(modelKorName).isEqualTo("조찬희");
        assertThat(modelEngName).isEqualTo("CHOCHANHEE");
        assertThat(modelDescription).isEqualTo("chaneeCho");
        assertThat(height).isEqualTo("170");
        assertThat(size3).isEqualTo("34-24-34");
        assertThat(shoes).isEqualTo("270");
        assertThat(visible).isEqualTo("Y");
        assertThat(fileName).isEqualTo("test.jpg");
        assertThat(fileMask).isEqualTo("test.jpg");
        assertThat(filePath).isEqualTo("/test/test.jpg");
        assertThat(imageType).isEqualTo("main");
        assertThat(typeName).isEqualTo("model");
    }

    @Test
    public void 모델배너리스트조회테스트() throws Exception {

        // given
        ConcurrentHashMap<String, Object> modelMap = new ConcurrentHashMap<>();
        modelMap.put("categoryCd", "1");

        // when
        List<FrontModelDTO> mainModelList = frontModelJpaRepository.getMainModelList(modelMap);

        Optional<FrontModelDTO> mainModelFirstInfo = frontModelJpaRepository.getMainModelList(modelMap).stream().findFirst();

        // then
        assertThat(mainModelList.size()).isGreaterThan(0);
        assertThat(mainModelFirstInfo.get().getCategoryCd()).isEqualTo(1);
        assertThat(mainModelFirstInfo.get().getModelMainYn()).isEqualTo("Y");
    }
}
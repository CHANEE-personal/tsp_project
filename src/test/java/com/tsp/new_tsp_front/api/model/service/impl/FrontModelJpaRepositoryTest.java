package com.tsp.new_tsp_front.api.model.service.impl;

import com.tsp.new_tsp_front.api.common.domain.CommonImageDTO;
import com.tsp.new_tsp_front.api.common.domain.CommonImageEntity;
import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
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
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_front.api.model.domain.FrontModelEntity.builder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("모델 Repository Test")
class FrontModelJpaRepositoryTest {

    private FrontModelEntity frontModelEntity;
    private FrontModelDTO frontModelDTO;
    private CommonImageEntity commonImageEntity;
    private CommonImageDTO commonImageDTO;
    List<CommonImageEntity> commonImageEntityList = new ArrayList<>();


    @Autowired
    private FrontModelJpaRepository frontModelJpaRepository;

    @Mock
    private FrontModelJpaRepository mockFrontModelJpaRepository;

    @BeforeEach
    public void init() {
        frontModelEntity = builder()
                .categoryCd(1)
                .categoryAge("2")
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .height("170")
                .size3("34-24-34")
                .shoes("270")
                .visible("Y")
                .build();

        frontModelDTO = FrontModelDTO.builder()
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

        commonImageEntity = CommonImageEntity.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("model")
                .build();

        commonImageDTO = CommonImageDTO.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("model")
                .build();
    }

    @Test
    public void 모델리스트갯수조회테스트() throws Exception {
        ConcurrentHashMap<String, Object> modelMap = new ConcurrentHashMap<>();
        modelMap.put("categoryCd", "1");

        // when
        Long modelListCount = frontModelJpaRepository.getModelCount(modelMap);

        // then
        assertThat(modelListCount).isGreaterThan(0);
    }

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
    public void 모델BDD조회테스트() throws Exception {

        // given
        ConcurrentHashMap<String, Object> modelMap = new ConcurrentHashMap<>();
        modelMap.put("categoryCd", "1");
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<FrontModelDTO> modelList = new ArrayList<>();
        modelList.add(FrontModelDTO.builder().idx(3).categoryCd(1).modelKorName("조찬희").modelImage(commonImageDtoList).build());

        given(mockFrontModelJpaRepository.getModelList(modelMap)).willReturn(modelList);

        // when
        Integer idx = mockFrontModelJpaRepository.getModelList(modelMap).get(0).getIdx();
        Integer categoryCd = mockFrontModelJpaRepository.getModelList(modelMap).get(0).getCategoryCd();
        String modelKorName = mockFrontModelJpaRepository.getModelList(modelMap).get(0).getModelKorName();
        String fileName = mockFrontModelJpaRepository.getModelList(modelMap).get(0).getModelImage().get(0).getFileName();

        assertThat(idx).isEqualTo(modelList.get(0).getIdx());
        assertThat(categoryCd).isEqualTo(modelList.get(0).getCategoryCd());
        assertThat(modelKorName).isEqualTo(modelList.get(0).getModelKorName());
        assertThat(fileName).isEqualTo(modelList.get(0).getModelImage().get(0).getFileName());
    }

    @Test
    public void 모델상세BDD조회테스트() throws Exception {

        // given
        commonImageEntityList.add(commonImageEntity);

        frontModelEntity = builder().idx(1).commonImageEntityList(commonImageEntityList).build();

        frontModelDTO = FrontModelDTO.builder()
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

        // when
        List<FrontModelDTO> mainModelList = frontModelJpaRepository.getMainModelList();

        Optional<FrontModelDTO> mainModelFirstInfo = frontModelJpaRepository.getMainModelList().stream().findFirst();

        // then
        assertThat(mainModelList.size()).isGreaterThan(0);
        assertThat(mainModelFirstInfo.get().getCategoryCd()).isEqualTo(1);
        assertThat(mainModelFirstInfo.get().getModelMainYn()).isEqualTo("Y");
    }
}
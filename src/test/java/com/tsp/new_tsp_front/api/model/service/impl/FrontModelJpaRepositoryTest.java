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

    private void createModel() {
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

    @BeforeEach
    public void init() {
        createModel();
    }

    @Test
    public void 모델리스트갯수조회테스트() throws Exception {
        ConcurrentHashMap<String, Object> modelMap = new ConcurrentHashMap<>();
        modelMap.put("categoryCd", "1");

        // then
        assertThat(frontModelJpaRepository.getModelCount(modelMap)).isGreaterThan(0);
    }

    @Test
    public void 모델리스트조회테스트() throws Exception {
        // given
        ConcurrentHashMap<String, Object> modelMap = new ConcurrentHashMap<>();
        modelMap.put("categoryCd", "1");
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        // then
        assertThat(frontModelJpaRepository.getModelList(modelMap).size()).isGreaterThan(0);
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

        assertThat(mockFrontModelJpaRepository.getModelList(modelMap).get(0).getIdx()).isEqualTo(modelList.get(0).getIdx());
        assertThat(mockFrontModelJpaRepository.getModelList(modelMap).get(0).getCategoryCd()).isEqualTo(modelList.get(0).getCategoryCd());
        assertThat(mockFrontModelJpaRepository.getModelList(modelMap).get(0).getModelKorName()).isEqualTo(modelList.get(0).getModelKorName());
        assertThat(mockFrontModelJpaRepository.getModelList(modelMap).get(0).getModelImage().get(0).getFileName()).isEqualTo(modelList.get(0).getModelImage().get(0).getFileName());
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

        assertThat(mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getIdx()).isEqualTo(1);
        assertThat(mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getCategoryCd()).isEqualTo(1);
        assertThat(mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getCategoryAge()).isEqualTo("2");
        assertThat(mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getModelKorName()).isEqualTo("조찬희");
        assertThat(mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getModelEngName()).isEqualTo("CHOCHANHEE");
        assertThat(mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getModelDescription()).isEqualTo("chaneeCho");
        assertThat(mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getHeight()).isEqualTo("170");
        assertThat(mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getSize3()).isEqualTo("34-24-34");
        assertThat(mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getShoes()).isEqualTo("270");
        assertThat(mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getVisible()).isEqualTo("Y");
        assertThat(mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getModelImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getModelImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getModelImage().get(0).getFilePath()).isEqualTo("/test/test.jpg");
        assertThat(mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getModelImage().get(0).getImageType()).isEqualTo("main");
        assertThat(mockFrontModelJpaRepository.getModelInfo(frontModelEntity).getModelImage().get(0).getTypeName()).isEqualTo("model");
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
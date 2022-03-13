package com.tsp.new_tsp_front.api.model.service.impl;

import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import com.tsp.new_tsp_front.api.model.service.FrontModelJpaApiService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_front.api.model.domain.FrontModelEntity.builder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("모델 Repository Test")
class FrontModelJpaRepositoryTest {

    @Autowired
    private FrontModelJpaRepository frontModelJpaRepository;

    @InjectMocks
    private FrontModelJpaApiService frontModelJpaApiService;

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
}
package com.tsp.new_tsp_front.api.model.service.impl;

import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyDTO;
import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyEntity;
import com.tsp.new_tsp_front.api.common.domain.CommonImageDTO;
import com.tsp.new_tsp_front.api.common.domain.CommonImageEntity;
import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import com.tsp.new_tsp_front.api.model.domain.recommend.FrontRecommendEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-local.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("모델 Repository Test")
class FrontModelJpaRepositoryTest {
    @Mock private FrontModelJpaRepository mockFrontModelJpaRepository;
    private final FrontModelJpaRepository frontModelJpaRepository;

    private FrontModelEntity frontModelEntity;
    private FrontModelDTO frontModelDTO;
    private FrontAgencyEntity frontAgencyEntity;
    private FrontAgencyDTO frontAgencyDTO;
    private CommonImageEntity commonImageEntity;
    private CommonImageDTO commonImageDTO;
    List<CommonImageEntity> commonImageEntityList = new ArrayList<>();

    private final EntityManager em;

    private void createModel() {
        frontAgencyEntity = FrontAgencyEntity.builder()
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

        frontAgencyDTO = FrontAgencyEntity.toDto(frontAgencyEntity);

        frontModelEntity = FrontModelEntity.builder()
                .categoryCd(1)
                .categoryAge(2)
                .agencyIdx(frontAgencyEntity.getIdx())
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .modelFavoriteCount(1)
                .newYn("N")
                .frontAgencyEntity(frontAgencyEntity)
                .modelViewCount(1)
                .visible("Y")
                .build();

        frontModelDTO = FrontModelEntity.toDto(frontModelEntity);

        commonImageEntity = CommonImageEntity.builder()
                .idx(1L)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1L)
                .typeName("model")
                .build();

        commonImageDTO = CommonImageEntity.toDto(commonImageEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createModel();
    }

    @Test
    @DisplayName("모델 리스트 갯수 조회 테스트")
    void 모델리스트갯수조회테스트() {
        // 정상
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", 1);

        // then
        assertThat(frontModelJpaRepository.findModelCount(modelMap)).isPositive();
    }

    @Test
    @DisplayName("모델 리스트 조회 테스트")
    void 모델리스트조회테스트() {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", 1);
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        // then
        assertThat(frontModelJpaRepository.findModelList(modelMap)).isNotEmpty();
    }

    @Test
    @DisplayName("모델 상세 조회 테스트")
    void 모델상세조회테스트() {
        // given
        FrontModelEntity menFrontModelEntity = FrontModelEntity.builder().idx(4L).agencyIdx(1L).build();

        FrontModelDTO menModelInfo = frontModelJpaRepository.findOneModel(menFrontModelEntity.getIdx());
        assertThat(menModelInfo.getIdx()).isEqualTo(4L);
        assertThat(menModelInfo.getModelKorFirstName()).isEqualTo("선");
        assertThat(menModelInfo.getModelKorSecondName()).isEqualTo("소연");
        assertThat(menModelInfo.getModelFirstName()).isEqualTo("Sun");
        assertThat(menModelInfo.getModelSecondName()).isEqualTo("so yeon");
        assertThat(menModelInfo.getAgencyIdx()).isEqualTo(1L);
        assertThat(menModelInfo.getModelAgency().getAgencyName()).isEqualTo("test");
        assertThat(menModelInfo.getModelAgency().getAgencyDescription()).isEqualTo("test");

        FrontModelEntity womenFrontModelEntity = FrontModelEntity.builder().idx(2L).agencyIdx(1L).build();

        FrontModelDTO womenModelInfo = frontModelJpaRepository.findOneModel(womenFrontModelEntity.getIdx());

        // then
        assertThat(womenModelInfo.getIdx()).isEqualTo(2L);
        assertThat(womenModelInfo.getModelKorFirstName()).isEqualTo("이");
        assertThat(womenModelInfo.getModelKorSecondName()).isEqualTo("화선");
        assertThat(womenModelInfo.getModelFirstName()).isEqualTo("Lee");
        assertThat(womenModelInfo.getModelSecondName()).isEqualTo("hwa seon");
        assertThat(womenModelInfo.getAgencyIdx()).isEqualTo(1L);
        assertThat(womenModelInfo.getModelAgency().getAgencyName()).isEqualTo("test");
        assertThat(womenModelInfo.getModelAgency().getAgencyDescription()).isEqualTo("test");
    }

    @Test
    @DisplayName("모델 리스트 조회 Mockito 테스트")
    void 모델리스트조회Mockito테스트() {
        // 정상
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", 1);
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<FrontModelDTO> modelList = new ArrayList<>();
        modelList.add(FrontModelDTO.builder().idx(3L).categoryCd(1).modelKorName("테스트모델1").modelAgency(frontAgencyDTO).modelImage(commonImageDtoList).build());
        modelList.add(FrontModelDTO.builder().idx(4L).categoryCd(2).modelKorName("테스트모델2").modelAgency(frontAgencyDTO).modelImage(commonImageDtoList).build());

        // when
        when(mockFrontModelJpaRepository.findModelList(modelMap)).thenReturn(modelList);
        List<FrontModelDTO> newModelList = mockFrontModelJpaRepository.findModelList(modelMap);

        // then
        assertThat(newModelList.get(0).getIdx()).isEqualTo(modelList.get(0).getIdx());
        assertThat(newModelList.get(0).getCategoryCd()).isEqualTo(modelList.get(0).getCategoryCd());
        assertThat(newModelList.get(0).getModelKorName()).isEqualTo(modelList.get(0).getModelKorName());
        assertThat(newModelList.get(0).getModelAgency().getAgencyName()).isEqualTo(modelList.get(0).getModelAgency().getAgencyName());
        assertThat(newModelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(modelList.get(0).getModelAgency().getAgencyDescription());
        assertThat(newModelList.get(0).getModelImage().get(0).getFileName()).isEqualTo(modelList.get(0).getModelImage().get(0).getFileName());

        // verify
        verify(mockFrontModelJpaRepository, times(1)).findModelList(modelMap);
        verify(mockFrontModelJpaRepository, atLeastOnce()).findModelList(modelMap);
        verifyNoMoreInteractions(mockFrontModelJpaRepository);

        InOrder inOrder = inOrder(mockFrontModelJpaRepository);
        inOrder.verify(mockFrontModelJpaRepository).findModelList(modelMap);
    }

    @Test
    @DisplayName("모델 리스트 조회 BDD 테스트")
    void 모델리스트조회BDD테스트() {
        // 정상
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", 1);
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<FrontModelDTO> modelList = new ArrayList<>();
        modelList.add(FrontModelDTO.builder().idx(3L).categoryCd(1).modelKorName("테스트모델1").modelAgency(frontAgencyDTO).modelImage(commonImageDtoList).build());
        modelList.add(FrontModelDTO.builder().idx(4L).categoryCd(2).modelKorName("테스트모델2").modelAgency(frontAgencyDTO).modelImage(commonImageDtoList).build());

        // when
        given(mockFrontModelJpaRepository.findModelList(modelMap)).willReturn(modelList);
        List<FrontModelDTO> newModelList = mockFrontModelJpaRepository.findModelList(modelMap);

        // then
        assertThat(newModelList.get(0).getIdx()).isEqualTo(modelList.get(0).getIdx());
        assertThat(newModelList.get(0).getCategoryCd()).isEqualTo(modelList.get(0).getCategoryCd());
        assertThat(newModelList.get(0).getModelKorName()).isEqualTo(modelList.get(0).getModelKorName());
        assertThat(newModelList.get(0).getModelAgency().getAgencyName()).isEqualTo(modelList.get(0).getModelAgency().getAgencyName());
        assertThat(newModelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(modelList.get(0).getModelAgency().getAgencyDescription());
        assertThat(newModelList.get(0).getModelImage().get(0).getFileName()).isEqualTo(modelList.get(0).getModelImage().get(0).getFileName());

        // verify
        then(mockFrontModelJpaRepository).should(times(1)).findModelList(modelMap);
        then(mockFrontModelJpaRepository).should(atLeastOnce()).findModelList(modelMap);
        then(mockFrontModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 상세 조회 Mockito 테스트")
    void 모델상세조회Mockito테스트() {
        // given
        commonImageEntityList.add(commonImageEntity);

        frontModelEntity = FrontModelEntity.builder().idx(1L).commonImageEntityList(commonImageEntityList).build();

        frontModelDTO = FrontModelDTO.builder()
                .idx(1L)
                .categoryCd(1)
                .categoryAge(2)
                .modelKorName("조찬희")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .modelAgency(FrontAgencyEntity.toDto(frontAgencyEntity))
                .modelImage(CommonImageEntity.toDtoList(commonImageEntityList))
                .build();

        // when
        when(mockFrontModelJpaRepository.findOneModel(frontModelEntity.getIdx())).thenReturn(frontModelDTO);
        FrontModelDTO modelInfo = mockFrontModelJpaRepository.findOneModel(frontModelEntity.getIdx());

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(1);
        assertThat(modelInfo.getCategoryCd()).isEqualTo(1);
        assertThat(modelInfo.getCategoryAge()).isEqualTo(2);
        assertThat(modelInfo.getModelKorName()).isEqualTo("조찬희");
        assertThat(modelInfo.getModelEngName()).isEqualTo("CHOCHANHEE");
        assertThat(modelInfo.getModelDescription()).isEqualTo("chaneeCho");
        assertThat(modelInfo.getHeight()).isEqualTo(170);
        assertThat(modelInfo.getSize3()).isEqualTo("34-24-34");
        assertThat(modelInfo.getShoes()).isEqualTo(270);
        assertThat(modelInfo.getVisible()).isEqualTo("Y");
        assertThat(modelInfo.getModelAgency().getAgencyName()).isEqualTo("agency");
        assertThat(modelInfo.getModelAgency().getAgencyDescription()).isEqualTo("agency");
        assertThat(modelInfo.getModelImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(modelInfo.getModelImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(modelInfo.getModelImage().get(0).getFilePath()).isEqualTo("/test/test.jpg");
        assertThat(modelInfo.getModelImage().get(0).getImageType()).isEqualTo("main");
        assertThat(modelInfo.getModelImage().get(0).getTypeName()).isEqualTo("model");

        // verify
        verify(mockFrontModelJpaRepository, times(1)).findOneModel(frontModelEntity.getIdx());
        verify(mockFrontModelJpaRepository, atLeastOnce()).findOneModel(frontModelEntity.getIdx());
        verifyNoMoreInteractions(mockFrontModelJpaRepository);

        InOrder inOrder = inOrder(mockFrontModelJpaRepository);
        inOrder.verify(mockFrontModelJpaRepository).findOneModel(frontModelEntity.getIdx());
    }

    @Test
    @DisplayName("모델 상세 조회 BDD 테스트")
    void 모델상세조회BDD테스트() {
        // given
        commonImageEntityList.add(commonImageEntity);

        frontModelEntity = FrontModelEntity.builder().idx(1L).commonImageEntityList(commonImageEntityList).build();

        frontModelDTO = FrontModelDTO.builder()
                .idx(1L)
                .categoryCd(1)
                .categoryAge(2)
                .modelKorName("조찬희")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .modelAgency(FrontAgencyEntity.toDto(frontAgencyEntity))
                .modelImage(CommonImageEntity.toDtoList(commonImageEntityList))
                .build();

        // when
        given(mockFrontModelJpaRepository.findOneModel(frontModelEntity.getIdx())).willReturn(frontModelDTO);
        FrontModelDTO modelInfo = mockFrontModelJpaRepository.findOneModel(frontModelEntity.getIdx());

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(1);
        assertThat(modelInfo.getCategoryCd()).isEqualTo(1);
        assertThat(modelInfo.getCategoryAge()).isEqualTo(2);
        assertThat(modelInfo.getModelKorName()).isEqualTo("조찬희");
        assertThat(modelInfo.getModelEngName()).isEqualTo("CHOCHANHEE");
        assertThat(modelInfo.getModelDescription()).isEqualTo("chaneeCho");
        assertThat(modelInfo.getHeight()).isEqualTo(170);
        assertThat(modelInfo.getSize3()).isEqualTo("34-24-34");
        assertThat(modelInfo.getShoes()).isEqualTo(270);
        assertThat(modelInfo.getVisible()).isEqualTo("Y");
        assertThat(modelInfo.getModelAgency().getAgencyName()).isEqualTo("agency");
        assertThat(modelInfo.getModelAgency().getAgencyDescription()).isEqualTo("agency");
        assertThat(modelInfo.getModelImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(modelInfo.getModelImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(modelInfo.getModelImage().get(0).getFilePath()).isEqualTo("/test/test.jpg");
        assertThat(modelInfo.getModelImage().get(0).getImageType()).isEqualTo("main");
        assertThat(modelInfo.getModelImage().get(0).getTypeName()).isEqualTo("model");

        // verify
        then(mockFrontModelJpaRepository).should(times(1)).findOneModel(frontModelEntity.getIdx());
        then(mockFrontModelJpaRepository).should(atLeastOnce()).findOneModel(frontModelEntity.getIdx());
        then(mockFrontModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 모델 상세 조회 테스트")
    void 이전or다음모델상세조회테스트() {
        // given
        frontModelEntity = FrontModelEntity.builder().idx(145L).categoryCd(2).build();

        // 이전 모델
        assertThat(frontModelJpaRepository.findPrevOneModel(frontModelEntity).getIdx()).isEqualTo(144);
        // 다음 모델
        assertThat(frontModelJpaRepository.findNextOneModel(frontModelEntity).getIdx()).isEqualTo(147);
    }

    @Test
    @DisplayName("이전 모델 상세 조회 Mockito 테스트")
    void 이전모델상세조회Mockito테스트() {
        // given
        frontModelEntity = FrontModelEntity.builder().idx(145L).categoryCd(2).build();
        // when
        frontModelDTO = frontModelJpaRepository.findPrevOneModel(frontModelEntity);

        when(mockFrontModelJpaRepository.findPrevOneModel(frontModelEntity)).thenReturn(frontModelDTO);
        FrontModelDTO modelInfo = mockFrontModelJpaRepository.findPrevOneModel(frontModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(144);

        // verify
        verify(mockFrontModelJpaRepository, times(1)).findPrevOneModel(frontModelEntity);
        verify(mockFrontModelJpaRepository, atLeastOnce()).findPrevOneModel(frontModelEntity);
        verifyNoMoreInteractions(mockFrontModelJpaRepository);

        InOrder inOrder = inOrder(mockFrontModelJpaRepository);
        inOrder.verify(mockFrontModelJpaRepository).findPrevOneModel(frontModelEntity);
    }

    @Test
    @DisplayName("이전 모델 상세 조회 BDD 테스트")
    void 이전모델상세조회BDD테스트() {
        // given
        frontModelEntity = FrontModelEntity.builder().idx(145L).categoryCd(2).build();
        // when
        frontModelDTO = frontModelJpaRepository.findPrevOneModel(frontModelEntity);

        given(mockFrontModelJpaRepository.findPrevOneModel(frontModelEntity)).willReturn(frontModelDTO);
        FrontModelDTO modelInfo = mockFrontModelJpaRepository.findPrevOneModel(frontModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(144);

        // verify
        then(mockFrontModelJpaRepository).should(times(1)).findPrevOneModel(frontModelEntity);
        then(mockFrontModelJpaRepository).should(atLeastOnce()).findPrevOneModel(frontModelEntity);
        then(mockFrontModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 모델 상세 조회 Mockito 테스트")
    void 다음모델상세조회Mockito테스트() {
        // given
        frontModelEntity = FrontModelEntity.builder().idx(145L).categoryCd(2).build();
        // when
        frontModelDTO = frontModelJpaRepository.findNextOneModel(frontModelEntity);

        when(mockFrontModelJpaRepository.findNextOneModel(frontModelEntity)).thenReturn(frontModelDTO);
        FrontModelDTO modelInfo = mockFrontModelJpaRepository.findNextOneModel(frontModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(147);

        // verify
        verify(mockFrontModelJpaRepository, times(1)).findNextOneModel(frontModelEntity);
        verify(mockFrontModelJpaRepository, atLeastOnce()).findNextOneModel(frontModelEntity);
        verifyNoMoreInteractions(mockFrontModelJpaRepository);

        InOrder inOrder = inOrder(mockFrontModelJpaRepository);
        inOrder.verify(mockFrontModelJpaRepository).findNextOneModel(frontModelEntity);
    }

    @Test
    @DisplayName("다음 모델 상세 조회 BDD 테스트")
    void 다음모델상세조회BDD테스트() {
        // given
        frontModelEntity = FrontModelEntity.builder().idx(145L).categoryCd(2).build();
        // when
        frontModelDTO = frontModelJpaRepository.findNextOneModel(frontModelEntity);

        given(mockFrontModelJpaRepository.findNextOneModel(frontModelEntity)).willReturn(frontModelDTO);
        FrontModelDTO modelInfo = mockFrontModelJpaRepository.findNextOneModel(frontModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(147);

        // verify
        then(mockFrontModelJpaRepository).should(times(1)).findNextOneModel(frontModelEntity);
        then(mockFrontModelJpaRepository).should(atLeastOnce()).findNextOneModel(frontModelEntity);
        then(mockFrontModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 메인 배너 조회 테스트")
    void 모델메인배너리스트조회테스트() {
        // when
        List<FrontModelDTO> mainModelList = frontModelJpaRepository.findMainModelList();

        Optional<FrontModelDTO> mainModelFirstInfo = frontModelJpaRepository.findMainModelList().stream().findFirst();

        // then
        assertThat(mainModelList).isNotEmpty();
        assertThat(mainModelFirstInfo.get().getCategoryCd()).isEqualTo(1);
        assertThat(mainModelFirstInfo.get().getModelMainYn()).isEqualTo("Y");
    }

    @Test
    @DisplayName("모델 메인 배너 조회 Mockito 테스트")
    void 모델메인배너조회Mockito테스트() {
        // given
        List<FrontModelDTO> returnModelList = new ArrayList<>();

        // 남성
        returnModelList.add(FrontModelDTO.builder().idx(1L).categoryCd(1).modelKorName("남성모델").modelEngName("menModel").modelMainYn("Y").build());
        // 여성
        returnModelList.add(FrontModelDTO.builder().idx(2L).categoryCd(2).modelKorName("여성모델").modelEngName("womenModel").modelMainYn("Y").build());
        // 시니어
        returnModelList.add(FrontModelDTO.builder().idx(3L).categoryCd(3).modelKorName("시니어모델").modelEngName("seniorModel").modelMainYn("Y").build());

        // when
        when(mockFrontModelJpaRepository.findMainModelList()).thenReturn(returnModelList);
        List<FrontModelDTO> modelList = mockFrontModelJpaRepository.findMainModelList();

        // then
        assertAll(
                () -> assertThat(modelList).isNotEmpty(),
                () -> assertThat(modelList).hasSize(3)
        );

        assertThat(modelList.get(0).getIdx()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(modelList.get(0).getCategoryCd()).isEqualTo(returnModelList.get(0).getCategoryCd());
        assertThat(modelList.get(0).getModelKorName()).isEqualTo(returnModelList.get(0).getModelKorName());
        assertThat(modelList.get(0).getModelMainYn()).isEqualTo(returnModelList.get(0).getModelMainYn());

        // verify
        verify(mockFrontModelJpaRepository, times(1)).findMainModelList();
        verify(mockFrontModelJpaRepository, atLeastOnce()).findMainModelList();
        verifyNoMoreInteractions(mockFrontModelJpaRepository);

        InOrder inOrder = inOrder(mockFrontModelJpaRepository);
        inOrder.verify(mockFrontModelJpaRepository).findMainModelList();
    }

    @Test
    @DisplayName("모델 메인 배너 조회 BDD 테스트")
    void 모델메인배너조회BDD테스트() {
        // given
        List<FrontModelDTO> returnModelList = new ArrayList<>();

        // 남성
        returnModelList.add(FrontModelDTO.builder().idx(1L).categoryCd(1).modelKorName("남성모델").modelEngName("menModel").modelMainYn("Y").build());
        // 여성
        returnModelList.add(FrontModelDTO.builder().idx(2L).categoryCd(2).modelKorName("여성모델").modelEngName("womenModel").modelMainYn("Y").build());
        // 시니어
        returnModelList.add(FrontModelDTO.builder().idx(3L).categoryCd(3).modelKorName("시니어모델").modelEngName("seniorModel").modelMainYn("Y").build());

        // when
        given(mockFrontModelJpaRepository.findMainModelList()).willReturn(returnModelList);
        List<FrontModelDTO> modelList = mockFrontModelJpaRepository.findMainModelList();

        // then
        assertAll(
                () -> assertThat(modelList).isNotEmpty(),
                () -> assertThat(modelList).hasSize(3)
        );

        assertThat(modelList.get(0).getIdx()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(modelList.get(0).getCategoryCd()).isEqualTo(returnModelList.get(0).getCategoryCd());
        assertThat(modelList.get(0).getModelKorName()).isEqualTo(returnModelList.get(0).getModelKorName());
        assertThat(modelList.get(0).getModelMainYn()).isEqualTo(returnModelList.get(0).getModelMainYn());

        // verify
        then(mockFrontModelJpaRepository).should(times(1)).findMainModelList();
        then(mockFrontModelJpaRepository).should(atLeastOnce()).findMainModelList();
        then(mockFrontModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 좋아요 갯수 조회 Mockito 테스트")
    void 모델좋아요갯수조회Mockito테스트() {
        // given
        em.persist(frontModelEntity);
        frontModelDTO = FrontModelEntity.toDto(frontModelEntity);

        // when
        when(mockFrontModelJpaRepository.findOneModel(frontModelEntity.getIdx())).thenReturn(frontModelDTO);
        FrontModelDTO modelInfo = mockFrontModelJpaRepository.findOneModel(frontModelEntity.getIdx());

        // then
        assertThat(modelInfo.getModelFavoriteCount()).isEqualTo(frontModelEntity.getModelFavoriteCount());

        // verify
        verify(mockFrontModelJpaRepository, times(1)).findOneModel(frontModelEntity.getIdx());
        verify(mockFrontModelJpaRepository, atLeastOnce()).findOneModel(frontModelEntity.getIdx());
        verifyNoMoreInteractions(mockFrontModelJpaRepository);
    }

    @Test
    @DisplayName("모델 좋아요 갯수 조회 BDD 테스트")
    void 모델좋아요갯수조회BDD테스트() {
        // given
        em.persist(frontModelEntity);
        frontModelDTO = FrontModelEntity.toDto(frontModelEntity);

        // when
        given(mockFrontModelJpaRepository.findOneModel(frontModelEntity.getIdx())).willReturn(frontModelDTO);
        FrontModelDTO modelInfo = mockFrontModelJpaRepository.findOneModel(frontModelEntity.getIdx());

        // then
        assertThat(modelInfo.getModelFavoriteCount()).isEqualTo(frontModelEntity.getModelFavoriteCount());

        // verify
        then(mockFrontModelJpaRepository).should(times(1)).findOneModel(frontModelEntity.getIdx());
        then(mockFrontModelJpaRepository).should(atLeastOnce()).findOneModel(frontModelEntity.getIdx());
        then(mockFrontModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 조회 수 Mockito 테스트")
    void 모델조회수Mockito테스트() {
        // given
        em.persist(frontModelEntity);
        frontModelDTO = FrontModelEntity.toDto(frontModelEntity);

        Integer viewCount = frontModelJpaRepository.updateModelViewCount(frontModelEntity.getIdx());

        // when
        when(mockFrontModelJpaRepository.updateModelViewCount(frontModelEntity.getIdx())).thenReturn(viewCount);
        Integer newViewCount = mockFrontModelJpaRepository.updateModelViewCount(frontModelEntity.getIdx());

        // then
        assertThat(newViewCount).isEqualTo(viewCount);

        // verify
        verify(mockFrontModelJpaRepository, times(1)).updateModelViewCount(frontModelEntity.getIdx());
        verify(mockFrontModelJpaRepository, atLeastOnce()).updateModelViewCount(frontModelEntity.getIdx());
        verifyNoMoreInteractions(mockFrontModelJpaRepository);

        InOrder inOrder = inOrder(mockFrontModelJpaRepository);
        inOrder.verify(mockFrontModelJpaRepository).updateModelViewCount(frontModelEntity.getIdx());
    }

    @Test
    @DisplayName("모델 조회 수 BDD 테스트")
    void 모델조회수BDD테스트() {
        // given
        em.persist(frontModelEntity);
        frontModelDTO = FrontModelEntity.toDto(frontModelEntity);

        Integer viewCount = frontModelJpaRepository.updateModelViewCount(frontModelEntity.getIdx());

        // when
        given(mockFrontModelJpaRepository.updateModelViewCount(frontModelEntity.getIdx())).willReturn(viewCount);
        Integer newViewCount = mockFrontModelJpaRepository.updateModelViewCount(frontModelEntity.getIdx());

        // then
        assertThat(newViewCount).isEqualTo(viewCount);

        // verify
        then(mockFrontModelJpaRepository).should(times(1)).updateModelViewCount(frontModelEntity.getIdx());
        then(mockFrontModelJpaRepository).should(atLeastOnce()).updateModelViewCount(frontModelEntity.getIdx());
        then(mockFrontModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 좋아요 Mockito 테스트")
    void 모델좋아요Mockito테스트() {
        // given
        em.persist(frontModelEntity);
        frontModelDTO = FrontModelEntity.toDto(frontModelEntity);

        Integer favoriteCount = frontModelJpaRepository.favoriteModel(frontModelEntity.getIdx());

        // when
        when(mockFrontModelJpaRepository.favoriteModel(frontModelEntity.getIdx())).thenReturn(favoriteCount);
        Integer newFavoriteCount = mockFrontModelJpaRepository.favoriteModel(frontModelEntity.getIdx());

        // then
        assertThat(newFavoriteCount).isEqualTo(favoriteCount);

        // verify
        verify(mockFrontModelJpaRepository, times(1)).favoriteModel(frontModelEntity.getIdx());
        verify(mockFrontModelJpaRepository, atLeastOnce()).favoriteModel(frontModelEntity.getIdx());
        verifyNoMoreInteractions(mockFrontModelJpaRepository);

        InOrder inOrder = inOrder(mockFrontModelJpaRepository);
        inOrder.verify(mockFrontModelJpaRepository).favoriteModel(frontModelEntity.getIdx());
    }

    @Test
    @DisplayName("모델 좋아요 BDD 테스트")
    void 모델좋아요BDD테스트() {
        // given
        em.persist(frontModelEntity);
        frontModelDTO = FrontModelEntity.toDto(frontModelEntity);

        Integer favoriteCount = frontModelJpaRepository.favoriteModel(frontModelEntity.getIdx());

        // when
        given(mockFrontModelJpaRepository.favoriteModel(frontModelEntity.getIdx())).willReturn(favoriteCount);
        Integer newFavoriteCount = mockFrontModelJpaRepository.favoriteModel(frontModelEntity.getIdx());

        // then
        assertThat(newFavoriteCount).isEqualTo(favoriteCount);

        // verify
        then(mockFrontModelJpaRepository).should(times(1)).favoriteModel(frontModelEntity.getIdx());
        then(mockFrontModelJpaRepository).should(atLeastOnce()).favoriteModel(frontModelEntity.getIdx());
        then(mockFrontModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("새로운 모델 리스트 조회 Mockito 테스트")
    void 새로운모델리스트조회Mockito테스트() {
        // 정상
        // given
        Map<String, Object> newModelMap = new HashMap<>();
        newModelMap.put("categoryCd", 1);
        newModelMap.put("jpaStartPage", 1);
        newModelMap.put("size", 3);
        newModelMap.put("newYn", "Y");

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<FrontModelDTO> modelList = new ArrayList<>();
        modelList.add(FrontModelDTO.builder().idx(3L).categoryCd(1).modelKorName("테스트모델1").newYn("Y").modelAgency(frontAgencyDTO).modelImage(commonImageDtoList).build());
        modelList.add(FrontModelDTO.builder().idx(4L).categoryCd(2).modelKorName("테스트모델2").newYn("Y").modelAgency(frontAgencyDTO).modelImage(commonImageDtoList).build());

        // when
        when(mockFrontModelJpaRepository.findModelList(newModelMap)).thenReturn(modelList);
        List<FrontModelDTO> newModelList = mockFrontModelJpaRepository.findModelList(newModelMap);

        // then
        assertThat(newModelList.get(0).getIdx()).isEqualTo(modelList.get(0).getIdx());
        assertThat(newModelList.get(0).getCategoryCd()).isEqualTo(modelList.get(0).getCategoryCd());
        assertThat(newModelList.get(0).getModelKorName()).isEqualTo(modelList.get(0).getModelKorName());
        assertThat(newModelList.get(0).getNewYn()).isEqualTo(modelList.get(0).getNewYn());
        assertThat(newModelList.get(0).getModelAgency().getAgencyName()).isEqualTo(modelList.get(0).getModelAgency().getAgencyName());
        assertThat(newModelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(modelList.get(0).getModelAgency().getAgencyDescription());
        assertThat(newModelList.get(0).getModelImage().get(0).getFileName()).isEqualTo(modelList.get(0).getModelImage().get(0).getFileName());

        // verify
        verify(mockFrontModelJpaRepository, times(1)).findModelList(newModelMap);
        verify(mockFrontModelJpaRepository, atLeastOnce()).findModelList(newModelMap);
        verifyNoMoreInteractions(mockFrontModelJpaRepository);

        InOrder inOrder = inOrder(mockFrontModelJpaRepository);
        inOrder.verify(mockFrontModelJpaRepository).findModelList(newModelMap);
    }

    @Test
    @DisplayName("새로운모델 리스트 조회 BDD 테스트")
    void 새로운모델리스트조회BDD테스트() {
        // 정상
        // given
        Map<String, Object> newModelMap = new HashMap<>();
        newModelMap.put("categoryCd", 1);
        newModelMap.put("jpaStartPage", 1);
        newModelMap.put("size", 3);
        newModelMap.put("newYn", "Y");

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<FrontModelDTO> modelList = new ArrayList<>();
        modelList.add(FrontModelDTO.builder().idx(3L).categoryCd(1).modelKorName("테스트모델1").newYn("Y").modelAgency(frontAgencyDTO).modelImage(commonImageDtoList).build());
        modelList.add(FrontModelDTO.builder().idx(4L).categoryCd(2).modelKorName("테스트모델2").newYn("Y").modelAgency(frontAgencyDTO).modelImage(commonImageDtoList).build());

        // when
        given(mockFrontModelJpaRepository.findModelList(newModelMap)).willReturn(modelList);
        List<FrontModelDTO> newModelList = mockFrontModelJpaRepository.findModelList(newModelMap);

        // then
        assertThat(newModelList.get(0).getIdx()).isEqualTo(modelList.get(0).getIdx());
        assertThat(newModelList.get(0).getCategoryCd()).isEqualTo(modelList.get(0).getCategoryCd());
        assertThat(newModelList.get(0).getModelKorName()).isEqualTo(modelList.get(0).getModelKorName());
        assertThat(newModelList.get(0).getNewYn()).isEqualTo(modelList.get(0).getNewYn());
        assertThat(newModelList.get(0).getModelAgency().getAgencyName()).isEqualTo(modelList.get(0).getModelAgency().getAgencyName());
        assertThat(newModelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(modelList.get(0).getModelAgency().getAgencyDescription());
        assertThat(newModelList.get(0).getModelImage().get(0).getFileName()).isEqualTo(modelList.get(0).getModelImage().get(0).getFileName());

        // verify
        then(mockFrontModelJpaRepository).should(times(1)).findModelList(newModelMap);
        then(mockFrontModelJpaRepository).should(atLeastOnce()).findModelList(newModelMap);
        then(mockFrontModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("추천 검색어 리스트 조회 테스트")
    void 추천검색어리스트조회테스트() {
        Map<String, Object> recommendMap = new HashMap<>();
        recommendMap.put("jpaStartPage", 0);
        recommendMap.put("size", 3);

        List<String> list = new ArrayList<>();
        list.add("모델1");
        list.add("모델2");

        FrontRecommendEntity frontRecommendEntity = FrontRecommendEntity.builder()
                .recommendKeyword(list)
                .build();

        em.persist(frontRecommendEntity);

        assertThat(frontModelJpaRepository.findRecommendList()).isNotEmpty();
    }
}
package com.tsp.new_tsp_front.api.model.service.impl;

import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyDTO;
import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyEntity;
import com.tsp.new_tsp_front.api.common.domain.CommonImageDTO;
import com.tsp.new_tsp_front.api.common.domain.CommonImageEntity;
import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import com.tsp.new_tsp_front.api.model.domain.recommend.FrontRecommendEntity;
import com.tsp.new_tsp_front.api.model.domain.search.FrontSearchDTO;
import com.tsp.new_tsp_front.api.model.domain.search.FrontSearchEntity;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

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
class FrontModelJpaQueryRepositoryTest {
    @Mock private FrontModelJpaQueryRepository mockFrontModelJpaQueryRepository;
    private final FrontModelJpaQueryRepository frontModelJpaQueryRepository;

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
    @DisplayName("모델 리스트 조회 테스트")
    void 모델리스트조회테스트() {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", 1);
        PageRequest pageRequest = PageRequest.of(1, 3);

        // then
        assertThat(frontModelJpaQueryRepository.findModelList(modelMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("모델 상세 조회 테스트")
    void 모델상세조회테스트() {
        // given
        FrontModelEntity menFrontModelEntity = FrontModelEntity.builder().idx(4L).agencyIdx(1L).build();

        FrontModelDTO menModelInfo = frontModelJpaQueryRepository.findOneModel(menFrontModelEntity.getIdx());
        assertThat(menModelInfo.getIdx()).isEqualTo(4L);
        assertThat(menModelInfo.getModelKorFirstName()).isEqualTo("선");
        assertThat(menModelInfo.getModelKorSecondName()).isEqualTo("소연");
        assertThat(menModelInfo.getModelFirstName()).isEqualTo("Sun");
        assertThat(menModelInfo.getModelSecondName()).isEqualTo("so yeon");
        assertThat(menModelInfo.getAgencyIdx()).isEqualTo(1L);
        assertThat(menModelInfo.getModelAgency().getAgencyName()).isEqualTo("test");
        assertThat(menModelInfo.getModelAgency().getAgencyDescription()).isEqualTo("test");

        FrontModelEntity womenFrontModelEntity = FrontModelEntity.builder().idx(2L).agencyIdx(1L).build();

        FrontModelDTO womenModelInfo = frontModelJpaQueryRepository.findOneModel(womenFrontModelEntity.getIdx());

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
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<FrontModelDTO> modelList = new ArrayList<>();
        modelList.add(FrontModelDTO.builder().idx(3L).categoryCd(1).modelKorName("테스트모델1").modelAgency(frontAgencyDTO).modelImage(commonImageDtoList).build());
        modelList.add(FrontModelDTO.builder().idx(4L).categoryCd(2).modelKorName("테스트모델2").modelAgency(frontAgencyDTO).modelImage(commonImageDtoList).build());

        Page<FrontModelDTO> resultPage = new PageImpl<>(modelList, pageRequest, modelList.size());
        // when
        when(mockFrontModelJpaQueryRepository.findModelList(modelMap, pageRequest)).thenReturn(resultPage);
        Page<FrontModelDTO> newModelList = mockFrontModelJpaQueryRepository.findModelList(modelMap, pageRequest);
        List<FrontModelDTO> findModelList = newModelList.stream().collect(Collectors.toList());

        // then
        assertThat(findModelList.get(0).getIdx()).isEqualTo(modelList.get(0).getIdx());
        assertThat(findModelList.get(0).getCategoryCd()).isEqualTo(modelList.get(0).getCategoryCd());
        assertThat(findModelList.get(0).getModelKorName()).isEqualTo(modelList.get(0).getModelKorName());
        assertThat(findModelList.get(0).getModelAgency().getAgencyName()).isEqualTo(modelList.get(0).getModelAgency().getAgencyName());
        assertThat(findModelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(modelList.get(0).getModelAgency().getAgencyDescription());
        assertThat(findModelList.get(0).getModelImage().get(0).getFileName()).isEqualTo(modelList.get(0).getModelImage().get(0).getFileName());

        // verify
        verify(mockFrontModelJpaQueryRepository, times(1)).findModelList(modelMap, pageRequest);
        verify(mockFrontModelJpaQueryRepository, atLeastOnce()).findModelList(modelMap, pageRequest);
        verifyNoMoreInteractions(mockFrontModelJpaQueryRepository);

        InOrder inOrder = inOrder(mockFrontModelJpaQueryRepository);
        inOrder.verify(mockFrontModelJpaQueryRepository).findModelList(modelMap, pageRequest);
    }

    @Test
    @DisplayName("모델 리스트 조회 BDD 테스트")
    void 모델리스트조회BDD테스트() {
        // 정상
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", 1);
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<FrontModelDTO> modelList = new ArrayList<>();
        modelList.add(FrontModelDTO.builder().idx(3L).categoryCd(1).modelKorName("테스트모델1").modelAgency(frontAgencyDTO).modelImage(commonImageDtoList).build());
        modelList.add(FrontModelDTO.builder().idx(4L).categoryCd(2).modelKorName("테스트모델2").modelAgency(frontAgencyDTO).modelImage(commonImageDtoList).build());

        Page<FrontModelDTO> resultPage = new PageImpl<>(modelList, pageRequest, modelList.size());
        // when
        given(mockFrontModelJpaQueryRepository.findModelList(modelMap, pageRequest)).willReturn(resultPage);
        Page<FrontModelDTO> newModelList = mockFrontModelJpaQueryRepository.findModelList(modelMap, pageRequest);
        List<FrontModelDTO> findModelList = newModelList.stream().collect(Collectors.toList());

        // then
        assertThat(findModelList.get(0).getIdx()).isEqualTo(modelList.get(0).getIdx());
        assertThat(findModelList.get(0).getCategoryCd()).isEqualTo(modelList.get(0).getCategoryCd());
        assertThat(findModelList.get(0).getModelKorName()).isEqualTo(modelList.get(0).getModelKorName());
        assertThat(findModelList.get(0).getModelAgency().getAgencyName()).isEqualTo(modelList.get(0).getModelAgency().getAgencyName());
        assertThat(findModelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(modelList.get(0).getModelAgency().getAgencyDescription());
        assertThat(findModelList.get(0).getModelImage().get(0).getFileName()).isEqualTo(modelList.get(0).getModelImage().get(0).getFileName());

        // verify
        then(mockFrontModelJpaQueryRepository).should(times(1)).findModelList(modelMap, pageRequest);
        then(mockFrontModelJpaQueryRepository).should(atLeastOnce()).findModelList(modelMap, pageRequest);
        then(mockFrontModelJpaQueryRepository).shouldHaveNoMoreInteractions();
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
        when(mockFrontModelJpaQueryRepository.findOneModel(frontModelEntity.getIdx())).thenReturn(frontModelDTO);
        FrontModelDTO modelInfo = mockFrontModelJpaQueryRepository.findOneModel(frontModelEntity.getIdx());

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
        verify(mockFrontModelJpaQueryRepository, times(1)).findOneModel(frontModelEntity.getIdx());
        verify(mockFrontModelJpaQueryRepository, atLeastOnce()).findOneModel(frontModelEntity.getIdx());
        verifyNoMoreInteractions(mockFrontModelJpaQueryRepository);

        InOrder inOrder = inOrder(mockFrontModelJpaQueryRepository);
        inOrder.verify(mockFrontModelJpaQueryRepository).findOneModel(frontModelEntity.getIdx());
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
        given(mockFrontModelJpaQueryRepository.findOneModel(frontModelEntity.getIdx())).willReturn(frontModelDTO);
        FrontModelDTO modelInfo = mockFrontModelJpaQueryRepository.findOneModel(frontModelEntity.getIdx());

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
        then(mockFrontModelJpaQueryRepository).should(times(1)).findOneModel(frontModelEntity.getIdx());
        then(mockFrontModelJpaQueryRepository).should(atLeastOnce()).findOneModel(frontModelEntity.getIdx());
        then(mockFrontModelJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 모델 상세 조회 테스트")
    void 이전or다음모델상세조회테스트() {
        // given
        frontModelEntity = FrontModelEntity.builder().idx(145L).categoryCd(2).build();

        // 이전 모델
        assertThat(frontModelJpaQueryRepository.findPrevOneModel(frontModelEntity).getIdx()).isEqualTo(144);
        // 다음 모델
        assertThat(frontModelJpaQueryRepository.findNextOneModel(frontModelEntity).getIdx()).isEqualTo(147);
    }

    @Test
    @DisplayName("이전 모델 상세 조회 Mockito 테스트")
    void 이전모델상세조회Mockito테스트() {
        // given
        frontModelEntity = FrontModelEntity.builder().idx(145L).categoryCd(2).build();
        // when
        frontModelDTO = frontModelJpaQueryRepository.findPrevOneModel(frontModelEntity);

        when(mockFrontModelJpaQueryRepository.findPrevOneModel(frontModelEntity)).thenReturn(frontModelDTO);
        FrontModelDTO modelInfo = mockFrontModelJpaQueryRepository.findPrevOneModel(frontModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(144);

        // verify
        verify(mockFrontModelJpaQueryRepository, times(1)).findPrevOneModel(frontModelEntity);
        verify(mockFrontModelJpaQueryRepository, atLeastOnce()).findPrevOneModel(frontModelEntity);
        verifyNoMoreInteractions(mockFrontModelJpaQueryRepository);

        InOrder inOrder = inOrder(mockFrontModelJpaQueryRepository);
        inOrder.verify(mockFrontModelJpaQueryRepository).findPrevOneModel(frontModelEntity);
    }

    @Test
    @DisplayName("이전 모델 상세 조회 BDD 테스트")
    void 이전모델상세조회BDD테스트() {
        // given
        frontModelEntity = FrontModelEntity.builder().idx(145L).categoryCd(2).build();
        // when
        frontModelDTO = frontModelJpaQueryRepository.findPrevOneModel(frontModelEntity);

        given(mockFrontModelJpaQueryRepository.findPrevOneModel(frontModelEntity)).willReturn(frontModelDTO);
        FrontModelDTO modelInfo = mockFrontModelJpaQueryRepository.findPrevOneModel(frontModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(144);

        // verify
        then(mockFrontModelJpaQueryRepository).should(times(1)).findPrevOneModel(frontModelEntity);
        then(mockFrontModelJpaQueryRepository).should(atLeastOnce()).findPrevOneModel(frontModelEntity);
        then(mockFrontModelJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 모델 상세 조회 Mockito 테스트")
    void 다음모델상세조회Mockito테스트() {
        // given
        frontModelEntity = FrontModelEntity.builder().idx(145L).categoryCd(2).build();
        // when
        frontModelDTO = frontModelJpaQueryRepository.findNextOneModel(frontModelEntity);

        when(mockFrontModelJpaQueryRepository.findNextOneModel(frontModelEntity)).thenReturn(frontModelDTO);
        FrontModelDTO modelInfo = mockFrontModelJpaQueryRepository.findNextOneModel(frontModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(147);

        // verify
        verify(mockFrontModelJpaQueryRepository, times(1)).findNextOneModel(frontModelEntity);
        verify(mockFrontModelJpaQueryRepository, atLeastOnce()).findNextOneModel(frontModelEntity);
        verifyNoMoreInteractions(mockFrontModelJpaQueryRepository);

        InOrder inOrder = inOrder(mockFrontModelJpaQueryRepository);
        inOrder.verify(mockFrontModelJpaQueryRepository).findNextOneModel(frontModelEntity);
    }

    @Test
    @DisplayName("다음 모델 상세 조회 BDD 테스트")
    void 다음모델상세조회BDD테스트() {
        // given
        frontModelEntity = FrontModelEntity.builder().idx(145L).categoryCd(2).build();
        // when
        frontModelDTO = frontModelJpaQueryRepository.findNextOneModel(frontModelEntity);

        given(mockFrontModelJpaQueryRepository.findNextOneModel(frontModelEntity)).willReturn(frontModelDTO);
        FrontModelDTO modelInfo = mockFrontModelJpaQueryRepository.findNextOneModel(frontModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(147);

        // verify
        then(mockFrontModelJpaQueryRepository).should(times(1)).findNextOneModel(frontModelEntity);
        then(mockFrontModelJpaQueryRepository).should(atLeastOnce()).findNextOneModel(frontModelEntity);
        then(mockFrontModelJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 메인 배너 조회 테스트")
    void 모델메인배너리스트조회테스트() {
        // when
        List<FrontModelDTO> mainModelList = frontModelJpaQueryRepository.findMainModelList();

        Optional<FrontModelDTO> mainModelFirstInfo = frontModelJpaQueryRepository.findMainModelList().stream().findFirst();

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
        when(mockFrontModelJpaQueryRepository.findMainModelList()).thenReturn(returnModelList);
        List<FrontModelDTO> modelList = mockFrontModelJpaQueryRepository.findMainModelList();

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
        verify(mockFrontModelJpaQueryRepository, times(1)).findMainModelList();
        verify(mockFrontModelJpaQueryRepository, atLeastOnce()).findMainModelList();
        verifyNoMoreInteractions(mockFrontModelJpaQueryRepository);

        InOrder inOrder = inOrder(mockFrontModelJpaQueryRepository);
        inOrder.verify(mockFrontModelJpaQueryRepository).findMainModelList();
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
        given(mockFrontModelJpaQueryRepository.findMainModelList()).willReturn(returnModelList);
        List<FrontModelDTO> modelList = mockFrontModelJpaQueryRepository.findMainModelList();

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
        then(mockFrontModelJpaQueryRepository).should(times(1)).findMainModelList();
        then(mockFrontModelJpaQueryRepository).should(atLeastOnce()).findMainModelList();
        then(mockFrontModelJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 좋아요 갯수 조회 Mockito 테스트")
    void 모델좋아요갯수조회Mockito테스트() {
        // given
        em.persist(frontModelEntity);
        frontModelDTO = FrontModelEntity.toDto(frontModelEntity);

        // when
        when(mockFrontModelJpaQueryRepository.findOneModel(frontModelEntity.getIdx())).thenReturn(frontModelDTO);
        FrontModelDTO modelInfo = mockFrontModelJpaQueryRepository.findOneModel(frontModelEntity.getIdx());

        // then
        assertThat(modelInfo.getModelFavoriteCount()).isEqualTo(frontModelEntity.getModelFavoriteCount());

        // verify
        verify(mockFrontModelJpaQueryRepository, times(1)).findOneModel(frontModelEntity.getIdx());
        verify(mockFrontModelJpaQueryRepository, atLeastOnce()).findOneModel(frontModelEntity.getIdx());
        verifyNoMoreInteractions(mockFrontModelJpaQueryRepository);
    }

    @Test
    @DisplayName("모델 좋아요 갯수 조회 BDD 테스트")
    void 모델좋아요갯수조회BDD테스트() {
        // given
        em.persist(frontModelEntity);
        frontModelDTO = FrontModelEntity.toDto(frontModelEntity);

        // when
        given(mockFrontModelJpaQueryRepository.findOneModel(frontModelEntity.getIdx())).willReturn(frontModelDTO);
        FrontModelDTO modelInfo = mockFrontModelJpaQueryRepository.findOneModel(frontModelEntity.getIdx());

        // then
        assertThat(modelInfo.getModelFavoriteCount()).isEqualTo(frontModelEntity.getModelFavoriteCount());

        // verify
        then(mockFrontModelJpaQueryRepository).should(times(1)).findOneModel(frontModelEntity.getIdx());
        then(mockFrontModelJpaQueryRepository).should(atLeastOnce()).findOneModel(frontModelEntity.getIdx());
        then(mockFrontModelJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 좋아요 Mockito 테스트")
    void 모델좋아요Mockito테스트() {
        // given
        em.persist(frontModelEntity);
        frontModelDTO = FrontModelEntity.toDto(frontModelEntity);

        Integer favoriteCount = frontModelJpaQueryRepository.favoriteModel(frontModelEntity.getIdx());

        // when
        when(mockFrontModelJpaQueryRepository.favoriteModel(frontModelEntity.getIdx())).thenReturn(favoriteCount);
        Integer newFavoriteCount = mockFrontModelJpaQueryRepository.favoriteModel(frontModelEntity.getIdx());

        // then
        assertThat(newFavoriteCount).isEqualTo(favoriteCount);

        // verify
        verify(mockFrontModelJpaQueryRepository, times(1)).favoriteModel(frontModelEntity.getIdx());
        verify(mockFrontModelJpaQueryRepository, atLeastOnce()).favoriteModel(frontModelEntity.getIdx());
        verifyNoMoreInteractions(mockFrontModelJpaQueryRepository);

        InOrder inOrder = inOrder(mockFrontModelJpaQueryRepository);
        inOrder.verify(mockFrontModelJpaQueryRepository).favoriteModel(frontModelEntity.getIdx());
    }

    @Test
    @DisplayName("모델 좋아요 BDD 테스트")
    void 모델좋아요BDD테스트() {
        // given
        em.persist(frontModelEntity);
        frontModelDTO = FrontModelEntity.toDto(frontModelEntity);

        Integer favoriteCount = frontModelJpaQueryRepository.favoriteModel(frontModelEntity.getIdx());

        // when
        given(mockFrontModelJpaQueryRepository.favoriteModel(frontModelEntity.getIdx())).willReturn(favoriteCount);
        Integer newFavoriteCount = mockFrontModelJpaQueryRepository.favoriteModel(frontModelEntity.getIdx());

        // then
        assertThat(newFavoriteCount).isEqualTo(favoriteCount);

        // verify
        then(mockFrontModelJpaQueryRepository).should(times(1)).favoriteModel(frontModelEntity.getIdx());
        then(mockFrontModelJpaQueryRepository).should(atLeastOnce()).favoriteModel(frontModelEntity.getIdx());
        then(mockFrontModelJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("새로운 모델 리스트 조회 Mockito 테스트")
    void 새로운모델리스트조회Mockito테스트() {
        // 정상
        // given
        Map<String, Object> newModelMap = new HashMap<>();
        newModelMap.put("categoryCd", 1);
        newModelMap.put("newYn", "Y");
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<FrontModelDTO> modelList = new ArrayList<>();
        modelList.add(FrontModelDTO.builder().idx(3L).categoryCd(1).modelKorName("테스트모델1").newYn("Y").modelAgency(frontAgencyDTO).modelImage(commonImageDtoList).build());
        modelList.add(FrontModelDTO.builder().idx(4L).categoryCd(2).modelKorName("테스트모델2").newYn("Y").modelAgency(frontAgencyDTO).modelImage(commonImageDtoList).build());

        Page<FrontModelDTO> resultPage = new PageImpl<>(modelList, pageRequest, modelList.size());
        // when
        when(mockFrontModelJpaQueryRepository.findModelList(newModelMap, pageRequest)).thenReturn(resultPage);
        Page<FrontModelDTO> newModelList = mockFrontModelJpaQueryRepository.findModelList(newModelMap, pageRequest);
        List<FrontModelDTO> findModelList = newModelList.stream().collect(Collectors.toList());

        // then
        assertThat(findModelList.get(0).getIdx()).isEqualTo(modelList.get(0).getIdx());
        assertThat(findModelList.get(0).getCategoryCd()).isEqualTo(modelList.get(0).getCategoryCd());
        assertThat(findModelList.get(0).getModelKorName()).isEqualTo(modelList.get(0).getModelKorName());
        assertThat(findModelList.get(0).getNewYn()).isEqualTo(modelList.get(0).getNewYn());
        assertThat(findModelList.get(0).getModelAgency().getAgencyName()).isEqualTo(modelList.get(0).getModelAgency().getAgencyName());
        assertThat(findModelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(modelList.get(0).getModelAgency().getAgencyDescription());
        assertThat(findModelList.get(0).getModelImage().get(0).getFileName()).isEqualTo(modelList.get(0).getModelImage().get(0).getFileName());

        // verify
        verify(mockFrontModelJpaQueryRepository, times(1)).findModelList(newModelMap, pageRequest);
        verify(mockFrontModelJpaQueryRepository, atLeastOnce()).findModelList(newModelMap, pageRequest);
        verifyNoMoreInteractions(mockFrontModelJpaQueryRepository);

        InOrder inOrder = inOrder(mockFrontModelJpaQueryRepository);
        inOrder.verify(mockFrontModelJpaQueryRepository).findModelList(newModelMap, pageRequest);
    }

    @Test
    @DisplayName("새로운모델 리스트 조회 BDD 테스트")
    void 새로운모델리스트조회BDD테스트() {
        // 정상
        // given
        Map<String, Object> newModelMap = new HashMap<>();
        newModelMap.put("categoryCd", 1);
        newModelMap.put("newYn", "Y");
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<FrontModelDTO> modelList = new ArrayList<>();
        modelList.add(FrontModelDTO.builder().idx(3L).categoryCd(1).modelKorName("테스트모델1").newYn("Y").modelAgency(frontAgencyDTO).modelImage(commonImageDtoList).build());
        modelList.add(FrontModelDTO.builder().idx(4L).categoryCd(2).modelKorName("테스트모델2").newYn("Y").modelAgency(frontAgencyDTO).modelImage(commonImageDtoList).build());

        Page<FrontModelDTO> resultPage = new PageImpl<>(modelList, pageRequest, modelList.size());
        // when
        given(mockFrontModelJpaQueryRepository.findModelList(newModelMap, pageRequest)).willReturn(resultPage);
        Page<FrontModelDTO> newModelList = mockFrontModelJpaQueryRepository.findModelList(newModelMap, pageRequest);
        List<FrontModelDTO> findModelList = newModelList.stream().collect(Collectors.toList());

        // then
        assertThat(findModelList.get(0).getIdx()).isEqualTo(modelList.get(0).getIdx());
        assertThat(findModelList.get(0).getCategoryCd()).isEqualTo(modelList.get(0).getCategoryCd());
        assertThat(findModelList.get(0).getModelKorName()).isEqualTo(modelList.get(0).getModelKorName());
        assertThat(findModelList.get(0).getNewYn()).isEqualTo(modelList.get(0).getNewYn());
        assertThat(findModelList.get(0).getModelAgency().getAgencyName()).isEqualTo(modelList.get(0).getModelAgency().getAgencyName());
        assertThat(findModelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(modelList.get(0).getModelAgency().getAgencyDescription());
        assertThat(findModelList.get(0).getModelImage().get(0).getFileName()).isEqualTo(modelList.get(0).getModelImage().get(0).getFileName());

        // verify
        then(mockFrontModelJpaQueryRepository).should(times(1)).findModelList(newModelMap, pageRequest);
        then(mockFrontModelJpaQueryRepository).should(atLeastOnce()).findModelList(newModelMap, pageRequest);
        then(mockFrontModelJpaQueryRepository).shouldHaveNoMoreInteractions();
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

        assertThat(frontModelJpaQueryRepository.findRecommendList(PageRequest.of(1, 10))).isNotEmpty();
    }

    @Test
    @DisplayName("검색어 랭킹 리스트 조회 테스트")
    void 검색어랭킹리스트조회테스트() {
        em.persist(FrontSearchEntity.builder().searchKeyword("모델1").build());
        em.persist(FrontSearchEntity.builder().searchKeyword("모델1").build());
        em.persist(FrontSearchEntity.builder().searchKeyword("모델1").build());
        em.persist(FrontSearchEntity.builder().searchKeyword("모델2").build());

        Page<FrontSearchDTO> searchResult = frontModelJpaQueryRepository.rankingKeywordList(PageRequest.of(1, 10));
        List<FrontSearchDTO> searchList = searchResult.stream().collect(Collectors.toList());

        assertThat(searchList.get(0).getSearchKeyword()).isEqualTo("모델1");
        assertThat(searchList.get(1).getSearchKeyword()).isEqualTo("모델2");
    }

    @Test
    @DisplayName("추천 검색어 or 검색어 랭킹을 통한 모델 검색 조회")
    void 추천검색어or검색어랭킹을통한모델검색조회() {
        assertThat(frontModelJpaQueryRepository.findModelKeyword("김예영").get(0).getModelKorName()).isEqualTo("김예영");
    }
}
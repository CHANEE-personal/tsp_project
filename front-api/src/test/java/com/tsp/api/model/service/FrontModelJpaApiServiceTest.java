package com.tsp.api.model.service;

import com.tsp.api.common.domain.CommonImageDTO;
import com.tsp.api.common.domain.CommonImageEntity;
import com.tsp.api.common.domain.NewCodeEntity;
import com.tsp.api.model.domain.FrontModelDTO;
import com.tsp.api.model.domain.FrontModelEntity;
import com.tsp.api.model.domain.agency.FrontAgencyDTO;
import com.tsp.api.model.domain.agency.FrontAgencyEntity;
import com.tsp.api.model.domain.recommend.FrontRecommendEntity;
import com.tsp.api.model.domain.search.FrontSearchDTO;
import com.tsp.api.model.domain.search.FrontSearchEntity;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("모델 Service Test")
class FrontModelJpaApiServiceTest {
    private final FrontModelJpaApiService frontModelJpaApiService;
    @Mock private FrontModelJpaApiService mockFrontModelJpaApiService;

    private FrontModelEntity frontModelEntity;
    private FrontModelDTO frontModelDTO;
    private FrontAgencyEntity frontAgencyEntity;
    private FrontAgencyDTO frontAgencyDTO;
    private CommonImageEntity commonImageEntity;
    private CommonImageDTO commonImageDTO;
    private NewCodeEntity newCodeEntity;


    private final EntityManager em;

    private void createModel() {
        frontAgencyEntity = FrontAgencyEntity.builder()
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

        em.persist(frontAgencyEntity);

        frontAgencyDTO = FrontAgencyEntity.toDto(frontAgencyEntity);

        newCodeEntity = NewCodeEntity.builder()
                .categoryCd(999)
                .categoryNm("남성모델")
                .visible("Y")
                .cmmType("model")
                .build();

        em.persist(newCodeEntity);

        frontModelEntity = FrontModelEntity.builder()
                .newModelCodeJpaDTO(newCodeEntity)
                .categoryAge(2)
                .frontAgencyEntity(frontAgencyEntity)
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
                .newYn("N")
                .modelFavoriteCount(1)
                .visible("Y")
                .build();

        em.persist(frontModelEntity);

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
    @DisplayName("모델 리스트 조회 예외 테스트")
    void 모델리스트조회예외테스트() {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", -1);
        PageRequest pageRequest = PageRequest.of(1, 3);

        // then
        assertThatThrownBy(() -> frontModelJpaApiService.findModelList(modelMap, pageRequest))
                .isInstanceOf(TspException.class);
    }

    @Test
    @DisplayName("모델리스트조회Mockito테스트")
    void 모델리스트조회Mockito테스트() {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", "1");
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontModelDTO> returnModelList = new ArrayList<>();

        // 남성
        returnModelList.add(FrontModelDTO.builder().idx(1L).categoryCd(1).modelKorName("남성모델").modelAgency(frontAgencyDTO).modelEngName("menModel").build());
        // 여성
        returnModelList.add(FrontModelDTO.builder().idx(2L).categoryCd(2).modelKorName("여성모델").modelAgency(frontAgencyDTO).modelEngName("womenModel").build());
        // 시니어
        returnModelList.add(FrontModelDTO.builder().idx(3L).categoryCd(3).modelKorName("시니어모델").modelAgency(frontAgencyDTO).modelEngName("seniorModel").build());

        Page<FrontModelDTO> resultPage = new PageImpl<>(returnModelList, pageRequest, returnModelList.size());

        // when
        when(mockFrontModelJpaApiService.findModelList(modelMap, pageRequest)).thenReturn(resultPage);
        Page<FrontModelDTO> modelList = mockFrontModelJpaApiService.findModelList(modelMap, pageRequest);

        List<FrontModelDTO> findModelList = modelList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findModelList).isNotEmpty(),
                () -> assertThat(findModelList).hasSize(3)
        );

        assertThat(findModelList.get(0).getIdx()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(findModelList.get(0).getCategoryCd()).isEqualTo(returnModelList.get(0).getCategoryCd());
        assertThat(findModelList.get(0).getModelKorName()).isEqualTo(returnModelList.get(0).getModelKorName());
        assertThat(findModelList.get(0).getModelEngName()).isEqualTo(returnModelList.get(0).getModelEngName());
        assertThat(findModelList.get(0).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyName());
        assertThat(findModelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyDescription());

        assertThat(findModelList.get(1).getIdx()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(findModelList.get(1).getCategoryCd()).isEqualTo(returnModelList.get(1).getCategoryCd());
        assertThat(findModelList.get(1).getModelKorName()).isEqualTo(returnModelList.get(1).getModelKorName());
        assertThat(findModelList.get(1).getModelEngName()).isEqualTo(returnModelList.get(1).getModelEngName());
        assertThat(findModelList.get(1).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyName());
        assertThat(findModelList.get(1).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyDescription());

        assertThat(findModelList.get(2).getIdx()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(findModelList.get(2).getCategoryCd()).isEqualTo(returnModelList.get(2).getCategoryCd());
        assertThat(findModelList.get(2).getModelKorName()).isEqualTo(returnModelList.get(2).getModelKorName());
        assertThat(findModelList.get(2).getModelEngName()).isEqualTo(returnModelList.get(2).getModelEngName());
        assertThat(findModelList.get(2).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyName());
        assertThat(findModelList.get(2).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyDescription());

        // verify
        verify(mockFrontModelJpaApiService, times(1)).findModelList(modelMap, pageRequest);
        verify(mockFrontModelJpaApiService, atLeastOnce()).findModelList(modelMap, pageRequest);
        verifyNoMoreInteractions(mockFrontModelJpaApiService);

        InOrder inOrder = inOrder(mockFrontModelJpaApiService);
        inOrder.verify(mockFrontModelJpaApiService).findModelList(modelMap, pageRequest);
    }

    @Test
    @DisplayName("모델리스트조회BDD테스트")
    void 모델리스트조회BDD테스트() {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", "1");
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontModelDTO> returnModelList = new ArrayList<>();

        // 남성
        returnModelList.add(FrontModelDTO.builder().idx(1L).categoryCd(1).modelKorName("남성모델").modelAgency(frontAgencyDTO).modelEngName("menModel").build());
        // 여성
        returnModelList.add(FrontModelDTO.builder().idx(2L).categoryCd(2).modelKorName("여성모델").modelAgency(frontAgencyDTO).modelEngName("womenModel").build());
        // 시니어
        returnModelList.add(FrontModelDTO.builder().idx(3L).categoryCd(3).modelKorName("시니어모델").modelAgency(frontAgencyDTO).modelEngName("seniorModel").build());

        Page<FrontModelDTO> resultPage = new PageImpl<>(returnModelList, pageRequest, returnModelList.size());
        // when
        given(mockFrontModelJpaApiService.findModelList(modelMap, pageRequest)).willReturn(resultPage);
        Page<FrontModelDTO> modelList = mockFrontModelJpaApiService.findModelList(modelMap, pageRequest);

        List<FrontModelDTO> findModelList = modelList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findModelList).isNotEmpty(),
                () -> assertThat(findModelList).hasSize(3)
        );

        assertThat(findModelList.get(0).getIdx()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(findModelList.get(0).getCategoryCd()).isEqualTo(returnModelList.get(0).getCategoryCd());
        assertThat(findModelList.get(0).getModelKorName()).isEqualTo(returnModelList.get(0).getModelKorName());
        assertThat(findModelList.get(0).getModelEngName()).isEqualTo(returnModelList.get(0).getModelEngName());
        assertThat(findModelList.get(0).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyName());
        assertThat(findModelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyDescription());

        assertThat(findModelList.get(1).getIdx()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(findModelList.get(1).getCategoryCd()).isEqualTo(returnModelList.get(1).getCategoryCd());
        assertThat(findModelList.get(1).getModelKorName()).isEqualTo(returnModelList.get(1).getModelKorName());
        assertThat(findModelList.get(1).getModelEngName()).isEqualTo(returnModelList.get(1).getModelEngName());
        assertThat(findModelList.get(1).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyName());
        assertThat(findModelList.get(1).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyDescription());

        assertThat(findModelList.get(2).getIdx()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(findModelList.get(2).getCategoryCd()).isEqualTo(returnModelList.get(2).getCategoryCd());
        assertThat(findModelList.get(2).getModelKorName()).isEqualTo(returnModelList.get(2).getModelKorName());
        assertThat(findModelList.get(2).getModelEngName()).isEqualTo(returnModelList.get(2).getModelEngName());
        assertThat(findModelList.get(2).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyName());
        assertThat(findModelList.get(2).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyDescription());

        // verify
        then(mockFrontModelJpaApiService).should(times(1)).findModelList(modelMap, pageRequest);
        then(mockFrontModelJpaApiService).should(atLeastOnce()).findModelList(modelMap, pageRequest);
        then(mockFrontModelJpaApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델배너리스트조회Mockito테스트")
    void 모델배너리스트조회Mockito테스트() {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", "1");

        frontModelJpaApiService.findMainModelList();

        List<FrontModelDTO> returnModelList = new ArrayList<>();

        // 남성
        returnModelList.add(FrontModelDTO.builder().idx(1L).categoryCd(1).modelKorName("남성모델").modelEngName("menModel").modelMainYn("Y").build());
        // 여성
        returnModelList.add(FrontModelDTO.builder().idx(2L).categoryCd(2).modelKorName("여성모델").modelEngName("womenModel").modelMainYn("Y").build());
        // 시니어
        returnModelList.add(FrontModelDTO.builder().idx(3L).categoryCd(3).modelKorName("시니어모델").modelEngName("seniorModel").modelMainYn("Y").build());

        // when
        when(mockFrontModelJpaApiService.findMainModelList()).thenReturn(returnModelList);
        List<FrontModelDTO> mainModelList = mockFrontModelJpaApiService.findMainModelList();

        // then
        assertAll(
                () -> assertThat(mainModelList).isNotEmpty(),
                () -> assertThat(mainModelList).hasSize(3)
        );

        assertThat(mainModelList.get(0).getIdx()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(mainModelList.get(0).getCategoryCd()).isEqualTo(returnModelList.get(0).getCategoryCd());
        assertThat(mainModelList.get(0).getModelKorName()).isEqualTo(returnModelList.get(0).getModelKorName());
        assertThat(mainModelList.get(0).getModelEngName()).isEqualTo(returnModelList.get(0).getModelEngName());
        assertThat(mainModelList.get(0).getModelMainYn()).isEqualTo(returnModelList.get(0).getModelMainYn());

        assertThat(mainModelList.get(1).getIdx()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(mainModelList.get(1).getCategoryCd()).isEqualTo(returnModelList.get(1).getCategoryCd());
        assertThat(mainModelList.get(1).getModelKorName()).isEqualTo(returnModelList.get(1).getModelKorName());
        assertThat(mainModelList.get(1).getModelEngName()).isEqualTo(returnModelList.get(1).getModelEngName());
        assertThat(mainModelList.get(1).getModelMainYn()).isEqualTo(returnModelList.get(1).getModelMainYn());

        assertThat(mainModelList.get(2).getIdx()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(mainModelList.get(2).getCategoryCd()).isEqualTo(returnModelList.get(2).getCategoryCd());
        assertThat(mainModelList.get(2).getModelKorName()).isEqualTo(returnModelList.get(2).getModelKorName());
        assertThat(mainModelList.get(2).getModelEngName()).isEqualTo(returnModelList.get(2).getModelEngName());
        assertThat(mainModelList.get(2).getModelMainYn()).isEqualTo(returnModelList.get(2).getModelMainYn());

        // verify
        verify(mockFrontModelJpaApiService, times(1)).findMainModelList();
        verify(mockFrontModelJpaApiService, atLeastOnce()).findMainModelList();
        verifyNoMoreInteractions(mockFrontModelJpaApiService);

        InOrder inOrder = inOrder(mockFrontModelJpaApiService);
        inOrder.verify(mockFrontModelJpaApiService).findMainModelList();
    }

    @Test
    @DisplayName("모델배너리스트조회BDD테스트")
    void 모델배너리스트조회BDD테스트() {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", "1");

        List<FrontModelDTO> returnModelList = new ArrayList<>();

        // 남성
        returnModelList.add(FrontModelDTO.builder().idx(1L).categoryCd(1).modelKorName("남성모델").modelEngName("menModel").modelMainYn("Y").build());
        // 여성
        returnModelList.add(FrontModelDTO.builder().idx(2L).categoryCd(2).modelKorName("여성모델").modelEngName("womenModel").modelMainYn("Y").build());
        // 시니어
        returnModelList.add(FrontModelDTO.builder().idx(3L).categoryCd(3).modelKorName("시니어모델").modelEngName("seniorModel").modelMainYn("Y").build());

        // when
        given(mockFrontModelJpaApiService.findMainModelList()).willReturn(returnModelList);
        List<FrontModelDTO> mainModelList = mockFrontModelJpaApiService.findMainModelList();

        // then
        assertAll(
                () -> assertThat(mainModelList).isNotEmpty(),
                () -> assertThat(mainModelList).hasSize(3)
        );

        assertThat(mainModelList.get(0).getIdx()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(mainModelList.get(0).getCategoryCd()).isEqualTo(returnModelList.get(0).getCategoryCd());
        assertThat(mainModelList.get(0).getModelKorName()).isEqualTo(returnModelList.get(0).getModelKorName());
        assertThat(mainModelList.get(0).getModelEngName()).isEqualTo(returnModelList.get(0).getModelEngName());
        assertThat(mainModelList.get(0).getModelMainYn()).isEqualTo(returnModelList.get(0).getModelMainYn());

        assertThat(mainModelList.get(1).getIdx()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(mainModelList.get(1).getCategoryCd()).isEqualTo(returnModelList.get(1).getCategoryCd());
        assertThat(mainModelList.get(1).getModelKorName()).isEqualTo(returnModelList.get(1).getModelKorName());
        assertThat(mainModelList.get(1).getModelEngName()).isEqualTo(returnModelList.get(1).getModelEngName());
        assertThat(mainModelList.get(1).getModelMainYn()).isEqualTo(returnModelList.get(1).getModelMainYn());

        assertThat(mainModelList.get(2).getIdx()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(mainModelList.get(2).getCategoryCd()).isEqualTo(returnModelList.get(2).getCategoryCd());
        assertThat(mainModelList.get(2).getModelKorName()).isEqualTo(returnModelList.get(2).getModelKorName());
        assertThat(mainModelList.get(2).getModelEngName()).isEqualTo(returnModelList.get(2).getModelEngName());
        assertThat(mainModelList.get(2).getModelMainYn()).isEqualTo(returnModelList.get(2).getModelMainYn());

        // verify
        then(mockFrontModelJpaApiService).should(times(1)).findMainModelList();
        then(mockFrontModelJpaApiService).should(atLeastOnce()).findMainModelList();
        then(mockFrontModelJpaApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델상세조회Mockito테스트")
    void 모델상세조회Mockito테스트() {
        // given
        frontModelDTO = FrontModelDTO.builder()
                .idx(1L)
                .categoryCd(1)
                .categoryAge(2)
                .agencyIdx(1L)
                .modelKorName("조찬희")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .modelAgency(FrontAgencyEntity.toDto(frontAgencyEntity))
                .build();

        // when
        when(mockFrontModelJpaApiService.findOneModel(frontModelEntity.getIdx())).thenReturn(frontModelDTO);
        FrontModelDTO modelInfo = mockFrontModelJpaApiService.findOneModel(frontModelEntity.getIdx());

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(frontModelDTO.getIdx());
        assertThat(modelInfo.getAgencyIdx()).isEqualTo(1);
        assertThat(modelInfo.getModelAgency().getAgencyName()).isEqualTo("agency");
        assertThat(modelInfo.getModelAgency().getAgencyDescription()).isEqualTo("agency");
        assertThat(modelInfo.getCategoryCd()).isEqualTo(frontModelDTO.getCategoryCd());
        assertThat(modelInfo.getModelKorName()).isEqualTo(frontModelDTO.getModelKorName());
        assertThat(modelInfo.getModelEngName()).isEqualTo(frontModelDTO.getModelEngName());

        // verify
        verify(mockFrontModelJpaApiService, times(1)).findOneModel(frontModelEntity.getIdx());
        verify(mockFrontModelJpaApiService, atLeastOnce()).findOneModel(frontModelEntity.getIdx());
        verifyNoMoreInteractions(mockFrontModelJpaApiService);

        InOrder inOrder = inOrder(mockFrontModelJpaApiService);
        inOrder.verify(mockFrontModelJpaApiService).findOneModel(frontModelEntity.getIdx());
    }

    @Test
    @DisplayName("모델상세조회BDD테스트")
    void 모델상세조회BDD테스트() {
        // given
        frontModelDTO = FrontModelDTO.builder()
                .idx(1L)
                .categoryCd(1)
                .categoryAge(2)
                .agencyIdx(1L)
                .modelKorName("조찬희")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .modelAgency(FrontAgencyEntity.toDto(frontAgencyEntity))
                .build();

        // when
        given(mockFrontModelJpaApiService.findOneModel(frontModelEntity.getIdx())).willReturn(frontModelDTO);
        FrontModelDTO modelInfo = mockFrontModelJpaApiService.findOneModel(frontModelEntity.getIdx());

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(frontModelDTO.getIdx());
        assertThat(modelInfo.getAgencyIdx()).isEqualTo(1);
        assertThat(modelInfo.getModelAgency().getAgencyName()).isEqualTo("agency");
        assertThat(modelInfo.getModelAgency().getAgencyDescription()).isEqualTo("agency");
        assertThat(modelInfo.getCategoryCd()).isEqualTo(frontModelDTO.getCategoryCd());
        assertThat(modelInfo.getModelKorName()).isEqualTo(frontModelDTO.getModelKorName());
        assertThat(modelInfo.getModelEngName()).isEqualTo(frontModelDTO.getModelEngName());

        // verify
        then(mockFrontModelJpaApiService).should(times(1)).findOneModel(frontModelEntity.getIdx());
        then(mockFrontModelJpaApiService).should(atLeastOnce()).findOneModel(frontModelEntity.getIdx());
        then(mockFrontModelJpaApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 모델 상세 조회 테스트")
    void 이전or다음모델상세조회테스트() {
        // when
        frontModelDTO = frontModelJpaApiService.findOneModel(frontModelEntity.getIdx());

        // 이전 모델
        assertThat(frontModelJpaApiService.findPrevOneModel(frontModelEntity.getIdx(), newCodeEntity.getCategoryCd()).getIdx()).isEqualTo(144);
        // 다음 모델
        assertThat(frontModelJpaApiService.findNextOneModel(frontModelEntity.getIdx(), newCodeEntity.getCategoryCd()).getIdx()).isEqualTo(147);
    }

    @Test
    @DisplayName("모델 좋아요 Mockito 테스트")
    void 모델좋아요Mockito테스트() {
        // given
        em.persist(frontModelEntity);

        Integer favoriteCount = frontModelJpaApiService.favoriteModel(frontModelEntity.getIdx());

        // when
        when(mockFrontModelJpaApiService.favoriteModel(frontModelEntity.getIdx())).thenReturn(favoriteCount);
        Integer newFavoriteCount = mockFrontModelJpaApiService.favoriteModel(frontModelEntity.getIdx());

        // then
        assertThat(newFavoriteCount).isEqualTo(favoriteCount);

        // verify
        verify(mockFrontModelJpaApiService, times(1)).favoriteModel(frontModelEntity.getIdx());
        verify(mockFrontModelJpaApiService, atLeastOnce()).favoriteModel(frontModelEntity.getIdx());
        verifyNoMoreInteractions(mockFrontModelJpaApiService);

        InOrder inOrder = inOrder(mockFrontModelJpaApiService);
        inOrder.verify(mockFrontModelJpaApiService).favoriteModel(frontModelEntity.getIdx());
    }

    @Test
    @DisplayName("모델 좋아요 BDD 테스트")
    void 모델좋아요BDD테스트() {
        // given
        em.persist(frontModelEntity);
        frontModelDTO = FrontModelEntity.toDto(frontModelEntity);

        Integer favoriteCount = frontModelJpaApiService.favoriteModel(frontModelEntity.getIdx());

        // when
        given(mockFrontModelJpaApiService.favoriteModel(frontModelEntity.getIdx())).willReturn(favoriteCount);
        Integer newFavoriteCount = mockFrontModelJpaApiService.favoriteModel(frontModelEntity.getIdx());

        // then
        assertThat(newFavoriteCount).isEqualTo(favoriteCount);

        // verify
        then(mockFrontModelJpaApiService).should(times(1)).favoriteModel(frontModelEntity.getIdx());
        then(mockFrontModelJpaApiService).should(atLeastOnce()).favoriteModel(frontModelEntity.getIdx());
        then(mockFrontModelJpaApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("새로운모델리스트조회Mockito테스트")
    void 새로운모델리스트조회Mockito테스트() {
        Map<String, Object> newModelMap = new HashMap<>();
        newModelMap.put("categoryCd", "1");
        newModelMap.put("newYn", "Y");
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontModelDTO> returnModelList = new ArrayList<>();

        // 남성
        returnModelList.add(FrontModelDTO.builder().idx(1L).categoryCd(1).modelKorName("남성모델").newYn("Y").modelAgency(frontAgencyDTO).modelEngName("menModel").build());
        // 여성
        returnModelList.add(FrontModelDTO.builder().idx(2L).categoryCd(2).modelKorName("여성모델").newYn("Y").modelAgency(frontAgencyDTO).modelEngName("womenModel").build());
        // 시니어
        returnModelList.add(FrontModelDTO.builder().idx(3L).categoryCd(3).modelKorName("시니어모델").newYn("Y").modelAgency(frontAgencyDTO).modelEngName("seniorModel").build());

        Page<FrontModelDTO> resultPage = new PageImpl<>(returnModelList, pageRequest, returnModelList.size());
        // when
        when(mockFrontModelJpaApiService.findModelList(newModelMap, pageRequest)).thenReturn(resultPage);
        Page<FrontModelDTO> newModelList = mockFrontModelJpaApiService.findModelList(newModelMap, pageRequest);

        List<FrontModelDTO> findModelList = newModelList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findModelList).isNotEmpty(),
                () -> assertThat(findModelList).hasSize(3)
        );

        assertThat(findModelList.get(0).getIdx()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(findModelList.get(0).getCategoryCd()).isEqualTo(returnModelList.get(0).getCategoryCd());
        assertThat(findModelList.get(0).getModelKorName()).isEqualTo(returnModelList.get(0).getModelKorName());
        assertThat(findModelList.get(0).getModelEngName()).isEqualTo(returnModelList.get(0).getModelEngName());
        assertThat(findModelList.get(0).getNewYn()).isEqualTo(returnModelList.get(0).getNewYn());
        assertThat(findModelList.get(0).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyName());
        assertThat(findModelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyDescription());

        assertThat(findModelList.get(1).getIdx()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(findModelList.get(1).getCategoryCd()).isEqualTo(returnModelList.get(1).getCategoryCd());
        assertThat(findModelList.get(1).getModelKorName()).isEqualTo(returnModelList.get(1).getModelKorName());
        assertThat(findModelList.get(1).getModelEngName()).isEqualTo(returnModelList.get(1).getModelEngName());
        assertThat(findModelList.get(1).getNewYn()).isEqualTo(returnModelList.get(1).getNewYn());
        assertThat(findModelList.get(1).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyName());
        assertThat(findModelList.get(1).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyDescription());

        assertThat(findModelList.get(2).getIdx()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(findModelList.get(2).getCategoryCd()).isEqualTo(returnModelList.get(2).getCategoryCd());
        assertThat(findModelList.get(2).getModelKorName()).isEqualTo(returnModelList.get(2).getModelKorName());
        assertThat(findModelList.get(2).getModelEngName()).isEqualTo(returnModelList.get(2).getModelEngName());
        assertThat(findModelList.get(2).getNewYn()).isEqualTo(returnModelList.get(2).getNewYn());
        assertThat(findModelList.get(2).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyName());
        assertThat(findModelList.get(2).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyDescription());

        // verify
        verify(mockFrontModelJpaApiService, times(1)).findModelList(newModelMap, pageRequest);
        verify(mockFrontModelJpaApiService, atLeastOnce()).findModelList(newModelMap, pageRequest);
        verifyNoMoreInteractions(mockFrontModelJpaApiService);

        InOrder inOrder = inOrder(mockFrontModelJpaApiService);
        inOrder.verify(mockFrontModelJpaApiService).findModelList(newModelMap, pageRequest);
    }

    @Test
    @DisplayName("새로운모델리스트조회BDD테스트")
    void 새로운모델리스트조회BDD테스트() {
        Map<String, Object> newModelMap = new HashMap<>();
        newModelMap.put("categoryCd", "1");
        newModelMap.put("newYn", "Y");
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontModelDTO> returnModelList = new ArrayList<>();

        // 남성
        returnModelList.add(FrontModelDTO.builder().idx(1L).categoryCd(1).modelKorName("남성모델").newYn("Y").modelAgency(frontAgencyDTO).modelEngName("menModel").build());
        // 여성
        returnModelList.add(FrontModelDTO.builder().idx(2L).categoryCd(2).modelKorName("여성모델").newYn("Y").modelAgency(frontAgencyDTO).modelEngName("womenModel").build());
        // 시니어
        returnModelList.add(FrontModelDTO.builder().idx(3L).categoryCd(3).modelKorName("시니어모델").newYn("Y").modelAgency(frontAgencyDTO).modelEngName("seniorModel").build());

        Page<FrontModelDTO> resultPage = new PageImpl<>(returnModelList, pageRequest, returnModelList.size());

        // when
        given(mockFrontModelJpaApiService.findModelList(newModelMap, pageRequest)).willReturn(resultPage);
        Page<FrontModelDTO> newModelList = mockFrontModelJpaApiService.findModelList(newModelMap, pageRequest);

        List<FrontModelDTO> findModelList = newModelList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findModelList).isNotEmpty(),
                () -> assertThat(findModelList).hasSize(3)
        );

        assertThat(findModelList.get(0).getIdx()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(findModelList.get(0).getCategoryCd()).isEqualTo(returnModelList.get(0).getCategoryCd());
        assertThat(findModelList.get(0).getModelKorName()).isEqualTo(returnModelList.get(0).getModelKorName());
        assertThat(findModelList.get(0).getModelEngName()).isEqualTo(returnModelList.get(0).getModelEngName());
        assertThat(findModelList.get(0).getNewYn()).isEqualTo(returnModelList.get(0).getNewYn());
        assertThat(findModelList.get(0).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyName());
        assertThat(findModelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyDescription());

        assertThat(findModelList.get(1).getIdx()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(findModelList.get(1).getCategoryCd()).isEqualTo(returnModelList.get(1).getCategoryCd());
        assertThat(findModelList.get(1).getModelKorName()).isEqualTo(returnModelList.get(1).getModelKorName());
        assertThat(findModelList.get(1).getModelEngName()).isEqualTo(returnModelList.get(1).getModelEngName());
        assertThat(findModelList.get(1).getNewYn()).isEqualTo(returnModelList.get(1).getNewYn());
        assertThat(findModelList.get(1).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyName());
        assertThat(findModelList.get(1).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyDescription());

        assertThat(findModelList.get(2).getIdx()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(findModelList.get(2).getCategoryCd()).isEqualTo(returnModelList.get(2).getCategoryCd());
        assertThat(findModelList.get(2).getModelKorName()).isEqualTo(returnModelList.get(2).getModelKorName());
        assertThat(findModelList.get(2).getModelEngName()).isEqualTo(returnModelList.get(2).getModelEngName());
        assertThat(findModelList.get(2).getNewYn()).isEqualTo(returnModelList.get(2).getNewYn());
        assertThat(findModelList.get(2).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyName());
        assertThat(findModelList.get(2).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyDescription());

        // verify
        then(mockFrontModelJpaApiService).should(times(1)).findModelList(newModelMap, pageRequest);
        then(mockFrontModelJpaApiService).should(atLeastOnce()).findModelList(newModelMap, pageRequest);
        then(mockFrontModelJpaApiService).shouldHaveNoMoreInteractions();
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

        assertThat(frontModelJpaApiService.findRecommendList(PageRequest.of(1, 10))).isNotEmpty();
    }

    @Test
    @DisplayName("검색어 랭킹 리스트 조회 테스트")
    void 검색어랭킹리스트조회테스트() {
        em.persist(FrontSearchEntity.builder().searchKeyword("모델1").build());
        em.persist(FrontSearchEntity.builder().searchKeyword("모델1").build());
        em.persist(FrontSearchEntity.builder().searchKeyword("모델1").build());
        em.persist(FrontSearchEntity.builder().searchKeyword("모델2").build());
        Page<FrontSearchDTO> searchResult = frontModelJpaApiService.rankingKeywordList(PageRequest.of(1, 10));
        List<FrontSearchDTO> searchList = searchResult.stream().collect(Collectors.toList());

        assertThat(searchList.get(0).getSearchKeyword()).isEqualTo("모델1");
        assertThat(searchList.get(1).getSearchKeyword()).isEqualTo("모델2");
    }

    @Test
    @DisplayName("추천 검색어 or 검색어 랭킹을 통한 모델 검색 조회")
    void 추천검색어or검색어랭킹을통한모델검색조회() {
        assertThat(frontModelJpaApiService.findModelKeyword("김예영").get(0).getModelKorName()).isEqualTo("김예영");
    }
}

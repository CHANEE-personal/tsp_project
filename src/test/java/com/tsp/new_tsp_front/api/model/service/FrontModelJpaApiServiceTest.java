package com.tsp.new_tsp_front.api.model.service;

import com.tsp.new_tsp_front.api.common.domain.CommonImageDTO;
import com.tsp.new_tsp_front.api.common.domain.CommonImageEntity;
import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import com.tsp.new_tsp_front.api.model.domain.agency.FrontAgencyDTO;
import com.tsp.new_tsp_front.api.model.domain.agency.FrontAgencyEntity;
import com.tsp.new_tsp_front.api.model.service.impl.Agency.AgencyMapper;
import com.tsp.new_tsp_front.api.model.service.impl.ModelImageMapper;
import com.tsp.new_tsp_front.exception.TspException;
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
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.api.model.service.impl.ModelMapper.INSTANCE;
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

    private final EntityManager em;

    private void createModel() {
        frontAgencyEntity = FrontAgencyEntity.builder()
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

        frontAgencyDTO = AgencyMapper.INSTANCE.toDto(frontAgencyEntity);

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
                .visible("Y")
                .build();

        frontModelDTO = INSTANCE.toDto(frontModelEntity);

        commonImageEntity = CommonImageEntity.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("model")
                .build();

        commonImageDTO = ModelImageMapper.INSTANCE.toDto(commonImageEntity);
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

        // then
        assertThatThrownBy(() -> frontModelJpaApiService.getModelList(modelMap))
                .isInstanceOf(TspException.class);
    }

    @Test
    @DisplayName("모델리스트조회Mockito테스트")
    void 모델리스트조회Mockito테스트() {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", "1");
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        List<FrontModelDTO> returnModelList = new ArrayList<>();

        // 남성
        returnModelList.add(FrontModelDTO.builder().idx(1).categoryCd(1).modelKorName("남성모델").modelAgency(frontAgencyDTO).modelEngName("menModel").build());
        // 여성
        returnModelList.add(FrontModelDTO.builder().idx(2).categoryCd(2).modelKorName("여성모델").modelAgency(frontAgencyDTO).modelEngName("womenModel").build());
        // 시니어
        returnModelList.add(FrontModelDTO.builder().idx(3).categoryCd(3).modelKorName("시니어모델").modelAgency(frontAgencyDTO).modelEngName("seniorModel").build());

        // when
        when(mockFrontModelJpaApiService.getModelList(modelMap)).thenReturn(returnModelList);
        List<FrontModelDTO> modelList = mockFrontModelJpaApiService.getModelList(modelMap);

        // then
        assertAll(
                () -> assertThat(modelList).isNotEmpty(),
                () -> assertThat(modelList).hasSize(3)
        );

        assertThat(modelList.get(0).getIdx()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(modelList.get(0).getCategoryCd()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(modelList.get(0).getModelKorName()).isEqualTo(returnModelList.get(0).getModelKorName());
        assertThat(modelList.get(0).getModelEngName()).isEqualTo(returnModelList.get(0).getModelEngName());
        assertThat(modelList.get(0).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyName());
        assertThat(modelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyDescription());

        assertThat(modelList.get(1).getIdx()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(modelList.get(1).getCategoryCd()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(modelList.get(1).getModelKorName()).isEqualTo(returnModelList.get(1).getModelKorName());
        assertThat(modelList.get(1).getModelEngName()).isEqualTo(returnModelList.get(1).getModelEngName());
        assertThat(modelList.get(1).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyName());
        assertThat(modelList.get(1).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyDescription());

        assertThat(modelList.get(2).getIdx()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(modelList.get(2).getCategoryCd()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(modelList.get(2).getModelKorName()).isEqualTo(returnModelList.get(2).getModelKorName());
        assertThat(modelList.get(2).getModelEngName()).isEqualTo(returnModelList.get(2).getModelEngName());
        assertThat(modelList.get(2).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyName());
        assertThat(modelList.get(2).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyDescription());

        // verify
        verify(mockFrontModelJpaApiService, times(1)).getModelList(modelMap);
        verify(mockFrontModelJpaApiService, atLeastOnce()).getModelList(modelMap);
        verifyNoMoreInteractions(mockFrontModelJpaApiService);

        InOrder inOrder = inOrder(mockFrontModelJpaApiService);
        inOrder.verify(mockFrontModelJpaApiService).getModelList(modelMap);
    }

    @Test
    @DisplayName("모델리스트조회BDD테스트")
    void 모델리스트조회BDD테스트() {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", "1");
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        List<FrontModelDTO> returnModelList = new ArrayList<>();

        // 남성
        returnModelList.add(FrontModelDTO.builder().idx(1).categoryCd(1).modelKorName("남성모델").modelAgency(frontAgencyDTO).modelEngName("menModel").build());
        // 여성
        returnModelList.add(FrontModelDTO.builder().idx(2).categoryCd(2).modelKorName("여성모델").modelAgency(frontAgencyDTO).modelEngName("womenModel").build());
        // 시니어
        returnModelList.add(FrontModelDTO.builder().idx(3).categoryCd(3).modelKorName("시니어모델").modelAgency(frontAgencyDTO).modelEngName("seniorModel").build());

        // when
        given(mockFrontModelJpaApiService.getModelList(modelMap)).willReturn(returnModelList);
        List<FrontModelDTO> modelList = mockFrontModelJpaApiService.getModelList(modelMap);

        // then
        assertAll(
                () -> assertThat(modelList).isNotEmpty(),
                () -> assertThat(modelList).hasSize(3)
        );

        assertThat(modelList.get(0).getIdx()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(modelList.get(0).getCategoryCd()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(modelList.get(0).getModelKorName()).isEqualTo(returnModelList.get(0).getModelKorName());
        assertThat(modelList.get(0).getModelEngName()).isEqualTo(returnModelList.get(0).getModelEngName());
        assertThat(modelList.get(0).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyName());
        assertThat(modelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyDescription());

        assertThat(modelList.get(1).getIdx()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(modelList.get(1).getCategoryCd()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(modelList.get(1).getModelKorName()).isEqualTo(returnModelList.get(1).getModelKorName());
        assertThat(modelList.get(1).getModelEngName()).isEqualTo(returnModelList.get(1).getModelEngName());
        assertThat(modelList.get(1).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyName());
        assertThat(modelList.get(1).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyDescription());

        assertThat(modelList.get(2).getIdx()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(modelList.get(2).getCategoryCd()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(modelList.get(2).getModelKorName()).isEqualTo(returnModelList.get(2).getModelKorName());
        assertThat(modelList.get(2).getModelEngName()).isEqualTo(returnModelList.get(2).getModelEngName());
        assertThat(modelList.get(2).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyName());
        assertThat(modelList.get(2).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyDescription());

        // verify
        then(mockFrontModelJpaApiService).should(times(1)).getModelList(modelMap);
        then(mockFrontModelJpaApiService).should(atLeastOnce()).getModelList(modelMap);
        then(mockFrontModelJpaApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델배너리스트조회Mockito테스트")
    void 모델배너리스트조회Mockito테스트() {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", "1");

        List<FrontModelDTO> returnModelList = new ArrayList<>();

        // 남성
        returnModelList.add(FrontModelDTO.builder().idx(1).categoryCd(1).modelKorName("남성모델").modelEngName("menModel").modelMainYn("Y").build());
        // 여성
        returnModelList.add(FrontModelDTO.builder().idx(2).categoryCd(2).modelKorName("여성모델").modelEngName("womenModel").modelMainYn("Y").build());
        // 시니어
        returnModelList.add(FrontModelDTO.builder().idx(3).categoryCd(3).modelKorName("시니어모델").modelEngName("seniorModel").modelMainYn("Y").build());

        // when
        when(mockFrontModelJpaApiService.getMainModelList()).thenReturn(returnModelList);
        List<FrontModelDTO> mainModelList = mockFrontModelJpaApiService.getMainModelList();

        // then
        assertAll(
                () -> assertThat(mainModelList).isNotEmpty(),
                () -> assertThat(mainModelList).hasSize(3)
        );

        assertThat(mainModelList.get(0).getIdx()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(mainModelList.get(0).getCategoryCd()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(mainModelList.get(0).getModelKorName()).isEqualTo(returnModelList.get(0).getModelKorName());
        assertThat(mainModelList.get(0).getModelEngName()).isEqualTo(returnModelList.get(0).getModelEngName());
        assertThat(mainModelList.get(0).getModelMainYn()).isEqualTo(returnModelList.get(0).getModelMainYn());

        assertThat(mainModelList.get(1).getIdx()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(mainModelList.get(1).getCategoryCd()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(mainModelList.get(1).getModelKorName()).isEqualTo(returnModelList.get(1).getModelKorName());
        assertThat(mainModelList.get(1).getModelEngName()).isEqualTo(returnModelList.get(1).getModelEngName());
        assertThat(mainModelList.get(1).getModelMainYn()).isEqualTo(returnModelList.get(1).getModelMainYn());

        assertThat(mainModelList.get(2).getIdx()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(mainModelList.get(2).getCategoryCd()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(mainModelList.get(2).getModelKorName()).isEqualTo(returnModelList.get(2).getModelKorName());
        assertThat(mainModelList.get(2).getModelEngName()).isEqualTo(returnModelList.get(2).getModelEngName());
        assertThat(mainModelList.get(2).getModelMainYn()).isEqualTo(returnModelList.get(2).getModelMainYn());

        // verify
        verify(mockFrontModelJpaApiService, times(1)).getMainModelList();
        verify(mockFrontModelJpaApiService, atLeastOnce()).getMainModelList();
        verifyNoMoreInteractions(mockFrontModelJpaApiService);

        InOrder inOrder = inOrder(mockFrontModelJpaApiService);
        inOrder.verify(mockFrontModelJpaApiService).getMainModelList();
    }

    @Test
    @DisplayName("모델배너리스트조회BDD테스트")
    void 모델배너리스트조회BDD테스트() {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", "1");

        List<FrontModelDTO> returnModelList = new ArrayList<>();

        // 남성
        returnModelList.add(FrontModelDTO.builder().idx(1).categoryCd(1).modelKorName("남성모델").modelEngName("menModel").modelMainYn("Y").build());
        // 여성
        returnModelList.add(FrontModelDTO.builder().idx(2).categoryCd(2).modelKorName("여성모델").modelEngName("womenModel").modelMainYn("Y").build());
        // 시니어
        returnModelList.add(FrontModelDTO.builder().idx(3).categoryCd(3).modelKorName("시니어모델").modelEngName("seniorModel").modelMainYn("Y").build());

        // when
        given(mockFrontModelJpaApiService.getMainModelList()).willReturn(returnModelList);
        List<FrontModelDTO> mainModelList = mockFrontModelJpaApiService.getMainModelList();

        // then
        assertAll(
                () -> assertThat(mainModelList).isNotEmpty(),
                () -> assertThat(mainModelList).hasSize(3)
        );

        assertThat(mainModelList.get(0).getIdx()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(mainModelList.get(0).getCategoryCd()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(mainModelList.get(0).getModelKorName()).isEqualTo(returnModelList.get(0).getModelKorName());
        assertThat(mainModelList.get(0).getModelEngName()).isEqualTo(returnModelList.get(0).getModelEngName());
        assertThat(mainModelList.get(0).getModelMainYn()).isEqualTo(returnModelList.get(0).getModelMainYn());

        assertThat(mainModelList.get(1).getIdx()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(mainModelList.get(1).getCategoryCd()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(mainModelList.get(1).getModelKorName()).isEqualTo(returnModelList.get(1).getModelKorName());
        assertThat(mainModelList.get(1).getModelEngName()).isEqualTo(returnModelList.get(1).getModelEngName());
        assertThat(mainModelList.get(1).getModelMainYn()).isEqualTo(returnModelList.get(1).getModelMainYn());

        assertThat(mainModelList.get(2).getIdx()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(mainModelList.get(2).getCategoryCd()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(mainModelList.get(2).getModelKorName()).isEqualTo(returnModelList.get(2).getModelKorName());
        assertThat(mainModelList.get(2).getModelEngName()).isEqualTo(returnModelList.get(2).getModelEngName());
        assertThat(mainModelList.get(2).getModelMainYn()).isEqualTo(returnModelList.get(2).getModelMainYn());

        // verify
        then(mockFrontModelJpaApiService).should(times(1)).getMainModelList();
        then(mockFrontModelJpaApiService).should(atLeastOnce()).getMainModelList();
        then(mockFrontModelJpaApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델상세조회Mockito테스트")
    void 모델상세조회Mockito테스트() {
        // given
        FrontModelEntity frontModelEntity = FrontModelEntity.builder().idx(1).categoryCd(1).agencyIdx(1).build();
        frontModelDTO = FrontModelDTO.builder()
                .idx(1)
                .categoryCd(1)
                .categoryAge(2)
                .agencyIdx(1)
                .modelKorName("조찬희")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .modelAgency(AgencyMapper.INSTANCE.toDto(frontAgencyEntity))
                .build();

        // when
        when(mockFrontModelJpaApiService.getModelInfo(frontModelEntity)).thenReturn(frontModelDTO);
        FrontModelDTO modelInfo = mockFrontModelJpaApiService.getModelInfo(frontModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(frontModelDTO.getIdx());
        assertThat(modelInfo.getAgencyIdx()).isEqualTo(1);
        assertThat(modelInfo.getModelAgency().getAgencyName()).isEqualTo("agency");
        assertThat(modelInfo.getModelAgency().getAgencyDescription()).isEqualTo("agency");
        assertThat(modelInfo.getCategoryCd()).isEqualTo(frontModelDTO.getCategoryCd());
        assertThat(modelInfo.getModelKorName()).isEqualTo(frontModelDTO.getModelKorName());
        assertThat(modelInfo.getModelEngName()).isEqualTo(frontModelDTO.getModelEngName());

        // verify
        verify(mockFrontModelJpaApiService, times(1)).getModelInfo(frontModelEntity);
        verify(mockFrontModelJpaApiService, atLeastOnce()).getModelInfo(frontModelEntity);
        verifyNoMoreInteractions(mockFrontModelJpaApiService);

        InOrder inOrder = inOrder(mockFrontModelJpaApiService);
        inOrder.verify(mockFrontModelJpaApiService).getModelInfo(frontModelEntity);
    }

    @Test
    @DisplayName("모델상세조회BDD테스트")
    void 모델상세조회BDD테스트() {
        // given
        FrontModelEntity frontModelEntity = FrontModelEntity.builder().idx(1).categoryCd(1).build();
        frontModelDTO = FrontModelDTO.builder()
                .idx(1)
                .categoryCd(1)
                .categoryAge(2)
                .agencyIdx(1)
                .modelKorName("조찬희")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .modelAgency(AgencyMapper.INSTANCE.toDto(frontAgencyEntity))
                .build();

        // when
        given(mockFrontModelJpaApiService.getModelInfo(frontModelEntity)).willReturn(frontModelDTO);
        FrontModelDTO modelInfo = mockFrontModelJpaApiService.getModelInfo(frontModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(frontModelDTO.getIdx());
        assertThat(modelInfo.getAgencyIdx()).isEqualTo(1);
        assertThat(modelInfo.getModelAgency().getAgencyName()).isEqualTo("agency");
        assertThat(modelInfo.getModelAgency().getAgencyDescription()).isEqualTo("agency");
        assertThat(modelInfo.getCategoryCd()).isEqualTo(frontModelDTO.getCategoryCd());
        assertThat(modelInfo.getModelKorName()).isEqualTo(frontModelDTO.getModelKorName());
        assertThat(modelInfo.getModelEngName()).isEqualTo(frontModelDTO.getModelEngName());

        // verify
        then(mockFrontModelJpaApiService).should(times(1)).getModelInfo(frontModelEntity);
        then(mockFrontModelJpaApiService).should(atLeastOnce()).getModelInfo(frontModelEntity);
        then(mockFrontModelJpaApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 좋아요 Mockito 테스트")
    void 모델좋아요Mockito테스트() {
        // given
        em.persist(frontModelEntity);

        Integer favoriteCount = frontModelJpaApiService.favoriteModel(frontModelEntity);

        // when
        when(mockFrontModelJpaApiService.favoriteModel(frontModelEntity)).thenReturn(favoriteCount);
        Integer newFavoriteCount = mockFrontModelJpaApiService.favoriteModel(frontModelEntity);

        // then
        assertThat(newFavoriteCount).isEqualTo(favoriteCount);

        // verify
        verify(mockFrontModelJpaApiService, times(1)).favoriteModel(frontModelEntity);
        verify(mockFrontModelJpaApiService, atLeastOnce()).favoriteModel(frontModelEntity);
        verifyNoMoreInteractions(mockFrontModelJpaApiService);

        InOrder inOrder = inOrder(mockFrontModelJpaApiService);
        inOrder.verify(mockFrontModelJpaApiService).favoriteModel(frontModelEntity);
    }

    @Test
    @DisplayName("모델 좋아요 BDD 테스트")
    void 모델좋아요BDD테스트() {
        // given
        em.persist(frontModelEntity);
        frontModelDTO = INSTANCE.toDto(frontModelEntity);

        Integer favoriteCount = frontModelJpaApiService.favoriteModel(frontModelEntity);

        // when
        given(mockFrontModelJpaApiService.favoriteModel(frontModelEntity)).willReturn(favoriteCount);
        Integer newFavoriteCount = mockFrontModelJpaApiService.favoriteModel(frontModelEntity);

        // then
        assertThat(newFavoriteCount).isEqualTo(favoriteCount);

        // verify
        then(mockFrontModelJpaApiService).should(times(1)).favoriteModel(frontModelEntity);
        then(mockFrontModelJpaApiService).should(atLeastOnce()).favoriteModel(frontModelEntity);
        then(mockFrontModelJpaApiService).shouldHaveNoMoreInteractions();
    }
}
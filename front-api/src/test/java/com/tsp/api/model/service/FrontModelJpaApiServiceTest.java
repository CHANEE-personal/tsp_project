package com.tsp.api.model.service;

import com.tsp.api.FrontCommonServiceTest;
import com.tsp.api.model.domain.FrontModelDTO;
import com.tsp.api.model.domain.recommend.FrontRecommendEntity;
import com.tsp.api.model.domain.search.FrontSearchDTO;
import com.tsp.api.model.domain.search.FrontSearchEntity;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
class FrontModelJpaApiServiceTest extends FrontCommonServiceTest {
    private final FrontModelJpaApiService frontModelJpaApiService;
    @Mock private FrontModelJpaRepository frontModelJpaRepository;
    @Mock private FrontModelJpaQueryRepository frontModelJpaQueryRepository;
    @InjectMocks private FrontModelJpaApiService mockFrontModelJpaApiService;

    private final EntityManager em;

    @Test
    @DisplayName("모델 리스트 조회 예외 테스트")
    void 모델리스트조회예외테스트() {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", -1);
        PageRequest pageRequest = PageRequest.of(0, 3);

        // then
        assertThatThrownBy(() -> frontModelJpaApiService.findModelList(modelMap, pageRequest))
                .isInstanceOf(TspException.class);
    }

    @Test
    @DisplayName("모델리스트조회Mockito테스트")
    void 모델리스트조회Mockito테스트() {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", "1");
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<FrontModelDTO> returnModelList = new ArrayList<>();

        // 남성
        returnModelList.add(FrontModelDTO.builder().idx(1L).categoryCd(1).modelKorName("남성모델").modelAgency(frontAgencyDTO).modelEngName("menModel").build());
        // 여성
        returnModelList.add(FrontModelDTO.builder().idx(2L).categoryCd(2).modelKorName("여성모델").modelAgency(frontAgencyDTO).modelEngName("womenModel").build());
        // 시니어
        returnModelList.add(FrontModelDTO.builder().idx(3L).categoryCd(3).modelKorName("시니어모델").modelAgency(frontAgencyDTO).modelEngName("seniorModel").build());

        Page<FrontModelDTO> resultPage = new PageImpl<>(returnModelList, pageRequest, returnModelList.size());

        // when
        when(frontModelJpaQueryRepository.findModelList(modelMap, pageRequest)).thenReturn(resultPage);
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
        verify(frontModelJpaQueryRepository, times(1)).findModelList(modelMap, pageRequest);
        verify(frontModelJpaQueryRepository, atLeastOnce()).findModelList(modelMap, pageRequest);
        verifyNoMoreInteractions(frontModelJpaQueryRepository);

        InOrder inOrder = inOrder(frontModelJpaQueryRepository);
        inOrder.verify(frontModelJpaQueryRepository).findModelList(modelMap, pageRequest);
    }

    @Test
    @DisplayName("모델리스트조회BDD테스트")
    void 모델리스트조회BDD테스트() {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", "1");
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<FrontModelDTO> returnModelList = new ArrayList<>();

        // 남성
        returnModelList.add(FrontModelDTO.builder().idx(1L).categoryCd(1).modelKorName("남성모델").modelAgency(frontAgencyDTO).modelEngName("menModel").build());
        // 여성
        returnModelList.add(FrontModelDTO.builder().idx(2L).categoryCd(2).modelKorName("여성모델").modelAgency(frontAgencyDTO).modelEngName("womenModel").build());
        // 시니어
        returnModelList.add(FrontModelDTO.builder().idx(3L).categoryCd(3).modelKorName("시니어모델").modelAgency(frontAgencyDTO).modelEngName("seniorModel").build());

        Page<FrontModelDTO> resultPage = new PageImpl<>(returnModelList, pageRequest, returnModelList.size());
        // when
        given(frontModelJpaQueryRepository.findModelList(modelMap, pageRequest)).willReturn(resultPage);
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
        then(frontModelJpaQueryRepository).should(times(1)).findModelList(modelMap, pageRequest);
        then(frontModelJpaQueryRepository).should(atLeastOnce()).findModelList(modelMap, pageRequest);
        then(frontModelJpaQueryRepository).shouldHaveNoMoreInteractions();
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
        when(frontModelJpaQueryRepository.findMainModelList()).thenReturn(returnModelList);
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
        verify(frontModelJpaQueryRepository, times(1)).findMainModelList();
        verify(frontModelJpaQueryRepository, atLeastOnce()).findMainModelList();
        verifyNoMoreInteractions(frontModelJpaQueryRepository);

        InOrder inOrder = inOrder(frontModelJpaQueryRepository);
        inOrder.verify(frontModelJpaQueryRepository).findMainModelList();
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
        given(frontModelJpaQueryRepository.findMainModelList()).willReturn(returnModelList);
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
        then(frontModelJpaQueryRepository).should(times(1)).findMainModelList();
        then(frontModelJpaQueryRepository).should(atLeastOnce()).findMainModelList();
        then(frontModelJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델상세조회Mockito테스트")
    void 모델상세조회Mockito테스트() {
        // when
        when(frontModelJpaRepository.findByIdx(frontModelEntity.getIdx())).thenReturn(Optional.ofNullable(frontModelEntity));
        FrontModelDTO modelInfo = mockFrontModelJpaApiService.findOneModel(frontModelEntity.getIdx());

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(frontModelEntity.getIdx());
        assertThat(modelInfo.getAgencyIdx()).isEqualTo(frontModelEntity.getFrontAgencyEntity().getIdx());
        assertThat(modelInfo.getCategoryCd()).isEqualTo(frontModelEntity.getNewModelCodeJpaDTO().getCategoryCd());
        assertThat(modelInfo.getModelKorName()).isEqualTo(frontModelEntity.getModelKorName());
        assertThat(modelInfo.getModelEngName()).isEqualTo(frontModelEntity.getModelEngName());

        // verify
        verify(frontModelJpaRepository, times(1)).findByIdx(frontModelEntity.getIdx());
        verify(frontModelJpaRepository, atLeastOnce()).findByIdx(frontModelEntity.getIdx());
        verifyNoMoreInteractions(frontModelJpaRepository);

        InOrder inOrder = inOrder(frontModelJpaRepository);
        inOrder.verify(frontModelJpaRepository).findByIdx(frontModelEntity.getIdx());
    }

    @Test
    @DisplayName("모델상세조회BDD테스트")
    void 모델상세조회BDD테스트() {
        // when
        given(frontModelJpaRepository.findByIdx(frontModelEntity.getIdx())).willReturn(Optional.ofNullable(frontModelEntity));
        FrontModelDTO modelInfo = mockFrontModelJpaApiService.findOneModel(frontModelEntity.getIdx());

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(frontModelEntity.getIdx());
        assertThat(modelInfo.getAgencyIdx()).isEqualTo(frontModelEntity.getFrontAgencyEntity().getIdx());
        assertThat(modelInfo.getCategoryCd()).isEqualTo(frontModelEntity.getNewModelCodeJpaDTO().getCategoryCd());
        assertThat(modelInfo.getModelKorName()).isEqualTo(frontModelEntity.getModelKorName());
        assertThat(modelInfo.getModelEngName()).isEqualTo(frontModelEntity.getModelEngName());

        // verify
        then(frontModelJpaRepository).should(times(1)).findByIdx(frontModelEntity.getIdx());
        then(frontModelJpaRepository).should(atLeastOnce()).findByIdx(frontModelEntity.getIdx());
        then(frontModelJpaRepository).shouldHaveNoMoreInteractions();
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
        // when
        when(frontModelJpaRepository.findById(frontModelEntity.getIdx())).thenReturn(Optional.ofNullable(frontModelEntity));
        Integer newFavoriteCount = mockFrontModelJpaApiService.favoriteModel(frontModelEntity.getIdx());

        // then
        assertThat(newFavoriteCount).isEqualTo(frontModelEntity.getModelFavoriteCount());

        // verify
        verify(frontModelJpaRepository, times(2)).findById(frontModelEntity.getIdx());
        verify(frontModelJpaRepository, atLeastOnce()).findById(frontModelEntity.getIdx());
    }

    @Test
    @DisplayName("모델 좋아요 BDD 테스트")
    void 모델좋아요BDD테스트() {
        // when
        given(frontModelJpaRepository.findById(frontModelEntity.getIdx())).willReturn(Optional.ofNullable(frontModelEntity));
        Integer newFavoriteCount = mockFrontModelJpaApiService.favoriteModel(frontModelEntity.getIdx());

        // then
        assertThat(newFavoriteCount).isEqualTo(frontModelEntity.getModelFavoriteCount());

        // verify
        then(frontModelJpaRepository).should(times(2)).findById(frontModelEntity.getIdx());
        then(frontModelJpaRepository).should(atLeastOnce()).findById(frontModelEntity.getIdx());
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
        when(frontModelJpaQueryRepository.findModelList(newModelMap, pageRequest)).thenReturn(resultPage);
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
        verify(frontModelJpaQueryRepository, times(1)).findModelList(newModelMap, pageRequest);
        verify(frontModelJpaQueryRepository, atLeastOnce()).findModelList(newModelMap, pageRequest);
        verifyNoMoreInteractions(frontModelJpaQueryRepository);

        InOrder inOrder = inOrder(frontModelJpaQueryRepository);
        inOrder.verify(frontModelJpaQueryRepository).findModelList(newModelMap, pageRequest);
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
        given(frontModelJpaQueryRepository.findModelList(newModelMap, pageRequest)).willReturn(resultPage);
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
        then(frontModelJpaQueryRepository).should(times(1)).findModelList(newModelMap, pageRequest);
        then(frontModelJpaQueryRepository).should(atLeastOnce()).findModelList(newModelMap, pageRequest);
        then(frontModelJpaQueryRepository).shouldHaveNoMoreInteractions();
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

        assertThat(frontModelJpaApiService.findRecommendList(PageRequest.of(0, 10))).isNotEmpty();
    }

    @Test
    @DisplayName("검색어 랭킹 리스트 조회 테스트")
    void 검색어랭킹리스트조회테스트() {
        em.persist(FrontSearchEntity.builder().searchKeyword("모델1").build());
        em.persist(FrontSearchEntity.builder().searchKeyword("모델1").build());
        em.persist(FrontSearchEntity.builder().searchKeyword("모델1").build());
        em.persist(FrontSearchEntity.builder().searchKeyword("모델2").build());
        Page<FrontSearchDTO> searchResult = frontModelJpaApiService.rankingKeywordList(PageRequest.of(0, 10));
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

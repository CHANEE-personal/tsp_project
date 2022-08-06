package com.tsp.new_tsp_front.api.model.service;

import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import com.tsp.new_tsp_front.api.model.service.impl.FrontModelJpaRepository;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.api.model.domain.FrontModelDTO.*;
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
    @Mock private FrontModelJpaRepository frontModelJpaRepository;
    @InjectMocks private FrontModelJpaApiService mockFrontModelJpaApiService;

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
        returnModelList.add(builder().idx(1).categoryCd(1).modelKorName("남성모델").modelEngName("menModel").build());
        // 여성
        returnModelList.add(builder().idx(2).categoryCd(2).modelKorName("여성모델").modelEngName("womenModel").build());
        // 시니어
        returnModelList.add(builder().idx(3).categoryCd(3).modelKorName("시니어모델").modelEngName("seniorModel").build());

        // when
        when(frontModelJpaRepository.getModelList(modelMap)).thenReturn(returnModelList);
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

        assertThat(modelList.get(1).getIdx()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(modelList.get(1).getCategoryCd()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(modelList.get(1).getModelKorName()).isEqualTo(returnModelList.get(1).getModelKorName());
        assertThat(modelList.get(1).getModelEngName()).isEqualTo(returnModelList.get(1).getModelEngName());

        assertThat(modelList.get(2).getIdx()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(modelList.get(2).getCategoryCd()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(modelList.get(2).getModelKorName()).isEqualTo(returnModelList.get(2).getModelKorName());
        assertThat(modelList.get(2).getModelEngName()).isEqualTo(returnModelList.get(2).getModelEngName());

        // verify
        verify(frontModelJpaRepository, times(1)).getModelList(modelMap);
        verify(frontModelJpaRepository, atLeastOnce()).getModelList(modelMap);
        verifyNoMoreInteractions(frontModelJpaRepository);
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
        returnModelList.add(builder().idx(1).categoryCd(1).modelKorName("남성모델").modelEngName("menModel").build());
        // 여성
        returnModelList.add(builder().idx(2).categoryCd(2).modelKorName("여성모델").modelEngName("womenModel").build());
        // 시니어
        returnModelList.add(builder().idx(3).categoryCd(3).modelKorName("시니어모델").modelEngName("seniorModel").build());

        // when
        given(frontModelJpaRepository.getModelList(modelMap)).willReturn(returnModelList);
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

        assertThat(modelList.get(1).getIdx()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(modelList.get(1).getCategoryCd()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(modelList.get(1).getModelKorName()).isEqualTo(returnModelList.get(1).getModelKorName());
        assertThat(modelList.get(1).getModelEngName()).isEqualTo(returnModelList.get(1).getModelEngName());

        assertThat(modelList.get(2).getIdx()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(modelList.get(2).getCategoryCd()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(modelList.get(2).getModelKorName()).isEqualTo(returnModelList.get(2).getModelKorName());
        assertThat(modelList.get(2).getModelEngName()).isEqualTo(returnModelList.get(2).getModelEngName());

        // verify
        then(frontModelJpaRepository).should(times(1)).getModelList(modelMap);
        then(frontModelJpaRepository).should(atLeastOnce()).getModelList(modelMap);
        then(frontModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델배너리스트조회Mockito테스트")
    void 모델배너리스트조회Mockito테스트() {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", "1");

        List<FrontModelDTO> returnModelList = new ArrayList<>();

        // 남성
        returnModelList.add(builder().idx(1).categoryCd(1).modelKorName("남성모델").modelEngName("menModel").modelMainYn("Y").build());
        // 여성
        returnModelList.add(builder().idx(2).categoryCd(2).modelKorName("여성모델").modelEngName("womenModel").modelMainYn("Y").build());
        // 시니어
        returnModelList.add(builder().idx(3).categoryCd(3).modelKorName("시니어모델").modelEngName("seniorModel").modelMainYn("Y").build());

        // when
        when(frontModelJpaRepository.getMainModelList()).thenReturn(returnModelList);
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
        verify(frontModelJpaRepository, times(1)).getMainModelList();
        verify(frontModelJpaRepository, atLeastOnce()).getMainModelList();
        verifyNoMoreInteractions(frontModelJpaRepository);
    }

    @Test
    @DisplayName("모델배너리스트조회BDD테스트")
    void 모델배너리스트조회BDD테스트() {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", "1");

        List<FrontModelDTO> returnModelList = new ArrayList<>();

        // 남성
        returnModelList.add(builder().idx(1).categoryCd(1).modelKorName("남성모델").modelEngName("menModel").modelMainYn("Y").build());
        // 여성
        returnModelList.add(builder().idx(2).categoryCd(2).modelKorName("여성모델").modelEngName("womenModel").modelMainYn("Y").build());
        // 시니어
        returnModelList.add(builder().idx(3).categoryCd(3).modelKorName("시니어모델").modelEngName("seniorModel").modelMainYn("Y").build());

        // when
        given(frontModelJpaRepository.getMainModelList()).willReturn(returnModelList);
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
        then(frontModelJpaRepository).should(times(1)).getMainModelList();
        then(frontModelJpaRepository).should(atLeastOnce()).getMainModelList();
        then(frontModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델상세조회Mockito테스트")
    void 모델상세조회Mockito테스트() {
        // given
        FrontModelEntity frontModelEntity = FrontModelEntity.builder().idx(1).categoryCd(1).build();
        FrontModelDTO frontModelDTO = builder().idx(1).categoryCd(1).modelKorName("조찬희").modelEngName("chochanhee").build();

        // when
        when(frontModelJpaRepository.getModelInfo(frontModelEntity)).thenReturn(frontModelDTO);
        FrontModelDTO modelInfo = mockFrontModelJpaApiService.getModelInfo(frontModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(frontModelDTO.getIdx());
        assertThat(modelInfo.getCategoryCd()).isEqualTo(frontModelDTO.getCategoryCd());
        assertThat(modelInfo.getModelKorName()).isEqualTo(frontModelDTO.getModelKorName());
        assertThat(modelInfo.getModelEngName()).isEqualTo(frontModelDTO.getModelEngName());

        // verify
        verify(frontModelJpaRepository, times(1)).getModelInfo(frontModelEntity);
        verify(frontModelJpaRepository, atLeastOnce()).getModelInfo(frontModelEntity);
        verifyNoMoreInteractions(frontModelJpaRepository);
    }

    @Test
    @DisplayName("모델상세조회BDD테스트")
    void 모델상세조회BDD테스트() {
        // given
        FrontModelEntity frontModelEntity = FrontModelEntity.builder().idx(1).categoryCd(1).build();
        FrontModelDTO frontModelDTO = builder().idx(1).categoryCd(1).modelKorName("조찬희").modelEngName("chochanhee").build();

        // when
        given(frontModelJpaRepository.getModelInfo(frontModelEntity)).willReturn(frontModelDTO);
        FrontModelDTO modelInfo = mockFrontModelJpaApiService.getModelInfo(frontModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(frontModelDTO.getIdx());
        assertThat(modelInfo.getCategoryCd()).isEqualTo(frontModelDTO.getCategoryCd());
        assertThat(modelInfo.getModelKorName()).isEqualTo(frontModelDTO.getModelKorName());
        assertThat(modelInfo.getModelEngName()).isEqualTo(frontModelDTO.getModelEngName());

        // verify
        then(frontModelJpaRepository).should(times(1)).getModelInfo(frontModelEntity);
        then(frontModelJpaRepository).should(atLeastOnce()).getModelInfo(frontModelEntity);
        then(frontModelJpaRepository).shouldHaveNoMoreInteractions();
    }
}
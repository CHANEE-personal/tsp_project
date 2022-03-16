package com.tsp.new_tsp_front.api.model.service;

import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import com.tsp.new_tsp_front.api.model.service.impl.FrontModelJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_front.api.model.domain.FrontModelDTO.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@Transactional
@ExtendWith(MockitoExtension.class)
@DisplayName("모델 Service Test")
class FrontModelJpaApiServiceTest {

    @Mock
    private FrontModelJpaRepository frontModelJpaRepository;

    @InjectMocks
    private FrontModelJpaApiService frontModelJpaApiService;

    @Test
    public void 모델리스트조회테스트() throws Exception {
        ConcurrentHashMap<String, Object> modelMap = new ConcurrentHashMap<>();
        modelMap.put("categoryCd", "1");
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        List<FrontModelDTO> returnModelList = new ArrayList<>();

        returnModelList.add(builder().idx(1).categoryCd(1).modelKorName("조찬희").modelEngName("chochanhee").build());

        given(frontModelJpaRepository.getModelList(modelMap)).willReturn(returnModelList);

        // when
        List<FrontModelDTO> modelList = frontModelJpaApiService.getModelList(modelMap);

        assertThat(modelList.get(0).getIdx()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(modelList.get(0).getCategoryCd()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(modelList.get(0).getModelKorName()).isEqualTo(returnModelList.get(0).getModelKorName());
        assertThat(modelList.get(0).getModelEngName()).isEqualTo(returnModelList.get(0).getModelEngName());
    }

    @Test
    public void 모델상세조회테스트() throws Exception {
        // given
        FrontModelEntity frontModelEntity = FrontModelEntity.builder().idx(1).categoryCd(1).build();
        FrontModelDTO frontModelDTO = builder().idx(1).categoryCd(1).modelKorName("조찬희").modelEngName("chochanhee").build();
        given(frontModelJpaRepository.getModelInfo(frontModelEntity)).willReturn(frontModelDTO);

        // when
        FrontModelDTO modelInfo = frontModelJpaApiService.getModelInfo(frontModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(frontModelDTO.getIdx());
        assertThat(modelInfo.getCategoryCd()).isEqualTo(frontModelDTO.getCategoryCd());
        assertThat(modelInfo.getModelKorName()).isEqualTo(frontModelDTO.getModelKorName());
        assertThat(modelInfo.getModelEngName()).isEqualTo(frontModelDTO.getModelEngName());
    }
}
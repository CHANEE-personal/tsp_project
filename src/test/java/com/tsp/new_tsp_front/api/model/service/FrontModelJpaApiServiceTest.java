package com.tsp.new_tsp_front.api.model.service;

import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.service.impl.FrontModelJpaRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_front.api.model.domain.FrontModelDTO.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("모델 Service Test")
class FrontModelJpaApiServiceTest {

    @Mock
    FrontModelJpaRepository frontModelJpaRepository;

    @InjectMocks
    FrontModelJpaApiService frontModelJpaApiService;

    @Test
    public void 모델리스트조회테스트() throws Exception {
        ConcurrentHashMap<String, Object> modelMap = new ConcurrentHashMap<>();
        modelMap.put("categoryCd", "1");
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        List<FrontModelDTO> returnModelList = new ArrayList<>();

        returnModelList.add(builder().idx(1).categoryCd(1).modelKorName("조찬희").build());

        doReturn(returnModelList)
                .when(frontModelJpaRepository).getModelList(modelMap);

        // when
        List<FrontModelDTO> modelList = frontModelJpaApiService.getModelList(modelMap);

        assertThat(modelList.size()).isEqualTo(1);
    }
}
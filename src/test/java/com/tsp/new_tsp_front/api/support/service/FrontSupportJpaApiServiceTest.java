package com.tsp.new_tsp_front.api.support.service;

import com.tsp.new_tsp_front.api.support.domain.FrontSupportEntity;
import com.tsp.new_tsp_front.api.support.service.impl.FrontSupportJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import static com.tsp.new_tsp_front.api.support.domain.FrontSupportEntity.builder;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("지원모델 Service Test")
class FrontSupportJpaApiServiceTest {
    @Mock
    private FrontSupportJpaRepository frontSupportJpaRepository;

    @InjectMocks
    private FrontSupportJpaApiService frontSupportJpaApiService;

    @Test
    @DisplayName("모델 지원하기 테스트")
    void 모델지원하기테스트() {
        FrontSupportEntity frontSupportEntity = builder()
                .supportName("조찬희")
                .supportHeight(170)
                .supportMessage("조찬희")
                .supportPhone("010-9466-2702")
                .supportSize3("31-24-31")
                .build();

        frontSupportJpaApiService.insertSupportModel(frontSupportEntity);
    }
}
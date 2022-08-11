package com.tsp.new_tsp_front.api.support.service;

import com.tsp.new_tsp_front.api.support.domain.FrontSupportEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("지원모델 Service Test")
class FrontSupportJpaApiServiceTest {
    @InjectMocks private FrontSupportJpaApiService frontSupportJpaApiService;

    @Test
    @DisplayName("모델 지원하기 테스트")
    void 모델지원하기테스트() {
        // given
        FrontSupportEntity frontSupportEntity = FrontSupportEntity.builder()
                .supportName("조찬희")
                .supportHeight(170)
                .supportMessage("조찬희")
                .supportPhone("010-9466-2702")
                .supportSize3("31-24-31")
                .build();

        frontSupportJpaApiService.insertSupportModel(frontSupportEntity);

        // then
        assertThat(frontSupportEntity.getSupportName()).isEqualTo("조찬희");
        assertThat(frontSupportEntity.getSupportHeight()).isEqualTo(170);
        assertThat(frontSupportEntity.getSupportMessage()).isEqualTo("조찬희");
        assertThat(frontSupportEntity.getSupportPhone()).isEqualTo("010-9466-2702");
        assertThat(frontSupportEntity.getSupportSize3()).isEqualTo("31-24-31");
    }
}
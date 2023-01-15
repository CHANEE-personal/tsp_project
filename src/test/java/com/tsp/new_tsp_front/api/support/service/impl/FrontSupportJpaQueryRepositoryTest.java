package com.tsp.new_tsp_front.api.support.service.impl;

import com.tsp.new_tsp_front.api.support.domain.FrontSupportEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-local.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("지원 모델 Repository Test")
class FrontSupportJpaQueryRepositoryTest {
    private final FrontSupportJpaQueryRepository frontSupportJpaQueryRepository;
    private FrontSupportEntity frontSupportEntity;

    private void createSupportModel() {
        frontSupportEntity = FrontSupportEntity.builder()
                .supportName("조찬희")
                .supportMessage("조찬희")
                .supportHeight(170)
                .supportPhone("010-9466-2702")
                .supportSize3("31-24-31")
                .supportInstagram("https://instagram.com")
                .visible("Y")
                .build();
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createSupportModel();
    }

    @Test
    @DisplayName("모델 지원하기 테스트")
    void 모델지원하기테스트() {
        assertThat(frontSupportJpaQueryRepository.insertSupportModel(frontSupportEntity)).isNotNull();
    }
}
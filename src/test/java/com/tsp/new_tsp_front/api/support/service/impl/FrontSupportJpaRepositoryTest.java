package com.tsp.new_tsp_front.api.support.service.impl;

import com.tsp.new_tsp_front.api.support.domain.FrontSupportDTO;
import com.tsp.new_tsp_front.api.support.domain.FrontSupportEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import static com.tsp.new_tsp_front.api.support.domain.FrontSupportEntity.*;
import static com.tsp.new_tsp_front.api.support.mapper.SupportMapper.INSTANCE;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("지원 모델 Repository Test")
class FrontSupportJpaRepositoryTest {
    FrontSupportEntity frontSupportEntity;
    FrontSupportDTO frontSupportDTO;
    @Autowired private FrontSupportJpaRepository frontSupportJpaRepository;

    private void createSupportModel() {
        frontSupportEntity = builder()
                .supportName("조찬희")
                .supportMessage("조찬희")
                .supportHeight(170)
                .supportPhone("010-9466-2702")
                .supportSize3("31-24-31")
                .supportInstagram("https://instagram.com")
                .build();

        frontSupportDTO = INSTANCE.toDto(frontSupportEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createSupportModel();
    }

    @Test
    @DisplayName("모델 지원하기 테스트")
    void 모델지원하기테스트() {
        frontSupportJpaRepository.insertSupportModel(frontSupportEntity);
    }
}
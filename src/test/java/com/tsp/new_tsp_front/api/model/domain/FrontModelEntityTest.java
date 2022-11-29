package com.tsp.new_tsp_front.api.model.domain;

import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FrontModelEntityTest {

    private FrontModelEntity frontModelEntity;
    private FrontAgencyEntity frontAgencyEntity;

    @BeforeEach
    void setUp() {

        frontAgencyEntity = FrontAgencyEntity.builder()
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

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
                .newYn("N")
                .frontAgencyEntity(frontAgencyEntity)
                .modelViewCount(1)
                .visible("Y")
                .build();
    }

    @Test
    @DisplayName("모델 조회 수 증가 테스트")
    void updateViewCount() {
        int beforeViewCount = frontModelEntity.getModelViewCount();
        frontModelEntity.updateViewCount();

        assertThat(frontModelEntity.getModelViewCount()).isEqualTo(beforeViewCount + 1);
    }

    @Test
    @DisplayName("모델 좋아요 수 증가 테스트")
    void updateFavoriteCount() {
        int beforeFavoriteCount = frontModelEntity.getModelFavoriteCount();
        frontModelEntity.updateFavoriteCount();

        assertThat(frontModelEntity.getModelFavoriteCount()).isEqualTo(beforeFavoriteCount + 1);
    }

}
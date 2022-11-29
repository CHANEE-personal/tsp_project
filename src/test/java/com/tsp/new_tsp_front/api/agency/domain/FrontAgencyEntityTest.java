package com.tsp.new_tsp_front.api.agency.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FrontAgencyEntityTest {

    private FrontAgencyEntity frontAgencyEntity;

    @BeforeEach
    void setUp() {
        frontAgencyEntity = FrontAgencyEntity.builder()
                .agencyName("소속사")
                .agencyDescription("소속사")
                .favoriteCount(1)
                .visible("Y").build();
    }

    @Test
    @DisplayName("소속사 좋아요 테스트")
    void updateFavoriteCount() {
        int beforeFavoriteCount = frontAgencyEntity.getFavoriteCount();
        frontAgencyEntity.updateFavoriteCount();

        assertThat(frontAgencyEntity.getFavoriteCount()).isEqualTo(beforeFavoriteCount + 1);
    }

}
package com.tsp.api.festival.service;

import com.tsp.api.festival.domain.FrontFestivalDTO;
import com.tsp.api.festival.domain.FrontFestivalEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("행사 Service Test")
class FrontFestivalJpaServiceTest {
    @Mock private final FrontFestivalJpaService mockFrontFestivalJpaService;
    private final FrontFestivalJpaService frontFestivalJpaService;

    private FrontFestivalEntity frontFestivalEntity;
    private FrontFestivalDTO frontFestivalDTO;

    private final EntityManager em;

    private void createFestival() {
        // 등록
        LocalDateTime dateTime = LocalDateTime.now();

        frontFestivalEntity = FrontFestivalEntity.builder()
                .festivalTitle("축제 제목")
                .festivalDescription("축제 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        em.persist(frontFestivalEntity);

        frontFestivalDTO = FrontFestivalEntity.toDto(frontFestivalEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createFestival();
    }

    @Test
    @DisplayName("행사 리스트 조회 테스트")
    void 행사리스트조회테스트() {
        // then
        assertThat(frontFestivalJpaService.findFestivalList(frontFestivalEntity, PageRequest.of(1, 10))).isNotEmpty();
    }

    @Test
    @DisplayName("행사 리스트 갯수 그룹 조회")
    void 행사리스트갯수그룹조회() {
        // 등록
        LocalDateTime dateTime = LocalDateTime.now();

        FrontFestivalEntity frontFestivalEntity1 = FrontFestivalEntity.builder()
                .festivalTitle("축제 제목")
                .festivalDescription("축제 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        em.persist(frontFestivalEntity1);

        FrontFestivalEntity frontFestivalEntity2 = FrontFestivalEntity.builder()
                .festivalTitle("축제 제목")
                .festivalDescription("축제 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth()+1)
                .festivalTime(dateTime)
                .build();

        em.persist(frontFestivalEntity2);

        em.flush();
        em.clear();

        assertThat(frontFestivalJpaService.findFestivalGroup(dateTime.getMonthValue())).isNotEmpty();
    }

    @Test
    @DisplayName("행사 상세 조회 테스트")
    void 행사상세조회테스트() {
        assertThat(frontFestivalJpaService.findOneFestival(frontFestivalDTO.getIdx()).getFestivalTitle()).isEqualTo("축제 제목");
    }
}

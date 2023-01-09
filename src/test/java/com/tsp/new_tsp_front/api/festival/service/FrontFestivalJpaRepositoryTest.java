package com.tsp.new_tsp_front.api.festival.service;

import com.tsp.new_tsp_front.api.festival.domain.FrontFestivalDTO;
import com.tsp.new_tsp_front.api.festival.domain.FrontFestivalEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-local.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("행사 Repository Test")
class FrontFestivalJpaRepositoryTest {

    @Mock
    private final FrontFestivalJpaRepository mockFrontFestivalJpaRepository;
    private final FrontFestivalJpaRepository frontFestivalJpaRepository;

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
        // given
        Map<String, Object> festivalMap = new HashMap<>();
        festivalMap.put("jpaStartPage", 0);
        festivalMap.put("size", 3);

        // then
        assertThat(frontFestivalJpaRepository.findFestivalList(festivalMap)).isNotEmpty();
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

        assertThat(frontFestivalJpaRepository.findFestivalGroup(dateTime.getMonthValue())).isNotEmpty();
    }

    @Test
    @DisplayName("행사 상세 조회 테스트")
    void 행사상세조회테스트() {
        assertThat(frontFestivalJpaRepository.findOneFestival(frontFestivalDTO.getIdx()).getFestivalTitle()).isEqualTo("축제 제목");
    }
}
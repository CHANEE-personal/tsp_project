package com.tsp.new_tsp_front.api.model.schedule.service;

import com.tsp.new_tsp_front.api.model.domain.schedule.FrontScheduleDTO;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("모델 스케줄 Service Test")
class FrontScheduleJpaApiServiceTest {
    @Mock private FrontScheduleJpaApiService mockFrontScheduleJpaApiService;
    private final FrontScheduleJpaApiService frontScheduleJpaApiService;

    @Test
    @DisplayName("모델 스케줄 리스트 조회 테스트")
    void 모델스케줄리스트조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        scheduleMap.put("searchKeyword", "김예영");
        scheduleMap.put("jpaStartPage", 0);
        scheduleMap.put("size", 100);

        // then
        assertThat(frontScheduleJpaApiService.findModelScheduleList(scheduleMap)).isNotEmpty();

        Map<String, Object> lastMonthScheduleMap = new HashMap<>();
        lastMonthScheduleMap.put("searchStartTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 1, 0, 0, 0, 0));
        lastMonthScheduleMap.put("searchEndTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 30, 23, 59, 59));
        lastMonthScheduleMap.put("jpaStartPage", 0);
        lastMonthScheduleMap.put("size", 100);

        // then
        assertThat(frontScheduleJpaApiService.findModelScheduleList(lastMonthScheduleMap)).isEmpty();

        Map<String, Object> currentScheduleMap = new HashMap<>();
        currentScheduleMap.put("searchStartTime", of(now().getYear(), LocalDate.now().getMonth(), 1, 0, 0, 0, 0));
        currentScheduleMap.put("searchEndTime", of(now().getYear(), LocalDate.now().getMonth(), 30, 23, 59, 59));
        currentScheduleMap.put("jpaStartPage", 0);
        currentScheduleMap.put("size", 100);

        // then
        assertThat(frontScheduleJpaApiService.findModelScheduleList(currentScheduleMap)).isNotEmpty();
    }

    @Test
    @DisplayName("모델 스케줄 Mockito 조회 테스트")
    void 모델스케줄Mockito조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        scheduleMap.put("jpaStartPage", 1);
        scheduleMap.put("size", 3);

        List<FrontScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(FrontScheduleDTO.builder().idx(1).modelIdx(1)
                .modelSchedule("스케줄 테스트").modelScheduleTime(LocalDateTime.now()).build());

        // when
        when(mockFrontScheduleJpaApiService.findScheduleList(scheduleMap)).thenReturn(scheduleList);
        List<FrontScheduleDTO> newScheduleList = mockFrontScheduleJpaApiService.findScheduleList(scheduleMap);

        // then
        assertThat(newScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(newScheduleList.get(0).getModelIdx()).isEqualTo(scheduleList.get(0).getModelIdx());
        assertThat(newScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());
        assertThat(newScheduleList.get(0).getModelScheduleTime()).isEqualTo(scheduleList.get(0).getModelScheduleTime());

        // verify
        verify(mockFrontScheduleJpaApiService, times(1)).findScheduleList(scheduleMap);
        verify(mockFrontScheduleJpaApiService, atLeastOnce()).findScheduleList(scheduleMap);
        verifyNoMoreInteractions(mockFrontScheduleJpaApiService);

        InOrder inOrder = inOrder(mockFrontScheduleJpaApiService);
        inOrder.verify(mockFrontScheduleJpaApiService).findScheduleList(scheduleMap);
    }

    @Test
    @DisplayName("모델 스케줄 BDD 조회 테스트")
    void 모델스케줄BDD조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        scheduleMap.put("jpaStartPage", 1);
        scheduleMap.put("size", 3);

        List<FrontScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(FrontScheduleDTO.builder().idx(1).modelIdx(1)
                .modelSchedule("스케줄 테스트").modelScheduleTime(LocalDateTime.now()).build());

        // when
        given(mockFrontScheduleJpaApiService.findScheduleList(scheduleMap)).willReturn(scheduleList);
        List<FrontScheduleDTO> newScheduleList = mockFrontScheduleJpaApiService.findScheduleList(scheduleMap);

        // then
        assertThat(newScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(newScheduleList.get(0).getModelIdx()).isEqualTo(scheduleList.get(0).getModelIdx());
        assertThat(newScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());
        assertThat(newScheduleList.get(0).getModelScheduleTime()).isEqualTo(scheduleList.get(0).getModelScheduleTime());

        // verify
        then(mockFrontScheduleJpaApiService).should(times(1)).findScheduleList(scheduleMap);
        then(mockFrontScheduleJpaApiService).should(atLeastOnce()).findScheduleList(scheduleMap);
        then(mockFrontScheduleJpaApiService).shouldHaveNoMoreInteractions();
    }
}
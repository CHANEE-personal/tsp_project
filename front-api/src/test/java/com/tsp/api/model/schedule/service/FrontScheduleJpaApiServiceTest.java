package com.tsp.api.model.schedule.service;

import com.tsp.api.model.domain.schedule.FrontScheduleDTO;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(FrontScheduleDTO.builder().idx(1L).modelIdx(1L)
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());

        Page<FrontScheduleDTO> resultSchedule = new PageImpl<>(scheduleList, pageRequest, scheduleList.size());
        // when
        when(mockFrontScheduleJpaApiService.findScheduleList(scheduleMap, pageRequest)).thenReturn(resultSchedule);
        Page<FrontScheduleDTO> newScheduleList = mockFrontScheduleJpaApiService.findScheduleList(scheduleMap, pageRequest);
        List<FrontScheduleDTO> findScheduleList = newScheduleList.stream().collect(Collectors.toList());

        // then
        assertThat(findScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(findScheduleList.get(0).getModelIdx()).isEqualTo(scheduleList.get(0).getModelIdx());
        assertThat(findScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());
        assertThat(findScheduleList.get(0).getModelScheduleTime()).isEqualTo(scheduleList.get(0).getModelScheduleTime());

        // verify
        verify(mockFrontScheduleJpaApiService, times(1)).findScheduleList(scheduleMap, pageRequest);
        verify(mockFrontScheduleJpaApiService, atLeastOnce()).findScheduleList(scheduleMap, pageRequest);
        verifyNoMoreInteractions(mockFrontScheduleJpaApiService);

        InOrder inOrder = inOrder(mockFrontScheduleJpaApiService);
        inOrder.verify(mockFrontScheduleJpaApiService).findScheduleList(scheduleMap, pageRequest);
    }

    @Test
    @DisplayName("모델 스케줄 BDD 조회 테스트")
    void 모델스케줄BDD조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(FrontScheduleDTO.builder().idx(1L).modelIdx(1L)
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());

        Page<FrontScheduleDTO> resultSchedule = new PageImpl<>(scheduleList, pageRequest, scheduleList.size());
        // when
        given(mockFrontScheduleJpaApiService.findScheduleList(scheduleMap, pageRequest)).willReturn(resultSchedule);
        Page<FrontScheduleDTO> newScheduleList = mockFrontScheduleJpaApiService.findScheduleList(scheduleMap, pageRequest);
        List<FrontScheduleDTO> findScheduleList = newScheduleList.stream().collect(Collectors.toList());

        // then
        assertThat(findScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(findScheduleList.get(0).getModelIdx()).isEqualTo(scheduleList.get(0).getModelIdx());
        assertThat(findScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());
        assertThat(findScheduleList.get(0).getModelScheduleTime()).isEqualTo(scheduleList.get(0).getModelScheduleTime());

        // verify
        then(mockFrontScheduleJpaApiService).should(times(1)).findScheduleList(scheduleMap, pageRequest);
        then(mockFrontScheduleJpaApiService).should(atLeastOnce()).findScheduleList(scheduleMap, pageRequest);
        then(mockFrontScheduleJpaApiService).shouldHaveNoMoreInteractions();
    }
}

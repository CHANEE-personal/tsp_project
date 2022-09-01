package com.tsp.new_tsp_front.api.model.schedule.service.impl;

import com.tsp.new_tsp_front.api.model.domain.schedule.FrontScheduleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@Slf4j
@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("모델 스케줄 Repository Test")
class FrontScheduleJpaRepositoryTest {
    @Mock private FrontScheduleJpaRepository mockFrontScheduleJpaRepository;

    @Test
    @DisplayName("모델 스케줄 Mockito 조회 테스트")
    void 모델스케줄Mockito조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        scheduleMap.put("jpaStartPage", 1);
        scheduleMap.put("size", 3);

        List<FrontScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(FrontScheduleDTO.builder().idx(1).modelIdx(1)
                .modelSchedule("스케줄 테스트").modelScheduleTime(new Date()).build());

        // when
        when(mockFrontScheduleJpaRepository.findScheduleList(scheduleMap)).thenReturn(scheduleList);
        List<FrontScheduleDTO> newScheduleList = mockFrontScheduleJpaRepository.findScheduleList(scheduleMap);

        // then
        assertThat(newScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(newScheduleList.get(0).getModelIdx()).isEqualTo(scheduleList.get(0).getModelIdx());
        assertThat(newScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());
        assertThat(newScheduleList.get(0).getModelScheduleTime()).isEqualTo(scheduleList.get(0).getModelScheduleTime());

        // verify
        verify(mockFrontScheduleJpaRepository, times(1)).findScheduleList(scheduleMap);
        verify(mockFrontScheduleJpaRepository, atLeastOnce()).findScheduleList(scheduleMap);
        verifyNoMoreInteractions(mockFrontScheduleJpaRepository);

        InOrder inOrder = inOrder(mockFrontScheduleJpaRepository);
        inOrder.verify(mockFrontScheduleJpaRepository).findScheduleList(scheduleMap);
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
                .modelSchedule("스케줄 테스트").modelScheduleTime(new Date()).build());

        // when
        given(mockFrontScheduleJpaRepository.findScheduleList(scheduleMap)).willReturn(scheduleList);
        List<FrontScheduleDTO> newScheduleList = mockFrontScheduleJpaRepository.findScheduleList(scheduleMap);

        // then
        assertThat(newScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(newScheduleList.get(0).getModelIdx()).isEqualTo(scheduleList.get(0).getModelIdx());
        assertThat(newScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());
        assertThat(newScheduleList.get(0).getModelScheduleTime()).isEqualTo(scheduleList.get(0).getModelScheduleTime());

        // verify
        then(mockFrontScheduleJpaRepository).should(times(1)).findScheduleList(scheduleMap);
        then(mockFrontScheduleJpaRepository).should(atLeastOnce()).findScheduleList(scheduleMap);
        then(mockFrontScheduleJpaRepository).shouldHaveNoMoreInteractions();
    }
}
package com.tsp.api.model.schedule.service;

import com.tsp.api.model.domain.FrontModelDTO;
import com.tsp.api.model.domain.schedule.FrontScheduleDTO;
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

@Slf4j
@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("모델 스케줄 Repository Test")
class FrontScheduleJpaQueryRepositoryTest {
    @Mock private FrontScheduleJpaQueryRepository mockFrontScheduleJpaQueryRepository;
    private final FrontScheduleJpaQueryRepository frontScheduleJpaQueryRepository;

    @Test
    @DisplayName("모델 스케줄 리스트 조회 테스트")
    void 모델스케줄리스트조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        scheduleMap.put("searchKeyword", "김예영");
        scheduleMap.put("jpaStartPage", 0);
        scheduleMap.put("size", 100);

        // then
        assertThat(frontScheduleJpaQueryRepository.findModelScheduleList(scheduleMap)).isNotEmpty();

        Map<String, Object> lastMonthScheduleMap = new HashMap<>();
        lastMonthScheduleMap.put("searchStartTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 1, 0, 0, 0, 0));
        lastMonthScheduleMap.put("searchEndTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 30, 23, 59, 59));
        lastMonthScheduleMap.put("jpaStartPage", 0);
        lastMonthScheduleMap.put("size", 100);

        // then
        assertThat(frontScheduleJpaQueryRepository.findModelScheduleList(lastMonthScheduleMap)).isEmpty();

        Map<String, Object> currentScheduleMap = new HashMap<>();
        currentScheduleMap.put("searchStartTime", of(now().getYear(), LocalDate.now().getMonth(), 1, 0, 0, 0, 0));
        currentScheduleMap.put("searchEndTime", of(now().getYear(), LocalDate.now().getMonth(), 30, 23, 59, 59));
        currentScheduleMap.put("jpaStartPage", 0);
        currentScheduleMap.put("size", 100);

        // then
        assertThat(frontScheduleJpaQueryRepository.findModelScheduleList(currentScheduleMap)).isNotEmpty();
    }

    @Test
    @DisplayName("모델 스케줄 Mockito 조회 테스트")
    void 모델스케줄Mockito조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(FrontScheduleDTO.builder().idx(1L).frontModelDTO(FrontModelDTO.builder().build())
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());

        Page<FrontScheduleDTO> resultSchedule = new PageImpl<>(scheduleList, pageRequest, scheduleList.size());
        // when
        when(mockFrontScheduleJpaQueryRepository.findScheduleList(scheduleMap, pageRequest)).thenReturn(resultSchedule);
        Page<FrontScheduleDTO> newScheduleList = mockFrontScheduleJpaQueryRepository.findScheduleList(scheduleMap, pageRequest);
        List<FrontScheduleDTO> findScheduleList = newScheduleList.stream().collect(Collectors.toList());

        // then
        assertThat(findScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(findScheduleList.get(0).getFrontModelDTO().getIdx()).isEqualTo(scheduleList.get(0).getFrontModelDTO().getIdx());
        assertThat(findScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());
        assertThat(findScheduleList.get(0).getModelScheduleTime()).isEqualTo(scheduleList.get(0).getModelScheduleTime());

        // verify
        verify(mockFrontScheduleJpaQueryRepository, times(1)).findScheduleList(scheduleMap, pageRequest);
        verify(mockFrontScheduleJpaQueryRepository, atLeastOnce()).findScheduleList(scheduleMap, pageRequest);
        verifyNoMoreInteractions(mockFrontScheduleJpaQueryRepository);

        InOrder inOrder = inOrder(mockFrontScheduleJpaQueryRepository);
        inOrder.verify(mockFrontScheduleJpaQueryRepository).findScheduleList(scheduleMap, pageRequest);
    }

    @Test
    @DisplayName("모델 스케줄 BDD 조회 테스트")
    void 모델스케줄BDD조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(FrontScheduleDTO.builder().idx(1L).frontModelDTO(FrontModelDTO.builder().build())
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());

        Page<FrontScheduleDTO> resultSchedule = new PageImpl<>(scheduleList, pageRequest, scheduleList.size());
        // when
        given(mockFrontScheduleJpaQueryRepository.findScheduleList(scheduleMap, pageRequest)).willReturn(resultSchedule);
        Page<FrontScheduleDTO> newScheduleList = mockFrontScheduleJpaQueryRepository.findScheduleList(scheduleMap, pageRequest);
        List<FrontScheduleDTO> findScheduleList = newScheduleList.stream().collect(Collectors.toList());

        // then
        assertThat(findScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(findScheduleList.get(0).getFrontModelDTO().getIdx()).isEqualTo(scheduleList.get(0).getFrontModelDTO().getIdx());
        assertThat(findScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());
        assertThat(findScheduleList.get(0).getModelScheduleTime()).isEqualTo(scheduleList.get(0).getModelScheduleTime());

        // verify
        then(mockFrontScheduleJpaQueryRepository).should(times(1)).findScheduleList(scheduleMap, pageRequest);
        then(mockFrontScheduleJpaQueryRepository).should(atLeastOnce()).findScheduleList(scheduleMap, pageRequest);
        then(mockFrontScheduleJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 스케줄 Mockito 조회 테스트")
    void 모델스케줄Mockito조회테스트0903() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        scheduleMap.put("jpaStartPage", 1);
        scheduleMap.put("size", 3);

        List<FrontScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(FrontScheduleDTO.builder().idx(1L).frontModelDTO(FrontModelDTO.builder().build())
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());
        scheduleList.add(FrontScheduleDTO.builder().idx(1L).frontModelDTO(FrontModelDTO.builder().build())
                .modelSchedule("스케줄 두번째 테스트").modelScheduleTime(now()).build());

        List<FrontModelDTO> modelScheduleList = new ArrayList<>();
        modelScheduleList.add(FrontModelDTO.builder().idx(1L).categoryCd(1).modelKorName("조찬희")
                .modelScheduleList(scheduleList).build());

        // when
        when(mockFrontScheduleJpaQueryRepository.findModelScheduleList(scheduleMap)).thenReturn(modelScheduleList);
        List<FrontModelDTO> newModelScheduleList = mockFrontScheduleJpaQueryRepository.findModelScheduleList(scheduleMap);

        // then
        assertThat(newModelScheduleList.get(0).getIdx()).isEqualTo(modelScheduleList.get(0).getIdx());
        assertThat(newModelScheduleList.get(0).getModelKorName()).isEqualTo(modelScheduleList.get(0).getModelKorName());
        assertThat(newModelScheduleList.get(0).getModelScheduleList().get(0).getModelSchedule()).isEqualTo(modelScheduleList.get(0).getModelScheduleList().get(0).getModelSchedule());

        // verify
        verify(mockFrontScheduleJpaQueryRepository, times(1)).findModelScheduleList(scheduleMap);
        verify(mockFrontScheduleJpaQueryRepository, atLeastOnce()).findModelScheduleList(scheduleMap);
        verifyNoMoreInteractions(mockFrontScheduleJpaQueryRepository);

        InOrder inOrder = inOrder(mockFrontScheduleJpaQueryRepository);
        inOrder.verify(mockFrontScheduleJpaQueryRepository).findModelScheduleList(scheduleMap);
    }

    @Test
    @DisplayName("모델 스케줄 BDD 조회 테스트")
    void 모델스케줄BDD조회테스트0903() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        scheduleMap.put("jpaStartPage", 1);
        scheduleMap.put("size", 3);

        List<FrontScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(FrontScheduleDTO.builder().idx(1L).frontModelDTO(FrontModelDTO.builder().build())
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());
        scheduleList.add(FrontScheduleDTO.builder().idx(1L).frontModelDTO(FrontModelDTO.builder().build())
                .modelSchedule("스케줄 두번째 테스트").modelScheduleTime(now()).build());

        List<FrontModelDTO> modelScheduleList = new ArrayList<>();
        modelScheduleList.add(FrontModelDTO.builder().idx(1L).categoryCd(1).modelKorName("조찬희")
                .modelScheduleList(scheduleList).build());

        frontScheduleJpaQueryRepository.findModelScheduleList(scheduleMap);

        // when
        given(mockFrontScheduleJpaQueryRepository.findModelScheduleList(scheduleMap)).willReturn(modelScheduleList);
        List<FrontModelDTO> newModelScheduleList = mockFrontScheduleJpaQueryRepository.findModelScheduleList(scheduleMap);

        // then
        assertThat(newModelScheduleList.get(0).getIdx()).isEqualTo(modelScheduleList.get(0).getIdx());
        assertThat(newModelScheduleList.get(0).getModelKorName()).isEqualTo(modelScheduleList.get(0).getModelKorName());
        assertThat(newModelScheduleList.get(0).getModelScheduleList().get(0).getModelSchedule()).isEqualTo(modelScheduleList.get(0).getModelScheduleList().get(0).getModelSchedule());

        // verify
        then(mockFrontScheduleJpaQueryRepository).should(times(1)).findModelScheduleList(scheduleMap);
        then(mockFrontScheduleJpaQueryRepository).should(atLeastOnce()).findModelScheduleList(scheduleMap);
        then(mockFrontScheduleJpaQueryRepository).shouldHaveNoMoreInteractions();
    }
}

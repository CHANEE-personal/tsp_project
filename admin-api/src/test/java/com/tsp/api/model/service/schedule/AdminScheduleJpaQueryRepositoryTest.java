package com.tsp.api.model.service.schedule;

import com.tsp.api.domain.model.AdminModelDTO;
import com.tsp.api.domain.model.AdminModelEntity;
import com.tsp.api.domain.model.CareerJson;
import com.tsp.api.domain.model.agency.AdminAgencyDTO;
import com.tsp.api.domain.model.agency.AdminAgencyEntity;
import com.tsp.api.domain.model.schedule.AdminScheduleDTO;
import com.tsp.api.domain.model.schedule.AdminScheduleEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.*;
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
class AdminScheduleJpaQueryRepositoryTest {
    @Mock private AdminScheduleJpaQueryRepository mockAdminScheduleJpaQueryRepository;
    private final AdminScheduleJpaQueryRepository adminScheduleJpaQueryRepository;
    private final EntityManager em;

    private AdminModelEntity adminModelEntity;
    private AdminModelDTO adminModelDTO;
    private AdminScheduleEntity adminScheduleEntity;
    private AdminScheduleDTO adminScheduleDTO;
    private AdminAgencyEntity adminAgencyEntity;
    private AdminAgencyDTO adminAgencyDTO;

    void createModelAndSchedule() {
        adminAgencyEntity = AdminAgencyEntity.builder()
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

        adminAgencyDTO = AdminAgencyEntity.toDto(adminAgencyEntity);

        ArrayList<CareerJson> careerList = new ArrayList<>();
        careerList.add(new CareerJson("title","txt"));

        adminModelEntity = AdminModelEntity.builder()
                .categoryCd(1)
                .categoryAge(2)
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .status("active")
                .newYn("N")
                .favoriteCount(1)
                .viewCount(1)
                .adminAgencyEntity(adminAgencyEntity)
                .careerList(careerList)
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        em.persist(adminModelEntity);

        adminModelDTO = AdminModelEntity.toDto(adminModelEntity);

        adminScheduleEntity = AdminScheduleEntity.builder()
                .modelSchedule("스케줄 테스트")
                .modelScheduleTime(now())
                .visible("Y")
                .build();

        adminScheduleDTO = AdminScheduleEntity.toDto(adminScheduleEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createModelAndSchedule();
    }

    @Test
    @DisplayName("모델 스케줄 리스트 조회 테스트")
    void 모델스케줄리스트조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        scheduleMap.put("searchKeyword", "김예영");
        PageRequest pageRequest = PageRequest.of(1, 100);

        // then
        assertThat(adminScheduleJpaQueryRepository.findScheduleList(scheduleMap, pageRequest)).isNotEmpty();

        Map<String, Object> lastMonthScheduleMap = new HashMap<>();
        lastMonthScheduleMap.put("searchStartTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 1, 0, 0, 0, 0));
        lastMonthScheduleMap.put("searchEndTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 30, 23, 59, 59));
        PageRequest pageRequest2 = PageRequest.of(1, 100);

        // then
        assertThat(adminScheduleJpaQueryRepository.findScheduleList(lastMonthScheduleMap, pageRequest2)).isEmpty();

        Map<String, Object> currentScheduleMap = new HashMap<>();
        currentScheduleMap.put("searchStartTime", of(now().getYear(), LocalDate.now().getMonth(), 1, 0, 0, 0, 0));
        currentScheduleMap.put("searchEndTime", of(now().getYear(), LocalDate.now().getMonth(), 30, 23, 59, 59));
        PageRequest pageRequest3 = PageRequest.of(1, 100);

        // then
        assertThat(adminScheduleJpaQueryRepository.findScheduleList(currentScheduleMap, pageRequest3)).isNotEmpty();
    }

    @Test
    @DisplayName("모델 스케줄 Mockito 조회 테스트")
    void 모델스케줄Mockito조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(AdminScheduleDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());
        scheduleList.add(AdminScheduleDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트 두번째").modelScheduleTime(now()).build());

        Page<AdminScheduleDTO> resultSchedule = new PageImpl<>(scheduleList, pageRequest, scheduleList.size());

        // when
        when(mockAdminScheduleJpaQueryRepository.findScheduleList(scheduleMap, pageRequest)).thenReturn(resultSchedule);
        Page<AdminScheduleDTO> newModelScheduleList = mockAdminScheduleJpaQueryRepository.findScheduleList(scheduleMap, pageRequest);
        List<AdminScheduleDTO> findScheduleList = newModelScheduleList.stream().collect(Collectors.toList());

        // then
        assertThat(findScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(findScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());

        // verify
        verify(mockAdminScheduleJpaQueryRepository, times(1)).findScheduleList(scheduleMap, pageRequest);
        verify(mockAdminScheduleJpaQueryRepository, atLeastOnce()).findScheduleList(scheduleMap, pageRequest);
        verifyNoMoreInteractions(mockAdminScheduleJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminScheduleJpaQueryRepository);
        inOrder.verify(mockAdminScheduleJpaQueryRepository).findScheduleList(scheduleMap, pageRequest);
    }

    @Test
    @DisplayName("모델 스케줄 BDD 조회 테스트")
    void 모델스케줄BDD조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(AdminScheduleDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());
        scheduleList.add(AdminScheduleDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트 두번째").modelScheduleTime(now()).build());

        Page<AdminScheduleDTO> resultSchedule = new PageImpl<>(scheduleList, pageRequest, scheduleList.size());

        // when
        given(mockAdminScheduleJpaQueryRepository.findScheduleList(scheduleMap, pageRequest)).willReturn(resultSchedule);
        Page<AdminScheduleDTO> newModelScheduleList = mockAdminScheduleJpaQueryRepository.findScheduleList(scheduleMap, pageRequest);
        List<AdminScheduleDTO> findScheduleList = newModelScheduleList.stream().collect(Collectors.toList());

        // then
        assertThat(findScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(findScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());

        // verify
        then(mockAdminScheduleJpaQueryRepository).should(times(1)).findScheduleList(scheduleMap, pageRequest);
        then(mockAdminScheduleJpaQueryRepository).should(atLeastOnce()).findScheduleList(scheduleMap, pageRequest);
        then(mockAdminScheduleJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 스케줄 Mockito 검색 및 날짜 조회 테스트")
    void 모델스케줄Mockito검색및날짜조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(AdminScheduleDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());
        scheduleList.add(AdminScheduleDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트 두번째").modelScheduleTime(now()).build());

        Page<AdminScheduleDTO> resultSchedule = new PageImpl<>(scheduleList, pageRequest, scheduleList.size());

        // when
        when(mockAdminScheduleJpaQueryRepository.findScheduleList(scheduleMap, pageRequest)).thenReturn(resultSchedule);
        Page<AdminScheduleDTO> newModelScheduleList = mockAdminScheduleJpaQueryRepository.findScheduleList(scheduleMap, pageRequest);
        List<AdminScheduleDTO> findScheduleList = newModelScheduleList.stream().collect(Collectors.toList());

        // then
        assertThat(findScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(findScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());

        // verify
        verify(mockAdminScheduleJpaQueryRepository, times(1)).findScheduleList(scheduleMap, pageRequest);
        verify(mockAdminScheduleJpaQueryRepository, atLeastOnce()).findScheduleList(scheduleMap, pageRequest);
        verifyNoMoreInteractions(mockAdminScheduleJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminScheduleJpaQueryRepository);
        inOrder.verify(mockAdminScheduleJpaQueryRepository).findScheduleList(scheduleMap, pageRequest);
    }
}

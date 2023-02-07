package com.tsp.api.model.service.schedule;

import com.tsp.api.model.domain.AdminModelDTO;
import com.tsp.api.model.domain.AdminModelEntity;
import com.tsp.api.model.domain.CareerJson;
import com.tsp.api.model.domain.agency.AdminAgencyDTO;
import com.tsp.api.model.domain.agency.AdminAgencyEntity;
import com.tsp.api.model.domain.schedule.AdminScheduleDTO;
import com.tsp.api.model.domain.schedule.AdminScheduleEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("모델 스케줄 Service Test")
class AdminScheduleJpaServiceTest {
    @Mock
    private AdminScheduleJpaService mockAdminScheduleJpaService;
    private final AdminScheduleJpaService adminScheduleJpaService;
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
        careerList.add(new CareerJson("title", "txt"));

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
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        em.persist(adminModelEntity);

        adminModelDTO = AdminModelEntity.toDto(adminModelEntity);

        adminScheduleEntity = AdminScheduleEntity.builder()
                .adminModelEntity(adminModelEntity)
                .modelSchedule("스케줄 테스트")
                .modelScheduleTime(now())
                .visible("Y")
                .build();
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
        assertThat(adminScheduleJpaService.findScheduleList(scheduleMap, pageRequest)).isNotEmpty();

        Map<String, Object> lastMonthScheduleMap = new HashMap<>();
        lastMonthScheduleMap.put("searchStartTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 1, 0, 0, 0, 0));
        lastMonthScheduleMap.put("searchEndTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 30, 23, 59, 59));
        PageRequest pageRequest2 = PageRequest.of(1, 100);

        // then
        assertThat(adminScheduleJpaService.findScheduleList(lastMonthScheduleMap, pageRequest2)).isEmpty();

        Map<String, Object> currentScheduleMap = new HashMap<>();
        currentScheduleMap.put("searchStartTime", of(now().getYear(), LocalDate.now().getMonth(), 1, 0, 0, 0, 0));
        currentScheduleMap.put("searchEndTime", of(now().getYear(), LocalDate.now().getMonth(), 30, 23, 59, 59));
        PageRequest pageRequest3 = PageRequest.of(1, 100);

        // then
        assertThat(adminScheduleJpaService.findScheduleList(currentScheduleMap, pageRequest3)).isNotEmpty();
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
        when(mockAdminScheduleJpaService.findScheduleList(scheduleMap, pageRequest)).thenReturn(resultSchedule);
        Page<AdminScheduleDTO> newModelScheduleList = mockAdminScheduleJpaService.findScheduleList(scheduleMap, pageRequest);
        List<AdminScheduleDTO> findScheduleList = newModelScheduleList.stream().collect(Collectors.toList());

        // then
        assertThat(findScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(findScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());

        // verify
        verify(mockAdminScheduleJpaService, times(1)).findScheduleList(scheduleMap, pageRequest);
        verify(mockAdminScheduleJpaService, atLeastOnce()).findScheduleList(scheduleMap, pageRequest);
        verifyNoMoreInteractions(mockAdminScheduleJpaService);

        InOrder inOrder = inOrder(mockAdminScheduleJpaService);
        inOrder.verify(mockAdminScheduleJpaService).findScheduleList(scheduleMap, pageRequest);
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
        given(mockAdminScheduleJpaService.findScheduleList(scheduleMap, pageRequest)).willReturn(resultSchedule);
        Page<AdminScheduleDTO> newModelScheduleList = mockAdminScheduleJpaService.findScheduleList(scheduleMap, pageRequest);
        List<AdminScheduleDTO> findScheduleList = newModelScheduleList.stream().collect(Collectors.toList());

        // then
        assertThat(findScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(findScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());

        // verify
        then(mockAdminScheduleJpaService).should(times(1)).findScheduleList(scheduleMap, pageRequest);
        then(mockAdminScheduleJpaService).should(atLeastOnce()).findScheduleList(scheduleMap, pageRequest);
        then(mockAdminScheduleJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델스케줄상세Mockito조회테스트")
    void 모델스케줄상세Mockito조회테스트() {
        // given
        AdminScheduleEntity adminScheduleEntity = AdminScheduleEntity.builder()
                .idx(1L)
                .modelSchedule("스케줄 테스트")
                .modelScheduleTime(now())
                .visible("Y")
                .build();

        adminScheduleDTO = AdminScheduleEntity.toDto(adminScheduleEntity);

        // when
        when(mockAdminScheduleJpaService.findOneSchedule(adminScheduleEntity.getIdx())).thenReturn(adminScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaService.findOneSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(scheduleInfo.getIdx()).isEqualTo(1);
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo("스케줄 테스트");
        assertThat(scheduleInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockAdminScheduleJpaService, times(1)).findOneSchedule(adminScheduleEntity.getIdx());
        verify(mockAdminScheduleJpaService, atLeastOnce()).findOneSchedule(adminScheduleEntity.getIdx());
        verifyNoMoreInteractions(mockAdminScheduleJpaService);

        InOrder inOrder = inOrder(mockAdminScheduleJpaService);
        inOrder.verify(mockAdminScheduleJpaService).findOneSchedule(adminScheduleEntity.getIdx());
    }

    @Test
    @DisplayName("모델스케줄상세BDD조회테스트")
    void 모델스케줄상세BDD조회테스트() {
        // given
        AdminScheduleEntity adminScheduleEntity = AdminScheduleEntity.builder()
                .idx(1L)
                .modelSchedule("스케줄 테스트")
                .modelScheduleTime(now())
                .visible("Y")
                .build();

        adminScheduleDTO = AdminScheduleEntity.toDto(adminScheduleEntity);

        // when
        given(mockAdminScheduleJpaService.findOneSchedule(adminScheduleEntity.getIdx())).willReturn(adminScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaService.findOneSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(scheduleInfo.getIdx()).isEqualTo(1);
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo("스케줄 테스트");
        assertThat(scheduleInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockAdminScheduleJpaService).should(times(1)).findOneSchedule(adminScheduleEntity.getIdx());
        then(mockAdminScheduleJpaService).should(atLeastOnce()).findOneSchedule(adminScheduleEntity.getIdx());
        then(mockAdminScheduleJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 모델 스케줄 상세 조회 테스트")
    void 이전or다음모델스케줄상세조회테스트() {
        // given
        adminScheduleEntity = AdminScheduleEntity.builder().idx(2L).build();

        // when
        adminScheduleDTO = adminScheduleJpaService.findOneSchedule(adminScheduleEntity.getIdx());

        // 이전 모델 섭외
        assertThat(adminScheduleJpaService.findPrevOneSchedule(adminScheduleEntity.getIdx()).getIdx()).isEqualTo(1);
        // 다음 모델 섭외
        assertThat(adminScheduleJpaService.findNextOneSchedule(adminScheduleEntity.getIdx()).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 모델 스케줄 상세 조회 Mockito 테스트")
    void 이전모델스케줄상세조회Mockito테스트() {
        // given
        adminScheduleEntity = AdminScheduleEntity.builder().idx(2L).build();

        // when
        adminScheduleDTO = adminScheduleJpaService.findOneSchedule(adminScheduleEntity.getIdx());

        when(mockAdminScheduleJpaService.findPrevOneSchedule(adminScheduleEntity.getIdx())).thenReturn(adminScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaService.findPrevOneSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(scheduleInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockAdminScheduleJpaService, times(1)).findPrevOneSchedule(adminScheduleEntity.getIdx());
        verify(mockAdminScheduleJpaService, atLeastOnce()).findPrevOneSchedule(adminScheduleEntity.getIdx());
        verifyNoMoreInteractions(mockAdminScheduleJpaService);

        InOrder inOrder = inOrder(mockAdminScheduleJpaService);
        inOrder.verify(mockAdminScheduleJpaService).findPrevOneSchedule(adminScheduleEntity.getIdx());
    }

    @Test
    @DisplayName("이전 모델 스케줄 상세 조회 BDD 테스트")
    void 이전모델스케줄상세조회BDD테스트() {
        // given
        adminScheduleEntity = AdminScheduleEntity.builder().idx(2L).build();

        // when
        adminScheduleDTO = adminScheduleJpaService.findOneSchedule(adminScheduleEntity.getIdx());

        given(mockAdminScheduleJpaService.findPrevOneSchedule(adminScheduleEntity.getIdx())).willReturn(adminScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaService.findPrevOneSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(scheduleInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockAdminScheduleJpaService).should(times(1)).findPrevOneSchedule(adminScheduleEntity.getIdx());
        then(mockAdminScheduleJpaService).should(atLeastOnce()).findPrevOneSchedule(adminScheduleEntity.getIdx());
        then(mockAdminScheduleJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 모델 스케줄 상세 조회 Mockito 테스트")
    void 다음모델스케줄상세조회Mockito테스트() {
        // given
        adminScheduleEntity = AdminScheduleEntity.builder().idx(2L).build();

        // when
        adminScheduleDTO = adminScheduleJpaService.findOneSchedule(adminScheduleEntity.getIdx());

        when(mockAdminScheduleJpaService.findNextOneSchedule(adminScheduleEntity.getIdx())).thenReturn(adminScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaService.findNextOneSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(scheduleInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockAdminScheduleJpaService, times(1)).findNextOneSchedule(adminScheduleEntity.getIdx());
        verify(mockAdminScheduleJpaService, atLeastOnce()).findNextOneSchedule(adminScheduleEntity.getIdx());
        verifyNoMoreInteractions(mockAdminScheduleJpaService);

        InOrder inOrder = inOrder(mockAdminScheduleJpaService);
        inOrder.verify(mockAdminScheduleJpaService).findNextOneSchedule(adminScheduleEntity.getIdx());
    }

    @Test
    @DisplayName("다음 모델 스케줄 상세 조회 BDD 테스트")
    void 다음모델스케줄상세조회BDD테스트() {
        // given
        adminScheduleEntity = AdminScheduleEntity.builder().idx(2L).build();

        // when
        adminScheduleDTO = adminScheduleJpaService.findOneSchedule(adminScheduleEntity.getIdx());

        given(mockAdminScheduleJpaService.findNextOneSchedule(adminScheduleEntity.getIdx())).willReturn(adminScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaService.findNextOneSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(scheduleInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockAdminScheduleJpaService).should(times(1)).findNextOneSchedule(adminScheduleEntity.getIdx());
        then(mockAdminScheduleJpaService).should(atLeastOnce()).findNextOneSchedule(adminScheduleEntity.getIdx());
        then(mockAdminScheduleJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델스케줄등록Mockito테스트")
    void 모델스케줄등록Mockito테스트() {
        // given
        AdminScheduleDTO oneSchedule = adminScheduleJpaService.insertSchedule(adminModelDTO.getIdx(), adminScheduleEntity);

        // when
        when(mockAdminScheduleJpaService.findOneSchedule(oneSchedule.getIdx())).thenReturn(oneSchedule);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaService.findOneSchedule(oneSchedule.getIdx());

        // then
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(adminModelDTO.getIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo("스케줄 테스트");
        assertThat(scheduleInfo.getModelScheduleTime()).isNotNull();

        // verify
        verify(mockAdminScheduleJpaService, times(1)).findOneSchedule(oneSchedule.getIdx());
        verify(mockAdminScheduleJpaService, atLeastOnce()).findOneSchedule(oneSchedule.getIdx());
        verifyNoMoreInteractions(mockAdminScheduleJpaService);

        InOrder inOrder = inOrder(mockAdminScheduleJpaService);
        inOrder.verify(mockAdminScheduleJpaService).findOneSchedule(oneSchedule.getIdx());
    }

    @Test
    @DisplayName("모델스케줄등록BDD테스트")
    void 모델스케줄등록BDD테스트() {
        // given
        AdminScheduleDTO oneSchedule = adminScheduleJpaService.insertSchedule(adminModelDTO.getIdx(), adminScheduleEntity);

        // when
        given(mockAdminScheduleJpaService.findOneSchedule(oneSchedule.getIdx())).willReturn(oneSchedule);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaService.findOneSchedule(oneSchedule.getIdx());

        // then
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(adminModelDTO.getIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo("스케줄 테스트");
        assertThat(scheduleInfo.getModelScheduleTime()).isNotNull();

        // verify
        then(mockAdminScheduleJpaService).should(times(1)).findOneSchedule(oneSchedule.getIdx());
        then(mockAdminScheduleJpaService).should(atLeastOnce()).findOneSchedule(oneSchedule.getIdx());
        then(mockAdminScheduleJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델스케줄수정Mockito테스트")
    void 모델스케줄수정Mockito테스트() {
        // given
        em.persist(adminModelEntity);
        AdminScheduleDTO insertScheduleDTO = adminScheduleJpaService.insertSchedule(adminModelDTO.getIdx(), adminScheduleEntity);

        adminScheduleEntity = AdminScheduleEntity.builder()
                .idx(insertScheduleDTO.getIdx())
                .modelSchedule("스케줄 수정 테스트")
                .adminModelEntity(adminModelEntity)
                .modelScheduleTime(insertScheduleDTO.getModelScheduleTime())
                .visible("Y")
                .build();

        AdminScheduleDTO updateScheduleDTO = adminScheduleJpaService.updateSchedule(adminScheduleEntity.getIdx(), adminScheduleEntity);

        em.flush();
        em.clear();

        // when
        when(mockAdminScheduleJpaService.findOneSchedule(updateScheduleDTO.getIdx())).thenReturn(updateScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaService.findOneSchedule(updateScheduleDTO.getIdx());

        // then
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(updateScheduleDTO.getModelIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo("스케줄 수정 테스트");

        // verify
        verify(mockAdminScheduleJpaService, times(1)).findOneSchedule(updateScheduleDTO.getIdx());
        verify(mockAdminScheduleJpaService, atLeastOnce()).findOneSchedule(updateScheduleDTO.getIdx());
        verifyNoMoreInteractions(mockAdminScheduleJpaService);

        InOrder inOrder = inOrder(mockAdminScheduleJpaService);
        inOrder.verify(mockAdminScheduleJpaService).findOneSchedule(updateScheduleDTO.getIdx());
    }

    @Test
    @DisplayName("모델스케줄수정BDD테스트")
    void 모델스케줄수정BDD테스트() {
        // given
        em.persist(adminModelEntity);
        AdminScheduleDTO insertScheduleDTO = adminScheduleJpaService.insertSchedule(adminModelDTO.getIdx(), adminScheduleEntity);

        adminScheduleEntity = AdminScheduleEntity.builder()
                .idx(insertScheduleDTO.getIdx())
                .modelSchedule("스케줄 수정 테스트")
                .adminModelEntity(adminModelEntity)
                .modelScheduleTime(insertScheduleDTO.getModelScheduleTime())
                .visible("Y")
                .build();

        AdminScheduleDTO updateScheduleDTO = adminScheduleJpaService.updateSchedule(adminScheduleEntity.getIdx(), adminScheduleEntity);

        em.flush();
        em.clear();

        // when
        given(mockAdminScheduleJpaService.findOneSchedule(updateScheduleDTO.getIdx())).willReturn(updateScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaService.findOneSchedule(updateScheduleDTO.getIdx());

        // then
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(updateScheduleDTO.getModelIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo("스케줄 수정 테스트");

        // verify
        then(mockAdminScheduleJpaService).should(times(1)).findOneSchedule(updateScheduleDTO.getIdx());
        then(mockAdminScheduleJpaService).should(atLeastOnce()).findOneSchedule(updateScheduleDTO.getIdx());
        then(mockAdminScheduleJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델스케줄삭제Mockito테스트")
    void 모델스케줄삭제Mockito테스트() {
        // given
        em.persist(adminScheduleEntity);
        adminScheduleDTO = AdminScheduleEntity.toDto(adminScheduleEntity);

        // when
        when(mockAdminScheduleJpaService.findOneSchedule(adminScheduleEntity.getIdx())).thenReturn(adminScheduleDTO);
        Long deleteIdx = adminScheduleJpaService.deleteSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(mockAdminScheduleJpaService.findOneSchedule(adminScheduleEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminScheduleJpaService, times(1)).findOneSchedule(adminScheduleEntity.getIdx());
        verify(mockAdminScheduleJpaService, atLeastOnce()).findOneSchedule(adminScheduleEntity.getIdx());
        verifyNoMoreInteractions(mockAdminScheduleJpaService);

        InOrder inOrder = inOrder(mockAdminScheduleJpaService);
        inOrder.verify(mockAdminScheduleJpaService).findOneSchedule(adminScheduleEntity.getIdx());
    }

    @Test
    @DisplayName("모델스케줄삭제BDD테스트")
    void 모델스케줄삭제BDD테스트() {
        // given
        em.persist(adminScheduleEntity);
        adminScheduleDTO = AdminScheduleEntity.toDto(adminScheduleEntity);

        // when
        given(mockAdminScheduleJpaService.findOneSchedule(adminScheduleEntity.getIdx())).willReturn(adminScheduleDTO);
        Long deleteIdx = adminScheduleJpaService.deleteSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(mockAdminScheduleJpaService.findOneSchedule(adminScheduleEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminScheduleJpaService).should(times(1)).findOneSchedule(adminScheduleEntity.getIdx());
        then(mockAdminScheduleJpaService).should(atLeastOnce()).findOneSchedule(adminScheduleEntity.getIdx());
        then(mockAdminScheduleJpaService).shouldHaveNoMoreInteractions();
    }
}

package com.tsp.api.model.service.schedule;

import com.tsp.api.model.domain.schedule.AdminScheduleDTO;
import com.tsp.api.model.domain.schedule.AdminScheduleEntity;
import com.tsp.api.model.service.AdminModelCommonServiceTest;
import com.tsp.api.model.service.AdminModelJpaRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
class AdminScheduleJpaServiceTest extends AdminModelCommonServiceTest {

    @Mock private AdminScheduleJpaQueryRepository adminScheduleJpaQueryRepository;
    @Mock private AdminScheduleJpaRepository adminScheduleJpaRepository;
    @Mock private AdminModelJpaRepository adminModelJpaRepository;
    @InjectMocks private AdminScheduleJpaServiceImpl mockAdminScheduleJpaService;
    private final AdminScheduleJpaService adminScheduleJpaService;
    private final EntityManager em;

    @Test
    @DisplayName("모델 스케줄 리스트 조회 테스트")
    void 모델스케줄리스트조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        scheduleMap.put("searchKeyword", "김예영");
        PageRequest pageRequest = PageRequest.of(0, 100);

        // then
        assertThat(adminScheduleJpaService.findScheduleList(scheduleMap, pageRequest)).isNotEmpty();

        Map<String, Object> lastMonthScheduleMap = new HashMap<>();
        lastMonthScheduleMap.put("searchStartTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 1, 0, 0, 0, 0));
        lastMonthScheduleMap.put("searchEndTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 30, 23, 59, 59));
        PageRequest pageRequest2 = PageRequest.of(0, 100);

        // then
        assertThat(adminScheduleJpaService.findScheduleList(lastMonthScheduleMap, pageRequest2)).isEmpty();

        Map<String, Object> currentScheduleMap = new HashMap<>();
        currentScheduleMap.put("searchStartTime", of(now().getYear(), LocalDate.now().getMonth(), 1, 0, 0, 0, 0));
        currentScheduleMap.put("searchEndTime", of(now().getYear(), LocalDate.now().getMonth(), 30, 23, 59, 59));
        PageRequest pageRequest3 = PageRequest.of(0, 100);

        // then
        assertThat(adminScheduleJpaService.findScheduleList(currentScheduleMap, pageRequest3)).isNotEmpty();
    }

    @Test
    @DisplayName("모델 스케줄 Mockito 조회 테스트")
    void 모델스케줄Mockito조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<AdminScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(AdminScheduleDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());
        scheduleList.add(AdminScheduleDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트 두번째").modelScheduleTime(now()).build());

        Page<AdminScheduleDTO> resultSchedule = new PageImpl<>(scheduleList, pageRequest, scheduleList.size());

        // when
        when(adminScheduleJpaQueryRepository.findScheduleList(scheduleMap, pageRequest)).thenReturn(resultSchedule);
        Page<AdminScheduleDTO> newModelScheduleList = mockAdminScheduleJpaService.findScheduleList(scheduleMap, pageRequest);
        List<AdminScheduleDTO> findScheduleList = newModelScheduleList.stream().collect(Collectors.toList());

        // then
        assertThat(findScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(findScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());

        // verify
        verify(adminScheduleJpaQueryRepository, times(1)).findScheduleList(scheduleMap, pageRequest);
        verify(adminScheduleJpaQueryRepository, atLeastOnce()).findScheduleList(scheduleMap, pageRequest);
        verifyNoMoreInteractions(adminScheduleJpaQueryRepository);

        InOrder inOrder = inOrder(adminScheduleJpaQueryRepository);
        inOrder.verify(adminScheduleJpaQueryRepository).findScheduleList(scheduleMap, pageRequest);
    }

    @Test
    @DisplayName("모델 스케줄 BDD 조회 테스트")
    void 모델스케줄BDD조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<AdminScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(AdminScheduleDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());
        scheduleList.add(AdminScheduleDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트 두번째").modelScheduleTime(now()).build());

        Page<AdminScheduleDTO> resultSchedule = new PageImpl<>(scheduleList, pageRequest, scheduleList.size());

        // when
        given(adminScheduleJpaQueryRepository.findScheduleList(scheduleMap, pageRequest)).willReturn(resultSchedule);
        Page<AdminScheduleDTO> newModelScheduleList = mockAdminScheduleJpaService.findScheduleList(scheduleMap, pageRequest);
        List<AdminScheduleDTO> findScheduleList = newModelScheduleList.stream().collect(Collectors.toList());

        // then
        assertThat(findScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(findScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());

        // verify
        then(adminScheduleJpaQueryRepository).should(times(1)).findScheduleList(scheduleMap, pageRequest);
        then(adminScheduleJpaQueryRepository).should(atLeastOnce()).findScheduleList(scheduleMap, pageRequest);
        then(adminScheduleJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델스케줄상세Mockito조회테스트")
    void 모델스케줄상세Mockito조회테스트() {
        // when
        when(adminScheduleJpaRepository.findById(adminScheduleEntity.getIdx())).thenReturn(Optional.ofNullable(adminScheduleEntity));
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaService.findOneSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(scheduleInfo.getIdx()).isEqualTo(adminScheduleEntity.getIdx());
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(adminScheduleEntity.getAdminModelEntity().getIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo(adminScheduleEntity.getModelSchedule());
        assertThat(scheduleInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(adminScheduleJpaRepository, times(1)).findById(adminScheduleEntity.getIdx());
        verify(adminScheduleJpaRepository, atLeastOnce()).findById(adminScheduleEntity.getIdx());
        verifyNoMoreInteractions(adminScheduleJpaRepository);

        InOrder inOrder = inOrder(adminScheduleJpaRepository);
        inOrder.verify(adminScheduleJpaRepository).findById(adminScheduleEntity.getIdx());
    }

    @Test
    @DisplayName("모델스케줄상세BDD조회테스트")
    void 모델스케줄상세BDD조회테스트() {
        // when
        given(adminScheduleJpaRepository.findById(adminScheduleEntity.getIdx())).willReturn(Optional.ofNullable(adminScheduleEntity));
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaService.findOneSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(scheduleInfo.getIdx()).isEqualTo(adminScheduleEntity.getIdx());
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(adminScheduleEntity.getAdminModelEntity().getIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo(adminScheduleEntity.getModelSchedule());
        assertThat(scheduleInfo.getVisible()).isEqualTo("Y");

        // verify
        then(adminScheduleJpaRepository).should(times(1)).findById(adminScheduleEntity.getIdx());
        then(adminScheduleJpaRepository).should(atLeastOnce()).findById(adminScheduleEntity.getIdx());
        then(adminScheduleJpaRepository).shouldHaveNoMoreInteractions();
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
        // when
        when(adminModelJpaRepository.findById(adminModelEntity.getIdx())).thenReturn(Optional.ofNullable(adminModelEntity));
        when(adminScheduleJpaRepository.save(adminScheduleEntity)).thenReturn(adminScheduleEntity);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaService.insertSchedule(adminModelEntity.getIdx(), adminScheduleEntity);

        // then
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo(adminScheduleEntity.getModelSchedule());
        assertThat(scheduleInfo.getModelScheduleTime()).isNotNull();

        // verify
        verify(adminModelJpaRepository, times(1)).findById(adminModelEntity.getIdx());
        verify(adminModelJpaRepository, atLeastOnce()).findById(adminModelEntity.getIdx());
        verifyNoMoreInteractions(adminModelJpaRepository);

        InOrder inOrder = inOrder(adminModelJpaRepository);
        inOrder.verify(adminModelJpaRepository).findById(adminModelEntity.getIdx());
    }

    @Test
    @DisplayName("모델스케줄등록BDD테스트")
    void 모델스케줄등록BDD테스트() {
        // when
        given(adminModelJpaRepository.findById(adminModelEntity.getIdx())).willReturn(Optional.ofNullable(adminModelEntity));
        given(adminScheduleJpaRepository.save(adminScheduleEntity)).willReturn(adminScheduleEntity);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaService.insertSchedule(adminModelEntity.getIdx(), adminScheduleEntity);

        // then
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo(adminScheduleEntity.getModelSchedule());
        assertThat(scheduleInfo.getModelScheduleTime()).isNotNull();

        // verify
        then(adminModelJpaRepository).should(times(1)).findById(adminModelEntity.getIdx());
        then(adminModelJpaRepository).should(atLeastOnce()).findById(adminModelEntity.getIdx());
        then(adminModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델스케줄수정Mockito테스트")
    void 모델스케줄수정Mockito테스트() {
        // given
        AdminScheduleEntity updateScheduleEntity = AdminScheduleEntity.builder()
                .idx(adminScheduleEntity.getIdx())
                .modelSchedule("스케줄 수정 테스트")
                .adminModelEntity(adminModelEntity)
                .modelScheduleTime(adminScheduleEntity.getModelScheduleTime())
                .visible("Y")
                .build();

        // when
        when(adminScheduleJpaRepository.findById(updateScheduleEntity.getIdx())).thenReturn(Optional.of(updateScheduleEntity));
        when(adminScheduleJpaRepository.save(updateScheduleEntity)).thenReturn(updateScheduleEntity);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaService.updateSchedule(updateScheduleEntity.getIdx(), updateScheduleEntity);

        // then
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(updateScheduleEntity.getAdminModelEntity().getIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo(updateScheduleEntity.getModelSchedule());

        // verify
        verify(adminScheduleJpaRepository, times(1)).findById(updateScheduleEntity.getIdx());
        verify(adminScheduleJpaRepository, atLeastOnce()).findById(updateScheduleEntity.getIdx());
        verifyNoMoreInteractions(adminScheduleJpaRepository);

        InOrder inOrder = inOrder(adminScheduleJpaRepository);
        inOrder.verify(adminScheduleJpaRepository).findById(updateScheduleEntity.getIdx());
    }

    @Test
    @DisplayName("모델스케줄수정BDD테스트")
    void 모델스케줄수정BDD테스트() {
        // given
        AdminScheduleEntity updateScheduleEntity = AdminScheduleEntity.builder()
                .idx(adminScheduleEntity.getIdx())
                .modelSchedule("스케줄 수정 테스트")
                .adminModelEntity(adminModelEntity)
                .modelScheduleTime(adminScheduleEntity.getModelScheduleTime())
                .visible("Y")
                .build();

        // when
        given(adminScheduleJpaRepository.findById(updateScheduleEntity.getIdx())).willReturn(Optional.of(updateScheduleEntity));
        given(adminScheduleJpaRepository.save(updateScheduleEntity)).willReturn(updateScheduleEntity);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaService.updateSchedule(updateScheduleEntity.getIdx(), updateScheduleEntity);

        // then
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(updateScheduleEntity.getAdminModelEntity().getIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo(updateScheduleEntity.getModelSchedule());

        // verify
        then(adminScheduleJpaRepository).should(times(1)).findById(updateScheduleEntity.getIdx());
        then(adminScheduleJpaRepository).should(atLeastOnce()).findById(updateScheduleEntity.getIdx());
        then(adminScheduleJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델스케줄삭제Mockito테스트")
    void 모델스케줄삭제Mockito테스트() {
        // when
        Long deleteIdx = adminScheduleJpaService.deleteSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(adminScheduleEntity.getIdx()).isEqualTo(deleteIdx);
    }
}

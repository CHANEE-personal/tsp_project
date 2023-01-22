package com.tsp.api.festival.service;

import com.tsp.api.domain.festival.AdminFestivalDTO;
import com.tsp.api.domain.festival.AdminFestivalEntity;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
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
@DisplayName("행사 Service Test")
class AdminFestivalJpaServiceTest {
    @Mock
    private AdminFestivalJpaService mockAdminFestivalJpaService;
    private final AdminFestivalJpaService adminFestivalJpaService;
    private final EntityManager em;

    private AdminFestivalEntity adminFestivalEntity;
    private AdminFestivalDTO adminFestivalDTO;

    void createFestival() {
        // 등록
        LocalDateTime dateTime = LocalDateTime.now();

        adminFestivalEntity = AdminFestivalEntity.builder()
                .festivalTitle("축제 제목")
                .festivalDescription("축제 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        em.persist(adminFestivalEntity);

        adminFestivalDTO = AdminFestivalEntity.toDto(adminFestivalEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createFestival();
    }

    @Test
    @DisplayName("축제 리스트 조회 테스트")
    void 축제리스트조회테스트() {
        Map<String, Object> festivalMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        assertThat(adminFestivalJpaService.findFestivalList(festivalMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("축제 리스트 조회 Mockito 테스트")
    void 축제리스트조회Mockito테스트() {
        Map<String, Object> festivalMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminFestivalDTO> festivalList = new ArrayList<>();
        festivalList.add(adminFestivalDTO);
        Page<AdminFestivalDTO> resultFestival = new PageImpl<>(festivalList, pageRequest, festivalList.size());

        // when
        when(mockAdminFestivalJpaService.findFestivalList(festivalMap, pageRequest)).thenReturn(resultFestival);
        Page<AdminFestivalDTO> findFestivalList = mockAdminFestivalJpaService.findFestivalList(festivalMap, pageRequest);
        List<AdminFestivalDTO> newFestivalList = findFestivalList.stream().collect(Collectors.toList());

        // then
        assertThat(newFestivalList.get(0).getFestivalTitle()).isEqualTo("축제 제목");
        assertThat(newFestivalList.get(0).getFestivalDescription()).isEqualTo("축제 내용");

        // verify
        verify(mockAdminFestivalJpaService, times(1)).findFestivalList(festivalMap, pageRequest);
        verify(mockAdminFestivalJpaService, atLeastOnce()).findFestivalList(festivalMap, pageRequest);
        verifyNoMoreInteractions(mockAdminFestivalJpaService);

        InOrder inOrder = inOrder(mockAdminFestivalJpaService);
        inOrder.verify(mockAdminFestivalJpaService).findFestivalList(festivalMap, pageRequest);
    }

    @Test
    @DisplayName("축제 상세 조회 테스트")
    void 축제상세조회테스트() {
        AdminFestivalDTO oneFestival = adminFestivalJpaService.findOneFestival(adminFestivalDTO.getIdx());
        assertThat(oneFestival.getFestivalTitle()).isEqualTo("축제 제목");
        assertThat(oneFestival.getFestivalDescription()).isEqualTo("축제 내용");
    }

    @Test
    @DisplayName("축제 상세 조회 Mockito 테스트")
    void 축제상세조회Mockito테스트() {
        // when
        when(mockAdminFestivalJpaService.findOneFestival(adminFestivalDTO.getIdx())).thenReturn(adminFestivalDTO);
        AdminFestivalDTO oneFestival = mockAdminFestivalJpaService.findOneFestival(adminFestivalDTO.getIdx());

        // then
        assertThat(oneFestival.getFestivalTitle()).isEqualTo("축제 제목");
        assertThat(oneFestival.getFestivalDescription()).isEqualTo("축제 내용");


        // verify
        verify(mockAdminFestivalJpaService, times(1)).findOneFestival(adminFestivalDTO.getIdx());
        verify(mockAdminFestivalJpaService, atLeastOnce()).findOneFestival(adminFestivalDTO.getIdx());
        verifyNoMoreInteractions(mockAdminFestivalJpaService);

        InOrder inOrder = inOrder(mockAdminFestivalJpaService);
        inOrder.verify(mockAdminFestivalJpaService).findOneFestival(adminFestivalDTO.getIdx());
    }

    @Test
    @DisplayName("축제 등록 테스트")
    void 축제등록테스트() {
        // 등록
        LocalDateTime dateTime = LocalDateTime.now();

        AdminFestivalEntity insertFestivalEntity = AdminFestivalEntity.builder()
                .festivalTitle("축제 등록 제목")
                .festivalDescription("축제 등록 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        AdminFestivalDTO oneFestival = adminFestivalJpaService.insertFestival(insertFestivalEntity);
        assertThat(oneFestival.getFestivalTitle()).isEqualTo("축제 등록 제목");
        assertThat(oneFestival.getFestivalDescription()).isEqualTo("축제 등록 내용");
    }

    @Test
    @DisplayName("축제 등록 Mockito 테스트")
    void 축제등록Mockito테스트() {
        LocalDateTime dateTime = LocalDateTime.now();

        AdminFestivalEntity insertFestivalEntity = AdminFestivalEntity.builder()
                .festivalTitle("축제 등록 제목")
                .festivalDescription("축제 등록 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        AdminFestivalDTO insertFestivalDTO = AdminFestivalEntity.toDto(insertFestivalEntity);

        // when
        when(mockAdminFestivalJpaService.insertFestival(insertFestivalEntity)).thenReturn(insertFestivalDTO);
        AdminFestivalDTO oneFestival = mockAdminFestivalJpaService.insertFestival(insertFestivalEntity);

        // then
        assertThat(oneFestival.getIdx()).isEqualTo(insertFestivalDTO.getIdx());
        assertThat(oneFestival.getFestivalTitle()).isEqualTo("축제 등록 제목");
        assertThat(oneFestival.getFestivalDescription()).isEqualTo("축제 등록 내용");

        // verify
        verify(mockAdminFestivalJpaService, times(1)).insertFestival(insertFestivalEntity);
        verify(mockAdminFestivalJpaService, atLeastOnce()).insertFestival(insertFestivalEntity);
        verifyNoMoreInteractions(mockAdminFestivalJpaService);

        InOrder inOrder = inOrder(mockAdminFestivalJpaService);
        inOrder.verify(mockAdminFestivalJpaService).insertFestival(insertFestivalEntity);
    }

    @Test
    @DisplayName("축제 수정 테스트")
    void 축제수정테스트() {
        LocalDateTime dateTime = LocalDateTime.now();

        AdminFestivalEntity updateFestivalEntity = AdminFestivalEntity.builder()
                .idx(adminFestivalDTO.getIdx())
                .festivalTitle("축제 수정 제목")
                .festivalDescription("축제 수정 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        AdminFestivalDTO updateFestival = adminFestivalJpaService.updateFestival(adminFestivalEntity.getIdx(), updateFestivalEntity);
        assertThat(updateFestival.getFestivalTitle()).isEqualTo("축제 수정 제목");
        assertThat(updateFestival.getFestivalDescription()).isEqualTo("축제 수정 내용");
    }

    @Test
    @DisplayName("축제 수정 Mockito 테스트")
    void 축제수정Mockito테스트() {
        LocalDateTime dateTime = LocalDateTime.now();

        AdminFestivalEntity updateFestivalEntity = AdminFestivalEntity.builder()
                .idx(adminFestivalDTO.getIdx())
                .festivalTitle("축제 수정 제목")
                .festivalDescription("축제 수정 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        AdminFestivalDTO updateFestivalDTO = AdminFestivalEntity.toDto(updateFestivalEntity);

        // when
        when(mockAdminFestivalJpaService.updateFestival(adminFestivalDTO.getIdx(), updateFestivalEntity)).thenReturn(updateFestivalDTO);
        AdminFestivalDTO oneFestival = mockAdminFestivalJpaService.updateFestival(adminFestivalDTO.getIdx(), updateFestivalEntity);

        // then
        assertThat(oneFestival.getIdx()).isEqualTo(updateFestivalDTO.getIdx());
        assertThat(oneFestival.getFestivalTitle()).isEqualTo("축제 수정 제목");
        assertThat(oneFestival.getFestivalDescription()).isEqualTo("축제 수정 내용");

        // verify
        verify(mockAdminFestivalJpaService, times(1)).updateFestival(adminFestivalDTO.getIdx(), updateFestivalEntity);
        verify(mockAdminFestivalJpaService, atLeastOnce()).updateFestival(adminFestivalDTO.getIdx(), updateFestivalEntity);
        verifyNoMoreInteractions(mockAdminFestivalJpaService);

        InOrder inOrder = inOrder(mockAdminFestivalJpaService);
        inOrder.verify(mockAdminFestivalJpaService).updateFestival(adminFestivalDTO.getIdx(), updateFestivalEntity);
    }

    @Test
    @DisplayName("축제 삭제 테스트")
    void 축제삭제테스트() {
        Long deleteIdx = adminFestivalJpaService.deleteFestival(adminFestivalDTO.getIdx());
        assertThat(deleteIdx).isEqualTo(adminFestivalDTO.getIdx());
    }

    @Test
    @DisplayName("축제 삭제 Mockito 테스트")
    void 축제삭제Mockito테스트() {
        // when
        when(mockAdminFestivalJpaService.deleteFestival(adminFestivalDTO.getIdx())).thenReturn(adminFestivalDTO.getIdx());
        Long deleteIdx = mockAdminFestivalJpaService.deleteFestival(adminFestivalDTO.getIdx());

        // then
        assertThat(deleteIdx).isEqualTo(adminFestivalDTO.getIdx());

        // verify
        verify(mockAdminFestivalJpaService, times(1)).deleteFestival(adminFestivalDTO.getIdx());
        verify(mockAdminFestivalJpaService, atLeastOnce()).deleteFestival(adminFestivalDTO.getIdx());
        verifyNoMoreInteractions(mockAdminFestivalJpaService);

        InOrder inOrder = inOrder(mockAdminFestivalJpaService);
        inOrder.verify(mockAdminFestivalJpaService).deleteFestival(adminFestivalDTO.getIdx());
    }
}

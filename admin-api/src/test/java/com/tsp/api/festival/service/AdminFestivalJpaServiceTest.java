package com.tsp.api.festival.service;

import com.tsp.api.festival.domain.AdminFestivalDTO;
import com.tsp.api.festival.domain.AdminFestivalEntity;
import com.tsp.api.model.service.AdminModelCommonServiceTest;
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

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;
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
class AdminFestivalJpaServiceTest extends AdminModelCommonServiceTest {

    @Mock private AdminFestivalJpaRepository adminFestivalJpaRepository;
    @Mock private AdminFestivalJpaQueryRepository adminFestivalJpaQueryRepository;
    @InjectMocks private AdminFestivalJpaServiceImpl mockAdminFestivalJpaService;
    private final AdminFestivalJpaService adminFestivalJpaService;

    @Test
    @DisplayName("축제 리스트 조회 테스트")
    void 축제리스트조회테스트() {
        Map<String, Object> festivalMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        assertThat(adminFestivalJpaService.findFestivalList(festivalMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("축제 리스트 조회 Mockito 테스트")
    void 축제리스트조회Mockito테스트() {
        Map<String, Object> festivalMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<AdminFestivalDTO> festivalList = new ArrayList<>();
        festivalList.add(adminFestivalDTO);
        Page<AdminFestivalDTO> resultFestival = new PageImpl<>(festivalList, pageRequest, festivalList.size());

        // when
        when(adminFestivalJpaQueryRepository.findFestivalList(festivalMap, pageRequest)).thenReturn(resultFestival);
        Page<AdminFestivalDTO> findFestivalList = mockAdminFestivalJpaService.findFestivalList(festivalMap, pageRequest);
        List<AdminFestivalDTO> newFestivalList = findFestivalList.stream().collect(Collectors.toList());

        // then
        assertThat(newFestivalList.get(0).getFestivalTitle()).isEqualTo("축제 제목");
        assertThat(newFestivalList.get(0).getFestivalDescription()).isEqualTo("축제 내용");

        // verify
        verify(adminFestivalJpaQueryRepository, times(1)).findFestivalList(festivalMap, pageRequest);
        verify(adminFestivalJpaQueryRepository, atLeastOnce()).findFestivalList(festivalMap, pageRequest);
        verifyNoMoreInteractions(adminFestivalJpaQueryRepository);

        InOrder inOrder = inOrder(adminFestivalJpaQueryRepository);
        inOrder.verify(adminFestivalJpaQueryRepository).findFestivalList(festivalMap, pageRequest);
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
        when(adminFestivalJpaRepository.findById(adminFestivalEntity.getIdx())).thenReturn(Optional.ofNullable(adminFestivalEntity));
        AdminFestivalDTO oneFestival = mockAdminFestivalJpaService.findOneFestival(adminFestivalEntity.getIdx());

        // then
        assertThat(oneFestival.getFestivalTitle()).isEqualTo(adminFestivalEntity.getFestivalTitle());
        assertThat(oneFestival.getFestivalDescription()).isEqualTo(adminFestivalEntity.getFestivalDescription());


        // verify
        verify(adminFestivalJpaRepository, times(1)).findById(adminFestivalEntity.getIdx());
        verify(adminFestivalJpaRepository, atLeastOnce()).findById(adminFestivalEntity.getIdx());
        verifyNoMoreInteractions(adminFestivalJpaRepository);

        InOrder inOrder = inOrder(adminFestivalJpaRepository);
        inOrder.verify(adminFestivalJpaRepository).findById(adminFestivalEntity.getIdx());
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

        // when
        when(adminFestivalJpaRepository.save(insertFestivalEntity)).thenReturn(insertFestivalEntity);
        AdminFestivalDTO oneFestival = mockAdminFestivalJpaService.insertFestival(insertFestivalEntity);

        // then
        assertThat(oneFestival.getIdx()).isEqualTo(insertFestivalEntity.getIdx());
        assertThat(oneFestival.getFestivalTitle()).isEqualTo(insertFestivalEntity.getFestivalTitle());
        assertThat(oneFestival.getFestivalDescription()).isEqualTo(insertFestivalEntity.getFestivalDescription());

        // verify
        verify(adminFestivalJpaRepository, times(1)).save(insertFestivalEntity);
        verify(adminFestivalJpaRepository, atLeastOnce()).save(insertFestivalEntity);
        verifyNoMoreInteractions(adminFestivalJpaRepository);

        InOrder inOrder = inOrder(adminFestivalJpaRepository);
        inOrder.verify(adminFestivalJpaRepository).save(insertFestivalEntity);
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
                .idx(adminFestivalEntity.getIdx())
                .festivalTitle("축제 수정 제목")
                .festivalDescription("축제 수정 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        // when
        when(adminFestivalJpaRepository.findById(updateFestivalEntity.getIdx())).thenReturn(Optional.of(updateFestivalEntity));
        when(adminFestivalJpaRepository.save(updateFestivalEntity)).thenReturn(updateFestivalEntity);
        AdminFestivalDTO oneFestival = mockAdminFestivalJpaService.updateFestival(updateFestivalEntity.getIdx(), updateFestivalEntity);

        // then
        assertThat(oneFestival.getIdx()).isEqualTo(updateFestivalEntity.getIdx());
        assertThat(oneFestival.getFestivalTitle()).isEqualTo(updateFestivalEntity.getFestivalTitle());
        assertThat(oneFestival.getFestivalDescription()).isEqualTo(updateFestivalEntity.getFestivalDescription());

        // verify
        verify(adminFestivalJpaRepository, times(1)).findById(updateFestivalEntity.getIdx());
        verify(adminFestivalJpaRepository, atLeastOnce()).findById(updateFestivalEntity.getIdx());
        verifyNoMoreInteractions(adminFestivalJpaRepository);

        InOrder inOrder = inOrder(adminFestivalJpaRepository);
        inOrder.verify(adminFestivalJpaRepository).findById(updateFestivalEntity.getIdx());
    }

    @Test
    @DisplayName("축제 삭제 테스트")
    void 축제삭제테스트() {
        Long deleteIdx = adminFestivalJpaService.deleteFestival(adminFestivalDTO.getIdx());
        assertThat(deleteIdx).isEqualTo(adminFestivalDTO.getIdx());
    }
}

package com.tsp.api.festival.service;

import com.tsp.api.festival.domain.AdminFestivalDto;
import com.tsp.api.festival.domain.AdminFestivalEntity;
import lombok.RequiredArgsConstructor;
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

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("모델 Repository Test")
class AdminFestivalJpaQueryRepositoryTest {

    @Mock
    private AdminFestivalJpaQueryRepository mockAdminFestivalJpaQueryRepository;
    private final AdminFestivalJpaQueryRepository adminFestivalJpaQueryRepository;
    private final EntityManager em;

    private AdminFestivalEntity adminFestivalEntity;
    private AdminFestivalDto adminFestivalDTO;

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

        assertThat(adminFestivalJpaQueryRepository.findFestivalList(festivalMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("축제 리스트 조회 Mockito 테스트")
    void 축제리스트조회Mockito테스트() {
        Map<String, Object> festivalMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminFestivalDto> festivalList = new ArrayList<>();
        festivalList.add(adminFestivalDTO);
        Page<AdminFestivalDto> resultFestival = new PageImpl<>(festivalList, pageRequest, festivalList.size());

        // when
        when(mockAdminFestivalJpaQueryRepository.findFestivalList(festivalMap, pageRequest)).thenReturn(resultFestival);
        Page<AdminFestivalDto> findFestivalList = mockAdminFestivalJpaQueryRepository.findFestivalList(festivalMap, pageRequest);
        List<AdminFestivalDto> newFestivalList = findFestivalList.stream().collect(Collectors.toList());

        // then
        assertThat(newFestivalList.get(0).getFestivalTitle()).isEqualTo("축제 제목");
        assertThat(newFestivalList.get(0).getFestivalDescription()).isEqualTo("축제 내용");

        // verify
        verify(mockAdminFestivalJpaQueryRepository, times(1)).findFestivalList(festivalMap, pageRequest);
        verify(mockAdminFestivalJpaQueryRepository, atLeastOnce()).findFestivalList(festivalMap, pageRequest);
        verifyNoMoreInteractions(mockAdminFestivalJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminFestivalJpaQueryRepository);
        inOrder.verify(mockAdminFestivalJpaQueryRepository).findFestivalList(festivalMap, pageRequest);
    }
}

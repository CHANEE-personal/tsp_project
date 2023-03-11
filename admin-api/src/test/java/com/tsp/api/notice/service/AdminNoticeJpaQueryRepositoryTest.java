package com.tsp.api.notice.service;

import com.tsp.api.notice.domain.AdminNoticeDto;
import com.tsp.api.notice.domain.AdminNoticeEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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
@DisplayName("공지사항 Repository Test")
class AdminNoticeJpaQueryRepositoryTest {

    @Mock
    private AdminNoticeJpaQueryRepository mockAdminNoticeJpaQueryRepository;
    private final AdminNoticeJpaQueryRepository adminNoticeJpaQueryRepository;

    private AdminNoticeEntity adminNoticeEntity;
    private AdminNoticeDto adminNoticeDTO;

    void createNotice() {
        adminNoticeEntity = AdminNoticeEntity.builder()
                .title("공지사항 테스트")
                .description("공지사항 테스트")
                .visible("Y")
                .topFixed(false)
                .viewCount(1)
                .build();

        adminNoticeDTO = AdminNoticeEntity.toDto(adminNoticeEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createNotice();
    }

    @Test
    @Disabled
    @DisplayName("공지사항리스트조회테스트")
    void 공지사항리스트조회테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 100);

        // then
        assertThat(adminNoticeJpaQueryRepository.findNoticeList(noticeMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("공지사항Mockito조회테스트")
    void 공지사항Mockito조회테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminNoticeDto> noticeList = new ArrayList<>();
        noticeList.add(AdminNoticeDto.builder().idx(1L).title("공지사항 테스트")
                .description("공지사항 테스트").build());

        Page<AdminNoticeDto> resultNotice = new PageImpl<>(noticeList, pageRequest, noticeList.size());

        // when
        when(mockAdminNoticeJpaQueryRepository.findNoticeList(noticeMap, pageRequest)).thenReturn(resultNotice);
        Page<AdminNoticeDto> newNoticeList = mockAdminNoticeJpaQueryRepository.findNoticeList(noticeMap, pageRequest);
        List<AdminNoticeDto> findNoticeList = newNoticeList.stream().collect(Collectors.toList());

        // then
        assertThat(findNoticeList.get(0).getIdx()).isEqualTo(noticeList.get(0).getIdx());
        assertThat(findNoticeList.get(0).getTitle()).isEqualTo(noticeList.get(0).getTitle());
        assertThat(findNoticeList.get(0).getDescription()).isEqualTo(noticeList.get(0).getDescription());
        assertThat(findNoticeList.get(0).getVisible()).isEqualTo(noticeList.get(0).getVisible());

        // verify
        verify(mockAdminNoticeJpaQueryRepository, times(1)).findNoticeList(noticeMap, pageRequest);
        verify(mockAdminNoticeJpaQueryRepository, atLeastOnce()).findNoticeList(noticeMap, pageRequest);
        verifyNoMoreInteractions(mockAdminNoticeJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminNoticeJpaQueryRepository);
        inOrder.verify(mockAdminNoticeJpaQueryRepository).findNoticeList(noticeMap, pageRequest);
    }

    @Test
    @DisplayName("공지사항BDD조회테스트")
    void 공지사항BDD조회테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminNoticeDto> noticeList = new ArrayList<>();
        noticeList.add(AdminNoticeDto.builder().idx(1L).title("공지사항 테스트")
                .description("공지사항 테스트").build());

        Page<AdminNoticeDto> resultNotice = new PageImpl<>(noticeList, pageRequest, noticeList.size());

        // when
        given(mockAdminNoticeJpaQueryRepository.findNoticeList(noticeMap, pageRequest)).willReturn(resultNotice);
        Page<AdminNoticeDto> newNoticeList = mockAdminNoticeJpaQueryRepository.findNoticeList(noticeMap, pageRequest);
        List<AdminNoticeDto> findNoticeList = newNoticeList.stream().collect(Collectors.toList());

        // then
        assertThat(findNoticeList.get(0).getIdx()).isEqualTo(noticeList.get(0).getIdx());
        assertThat(findNoticeList.get(0).getTitle()).isEqualTo(noticeList.get(0).getTitle());
        assertThat(findNoticeList.get(0).getDescription()).isEqualTo(noticeList.get(0).getDescription());
        assertThat(findNoticeList.get(0).getVisible()).isEqualTo(noticeList.get(0).getVisible());

        // verify
        then(mockAdminNoticeJpaQueryRepository).should(times(1)).findNoticeList(noticeMap, pageRequest);
        then(mockAdminNoticeJpaQueryRepository).should(atLeastOnce()).findNoticeList(noticeMap, pageRequest);
        then(mockAdminNoticeJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 공지사항 상세 조회 테스트")
    void 이전or다음공지사항상세조회테스트() {
        // given
        adminNoticeEntity = AdminNoticeEntity.builder().idx(2L).build();

        // 이전 공지사항
        assertThat(adminNoticeJpaQueryRepository.findPrevOneNotice(adminNoticeEntity.getIdx()).getIdx()).isEqualTo(1);
        // 다음 공지사항
        assertThat(adminNoticeJpaQueryRepository.findNextOneNotice(adminNoticeEntity.getIdx()).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 공지사항 상세 조회 Mockito 테스트")
    void 이전공지사항상세조회Mockito테스트() {
        // given
        adminNoticeEntity = AdminNoticeEntity.builder().idx(2L).build();

        // when
        adminNoticeDTO = adminNoticeJpaQueryRepository.findPrevOneNotice(adminNoticeEntity.getIdx());

        when(mockAdminNoticeJpaQueryRepository.findPrevOneNotice(adminNoticeEntity.getIdx())).thenReturn(adminNoticeDTO);
        AdminNoticeDto noticeInfo = mockAdminNoticeJpaQueryRepository.findPrevOneNotice(adminNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockAdminNoticeJpaQueryRepository, times(1)).findPrevOneNotice(adminNoticeEntity.getIdx());
        verify(mockAdminNoticeJpaQueryRepository, atLeastOnce()).findPrevOneNotice(adminNoticeEntity.getIdx());
        verifyNoMoreInteractions(mockAdminNoticeJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminNoticeJpaQueryRepository);
        inOrder.verify(mockAdminNoticeJpaQueryRepository).findPrevOneNotice(adminNoticeEntity.getIdx());
    }

    @Test
    @DisplayName("이전 공지사항 상세 조회 BDD 테스트")
    void 이전공지사항상세조회BDD테스트() {
        // given
        adminNoticeEntity = AdminNoticeEntity.builder().idx(2L).build();

        // when
        adminNoticeDTO = adminNoticeJpaQueryRepository.findPrevOneNotice(adminNoticeEntity.getIdx());

        given(mockAdminNoticeJpaQueryRepository.findPrevOneNotice(adminNoticeEntity.getIdx())).willReturn(adminNoticeDTO);
        AdminNoticeDto noticeInfo = mockAdminNoticeJpaQueryRepository.findPrevOneNotice(adminNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockAdminNoticeJpaQueryRepository).should(times(1)).findPrevOneNotice(adminNoticeEntity.getIdx());
        then(mockAdminNoticeJpaQueryRepository).should(atLeastOnce()).findPrevOneNotice(adminNoticeEntity.getIdx());
        then(mockAdminNoticeJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 공지사항 상세 조회 Mockito 테스트")
    void 다음공지사항상세조회Mockito테스트() {
        // given
        adminNoticeEntity = AdminNoticeEntity.builder().idx(2L).build();

        // when
        adminNoticeDTO = adminNoticeJpaQueryRepository.findNextOneNotice(adminNoticeEntity.getIdx());

        when(mockAdminNoticeJpaQueryRepository.findNextOneNotice(adminNoticeEntity.getIdx())).thenReturn(adminNoticeDTO);
        AdminNoticeDto noticeInfo = mockAdminNoticeJpaQueryRepository.findNextOneNotice(adminNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockAdminNoticeJpaQueryRepository, times(1)).findNextOneNotice(adminNoticeEntity.getIdx());
        verify(mockAdminNoticeJpaQueryRepository, atLeastOnce()).findNextOneNotice(adminNoticeEntity.getIdx());
        verifyNoMoreInteractions(mockAdminNoticeJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminNoticeJpaQueryRepository);
        inOrder.verify(mockAdminNoticeJpaQueryRepository).findNextOneNotice(adminNoticeEntity.getIdx());
    }

    @Test
    @DisplayName("다음 공지사항 상세 조회 BDD 테스트")
    void 다음공지사항상세조회BDD테스트() {
        // given
        adminNoticeEntity = AdminNoticeEntity.builder().idx(2L).build();

        // when
        adminNoticeDTO = adminNoticeJpaQueryRepository.findNextOneNotice(adminNoticeEntity.getIdx());

        given(mockAdminNoticeJpaQueryRepository.findNextOneNotice(adminNoticeEntity.getIdx())).willReturn(adminNoticeDTO);
        AdminNoticeDto noticeInfo = mockAdminNoticeJpaQueryRepository.findNextOneNotice(adminNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockAdminNoticeJpaQueryRepository).should(times(1)).findNextOneNotice(adminNoticeEntity.getIdx());
        then(mockAdminNoticeJpaQueryRepository).should(atLeastOnce()).findNextOneNotice(adminNoticeEntity.getIdx());
        then(mockAdminNoticeJpaQueryRepository).shouldHaveNoMoreInteractions();
    }
}

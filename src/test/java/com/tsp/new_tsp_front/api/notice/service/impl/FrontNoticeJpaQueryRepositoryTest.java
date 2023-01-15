package com.tsp.new_tsp_front.api.notice.service.impl;

import com.tsp.new_tsp_front.api.notice.domain.FrontNoticeDTO;
import com.tsp.new_tsp_front.api.notice.domain.FrontNoticeEntity;
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
@TestPropertySource(locations = "classpath:application-local.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("공지사항 Repository Test")
class FrontNoticeJpaQueryRepositoryTest {

    @Mock private final FrontNoticeJpaQueryRepository mockFrontNoticeJpaQueryRepository;
    private final FrontNoticeJpaQueryRepository frontNoticeJpaQueryRepository;
    private FrontNoticeEntity frontNoticeEntity;
    private FrontNoticeDTO frontNoticeDTO;

    private void createNotice() {
        frontNoticeEntity = FrontNoticeEntity.builder()
                .title("공지사항 테스트")
                .description("공지사항 테스트")
                .visible("Y")
                .viewCount(1)
                .build();

        frontNoticeDTO = FrontNoticeEntity.toDto(frontNoticeEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createNotice();
    }

    @Test
    @DisplayName("공지사항 리스트 조회 테스트")
    void 공지사항리스트조회테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("searchType", 0);
        noticeMap.put("searchKeyword", "하하");
        PageRequest pageRequest = PageRequest.of(1, 3);

        // then
        assertThat(frontNoticeJpaQueryRepository.findNoticeList(noticeMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("공지사항 리스트 조회 Mockito 테스트")
    void 공지사항리스트조회Mockito테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontNoticeDTO> noticeList = new ArrayList<>();
        noticeList.add(FrontNoticeDTO.builder().idx(1L).title("공지사항").description("공지사항").visible("Y").build());
        Page<FrontNoticeDTO> resultNotice = new PageImpl<>(noticeList, pageRequest, noticeList.size());

        // when
        when(mockFrontNoticeJpaQueryRepository.findNoticeList(noticeMap, pageRequest)).thenReturn(resultNotice);
        Page<FrontNoticeDTO> newNoticeList = mockFrontNoticeJpaQueryRepository.findNoticeList(noticeMap, pageRequest);
        List<FrontNoticeDTO> findNoticeList = newNoticeList.stream().collect(Collectors.toList());

        // then
        assertThat(findNoticeList.get(0).getIdx()).isEqualTo(noticeList.get(0).getIdx());
        assertThat(findNoticeList.get(0).getTitle()).isEqualTo(noticeList.get(0).getTitle());
        assertThat(findNoticeList.get(0).getDescription()).isEqualTo(noticeList.get(0).getDescription());

        // verify
        verify(mockFrontNoticeJpaQueryRepository, times(1)).findNoticeList(noticeMap, pageRequest);
        verify(mockFrontNoticeJpaQueryRepository, atLeastOnce()).findNoticeList(noticeMap, pageRequest);
        verifyNoMoreInteractions(mockFrontNoticeJpaQueryRepository);

        InOrder inOrder = inOrder(mockFrontNoticeJpaQueryRepository);
        inOrder.verify(mockFrontNoticeJpaQueryRepository).findNoticeList(noticeMap, pageRequest);
    }

    @Test
    @DisplayName("공지사항 리스트 조회 BDD 테스트")
    void 공지사항리스트조회BDD테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontNoticeDTO> noticeList = new ArrayList<>();
        noticeList.add(FrontNoticeDTO.builder().idx(1L).title("공지사항").description("공지사항").visible("Y").build());
        Page<FrontNoticeDTO> resultNotice = new PageImpl<>(noticeList, pageRequest, noticeList.size());

        // when
        given(mockFrontNoticeJpaQueryRepository.findNoticeList(noticeMap, pageRequest)).willReturn(resultNotice);
        Page<FrontNoticeDTO> newNoticeList = mockFrontNoticeJpaQueryRepository.findNoticeList(noticeMap, pageRequest);
        List<FrontNoticeDTO> findNoticeList = newNoticeList.stream().collect(Collectors.toList());

        // then
        assertThat(findNoticeList.get(0).getIdx()).isEqualTo(noticeList.get(0).getIdx());
        assertThat(findNoticeList.get(0).getTitle()).isEqualTo(noticeList.get(0).getTitle());
        assertThat(findNoticeList.get(0).getDescription()).isEqualTo(noticeList.get(0).getDescription());

        // verify
        then(mockFrontNoticeJpaQueryRepository).should(times(1)).findNoticeList(noticeMap, pageRequest);
        then(mockFrontNoticeJpaQueryRepository).should(atLeastOnce()).findNoticeList(noticeMap, pageRequest);
        then(mockFrontNoticeJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 공지사항 상세 조회 Mockito 테스트")
    void 이전공지사항상세조회Mockito테스트() {
        // given
        frontNoticeEntity = FrontNoticeEntity.builder().idx(2L).build();

        // when
        frontNoticeDTO = frontNoticeJpaQueryRepository.findPrevOneNotice(frontNoticeEntity.getIdx());

        when(mockFrontNoticeJpaQueryRepository.findPrevOneNotice(frontNoticeEntity.getIdx())).thenReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaQueryRepository.findPrevOneNotice(frontNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockFrontNoticeJpaQueryRepository, times(1)).findPrevOneNotice(frontNoticeEntity.getIdx());
        verify(mockFrontNoticeJpaQueryRepository, atLeastOnce()).findPrevOneNotice(frontNoticeEntity.getIdx());
        verifyNoMoreInteractions(mockFrontNoticeJpaQueryRepository);

        InOrder inOrder = inOrder(mockFrontNoticeJpaQueryRepository);
        inOrder.verify(mockFrontNoticeJpaQueryRepository).findPrevOneNotice(frontNoticeEntity.getIdx());
    }

    @Test
    @DisplayName("이전 공지사항 상세 조회 BDD 테스트")
    void 이전공지사항상세조회BDD테스트() {
        // given
        frontNoticeEntity = FrontNoticeEntity.builder().idx(2L).build();

        // when
        frontNoticeDTO = frontNoticeJpaQueryRepository.findPrevOneNotice(frontNoticeEntity.getIdx());

        given(mockFrontNoticeJpaQueryRepository.findPrevOneNotice(frontNoticeEntity.getIdx())).willReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaQueryRepository.findPrevOneNotice(frontNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockFrontNoticeJpaQueryRepository).should(times(1)).findPrevOneNotice(frontNoticeEntity.getIdx());
        then(mockFrontNoticeJpaQueryRepository).should(atLeastOnce()).findPrevOneNotice(frontNoticeEntity.getIdx());
        then(mockFrontNoticeJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 공지사항 상세 조회 Mockito 테스트")
    void 다음공지사항상세조회Mockito테스트() {
        // given
        frontNoticeEntity = FrontNoticeEntity.builder().idx(2L).build();

        // when
        frontNoticeDTO = frontNoticeJpaQueryRepository.findNextOneNotice(frontNoticeEntity.getIdx());

        when(mockFrontNoticeJpaQueryRepository.findNextOneNotice(frontNoticeEntity.getIdx())).thenReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaQueryRepository.findNextOneNotice(frontNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockFrontNoticeJpaQueryRepository, times(1)).findNextOneNotice(frontNoticeEntity.getIdx());
        verify(mockFrontNoticeJpaQueryRepository, atLeastOnce()).findNextOneNotice(frontNoticeEntity.getIdx());
        verifyNoMoreInteractions(mockFrontNoticeJpaQueryRepository);

        InOrder inOrder = inOrder(mockFrontNoticeJpaQueryRepository);
        inOrder.verify(mockFrontNoticeJpaQueryRepository).findNextOneNotice(frontNoticeEntity.getIdx());
    }

    @Test
    @DisplayName("다음 공지사항 상세 조회 BDD 테스트")
    void 다음공지사항상세조회BDD테스트() {
        // given
        frontNoticeEntity = FrontNoticeEntity.builder().idx(2L).build();

        // when
        frontNoticeDTO = frontNoticeJpaQueryRepository.findNextOneNotice(frontNoticeEntity.getIdx());

        given(mockFrontNoticeJpaQueryRepository.findNextOneNotice(frontNoticeEntity.getIdx())).willReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaQueryRepository.findNextOneNotice(frontNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockFrontNoticeJpaQueryRepository).should(times(1)).findNextOneNotice(frontNoticeEntity.getIdx());
        then(mockFrontNoticeJpaQueryRepository).should(atLeastOnce()).findNextOneNotice(frontNoticeEntity.getIdx());
        then(mockFrontNoticeJpaQueryRepository).shouldHaveNoMoreInteractions();
    }
}
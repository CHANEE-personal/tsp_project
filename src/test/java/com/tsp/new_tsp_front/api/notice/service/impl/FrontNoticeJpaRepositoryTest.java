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
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
class FrontNoticeJpaRepositoryTest {

    @Mock private final FrontNoticeJpaRepository mockFrontNoticeJpaRepository;
    private final FrontNoticeJpaRepository frontNoticeJpaRepository;
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
        noticeMap.put("jpaStartPage", 0);
        noticeMap.put("size", 3);
        noticeMap.put("searchType", 0);
        noticeMap.put("searchKeyword", "하하");

        // then
        assertThat(frontNoticeJpaRepository.findNoticeList(noticeMap)).isNotEmpty();
    }

    @Test
    @DisplayName("공지사항 리스트 조회 Mockito 테스트")
    void 공지사항리스트조회Mockito테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("jpaStartPage", 1);
        noticeMap.put("size", 3);

        List<FrontNoticeDTO> noticeList = new ArrayList<>();
        noticeList.add(FrontNoticeDTO.builder().idx(1L).title("공지사항").description("공지사항").visible("Y").build());

        // when
        when(mockFrontNoticeJpaRepository.findNoticeList(noticeMap)).thenReturn(noticeList);
        List<FrontNoticeDTO> newNoticeList = mockFrontNoticeJpaRepository.findNoticeList(noticeMap);

        // then
        assertThat(newNoticeList.get(0).getIdx()).isEqualTo(noticeList.get(0).getIdx());
        assertThat(newNoticeList.get(0).getTitle()).isEqualTo(noticeList.get(0).getTitle());
        assertThat(newNoticeList.get(0).getDescription()).isEqualTo(noticeList.get(0).getDescription());

        // verify
        verify(mockFrontNoticeJpaRepository, times(1)).findNoticeList(noticeMap);
        verify(mockFrontNoticeJpaRepository, atLeastOnce()).findNoticeList(noticeMap);
        verifyNoMoreInteractions(mockFrontNoticeJpaRepository);

        InOrder inOrder = inOrder(mockFrontNoticeJpaRepository);
        inOrder.verify(mockFrontNoticeJpaRepository).findNoticeList(noticeMap);
    }

    @Test
    @DisplayName("공지사항 리스트 조회 BDD 테스트")
    void 공지사항리스트조회BDD테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("jpaStartPage", 1);
        noticeMap.put("size", 3);

        List<FrontNoticeDTO> noticeList = new ArrayList<>();
        noticeList.add(FrontNoticeDTO.builder().idx(1L).title("공지사항").description("공지사항").visible("Y").build());

        // when
        given(mockFrontNoticeJpaRepository.findNoticeList(noticeMap)).willReturn(noticeList);
        List<FrontNoticeDTO> newNoticeList = mockFrontNoticeJpaRepository.findNoticeList(noticeMap);

        // then
        assertThat(newNoticeList.get(0).getIdx()).isEqualTo(noticeList.get(0).getIdx());
        assertThat(newNoticeList.get(0).getTitle()).isEqualTo(noticeList.get(0).getTitle());
        assertThat(newNoticeList.get(0).getDescription()).isEqualTo(noticeList.get(0).getDescription());

        // verify
        then(mockFrontNoticeJpaRepository).should(times(1)).findNoticeList(noticeMap);
        then(mockFrontNoticeJpaRepository).should(atLeastOnce()).findNoticeList(noticeMap);
        then(mockFrontNoticeJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("상단 고정 공지사항 리스트 조회 Mockito 테스트")
    void 상단고정공지사항리스트조회Mockito테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("jpaStartPage", 1);
        noticeMap.put("size", 3);
        noticeMap.put("topFixed", true);

        List<FrontNoticeDTO> noticeList = new ArrayList<>();
        noticeList.add(FrontNoticeDTO.builder().idx(1L).title("공지사항").description("공지사항").visible("Y").topFixed(Boolean.TRUE.toString()).build());

        // when
        when(mockFrontNoticeJpaRepository.findNoticeList(noticeMap)).thenReturn(noticeList);
        List<FrontNoticeDTO> newNoticeList = mockFrontNoticeJpaRepository.findNoticeList(noticeMap);

        // then
        assertThat(newNoticeList.get(0).getIdx()).isEqualTo(noticeList.get(0).getIdx());
        assertThat(newNoticeList.get(0).getTitle()).isEqualTo(noticeList.get(0).getTitle());
        assertThat(newNoticeList.get(0).getDescription()).isEqualTo(noticeList.get(0).getDescription());
        assertThat(newNoticeList.get(0).getTopFixed()).isEqualTo(noticeList.get(0).getTopFixed());

        // verify
        verify(mockFrontNoticeJpaRepository, times(1)).findNoticeList(noticeMap);
        verify(mockFrontNoticeJpaRepository, atLeastOnce()).findNoticeList(noticeMap);
        verifyNoMoreInteractions(mockFrontNoticeJpaRepository);

        InOrder inOrder = inOrder(mockFrontNoticeJpaRepository);
        inOrder.verify(mockFrontNoticeJpaRepository).findNoticeList(noticeMap);
    }

    @Test
    @DisplayName("상단 고정 공지사항 리스트 조회 BDD 테스트")
    void 상단고정공지사항리스트조회BDD테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("jpaStartPage", 1);
        noticeMap.put("size", 3);
        noticeMap.put("topFixed", true);

        List<FrontNoticeDTO> noticeList = new ArrayList<>();
        noticeList.add(FrontNoticeDTO.builder().idx(1L).title("공지사항").description("공지사항").visible("Y").topFixed(Boolean.TRUE.toString()).build());

        // when
        given(mockFrontNoticeJpaRepository.findNoticeList(noticeMap)).willReturn(noticeList);
        List<FrontNoticeDTO> newNoticeList = mockFrontNoticeJpaRepository.findNoticeList(noticeMap);

        // then
        assertThat(newNoticeList.get(0).getIdx()).isEqualTo(noticeList.get(0).getIdx());
        assertThat(newNoticeList.get(0).getTitle()).isEqualTo(noticeList.get(0).getTitle());
        assertThat(newNoticeList.get(0).getDescription()).isEqualTo(noticeList.get(0).getDescription());
        assertThat(newNoticeList.get(0).getTopFixed()).isEqualTo(noticeList.get(0).getTopFixed());

        // verify
        then(mockFrontNoticeJpaRepository).should(times(1)).findNoticeList(noticeMap);
        then(mockFrontNoticeJpaRepository).should(atLeastOnce()).findNoticeList(noticeMap);
        then(mockFrontNoticeJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공지사항상세Mockito조회테스트")
    void 공지사항상세Mockito조회테스트() {
        // given
        FrontNoticeEntity frontNoticeEntity = FrontNoticeEntity.builder()
                .idx(1L)
                .title("공지사항 테스트")
                .description("공지사항 테스트")
                .visible("Y")
                .build();

        frontNoticeDTO = FrontNoticeEntity.toDto(frontNoticeEntity);

        // when
        when(mockFrontNoticeJpaRepository.findOneNotice(frontNoticeEntity.getIdx())).thenReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaRepository.findOneNotice(frontNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);
        assertThat(noticeInfo.getTitle()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getDescription()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockFrontNoticeJpaRepository, times(1)).findOneNotice(frontNoticeEntity.getIdx());
        verify(mockFrontNoticeJpaRepository, atLeastOnce()).findOneNotice(frontNoticeEntity.getIdx());
        verifyNoMoreInteractions(mockFrontNoticeJpaRepository);

        InOrder inOrder = inOrder(mockFrontNoticeJpaRepository);
        inOrder.verify(mockFrontNoticeJpaRepository).findOneNotice(frontNoticeEntity.getIdx());
    }

    @Test
    @DisplayName("공지사항상세BDD조회테스트")
    void 공지사항상세BDD조회테스트() {
        // given
        FrontNoticeEntity frontNoticeEntity = FrontNoticeEntity.builder()
                .idx(1L)
                .title("공지사항 테스트")
                .description("공지사항 테스트")
                .visible("Y")
                .build();

        frontNoticeDTO = FrontNoticeEntity.toDto(frontNoticeEntity);

        // when
        given(mockFrontNoticeJpaRepository.findOneNotice(frontNoticeEntity.getIdx())).willReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaRepository.findOneNotice(frontNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);
        assertThat(noticeInfo.getTitle()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getDescription()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockFrontNoticeJpaRepository).should(times(1)).findOneNotice(frontNoticeEntity.getIdx());
        then(mockFrontNoticeJpaRepository).should(atLeastOnce()).findOneNotice(frontNoticeEntity.getIdx());
        then(mockFrontNoticeJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 공지사항 상세 조회 테스트")
    void 이전or다음공지사항상세조회테스트() {
        // given
        frontNoticeEntity = FrontNoticeEntity.builder().idx(2L).build();

        // when
        frontNoticeDTO = frontNoticeJpaRepository.findOneNotice(frontNoticeEntity.getIdx());

        // 이전 프로덕션
        assertThat(frontNoticeJpaRepository.findPrevOneNotice(frontNoticeEntity.getIdx()).getIdx()).isEqualTo(1);
        // 다음 프로덕션
        assertThat(frontNoticeJpaRepository.findNextOneNotice(frontNoticeEntity.getIdx()).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 공지사항 상세 조회 Mockito 테스트")
    void 이전공지사항상세조회Mockito테스트() {
        // given
        frontNoticeEntity = FrontNoticeEntity.builder().idx(2L).build();

        // when
        frontNoticeDTO = frontNoticeJpaRepository.findPrevOneNotice(frontNoticeEntity.getIdx());

        when(mockFrontNoticeJpaRepository.findPrevOneNotice(frontNoticeEntity.getIdx())).thenReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaRepository.findPrevOneNotice(frontNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockFrontNoticeJpaRepository, times(1)).findPrevOneNotice(frontNoticeEntity.getIdx());
        verify(mockFrontNoticeJpaRepository, atLeastOnce()).findPrevOneNotice(frontNoticeEntity.getIdx());
        verifyNoMoreInteractions(mockFrontNoticeJpaRepository);

        InOrder inOrder = inOrder(mockFrontNoticeJpaRepository);
        inOrder.verify(mockFrontNoticeJpaRepository).findPrevOneNotice(frontNoticeEntity.getIdx());
    }

    @Test
    @DisplayName("이전 공지사항 상세 조회 BDD 테스트")
    void 이전공지사항상세조회BDD테스트() {
        // given
        frontNoticeEntity = FrontNoticeEntity.builder().idx(2L).build();

        // when
        frontNoticeDTO = frontNoticeJpaRepository.findPrevOneNotice(frontNoticeEntity.getIdx());

        given(mockFrontNoticeJpaRepository.findPrevOneNotice(frontNoticeEntity.getIdx())).willReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaRepository.findPrevOneNotice(frontNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockFrontNoticeJpaRepository).should(times(1)).findPrevOneNotice(frontNoticeEntity.getIdx());
        then(mockFrontNoticeJpaRepository).should(atLeastOnce()).findPrevOneNotice(frontNoticeEntity.getIdx());
        then(mockFrontNoticeJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 공지사항 상세 조회 Mockito 테스트")
    void 다음공지사항상세조회Mockito테스트() {
        // given
        frontNoticeEntity = FrontNoticeEntity.builder().idx(2L).build();

        // when
        frontNoticeDTO = frontNoticeJpaRepository.findNextOneNotice(frontNoticeEntity.getIdx());

        when(mockFrontNoticeJpaRepository.findNextOneNotice(frontNoticeEntity.getIdx())).thenReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaRepository.findNextOneNotice(frontNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockFrontNoticeJpaRepository, times(1)).findNextOneNotice(frontNoticeEntity.getIdx());
        verify(mockFrontNoticeJpaRepository, atLeastOnce()).findNextOneNotice(frontNoticeEntity.getIdx());
        verifyNoMoreInteractions(mockFrontNoticeJpaRepository);

        InOrder inOrder = inOrder(mockFrontNoticeJpaRepository);
        inOrder.verify(mockFrontNoticeJpaRepository).findNextOneNotice(frontNoticeEntity.getIdx());
    }

    @Test
    @DisplayName("다음 공지사항 상세 조회 BDD 테스트")
    void 다음공지사항상세조회BDD테스트() {
        // given
        frontNoticeEntity = FrontNoticeEntity.builder().idx(2L).build();

        // when
        frontNoticeDTO = frontNoticeJpaRepository.findNextOneNotice(frontNoticeEntity.getIdx());

        given(mockFrontNoticeJpaRepository.findNextOneNotice(frontNoticeEntity.getIdx())).willReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaRepository.findNextOneNotice(frontNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockFrontNoticeJpaRepository).should(times(1)).findNextOneNotice(frontNoticeEntity.getIdx());
        then(mockFrontNoticeJpaRepository).should(atLeastOnce()).findNextOneNotice(frontNoticeEntity.getIdx());
        then(mockFrontNoticeJpaRepository).shouldHaveNoMoreInteractions();
    }
}
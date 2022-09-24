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

import static com.tsp.new_tsp_front.api.notice.service.impl.NoticeMapper.INSTANCE;
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

        frontNoticeDTO = INSTANCE.toDto(frontNoticeEntity);
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
        assertThat(frontNoticeJpaRepository.findNoticesList(noticeMap)).isNotEmpty();
    }

    @Test
    @DisplayName("공지사항 리스트 조회 Mockito 테스트")
    void 공지사항리스트조회Mockito테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("jpaStartPage", 1);
        noticeMap.put("size", 3);

        List<FrontNoticeDTO> noticeList = new ArrayList<>();
        noticeList.add(FrontNoticeDTO.builder().idx(1).title("공지사항").description("공지사항").visible("Y").build());

        // when
        when(mockFrontNoticeJpaRepository.findNoticesList(noticeMap)).thenReturn(noticeList);
        List<FrontNoticeDTO> newNoticeList = mockFrontNoticeJpaRepository.findNoticesList(noticeMap);

        // then
        assertThat(newNoticeList.get(0).getIdx()).isEqualTo(noticeList.get(0).getIdx());
        assertThat(newNoticeList.get(0).getTitle()).isEqualTo(noticeList.get(0).getTitle());
        assertThat(newNoticeList.get(0).getDescription()).isEqualTo(noticeList.get(0).getDescription());

        // verify
        verify(mockFrontNoticeJpaRepository, times(1)).findNoticesList(noticeMap);
        verify(mockFrontNoticeJpaRepository, atLeastOnce()).findNoticesList(noticeMap);
        verifyNoMoreInteractions(mockFrontNoticeJpaRepository);

        InOrder inOrder = inOrder(mockFrontNoticeJpaRepository);
        inOrder.verify(mockFrontNoticeJpaRepository).findNoticesList(noticeMap);
    }

    @Test
    @DisplayName("공지사항 리스트 조회 BDD 테스트")
    void 공지사항리스트조회BDD테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("jpaStartPage", 1);
        noticeMap.put("size", 3);

        List<FrontNoticeDTO> noticeList = new ArrayList<>();
        noticeList.add(FrontNoticeDTO.builder().idx(1).title("공지사항").description("공지사항").visible("Y").build());

        // when
        given(mockFrontNoticeJpaRepository.findNoticesList(noticeMap)).willReturn(noticeList);
        List<FrontNoticeDTO> newNoticeList = mockFrontNoticeJpaRepository.findNoticesList(noticeMap);

        // then
        assertThat(newNoticeList.get(0).getIdx()).isEqualTo(noticeList.get(0).getIdx());
        assertThat(newNoticeList.get(0).getTitle()).isEqualTo(noticeList.get(0).getTitle());
        assertThat(newNoticeList.get(0).getDescription()).isEqualTo(noticeList.get(0).getDescription());

        // verify
        then(mockFrontNoticeJpaRepository).should(times(1)).findNoticesList(noticeMap);
        then(mockFrontNoticeJpaRepository).should(atLeastOnce()).findNoticesList(noticeMap);
        then(mockFrontNoticeJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("상단 고정 공지사항 리스트 조회 Mockito 테스트")
    void 상단고정공지사항리스트조회Mockito테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("jpaStartPage", 1);
        noticeMap.put("size", 3);

        List<FrontNoticeDTO> noticeList = new ArrayList<>();
        noticeList.add(FrontNoticeDTO.builder().idx(1).title("공지사항").description("공지사항").visible("Y").topFixed(Boolean.TRUE.toString()).build());

        // when
        when(mockFrontNoticeJpaRepository.findFixedNoticesList(noticeMap)).thenReturn(noticeList);
        List<FrontNoticeDTO> newNoticeList = mockFrontNoticeJpaRepository.findFixedNoticesList(noticeMap);

        // then
        assertThat(newNoticeList.get(0).getIdx()).isEqualTo(noticeList.get(0).getIdx());
        assertThat(newNoticeList.get(0).getTitle()).isEqualTo(noticeList.get(0).getTitle());
        assertThat(newNoticeList.get(0).getDescription()).isEqualTo(noticeList.get(0).getDescription());
        assertThat(newNoticeList.get(0).getTopFixed()).isEqualTo(noticeList.get(0).getTopFixed());

        // verify
        verify(mockFrontNoticeJpaRepository, times(1)).findFixedNoticesList(noticeMap);
        verify(mockFrontNoticeJpaRepository, atLeastOnce()).findFixedNoticesList(noticeMap);
        verifyNoMoreInteractions(mockFrontNoticeJpaRepository);

        InOrder inOrder = inOrder(mockFrontNoticeJpaRepository);
        inOrder.verify(mockFrontNoticeJpaRepository).findFixedNoticesList(noticeMap);
    }

    @Test
    @DisplayName("상단 고정 공지사항 리스트 조회 BDD 테스트")
    void 상단고정공지사항리스트조회BDD테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("jpaStartPage", 1);
        noticeMap.put("size", 3);

        List<FrontNoticeDTO> noticeList = new ArrayList<>();
        noticeList.add(FrontNoticeDTO.builder().idx(1).title("공지사항").description("공지사항").visible("Y").topFixed(Boolean.TRUE.toString()).build());

        // when
        given(mockFrontNoticeJpaRepository.findFixedNoticesList(noticeMap)).willReturn(noticeList);
        List<FrontNoticeDTO> newNoticeList = mockFrontNoticeJpaRepository.findFixedNoticesList(noticeMap);

        // then
        assertThat(newNoticeList.get(0).getIdx()).isEqualTo(noticeList.get(0).getIdx());
        assertThat(newNoticeList.get(0).getTitle()).isEqualTo(noticeList.get(0).getTitle());
        assertThat(newNoticeList.get(0).getDescription()).isEqualTo(noticeList.get(0).getDescription());
        assertThat(newNoticeList.get(0).getTopFixed()).isEqualTo(noticeList.get(0).getTopFixed());

        // verify
        then(mockFrontNoticeJpaRepository).should(times(1)).findFixedNoticesList(noticeMap);
        then(mockFrontNoticeJpaRepository).should(atLeastOnce()).findFixedNoticesList(noticeMap);
        then(mockFrontNoticeJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공지사항상세Mockito조회테스트")
    void 공지사항상세Mockito조회테스트() {
        // given
        FrontNoticeEntity frontNoticeEntity = FrontNoticeEntity.builder()
                .idx(1)
                .title("공지사항 테스트")
                .description("공지사항 테스트")
                .visible("Y")
                .build();

        frontNoticeDTO = INSTANCE.toDto(frontNoticeEntity);

        // when
        when(mockFrontNoticeJpaRepository.findOneNotice(frontNoticeEntity)).thenReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaRepository.findOneNotice(frontNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);
        assertThat(noticeInfo.getTitle()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getDescription()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockFrontNoticeJpaRepository, times(1)).findOneNotice(frontNoticeEntity);
        verify(mockFrontNoticeJpaRepository, atLeastOnce()).findOneNotice(frontNoticeEntity);
        verifyNoMoreInteractions(mockFrontNoticeJpaRepository);

        InOrder inOrder = inOrder(mockFrontNoticeJpaRepository);
        inOrder.verify(mockFrontNoticeJpaRepository).findOneNotice(frontNoticeEntity);
    }

    @Test
    @DisplayName("공지사항상세BDD조회테스트")
    void 공지사항상세BDD조회테스트() {
        // given
        FrontNoticeEntity frontNoticeEntity = FrontNoticeEntity.builder()
                .idx(1)
                .title("공지사항 테스트")
                .description("공지사항 테스트")
                .visible("Y")
                .build();

        frontNoticeDTO = INSTANCE.toDto(frontNoticeEntity);

        // when
        given(mockFrontNoticeJpaRepository.findOneNotice(frontNoticeEntity)).willReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaRepository.findOneNotice(frontNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);
        assertThat(noticeInfo.getTitle()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getDescription()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockFrontNoticeJpaRepository).should(times(1)).findOneNotice(frontNoticeEntity);
        then(mockFrontNoticeJpaRepository).should(atLeastOnce()).findOneNotice(frontNoticeEntity);
        then(mockFrontNoticeJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 공지사항 상세 조회 테스트")
    void 이전or다음공지사항상세조회테스트() {
        // given
        frontNoticeEntity = FrontNoticeEntity.builder().idx(2).build();

        // when
        frontNoticeDTO = frontNoticeJpaRepository.findOneNotice(frontNoticeEntity);

        // 이전 프로덕션
        assertThat(frontNoticeJpaRepository.findPrevOneNotice(frontNoticeEntity).getIdx()).isEqualTo(1);
        // 다음 프로덕션
        assertThat(frontNoticeJpaRepository.findNextOneNotice(frontNoticeEntity).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 공지사항 상세 조회 Mockito 테스트")
    void 이전공지사항상세조회Mockito테스트() {
        // given
        frontNoticeEntity = FrontNoticeEntity.builder().idx(2).build();

        // when
        frontNoticeDTO = frontNoticeJpaRepository.findPrevOneNotice(frontNoticeEntity);

        when(mockFrontNoticeJpaRepository.findPrevOneNotice(frontNoticeEntity)).thenReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaRepository.findPrevOneNotice(frontNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockFrontNoticeJpaRepository, times(1)).findPrevOneNotice(frontNoticeEntity);
        verify(mockFrontNoticeJpaRepository, atLeastOnce()).findPrevOneNotice(frontNoticeEntity);
        verifyNoMoreInteractions(mockFrontNoticeJpaRepository);

        InOrder inOrder = inOrder(mockFrontNoticeJpaRepository);
        inOrder.verify(mockFrontNoticeJpaRepository).findPrevOneNotice(frontNoticeEntity);
    }

    @Test
    @DisplayName("이전 공지사항 상세 조회 BDD 테스트")
    void 이전공지사항상세조회BDD테스트() {
        // given
        frontNoticeEntity = FrontNoticeEntity.builder().idx(2).build();

        // when
        frontNoticeDTO = frontNoticeJpaRepository.findPrevOneNotice(frontNoticeEntity);

        given(mockFrontNoticeJpaRepository.findPrevOneNotice(frontNoticeEntity)).willReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaRepository.findPrevOneNotice(frontNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockFrontNoticeJpaRepository).should(times(1)).findPrevOneNotice(frontNoticeEntity);
        then(mockFrontNoticeJpaRepository).should(atLeastOnce()).findPrevOneNotice(frontNoticeEntity);
        then(mockFrontNoticeJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 공지사항 상세 조회 Mockito 테스트")
    void 다음공지사항상세조회Mockito테스트() {
        // given
        frontNoticeEntity = FrontNoticeEntity.builder().idx(2).build();

        // when
        frontNoticeDTO = frontNoticeJpaRepository.findNextOneNotice(frontNoticeEntity);

        when(mockFrontNoticeJpaRepository.findNextOneNotice(frontNoticeEntity)).thenReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaRepository.findNextOneNotice(frontNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockFrontNoticeJpaRepository, times(1)).findNextOneNotice(frontNoticeEntity);
        verify(mockFrontNoticeJpaRepository, atLeastOnce()).findNextOneNotice(frontNoticeEntity);
        verifyNoMoreInteractions(mockFrontNoticeJpaRepository);

        InOrder inOrder = inOrder(mockFrontNoticeJpaRepository);
        inOrder.verify(mockFrontNoticeJpaRepository).findNextOneNotice(frontNoticeEntity);
    }

    @Test
    @DisplayName("다음 공지사항 상세 조회 BDD 테스트")
    void 다음공지사항상세조회BDD테스트() {
        // given
        frontNoticeEntity = FrontNoticeEntity.builder().idx(2).build();

        // when
        frontNoticeDTO = frontNoticeJpaRepository.findNextOneNotice(frontNoticeEntity);

        given(mockFrontNoticeJpaRepository.findNextOneNotice(frontNoticeEntity)).willReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaRepository.findNextOneNotice(frontNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockFrontNoticeJpaRepository).should(times(1)).findNextOneNotice(frontNoticeEntity);
        then(mockFrontNoticeJpaRepository).should(atLeastOnce()).findNextOneNotice(frontNoticeEntity);
        then(mockFrontNoticeJpaRepository).shouldHaveNoMoreInteractions();
    }
}
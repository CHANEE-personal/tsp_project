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
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
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
}
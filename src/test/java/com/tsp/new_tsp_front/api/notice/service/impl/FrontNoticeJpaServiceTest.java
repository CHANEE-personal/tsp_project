package com.tsp.new_tsp_front.api.notice.service.impl;

import com.tsp.new_tsp_front.api.notice.domain.FrontNoticeDTO;
import com.tsp.new_tsp_front.api.notice.domain.FrontNoticeEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("공지사항 Service Test")
class FrontNoticeJpaServiceTest {
    @Mock
    private FrontNoticeJpaService mockFrontNoticeJpaService;
    private final FrontNoticeJpaService frontNoticeJpaService;

    @Test
    @DisplayName("공지사항리스트조회Mockito테스트")
    void 공지사항리스트조회Mockito테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("jpaStartPage", 1);
        noticeMap.put("size", 3);

        List<FrontNoticeDTO> returnNoticeList = new ArrayList<>();

        returnNoticeList.add(FrontNoticeDTO.builder().idx(1L).title("공지사항테스트").description("공지사항테스트").visible("Y").build());
        returnNoticeList.add(FrontNoticeDTO.builder().idx(2L).title("noticeTest").description("noticeTest").visible("Y").build());

        // when
        when(mockFrontNoticeJpaService.findNoticeList(noticeMap)).thenReturn(returnNoticeList);
        List<FrontNoticeDTO> noticeList = mockFrontNoticeJpaService.findNoticeList(noticeMap);

        // then
        assertAll(
                () -> assertThat(noticeList).isNotEmpty(),
                () -> assertThat(noticeList).hasSize(2)
        );

        assertThat(noticeList.get(0).getIdx()).isEqualTo(returnNoticeList.get(0).getIdx());
        assertThat(noticeList.get(0).getTitle()).isEqualTo(returnNoticeList.get(0).getTitle());
        assertThat(noticeList.get(0).getDescription()).isEqualTo(returnNoticeList.get(0).getDescription());
        assertThat(noticeList.get(0).getVisible()).isEqualTo(returnNoticeList.get(0).getVisible());

        // verify
        verify(mockFrontNoticeJpaService, times(1)).findNoticeList(noticeMap);
        verify(mockFrontNoticeJpaService, atLeastOnce()).findNoticeList(noticeMap);
        verifyNoMoreInteractions(mockFrontNoticeJpaService);

        InOrder inOrder = inOrder(mockFrontNoticeJpaService);
        inOrder.verify(mockFrontNoticeJpaService).findNoticeList(noticeMap);
    }

    @Test
    @DisplayName("공지사항리스트조회BDD테스트")
    void 공지사항리스트조회BDD테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("jpaStartPage", 1);
        noticeMap.put("size", 3);

        List<FrontNoticeDTO> returnNoticeList = new ArrayList<>();

        returnNoticeList.add(FrontNoticeDTO.builder().idx(1L).title("공지사항테스트").description("공지사항테스트").visible("Y").build());
        returnNoticeList.add(FrontNoticeDTO.builder().idx(2L).title("noticeTest").description("noticeTest").visible("Y").build());

        // when
        given(mockFrontNoticeJpaService.findNoticeList(noticeMap)).willReturn(returnNoticeList);
        List<FrontNoticeDTO> noticeList = mockFrontNoticeJpaService.findNoticeList(noticeMap);

        // then
        assertAll(
                () -> assertThat(noticeList).isNotEmpty(),
                () -> assertThat(noticeList).hasSize(2)
        );

        assertThat(noticeList.get(0).getIdx()).isEqualTo(returnNoticeList.get(0).getIdx());
        assertThat(noticeList.get(0).getTitle()).isEqualTo(returnNoticeList.get(0).getTitle());
        assertThat(noticeList.get(0).getDescription()).isEqualTo(returnNoticeList.get(0).getDescription());
        assertThat(noticeList.get(0).getVisible()).isEqualTo(returnNoticeList.get(0).getVisible());

        // verify
        then(mockFrontNoticeJpaService).should(times(1)).findNoticeList(noticeMap);
        then(mockFrontNoticeJpaService).should(atLeastOnce()).findNoticeList(noticeMap);
        then(mockFrontNoticeJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("상단 고정 공지사항 리스트 조회 Mockito 테스트")
    void 상단고정공지사항리스트조회Mockito테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("jpaStartPage", 1);
        noticeMap.put("size", 3);

        List<FrontNoticeDTO> noticeList = new ArrayList<>();
        noticeList.add(FrontNoticeDTO.builder().idx(1L).title("공지사항").description("공지사항").visible("Y").topFixed(Boolean.TRUE.toString()).build());

        // when
        when(mockFrontNoticeJpaService.findFixedNoticeList(noticeMap)).thenReturn(noticeList);
        List<FrontNoticeDTO> newNoticeList = mockFrontNoticeJpaService.findFixedNoticeList(noticeMap);

        // then
        assertThat(newNoticeList.get(0).getIdx()).isEqualTo(noticeList.get(0).getIdx());
        assertThat(newNoticeList.get(0).getTitle()).isEqualTo(noticeList.get(0).getTitle());
        assertThat(newNoticeList.get(0).getDescription()).isEqualTo(noticeList.get(0).getDescription());
        assertThat(newNoticeList.get(0).getTopFixed()).isEqualTo(noticeList.get(0).getTopFixed());

        // verify
        verify(mockFrontNoticeJpaService, times(1)).findFixedNoticeList(noticeMap);
        verify(mockFrontNoticeJpaService, atLeastOnce()).findFixedNoticeList(noticeMap);
        verifyNoMoreInteractions(mockFrontNoticeJpaService);

        InOrder inOrder = inOrder(mockFrontNoticeJpaService);
        inOrder.verify(mockFrontNoticeJpaService).findFixedNoticeList(noticeMap);
    }

    @Test
    @DisplayName("상단 고정 공지사항 리스트 조회 BDD 테스트")
    void 상단고정공지사항리스트조회BDD테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("jpaStartPage", 1);
        noticeMap.put("size", 3);

        List<FrontNoticeDTO> noticeList = new ArrayList<>();
        noticeList.add(FrontNoticeDTO.builder().idx(1L).title("공지사항").description("공지사항").visible("Y").topFixed(Boolean.TRUE.toString()).build());

        // when
        given(mockFrontNoticeJpaService.findFixedNoticeList(noticeMap)).willReturn(noticeList);
        List<FrontNoticeDTO> newNoticeList = mockFrontNoticeJpaService.findFixedNoticeList(noticeMap);

        // then
        assertThat(newNoticeList.get(0).getIdx()).isEqualTo(noticeList.get(0).getIdx());
        assertThat(newNoticeList.get(0).getTitle()).isEqualTo(noticeList.get(0).getTitle());
        assertThat(newNoticeList.get(0).getDescription()).isEqualTo(noticeList.get(0).getDescription());
        assertThat(newNoticeList.get(0).getTopFixed()).isEqualTo(noticeList.get(0).getTopFixed());

        // verify
        then(mockFrontNoticeJpaService).should(times(1)).findFixedNoticeList(noticeMap);
        then(mockFrontNoticeJpaService).should(atLeastOnce()).findFixedNoticeList(noticeMap);
        then(mockFrontNoticeJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공지사항상세조회Mockito테스트")
    void 공지사항상세조회Mockito테스트() {
        // given
        FrontNoticeEntity frontNoticeEntity = FrontNoticeEntity.builder().idx(1L).title("noticeTest").description("noticeTest").build();
        FrontNoticeDTO frontNoticeDTO = FrontNoticeEntity.toDto(frontNoticeEntity);

        // when
        when(mockFrontNoticeJpaService.findOneNotice(frontNoticeEntity)).thenReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaService.findOneNotice(frontNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(frontNoticeDTO.getIdx());
        assertThat(noticeInfo.getTitle()).isEqualTo(frontNoticeDTO.getTitle());
        assertThat(noticeInfo.getDescription()).isEqualTo(frontNoticeDTO.getDescription());
        assertThat(noticeInfo.getVisible()).isEqualTo(frontNoticeDTO.getVisible());

        // verify
        verify(mockFrontNoticeJpaService, times(1)).findOneNotice(frontNoticeEntity);
        verify(mockFrontNoticeJpaService, atLeastOnce()).findOneNotice(frontNoticeEntity);
        verifyNoMoreInteractions(mockFrontNoticeJpaService);

        InOrder inOrder = inOrder(mockFrontNoticeJpaService);
        inOrder.verify(mockFrontNoticeJpaService).findOneNotice(frontNoticeEntity);
    }

    @Test
    @DisplayName("공지사항상세조회BDD테스트")
    void 공지사항상세조회BDD테스트() {
        // given
        FrontNoticeEntity frontNoticeEntity = FrontNoticeEntity.builder().idx(1L).title("noticeTest").description("noticeTest").build();
        FrontNoticeDTO frontNoticeDTO = FrontNoticeEntity.toDto(frontNoticeEntity);

        // when
        when(mockFrontNoticeJpaService.findOneNotice(frontNoticeEntity)).thenReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaService.findOneNotice(frontNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(frontNoticeDTO.getIdx());
        assertThat(noticeInfo.getTitle()).isEqualTo(frontNoticeDTO.getTitle());
        assertThat(noticeInfo.getDescription()).isEqualTo(frontNoticeDTO.getDescription());
        assertThat(noticeInfo.getVisible()).isEqualTo(frontNoticeDTO.getVisible());

        // verify
        then(mockFrontNoticeJpaService).should(times(1)).findOneNotice(frontNoticeEntity);
        then(mockFrontNoticeJpaService).should(atLeastOnce()).findOneNotice(frontNoticeEntity);
        then(mockFrontNoticeJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 공지사항 상세 조회 테스트")
    void 이전or다음공지사항상세조회테스트() {
        // given
        FrontNoticeEntity frontNoticeEntity = FrontNoticeEntity.builder().idx(2L).build();

        // when
        FrontNoticeDTO frontNoticeDTO = frontNoticeJpaService.findOneNotice(frontNoticeEntity);

        // 이전 프로덕션
        assertThat(frontNoticeJpaService.findPrevOneNotice(frontNoticeEntity).getIdx()).isEqualTo(1);
        // 다음 프로덕션
        assertThat(frontNoticeJpaService.findNextOneNotice(frontNoticeEntity).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 공지사항 상세 조회 Mockito 테스트")
    void 이전공지사항상세조회Mockito테스트() {
        // given
        FrontNoticeEntity frontNoticeEntity = FrontNoticeEntity.builder().idx(2L).build();

        // when
        FrontNoticeDTO frontNoticeDTO = frontNoticeJpaService.findPrevOneNotice(frontNoticeEntity);

        when(mockFrontNoticeJpaService.findPrevOneNotice(frontNoticeEntity)).thenReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaService.findPrevOneNotice(frontNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockFrontNoticeJpaService, times(1)).findPrevOneNotice(frontNoticeEntity);
        verify(mockFrontNoticeJpaService, atLeastOnce()).findPrevOneNotice(frontNoticeEntity);
        verifyNoMoreInteractions(mockFrontNoticeJpaService);

        InOrder inOrder = inOrder(mockFrontNoticeJpaService);
        inOrder.verify(mockFrontNoticeJpaService).findPrevOneNotice(frontNoticeEntity);
    }

    @Test
    @DisplayName("이전 공지사항 상세 조회 BDD 테스트")
    void 이전공지사항상세조회BDD테스트() {
        // given
        FrontNoticeEntity frontNoticeEntity = FrontNoticeEntity.builder().idx(2L).build();

        // when
        FrontNoticeDTO frontNoticeDTO = frontNoticeJpaService.findPrevOneNotice(frontNoticeEntity);

        given(mockFrontNoticeJpaService.findPrevOneNotice(frontNoticeEntity)).willReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaService.findPrevOneNotice(frontNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockFrontNoticeJpaService).should(times(1)).findPrevOneNotice(frontNoticeEntity);
        then(mockFrontNoticeJpaService).should(atLeastOnce()).findPrevOneNotice(frontNoticeEntity);
        then(mockFrontNoticeJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 프로덕션 상세 조회 Mockito 테스트")
    void 다음프로덕션상세조회Mockito테스트() {
        // given
        FrontNoticeEntity frontNoticeEntity = FrontNoticeEntity.builder().idx(2L).build();

        // when
        FrontNoticeDTO frontNoticeDTO = frontNoticeJpaService.findNextOneNotice(frontNoticeEntity);

        when(mockFrontNoticeJpaService.findNextOneNotice(frontNoticeEntity)).thenReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaService.findNextOneNotice(frontNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockFrontNoticeJpaService, times(1)).findNextOneNotice(frontNoticeEntity);
        verify(mockFrontNoticeJpaService, atLeastOnce()).findNextOneNotice(frontNoticeEntity);
        verifyNoMoreInteractions(mockFrontNoticeJpaService);

        InOrder inOrder = inOrder(mockFrontNoticeJpaService);
        inOrder.verify(mockFrontNoticeJpaService).findNextOneNotice(frontNoticeEntity);
    }

    @Test
    @DisplayName("다음 프로덕션 상세 조회 BDD 테스트")
    void 다음프로덕션상세조회BDD테스트() {
        // given
        FrontNoticeEntity frontNoticeEntity = FrontNoticeEntity.builder().idx(2L).build();

        // when
        FrontNoticeDTO frontNoticeDTO = frontNoticeJpaService.findNextOneNotice(frontNoticeEntity);

        given(mockFrontNoticeJpaService.findNextOneNotice(frontNoticeEntity)).willReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaService.findNextOneNotice(frontNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockFrontNoticeJpaService).should(times(1)).findNextOneNotice(frontNoticeEntity);
        then(mockFrontNoticeJpaService).should(atLeastOnce()).findNextOneNotice(frontNoticeEntity);
        then(mockFrontNoticeJpaService).shouldHaveNoMoreInteractions();
    }
}
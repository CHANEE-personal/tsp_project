package com.tsp.new_tsp_front.api.notice.service.impl;

import com.tsp.new_tsp_front.api.notice.domain.FrontNoticeDTO;
import com.tsp.new_tsp_front.api.notice.domain.FrontNoticeEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.api.notice.service.impl.NoticeMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("공지사항 Service Test")
class FrontNoticeJpaServiceTest {
    @Mock
    private FrontNoticeJpaService mockFrontNoticeJpaService;

    @Test
    @DisplayName("공지사항리스트조회Mockito테스트")
    void 공지사항리스트조회Mockito테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("jpaStartPage", 1);
        noticeMap.put("size", 3);

        List<FrontNoticeDTO> returnNoticeList = new ArrayList<>();

        returnNoticeList.add(FrontNoticeDTO.builder().idx(1).title("공지사항테스트").description("공지사항테스트").visible("Y").build());
        returnNoticeList.add(FrontNoticeDTO.builder().idx(2).title("noticeTest").description("noticeTest").visible("Y").build());

        // when
        when(mockFrontNoticeJpaService.findNoticesList(noticeMap)).thenReturn(returnNoticeList);
        List<FrontNoticeDTO> noticeList = mockFrontNoticeJpaService.findNoticesList(noticeMap);

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
        verify(mockFrontNoticeJpaService, times(1)).findNoticesList(noticeMap);
        verify(mockFrontNoticeJpaService, atLeastOnce()).findNoticesList(noticeMap);
        verifyNoMoreInteractions(mockFrontNoticeJpaService);

        InOrder inOrder = inOrder(mockFrontNoticeJpaService);
        inOrder.verify(mockFrontNoticeJpaService).findNoticesList(noticeMap);
    }

    @Test
    @DisplayName("공지사항리스트조회BDD테스트")
    void 공지사항리스트조회BDD테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("jpaStartPage", 1);
        noticeMap.put("size", 3);

        List<FrontNoticeDTO> returnNoticeList = new ArrayList<>();

        returnNoticeList.add(FrontNoticeDTO.builder().idx(1).title("공지사항테스트").description("공지사항테스트").visible("Y").build());
        returnNoticeList.add(FrontNoticeDTO.builder().idx(2).title("noticeTest").description("noticeTest").visible("Y").build());

        // when
        given(mockFrontNoticeJpaService.findNoticesList(noticeMap)).willReturn(returnNoticeList);
        List<FrontNoticeDTO> noticeList = mockFrontNoticeJpaService.findNoticesList(noticeMap);

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
        then(mockFrontNoticeJpaService).should(times(1)).findNoticesList(noticeMap);
        then(mockFrontNoticeJpaService).should(atLeastOnce()).findNoticesList(noticeMap);
        then(mockFrontNoticeJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공지사항상세조회Mockito테스트")
    void 공지사항상세조회Mockito테스트() {
        // given
        FrontNoticeEntity frontNoticeEntity = FrontNoticeEntity.builder().idx(1).title("noticeTest").description("noticeTest").build();
        FrontNoticeDTO frontNoticeDTO = INSTANCE.toDto(frontNoticeEntity);

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
        FrontNoticeEntity frontNoticeEntity = FrontNoticeEntity.builder().idx(1).title("noticeTest").description("noticeTest").build();
        FrontNoticeDTO frontNoticeDTO = INSTANCE.toDto(frontNoticeEntity);

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
}
package com.tsp.api.notice.service;

import com.tsp.api.FrontCommonServiceTest;
import com.tsp.api.notice.domain.FrontNoticeDTO;
import com.tsp.api.notice.domain.FrontNoticeEntity;
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

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
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
class FrontNoticeJpaServiceTest extends FrontCommonServiceTest {
    @Mock private FrontNoticeJpaRepository frontNoticeJpaRepository;
    @Mock private FrontNoticeJpaQueryRepository frontNoticeJpaQueryRepository;
    @InjectMocks private FrontNoticeJpaService mockFrontNoticeJpaService;
    private final FrontNoticeJpaService frontNoticeJpaService;

    @Test
    @DisplayName("공지사항 리스트 조회 Mockito 테스트")
    void 공지사항리스트조회Mockito테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<FrontNoticeDTO> noticeList = new ArrayList<>();
        noticeList.add(FrontNoticeDTO.builder().idx(1L).title("공지사항").description("공지사항").visible("Y").build());
        Page<FrontNoticeDTO> resultNotice = new PageImpl<>(noticeList, pageRequest, noticeList.size());

        // when
        when(frontNoticeJpaQueryRepository.findNoticeList(noticeMap, pageRequest)).thenReturn(resultNotice);
        Page<FrontNoticeDTO> newNoticeList = mockFrontNoticeJpaService.findNoticeList(noticeMap, pageRequest);
        List<FrontNoticeDTO> findNoticeList = newNoticeList.stream().collect(Collectors.toList());

        // then
        assertThat(findNoticeList.get(0).getIdx()).isEqualTo(noticeList.get(0).getIdx());
        assertThat(findNoticeList.get(0).getTitle()).isEqualTo(noticeList.get(0).getTitle());
        assertThat(findNoticeList.get(0).getDescription()).isEqualTo(noticeList.get(0).getDescription());

        // verify
        verify(frontNoticeJpaQueryRepository, times(1)).findNoticeList(noticeMap, pageRequest);
        verify(frontNoticeJpaQueryRepository, atLeastOnce()).findNoticeList(noticeMap, pageRequest);
        verifyNoMoreInteractions(frontNoticeJpaQueryRepository);

        InOrder inOrder = inOrder(frontNoticeJpaQueryRepository);
        inOrder.verify(frontNoticeJpaQueryRepository).findNoticeList(noticeMap, pageRequest);
    }

    @Test
    @DisplayName("공지사항 리스트 조회 BDD 테스트")
    void 공지사항리스트조회BDD테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<FrontNoticeDTO> noticeList = new ArrayList<>();
        noticeList.add(FrontNoticeDTO.builder().idx(1L).title("공지사항").description("공지사항").visible("Y").build());
        Page<FrontNoticeDTO> resultNotice = new PageImpl<>(noticeList, pageRequest, noticeList.size());

        // when
        given(frontNoticeJpaQueryRepository.findNoticeList(noticeMap, pageRequest)).willReturn(resultNotice);
        Page<FrontNoticeDTO> newNoticeList = mockFrontNoticeJpaService.findNoticeList(noticeMap, pageRequest);
        List<FrontNoticeDTO> findNoticeList = newNoticeList.stream().collect(Collectors.toList());

        // then
        assertThat(findNoticeList.get(0).getIdx()).isEqualTo(noticeList.get(0).getIdx());
        assertThat(findNoticeList.get(0).getTitle()).isEqualTo(noticeList.get(0).getTitle());
        assertThat(findNoticeList.get(0).getDescription()).isEqualTo(noticeList.get(0).getDescription());

        // verify
        then(frontNoticeJpaQueryRepository).should(times(1)).findNoticeList(noticeMap, pageRequest);
        then(frontNoticeJpaQueryRepository).should(atLeastOnce()).findNoticeList(noticeMap, pageRequest);
        then(frontNoticeJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공지사항상세조회Mockito테스트")
    void 공지사항상세조회Mockito테스트() {
        // when
        when(frontNoticeJpaRepository.findById(frontNoticeEntity.getIdx())).thenReturn(Optional.ofNullable(frontNoticeEntity));
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaService.findOneNotice(frontNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(frontNoticeEntity.getIdx());
        assertThat(noticeInfo.getTitle()).isEqualTo(frontNoticeEntity.getTitle());
        assertThat(noticeInfo.getDescription()).isEqualTo(frontNoticeEntity.getDescription());
        assertThat(noticeInfo.getVisible()).isEqualTo(frontNoticeEntity.getVisible());

        // verify
        verify(frontNoticeJpaRepository, times(1)).findById(frontNoticeEntity.getIdx());
        verify(frontNoticeJpaRepository, atLeastOnce()).findById(frontNoticeEntity.getIdx());
        verifyNoMoreInteractions(frontNoticeJpaRepository);

        InOrder inOrder = inOrder(frontNoticeJpaRepository);
        inOrder.verify(frontNoticeJpaRepository).findById(frontNoticeEntity.getIdx());
    }

    @Test
    @DisplayName("공지사항상세조회BDD테스트")
    void 공지사항상세조회BDD테스트() {
        // when
        given(frontNoticeJpaRepository.findById(frontNoticeEntity.getIdx())).willReturn(Optional.ofNullable(frontNoticeEntity));
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaService.findOneNotice(frontNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(frontNoticeEntity.getIdx());
        assertThat(noticeInfo.getTitle()).isEqualTo(frontNoticeEntity.getTitle());
        assertThat(noticeInfo.getDescription()).isEqualTo(frontNoticeEntity.getDescription());
        assertThat(noticeInfo.getVisible()).isEqualTo(frontNoticeEntity.getVisible());

        // verify
        then(frontNoticeJpaRepository).should(times(1)).findById(frontNoticeEntity.getIdx());
        then(frontNoticeJpaRepository).should(atLeastOnce()).findById(frontNoticeEntity.getIdx());
        then(frontNoticeJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 공지사항 상세 조회 테스트")
    void 이전or다음공지사항상세조회테스트() {
        // given
        FrontNoticeEntity frontNoticeEntity = FrontNoticeEntity.builder().idx(2L).build();

        // when
        FrontNoticeDTO frontNoticeDTO = frontNoticeJpaService.findOneNotice(frontNoticeEntity.getIdx());

        // 이전 프로덕션
        assertThat(frontNoticeJpaService.findPrevOneNotice(frontNoticeEntity.getIdx()).getIdx()).isEqualTo(1);
        // 다음 프로덕션
        assertThat(frontNoticeJpaService.findNextOneNotice(frontNoticeEntity.getIdx()).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 공지사항 상세 조회 Mockito 테스트")
    void 이전공지사항상세조회Mockito테스트() {
        // given
        FrontNoticeEntity frontNoticeEntity = FrontNoticeEntity.builder().idx(2L).build();

        // when
        FrontNoticeDTO frontNoticeDTO = frontNoticeJpaService.findPrevOneNotice(frontNoticeEntity.getIdx());

        when(mockFrontNoticeJpaService.findPrevOneNotice(frontNoticeEntity.getIdx())).thenReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaService.findPrevOneNotice(frontNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockFrontNoticeJpaService, times(1)).findPrevOneNotice(frontNoticeEntity.getIdx());
        verify(mockFrontNoticeJpaService, atLeastOnce()).findPrevOneNotice(frontNoticeEntity.getIdx());
        verifyNoMoreInteractions(mockFrontNoticeJpaService);

        InOrder inOrder = inOrder(mockFrontNoticeJpaService);
        inOrder.verify(mockFrontNoticeJpaService).findPrevOneNotice(frontNoticeEntity.getIdx());
    }

    @Test
    @DisplayName("이전 공지사항 상세 조회 BDD 테스트")
    void 이전공지사항상세조회BDD테스트() {
        // given
        FrontNoticeEntity frontNoticeEntity = FrontNoticeEntity.builder().idx(2L).build();

        // when
        FrontNoticeDTO frontNoticeDTO = frontNoticeJpaService.findPrevOneNotice(frontNoticeEntity.getIdx());

        given(mockFrontNoticeJpaService.findPrevOneNotice(frontNoticeEntity.getIdx())).willReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaService.findPrevOneNotice(frontNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockFrontNoticeJpaService).should(times(1)).findPrevOneNotice(frontNoticeEntity.getIdx());
        then(mockFrontNoticeJpaService).should(atLeastOnce()).findPrevOneNotice(frontNoticeEntity.getIdx());
        then(mockFrontNoticeJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 공지사항 상세 조회 Mockito 테스트")
    void 다음공지사항상세조회Mockito테스트() {
        // given
        FrontNoticeEntity frontNoticeEntity = FrontNoticeEntity.builder().idx(2L).build();

        // when
        FrontNoticeDTO frontNoticeDTO = frontNoticeJpaService.findNextOneNotice(frontNoticeEntity.getIdx());

        when(mockFrontNoticeJpaService.findNextOneNotice(frontNoticeEntity.getIdx())).thenReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaService.findNextOneNotice(frontNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockFrontNoticeJpaService, times(1)).findNextOneNotice(frontNoticeEntity.getIdx());
        verify(mockFrontNoticeJpaService, atLeastOnce()).findNextOneNotice(frontNoticeEntity.getIdx());
        verifyNoMoreInteractions(mockFrontNoticeJpaService);

        InOrder inOrder = inOrder(mockFrontNoticeJpaService);
        inOrder.verify(mockFrontNoticeJpaService).findNextOneNotice(frontNoticeEntity.getIdx());
    }

    @Test
    @DisplayName("다음 공지사항 상세 조회 BDD 테스트")
    void 다음공지사항상세조회BDD테스트() {
        // given
        FrontNoticeEntity frontNoticeEntity = FrontNoticeEntity.builder().idx(2L).build();

        // when
        FrontNoticeDTO frontNoticeDTO = frontNoticeJpaService.findNextOneNotice(frontNoticeEntity.getIdx());

        given(mockFrontNoticeJpaService.findNextOneNotice(frontNoticeEntity.getIdx())).willReturn(frontNoticeDTO);
        FrontNoticeDTO noticeInfo = mockFrontNoticeJpaService.findNextOneNotice(frontNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockFrontNoticeJpaService).should(times(1)).findNextOneNotice(frontNoticeEntity.getIdx());
        then(mockFrontNoticeJpaService).should(atLeastOnce()).findNextOneNotice(frontNoticeEntity.getIdx());
        then(mockFrontNoticeJpaService).shouldHaveNoMoreInteractions();
    }
}

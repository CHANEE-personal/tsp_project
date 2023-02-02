package com.tsp.api.notice.service;

import com.tsp.api.model.service.AdminModelCommonServiceTest;
import com.tsp.api.notice.domain.AdminNoticeDTO;
import com.tsp.api.notice.domain.AdminNoticeEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("공지사항 Service Test")
class AdminNoticeJpaServiceTest extends AdminModelCommonServiceTest {
    @Mock
    private AdminNoticeJpaService mockAdminNoticeJpaService;
    private final AdminNoticeJpaService adminNoticeJpaService;

    private final EntityManager em;

    @Test
    @DisplayName("공지사항 리스트 조회 테스트")
    void 공지사항리스트조회테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 100);

        // then
        assertThat(adminNoticeJpaService.findNoticeList(noticeMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("공지사항 리스트 조회 Mockito 테스트")
    void 공지사항리스트조회Mockito테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<AdminNoticeDTO> returnNoticeList = new ArrayList<>();

        returnNoticeList.add(AdminNoticeDTO.builder().idx(1L).title("공지사항테스트").description("공지사항테스트").visible("Y").build());
        returnNoticeList.add(AdminNoticeDTO.builder().idx(2L).title("productionTest").description("productionTest").visible("Y").build());
        Page<AdminNoticeDTO> resultNotice = new PageImpl<>(returnNoticeList, pageRequest, returnNoticeList.size());

        // when
        when(mockAdminNoticeJpaService.findNoticeList(noticeMap, pageRequest)).thenReturn(resultNotice);
        Page<AdminNoticeDTO> noticeList = mockAdminNoticeJpaService.findNoticeList(noticeMap, pageRequest);
        List<AdminNoticeDTO> findNoticeList = noticeList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findNoticeList).isNotEmpty(),
                () -> assertThat(findNoticeList).hasSize(2)
        );

        assertThat(findNoticeList.get(0).getIdx()).isEqualTo(returnNoticeList.get(0).getIdx());
        assertThat(findNoticeList.get(0).getTitle()).isEqualTo(returnNoticeList.get(0).getTitle());
        assertThat(findNoticeList.get(0).getDescription()).isEqualTo(returnNoticeList.get(0).getDescription());
        assertThat(findNoticeList.get(0).getVisible()).isEqualTo(returnNoticeList.get(0).getVisible());

        // verify
        verify(mockAdminNoticeJpaService, times(1)).findNoticeList(noticeMap, pageRequest);
        verify(mockAdminNoticeJpaService, atLeastOnce()).findNoticeList(noticeMap, pageRequest);
        verifyNoMoreInteractions(mockAdminNoticeJpaService);

        InOrder inOrder = inOrder(mockAdminNoticeJpaService);
        inOrder.verify(mockAdminNoticeJpaService).findNoticeList(noticeMap, pageRequest);
    }

    @Test
    @DisplayName("공지사항 리스트 조회 BDD 테스트")
    void 공지사항리스트조회BDD테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<AdminNoticeDTO> returnNoticeList = new ArrayList<>();

        returnNoticeList.add(AdminNoticeDTO.builder().idx(1L).title("공지사항테스트").description("공지사항테스트").visible("Y").build());
        returnNoticeList.add(AdminNoticeDTO.builder().idx(2L).title("productionTest").description("productionTest").visible("Y").build());
        Page<AdminNoticeDTO> resultNotice = new PageImpl<>(returnNoticeList, pageRequest, returnNoticeList.size());

        // when
        given(mockAdminNoticeJpaService.findNoticeList(noticeMap, pageRequest)).willReturn(resultNotice);
        Page<AdminNoticeDTO> noticeList = mockAdminNoticeJpaService.findNoticeList(noticeMap, pageRequest);
        List<AdminNoticeDTO> findNoticeList = noticeList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findNoticeList).isNotEmpty(),
                () -> assertThat(findNoticeList).hasSize(2)
        );

        assertThat(findNoticeList.get(0).getIdx()).isEqualTo(returnNoticeList.get(0).getIdx());
        assertThat(findNoticeList.get(0).getTitle()).isEqualTo(returnNoticeList.get(0).getTitle());
        assertThat(findNoticeList.get(0).getDescription()).isEqualTo(returnNoticeList.get(0).getDescription());
        assertThat(findNoticeList.get(0).getVisible()).isEqualTo(returnNoticeList.get(0).getVisible());

        // verify
        then(mockAdminNoticeJpaService).should(times(1)).findNoticeList(noticeMap, pageRequest);
        then(mockAdminNoticeJpaService).should(atLeastOnce()).findNoticeList(noticeMap, pageRequest);
        then(mockAdminNoticeJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공지사항상세Mockito조회테스트")
    void 공지사항상세Mockito조회테스트() {
        // when
        when(mockAdminNoticeJpaService.findOneNotice(adminNoticeDTO.getIdx())).thenReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findOneNotice(adminNoticeDTO.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(adminNoticeDTO.getIdx());
        assertThat(noticeInfo.getTitle()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getDescription()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockAdminNoticeJpaService, times(1)).findOneNotice(adminNoticeDTO.getIdx());
        verify(mockAdminNoticeJpaService, atLeastOnce()).findOneNotice(adminNoticeDTO.getIdx());
        verifyNoMoreInteractions(mockAdminNoticeJpaService);

        InOrder inOrder = inOrder(mockAdminNoticeJpaService);
        inOrder.verify(mockAdminNoticeJpaService).findOneNotice(adminNoticeDTO.getIdx());
    }

    @Test
    @DisplayName("공지사항상세BDD조회테스트")
    void 공지사항상세BDD조회테스트() {
        // when
        given(mockAdminNoticeJpaService.findOneNotice(adminNoticeDTO.getIdx())).willReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findOneNotice(adminNoticeDTO.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(adminNoticeDTO.getIdx());
        assertThat(noticeInfo.getTitle()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getDescription()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockAdminNoticeJpaService).should(times(1)).findOneNotice(adminNoticeDTO.getIdx());
        then(mockAdminNoticeJpaService).should(atLeastOnce()).findOneNotice(adminNoticeDTO.getIdx());
        then(mockAdminNoticeJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 공지사항 상세 조회 테스트")
    void 이전or다음공지사항상세조회테스트() {
        // given
        adminNoticeEntity = AdminNoticeEntity.builder().idx(2L).build();

        // when
        adminNoticeDTO = adminNoticeJpaService.findOneNotice(adminNoticeEntity.getIdx());

        // 이전 공지사항
        assertThat(adminNoticeJpaService.findPrevOneNotice(adminNoticeEntity.getIdx()).getIdx()).isEqualTo(1);
        // 다음 공지사항
        assertThat(adminNoticeJpaService.findNextOneNotice(adminNoticeEntity.getIdx()).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 공지사항 상세 조회 Mockito 테스트")
    void 이전공지사항상세조회Mockito테스트() {
        // given
        adminNoticeEntity = AdminNoticeEntity.builder().idx(2L).build();

        // when
        adminNoticeDTO = adminNoticeJpaService.findPrevOneNotice(adminNoticeEntity.getIdx());

        when(mockAdminNoticeJpaService.findPrevOneNotice(adminNoticeEntity.getIdx())).thenReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findPrevOneNotice(adminNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockAdminNoticeJpaService, times(1)).findPrevOneNotice(adminNoticeEntity.getIdx());
        verify(mockAdminNoticeJpaService, atLeastOnce()).findPrevOneNotice(adminNoticeEntity.getIdx());
        verifyNoMoreInteractions(mockAdminNoticeJpaService);

        InOrder inOrder = inOrder(mockAdminNoticeJpaService);
        inOrder.verify(mockAdminNoticeJpaService).findPrevOneNotice(adminNoticeEntity.getIdx());
    }

    @Test
    @DisplayName("이전 공지사항 상세 조회 BDD 테스트")
    void 이전공지사항상세조회BDD테스트() {
        // given
        adminNoticeEntity = AdminNoticeEntity.builder().idx(2L).build();

        // when
        adminNoticeDTO = adminNoticeJpaService.findPrevOneNotice(adminNoticeEntity.getIdx());

        given(mockAdminNoticeJpaService.findPrevOneNotice(adminNoticeEntity.getIdx())).willReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findPrevOneNotice(adminNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockAdminNoticeJpaService).should(times(1)).findPrevOneNotice(adminNoticeEntity.getIdx());
        then(mockAdminNoticeJpaService).should(atLeastOnce()).findPrevOneNotice(adminNoticeEntity.getIdx());
        then(mockAdminNoticeJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 공지사항 상세 조회 Mockito 테스트")
    void 다음공지사항상세조회Mockito테스트() {
        // given
        adminNoticeEntity = AdminNoticeEntity.builder().idx(2L).build();

        // when
        adminNoticeDTO = adminNoticeJpaService.findNextOneNotice(adminNoticeEntity.getIdx());

        when(mockAdminNoticeJpaService.findNextOneNotice(adminNoticeEntity.getIdx())).thenReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findNextOneNotice(adminNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockAdminNoticeJpaService, times(1)).findNextOneNotice(adminNoticeEntity.getIdx());
        verify(mockAdminNoticeJpaService, atLeastOnce()).findNextOneNotice(adminNoticeEntity.getIdx());
        verifyNoMoreInteractions(mockAdminNoticeJpaService);

        InOrder inOrder = inOrder(mockAdminNoticeJpaService);
        inOrder.verify(mockAdminNoticeJpaService).findNextOneNotice(adminNoticeEntity.getIdx());
    }

    @Test
    @DisplayName("다음 공지사항 상세 조회 BDD 테스트")
    void 다음공지사항상세조회BDD테스트() {
        // given
        adminNoticeEntity = AdminNoticeEntity.builder().idx(2L).build();

        // when
        adminNoticeDTO = adminNoticeJpaService.findNextOneNotice(adminNoticeEntity.getIdx());

        given(mockAdminNoticeJpaService.findNextOneNotice(adminNoticeEntity.getIdx())).willReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findNextOneNotice(adminNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockAdminNoticeJpaService).should(times(1)).findNextOneNotice(adminNoticeEntity.getIdx());
        then(mockAdminNoticeJpaService).should(atLeastOnce()).findNextOneNotice(adminNoticeEntity.getIdx());
        then(mockAdminNoticeJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공지사항등록Mockito테스트")
    void 공지사항등록Mockito테스트() {
        // when
        when(mockAdminNoticeJpaService.findOneNotice(adminNoticeDTO.getIdx())).thenReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findOneNotice(adminNoticeDTO.getIdx());

        // then
        assertThat(noticeInfo.getTitle()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getDescription()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockAdminNoticeJpaService, times(1)).findOneNotice(adminNoticeDTO.getIdx());
        verify(mockAdminNoticeJpaService, atLeastOnce()).findOneNotice(adminNoticeDTO.getIdx());
        verifyNoMoreInteractions(mockAdminNoticeJpaService);

        InOrder inOrder = inOrder(mockAdminNoticeJpaService);
        inOrder.verify(mockAdminNoticeJpaService).findOneNotice(adminNoticeDTO.getIdx());
    }

    @Test
    @DisplayName("공지사항등록BDD테스트")
    void 공지사항등록BDD테스트() {
        // when
        given(mockAdminNoticeJpaService.findOneNotice(adminNoticeDTO.getIdx())).willReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findOneNotice(adminNoticeDTO.getIdx());

        // then
        assertThat(noticeInfo.getTitle()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getDescription()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockAdminNoticeJpaService).should(times(1)).findOneNotice(adminNoticeDTO.getIdx());
        then(mockAdminNoticeJpaService).should(atLeastOnce()).findOneNotice(adminNoticeDTO.getIdx());
        then(mockAdminNoticeJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공지사항수정Mockito테스트")
    void 공지사항수정Mockito테스트() {
        adminNoticeEntity = AdminNoticeEntity.builder()
                .idx(adminNoticeDTO.getIdx())
                .title("공지사항 테스트1")
                .description("공지사항 테스트1")
                .visible("Y")
                .build();

        AdminNoticeDTO updateNotice = adminNoticeJpaService.updateNotice(adminNoticeDTO.getIdx(), adminNoticeEntity);

        // when
        when(mockAdminNoticeJpaService.findOneNotice(updateNotice.getIdx())).thenReturn(updateNotice);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findOneNotice(updateNotice.getIdx());

        // then
        assertThat(noticeInfo.getTitle()).isEqualTo("공지사항 테스트1");
        assertThat(noticeInfo.getDescription()).isEqualTo("공지사항 테스트1");

        // verify
        verify(mockAdminNoticeJpaService, times(1)).findOneNotice(updateNotice.getIdx());
        verify(mockAdminNoticeJpaService, atLeastOnce()).findOneNotice(updateNotice.getIdx());
        verifyNoMoreInteractions(mockAdminNoticeJpaService);

        InOrder inOrder = inOrder(mockAdminNoticeJpaService);
        inOrder.verify(mockAdminNoticeJpaService).findOneNotice(updateNotice.getIdx());
    }

    @Test
    @DisplayName("공지사항수정BDD테스트")
    void 공지사항수정BDD테스트() {
        adminNoticeEntity = AdminNoticeEntity.builder()
                .idx(adminNoticeDTO.getIdx())
                .title("공지사항 테스트1")
                .description("공지사항 테스트1")
                .visible("Y")
                .build();

        AdminNoticeDTO updateNotice = adminNoticeJpaService.updateNotice(adminNoticeDTO.getIdx(), adminNoticeEntity);

        // when
        when(mockAdminNoticeJpaService.findOneNotice(updateNotice.getIdx())).thenReturn(updateNotice);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findOneNotice(updateNotice.getIdx());

        // then
        assertThat(noticeInfo.getTitle()).isEqualTo("공지사항 테스트1");
        assertThat(noticeInfo.getDescription()).isEqualTo("공지사항 테스트1");

        // verify
        then(mockAdminNoticeJpaService).should(times(1)).findOneNotice(updateNotice.getIdx());
        then(mockAdminNoticeJpaService).should(atLeastOnce()).findOneNotice(updateNotice.getIdx());
        then(mockAdminNoticeJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공지사항상단고정Mockito테스트")
    void 공지사항상단고정Mockito테스트() {
        Boolean fixed = adminNoticeJpaService.toggleFixed(adminNoticeDTO.getIdx());
        em.flush();
        em.clear();

        adminNoticeEntity = AdminNoticeEntity.builder()
                .idx(adminNoticeDTO.getIdx())
                .title("공지사항 테스트1")
                .description("공지사항 테스트1")
                .topFixed(fixed)
                .visible("Y")
                .build();

        AdminNoticeDTO adminNoticeDTO = AdminNoticeEntity.toDto(adminNoticeEntity);

        // when
        when(mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity.getIdx())).thenReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getTopFixed()).isTrue();

        // verify
        verify(mockAdminNoticeJpaService, times(1)).findOneNotice(adminNoticeEntity.getIdx());
        verify(mockAdminNoticeJpaService, atLeastOnce()).findOneNotice(adminNoticeEntity.getIdx());
        verifyNoMoreInteractions(mockAdminNoticeJpaService);

        InOrder inOrder = inOrder(mockAdminNoticeJpaService);
        inOrder.verify(mockAdminNoticeJpaService).findOneNotice(adminNoticeEntity.getIdx());
    }

    @Test
    @DisplayName("공지사항상단고정BDD테스트")
    void 공지사항상단고정BDD테스트() {
        // given
        Long idx = adminNoticeJpaService.insertNotice(adminNoticeEntity).getIdx();

        Boolean fixed = adminNoticeJpaService.toggleFixed(idx);
        em.flush();
        em.clear();

        adminNoticeEntity = AdminNoticeEntity.builder()
                .idx(idx)
                .title("공지사항 테스트1")
                .description("공지사항 테스트1")
                .topFixed(fixed)
                .visible("Y")
                .build();

        AdminNoticeDTO adminNoticeDTO = AdminNoticeEntity.toDto(adminNoticeEntity);

        // when
        given(mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity.getIdx())).willReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getTopFixed()).isTrue();

        // verify
        then(mockAdminNoticeJpaService).should(times(1)).findOneNotice(adminNoticeEntity.getIdx());
        then(mockAdminNoticeJpaService).should(atLeastOnce()).findOneNotice(adminNoticeEntity.getIdx());
        then(mockAdminNoticeJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공지사항삭제테스트")
    void 공지사항삭제테스트() {
        // given
        Long deleteIdx = adminNoticeJpaService.deleteNotice(adminNoticeDTO.getIdx());

        // then
        assertThat(adminNoticeDTO.getIdx()).isEqualTo(deleteIdx);
    }

    @Test
    @DisplayName("공지사항삭제Mockito테스트")
    void 공지사항삭제Mockito테스트() {
        // when
        when(mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity.getIdx())).thenReturn(adminNoticeDTO);
        Long deleteIdx = adminNoticeJpaService.deleteNotice(adminNoticeEntity.getIdx());

        // then
        assertThat(mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminNoticeJpaService, times(1)).findOneNotice(adminNoticeEntity.getIdx());
        verify(mockAdminNoticeJpaService, atLeastOnce()).findOneNotice(adminNoticeEntity.getIdx());
        verifyNoMoreInteractions(mockAdminNoticeJpaService);

        InOrder inOrder = inOrder(mockAdminNoticeJpaService);
        inOrder.verify(mockAdminNoticeJpaService).findOneNotice(adminNoticeEntity.getIdx());
    }

    @Test
    @DisplayName("공지사항삭제BDD테스트")
    void 공지사항삭제BDD테스트() {
        // when
        given(mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity.getIdx())).willReturn(adminNoticeDTO);
        Long deleteIdx = adminNoticeJpaService.deleteNotice(adminNoticeEntity.getIdx());

        // then
        assertThat(mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminNoticeJpaService).should(times(1)).findOneNotice(adminNoticeEntity.getIdx());
        then(mockAdminNoticeJpaService).should(atLeastOnce()).findOneNotice(adminNoticeEntity.getIdx());
        then(mockAdminNoticeJpaService).shouldHaveNoMoreInteractions();
    }
}

package com.tsp.api.notice.service;

import com.tsp.api.model.service.AdminModelCommonServiceTest;
import com.tsp.api.notice.domain.AdminNoticeDto;
import com.tsp.api.notice.domain.AdminNoticeEntity;
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

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.*;
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
    @Mock private AdminNoticeJpaRepository adminNoticeJpaRepository;
    @Mock private AdminNoticeJpaQueryRepository adminNoticeJpaQueryRepository;
    @InjectMocks private AdminNoticeJpaServiceImpl mockAdminNoticeJpaService;
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

        List<AdminNoticeDto> returnNoticeList = new ArrayList<>();

        returnNoticeList.add(AdminNoticeDto.builder().idx(1L).title("공지사항테스트").description("공지사항테스트").visible("Y").build());
        returnNoticeList.add(AdminNoticeDto.builder().idx(2L).title("productionTest").description("productionTest").visible("Y").build());
        Page<AdminNoticeDto> resultNotice = new PageImpl<>(returnNoticeList, pageRequest, returnNoticeList.size());

        // when
        when(adminNoticeJpaQueryRepository.findNoticeList(noticeMap, pageRequest)).thenReturn(resultNotice);
        Page<AdminNoticeDto> noticeList = mockAdminNoticeJpaService.findNoticeList(noticeMap, pageRequest);
        List<AdminNoticeDto> findNoticeList = noticeList.stream().collect(Collectors.toList());

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
        verify(adminNoticeJpaQueryRepository, times(1)).findNoticeList(noticeMap, pageRequest);
        verify(adminNoticeJpaQueryRepository, atLeastOnce()).findNoticeList(noticeMap, pageRequest);
        verifyNoMoreInteractions(adminNoticeJpaQueryRepository);

        InOrder inOrder = inOrder(adminNoticeJpaQueryRepository);
        inOrder.verify(adminNoticeJpaQueryRepository).findNoticeList(noticeMap, pageRequest);
    }

    @Test
    @DisplayName("공지사항 리스트 조회 BDD 테스트")
    void 공지사항리스트조회BDD테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<AdminNoticeDto> returnNoticeList = new ArrayList<>();

        returnNoticeList.add(AdminNoticeDto.builder().idx(1L).title("공지사항테스트").description("공지사항테스트").visible("Y").build());
        returnNoticeList.add(AdminNoticeDto.builder().idx(2L).title("productionTest").description("productionTest").visible("Y").build());
        Page<AdminNoticeDto> resultNotice = new PageImpl<>(returnNoticeList, pageRequest, returnNoticeList.size());

        // when
        given(adminNoticeJpaQueryRepository.findNoticeList(noticeMap, pageRequest)).willReturn(resultNotice);
        Page<AdminNoticeDto> noticeList = mockAdminNoticeJpaService.findNoticeList(noticeMap, pageRequest);
        List<AdminNoticeDto> findNoticeList = noticeList.stream().collect(Collectors.toList());

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
        then(adminNoticeJpaQueryRepository).should(times(1)).findNoticeList(noticeMap, pageRequest);
        then(adminNoticeJpaQueryRepository).should(atLeastOnce()).findNoticeList(noticeMap, pageRequest);
        then(adminNoticeJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공지사항상세Mockito조회테스트")
    void 공지사항상세Mockito조회테스트() {
        // when
        when(adminNoticeJpaRepository.findById(adminNoticeEntity.getIdx())).thenReturn(Optional.ofNullable(adminNoticeEntity));
        AdminNoticeDto noticeInfo = mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(adminNoticeEntity.getIdx());
        assertThat(noticeInfo.getTitle()).isEqualTo(adminNoticeEntity.getTitle());
        assertThat(noticeInfo.getDescription()).isEqualTo(adminNoticeEntity.getDescription());
        assertThat(noticeInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(adminNoticeJpaRepository, times(1)).findById(adminNoticeEntity.getIdx());
        verify(adminNoticeJpaRepository, atLeastOnce()).findById(adminNoticeEntity.getIdx());
        verifyNoMoreInteractions(adminNoticeJpaRepository);

        InOrder inOrder = inOrder(adminNoticeJpaRepository);
        inOrder.verify(adminNoticeJpaRepository).findById(adminNoticeEntity.getIdx());
    }

    @Test
    @DisplayName("공지사항상세BDD조회테스트")
    void 공지사항상세BDD조회테스트() {
        // when
        given(adminNoticeJpaRepository.findById(adminNoticeEntity.getIdx())).willReturn(Optional.ofNullable(adminNoticeEntity));
        AdminNoticeDto noticeInfo = mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(adminNoticeEntity.getIdx());
        assertThat(noticeInfo.getTitle()).isEqualTo(adminNoticeEntity.getTitle());
        assertThat(noticeInfo.getDescription()).isEqualTo(adminNoticeEntity.getDescription());
        assertThat(noticeInfo.getVisible()).isEqualTo("Y");

        // verify
        then(adminNoticeJpaRepository).should(times(1)).findById(adminNoticeEntity.getIdx());
        then(adminNoticeJpaRepository).should(atLeastOnce()).findById(adminNoticeEntity.getIdx());
        then(adminNoticeJpaRepository).shouldHaveNoMoreInteractions();
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
        AdminNoticeDto noticeInfo = mockAdminNoticeJpaService.findPrevOneNotice(adminNoticeEntity.getIdx());

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
        AdminNoticeDto noticeInfo = mockAdminNoticeJpaService.findPrevOneNotice(adminNoticeEntity.getIdx());

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
        AdminNoticeDto noticeInfo = mockAdminNoticeJpaService.findNextOneNotice(adminNoticeEntity.getIdx());

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
        AdminNoticeDto noticeInfo = mockAdminNoticeJpaService.findNextOneNotice(adminNoticeEntity.getIdx());

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
        when(adminNoticeJpaRepository.save(adminNoticeEntity)).thenReturn(adminNoticeEntity);
        AdminNoticeDto noticeInfo = mockAdminNoticeJpaService.insertNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getTitle()).isEqualTo(adminNoticeEntity.getTitle());
        assertThat(noticeInfo.getDescription()).isEqualTo(adminNoticeEntity.getDescription());
        assertThat(noticeInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(adminNoticeJpaRepository, times(1)).save(adminNoticeEntity);
        verify(adminNoticeJpaRepository, atLeastOnce()).save(adminNoticeEntity);
        verifyNoMoreInteractions(adminNoticeJpaRepository);

        InOrder inOrder = inOrder(adminNoticeJpaRepository);
        inOrder.verify(adminNoticeJpaRepository).save(adminNoticeEntity);
    }

    @Test
    @DisplayName("공지사항등록BDD테스트")
    void 공지사항등록BDD테스트() {
        // when
        given(adminNoticeJpaRepository.save(adminNoticeEntity)).willReturn(adminNoticeEntity);
        AdminNoticeDto noticeInfo = mockAdminNoticeJpaService.insertNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getTitle()).isEqualTo(adminNoticeEntity.getTitle());
        assertThat(noticeInfo.getDescription()).isEqualTo(adminNoticeEntity.getDescription());
        assertThat(noticeInfo.getVisible()).isEqualTo("Y");

        // verify
        then(adminNoticeJpaRepository).should(times(1)).save(adminNoticeEntity);
        then(adminNoticeJpaRepository).should(atLeastOnce()).save(adminNoticeEntity);
        then(adminNoticeJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공지사항수정Mockito테스트")
    void 공지사항수정Mockito테스트() {
        AdminNoticeEntity updateNoticeEntity = AdminNoticeEntity.builder()
                .idx(adminNoticeEntity.getIdx())
                .title("공지사항 테스트1")
                .description("공지사항 테스트1")
                .visible("Y")
                .build();

        // when
        when(adminNoticeJpaRepository.findById(updateNoticeEntity.getIdx())).thenReturn(Optional.of(updateNoticeEntity));
        when(adminNoticeJpaRepository.save(updateNoticeEntity)).thenReturn(updateNoticeEntity);
        AdminNoticeDto noticeInfo = mockAdminNoticeJpaService.updateNotice(updateNoticeEntity.getIdx(), updateNoticeEntity);

        // then
        assertThat(noticeInfo.getTitle()).isEqualTo(updateNoticeEntity.getTitle());
        assertThat(noticeInfo.getDescription()).isEqualTo(updateNoticeEntity.getDescription());

        // verify
        verify(adminNoticeJpaRepository, times(1)).findById(updateNoticeEntity.getIdx());
        verify(adminNoticeJpaRepository, atLeastOnce()).findById(updateNoticeEntity.getIdx());
        verifyNoMoreInteractions(adminNoticeJpaRepository);

        InOrder inOrder = inOrder(adminNoticeJpaRepository);
        inOrder.verify(adminNoticeJpaRepository).findById(updateNoticeEntity.getIdx());
    }

    @Test
    @DisplayName("공지사항수정BDD테스트")
    void 공지사항수정BDD테스트() {
        AdminNoticeEntity updateNoticeEntity = AdminNoticeEntity.builder()
                .idx(adminNoticeEntity.getIdx())
                .title("공지사항 테스트1")
                .description("공지사항 테스트1")
                .visible("Y")
                .build();

        // when
        given(adminNoticeJpaRepository.findById(updateNoticeEntity.getIdx())).willReturn(Optional.of(updateNoticeEntity));
        given(adminNoticeJpaRepository.save(updateNoticeEntity)).willReturn(updateNoticeEntity);
        AdminNoticeDto noticeInfo = mockAdminNoticeJpaService.updateNotice(updateNoticeEntity.getIdx(), updateNoticeEntity);

        // then
        assertThat(noticeInfo.getTitle()).isEqualTo(updateNoticeEntity.getTitle());
        assertThat(noticeInfo.getDescription()).isEqualTo(updateNoticeEntity.getDescription());

        // verify
        then(adminNoticeJpaRepository).should(times(1)).findById(updateNoticeEntity.getIdx());
        then(adminNoticeJpaRepository).should(atLeastOnce()).findById(updateNoticeEntity.getIdx());
        then(adminNoticeJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공지사항상단고정Mockito테스트")
    void 공지사항상단고정Mockito테스트() {
        adminNoticeEntity = AdminNoticeEntity.builder()
                .idx(adminNoticeDTO.getIdx())
                .title("공지사항 테스트1")
                .description("공지사항 테스트1")
                .topFixed(false)
                .visible("Y")
                .build();

        // when
        when(adminNoticeJpaRepository.findById(adminNoticeEntity.getIdx())).thenReturn(Optional.ofNullable(adminNoticeEntity));
        when(adminNoticeJpaRepository.save(adminNoticeEntity)).thenReturn(adminNoticeEntity);
        Boolean updateBool = mockAdminNoticeJpaService.toggleFixed(adminNoticeEntity.getIdx());

        // then
        assertThat(updateBool).isTrue();

        // verify
        verify(adminNoticeJpaRepository, times(1)).findById(adminNoticeEntity.getIdx());
        verify(adminNoticeJpaRepository, atLeastOnce()).findById(adminNoticeEntity.getIdx());
        verifyNoMoreInteractions(adminNoticeJpaRepository);

        InOrder inOrder = inOrder(adminNoticeJpaRepository);
        inOrder.verify(adminNoticeJpaRepository).findById(adminNoticeEntity.getIdx());
    }

    @Test
    @DisplayName("공지사항상단고정BDD테스트")
    void 공지사항상단고정BDD테스트() {
        adminNoticeEntity = AdminNoticeEntity.builder()
                .idx(adminNoticeDTO.getIdx())
                .title("공지사항 테스트1")
                .description("공지사항 테스트1")
                .topFixed(false)
                .visible("Y")
                .build();

        // when
        given(adminNoticeJpaRepository.findById(adminNoticeEntity.getIdx())).willReturn(Optional.ofNullable(adminNoticeEntity));
        given(adminNoticeJpaRepository.save(adminNoticeEntity)).willReturn(adminNoticeEntity);
        Boolean updateBool = mockAdminNoticeJpaService.toggleFixed(adminNoticeEntity.getIdx());

        // then
        assertThat(updateBool).isTrue();

        // verify
        then(adminNoticeJpaRepository).should(times(1)).findById(adminNoticeEntity.getIdx());
        then(adminNoticeJpaRepository).should(atLeastOnce()).findById(adminNoticeEntity.getIdx());
        then(adminNoticeJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공지사항삭제테스트")
    void 공지사항삭제테스트() {
        // given
        Long deleteIdx = adminNoticeJpaService.deleteNotice(adminNoticeDTO.getIdx());

        // then
        assertThat(adminNoticeDTO.getIdx()).isEqualTo(deleteIdx);
    }
}

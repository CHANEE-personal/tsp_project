package com.tsp.api.faq.service;

import com.tsp.api.faq.domain.AdminFaqDTO;
import com.tsp.api.faq.domain.AdminFaqEntity;
import com.tsp.api.model.service.AdminModelCommonServiceTest;
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
@DisplayName("FAQ Service Test")
class AdminFaqJpaServiceTest extends AdminModelCommonServiceTest {
    @Mock AdminFaqJpaService mockAdminFaqJpaService;
    private final AdminFaqJpaService adminFaqJpaService;

    @Test
    @DisplayName("FAQ 리스트 조회 테스트")
    void FAQ리스트조회테스트() {
        // given
        Map<String, Object> faqMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 100);

        // then
        assertThat(adminFaqJpaService.findFaqList(faqMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("FAQ 리스트 조회 Mockito 테스트")
    void FAQ리스트조회Mockito테스트() {
        // given
        Map<String, Object> faqMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminFaqDTO> returnFaqList = new ArrayList<>();

        returnFaqList.add(AdminFaqDTO.builder().idx(1L).title("FAQ테스트").description("FAQ테스트").visible("Y").build());
        returnFaqList.add(AdminFaqDTO.builder().idx(2L).title("faqTest").description("faqTest").visible("Y").build());

        Page<AdminFaqDTO> resultFaq = new PageImpl<>(returnFaqList, pageRequest, returnFaqList.size());

        // when
        when(mockAdminFaqJpaService.findFaqList(faqMap, pageRequest)).thenReturn(resultFaq);
        Page<AdminFaqDTO> faqList = mockAdminFaqJpaService.findFaqList(faqMap, pageRequest);
        List<AdminFaqDTO> findFaqList = faqList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findFaqList).isNotEmpty(),
                () -> assertThat(findFaqList).hasSize(2)
        );

        assertThat(findFaqList.get(0).getIdx()).isEqualTo(returnFaqList.get(0).getIdx());
        assertThat(findFaqList.get(0).getTitle()).isEqualTo(returnFaqList.get(0).getTitle());
        assertThat(findFaqList.get(0).getDescription()).isEqualTo(returnFaqList.get(0).getDescription());
        assertThat(findFaqList.get(0).getVisible()).isEqualTo(returnFaqList.get(0).getVisible());

        // verify
        verify(mockAdminFaqJpaService, times(1)).findFaqList(faqMap, pageRequest);
        verify(mockAdminFaqJpaService, atLeastOnce()).findFaqList(faqMap, pageRequest);
        verifyNoMoreInteractions(mockAdminFaqJpaService);

        InOrder inOrder = inOrder(mockAdminFaqJpaService);
        inOrder.verify(mockAdminFaqJpaService).findFaqList(faqMap, pageRequest);
    }

    @Test
    @DisplayName("FAQ 리스트 조회 BDD 테스트")
    void FAQ리스트조회BDD테스트() {
        // given
        Map<String, Object> faqMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminFaqDTO> returnFaqList = new ArrayList<>();

        returnFaqList.add(AdminFaqDTO.builder().idx(1L).title("FAQ테스트").description("FAQ테스트").visible("Y").build());
        returnFaqList.add(AdminFaqDTO.builder().idx(2L).title("faqTest").description("faqTest").visible("Y").build());

        Page<AdminFaqDTO> resultFaq = new PageImpl<>(returnFaqList, pageRequest, returnFaqList.size());

        // when
        given(mockAdminFaqJpaService.findFaqList(faqMap, pageRequest)).willReturn(resultFaq);
        Page<AdminFaqDTO> faqList = mockAdminFaqJpaService.findFaqList(faqMap, pageRequest);
        List<AdminFaqDTO> findFaqList = faqList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findFaqList).isNotEmpty(),
                () -> assertThat(findFaqList).hasSize(2)
        );

        assertThat(findFaqList.get(0).getIdx()).isEqualTo(returnFaqList.get(0).getIdx());
        assertThat(findFaqList.get(0).getTitle()).isEqualTo(returnFaqList.get(0).getTitle());
        assertThat(findFaqList.get(0).getDescription()).isEqualTo(returnFaqList.get(0).getDescription());
        assertThat(findFaqList.get(0).getVisible()).isEqualTo(returnFaqList.get(0).getVisible());

        // verify
        then(mockAdminFaqJpaService).should(times(1)).findFaqList(faqMap, pageRequest);
        then(mockAdminFaqJpaService).should(atLeastOnce()).findFaqList(faqMap, pageRequest);
        then(mockAdminFaqJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("FAQ상세Mockito조회테스트")
    void FAQ상세Mockito조회테스트() {
        // given
        AdminFaqEntity adminFaqEntity = AdminFaqEntity.builder()
                .idx(1L)
                .title("FAQ 테스트")
                .description("FAQ 테스트")
                .visible("Y")
                .build();

        adminFaqDTO = AdminFaqEntity.toDto(adminFaqEntity);

        // when
        when(mockAdminFaqJpaService.findOneFaq(adminFaqEntity.getIdx())).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaService.findOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockAdminFaqJpaService, times(1)).findOneFaq(adminFaqEntity.getIdx());
        verify(mockAdminFaqJpaService, atLeastOnce()).findOneFaq(adminFaqEntity.getIdx());
        verifyNoMoreInteractions(mockAdminFaqJpaService);

        InOrder inOrder = inOrder(mockAdminFaqJpaService);
        inOrder.verify(mockAdminFaqJpaService).findOneFaq(adminFaqEntity.getIdx());
    }

    @Test
    @DisplayName("FAQ상세BDD조회테스트")
    void FAQ상세BDD조회테스트() {
        // given
        AdminFaqEntity adminFaqEntity = AdminFaqEntity.builder()
                .idx(1L)
                .title("FAQ 테스트")
                .description("FAQ 테스트")
                .visible("Y")
                .build();

        adminFaqDTO = AdminFaqEntity.toDto(adminFaqEntity);

        // when
        given(mockAdminFaqJpaService.findOneFaq(adminFaqEntity.getIdx())).willReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaService.findOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockAdminFaqJpaService).should(times(1)).findOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaService).should(atLeastOnce()).findOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 FAQ 상세 조회 테스트")
    void 이전or다음FAQ상세조회테스트() {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2L).build();

        // when
        adminFaqDTO = adminFaqJpaService.findOneFaq(adminFaqEntity.getIdx());

        // 이전 FAQ
        assertThat(adminFaqJpaService.findPrevOneFaq(adminFaqEntity.getIdx()).getIdx()).isEqualTo(1);
        // 다음 FAQ
        assertThat(adminFaqJpaService.findNextOneFaq(adminFaqEntity.getIdx()).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 FAQ 상세 조회 Mockito 테스트")
    void 이전FAQ상세조회Mockito테스트() {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2L).build();

        // when
        adminFaqDTO = adminFaqJpaService.findPrevOneFaq(adminFaqEntity.getIdx());

        when(mockAdminFaqJpaService.findPrevOneFaq(adminFaqEntity.getIdx())).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaService.findPrevOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockAdminFaqJpaService, times(1)).findPrevOneFaq(adminFaqEntity.getIdx());
        verify(mockAdminFaqJpaService, atLeastOnce()).findPrevOneFaq(adminFaqEntity.getIdx());
        verifyNoMoreInteractions(mockAdminFaqJpaService);

        InOrder inOrder = inOrder(mockAdminFaqJpaService);
        inOrder.verify(mockAdminFaqJpaService).findPrevOneFaq(adminFaqEntity.getIdx());
    }

    @Test
    @DisplayName("이전 FAQ 상세 조회 BDD 테스트")
    void 이전FAQ상세조회BDD테스트() {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2L).build();

        // when
        adminFaqDTO = adminFaqJpaService.findPrevOneFaq(adminFaqEntity.getIdx());

        given(mockAdminFaqJpaService.findPrevOneFaq(adminFaqEntity.getIdx())).willReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaService.findPrevOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockAdminFaqJpaService).should(times(1)).findPrevOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaService).should(atLeastOnce()).findPrevOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 FAQ 상세 조회 Mockito 테스트")
    void 다음FAQ상세조회Mockito테스트() {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2L).build();

        // when
        adminFaqDTO = adminFaqJpaService.findNextOneFaq(adminFaqEntity.getIdx());

        when(mockAdminFaqJpaService.findNextOneFaq(adminFaqEntity.getIdx())).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaService.findNextOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockAdminFaqJpaService, times(1)).findNextOneFaq(adminFaqEntity.getIdx());
        verify(mockAdminFaqJpaService, atLeastOnce()).findNextOneFaq(adminFaqEntity.getIdx());
        verifyNoMoreInteractions(mockAdminFaqJpaService);

        InOrder inOrder = inOrder(mockAdminFaqJpaService);
        inOrder.verify(mockAdminFaqJpaService).findNextOneFaq(adminFaqEntity.getIdx());
    }

    @Test
    @DisplayName("다음 FAQ 상세 조회 BDD 테스트")
    void 다음FAQ상세조회BDD테스트() {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2L).build();

        // when
        adminFaqDTO = adminFaqJpaService.findNextOneFaq(adminFaqEntity.getIdx());

        given(mockAdminFaqJpaService.findNextOneFaq(adminFaqEntity.getIdx())).willReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaService.findNextOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockAdminFaqJpaService).should(times(1)).findNextOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaService).should(atLeastOnce()).findNextOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("FAQ등록Mockito테스트")
    void FAQ등록Mockito테스트() {
        // given
        adminFaqJpaService.insertFaq(adminFaqEntity);

        // when
        when(mockAdminFaqJpaService.findOneFaq(adminFaqEntity.getIdx())).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaService.findOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockAdminFaqJpaService, times(1)).findOneFaq(adminFaqEntity.getIdx());
        verify(mockAdminFaqJpaService, atLeastOnce()).findOneFaq(adminFaqEntity.getIdx());
        verifyNoMoreInteractions(mockAdminFaqJpaService);

        InOrder inOrder = inOrder(mockAdminFaqJpaService);
        inOrder.verify(mockAdminFaqJpaService).findOneFaq(adminFaqEntity.getIdx());
    }

    @Test
    @DisplayName("FAQ등록BDD테스트")
    void FAQ등록BDD테스트() {
        // given
        adminFaqJpaService.insertFaq(adminFaqEntity);

        // when
        given(mockAdminFaqJpaService.findOneFaq(adminFaqEntity.getIdx())).willReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaService.findOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockAdminFaqJpaService).should(times(1)).findOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaService).should(atLeastOnce()).findOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("FAQ수정Mockito테스트")
    void FAQ수정Mockito테스트() {
        // given
        Long idx = adminFaqJpaService.insertFaq(adminFaqEntity).getIdx();

        adminFaqEntity = AdminFaqEntity.builder()
                .idx(idx)
                .title("FAQ 테스트1")
                .description("FAQ 테스트1")
                .visible("Y")
                .build();

        AdminFaqDTO adminFaqDTO = AdminFaqEntity.toDto(adminFaqEntity);

        adminFaqJpaService.updateFaq(idx, adminFaqEntity);

        // when
        when(mockAdminFaqJpaService.findOneFaq(adminFaqEntity.getIdx())).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaService.findOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트1");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트1");

        // verify
        verify(mockAdminFaqJpaService, times(1)).findOneFaq(adminFaqEntity.getIdx());
        verify(mockAdminFaqJpaService, atLeastOnce()).findOneFaq(adminFaqEntity.getIdx());
        verifyNoMoreInteractions(mockAdminFaqJpaService);

        InOrder inOrder = inOrder(mockAdminFaqJpaService);
        inOrder.verify(mockAdminFaqJpaService).findOneFaq(adminFaqEntity.getIdx());
    }

    @Test
    @DisplayName("FAQ수정BDD테스트")
    void FAQ수정BDD테스트() {
        // given
        Long idx = adminFaqJpaService.insertFaq(adminFaqEntity).getIdx();

        adminFaqEntity = AdminFaqEntity.builder()
                .idx(idx)
                .title("FAQ 테스트1")
                .description("FAQ 테스트1")
                .visible("Y")
                .build();

        AdminFaqDTO adminFaqDTO = AdminFaqEntity.toDto(adminFaqEntity);

        adminFaqJpaService.updateFaq(idx, adminFaqEntity);

        // when
        when(mockAdminFaqJpaService.findOneFaq(adminFaqEntity.getIdx())).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaService.findOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트1");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트1");

        // verify
        then(mockAdminFaqJpaService).should(times(1)).findOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaService).should(atLeastOnce()).findOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("FAQ삭제테스트")
    void FAQ삭제테스트() {
        // given
        adminFaqJpaService.insertFaq(adminFaqEntity);

        Long entityIdx = adminFaqEntity.getIdx();
        Long idx = adminFaqJpaService.deleteFaq(adminFaqEntity.getIdx());

        // then
        assertThat(entityIdx).isEqualTo(idx);
    }

    @Test
    @DisplayName("FAQ삭제Mockito테스트")
    void FAQ삭제Mockito테스트() {
        // given
        adminFaqJpaService.insertFaq(adminFaqEntity);
        adminFaqDTO = AdminFaqEntity.toDto(adminFaqEntity);

        // when
        when(mockAdminFaqJpaService.findOneFaq(adminFaqEntity.getIdx())).thenReturn(adminFaqDTO);
        Long deleteIdx = adminFaqJpaService.deleteFaq(adminFaqEntity.getIdx());

        // then
        assertThat(mockAdminFaqJpaService.findOneFaq(adminFaqEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminFaqJpaService, times(1)).findOneFaq(adminFaqEntity.getIdx());
        verify(mockAdminFaqJpaService, atLeastOnce()).findOneFaq(adminFaqEntity.getIdx());
        verifyNoMoreInteractions(mockAdminFaqJpaService);

        InOrder inOrder = inOrder(mockAdminFaqJpaService);
        inOrder.verify(mockAdminFaqJpaService).findOneFaq(adminFaqEntity.getIdx());
    }

    @Test
    @DisplayName("FAQ삭제BDD테스트")
    void FAQ삭제BDD테스트() {
        // given
        adminFaqJpaService.insertFaq(adminFaqEntity);
        adminFaqDTO = AdminFaqEntity.toDto(adminFaqEntity);

        // when
        when(mockAdminFaqJpaService.findOneFaq(adminFaqEntity.getIdx())).thenReturn(adminFaqDTO);
        Long deleteIdx = adminFaqJpaService.deleteFaq(adminFaqEntity.getIdx());

        // then
        assertThat(mockAdminFaqJpaService.findOneFaq(adminFaqEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminFaqJpaService).should(times(1)).findOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaService).should(atLeastOnce()).findOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaService).shouldHaveNoMoreInteractions();
    }
}

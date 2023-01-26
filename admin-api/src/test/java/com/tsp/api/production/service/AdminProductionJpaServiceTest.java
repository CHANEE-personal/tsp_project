package com.tsp.api.production.service;

import com.tsp.api.domain.comment.AdminCommentDTO;
import com.tsp.api.domain.comment.AdminCommentEntity;
import com.tsp.api.domain.production.AdminProductionDTO;
import com.tsp.api.domain.production.AdminProductionEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("프로덕션 Service Test")
class AdminProductionJpaServiceTest {
    @Mock private AdminProductionJpaService mockAdminProductionJpaService;
    private final AdminProductionJpaService adminProductionJpaService;

    private AdminProductionEntity adminProductionEntity;
    private AdminProductionDTO adminProductionDTO;
    private AdminCommentEntity adminCommentEntity;
    private AdminCommentDTO adminCommentDTO;

    void createProduction() {
        adminProductionEntity = AdminProductionEntity.builder()
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .build();

        adminProductionDTO = AdminProductionEntity.toDto(adminProductionEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createProduction();
    }

    @Test
    @DisplayName("프로덕션 리스트 조회 테스트")
    void 프로덕션리스트조회테스트() {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        // then
        assertThat(adminProductionJpaService.findProductionList(productionMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("프로덕션 리스트 조회 Mockito 테스트")
    void 프로덕션리스트조회Mockito테스트() {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminProductionDTO> returnProductionList = new ArrayList<>();

        returnProductionList.add(AdminProductionDTO.builder().idx(1L).title("프로덕션테스트").description("프로덕션테스트").visible("Y").build());
        returnProductionList.add(AdminProductionDTO.builder().idx(2L).title("productionTest").description("productionTest").visible("Y").build());
        Page<AdminProductionDTO> resultProduction = new PageImpl<>(returnProductionList, pageRequest, returnProductionList.size());

        // when
        when(mockAdminProductionJpaService.findProductionList(productionMap, pageRequest)).thenReturn(resultProduction);
        Page<AdminProductionDTO> productionList = mockAdminProductionJpaService.findProductionList(productionMap, pageRequest);
        List<AdminProductionDTO> findProductionList = productionList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findProductionList).isNotEmpty(),
                () -> assertThat(findProductionList).hasSize(2)
        );

        assertThat(findProductionList.get(0).getIdx()).isEqualTo(returnProductionList.get(0).getIdx());
        assertThat(findProductionList.get(0).getTitle()).isEqualTo(returnProductionList.get(0).getTitle());
        assertThat(findProductionList.get(0).getDescription()).isEqualTo(returnProductionList.get(0).getDescription());
        assertThat(findProductionList.get(0).getVisible()).isEqualTo(returnProductionList.get(0).getVisible());

        // verify
        verify(mockAdminProductionJpaService, times(1)).findProductionList(productionMap, pageRequest);
        verify(mockAdminProductionJpaService, atLeastOnce()).findProductionList(productionMap, pageRequest);
        verifyNoMoreInteractions(mockAdminProductionJpaService);

        InOrder inOrder = inOrder(mockAdminProductionJpaService);
        inOrder.verify(mockAdminProductionJpaService).findProductionList(productionMap, pageRequest);
    }

    @Test
    @DisplayName("프로덕션 리스트 조회 BDD 테스트")
    void 프로덕션리스트조회BDD테스트() {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminProductionDTO> returnProductionList = new ArrayList<>();

        returnProductionList.add(AdminProductionDTO.builder().idx(1L).title("프로덕션테스트").description("프로덕션테스트").visible("Y").build());
        returnProductionList.add(AdminProductionDTO.builder().idx(2L).title("productionTest").description("productionTest").visible("Y").build());
        Page<AdminProductionDTO> resultProduction = new PageImpl<>(returnProductionList, pageRequest, returnProductionList.size());

        // when
        given(mockAdminProductionJpaService.findProductionList(productionMap, pageRequest)).willReturn(resultProduction);
        Page<AdminProductionDTO> productionList = mockAdminProductionJpaService.findProductionList(productionMap, pageRequest);
        List<AdminProductionDTO> findProductionList = productionList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findProductionList).isNotEmpty(),
                () -> assertThat(findProductionList).hasSize(2)
        );

        assertThat(findProductionList.get(0).getIdx()).isEqualTo(returnProductionList.get(0).getIdx());
        assertThat(findProductionList.get(0).getTitle()).isEqualTo(returnProductionList.get(0).getTitle());
        assertThat(findProductionList.get(0).getDescription()).isEqualTo(returnProductionList.get(0).getDescription());
        assertThat(findProductionList.get(0).getVisible()).isEqualTo(returnProductionList.get(0).getVisible());

        // verify
        then(mockAdminProductionJpaService).should(times(1)).findProductionList(productionMap, pageRequest);
        then(mockAdminProductionJpaService).should(atLeastOnce()).findProductionList(productionMap, pageRequest);
        then(mockAdminProductionJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션 상세 조회 테스트")
    void 프로덕션상세조회테스트() {
        // given
        adminProductionEntity = AdminProductionEntity.builder().idx(119L).build();

        // then
        assertThat(adminProductionJpaService.findOneProduction(adminProductionEntity.getIdx()).getTitle()).isEqualTo("하하");
    }

    @Test
    @DisplayName("프로덕션상세조회Mockito테스트")
    void 프로덕션상세조회Mockito테스트() {
        // when
        when(mockAdminProductionJpaService.findOneProduction(adminProductionEntity.getIdx())).thenReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.findOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(adminProductionDTO.getIdx());
        assertThat(productionInfo.getTitle()).isEqualTo(adminProductionDTO.getTitle());
        assertThat(productionInfo.getDescription()).isEqualTo(adminProductionDTO.getDescription());
        assertThat(productionInfo.getVisible()).isEqualTo(adminProductionDTO.getVisible());

        // verify
        verify(mockAdminProductionJpaService, times(1)).findOneProduction(adminProductionEntity.getIdx());
        verify(mockAdminProductionJpaService, atLeastOnce()).findOneProduction(adminProductionEntity.getIdx());
        verifyNoMoreInteractions(mockAdminProductionJpaService);

        InOrder inOrder = inOrder(mockAdminProductionJpaService);
        inOrder.verify(mockAdminProductionJpaService).findOneProduction(adminProductionEntity.getIdx());
    }

    @Test
    @DisplayName("프로덕션상세조회BDD테스트")
    void 프로덕션상세조회BDD테스트() {
        // when
        given(mockAdminProductionJpaService.findOneProduction(adminProductionEntity.getIdx())).willReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.findOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(adminProductionDTO.getIdx());
        assertThat(productionInfo.getTitle()).isEqualTo(adminProductionDTO.getTitle());
        assertThat(productionInfo.getDescription()).isEqualTo(adminProductionDTO.getDescription());
        assertThat(productionInfo.getVisible()).isEqualTo(adminProductionDTO.getVisible());

        // verify
        then(mockAdminProductionJpaService).should(times(1)).findOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaService).should(atLeastOnce()).findOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 프로덕션 상세 조회 Mockito 테스트")
    void 이전프로덕션상세조회Mockito테스트() {
        // given
        adminProductionEntity = AdminProductionEntity.builder().idx(118L).build();

        // when
        adminProductionDTO = adminProductionJpaService.findOneProduction(adminProductionEntity.getIdx());

        when(mockAdminProductionJpaService.findPrevOneProduction(adminProductionEntity.getIdx())).thenReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.findPrevOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(117);

        // verify
        verify(mockAdminProductionJpaService, times(1)).findPrevOneProduction(adminProductionEntity.getIdx());
        verify(mockAdminProductionJpaService, atLeastOnce()).findPrevOneProduction(adminProductionEntity.getIdx());
        verifyNoMoreInteractions(mockAdminProductionJpaService);

        InOrder inOrder = inOrder(mockAdminProductionJpaService);
        inOrder.verify(mockAdminProductionJpaService).findPrevOneProduction(adminProductionEntity.getIdx());
    }

    @Test
    @DisplayName("이전 프로덕션 상세 조회 BDD 테스트")
    void 이전프로덕션상세조회BDD테스트() {
        // given
        adminProductionEntity = AdminProductionEntity.builder().idx(118L).build();

        // when
        adminProductionDTO = adminProductionJpaService.findOneProduction(adminProductionEntity.getIdx());

        given(mockAdminProductionJpaService.findPrevOneProduction(adminProductionEntity.getIdx())).willReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.findPrevOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(117);

        // verify
        then(mockAdminProductionJpaService).should(times(1)).findPrevOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaService).should(atLeastOnce()).findPrevOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 프로덕션 상세 조회 Mockito 테스트")
    void 다음프로덕션상세조회Mockito테스트() {
        // given
        adminProductionEntity = AdminProductionEntity.builder().idx(118L).build();

        // when
        adminProductionDTO = adminProductionJpaService.findOneProduction(adminProductionEntity.getIdx());

        when(mockAdminProductionJpaService.findNextOneProduction(adminProductionEntity.getIdx())).thenReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.findNextOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(119);

        // verify
        verify(mockAdminProductionJpaService, times(1)).findNextOneProduction(adminProductionEntity.getIdx());
        verify(mockAdminProductionJpaService, atLeastOnce()).findNextOneProduction(adminProductionEntity.getIdx());
        verifyNoMoreInteractions(mockAdminProductionJpaService);

        InOrder inOrder = inOrder(mockAdminProductionJpaService);
        inOrder.verify(mockAdminProductionJpaService).findNextOneProduction(adminProductionEntity.getIdx());
    }

    @Test
    @DisplayName("다음 프로덕션 상세 조회 BDD 테스트")
    void 다음프로덕션상세조회BDD테스트() {
        // given
        adminProductionEntity = AdminProductionEntity.builder().idx(118L).build();

        // when
        adminProductionDTO = adminProductionJpaService.findOneProduction(adminProductionEntity.getIdx());

        given(mockAdminProductionJpaService.findNextOneProduction(adminProductionEntity.getIdx())).willReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.findNextOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(119);

        // verify
        then(mockAdminProductionJpaService).should(times(1)).findNextOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaService).should(atLeastOnce()).findNextOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션 등록 Mockito 테스트")
    void 프로덕션등록Mockito테스트() {
        // given
        adminProductionJpaService.insertProduction(adminProductionEntity);

        // when
        when(mockAdminProductionJpaService.findOneProduction(adminProductionEntity.getIdx())).thenReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.findOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getTitle()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getDescription()).isEqualTo("프로덕션 테스트");

        // verify
        verify(mockAdminProductionJpaService, times(1)).findOneProduction(adminProductionEntity.getIdx());
        verify(mockAdminProductionJpaService, atLeastOnce()).findOneProduction(adminProductionEntity.getIdx());
        verifyNoMoreInteractions(mockAdminProductionJpaService);

        InOrder inOrder = inOrder(mockAdminProductionJpaService);
        inOrder.verify(mockAdminProductionJpaService).findOneProduction(adminProductionEntity.getIdx());
    }

    @Test
    @DisplayName("프로덕션 등록 BDD 테스트")
    void 프로덕션등록BDD테스트() {
        // given
        adminProductionJpaService.insertProduction(adminProductionEntity);

        // when
        given(mockAdminProductionJpaService.findOneProduction(adminProductionEntity.getIdx())).willReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.findOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getTitle()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getDescription()).isEqualTo("프로덕션 테스트");

        // verify
        then(mockAdminProductionJpaService).should(times(1)).findOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaService).should(atLeastOnce()).findOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션 수정 Mockito 테스트")
    void 프로덕션수정Mockito테스트() {
        // given
        Long idx = adminProductionJpaService.insertProduction(adminProductionEntity).getIdx();

        adminProductionEntity = AdminProductionEntity.builder()
                .idx(idx)
                .title("프로덕션 테스트1")
                .description("프로덕션 테스트1")
                .visible("Y")
                .build();

        AdminProductionDTO adminProductionDTO = AdminProductionEntity.toDto(adminProductionEntity);

        adminProductionJpaService.updateProduction(idx, adminProductionEntity);

        // when
        when(mockAdminProductionJpaService.findOneProduction(adminProductionEntity.getIdx())).thenReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.findOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getTitle()).isEqualTo("프로덕션 테스트1");
        assertThat(productionInfo.getDescription()).isEqualTo("프로덕션 테스트1");

        // verify
        verify(mockAdminProductionJpaService, times(1)).findOneProduction(adminProductionEntity.getIdx());
        verify(mockAdminProductionJpaService, atLeastOnce()).findOneProduction(adminProductionEntity.getIdx());
        verifyNoMoreInteractions(mockAdminProductionJpaService);

        InOrder inOrder = inOrder(mockAdminProductionJpaService);
        inOrder.verify(mockAdminProductionJpaService).findOneProduction(adminProductionEntity.getIdx());
    }

    @Test
    @DisplayName("프로덕션 수정 BDD 테스트")
    void 프로덕션수정BDD테스트() {
        // given
        Long idx = adminProductionJpaService.insertProduction(adminProductionEntity).getIdx();

        adminProductionEntity = AdminProductionEntity.builder()
                .idx(idx)
                .title("프로덕션 테스트1")
                .description("프로덕션 테스트1")
                .visible("Y")
                .build();

        AdminProductionDTO adminProductionDTO = AdminProductionEntity.toDto(adminProductionEntity);

        adminProductionJpaService.updateProduction(idx, adminProductionEntity);

        // when
        given(mockAdminProductionJpaService.findOneProduction(adminProductionEntity.getIdx())).willReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.findOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getTitle()).isEqualTo("프로덕션 테스트1");
        assertThat(productionInfo.getDescription()).isEqualTo("프로덕션 테스트1");

        // verify
        then(mockAdminProductionJpaService).should(times(1)).findOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaService).should(atLeastOnce()).findOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션 삭제 테스트")
    void 프로덕션삭제테스트() {
        // given
        Long idx = adminProductionJpaService.insertProduction(adminProductionEntity).getIdx();

        // then
        assertThat(adminProductionJpaService.deleteProduction(idx)).isNotNull();
    }
}

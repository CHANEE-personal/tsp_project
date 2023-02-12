package com.tsp.api.production.service;

import com.tsp.api.model.service.AdminModelCommonServiceTest;
import com.tsp.api.production.domain.AdminProductionDTO;
import com.tsp.api.production.domain.AdminProductionEntity;
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
class AdminProductionJpaServiceTest extends AdminModelCommonServiceTest {
    @Mock private AdminProductionJpaRepository adminProductionJpaRepository;
    @Mock private AdminProductionJpaQueryRepository adminProductionJpaQueryRepository;
    @InjectMocks private AdminProductionJpaServiceImpl mockAdminProductionJpaService;
    private final AdminProductionJpaService adminProductionJpaService;

    @Test
    @DisplayName("프로덕션 리스트 조회 테스트")
    void 프로덕션리스트조회테스트() {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        // then
        assertThat(adminProductionJpaService.findProductionList(productionMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("프로덕션 리스트 조회 Mockito 테스트")
    void 프로덕션리스트조회Mockito테스트() {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<AdminProductionDTO> returnProductionList = new ArrayList<>();

        returnProductionList.add(AdminProductionDTO.builder().idx(1L).title("프로덕션테스트").description("프로덕션테스트").visible("Y").build());
        returnProductionList.add(AdminProductionDTO.builder().idx(2L).title("productionTest").description("productionTest").visible("Y").build());
        Page<AdminProductionDTO> resultProduction = new PageImpl<>(returnProductionList, pageRequest, returnProductionList.size());

        // when
        when(adminProductionJpaQueryRepository.findProductionList(productionMap, pageRequest)).thenReturn(resultProduction);
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
        verify(adminProductionJpaQueryRepository, times(1)).findProductionList(productionMap, pageRequest);
        verify(adminProductionJpaQueryRepository, atLeastOnce()).findProductionList(productionMap, pageRequest);
        verifyNoMoreInteractions(adminProductionJpaQueryRepository);

        InOrder inOrder = inOrder(adminProductionJpaQueryRepository);
        inOrder.verify(adminProductionJpaQueryRepository).findProductionList(productionMap, pageRequest);
    }

    @Test
    @DisplayName("프로덕션 리스트 조회 BDD 테스트")
    void 프로덕션리스트조회BDD테스트() {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<AdminProductionDTO> returnProductionList = new ArrayList<>();

        returnProductionList.add(AdminProductionDTO.builder().idx(1L).title("프로덕션테스트").description("프로덕션테스트").visible("Y").build());
        returnProductionList.add(AdminProductionDTO.builder().idx(2L).title("productionTest").description("productionTest").visible("Y").build());
        Page<AdminProductionDTO> resultProduction = new PageImpl<>(returnProductionList, pageRequest, returnProductionList.size());

        // when
        given(adminProductionJpaQueryRepository.findProductionList(productionMap, pageRequest)).willReturn(resultProduction);
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
        then(adminProductionJpaQueryRepository).should(times(1)).findProductionList(productionMap, pageRequest);
        then(adminProductionJpaQueryRepository).should(atLeastOnce()).findProductionList(productionMap, pageRequest);
        then(adminProductionJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션 상세 조회 테스트")
    void 프로덕션상세조회테스트() {
        // then
        assertThat(adminProductionJpaService.findOneProduction(adminProductionEntity.getIdx())).isNotNull();
    }

    @Test
    @DisplayName("프로덕션상세조회Mockito테스트")
    void 프로덕션상세조회Mockito테스트() {
        // when
        when(adminProductionJpaRepository.findByIdx(adminProductionEntity.getIdx())).thenReturn(Optional.ofNullable(adminProductionEntity));
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.findOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(adminProductionEntity.getIdx());
        assertThat(productionInfo.getTitle()).isEqualTo(adminProductionEntity.getTitle());
        assertThat(productionInfo.getDescription()).isEqualTo(adminProductionEntity.getDescription());
        assertThat(productionInfo.getVisible()).isEqualTo(adminProductionEntity.getVisible());

        // verify
        verify(adminProductionJpaRepository, times(1)).findByIdx(adminProductionEntity.getIdx());
        verify(adminProductionJpaRepository, atLeastOnce()).findByIdx(adminProductionEntity.getIdx());
        verifyNoMoreInteractions(adminProductionJpaRepository);

        InOrder inOrder = inOrder(adminProductionJpaRepository);
        inOrder.verify(adminProductionJpaRepository).findByIdx(adminProductionEntity.getIdx());
    }

    @Test
    @DisplayName("프로덕션상세조회BDD테스트")
    void 프로덕션상세조회BDD테스트() {
        // when
        given(adminProductionJpaRepository.findByIdx(adminProductionEntity.getIdx())).willReturn(Optional.ofNullable(adminProductionEntity));
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.findOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(adminProductionEntity.getIdx());
        assertThat(productionInfo.getTitle()).isEqualTo(adminProductionEntity.getTitle());
        assertThat(productionInfo.getDescription()).isEqualTo(adminProductionEntity.getDescription());
        assertThat(productionInfo.getVisible()).isEqualTo(adminProductionEntity.getVisible());

        // verify
        then(adminProductionJpaRepository).should(times(1)).findByIdx(adminProductionEntity.getIdx());
        then(adminProductionJpaRepository).should(atLeastOnce()).findByIdx(adminProductionEntity.getIdx());
        then(adminProductionJpaRepository).shouldHaveNoMoreInteractions();
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
        // when
        when(adminProductionJpaRepository.save(adminProductionEntity)).thenReturn(adminProductionEntity);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.insertProduction(adminProductionEntity);

        // then
        assertThat(productionInfo.getTitle()).isEqualTo(adminProductionEntity.getTitle());
        assertThat(productionInfo.getDescription()).isEqualTo(adminProductionEntity.getDescription());

        // verify
        verify(adminProductionJpaRepository, times(1)).save(adminProductionEntity);
        verify(adminProductionJpaRepository, atLeastOnce()).save(adminProductionEntity);
        verifyNoMoreInteractions(adminProductionJpaRepository);

        InOrder inOrder = inOrder(adminProductionJpaRepository);
        inOrder.verify(adminProductionJpaRepository).save(adminProductionEntity);
    }

    @Test
    @DisplayName("프로덕션 등록 BDD 테스트")
    void 프로덕션등록BDD테스트() {
        // when
        given(adminProductionJpaRepository.save(adminProductionEntity)).willReturn(adminProductionEntity);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.insertProduction(adminProductionEntity);

        // then
        assertThat(productionInfo.getTitle()).isEqualTo(adminProductionEntity.getTitle());
        assertThat(productionInfo.getDescription()).isEqualTo(adminProductionEntity.getDescription());

        // verify
        then(adminProductionJpaRepository).should(times(1)).save(adminProductionEntity);
        then(adminProductionJpaRepository).should(atLeastOnce()).save(adminProductionEntity);
        then(adminProductionJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션 수정 Mockito 테스트")
    void 프로덕션수정Mockito테스트() {
        // given
        AdminProductionEntity updateProduction = AdminProductionEntity.builder()
                .idx(adminProductionDTO.getIdx())
                .title("프로덕션 테스트1")
                .description("프로덕션 테스트1")
                .visible("Y")
                .build();

        // when
        when(adminProductionJpaRepository.findById(updateProduction.getIdx())).thenReturn(Optional.of(updateProduction));
        when(adminProductionJpaRepository.save(updateProduction)).thenReturn(updateProduction);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.updateProduction(updateProduction.getIdx(), updateProduction);

        // then
        assertThat(productionInfo.getTitle()).isEqualTo(updateProduction.getTitle());
        assertThat(productionInfo.getDescription()).isEqualTo(updateProduction.getDescription());

        // verify
        verify(adminProductionJpaRepository, times(1)).findById(updateProduction.getIdx());
        verify(adminProductionJpaRepository, atLeastOnce()).findById(updateProduction.getIdx());
        verifyNoMoreInteractions(adminProductionJpaRepository);

        InOrder inOrder = inOrder(adminProductionJpaRepository);
        inOrder.verify(adminProductionJpaRepository).findById(updateProduction.getIdx());
    }

    @Test
    @DisplayName("프로덕션 수정 BDD 테스트")
    void 프로덕션수정BDD테스트() {
        // given
        AdminProductionEntity updateProduction = AdminProductionEntity.builder()
                .idx(adminProductionDTO.getIdx())
                .title("프로덕션 테스트1")
                .description("프로덕션 테스트1")
                .visible("Y")
                .build();

        // when
        given(adminProductionJpaRepository.findById(updateProduction.getIdx())).willReturn(Optional.of(updateProduction));
        given(adminProductionJpaRepository.save(updateProduction)).willReturn(updateProduction);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.updateProduction(updateProduction.getIdx(), updateProduction);

        // then
        assertThat(productionInfo.getTitle()).isEqualTo(updateProduction.getTitle());
        assertThat(productionInfo.getDescription()).isEqualTo(updateProduction.getDescription());

        // verify
        then(adminProductionJpaRepository).should(times(1)).findById(updateProduction.getIdx());
        then(adminProductionJpaRepository).should(atLeastOnce()).findById(updateProduction.getIdx());
        then(adminProductionJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션 삭제 테스트")
    void 프로덕션삭제테스트() {
        // then
        assertThat(adminProductionJpaService.deleteProduction(adminProductionDTO.getIdx())).isNotNull();
    }
}

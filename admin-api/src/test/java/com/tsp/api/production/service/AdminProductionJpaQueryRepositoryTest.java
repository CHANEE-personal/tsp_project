package com.tsp.api.production.service;

import com.tsp.api.common.EntityType;
import com.tsp.api.domain.comment.AdminCommentDTO;
import com.tsp.api.domain.comment.AdminCommentEntity;
import com.tsp.api.domain.common.CommonImageDTO;
import com.tsp.api.domain.common.CommonImageEntity;
import com.tsp.api.domain.production.AdminProductionDTO;
import com.tsp.api.domain.production.AdminProductionEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@Slf4j
@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("프로덕션 Repository Test")
class AdminProductionJpaQueryRepositoryTest {
    @Mock private AdminProductionJpaQueryRepository mockAdminProductionJpaQueryRepository;
    private final AdminProductionJpaQueryRepository adminProductionJpaQueryRepository;
    private final AdminProductionJpaRepository adminProductionJpaRepository;

    private AdminProductionEntity adminProductionEntity;
    private AdminProductionDTO adminProductionDTO;
    private CommonImageEntity commonImageEntity;
    private CommonImageDTO commonImageDTO;
    private AdminCommentEntity adminCommentEntity;
    private AdminCommentDTO adminCommentDTO;

    void createProductionAndImage() {
        adminProductionEntity = AdminProductionEntity.builder()
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .build();

        adminProductionDTO = AdminProductionEntity.toDto(adminProductionEntity);

        commonImageEntity = CommonImageEntity.builder()
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1L)
                .typeName(EntityType.PRODUCTION)
                .visible("Y")
                .build();

        commonImageDTO = CommonImageEntity.toDto(commonImageEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createProductionAndImage();
    }

    @Test
    @DisplayName("프로덕션리스트조회테스트")
    void 프로덕션리스트조회테스트() {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        // then
        assertThat(adminProductionJpaQueryRepository.findProductionList(productionMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("프로덕션상세조회테스트")
    void 프로덕션상세조회테스트() {
        // given
        adminProductionEntity = AdminProductionEntity.builder().idx(1L).build();

        // when
        adminProductionDTO = adminProductionJpaQueryRepository.findOneProduction(adminProductionEntity.getIdx());

        // then
        assertAll(() -> assertThat(adminProductionDTO.getIdx()).isEqualTo(1),
                () -> {
                    assertThat(adminProductionDTO.getTitle()).isEqualTo("테스트1");
                    assertNotNull(adminProductionDTO.getTitle());
                },
                () -> {
                    assertThat(adminProductionDTO.getDescription()).isEqualTo("테스트1");
                    assertNotNull(adminProductionDTO.getDescription());
                },
                () -> {
                    assertThat(adminProductionDTO.getVisible()).isEqualTo("Y");
                    assertNotNull(adminProductionDTO.getVisible());
                });

        assertThat(adminProductionDTO.getProductionImage().get(0).getTypeName()).isEqualTo("production");
        assertThat(adminProductionDTO.getProductionImage().get(0).getImageType()).isEqualTo("main");
        assertThat(adminProductionDTO.getProductionImage().get(0).getFileName()).isEqualTo("52d4fdc8-f109-408e-b243-85cc1be207c5.jpg");
        assertThat(adminProductionDTO.getProductionImage().get(0).getFileMask()).isEqualTo("1223024921206.jpg");
    }

    @Test
    @DisplayName("프로덕션Mockito조회테스트")
    void 프로덕션Mockito조회테스트() {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminProductionDTO> productionList = new ArrayList<>();
        productionList.add(AdminProductionDTO.builder().idx(1L).title("프로덕션 테스트")
                .description("프로덕션 테스트").productionImage(commonImageDtoList).build());
        Page<AdminProductionDTO> resultProduction = new PageImpl<>(productionList, pageRequest, productionList.size());

        // when
        when(mockAdminProductionJpaQueryRepository.findProductionList(productionMap, pageRequest)).thenReturn(resultProduction);
        Page<AdminProductionDTO> newProductionList = mockAdminProductionJpaQueryRepository.findProductionList(productionMap, pageRequest);
        List<AdminProductionDTO> findProductionList = newProductionList.stream().collect(Collectors.toList());

        // then
        assertThat(findProductionList.get(0).getIdx()).isEqualTo(productionList.get(0).getIdx());
        assertThat(findProductionList.get(0).getTitle()).isEqualTo(productionList.get(0).getTitle());
        assertThat(findProductionList.get(0).getDescription()).isEqualTo(productionList.get(0).getDescription());
        assertThat(findProductionList.get(0).getVisible()).isEqualTo(productionList.get(0).getVisible());

        // verify
        verify(mockAdminProductionJpaQueryRepository, times(1)).findProductionList(productionMap, pageRequest);
        verify(mockAdminProductionJpaQueryRepository, atLeastOnce()).findProductionList(productionMap, pageRequest);
        verifyNoMoreInteractions(mockAdminProductionJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminProductionJpaQueryRepository);
        inOrder.verify(mockAdminProductionJpaQueryRepository).findProductionList(productionMap, pageRequest);
    }

    @Test
    @DisplayName("프로덕션BDD조회테스트")
    void 프로덕션BDD조회테스트() {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminProductionDTO> productionList = new ArrayList<>();
        productionList.add(AdminProductionDTO.builder().idx(1L).title("프로덕션 테스트")
                .description("프로덕션 테스트").productionImage(commonImageDtoList).build());
        Page<AdminProductionDTO> resultProduction = new PageImpl<>(productionList, pageRequest, productionList.size());

        // when
        given(mockAdminProductionJpaQueryRepository.findProductionList(productionMap, pageRequest)).willReturn(resultProduction);
        Page<AdminProductionDTO> newProductionList = mockAdminProductionJpaQueryRepository.findProductionList(productionMap, pageRequest);
        List<AdminProductionDTO> findProductionList = newProductionList.stream().collect(Collectors.toList());

        // then
        assertThat(findProductionList.get(0).getIdx()).isEqualTo(productionList.get(0).getIdx());
        assertThat(findProductionList.get(0).getTitle()).isEqualTo(productionList.get(0).getTitle());
        assertThat(findProductionList.get(0).getDescription()).isEqualTo(productionList.get(0).getDescription());
        assertThat(findProductionList.get(0).getVisible()).isEqualTo(productionList.get(0).getVisible());

        // verify
        then(mockAdminProductionJpaQueryRepository).should(times(1)).findProductionList(productionMap, pageRequest);
        then(mockAdminProductionJpaQueryRepository).should(atLeastOnce()).findProductionList(productionMap, pageRequest);
        then(mockAdminProductionJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션상세Mockito조회테스트")
    void 프로덕션상세Mockito조회테스트() {
        // given
        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        AdminProductionEntity adminProductionEntity = AdminProductionEntity.builder().idx(1L).commonImageEntityList(commonImageEntityList).build();

        AdminProductionDTO adminProductionDTO = AdminProductionDTO.builder()
                .idx(1L)
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .productionImage(commonImageEntityList.stream().map(CommonImageEntity::toDto).collect(Collectors.toList()))
                .build();

        // when
        when(mockAdminProductionJpaQueryRepository.findOneProduction(adminProductionEntity.getIdx())).thenReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaQueryRepository.findOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(1);
        assertThat(productionInfo.getTitle()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getDescription()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getVisible()).isEqualTo("Y");
        assertThat(productionInfo.getProductionImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(productionInfo.getProductionImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(productionInfo.getProductionImage().get(0).getFilePath()).isEqualTo("/test/test.jpg");
        assertThat(productionInfo.getProductionImage().get(0).getImageType()).isEqualTo("main");
        assertThat(productionInfo.getProductionImage().get(0).getTypeName()).isEqualTo(EntityType.PRODUCTION);

        // verify
        verify(mockAdminProductionJpaQueryRepository, times(1)).findOneProduction(adminProductionEntity.getIdx());
        verify(mockAdminProductionJpaQueryRepository, atLeastOnce()).findOneProduction(adminProductionEntity.getIdx());
        verifyNoMoreInteractions(mockAdminProductionJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminProductionJpaQueryRepository);
        inOrder.verify(mockAdminProductionJpaQueryRepository).findOneProduction(adminProductionEntity.getIdx());
    }

    @Test
    @DisplayName("프로덕션상세BDD조회테스트")
    void 프로덕션상세BDD조회테스트() {
        // given
        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        AdminProductionEntity adminProductionEntity = AdminProductionEntity.builder().idx(1L).commonImageEntityList(commonImageEntityList).build();

        AdminProductionDTO adminProductionDTO = AdminProductionDTO.builder()
                .idx(1L)
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .productionImage(commonImageEntityList.stream().map(CommonImageEntity::toDto).collect(Collectors.toList()))
                .build();

        // when
        given(mockAdminProductionJpaQueryRepository.findOneProduction(adminProductionEntity.getIdx())).willReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaQueryRepository.findOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(1);
        assertThat(productionInfo.getTitle()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getDescription()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getVisible()).isEqualTo("Y");
        assertThat(productionInfo.getProductionImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(productionInfo.getProductionImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(productionInfo.getProductionImage().get(0).getFilePath()).isEqualTo("/test/test.jpg");
        assertThat(productionInfo.getProductionImage().get(0).getImageType()).isEqualTo("main");
        assertThat(productionInfo.getProductionImage().get(0).getTypeName()).isEqualTo(EntityType.PRODUCTION);

        // verify
        then(mockAdminProductionJpaQueryRepository).should(times(1)).findOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaQueryRepository).should(atLeastOnce()).findOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 프로덕션 상세 조회 테스트")
    void 이전or다음프로덕션상세조회테스트() {
        // given
        adminProductionEntity = AdminProductionEntity.builder().idx(118L).build();

        // when
        adminProductionDTO = adminProductionJpaQueryRepository.findOneProduction(adminProductionEntity.getIdx());

        // 이전 프로덕션
        assertThat(adminProductionJpaQueryRepository.findPrevOneProduction(adminProductionEntity.getIdx()).getIdx()).isEqualTo(117);
        // 다음 프로덕션
        assertThat(adminProductionJpaQueryRepository.findNextOneProduction(adminProductionEntity.getIdx()).getIdx()).isEqualTo(119);
    }

    @Test
    @DisplayName("이전 프로덕션 상세 조회 Mockito 테스트")
    void 이전프로덕션상세조회Mockito테스트() {
        // given
        adminProductionEntity = AdminProductionEntity.builder().idx(118L).build();

        // when
        adminProductionDTO = adminProductionJpaQueryRepository.findOneProduction(adminProductionEntity.getIdx());

        when(mockAdminProductionJpaQueryRepository.findPrevOneProduction(adminProductionEntity.getIdx())).thenReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaQueryRepository.findPrevOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(117);

        // verify
        verify(mockAdminProductionJpaQueryRepository, times(1)).findPrevOneProduction(adminProductionEntity.getIdx());
        verify(mockAdminProductionJpaQueryRepository, atLeastOnce()).findPrevOneProduction(adminProductionEntity.getIdx());
        verifyNoMoreInteractions(mockAdminProductionJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminProductionJpaQueryRepository);
        inOrder.verify(mockAdminProductionJpaQueryRepository).findPrevOneProduction(adminProductionEntity.getIdx());
    }

    @Test
    @DisplayName("이전 프로덕션 상세 조회 BDD 테스트")
    void 이전프로덕션상세조회BDD테스트() {
        // given
        adminProductionEntity = AdminProductionEntity.builder().idx(118L).build();

        // when
        adminProductionDTO = adminProductionJpaQueryRepository.findOneProduction(adminProductionEntity.getIdx());

        given(mockAdminProductionJpaQueryRepository.findPrevOneProduction(adminProductionEntity.getIdx())).willReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaQueryRepository.findPrevOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(117);

        // verify
        then(mockAdminProductionJpaQueryRepository).should(times(1)).findPrevOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaQueryRepository).should(atLeastOnce()).findPrevOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 프로덕션 상세 조회 Mockito 테스트")
    void 다음프로덕션상세조회Mockito테스트() {
        // given
        adminProductionEntity = AdminProductionEntity.builder().idx(118L).build();

        // when
        adminProductionDTO = adminProductionJpaQueryRepository.findOneProduction(adminProductionEntity.getIdx());

        when(mockAdminProductionJpaQueryRepository.findNextOneProduction(adminProductionEntity.getIdx())).thenReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaQueryRepository.findNextOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(119);

        // verify
        verify(mockAdminProductionJpaQueryRepository, times(1)).findNextOneProduction(adminProductionEntity.getIdx());
        verify(mockAdminProductionJpaQueryRepository, atLeastOnce()).findNextOneProduction(adminProductionEntity.getIdx());
        verifyNoMoreInteractions(mockAdminProductionJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminProductionJpaQueryRepository);
        inOrder.verify(mockAdminProductionJpaQueryRepository).findNextOneProduction(adminProductionEntity.getIdx());
    }

    @Test
    @DisplayName("다음 프로덕션 상세 조회 BDD 테스트")
    void 다음프로덕션상세조회BDD테스트() {
        // given
        adminProductionEntity = AdminProductionEntity.builder().idx(118L).build();

        // when
        adminProductionDTO = adminProductionJpaQueryRepository.findOneProduction(adminProductionEntity.getIdx());

        given(mockAdminProductionJpaQueryRepository.findNextOneProduction(adminProductionEntity.getIdx())).willReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaQueryRepository.findNextOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(119);

        // verify
        then(mockAdminProductionJpaQueryRepository).should(times(1)).findNextOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaQueryRepository).should(atLeastOnce()).findNextOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaQueryRepository).shouldHaveNoMoreInteractions();
    }
}

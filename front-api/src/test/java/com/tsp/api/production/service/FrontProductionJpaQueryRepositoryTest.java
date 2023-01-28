package com.tsp.api.production.service;

import com.tsp.api.common.domain.CommonImageDTO;
import com.tsp.api.common.domain.CommonImageEntity;
import com.tsp.api.production.domain.FrontProductionDTO;
import com.tsp.api.production.domain.FrontProductionEntity;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-local.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("프로덕션 Repository Test")
class FrontProductionJpaRepositoryTest {
    @Mock private FrontProductionJpaQueryRepository mockFrontProductionJpaRepository;
    private final FrontProductionJpaQueryRepository frontProductionJpaRepository;

    private FrontProductionEntity frontProductionEntity;
    private FrontProductionDTO frontProductionDTO;
    private CommonImageEntity commonImageEntity;
    private CommonImageDTO commonImageDTO;
    List<CommonImageEntity> commonImageEntityList = new ArrayList<>();

    private final EntityManager em;

    private void createProduction() {
        commonImageEntity = CommonImageEntity.builder()
                .idx(1L)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1L)
                .typeName("production")
                .build();

        commonImageDTO = CommonImageEntity.toDto(commonImageEntity);

        commonImageEntityList.add(commonImageEntity);

        frontProductionEntity = FrontProductionEntity.builder()
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .viewCount(1)
//                .commonImageEntityList(commonImageEntityList)
                .build();

        frontProductionDTO = FrontProductionEntity.toDto(frontProductionEntity);

//        frontProductionDTO = FrontProductionDTO.builder()
//                .idx(1)
//                .title("프로덕션 테스트")
//                .description("프로덕션 테스트")
//                .visible("Y")
//                .viewCount(1)
//                .productionImage(INSTANCE.toDtoList(commonImageEntityList))
//                .build();

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
        productionMap.put("searchType", 0);
        productionMap.put("searchKeyword", "하하");
        PageRequest pageRequest = PageRequest.of(1, 3);

        assertThat(frontProductionJpaRepository.findProductionList(productionMap, pageRequest)).isNotEmpty();
        // then
    }

    @Test
    @DisplayName("프로덕션 리스트 조회 Mockito 테스트")
    void 프로덕션리스트조회Mockito테스트() {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<FrontProductionDTO> productionList = new ArrayList<>();
        productionList.add(FrontProductionDTO.builder().idx(1L).title("프로덕션").description("프로덕션").visible("Y")
                .productionImage(commonImageDtoList).build());
        Page<FrontProductionDTO> resultProduction = new PageImpl<>(productionList, pageRequest, productionList.size());

        // when
        when(mockFrontProductionJpaRepository.findProductionList(productionMap, pageRequest)).thenReturn(resultProduction);
        Page<FrontProductionDTO> newProductionList = mockFrontProductionJpaRepository.findProductionList(productionMap, pageRequest);
        List<FrontProductionDTO> findProductionList = newProductionList.stream().collect(Collectors.toList());

        // then
        assertThat(findProductionList.get(0).getIdx()).isEqualTo(productionList.get(0).getIdx());
        assertThat(findProductionList.get(0).getTitle()).isEqualTo(productionList.get(0).getTitle());
        assertThat(findProductionList.get(0).getDescription()).isEqualTo(productionList.get(0).getDescription());
        assertThat(findProductionList.get(0).getProductionImage().get(0).getFileName()).isEqualTo(productionList.get(0).getProductionImage().get(0).getFileName());
        assertThat(findProductionList.get(0).getProductionImage().get(0).getTypeName()).isEqualTo(productionList.get(0).getProductionImage().get(0).getTypeName());

        // verify
        verify(mockFrontProductionJpaRepository, times(1)).findProductionList(productionMap, pageRequest);
        verify(mockFrontProductionJpaRepository, atLeastOnce()).findProductionList(productionMap, pageRequest);
        verifyNoMoreInteractions(mockFrontProductionJpaRepository);

        InOrder inOrder = inOrder(mockFrontProductionJpaRepository);
        inOrder.verify(mockFrontProductionJpaRepository).findProductionList(productionMap, pageRequest);
    }

    @Test
    @DisplayName("프로덕션 리스트 조회 BDD 테스트")
    void 프로덕션리스트조회BDD테스트() {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<FrontProductionDTO> productionList = new ArrayList<>();
        productionList.add(FrontProductionDTO.builder().idx(1L).title("프로덕션").description("프로덕션").visible("Y")
                .productionImage(commonImageDtoList).build());
        Page<FrontProductionDTO> resultProduction = new PageImpl<>(productionList, pageRequest, productionList.size());

        // when
        given(mockFrontProductionJpaRepository.findProductionList(productionMap, pageRequest)).willReturn(resultProduction);
        Page<FrontProductionDTO> newProductionList = mockFrontProductionJpaRepository.findProductionList(productionMap, pageRequest);
        List<FrontProductionDTO> findProductionList = newProductionList.stream().collect(Collectors.toList());

        // then
        assertThat(findProductionList.get(0).getIdx()).isEqualTo(productionList.get(0).getIdx());
        assertThat(findProductionList.get(0).getTitle()).isEqualTo(productionList.get(0).getTitle());
        assertThat(findProductionList.get(0).getDescription()).isEqualTo(productionList.get(0).getDescription());
        assertThat(findProductionList.get(0).getProductionImage().get(0).getFileName()).isEqualTo(productionList.get(0).getProductionImage().get(0).getFileName());
        assertThat(findProductionList.get(0).getProductionImage().get(0).getTypeName()).isEqualTo(productionList.get(0).getProductionImage().get(0).getTypeName());

        // verify
        then(mockFrontProductionJpaRepository).should(times(1)).findProductionList(productionMap, pageRequest);
        then(mockFrontProductionJpaRepository).should(atLeastOnce()).findProductionList(productionMap, pageRequest);
        then(mockFrontProductionJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션 상세 조회 Mockito 테스트")
    void 프로덕션상세조회Mockito테스트() {
        // given
        when(mockFrontProductionJpaRepository.findOneProduction(frontProductionEntity.getIdx())).thenReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaRepository.findOneProduction(frontProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getTitle()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getDescription()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getVisible()).isEqualTo("Y");
        assertThat(productionInfo.getProductionImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(productionInfo.getProductionImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(productionInfo.getProductionImage().get(0).getImageType()).isEqualTo("main");
        assertThat(productionInfo.getProductionImage().get(0).getTypeName()).isEqualTo("production");

        // verify
        verify(mockFrontProductionJpaRepository, times(1)).findOneProduction(frontProductionEntity.getIdx());
        verify(mockFrontProductionJpaRepository, atLeastOnce()).findOneProduction(frontProductionEntity.getIdx());
        verifyNoMoreInteractions(mockFrontProductionJpaRepository);

        InOrder inOrder = inOrder(mockFrontProductionJpaRepository);
        inOrder.verify(mockFrontProductionJpaRepository).findOneProduction(frontProductionEntity.getIdx());
    }

    @Test
    @DisplayName("프로덕션 상세 조회 BDD 테스트")
    void 프로덕션상세조회BDD테스트() {
        // given
        given(mockFrontProductionJpaRepository.findOneProduction(frontProductionEntity.getIdx())).willReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaRepository.findOneProduction(frontProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getTitle()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getDescription()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getVisible()).isEqualTo("Y");
        assertThat(productionInfo.getProductionImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(productionInfo.getProductionImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(productionInfo.getProductionImage().get(0).getImageType()).isEqualTo("main");
        assertThat(productionInfo.getProductionImage().get(0).getTypeName()).isEqualTo("production");

        // verify
        then(mockFrontProductionJpaRepository).should(times(1)).findOneProduction(frontProductionEntity.getIdx());
        then(mockFrontProductionJpaRepository).should(atLeastOnce()).findOneProduction(frontProductionEntity.getIdx());
        then(mockFrontProductionJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 프로덕션 상세 조회 테스트")
    void 이전or다음프로덕션상세조회테스트() {
        // given
        frontProductionEntity = FrontProductionEntity.builder().idx(118L).build();

        // when
        frontProductionDTO = frontProductionJpaRepository.findOneProduction(frontProductionEntity.getIdx());

        // 이전 프로덕션
        assertThat(frontProductionJpaRepository.findPrevOneProduction(frontProductionEntity.getIdx()).getIdx()).isEqualTo(117);
        // 다음 프로덕션
        assertThat(frontProductionJpaRepository.findNextOneProduction(frontProductionEntity.getIdx()).getIdx()).isEqualTo(119);
    }

    @Test
    @DisplayName("이전 프로덕션 상세 조회 Mockito 테스트")
    void 이전프로덕션상세조회Mockito테스트() {
        // given
        frontProductionEntity = FrontProductionEntity.builder().idx(118L).build();

        // when
        frontProductionDTO = frontProductionJpaRepository.findPrevOneProduction(frontProductionEntity.getIdx());

        when(mockFrontProductionJpaRepository.findPrevOneProduction(frontProductionEntity.getIdx())).thenReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaRepository.findPrevOneProduction(frontProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(117);

        // verify
        verify(mockFrontProductionJpaRepository, times(1)).findPrevOneProduction(frontProductionEntity.getIdx());
        verify(mockFrontProductionJpaRepository, atLeastOnce()).findPrevOneProduction(frontProductionEntity.getIdx());
        verifyNoMoreInteractions(mockFrontProductionJpaRepository);

        InOrder inOrder = inOrder(mockFrontProductionJpaRepository);
        inOrder.verify(mockFrontProductionJpaRepository).findPrevOneProduction(frontProductionEntity.getIdx());
    }

    @Test
    @DisplayName("이전 프로덕션 상세 조회 BDD 테스트")
    void 이전프로덕션상세조회BDD테스트() {
        // given
        frontProductionEntity = FrontProductionEntity.builder().idx(118L).build();

        // when
        frontProductionDTO = frontProductionJpaRepository.findPrevOneProduction(frontProductionEntity.getIdx());

        given(mockFrontProductionJpaRepository.findPrevOneProduction(frontProductionEntity.getIdx())).willReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaRepository.findPrevOneProduction(frontProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(117);

        // verify
        then(mockFrontProductionJpaRepository).should(times(1)).findPrevOneProduction(frontProductionEntity.getIdx());
        then(mockFrontProductionJpaRepository).should(atLeastOnce()).findPrevOneProduction(frontProductionEntity.getIdx());
        then(mockFrontProductionJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 프로덕션 상세 조회 Mockito 테스트")
    void 다음프로덕션상세조회Mockito테스트() {
        // given
        frontProductionEntity = FrontProductionEntity.builder().idx(118L).build();

        // when
        frontProductionDTO = frontProductionJpaRepository.findNextOneProduction(frontProductionEntity.getIdx());

        when(mockFrontProductionJpaRepository.findNextOneProduction(frontProductionEntity.getIdx())).thenReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaRepository.findNextOneProduction(frontProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(119);

        // verify
        verify(mockFrontProductionJpaRepository, times(1)).findNextOneProduction(frontProductionEntity.getIdx());
        verify(mockFrontProductionJpaRepository, atLeastOnce()).findNextOneProduction(frontProductionEntity.getIdx());
        verifyNoMoreInteractions(mockFrontProductionJpaRepository);

        InOrder inOrder = inOrder(mockFrontProductionJpaRepository);
        inOrder.verify(mockFrontProductionJpaRepository).findNextOneProduction(frontProductionEntity.getIdx());
    }

    @Test
    @DisplayName("다음 프로덕션 상세 조회 BDD 테스트")
    void 다음프로덕션상세조회BDD테스트() {
        // given
        frontProductionEntity = FrontProductionEntity.builder().idx(118L).build();

        // when
        frontProductionDTO = frontProductionJpaRepository.findNextOneProduction(frontProductionEntity.getIdx());

        given(mockFrontProductionJpaRepository.findNextOneProduction(frontProductionEntity.getIdx())).willReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaRepository.findNextOneProduction(frontProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(119);

        // verify
        then(mockFrontProductionJpaRepository).should(times(1)).findNextOneProduction(frontProductionEntity.getIdx());
        then(mockFrontProductionJpaRepository).should(atLeastOnce()).findNextOneProduction(frontProductionEntity.getIdx());
        then(mockFrontProductionJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션 조회 수 Mockito 테스트")
    void 프로덕션조회수Mockito테스트() {
        // given
        em.persist(frontProductionEntity);
        frontProductionDTO = FrontProductionEntity.toDto(frontProductionEntity);

        Integer viewCount = frontProductionJpaRepository.updateProductionViewCount(frontProductionEntity.getIdx());

        // when
        when(mockFrontProductionJpaRepository.updateProductionViewCount(frontProductionEntity.getIdx())).thenReturn(viewCount);
        Integer newViewCount = mockFrontProductionJpaRepository.updateProductionViewCount(frontProductionEntity.getIdx());

        // then
        assertThat(newViewCount).isEqualTo(viewCount);

        // verify
        verify(mockFrontProductionJpaRepository, times(1)).updateProductionViewCount(frontProductionEntity.getIdx());
        verify(mockFrontProductionJpaRepository, atLeastOnce()).updateProductionViewCount(frontProductionEntity.getIdx());
        verifyNoMoreInteractions(mockFrontProductionJpaRepository);

        InOrder inOrder = inOrder(mockFrontProductionJpaRepository);
        inOrder.verify(mockFrontProductionJpaRepository).updateProductionViewCount(frontProductionEntity.getIdx());
    }

    @Test
    @DisplayName("프로덕션 조회 수 BDD 테스트")
    void 프로덕션조회수BDD테스트() {
        // given
        em.persist(frontProductionEntity);
        frontProductionDTO = FrontProductionEntity.toDto(frontProductionEntity);

        Integer viewCount = frontProductionJpaRepository.updateProductionViewCount(frontProductionEntity.getIdx());

        // when
        given(mockFrontProductionJpaRepository.updateProductionViewCount(frontProductionEntity.getIdx())).willReturn(viewCount);
        Integer newViewCount = mockFrontProductionJpaRepository.updateProductionViewCount(frontProductionEntity.getIdx());

        // then
        assertThat(newViewCount).isEqualTo(viewCount);

        // verify
        then(mockFrontProductionJpaRepository).should(times(1)).updateProductionViewCount(frontProductionEntity.getIdx());
        then(mockFrontProductionJpaRepository).should(atLeastOnce()).updateProductionViewCount(frontProductionEntity.getIdx());
        then(mockFrontProductionJpaRepository).shouldHaveNoMoreInteractions();
    }
}

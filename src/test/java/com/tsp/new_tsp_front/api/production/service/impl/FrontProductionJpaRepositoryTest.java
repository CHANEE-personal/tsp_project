package com.tsp.new_tsp_front.api.production.service.impl;

import com.tsp.new_tsp_front.api.common.domain.CommonImageDTO;
import com.tsp.new_tsp_front.api.common.domain.CommonImageEntity;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionDTO;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity;
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
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.api.production.service.impl.ProductionMapper.INSTANCE;
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
    @Mock private FrontProductionJpaRepository mockFrontProductionJpaRepository;
    private final FrontProductionJpaRepository frontProductionJpaRepository;

    private FrontProductionEntity frontProductionEntity;
    private FrontProductionDTO frontProductionDTO;
    private CommonImageEntity commonImageEntity;
    private CommonImageDTO commonImageDTO;
    List<CommonImageEntity> commonImageEntityList = new ArrayList<>();

    private final EntityManager em;

    private void createProduction() {
        commonImageEntity = CommonImageEntity.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("production")
                .build();

        commonImageDTO = ProductionImageMapper.INSTANCE.toDto(commonImageEntity);

        commonImageEntityList.add(commonImageEntity);

        frontProductionEntity = FrontProductionEntity.builder()
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .viewCount(1)
                .commonImageEntityList(commonImageEntityList)
                .build();

        frontProductionDTO = INSTANCE.toDto(frontProductionEntity);

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
        productionMap.put("jpaStartPage", 0);
        productionMap.put("size", 3);
        productionMap.put("searchType", 0);
        productionMap.put("searchKeyword", "하하");

        assertThat(frontProductionJpaRepository.getProductionList(productionMap)).isNotEmpty();
        // then
    }

    @Test
    @DisplayName("프로덕션 리스트 조회 Mockito 테스트")
    void 프로덕션리스트조회Mockito테스트() {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        productionMap.put("jpaStartPage", 1);
        productionMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<FrontProductionDTO> productionList = new ArrayList<>();
        productionList.add(FrontProductionDTO.builder().idx(1).title("프로덕션").description("프로덕션").visible("Y")
                .productionImage(commonImageDtoList).build());

        // when
        when(mockFrontProductionJpaRepository.getProductionList(productionMap)).thenReturn(productionList);
        List<FrontProductionDTO> newProductionList = mockFrontProductionJpaRepository.getProductionList(productionMap);

        // then
        assertThat(newProductionList.get(0).getIdx()).isEqualTo(productionList.get(0).getIdx());
        assertThat(newProductionList.get(0).getTitle()).isEqualTo(productionList.get(0).getTitle());
        assertThat(newProductionList.get(0).getDescription()).isEqualTo(productionList.get(0).getDescription());
        assertThat(newProductionList.get(0).getProductionImage().get(0).getFileName()).isEqualTo(productionList.get(0).getProductionImage().get(0).getFileName());
        assertThat(newProductionList.get(0).getProductionImage().get(0).getTypeName()).isEqualTo(productionList.get(0).getProductionImage().get(0).getTypeName());

        // verify
        verify(mockFrontProductionJpaRepository, times(1)).getProductionList(productionMap);
        verify(mockFrontProductionJpaRepository, atLeastOnce()).getProductionList(productionMap);
        verifyNoMoreInteractions(mockFrontProductionJpaRepository);

        InOrder inOrder = inOrder(mockFrontProductionJpaRepository);
        inOrder.verify(mockFrontProductionJpaRepository).getProductionList(productionMap);
    }

    @Test
    @DisplayName("프로덕션 리스트 조회 BDD 테스트")
    void 프로덕션리스트조회BDD테스트() {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        productionMap.put("jpaStartPage", 1);
        productionMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<FrontProductionDTO> productionList = new ArrayList<>();
        productionList.add(FrontProductionDTO.builder().idx(1).title("프로덕션").description("프로덕션").visible("Y")
                .productionImage(commonImageDtoList).build());

        // when
        given(mockFrontProductionJpaRepository.getProductionList(productionMap)).willReturn(productionList);
        List<FrontProductionDTO> newProductionList = mockFrontProductionJpaRepository.getProductionList(productionMap);

        // then
        assertThat(newProductionList.get(0).getIdx()).isEqualTo(productionList.get(0).getIdx());
        assertThat(newProductionList.get(0).getTitle()).isEqualTo(productionList.get(0).getTitle());
        assertThat(newProductionList.get(0).getDescription()).isEqualTo(productionList.get(0).getDescription());
        assertThat(newProductionList.get(0).getProductionImage().get(0).getFileName()).isEqualTo(productionList.get(0).getProductionImage().get(0).getFileName());
        assertThat(newProductionList.get(0).getProductionImage().get(0).getTypeName()).isEqualTo(productionList.get(0).getProductionImage().get(0).getTypeName());

        // verify
        then(mockFrontProductionJpaRepository).should(times(1)).getProductionList(productionMap);
        then(mockFrontProductionJpaRepository).should(atLeastOnce()).getProductionList(productionMap);
        then(mockFrontProductionJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션 상세 조회 Mockito 테스트")
    void 프로덕션상세조회Mockito테스트() {
        // given
        when(mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity)).thenReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity);

        // then
        assertThat(productionInfo.getTitle()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getDescription()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getVisible()).isEqualTo("Y");
        assertThat(productionInfo.getProductionImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(productionInfo.getProductionImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(productionInfo.getProductionImage().get(0).getImageType()).isEqualTo("main");
        assertThat(productionInfo.getProductionImage().get(0).getTypeName()).isEqualTo("production");

        // verify
        verify(mockFrontProductionJpaRepository, times(1)).getProductionInfo(frontProductionEntity);
        verify(mockFrontProductionJpaRepository, atLeastOnce()).getProductionInfo(frontProductionEntity);
        verifyNoMoreInteractions(mockFrontProductionJpaRepository);

        InOrder inOrder = inOrder(mockFrontProductionJpaRepository);
        inOrder.verify(mockFrontProductionJpaRepository).getProductionInfo(frontProductionEntity);
    }

    @Test
    @DisplayName("프로덕션 상세 조회 BDD 테스트")
    void 프로덕션상세조회BDD테스트() {
        // given
        given(mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity)).willReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaRepository.getProductionInfo(frontProductionEntity);

        // then
        assertThat(productionInfo.getTitle()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getDescription()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getVisible()).isEqualTo("Y");
        assertThat(productionInfo.getProductionImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(productionInfo.getProductionImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(productionInfo.getProductionImage().get(0).getImageType()).isEqualTo("main");
        assertThat(productionInfo.getProductionImage().get(0).getTypeName()).isEqualTo("production");

        // verify
        then(mockFrontProductionJpaRepository).should(times(1)).getProductionInfo(frontProductionEntity);
        then(mockFrontProductionJpaRepository).should(atLeastOnce()).getProductionInfo(frontProductionEntity);
        then(mockFrontProductionJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 프로덕션 상세 조회 테스트")
    void 이전or다음프로덕션상세조회테스트() {
        // given
        frontProductionEntity = FrontProductionEntity.builder().idx(118).build();

        // when
        frontProductionDTO = frontProductionJpaRepository.getProductionInfo(frontProductionEntity);

        // 이전 프로덕션
        assertThat(frontProductionJpaRepository.findPrevOneProduction(frontProductionEntity).getIdx()).isEqualTo(117);
        // 다음 프로덕션
        assertThat(frontProductionJpaRepository.findNextOneProduction(frontProductionEntity).getIdx()).isEqualTo(119);
    }

    @Test
    @DisplayName("이전 프로덕션 상세 조회 Mockito 테스트")
    void 이전프로덕션상세조회Mockito테스트() {
        // given
        frontProductionEntity = FrontProductionEntity.builder().idx(118).build();

        // when
        frontProductionDTO = frontProductionJpaRepository.findPrevOneProduction(frontProductionEntity);

        when(mockFrontProductionJpaRepository.findPrevOneProduction(frontProductionEntity)).thenReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaRepository.findPrevOneProduction(frontProductionEntity);

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(117);

        // verify
        verify(mockFrontProductionJpaRepository, times(1)).findPrevOneProduction(frontProductionEntity);
        verify(mockFrontProductionJpaRepository, atLeastOnce()).findPrevOneProduction(frontProductionEntity);
        verifyNoMoreInteractions(mockFrontProductionJpaRepository);

        InOrder inOrder = inOrder(mockFrontProductionJpaRepository);
        inOrder.verify(mockFrontProductionJpaRepository).findPrevOneProduction(frontProductionEntity);
    }

    @Test
    @DisplayName("이전 프로덕션 상세 조회 BDD 테스트")
    void 이전프로덕션상세조회BDD테스트() {
        // given
        frontProductionEntity = FrontProductionEntity.builder().idx(118).build();

        // when
        frontProductionDTO = frontProductionJpaRepository.findPrevOneProduction(frontProductionEntity);

        given(mockFrontProductionJpaRepository.findPrevOneProduction(frontProductionEntity)).willReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaRepository.findPrevOneProduction(frontProductionEntity);

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(117);

        // verify
        then(mockFrontProductionJpaRepository).should(times(1)).findPrevOneProduction(frontProductionEntity);
        then(mockFrontProductionJpaRepository).should(atLeastOnce()).findPrevOneProduction(frontProductionEntity);
        then(mockFrontProductionJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 프로덕션 상세 조회 Mockito 테스트")
    void 다음프로덕션상세조회Mockito테스트() {
        // given
        frontProductionEntity = FrontProductionEntity.builder().idx(118).build();

        // when
        frontProductionDTO = frontProductionJpaRepository.findNextOneProduction(frontProductionEntity);

        when(mockFrontProductionJpaRepository.findNextOneProduction(frontProductionEntity)).thenReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaRepository.findNextOneProduction(frontProductionEntity);

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(119);

        // verify
        verify(mockFrontProductionJpaRepository, times(1)).findNextOneProduction(frontProductionEntity);
        verify(mockFrontProductionJpaRepository, atLeastOnce()).findNextOneProduction(frontProductionEntity);
        verifyNoMoreInteractions(mockFrontProductionJpaRepository);

        InOrder inOrder = inOrder(mockFrontProductionJpaRepository);
        inOrder.verify(mockFrontProductionJpaRepository).findNextOneProduction(frontProductionEntity);
    }

    @Test
    @DisplayName("다음 프로덕션 상세 조회 BDD 테스트")
    void 다음프로덕션상세조회BDD테스트() {
        // given
        frontProductionEntity = FrontProductionEntity.builder().idx(118).build();

        // when
        frontProductionDTO = frontProductionJpaRepository.findNextOneProduction(frontProductionEntity);

        given(mockFrontProductionJpaRepository.findNextOneProduction(frontProductionEntity)).willReturn(frontProductionDTO);
        FrontProductionDTO productionInfo = mockFrontProductionJpaRepository.findNextOneProduction(frontProductionEntity);

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(119);

        // verify
        then(mockFrontProductionJpaRepository).should(times(1)).findNextOneProduction(frontProductionEntity);
        then(mockFrontProductionJpaRepository).should(atLeastOnce()).findNextOneProduction(frontProductionEntity);
        then(mockFrontProductionJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션 조회 수 Mockito 테스트")
    void 프로덕션조회수Mockito테스트() {
        // given
        em.persist(frontProductionEntity);
        frontProductionDTO = INSTANCE.toDto(frontProductionEntity);

        Integer viewCount = frontProductionJpaRepository.updateProductionViewCount(frontProductionEntity);

        // when
        when(mockFrontProductionJpaRepository.updateProductionViewCount(frontProductionEntity)).thenReturn(viewCount);
        Integer newViewCount = mockFrontProductionJpaRepository.updateProductionViewCount(frontProductionEntity);

        // then
        assertThat(newViewCount).isEqualTo(viewCount);

        // verify
        verify(mockFrontProductionJpaRepository, times(1)).updateProductionViewCount(frontProductionEntity);
        verify(mockFrontProductionJpaRepository, atLeastOnce()).updateProductionViewCount(frontProductionEntity);
        verifyNoMoreInteractions(mockFrontProductionJpaRepository);

        InOrder inOrder = inOrder(mockFrontProductionJpaRepository);
        inOrder.verify(mockFrontProductionJpaRepository).updateProductionViewCount(frontProductionEntity);
    }

    @Test
    @DisplayName("프로덕션 조회 수 BDD 테스트")
    void 프로덕션조회수BDD테스트() {
        // given
        em.persist(frontProductionEntity);
        frontProductionDTO = INSTANCE.toDto(frontProductionEntity);

        Integer viewCount = frontProductionJpaRepository.updateProductionViewCount(frontProductionEntity);

        // when
        given(mockFrontProductionJpaRepository.updateProductionViewCount(frontProductionEntity)).willReturn(viewCount);
        Integer newViewCount = mockFrontProductionJpaRepository.updateProductionViewCount(frontProductionEntity);

        // then
        assertThat(newViewCount).isEqualTo(viewCount);

        // verify
        then(mockFrontProductionJpaRepository).should(times(1)).updateProductionViewCount(frontProductionEntity);
        then(mockFrontProductionJpaRepository).should(atLeastOnce()).updateProductionViewCount(frontProductionEntity);
        then(mockFrontProductionJpaRepository).shouldHaveNoMoreInteractions();
    }
}
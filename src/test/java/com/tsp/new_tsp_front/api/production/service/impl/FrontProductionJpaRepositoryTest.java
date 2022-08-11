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

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.api.production.service.impl.ProductionImageMapper.INSTANCE;
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

        commonImageDTO = INSTANCE.toDto(commonImageEntity);

        commonImageEntityList.add(commonImageEntity);

        frontProductionDTO = FrontProductionDTO.builder()
                .idx(1)
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .productionImage(INSTANCE.toDtoList(commonImageEntityList))
                .build();

        frontProductionEntity = FrontProductionEntity.builder().idx(1).commonImageEntityList(commonImageEntityList).build();
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
        productionMap.put("jpaStartPage", 1);
        productionMap.put("size", 3);
        productionMap.put("searchType", 0);
        productionMap.put("searchKeyword", "하하");

        // then
        assertThat(frontProductionJpaRepository.getProductionList(productionMap)).isNotEmpty();
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
        assertThat(productionInfo.getIdx()).isEqualTo(1);
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
        assertThat(productionInfo.getIdx()).isEqualTo(1);
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
}
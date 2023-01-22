package com.tsp.api.model.service.agency;

import com.tsp.api.common.EntityType;
import com.tsp.api.domain.common.CommonImageDTO;
import com.tsp.api.domain.common.CommonImageEntity;
import com.tsp.api.domain.model.agency.AdminAgencyDTO;
import com.tsp.api.domain.model.agency.AdminAgencyEntity;
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

@Slf4j
@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("모델 소속사 Repository Test")
class AdminAgencyJpaQueryRepositoryTest {
    @Mock private AdminAgencyJpaQueryRepository mockAdminAgencyJpaQueryRepository;
    private final AdminAgencyJpaQueryRepository adminAgencyJpaQueryRepository;
    private final EntityManager em;

    private AdminAgencyEntity adminAgencyEntity;
    private AdminAgencyDTO adminAgencyDTO;
    private CommonImageEntity commonImageEntity;
    private CommonImageDTO commonImageDTO;

    void createAgency() {
        adminAgencyEntity = AdminAgencyEntity.builder()
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

        adminAgencyDTO = AdminAgencyEntity.toDto(adminAgencyEntity);

        commonImageEntity = CommonImageEntity.builder()
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1L)
                .typeName(EntityType.AGENCY)
                .visible("Y")
                .build();

        commonImageDTO = CommonImageEntity.toDto(commonImageEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createAgency();
    }

    @Test
    @DisplayName("소속사리스트조회테스트")
    void 소속사리스트조회테스트() {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        // then
        assertThat(adminAgencyJpaQueryRepository.findAgencyList(agencyMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("소속사상세조회테스트")
    void 소속사상세조회테스트() {
        // given
        adminAgencyEntity = AdminAgencyEntity.builder().idx(1L).build();

        // when
        adminAgencyDTO = adminAgencyJpaQueryRepository.findOneAgency(adminAgencyEntity.getIdx());

        // then
        assertAll(() -> assertThat(adminAgencyDTO.getIdx()).isEqualTo(1),
                () -> {
                    assertThat(adminAgencyDTO.getAgencyName()).isEqualTo("agency");
                    assertNotNull(adminAgencyDTO.getAgencyName());
                },
                () -> {
                    assertThat(adminAgencyDTO.getAgencyDescription()).isEqualTo("agency");
                    assertNotNull(adminAgencyDTO.getAgencyDescription());
                },
                () -> {
                    assertThat(adminAgencyDTO.getVisible()).isEqualTo("Y");
                    assertNotNull(adminAgencyDTO.getVisible());
                });
    }

    @Test
    @DisplayName("소속사Mockito조회테스트")
    void 소속사Mockito조회테스트() {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminAgencyDTO> agencyList = new ArrayList<>();
        agencyList.add(AdminAgencyDTO.builder().idx(1L)
                .agencyName("agency").agencyDescription("agency").build());
        Page<AdminAgencyDTO> resultAgency = new PageImpl<>(agencyList, pageRequest, agencyList.size());

        // when
        when(mockAdminAgencyJpaQueryRepository.findAgencyList(agencyMap, pageRequest)).thenReturn(resultAgency);
        Page<AdminAgencyDTO> newAgencyList = mockAdminAgencyJpaQueryRepository.findAgencyList(agencyMap, pageRequest);
        List<AdminAgencyDTO> findAgencyList = newAgencyList.stream().collect(Collectors.toList());

        // then
        assertThat(findAgencyList.get(0).getIdx()).isEqualTo(agencyList.get(0).getIdx());
        assertThat(findAgencyList.get(0).getAgencyName()).isEqualTo(agencyList.get(0).getAgencyName());
        assertThat(findAgencyList.get(0).getAgencyDescription()).isEqualTo(agencyList.get(0).getAgencyDescription());
        assertThat(findAgencyList.get(0).getVisible()).isEqualTo(agencyList.get(0).getVisible());

        // verify
        verify(mockAdminAgencyJpaQueryRepository, times(1)).findAgencyList(agencyMap, pageRequest);
        verify(mockAdminAgencyJpaQueryRepository, atLeastOnce()).findAgencyList(agencyMap, pageRequest);
        verifyNoMoreInteractions(mockAdminAgencyJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminAgencyJpaQueryRepository);
        inOrder.verify(mockAdminAgencyJpaQueryRepository).findAgencyList(agencyMap, pageRequest);
    }

    @Test
    @DisplayName("소속사BDD조회테스트")
    void 소속사BDD조회테스트() {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminAgencyDTO> agencyList = new ArrayList<>();
        agencyList.add(AdminAgencyDTO.builder().idx(1L)
                .agencyName("agency").agencyDescription("agency").build());
        Page<AdminAgencyDTO> resultAgency = new PageImpl<>(agencyList, pageRequest, agencyList.size());

        // when
        given(mockAdminAgencyJpaQueryRepository.findAgencyList(agencyMap, pageRequest)).willReturn(resultAgency);
        Page<AdminAgencyDTO> newAgencyList = mockAdminAgencyJpaQueryRepository.findAgencyList(agencyMap, pageRequest);
        List<AdminAgencyDTO> findAgencyList = newAgencyList.stream().collect(Collectors.toList());

        // then
        assertThat(findAgencyList.get(0).getIdx()).isEqualTo(agencyList.get(0).getIdx());
        assertThat(findAgencyList.get(0).getAgencyName()).isEqualTo(agencyList.get(0).getAgencyName());
        assertThat(findAgencyList.get(0).getAgencyDescription()).isEqualTo(agencyList.get(0).getAgencyDescription());
        assertThat(findAgencyList.get(0).getVisible()).isEqualTo(agencyList.get(0).getVisible());

        // verify
        then(mockAdminAgencyJpaQueryRepository).should(times(1)).findAgencyList(agencyMap, pageRequest);
        then(mockAdminAgencyJpaQueryRepository).should(atLeastOnce()).findAgencyList(agencyMap, pageRequest);
        then(mockAdminAgencyJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("소속사상세Mockito조회테스트")
    void 소속사상세Mockito조회테스트() {
        // given
        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        AdminAgencyEntity adminAgencyEntity = AdminAgencyEntity.builder().idx(1L).commonImageEntityList(commonImageEntityList).build();
        AdminAgencyDTO adminAgencyDTO = AdminAgencyDTO.builder()
                .idx(1L)
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .agencyImage(commonImageEntityList.stream().map(CommonImageEntity::toDto).collect(Collectors.toList()))
                .build();

        // when
        when(mockAdminAgencyJpaQueryRepository.findOneAgency(adminAgencyEntity.getIdx())).thenReturn(adminAgencyDTO);
        AdminAgencyDTO agencyInfo = mockAdminAgencyJpaQueryRepository.findOneAgency(adminAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(1);
        assertThat(agencyInfo.getAgencyName()).isEqualTo("agency");
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo("agency");
        assertThat(agencyInfo.getVisible()).isEqualTo("Y");
        assertThat(agencyInfo.getAgencyImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(agencyInfo.getAgencyImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(agencyInfo.getAgencyImage().get(0).getFilePath()).isEqualTo("/test/test.jpg");
        assertThat(agencyInfo.getAgencyImage().get(0).getImageType()).isEqualTo("main");
        assertThat(agencyInfo.getAgencyImage().get(0).getTypeName()).isEqualTo(EntityType.AGENCY);

        // verify
        verify(mockAdminAgencyJpaQueryRepository, times(1)).findOneAgency(adminAgencyEntity.getIdx());
        verify(mockAdminAgencyJpaQueryRepository, atLeastOnce()).findOneAgency(adminAgencyEntity.getIdx());
        verifyNoMoreInteractions(mockAdminAgencyJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminAgencyJpaQueryRepository);
        inOrder.verify(mockAdminAgencyJpaQueryRepository).findOneAgency(adminAgencyEntity.getIdx());
    }

    @Test
    @DisplayName("소속사상세BDD조회테스트")
    void 소속사상세BDD조회테스트() {
        // given
        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        AdminAgencyEntity adminAgencyEntity = AdminAgencyEntity.builder().idx(1L).commonImageEntityList(commonImageEntityList).build();
        AdminAgencyDTO adminAgencyDTO = AdminAgencyDTO.builder()
                .idx(1L)
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .agencyImage(commonImageEntityList.stream().map(CommonImageEntity::toDto).collect(Collectors.toList()))
                .build();

        // when
        given(mockAdminAgencyJpaQueryRepository.findOneAgency(adminAgencyEntity.getIdx())).willReturn(adminAgencyDTO);
        AdminAgencyDTO agencyInfo = mockAdminAgencyJpaQueryRepository.findOneAgency(adminAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(1);
        assertThat(agencyInfo.getAgencyName()).isEqualTo("agency");
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo("agency");
        assertThat(agencyInfo.getVisible()).isEqualTo("Y");
        assertThat(agencyInfo.getAgencyImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(agencyInfo.getAgencyImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(agencyInfo.getAgencyImage().get(0).getFilePath()).isEqualTo("/test/test.jpg");
        assertThat(agencyInfo.getAgencyImage().get(0).getImageType()).isEqualTo("main");
        assertThat(agencyInfo.getAgencyImage().get(0).getTypeName()).isEqualTo(EntityType.AGENCY);

        // verify
        then(mockAdminAgencyJpaQueryRepository).should(times(1)).findOneAgency(adminAgencyEntity.getIdx());
        then(mockAdminAgencyJpaQueryRepository).should(atLeastOnce()).findOneAgency(adminAgencyEntity.getIdx());
        then(mockAdminAgencyJpaQueryRepository).shouldHaveNoMoreInteractions();
    }
}

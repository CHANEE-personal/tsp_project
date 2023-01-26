package com.tsp.api.model.service.agency;

import com.tsp.api.model.domain.agency.AdminAgencyDTO;
import com.tsp.api.model.domain.agency.AdminAgencyEntity;
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
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("소속사 Service Test")
class AdminAgencyJpaServiceTest {
    @Mock private AdminAgencyJpaService mockAdminAgencyJpaService;
    private final AdminAgencyJpaService adminAgencyJpaService;

    private AdminAgencyEntity adminAgencyEntity;
    private AdminAgencyDTO adminAgencyDTO;

    void createAgency() {
        adminAgencyEntity = AdminAgencyEntity.builder()
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

        adminAgencyDTO = adminAgencyJpaService.insertAgency(adminAgencyEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createAgency();
    }

    @Test
    @DisplayName("소속사 리스트 조회 테스트")
    void 소속사리스트조회테스트() {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        // then
        assertThat(adminAgencyJpaService.findAgencyList(agencyMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("소속사 리스트 조회 Mockito 테스트")
    void 소속사리스트조회Mockito테스트() {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminAgencyDTO> returnAgencyList = new ArrayList<>();

        returnAgencyList.add(AdminAgencyDTO.builder().idx(1L).agencyName("agency1").agencyDescription("agency1").visible("Y").build());
        returnAgencyList.add(AdminAgencyDTO.builder().idx(2L).agencyName("agency2").agencyDescription("agency2").visible("Y").build());

        Page<AdminAgencyDTO> resultAgency = new PageImpl<>(returnAgencyList, pageRequest, returnAgencyList.size());

        // when
        when(mockAdminAgencyJpaService.findAgencyList(agencyMap, pageRequest)).thenReturn(resultAgency);
        Page<AdminAgencyDTO> agencyList = mockAdminAgencyJpaService.findAgencyList(agencyMap, pageRequest);
        List<AdminAgencyDTO> findAgencyList = agencyList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findAgencyList).isNotEmpty(),
                () -> assertThat(findAgencyList).hasSize(2)
        );

        assertThat(findAgencyList.get(0).getIdx()).isEqualTo(returnAgencyList.get(0).getIdx());
        assertThat(findAgencyList.get(0).getAgencyName()).isEqualTo(returnAgencyList.get(0).getAgencyName());
        assertThat(findAgencyList.get(0).getAgencyDescription()).isEqualTo(returnAgencyList.get(0).getAgencyDescription());
        assertThat(findAgencyList.get(0).getVisible()).isEqualTo(returnAgencyList.get(0).getVisible());

        assertThat(findAgencyList.get(1).getIdx()).isEqualTo(returnAgencyList.get(1).getIdx());
        assertThat(findAgencyList.get(1).getAgencyName()).isEqualTo(returnAgencyList.get(1).getAgencyName());
        assertThat(findAgencyList.get(1).getAgencyDescription()).isEqualTo(returnAgencyList.get(1).getAgencyDescription());
        assertThat(findAgencyList.get(1).getVisible()).isEqualTo(returnAgencyList.get(1).getVisible());

        // verify
        verify(mockAdminAgencyJpaService, times(1)).findAgencyList(agencyMap, pageRequest);
        verify(mockAdminAgencyJpaService, atLeastOnce()).findAgencyList(agencyMap, pageRequest);
        verifyNoMoreInteractions(mockAdminAgencyJpaService);

        InOrder inOrder = inOrder(mockAdminAgencyJpaService);
        inOrder.verify(mockAdminAgencyJpaService).findAgencyList(agencyMap, pageRequest);
    }

    @Test
    @DisplayName("소속사 리스트 조회 BDD 테스트")
    void 소속사리스트조회BDD테스트() {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminAgencyDTO> returnAgencyList = new ArrayList<>();

        returnAgencyList.add(AdminAgencyDTO.builder().idx(1L).agencyName("agency1").agencyDescription("agency1").visible("Y").build());
        returnAgencyList.add(AdminAgencyDTO.builder().idx(2L).agencyName("agency2").agencyDescription("agency2").visible("Y").build());

        Page<AdminAgencyDTO> resultAgency = new PageImpl<>(returnAgencyList, pageRequest, returnAgencyList.size());

        // when
        given(mockAdminAgencyJpaService.findAgencyList(agencyMap, pageRequest)).willReturn(resultAgency);
        Page<AdminAgencyDTO> agencyList = mockAdminAgencyJpaService.findAgencyList(agencyMap, pageRequest);
        List<AdminAgencyDTO> findAgencyList = agencyList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findAgencyList).isNotEmpty(),
                () -> assertThat(findAgencyList).hasSize(2)
        );

        assertThat(findAgencyList.get(0).getIdx()).isEqualTo(returnAgencyList.get(0).getIdx());
        assertThat(findAgencyList.get(0).getAgencyName()).isEqualTo(returnAgencyList.get(0).getAgencyName());
        assertThat(findAgencyList.get(0).getAgencyDescription()).isEqualTo(returnAgencyList.get(0).getAgencyDescription());
        assertThat(findAgencyList.get(0).getVisible()).isEqualTo(returnAgencyList.get(0).getVisible());

        assertThat(findAgencyList.get(1).getIdx()).isEqualTo(returnAgencyList.get(1).getIdx());
        assertThat(findAgencyList.get(1).getAgencyName()).isEqualTo(returnAgencyList.get(1).getAgencyName());
        assertThat(findAgencyList.get(1).getAgencyDescription()).isEqualTo(returnAgencyList.get(1).getAgencyDescription());
        assertThat(findAgencyList.get(1).getVisible()).isEqualTo(returnAgencyList.get(1).getVisible());

        // verify
        then(mockAdminAgencyJpaService).should(times(1)).findAgencyList(agencyMap, pageRequest);
        then(mockAdminAgencyJpaService).should(atLeastOnce()).findAgencyList(agencyMap, pageRequest);
        then(mockAdminAgencyJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("소속사 상세 조회 테스트")
    void 소속사상세조회테스트() {
        // then
        assertThat(adminAgencyJpaService.findOneAgency(adminAgencyDTO.getIdx()).getAgencyName()).isEqualTo("agency");
    }

    @Test
    @DisplayName("소속사상세조회Mockito테스트")
    void 소속사상세조회Mockito테스트() {
        // when
        when(mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity.getIdx())).thenReturn(adminAgencyDTO);
        AdminAgencyDTO agencyInfo = mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(adminAgencyDTO.getIdx());
        assertThat(agencyInfo.getAgencyName()).isEqualTo(adminAgencyDTO.getAgencyName());
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo(adminAgencyDTO.getAgencyDescription());
        assertThat(agencyInfo.getVisible()).isEqualTo(adminAgencyDTO.getVisible());

        // verify
        verify(mockAdminAgencyJpaService, times(1)).findOneAgency(adminAgencyEntity.getIdx());
        verify(mockAdminAgencyJpaService, atLeastOnce()).findOneAgency(adminAgencyEntity.getIdx());
        verifyNoMoreInteractions(mockAdminAgencyJpaService);

        InOrder inOrder = inOrder(mockAdminAgencyJpaService);
        inOrder.verify(mockAdminAgencyJpaService).findOneAgency(adminAgencyEntity.getIdx());
    }

    @Test
    @DisplayName("소속사상세조회BDD테스트")
    void 소속사상세조회BDD테스트() {
        // when
        given(mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity.getIdx())).willReturn(adminAgencyDTO);
        AdminAgencyDTO agencyInfo = mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(adminAgencyDTO.getIdx());
        assertThat(agencyInfo.getAgencyName()).isEqualTo(adminAgencyDTO.getAgencyName());
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo(adminAgencyDTO.getAgencyDescription());
        assertThat(agencyInfo.getVisible()).isEqualTo(adminAgencyDTO.getVisible());

        // verify
        then(mockAdminAgencyJpaService).should(times(1)).findOneAgency(adminAgencyEntity.getIdx());
        then(mockAdminAgencyJpaService).should(atLeastOnce()).findOneAgency(adminAgencyEntity.getIdx());
        then(mockAdminAgencyJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("소속사 수정 Mockito 테스트")
    void 소속사수정Mockito테스트() {
        // given
        Long idx = adminAgencyJpaService.insertAgency(adminAgencyEntity).getIdx();

        adminAgencyEntity = AdminAgencyEntity.builder()
                .idx(idx)
                .agencyName("newAgency")
                .agencyDescription("newAgency")
                .visible("Y")
                .build();

        AdminAgencyDTO adminAgencyDTO = AdminAgencyEntity.toDto(adminAgencyEntity);

        adminAgencyJpaService.updateAgency(idx, adminAgencyEntity);

        // when
        when(mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity.getIdx())).thenReturn(adminAgencyDTO);
        AdminAgencyDTO agencyInfo = mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getAgencyName()).isEqualTo("newAgency");
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo("newAgency");

        // verify
        verify(mockAdminAgencyJpaService, times(1)).findOneAgency(adminAgencyEntity.getIdx());
        verify(mockAdminAgencyJpaService, atLeastOnce()).findOneAgency(adminAgencyEntity.getIdx());
        verifyNoMoreInteractions(mockAdminAgencyJpaService);

        InOrder inOrder = inOrder(mockAdminAgencyJpaService);
        inOrder.verify(mockAdminAgencyJpaService).findOneAgency(adminAgencyEntity.getIdx());
    }

    @Test
    @DisplayName("소속사 수정 BDD 테스트")
    void 소속사수정BDD테스트() {
        // given
        Long idx = adminAgencyJpaService.insertAgency(adminAgencyEntity).getIdx();

        adminAgencyEntity = AdminAgencyEntity.builder()
                .idx(idx)
                .agencyName("newAgency")
                .agencyDescription("newAgency")
                .visible("Y")
                .build();

        AdminAgencyDTO adminAgencyDTO = AdminAgencyEntity.toDto(adminAgencyEntity);

        adminAgencyJpaService.updateAgency(idx, adminAgencyEntity);

        // when
        given(mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity.getIdx())).willReturn(adminAgencyDTO);
        AdminAgencyDTO agencyInfo = mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getAgencyName()).isEqualTo("newAgency");
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo("newAgency");

        // verify
        then(mockAdminAgencyJpaService).should(times(1)).findOneAgency(adminAgencyEntity.getIdx());
        then(mockAdminAgencyJpaService).should(atLeastOnce()).findOneAgency(adminAgencyEntity.getIdx());
        then(mockAdminAgencyJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("소속사 삭제 Mockito 테스트")
    void 소속사삭제Mockito테스트() {
        // given
        adminAgencyJpaService.insertAgency(adminAgencyEntity);
        adminAgencyDTO = AdminAgencyEntity.toDto(adminAgencyEntity);

        // when
        when(mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity.getIdx())).thenReturn(adminAgencyDTO);
        Long deleteIdx = adminAgencyJpaService.deleteAgency(adminAgencyEntity.getIdx());

        // then
        assertThat(mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminAgencyJpaService, times(1)).findOneAgency(adminAgencyEntity.getIdx());
        verify(mockAdminAgencyJpaService, atLeastOnce()).findOneAgency(adminAgencyEntity.getIdx());
        verifyNoMoreInteractions(mockAdminAgencyJpaService);

        InOrder inOrder = inOrder(mockAdminAgencyJpaService);
        inOrder.verify(mockAdminAgencyJpaService).findOneAgency(adminAgencyEntity.getIdx());
    }

    @Test
    @DisplayName("소속사 삭제 BDD 테스트")
    void 소속사삭제BDD테스트() {
        // given
        adminAgencyJpaService.insertAgency(adminAgencyEntity);
        adminAgencyDTO = AdminAgencyEntity.toDto(adminAgencyEntity);

        // when
        given(mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity.getIdx())).willReturn(adminAgencyDTO);
        Long deleteIdx = adminAgencyJpaService.deleteAgency(adminAgencyEntity.getIdx());

        // then
        assertThat(mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminAgencyJpaService).should(times(1)).findOneAgency(adminAgencyEntity.getIdx());
        then(mockAdminAgencyJpaService).should(atLeastOnce()).findOneAgency(adminAgencyEntity.getIdx());
        then(mockAdminAgencyJpaService).shouldHaveNoMoreInteractions();
    }
}

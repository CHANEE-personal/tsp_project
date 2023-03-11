package com.tsp.api.model.service.agency;

import com.tsp.api.model.domain.agency.AdminAgencyDto;
import com.tsp.api.model.domain.agency.AdminAgencyEntity;
import com.tsp.api.model.service.AdminModelCommonServiceTest;
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
class AdminAgencyJpaServiceTest extends AdminModelCommonServiceTest {

    @Mock private AdminAgencyJpaQueryRepository adminAgencyJpaQueryRepository;
    @Mock private AdminAgencyJpaRepository adminAgencyJpaRepository;
    @InjectMocks private AdminAgencyJpaServiceImpl mockAdminAgencyJpaService;
    private final AdminAgencyJpaService adminAgencyJpaService;

    @Test
    @DisplayName("소속사 리스트 조회 테스트")
    void 소속사리스트조회테스트() {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        // then
        assertThat(adminAgencyJpaService.findAgencyList(agencyMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("소속사 리스트 조회 Mockito 테스트")
    void 소속사리스트조회Mockito테스트() {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminAgencyDto> returnAgencyList = new ArrayList<>();

        returnAgencyList.add(AdminAgencyDto.builder().idx(1L).agencyName("agency1").agencyDescription("agency1").visible("Y").build());
        returnAgencyList.add(AdminAgencyDto.builder().idx(2L).agencyName("agency2").agencyDescription("agency2").visible("Y").build());

        Page<AdminAgencyDto> resultAgency = new PageImpl<>(returnAgencyList, pageRequest, returnAgencyList.size());

        // when
        when(adminAgencyJpaQueryRepository.findAgencyList(agencyMap, pageRequest)).thenReturn(resultAgency);
        Page<AdminAgencyDto> agencyList = mockAdminAgencyJpaService.findAgencyList(agencyMap, pageRequest);
        List<AdminAgencyDto> findAgencyList = agencyList.stream().collect(Collectors.toList());

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
        verify(adminAgencyJpaQueryRepository, times(1)).findAgencyList(agencyMap, pageRequest);
        verify(adminAgencyJpaQueryRepository, atLeastOnce()).findAgencyList(agencyMap, pageRequest);
        verifyNoMoreInteractions(adminAgencyJpaQueryRepository);

        InOrder inOrder = inOrder(adminAgencyJpaQueryRepository);
        inOrder.verify(adminAgencyJpaQueryRepository).findAgencyList(agencyMap, pageRequest);
    }

    @Test
    @DisplayName("소속사 리스트 조회 BDD 테스트")
    void 소속사리스트조회BDD테스트() {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminAgencyDto> returnAgencyList = new ArrayList<>();

        returnAgencyList.add(AdminAgencyDto.builder().idx(1L).agencyName("agency1").agencyDescription("agency1").visible("Y").build());
        returnAgencyList.add(AdminAgencyDto.builder().idx(2L).agencyName("agency2").agencyDescription("agency2").visible("Y").build());

        Page<AdminAgencyDto> resultAgency = new PageImpl<>(returnAgencyList, pageRequest, returnAgencyList.size());

        // when
        given(adminAgencyJpaQueryRepository.findAgencyList(agencyMap, pageRequest)).willReturn(resultAgency);
        Page<AdminAgencyDto> agencyList = mockAdminAgencyJpaService.findAgencyList(agencyMap, pageRequest);
        List<AdminAgencyDto> findAgencyList = agencyList.stream().collect(Collectors.toList());

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
        then(adminAgencyJpaQueryRepository).should(times(1)).findAgencyList(agencyMap, pageRequest);
        then(adminAgencyJpaQueryRepository).should(atLeastOnce()).findAgencyList(agencyMap, pageRequest);
        then(adminAgencyJpaQueryRepository).shouldHaveNoMoreInteractions();
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
        when(adminAgencyJpaRepository.findByIdx(adminAgencyEntity.getIdx())).thenReturn(Optional.ofNullable(adminAgencyEntity));
        AdminAgencyDto agencyInfo = mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(adminAgencyEntity.getIdx());
        assertThat(agencyInfo.getAgencyName()).isEqualTo(adminAgencyEntity.getAgencyName());
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo(adminAgencyEntity.getAgencyDescription());
        assertThat(agencyInfo.getVisible()).isEqualTo(adminAgencyEntity.getVisible());

        // verify
        verify(adminAgencyJpaRepository, times(1)).findByIdx(adminAgencyEntity.getIdx());
        verify(adminAgencyJpaRepository, atLeastOnce()).findByIdx(adminAgencyEntity.getIdx());
        verifyNoMoreInteractions(adminAgencyJpaRepository);

        InOrder inOrder = inOrder(adminAgencyJpaRepository);
        inOrder.verify(adminAgencyJpaRepository).findByIdx(adminAgencyEntity.getIdx());
    }

    @Test
    @DisplayName("소속사상세조회BDD테스트")
    void 소속사상세조회BDD테스트() {
        // when
        given(adminAgencyJpaRepository.findByIdx(adminAgencyEntity.getIdx())).willReturn(Optional.ofNullable(adminAgencyEntity));
        AdminAgencyDto agencyInfo = mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(adminAgencyEntity.getIdx());
        assertThat(agencyInfo.getAgencyName()).isEqualTo(adminAgencyEntity.getAgencyName());
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo(adminAgencyEntity.getAgencyDescription());
        assertThat(agencyInfo.getVisible()).isEqualTo(adminAgencyEntity.getVisible());

        // verify
        then(adminAgencyJpaRepository).should(times(1)).findByIdx(adminAgencyEntity.getIdx());
        then(adminAgencyJpaRepository).should(atLeastOnce()).findByIdx(adminAgencyEntity.getIdx());
        then(adminAgencyJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("소속사 수정 Mockito 테스트")
    void 소속사수정Mockito테스트() {
        // given
        AdminAgencyEntity updateAgencyEntity = AdminAgencyEntity.builder()
                .idx(adminAgencyEntity.getIdx())
                .agencyName("newAgency")
                .agencyDescription("newAgency")
                .visible("Y")
                .build();

        // when
        when(adminAgencyJpaRepository.findByIdx(updateAgencyEntity.getIdx())).thenReturn(Optional.of(updateAgencyEntity));
        when(adminAgencyJpaRepository.save(updateAgencyEntity)).thenReturn(updateAgencyEntity);
        AdminAgencyDto agencyInfo = mockAdminAgencyJpaService.updateAgency(updateAgencyEntity.getIdx(), updateAgencyEntity);

        // then
        assertThat(agencyInfo.getAgencyName()).isEqualTo(updateAgencyEntity.getAgencyName());
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo(updateAgencyEntity.getAgencyDescription());

        // verify
        verify(adminAgencyJpaRepository, times(1)).findByIdx(updateAgencyEntity.getIdx());
        verify(adminAgencyJpaRepository, atLeastOnce()).findByIdx(updateAgencyEntity.getIdx());
        verifyNoMoreInteractions(adminAgencyJpaRepository);

        InOrder inOrder = inOrder(adminAgencyJpaRepository);
        inOrder.verify(adminAgencyJpaRepository).findByIdx(updateAgencyEntity.getIdx());
    }

    @Test
    @DisplayName("소속사 수정 BDD 테스트")
    void 소속사수정BDD테스트() {
        // given
        AdminAgencyEntity updateAgencyEntity = AdminAgencyEntity.builder()
                .idx(adminAgencyEntity.getIdx())
                .agencyName("newAgency")
                .agencyDescription("newAgency")
                .visible("Y")
                .build();

        // when
        given(adminAgencyJpaRepository.findByIdx(updateAgencyEntity.getIdx())).willReturn(Optional.of(updateAgencyEntity));
        given(adminAgencyJpaRepository.save(updateAgencyEntity)).willReturn(updateAgencyEntity);
        AdminAgencyDto agencyInfo = mockAdminAgencyJpaService.updateAgency(updateAgencyEntity.getIdx(), updateAgencyEntity);

        // then
        assertThat(agencyInfo.getAgencyName()).isEqualTo(updateAgencyEntity.getAgencyName());
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo(updateAgencyEntity.getAgencyDescription());

        // verify
        then(adminAgencyJpaRepository).should(times(1)).findByIdx(updateAgencyEntity.getIdx());
        then(adminAgencyJpaRepository).should(atLeastOnce()).findByIdx(updateAgencyEntity.getIdx());
        then(adminAgencyJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("소속사 삭제 테스트")
    void 소속사삭제테스트() {
        // when
        Long deleteIdx = adminAgencyJpaService.deleteAgency(adminAgencyEntity.getIdx());

        // then
        assertThat(adminAgencyEntity.getIdx()).isEqualTo(deleteIdx);
    }
}

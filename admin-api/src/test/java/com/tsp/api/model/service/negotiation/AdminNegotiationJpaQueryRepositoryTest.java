package com.tsp.api.model.service.negotiation;

import com.tsp.api.model.domain.AdminModelDTO;
import com.tsp.api.model.domain.AdminModelEntity;
import com.tsp.api.model.domain.CareerJson;
import com.tsp.api.model.domain.agency.AdminAgencyDTO;
import com.tsp.api.model.domain.agency.AdminAgencyEntity;
import com.tsp.api.model.domain.negotiation.AdminNegotiationDTO;
import com.tsp.api.model.domain.negotiation.AdminNegotiationEntity;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.of;
import static org.assertj.core.api.Assertions.assertThat;
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
@DisplayName("모델 섭외 Repository Test")
class AdminNegotiationJpaQueryRepositoryTest {
    @Mock private AdminNegotiationJpaQueryRepository mockAdminNegotiationJpaQueryRepository;
    private final AdminNegotiationJpaQueryRepository adminNegotiationJpaQueryRepository;
    private final EntityManager em;

    private AdminModelEntity adminModelEntity;
    private AdminModelDTO adminModelDTO;
    private AdminNegotiationEntity adminNegotiationEntity;
    private AdminNegotiationDTO adminNegotiationDTO;
    private AdminAgencyEntity adminAgencyEntity;
    private AdminAgencyDTO adminAgencyDTO;

    void createModelAndNegotiation() {
        adminAgencyEntity = AdminAgencyEntity.builder()
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

        adminAgencyDTO = AdminAgencyEntity.toDto(adminAgencyEntity);

        ArrayList<CareerJson> careerList = new ArrayList<>();
        careerList.add(new CareerJson("title","txt"));

        adminModelEntity = AdminModelEntity.builder()
                .categoryCd(1)
                .categoryAge(2)
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .status("active")
                .newYn("N")
                .favoriteCount(1)
                .viewCount(1)
                .adminAgencyEntity(adminAgencyEntity)
                .careerList(careerList)
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        em.persist(adminModelEntity);

        adminModelDTO = AdminModelEntity.toDto(adminModelEntity);

        adminNegotiationEntity = AdminNegotiationEntity.builder()
                .modelKorName(adminModelEntity.getModelKorName())
                .modelNegotiationDesc("영화 프로젝트 참여")
                .modelNegotiationDate(now())
                .name("조찬희")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        adminNegotiationDTO = AdminNegotiationEntity.toDto(adminNegotiationEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createModelAndNegotiation();
    }

    @Test
    @DisplayName("모델 섭외 리스트 조회 테스트")
    void 모델섭외리스트조회테스트() {
        // given
        Map<String, Object> negotiationMap = new HashMap<>();
        negotiationMap.put("searchKeyword", "김예영");
        PageRequest pageRequest = PageRequest.of(1, 100);

        // then
        assertThat(adminNegotiationJpaQueryRepository.findNegotiationList(negotiationMap, pageRequest)).isNotEmpty();

        Map<String, Object> lastMonthNegotiationMap = new HashMap<>();
        lastMonthNegotiationMap.put("searchStartTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 1, 0, 0, 0, 0));
        lastMonthNegotiationMap.put("searchEndTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 30, 23, 59, 59));
        PageRequest pageRequest2 = PageRequest.of(1, 100);

        // then
        assertThat(adminNegotiationJpaQueryRepository.findNegotiationList(negotiationMap, pageRequest2)).isNotEmpty();

        Map<String, Object> currentNegotiationMap = new HashMap<>();
        currentNegotiationMap.put("searchStartTime", of(now().getYear(), LocalDate.now().getMonth(), 1, 0, 0, 0, 0));
        currentNegotiationMap.put("searchEndTime", of(now().getYear(), LocalDate.now().getMonth(), 30, 23, 59, 59));
        PageRequest pageRequest3 = PageRequest.of(1, 100);

        // then
        assertThat(adminNegotiationJpaQueryRepository.findNegotiationList(negotiationMap, pageRequest3)).isNotEmpty();
    }

    @Test
    @DisplayName("모델 섭외 Mockito 조회 테스트")
    void 모델섭외Mockito조회테스트() {
        // given
        Map<String, Object> negotiationMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminNegotiationDTO> negotiationList = new ArrayList<>();
//        negotiationList.add(AdminNegotiationDTO.builder().modelIdx(adminModelEntity.getIdx())
//                .modelNegotiationDesc("영화 프로젝트 참여 테스트 첫번째").modelNegotiationDate(now()).build());
//        negotiationList.add(AdminNegotiationDTO.builder().modelIdx(adminModelEntity.getIdx())
//                .modelNegotiationDesc("영화 프로젝트 참여 테스트 두번째").modelNegotiationDate(now()).build());

        Page<AdminNegotiationDTO> resultNegotiation = new PageImpl<>(negotiationList, pageRequest, negotiationList.size());

        // when
        when(mockAdminNegotiationJpaQueryRepository.findNegotiationList(negotiationMap, pageRequest)).thenReturn(resultNegotiation);
        Page<AdminNegotiationDTO> newModelNegotiationList = mockAdminNegotiationJpaQueryRepository.findNegotiationList(negotiationMap, pageRequest);
        List<AdminNegotiationDTO> findNegotiationList = newModelNegotiationList.stream().collect(Collectors.toList());

        // then
        assertThat(findNegotiationList.get(0).getIdx()).isEqualTo(negotiationList.get(0).getIdx());
        assertThat(findNegotiationList.get(0).getModelNegotiationDesc()).isEqualTo(negotiationList.get(0).getModelNegotiationDesc());

        // verify
        verify(mockAdminNegotiationJpaQueryRepository, times(1)).findNegotiationList(negotiationMap, pageRequest);
        verify(mockAdminNegotiationJpaQueryRepository, atLeastOnce()).findNegotiationList(negotiationMap, pageRequest);
        verifyNoMoreInteractions(mockAdminNegotiationJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminNegotiationJpaQueryRepository);
        inOrder.verify(mockAdminNegotiationJpaQueryRepository).findNegotiationList(negotiationMap, pageRequest);
    }

    @Test
    @DisplayName("모델 섭외 BDD 조회 테스트")
    void 모델섭외BDD조회테스트() {
        // given
        Map<String, Object> negotiationMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminNegotiationDTO> negotiationList = new ArrayList<>();
//        negotiationList.add(AdminNegotiationDTO.builder().modelIdx(adminModelEntity.getIdx())
//                .modelNegotiationDesc("영화 프로젝트 참여 테스트 첫번째").modelNegotiationDate(now()).build());
//        negotiationList.add(AdminNegotiationDTO.builder().modelIdx(adminModelEntity.getIdx())
//                .modelNegotiationDesc("영화 프로젝트 참여 테스트 두번째").modelNegotiationDate(now()).build());

        Page<AdminNegotiationDTO> resultNegotiation = new PageImpl<>(negotiationList, pageRequest, negotiationList.size());

        // when
        given(mockAdminNegotiationJpaQueryRepository.findNegotiationList(negotiationMap, pageRequest)).willReturn(resultNegotiation);
        Page<AdminNegotiationDTO> newModelNegotiationList = mockAdminNegotiationJpaQueryRepository.findNegotiationList(negotiationMap, pageRequest);
        List<AdminNegotiationDTO> findNegotiationList = newModelNegotiationList.stream().collect(Collectors.toList());

        // then
        assertThat(findNegotiationList.get(0).getIdx()).isEqualTo(negotiationList.get(0).getIdx());
        assertThat(findNegotiationList.get(0).getModelNegotiationDesc()).isEqualTo(negotiationList.get(0).getModelNegotiationDesc());

        // verify
        then(mockAdminNegotiationJpaQueryRepository).should(times(1)).findNegotiationList(negotiationMap, pageRequest);
        then(mockAdminNegotiationJpaQueryRepository).should(atLeastOnce()).findNegotiationList(negotiationMap, pageRequest);
        then(mockAdminNegotiationJpaQueryRepository).shouldHaveNoMoreInteractions();
    }
}

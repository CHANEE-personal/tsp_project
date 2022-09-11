package com.tsp.new_tsp_front.api.model.negotiation.service.impl;

import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyDTO;
import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyEntity;
import com.tsp.new_tsp_front.api.agency.service.impl.AgencyMapper;
import com.tsp.new_tsp_front.api.model.domain.CareerJson;
import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import com.tsp.new_tsp_front.api.model.domain.negotiation.FrontNegotiationDTO;
import com.tsp.new_tsp_front.api.model.domain.negotiation.FrontNegotiationEntity;
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
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.api.model.service.impl.ModelMapper.INSTANCE;
import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;
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
@DisplayName("모델 스케줄 Repository Test")
class FrontNegotiationJpaRepositoryTest {
    @Mock
    private FrontNegotiationJpaRepository mockFrontNegotiationJpaRepository;
    private final FrontNegotiationJpaRepository frontNegotiationJpaRepository;
    private final EntityManager em;

    private FrontModelEntity frontModelEntity;
    private FrontModelDTO frontModelDTO;
    private FrontNegotiationEntity frontNegotiationEntity;
    private FrontNegotiationDTO frontNegotiationDTO;
    private FrontAgencyEntity frontAgencyEntity;
    private FrontAgencyDTO frontAgencyDTO;

    void createModelAndNegotiation() {
        frontAgencyEntity = FrontAgencyEntity.builder()
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

        frontAgencyDTO = AgencyMapper.INSTANCE.toDto(frontAgencyEntity);

        ArrayList<CareerJson> careerList = new ArrayList<>();
        careerList.add(new CareerJson("title","txt"));

        frontModelEntity = FrontModelEntity.builder()
                .categoryCd(1)
                .categoryAge(2)
                .agencyIdx(frontAgencyEntity.getIdx())
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .modelFavoriteCount(1)
                .newYn("N")
                .frontAgencyEntity(frontAgencyEntity)
                .modelViewCount(1)
                .visible("Y")
                .build();

        em.persist(frontModelEntity);

        frontModelDTO = INSTANCE.toDto(frontModelEntity);

        frontNegotiationEntity = FrontNegotiationEntity.builder()
                .modelIdx(frontModelEntity.getIdx())
                .modelKorName(frontModelEntity.getModelKorName())
                .modelNegotiationDesc("영화 프로젝트 참여")
                .modelNegotiationDate(now())
                .name("조찬희")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        frontNegotiationDTO = FrontNegotiationMapper.INSTANCE.toDto(frontNegotiationEntity);
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
        negotiationMap.put("jpaStartPage", 0);
        negotiationMap.put("size", 100);

        // then
        assertThat(frontNegotiationJpaRepository.findModelNegotiationList(negotiationMap)).isNotEmpty();

        Map<String, Object> lastMonthNegotiationMap = new HashMap<>();
        lastMonthNegotiationMap.put("searchStartTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 1, 0, 0, 0, 0));
        lastMonthNegotiationMap.put("searchEndTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 30, 23, 59, 59));
        lastMonthNegotiationMap.put("jpaStartPage", 0);
        lastMonthNegotiationMap.put("size", 100);

        // then
        assertThat(frontNegotiationJpaRepository.findModelNegotiationList(negotiationMap)).isNotEmpty();

        Map<String, Object> currentNegotiationMap = new HashMap<>();
        currentNegotiationMap.put("searchStartTime", of(now().getYear(), LocalDate.now().getMonth(), 1, 0, 0, 0, 0));
        currentNegotiationMap.put("searchEndTime", of(now().getYear(), LocalDate.now().getMonth(), 30, 23, 59, 59));
        currentNegotiationMap.put("jpaStartPage", 0);
        currentNegotiationMap.put("size", 100);

        // then
        assertThat(frontNegotiationJpaRepository.findModelNegotiationList(negotiationMap)).isNotEmpty();
    }

    @Test
    @DisplayName("모델 섭외 Mockito 조회 테스트")
    void 모델섭외Mockito조회테스트() {
        // given
        Map<String, Object> negotiationMap = new HashMap<>();
        negotiationMap.put("jpaStartPage", 1);
        negotiationMap.put("size", 3);

        List<FrontNegotiationDTO> negotiationList = new ArrayList<>();
        negotiationList.add(FrontNegotiationDTO.builder().modelIdx(frontModelEntity.getIdx())
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 첫번째").modelNegotiationDate(now()).build());
        negotiationList.add(FrontNegotiationDTO.builder().modelIdx(frontModelEntity.getIdx())
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 두번째").modelNegotiationDate(now()).build());

        List<FrontModelDTO> modelNegotiationList = new ArrayList<>();
        modelNegotiationList.add(FrontModelDTO.builder().idx(3).categoryCd(1).modelKorName("조찬희")
                .modelNegotiationList(negotiationList).build());

        // when
        when(mockFrontNegotiationJpaRepository.findModelNegotiationList(negotiationMap)).thenReturn(modelNegotiationList);
        List<FrontModelDTO> newModelNegotiationList = mockFrontNegotiationJpaRepository.findModelNegotiationList(negotiationMap);

        // then
        assertThat(newModelNegotiationList.get(0).getIdx()).isEqualTo(modelNegotiationList.get(0).getIdx());
        assertThat(newModelNegotiationList.get(0).getModelNegotiationList().get(0).getModelNegotiationDesc()).isEqualTo(negotiationList.get(0).getModelNegotiationDesc());

        // verify
        verify(mockFrontNegotiationJpaRepository, times(1)).findModelNegotiationList(negotiationMap);
        verify(mockFrontNegotiationJpaRepository, atLeastOnce()).findModelNegotiationList(negotiationMap);
        verifyNoMoreInteractions(mockFrontNegotiationJpaRepository);

        InOrder inOrder = inOrder(mockFrontNegotiationJpaRepository);
        inOrder.verify(mockFrontNegotiationJpaRepository).findModelNegotiationList(negotiationMap);
    }

    @Test
    @DisplayName("모델 섭외 BDD 조회 테스트")
    void 모델섭외BDD조회테스트() {
        // given
        Map<String, Object> negotiationMap = new HashMap<>();
        negotiationMap.put("jpaStartPage", 1);
        negotiationMap.put("size", 3);

        List<FrontNegotiationDTO> negotiationList = new ArrayList<>();
        negotiationList.add(FrontNegotiationDTO.builder().modelIdx(frontModelEntity.getIdx())
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 첫번째").modelNegotiationDate(now()).build());
        negotiationList.add(FrontNegotiationDTO.builder().modelIdx(frontModelEntity.getIdx())
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 두번째").modelNegotiationDate(now()).build());

        List<FrontModelDTO> modelNegotiationList = new ArrayList<>();
        modelNegotiationList.add(FrontModelDTO.builder().idx(3).categoryCd(1).modelKorName("조찬희")
                .modelNegotiationList(negotiationList).build());

        // when
        given(mockFrontNegotiationJpaRepository.findModelNegotiationList(negotiationMap)).willReturn(modelNegotiationList);
        List<FrontModelDTO> newModelNegotiationList = mockFrontNegotiationJpaRepository.findModelNegotiationList(negotiationMap);

        // then
        assertThat(newModelNegotiationList.get(0).getIdx()).isEqualTo(modelNegotiationList.get(0).getIdx());
        assertThat(newModelNegotiationList.get(0).getModelNegotiationList().get(0).getModelNegotiationDesc()).isEqualTo(negotiationList.get(0).getModelNegotiationDesc());

        // verify
        then(mockFrontNegotiationJpaRepository).should(times(1)).findModelNegotiationList(negotiationMap);
        then(mockFrontNegotiationJpaRepository).should(atLeastOnce()).findModelNegotiationList(negotiationMap);
        then(mockFrontNegotiationJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델섭외등록Mockito테스트")
    void 모델섭외등록Mockito테스트() {
        // given
        frontNegotiationJpaRepository.insertModelNegotiation(frontNegotiationEntity);

        // when
        when(mockFrontNegotiationJpaRepository.findOneNegotiation(frontNegotiationEntity)).thenReturn(frontNegotiationDTO);
        FrontNegotiationDTO negotiationInfo = mockFrontNegotiationJpaRepository.findOneNegotiation(frontNegotiationEntity);

        // then
        assertThat(negotiationInfo.getModelIdx()).isEqualTo(frontModelEntity.getIdx());
        assertThat(negotiationInfo.getModelKorName()).isEqualTo("조찬희");
        assertThat(negotiationInfo.getModelNegotiationDesc()).isNotNull();

        // verify
        verify(mockFrontNegotiationJpaRepository, times(1)).findOneNegotiation(frontNegotiationEntity);
        verify(mockFrontNegotiationJpaRepository, atLeastOnce()).findOneNegotiation(frontNegotiationEntity);
        verifyNoMoreInteractions(mockFrontNegotiationJpaRepository);

        InOrder inOrder = inOrder(mockFrontNegotiationJpaRepository);
        inOrder.verify(mockFrontNegotiationJpaRepository).findOneNegotiation(frontNegotiationEntity);
    }

    @Test
    @DisplayName("모델섭외등록BDD테스트")
    void 모델섭외등록BDD테스트() {
        // given
        frontNegotiationJpaRepository.insertModelNegotiation(frontNegotiationEntity);

        // when
        given(mockFrontNegotiationJpaRepository.findOneNegotiation(frontNegotiationEntity)).willReturn(frontNegotiationDTO);
        FrontNegotiationDTO negotiationInfo = mockFrontNegotiationJpaRepository.findOneNegotiation(frontNegotiationEntity);

        // then
        assertThat(negotiationInfo.getModelIdx()).isEqualTo(frontModelEntity.getIdx());
        assertThat(negotiationInfo.getModelKorName()).isEqualTo("조찬희");
        assertThat(negotiationInfo.getModelNegotiationDesc()).isNotNull();

        // verify
        then(mockFrontNegotiationJpaRepository).should(times(1)).findOneNegotiation(frontNegotiationEntity);
        then(mockFrontNegotiationJpaRepository).should(atLeastOnce()).findOneNegotiation(frontNegotiationEntity);
        then(mockFrontNegotiationJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델섭외수정Mockito테스트")
    void 모델섭외수정Mockito테스트() {
        // given
        Integer idx = frontNegotiationJpaRepository.insertModelNegotiation(frontNegotiationEntity).getIdx();

        frontNegotiationEntity = FrontNegotiationEntity.builder()
                .idx(idx)
                .modelIdx(frontModelEntity.getIdx())
                .modelKorName(frontModelEntity.getModelKorName())
                .modelNegotiationDesc("섭외 수정 테스트")
                .modelNegotiationDate(now())
                .name("조찬희")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        FrontNegotiationDTO frontNegotiationDTO = FrontNegotiationMapper.INSTANCE.toDto(frontNegotiationEntity);

        frontNegotiationJpaRepository.updateModelNegotiation(frontNegotiationEntity);

        // when
        when(mockFrontNegotiationJpaRepository.findOneNegotiation(frontNegotiationEntity)).thenReturn(frontNegotiationDTO);
        FrontNegotiationDTO negotiationInfo = mockFrontNegotiationJpaRepository.findOneNegotiation(frontNegotiationEntity);

        // then
        assertThat(negotiationInfo.getModelIdx()).isEqualTo(frontModelEntity.getIdx());
        assertThat(negotiationInfo.getModelNegotiationDesc()).isEqualTo("섭외 수정 테스트");

        // verify
        verify(mockFrontNegotiationJpaRepository, times(1)).findOneNegotiation(frontNegotiationEntity);
        verify(mockFrontNegotiationJpaRepository, atLeastOnce()).findOneNegotiation(frontNegotiationEntity);
        verifyNoMoreInteractions(mockFrontNegotiationJpaRepository);

        InOrder inOrder = inOrder(mockFrontNegotiationJpaRepository);
        inOrder.verify(mockFrontNegotiationJpaRepository).findOneNegotiation(frontNegotiationEntity);
    }

    @Test
    @DisplayName("모델스케줄수정BDD테스트")
    void 모델스케줄수정BDD테스트() {
        // given
        Integer idx = frontNegotiationJpaRepository.insertModelNegotiation(frontNegotiationEntity).getIdx();

        frontNegotiationEntity = FrontNegotiationEntity.builder()
                .idx(idx)
                .modelIdx(frontModelEntity.getIdx())
                .modelKorName(frontModelEntity.getModelKorName())
                .modelNegotiationDesc("섭외 수정 테스트")
                .modelNegotiationDate(now())
                .name("조찬희")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        FrontNegotiationDTO frontNegotiationDTO = FrontNegotiationMapper.INSTANCE.toDto(frontNegotiationEntity);

        frontNegotiationJpaRepository.updateModelNegotiation(frontNegotiationEntity);

        // when
        given(mockFrontNegotiationJpaRepository.findOneNegotiation(frontNegotiationEntity)).willReturn(frontNegotiationDTO);
        FrontNegotiationDTO negotiationInfo = mockFrontNegotiationJpaRepository.findOneNegotiation(frontNegotiationEntity);

        // then
        assertThat(negotiationInfo.getModelIdx()).isEqualTo(frontModelEntity.getIdx());
        assertThat(negotiationInfo.getModelNegotiationDesc()).isEqualTo("섭외 수정 테스트");

        // verify
        then(mockFrontNegotiationJpaRepository).should(times(1)).findOneNegotiation(frontNegotiationEntity);
        then(mockFrontNegotiationJpaRepository).should(atLeastOnce()).findOneNegotiation(frontNegotiationEntity);
        then(mockFrontNegotiationJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델섭외삭제Mockito테스트")
    void 모델섭외삭제Mockito테스트() {
        // given
        em.persist(frontNegotiationEntity);
        frontNegotiationDTO = FrontNegotiationMapper.INSTANCE.toDto(frontNegotiationEntity);

        // when
        when(mockFrontNegotiationJpaRepository.findOneNegotiation(frontNegotiationEntity)).thenReturn(frontNegotiationDTO);
        Integer deleteIdx = frontNegotiationJpaRepository.deleteModelNegotiation(frontNegotiationEntity.getIdx());

        // then
        assertThat(mockFrontNegotiationJpaRepository.findOneNegotiation(frontNegotiationEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockFrontNegotiationJpaRepository, times(1)).findOneNegotiation(frontNegotiationEntity);
        verify(mockFrontNegotiationJpaRepository, atLeastOnce()).findOneNegotiation(frontNegotiationEntity);
        verifyNoMoreInteractions(mockFrontNegotiationJpaRepository);

        InOrder inOrder = inOrder(mockFrontNegotiationJpaRepository);
        inOrder.verify(mockFrontNegotiationJpaRepository).findOneNegotiation(frontNegotiationEntity);
    }

    @Test
    @DisplayName("모델섭외삭제BDD테스트")
    void 모델섭외삭제BDD테스트() {
        // given
        em.persist(frontNegotiationEntity);
        frontNegotiationDTO = FrontNegotiationMapper.INSTANCE.toDto(frontNegotiationEntity);

        // when
        given(mockFrontNegotiationJpaRepository.findOneNegotiation(frontNegotiationEntity)).willReturn(frontNegotiationDTO);
        Integer deleteIdx = frontNegotiationJpaRepository.deleteModelNegotiation(frontNegotiationEntity.getIdx());

        // then
        assertThat(mockFrontNegotiationJpaRepository.findOneNegotiation(frontNegotiationEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockFrontNegotiationJpaRepository).should(times(1)).findOneNegotiation(frontNegotiationEntity);
        then(mockFrontNegotiationJpaRepository).should(atLeastOnce()).findOneNegotiation(frontNegotiationEntity);
        then(mockFrontNegotiationJpaRepository).shouldHaveNoMoreInteractions();
    }
}
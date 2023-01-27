package com.tsp.api.model.negotiation.service;

import com.tsp.api.common.domain.NewCodeEntity;
import com.tsp.api.model.domain.CareerJson;
import com.tsp.api.model.domain.FrontModelDTO;
import com.tsp.api.model.domain.FrontModelEntity;
import com.tsp.api.model.domain.agency.FrontAgencyDTO;
import com.tsp.api.model.domain.agency.FrontAgencyEntity;
import com.tsp.api.model.domain.negotiation.FrontNegotiationDTO;
import com.tsp.api.model.domain.negotiation.FrontNegotiationEntity;
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
import static org.mockito.Mockito.atLeastOnce;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("모델 섭외 Service Test")
class FrontNegotiationApiServiceTest {
    @Mock
    private FrontNegotiationJpaApiService mockFrontNegotiationJpaApiService;
    private final FrontNegotiationJpaApiService frontNegotiationJpaApiService;
    private final EntityManager em;

    private FrontModelEntity frontModelEntity;
    private FrontModelDTO frontModelDTO;
    private FrontNegotiationEntity frontNegotiationEntity;
    private FrontNegotiationDTO frontNegotiationDTO;
    private FrontAgencyEntity frontAgencyEntity;
    private FrontAgencyDTO frontAgencyDTO;

    void createModelAndNegotiation() {
        ArrayList<CareerJson> careerList = new ArrayList<>();
        careerList.add(new CareerJson("title","txt"));

        frontAgencyEntity = FrontAgencyEntity.builder()
                .agencyName("소속사")
                .agencyDescription("소속사")
                .favoriteCount(1)
                .visible("Y")
                .build();

        em.persist(frontAgencyEntity);

        NewCodeEntity newCodeEntity = NewCodeEntity.builder()
                .categoryCd(999)
                .categoryNm("남성모델")
                .visible("Y")
                .cmmType("model")
                .build();

        em.persist(newCodeEntity);

        frontModelEntity = FrontModelEntity.builder()
                .newModelCodeJpaDTO(newCodeEntity)
                .categoryAge(2)
                .frontAgencyEntity(frontAgencyEntity)
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
                .newYn("N")
                .modelFavoriteCount(1)
                .visible("Y")
                .build();

        em.persist(frontModelEntity);

        frontModelDTO = FrontModelEntity.toDto(frontModelEntity);

        frontNegotiationEntity = FrontNegotiationEntity.builder()
                .frontModelEntity(frontModelEntity)
                .modelKorName(frontModelEntity.getModelKorName())
                .modelNegotiationDesc("영화 프로젝트 참여")
                .modelNegotiationDate(now())
                .name("조찬희")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        frontNegotiationDTO = FrontNegotiationEntity.toDto(frontNegotiationEntity);
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
        assertThat(frontNegotiationJpaApiService.findModelNegotiationList(negotiationMap, pageRequest)).isNotEmpty();

        Map<String, Object> lastMonthNegotiationMap = new HashMap<>();
        lastMonthNegotiationMap.put("searchStartTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 1, 0, 0, 0, 0));
        lastMonthNegotiationMap.put("searchEndTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 30, 23, 59, 59));
        PageRequest pageRequest2 = PageRequest.of(1, 100);

        // then
        assertThat(frontNegotiationJpaApiService.findModelNegotiationList(negotiationMap, pageRequest2)).isNotEmpty();

        Map<String, Object> currentNegotiationMap = new HashMap<>();
        currentNegotiationMap.put("searchStartTime", of(now().getYear(), LocalDate.now().getMonth(), 1, 0, 0, 0, 0));
        currentNegotiationMap.put("searchEndTime", of(now().getYear(), LocalDate.now().getMonth(), 30, 23, 59, 59));
        PageRequest pageRequest3 = PageRequest.of(1, 100);

        // then
        assertThat(frontNegotiationJpaApiService.findModelNegotiationList(negotiationMap, pageRequest3)).isNotEmpty();
    }

    @Test
    @DisplayName("모델 섭외 Mockito 조회 테스트")
    void 모델섭외Mockito조회테스트() {
        // given
        Map<String, Object> negotiationMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontNegotiationDTO> negotiationList = new ArrayList<>();
        negotiationList.add(FrontNegotiationDTO.builder().frontModelDTO(frontModelDTO)
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 첫번째").modelNegotiationDate(now()).build());
        negotiationList.add(FrontNegotiationDTO.builder().frontModelDTO(frontModelDTO)
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 두번째").modelNegotiationDate(now()).build());

        Page<FrontNegotiationDTO> resultNegotiation = new PageImpl<>(negotiationList, pageRequest, negotiationList.size());
        // when
        when(mockFrontNegotiationJpaApiService.findModelNegotiationList(negotiationMap, pageRequest)).thenReturn(resultNegotiation);
        Page<FrontNegotiationDTO> newModelNegotiationList = mockFrontNegotiationJpaApiService.findModelNegotiationList(negotiationMap, pageRequest);
        List<FrontNegotiationDTO> findNegotiationList = newModelNegotiationList.stream().collect(Collectors.toList());

        // then
        assertThat(findNegotiationList.get(0).getIdx()).isEqualTo(negotiationList.get(0).getIdx());
        assertThat(findNegotiationList.get(0).getModelNegotiationDesc()).isEqualTo(negotiationList.get(0).getModelNegotiationDesc());

        // verify
        verify(mockFrontNegotiationJpaApiService, times(1)).findModelNegotiationList(negotiationMap, pageRequest);
        verify(mockFrontNegotiationJpaApiService, atLeastOnce()).findModelNegotiationList(negotiationMap, pageRequest);
        verifyNoMoreInteractions(mockFrontNegotiationJpaApiService);

        InOrder inOrder = inOrder(mockFrontNegotiationJpaApiService);
        inOrder.verify(mockFrontNegotiationJpaApiService).findModelNegotiationList(negotiationMap, pageRequest);
    }

    @Test
    @DisplayName("모델 섭외 BDD 조회 테스트")
    void 모델섭외BDD조회테스트() {
        // given
        Map<String, Object> negotiationMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontNegotiationDTO> negotiationList = new ArrayList<>();
        negotiationList.add(FrontNegotiationDTO.builder().frontModelDTO(frontModelDTO)
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 첫번째").modelNegotiationDate(now()).build());
        negotiationList.add(FrontNegotiationDTO.builder().frontModelDTO(frontModelDTO)
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 두번째").modelNegotiationDate(now()).build());

        Page<FrontNegotiationDTO> resultNegotiation = new PageImpl<>(negotiationList, pageRequest, negotiationList.size());
        // when
        given(mockFrontNegotiationJpaApiService.findModelNegotiationList(negotiationMap, pageRequest)).willReturn(resultNegotiation);
        Page<FrontNegotiationDTO> newModelNegotiationList = mockFrontNegotiationJpaApiService.findModelNegotiationList(negotiationMap, pageRequest);
        List<FrontNegotiationDTO> findNegotiationList = newModelNegotiationList.stream().collect(Collectors.toList());

        // then
        assertThat(findNegotiationList.get(0).getIdx()).isEqualTo(negotiationList.get(0).getIdx());
        assertThat(findNegotiationList.get(0).getModelNegotiationDesc()).isEqualTo(negotiationList.get(0).getModelNegotiationDesc());

        // verify
        then(mockFrontNegotiationJpaApiService).should(times(1)).findModelNegotiationList(negotiationMap, pageRequest);
        then(mockFrontNegotiationJpaApiService).should(atLeastOnce()).findModelNegotiationList(negotiationMap, pageRequest);
        then(mockFrontNegotiationJpaApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델섭외등록Mockito테스트")
    void 모델섭외등록Mockito테스트() {
        // given
        frontNegotiationJpaApiService.insertModelNegotiation(frontModelDTO.getIdx(), frontNegotiationEntity);

        // when
        when(mockFrontNegotiationJpaApiService.findOneNegotiation(frontNegotiationEntity.getIdx())).thenReturn(frontNegotiationDTO);
        FrontNegotiationDTO negotiationInfo = mockFrontNegotiationJpaApiService.findOneNegotiation(frontNegotiationEntity.getIdx());

        // then
        assertThat(negotiationInfo.getFrontModelDTO().getIdx()).isEqualTo(frontModelEntity.getIdx());
        assertThat(negotiationInfo.getModelKorName()).isEqualTo("조찬희");
        assertThat(negotiationInfo.getModelNegotiationDesc()).isNotNull();

        // verify
        verify(mockFrontNegotiationJpaApiService, times(1)).findOneNegotiation(frontNegotiationEntity.getIdx());
        verify(mockFrontNegotiationJpaApiService, atLeastOnce()).findOneNegotiation(frontNegotiationEntity.getIdx());
        verifyNoMoreInteractions(mockFrontNegotiationJpaApiService);

        InOrder inOrder = inOrder(mockFrontNegotiationJpaApiService);
        inOrder.verify(mockFrontNegotiationJpaApiService).findOneNegotiation(frontNegotiationEntity.getIdx());
    }

    @Test
    @DisplayName("모델섭외등록BDD테스트")
    void 모델섭외등록BDD테스트() {
        // given
        frontNegotiationJpaApiService.insertModelNegotiation(frontModelDTO.getIdx(), frontNegotiationEntity);

        // when
        given(mockFrontNegotiationJpaApiService.findOneNegotiation(frontNegotiationEntity.getIdx())).willReturn(frontNegotiationDTO);
        FrontNegotiationDTO negotiationInfo = mockFrontNegotiationJpaApiService.findOneNegotiation(frontNegotiationEntity.getIdx());

        // then
        assertThat(negotiationInfo.getFrontModelDTO().getIdx()).isEqualTo(frontModelEntity.getIdx());
        assertThat(negotiationInfo.getModelKorName()).isEqualTo("조찬희");
        assertThat(negotiationInfo.getModelNegotiationDesc()).isNotNull();

        // verify
        then(mockFrontNegotiationJpaApiService).should(times(1)).findOneNegotiation(frontNegotiationEntity.getIdx());
        then(mockFrontNegotiationJpaApiService).should(atLeastOnce()).findOneNegotiation(frontNegotiationEntity.getIdx());
        then(mockFrontNegotiationJpaApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델섭외수정Mockito테스트")
    void 모델섭외수정Mockito테스트() {
        // given
        Long idx = frontNegotiationJpaApiService.insertModelNegotiation(frontModelDTO.getIdx(), frontNegotiationEntity).getIdx();

        frontNegotiationEntity = FrontNegotiationEntity.builder()
                .idx(idx)
                .frontModelEntity(frontModelEntity)
                .modelKorName(frontModelEntity.getModelKorName())
                .modelNegotiationDesc("섭외 수정 테스트")
                .modelNegotiationDate(now())
                .name("조찬희")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        FrontNegotiationDTO frontNegotiationDTO = FrontNegotiationEntity.toDto(frontNegotiationEntity);

        frontNegotiationJpaApiService.updateModelNegotiation(frontNegotiationDTO.getIdx(), frontNegotiationEntity);

        // when
        when(mockFrontNegotiationJpaApiService.findOneNegotiation(frontNegotiationEntity.getIdx())).thenReturn(frontNegotiationDTO);
        FrontNegotiationDTO negotiationInfo = mockFrontNegotiationJpaApiService.findOneNegotiation(frontNegotiationEntity.getIdx());

        // then
        assertThat(negotiationInfo.getFrontModelDTO().getIdx()).isEqualTo(frontModelEntity.getIdx());
        assertThat(negotiationInfo.getModelNegotiationDesc()).isEqualTo("섭외 수정 테스트");

        // verify
        verify(mockFrontNegotiationJpaApiService, times(1)).findOneNegotiation(frontNegotiationEntity.getIdx());
        verify(mockFrontNegotiationJpaApiService, atLeastOnce()).findOneNegotiation(frontNegotiationEntity.getIdx());
        verifyNoMoreInteractions(mockFrontNegotiationJpaApiService);

        InOrder inOrder = inOrder(mockFrontNegotiationJpaApiService);
        inOrder.verify(mockFrontNegotiationJpaApiService).findOneNegotiation(frontNegotiationEntity.getIdx());
    }

    @Test
    @DisplayName("모델섭외수정BDD테스트")
    void 모델섭외수정BDD테스트() {
        // given
        Long idx = frontNegotiationJpaApiService.insertModelNegotiation(frontModelDTO.getIdx(), frontNegotiationEntity).getIdx();

        frontNegotiationEntity = FrontNegotiationEntity.builder()
                .idx(idx)
                .frontModelEntity(frontModelEntity)
                .modelKorName(frontModelEntity.getModelKorName())
                .modelNegotiationDesc("섭외 수정 테스트")
                .modelNegotiationDate(now())
                .name("조찬희")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        FrontNegotiationDTO frontNegotiationDTO = FrontNegotiationEntity.toDto(frontNegotiationEntity);

        frontNegotiationJpaApiService.updateModelNegotiation(frontNegotiationDTO.getIdx(), frontNegotiationEntity);

        // when
        given(mockFrontNegotiationJpaApiService.findOneNegotiation(frontNegotiationEntity.getIdx())).willReturn(frontNegotiationDTO);
        FrontNegotiationDTO negotiationInfo = mockFrontNegotiationJpaApiService.findOneNegotiation(frontNegotiationEntity.getIdx());

        // then
        assertThat(negotiationInfo.getFrontModelDTO().getIdx()).isEqualTo(frontModelEntity.getIdx());
        assertThat(negotiationInfo.getModelNegotiationDesc()).isEqualTo("섭외 수정 테스트");

        // verify
        then(mockFrontNegotiationJpaApiService).should(times(1)).findOneNegotiation(frontNegotiationEntity.getIdx());
        then(mockFrontNegotiationJpaApiService).should(atLeastOnce()).findOneNegotiation(frontNegotiationEntity.getIdx());
        then(mockFrontNegotiationJpaApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델섭외삭제Mockito테스트")
    void 모델섭외삭제Mockito테스트() {
        // given
        em.persist(frontNegotiationEntity);
        frontNegotiationDTO = FrontNegotiationEntity.toDto(frontNegotiationEntity);

        // when
        when(mockFrontNegotiationJpaApiService.findOneNegotiation(frontNegotiationEntity.getIdx())).thenReturn(frontNegotiationDTO);
        Long deleteIdx = frontNegotiationJpaApiService.deleteModelNegotiation(frontNegotiationEntity.getIdx());

        // then
        assertThat(mockFrontNegotiationJpaApiService.findOneNegotiation(frontNegotiationEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockFrontNegotiationJpaApiService, times(1)).findOneNegotiation(frontNegotiationEntity.getIdx());
        verify(mockFrontNegotiationJpaApiService, atLeastOnce()).findOneNegotiation(frontNegotiationEntity.getIdx());
        verifyNoMoreInteractions(mockFrontNegotiationJpaApiService);

        InOrder inOrder = inOrder(mockFrontNegotiationJpaApiService);
        inOrder.verify(mockFrontNegotiationJpaApiService).findOneNegotiation(frontNegotiationEntity.getIdx());
    }

    @Test
    @DisplayName("모델섭외삭제BDD테스트")
    void 모델섭외삭제BDD테스트() {
        // given
        em.persist(frontNegotiationEntity);
        frontNegotiationDTO = FrontNegotiationEntity.toDto(frontNegotiationEntity);

        // when
        given(mockFrontNegotiationJpaApiService.findOneNegotiation(frontNegotiationEntity.getIdx())).willReturn(frontNegotiationDTO);
        Long deleteIdx = frontNegotiationJpaApiService.deleteModelNegotiation(frontNegotiationEntity.getIdx());

        // then
        assertThat(mockFrontNegotiationJpaApiService.findOneNegotiation(frontNegotiationEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockFrontNegotiationJpaApiService).should(times(1)).findOneNegotiation(frontNegotiationEntity.getIdx());
        then(mockFrontNegotiationJpaApiService).should(atLeastOnce()).findOneNegotiation(frontNegotiationEntity.getIdx());
        then(mockFrontNegotiationJpaApiService).shouldHaveNoMoreInteractions();
    }

}

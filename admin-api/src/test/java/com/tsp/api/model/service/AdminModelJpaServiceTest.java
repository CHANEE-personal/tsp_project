package com.tsp.api.model.service;

import com.tsp.api.comment.service.AdminCommentJpaService;
import com.tsp.api.comment.domain.AdminCommentDTO;
import com.tsp.api.comment.domain.AdminCommentEntity;
import com.tsp.api.common.domain.CommonImageDTO;
import com.tsp.api.model.domain.AdminModelDTO;
import com.tsp.api.model.domain.AdminModelEntity;
import com.tsp.api.model.domain.agency.AdminAgencyDTO;
import com.tsp.api.model.domain.recommend.AdminRecommendDTO;
import com.tsp.api.model.domain.recommend.AdminRecommendEntity;
import com.tsp.api.model.domain.schedule.AdminScheduleDTO;
import com.tsp.api.model.service.agency.AdminAgencyJpaService;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("모델 Service Test")
class AdminModelJpaServiceTest extends AdminModelCommonServiceTest {
    @Mock private AdminModelJpaService mockAdminModelJpaService;
    @Mock private AdminCommentJpaService mockAdminCommentJpaService;
    private final AdminModelJpaService adminModelJpaService;
    private final AdminAgencyJpaService adminAgencyJpaService;

    private final EntityManager em;

    @Test
    @DisplayName("모델 리스트 조회 예외 테스트")
    void 모델리스트조회예외테스트() {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", -1);
        PageRequest pageRequest = PageRequest.of(1, 3);

        // then
        assertThatThrownBy(() -> adminModelJpaService.findModelList(modelMap, pageRequest))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("모델 리스트 조회 테스트")
    void 모델리스트조회테스트() {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", 1);
        PageRequest pageRequest = PageRequest.of(1, 100);

        // then
        assertThat(adminModelJpaService.findModelList(modelMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("모델 리스트 조회 Mockito 테스트")
    void 모델리스트조회Mockito테스트() {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", "1");
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminModelDTO> returnModelList = new ArrayList<>();
        // 남성
        returnModelList.add(AdminModelDTO.builder().idx(1L).categoryCd(1).modelKorName("남성모델").modelEngName("menModel")
                .modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());
        // 여성
        returnModelList.add(AdminModelDTO.builder().idx(2L).categoryCd(2).modelKorName("여성모델").modelEngName("womenModel")
                .modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());
        // 시니어
        returnModelList.add(AdminModelDTO.builder().idx(3L).categoryCd(3).modelKorName("시니어모델").modelEngName("seniorModel")
                .modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());

        Page<AdminModelDTO> resultModel = new PageImpl<>(returnModelList, pageRequest, returnModelList.size());

        // when
        when(mockAdminModelJpaService.findModelList(modelMap, pageRequest)).thenReturn(resultModel);
        Page<AdminModelDTO> modelList = mockAdminModelJpaService.findModelList(modelMap, pageRequest);
        List<AdminModelDTO> findModelList = modelList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findModelList).isNotEmpty(),
                () -> assertThat(findModelList).hasSize(3)
        );

        assertThat(findModelList.get(0).getIdx()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(findModelList.get(0).getCategoryCd()).isEqualTo(returnModelList.get(0).getCategoryCd());
        assertThat(findModelList.get(0).getModelKorName()).isEqualTo(returnModelList.get(0).getModelKorName());
        assertThat(findModelList.get(0).getModelEngName()).isEqualTo(returnModelList.get(0).getModelEngName());
        assertThat(findModelList.get(0).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyName());
        assertThat(findModelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyDescription());

        assertThat(findModelList.get(1).getIdx()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(findModelList.get(1).getCategoryCd()).isEqualTo(returnModelList.get(1).getCategoryCd());
        assertThat(findModelList.get(1).getModelKorName()).isEqualTo(returnModelList.get(1).getModelKorName());
        assertThat(findModelList.get(1).getModelEngName()).isEqualTo(returnModelList.get(1).getModelEngName());
        assertThat(findModelList.get(1).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyName());
        assertThat(findModelList.get(1).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyDescription());

        assertThat(findModelList.get(2).getIdx()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(findModelList.get(2).getCategoryCd()).isEqualTo(returnModelList.get(2).getCategoryCd());
        assertThat(findModelList.get(2).getModelKorName()).isEqualTo(returnModelList.get(2).getModelKorName());
        assertThat(findModelList.get(2).getModelEngName()).isEqualTo(returnModelList.get(2).getModelEngName());
        assertThat(findModelList.get(2).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyName());
        assertThat(findModelList.get(2).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyDescription());

        // verify
        verify(mockAdminModelJpaService, times(1)).findModelList(modelMap, pageRequest);
        verify(mockAdminModelJpaService, atLeastOnce()).findModelList(modelMap, pageRequest);
        verifyNoMoreInteractions(mockAdminModelJpaService);

        InOrder inOrder = inOrder(mockAdminModelJpaService);
        inOrder.verify(mockAdminModelJpaService).findModelList(modelMap, pageRequest);
    }

    @Test
    @DisplayName("모델 리스트 조회 BDD 테스트")
    void 모델리스트조회BDD테스트() {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", "1");
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminModelDTO> returnModelList = new ArrayList<>();
        // 남성
        returnModelList.add(AdminModelDTO.builder().idx(1L).categoryCd(1).modelKorName("남성모델").modelEngName("menModel")
                .modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());
        // 여성
        returnModelList.add(AdminModelDTO.builder().idx(2L).categoryCd(2).modelKorName("여성모델").modelEngName("womenModel")
                .modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());
        // 시니어
        returnModelList.add(AdminModelDTO.builder().idx(3L).categoryCd(3).modelKorName("시니어모델").modelEngName("seniorModel")
                .modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());

        Page<AdminModelDTO> resultModel = new PageImpl<>(returnModelList, pageRequest, returnModelList.size());

        // when
        given(mockAdminModelJpaService.findModelList(modelMap, pageRequest)).willReturn(resultModel);
        Page<AdminModelDTO> modelList = mockAdminModelJpaService.findModelList(modelMap, pageRequest);
        List<AdminModelDTO> findModelList = modelList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findModelList).isNotEmpty(),
                () -> assertThat(findModelList).hasSize(3)
        );

        assertThat(findModelList.get(0).getIdx()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(findModelList.get(0).getCategoryCd()).isEqualTo(returnModelList.get(0).getCategoryCd());
        assertThat(findModelList.get(0).getModelKorName()).isEqualTo(returnModelList.get(0).getModelKorName());
        assertThat(findModelList.get(0).getModelEngName()).isEqualTo(returnModelList.get(0).getModelEngName());
        assertThat(findModelList.get(0).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyName());
        assertThat(findModelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyDescription());

        assertThat(findModelList.get(1).getIdx()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(findModelList.get(1).getCategoryCd()).isEqualTo(returnModelList.get(1).getCategoryCd());
        assertThat(findModelList.get(1).getModelKorName()).isEqualTo(returnModelList.get(1).getModelKorName());
        assertThat(findModelList.get(1).getModelEngName()).isEqualTo(returnModelList.get(1).getModelEngName());
        assertThat(findModelList.get(1).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyName());
        assertThat(findModelList.get(1).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyDescription());

        assertThat(findModelList.get(2).getIdx()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(findModelList.get(2).getCategoryCd()).isEqualTo(returnModelList.get(2).getCategoryCd());
        assertThat(findModelList.get(2).getModelKorName()).isEqualTo(returnModelList.get(2).getModelKorName());
        assertThat(findModelList.get(2).getModelEngName()).isEqualTo(returnModelList.get(2).getModelEngName());
        assertThat(findModelList.get(2).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyName());
        assertThat(findModelList.get(2).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyDescription());

        // verify
        then(mockAdminModelJpaService).should(times(1)).findModelList(modelMap, pageRequest);
        then(mockAdminModelJpaService).should(atLeastOnce()).findModelList(modelMap, pageRequest);
        then(mockAdminModelJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 상세 조회 예외 테스트")
    void 모델상세조회예외테스트() {
        // given
        adminModelEntity = AdminModelEntity.builder().categoryCd(-1).build();

        // then
        assertThatThrownBy(() -> adminModelJpaService.findOneModel(adminModelDTO.getIdx()))
                .isInstanceOf(TspException.class);
    }

    @Test
    @DisplayName("모델 상세 조회 테스트")
    void 모델상세조회테스트() {
        AdminModelDTO modelInfo = adminModelJpaService.findOneModel(143L);
        // then
        assertThat(modelInfo).isNotNull();
        assertThat(modelInfo.getIdx()).isEqualTo(143);
        assertThat(modelInfo.getCategoryCd()).isEqualTo(2);
        assertThat(modelInfo.getModelKorFirstName()).isEqualTo("김");
        assertThat(modelInfo.getModelKorSecondName()).isEqualTo("예영");
    }

    @Test
    @DisplayName("모델 상세 조회 Mockito 테스트")
    void 모델상세조회Mockito테스트() {
        // when
        when(mockAdminModelJpaService.findOneModel(adminModelDTO.getIdx())).thenReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findOneModel(adminModelDTO.getIdx());

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(adminModelDTO.getIdx());
        assertThat(modelInfo.getCategoryCd()).isEqualTo(adminModelDTO.getCategoryCd());
        assertThat(modelInfo.getModelKorName()).isEqualTo(adminModelDTO.getModelKorName());
        assertThat(modelInfo.getModelEngName()).isEqualTo(adminModelDTO.getModelEngName());

        // verify
        verify(mockAdminModelJpaService, times(1)).findOneModel(adminModelDTO.getIdx());
        verify(mockAdminModelJpaService, atLeastOnce()).findOneModel(adminModelDTO.getIdx());
        verifyNoMoreInteractions(mockAdminModelJpaService);

        InOrder inOrder = inOrder(mockAdminModelJpaService);
        inOrder.verify(mockAdminModelJpaService).findOneModel(adminModelDTO.getIdx());
    }

    @Test
    @DisplayName("모델 상세 조회 BDD 테스트")
    void 모델상세조회BDD테스트() {
        // when
        given(mockAdminModelJpaService.findOneModel(adminModelDTO.getIdx())).willReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findOneModel(adminModelDTO.getIdx());

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(adminModelDTO.getIdx());
        assertThat(modelInfo.getCategoryCd()).isEqualTo(adminModelDTO.getCategoryCd());
        assertThat(modelInfo.getModelKorName()).isEqualTo(adminModelDTO.getModelKorName());
        assertThat(modelInfo.getModelEngName()).isEqualTo(adminModelDTO.getModelEngName());

        // verify
        then(mockAdminModelJpaService).should(times(1)).findOneModel(adminModelDTO.getIdx());
        then(mockAdminModelJpaService).should(atLeastOnce()).findOneModel(adminModelDTO.getIdx());
        then(mockAdminModelJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 모델 상세 조회 테스트")
    void 이전or다음모델상세조회테스트() {
        // given
        adminModelEntity = AdminModelEntity.builder().idx(145L).categoryCd(2).build();

        // when
        adminModelDTO = adminModelJpaService.findOneModel(adminModelDTO.getIdx());

        // 이전 모델
        assertThat(adminModelJpaService.findPrevOneModel(adminModelEntity).getIdx()).isEqualTo(144);
        // 다음 모델
        assertThat(adminModelJpaService.findNextOneModel(adminModelEntity).getIdx()).isEqualTo(147);
    }

    @Test
    @DisplayName("이전 모델 상세 조회 Mockito 테스트")
    void 이전모델상세조회Mockito테스트() {
        // given
        adminModelEntity = AdminModelEntity.builder().idx(145L).categoryCd(2).build();
        // when
        adminModelDTO = adminModelJpaService.findPrevOneModel(adminModelEntity);

        when(mockAdminModelJpaService.findPrevOneModel(adminModelEntity)).thenReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findPrevOneModel(adminModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(144);

        // verify
        verify(mockAdminModelJpaService, times(1)).findPrevOneModel(adminModelEntity);
        verify(mockAdminModelJpaService, atLeastOnce()).findPrevOneModel(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaService);

        InOrder inOrder = inOrder(mockAdminModelJpaService);
        inOrder.verify(mockAdminModelJpaService).findPrevOneModel(adminModelEntity);
    }

    @Test
    @DisplayName("이전 모델 상세 조회 BDD 테스트")
    void 이전모델상세조회BDD테스트() {
        // given
        adminModelEntity = AdminModelEntity.builder().idx(145L).categoryCd(2).build();
        // when
        adminModelDTO = adminModelJpaService.findPrevOneModel(adminModelEntity);

        given(mockAdminModelJpaService.findPrevOneModel(adminModelEntity)).willReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findPrevOneModel(adminModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(144);

        // verify
        then(mockAdminModelJpaService).should(times(1)).findPrevOneModel(adminModelEntity);
        then(mockAdminModelJpaService).should(atLeastOnce()).findPrevOneModel(adminModelEntity);
        then(mockAdminModelJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 모델 상세 조회 Mockito 테스트")
    void 다음모델상세조회Mockito테스트() {
        // given
        adminModelEntity = AdminModelEntity.builder().idx(145L).categoryCd(2).build();
        // when
        adminModelDTO = adminModelJpaService.findNextOneModel(adminModelEntity);

        when(mockAdminModelJpaService.findNextOneModel(adminModelEntity)).thenReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findNextOneModel(adminModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(147);

        // verify
        verify(mockAdminModelJpaService, times(1)).findNextOneModel(adminModelEntity);
        verify(mockAdminModelJpaService, atLeastOnce()).findNextOneModel(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaService);

        InOrder inOrder = inOrder(mockAdminModelJpaService);
        inOrder.verify(mockAdminModelJpaService).findNextOneModel(adminModelEntity);
    }

    @Test
    @DisplayName("다음 모델 상세 조회 BDD 테스트")
    void 다음모델상세조회BDD테스트() {
        // given
        adminModelEntity = AdminModelEntity.builder().idx(145L).categoryCd(2).build();
        // when
        adminModelDTO = adminModelJpaService.findNextOneModel(adminModelEntity);

        given(mockAdminModelJpaService.findNextOneModel(adminModelEntity)).willReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findNextOneModel(adminModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(147);

        // verify
        then(mockAdminModelJpaService).should(times(1)).findNextOneModel(adminModelEntity);
        then(mockAdminModelJpaService).should(atLeastOnce()).findNextOneModel(adminModelEntity);
        then(mockAdminModelJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 등록 Mockito 테스트")
    void 모델등록Mockito테스트() {
        // when
        when(mockAdminModelJpaService.findOneModel(adminModelDTO.getIdx())).thenReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findOneModel(adminModelDTO.getIdx());

        // then
        assertThat(modelInfo.getCategoryCd()).isEqualTo(adminModelDTO.getCategoryCd());
        assertThat(modelInfo.getModelKorFirstName()).isEqualTo(adminModelDTO.getModelKorFirstName());
        assertThat(modelInfo.getModelKorSecondName()).isEqualTo(adminModelDTO.getModelKorSecondName());

        // verify
        verify(mockAdminModelJpaService, times(1)).findOneModel(adminModelDTO.getIdx());
        verify(mockAdminModelJpaService, atLeastOnce()).findOneModel(adminModelDTO.getIdx());
        verifyNoMoreInteractions(mockAdminModelJpaService);

        InOrder inOrder = inOrder(mockAdminModelJpaService);
        inOrder.verify(mockAdminModelJpaService).findOneModel(adminModelDTO.getIdx());
    }

    @Test
    @DisplayName("모델 등록 BDD 테스트")
    void 모델등록BDD테스트() {
        // when
        given(mockAdminModelJpaService.findOneModel(adminModelDTO.getIdx())).willReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findOneModel(adminModelDTO.getIdx());

        // then
        assertThat(modelInfo.getCategoryCd()).isEqualTo(adminModelDTO.getCategoryCd());
        assertThat(modelInfo.getModelKorFirstName()).isEqualTo(adminModelDTO.getModelKorFirstName());
        assertThat(modelInfo.getModelKorSecondName()).isEqualTo(adminModelDTO.getModelKorSecondName());

        // verify
        then(mockAdminModelJpaService).should(times(1)).findOneModel(adminModelDTO.getIdx());
        then(mockAdminModelJpaService).should(atLeastOnce()).findOneModel(adminModelDTO.getIdx());
        then(mockAdminModelJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 등록 예외 테스트")
    void 모델등록예외테스트() {
        // given
        adminModelEntity = AdminModelEntity.builder()
                .categoryCd(-1)
                .categoryAge(2)
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
                .visible("Y")
                .build();

        // then
        assertThatThrownBy(() -> adminModelJpaService.insertModel(adminModelEntity))
                .isInstanceOf(TspException.class);
    }

    @Test
    @DisplayName("모델 수정 Mockito 테스트")
    void 모델수정Mockito테스트() {
        // given
        adminModelEntity = AdminModelEntity.builder()
                .idx(adminModelDTO.getIdx())
                .categoryCd(2)
                .categoryAge(3)
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
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        adminModelJpaService.updateModel(adminModelDTO.getIdx(), adminModelEntity);

        em.flush();
        em.clear();

        adminModelDTO = AdminModelEntity.toDto(adminModelEntity);

        // when
        when(mockAdminModelJpaService.findOneModel(adminModelDTO.getIdx())).thenReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findOneModel(adminModelDTO.getIdx());

        // then
        assertThat(modelInfo.getModelKorFirstName()).isEqualTo("조");
        assertThat(modelInfo.getModelKorSecondName()).isEqualTo("찬희");

        // verify
        verify(mockAdminModelJpaService, times(1)).findOneModel(adminModelDTO.getIdx());
        verify(mockAdminModelJpaService, atLeastOnce()).findOneModel(adminModelDTO.getIdx());
        verifyNoMoreInteractions(mockAdminModelJpaService);

        InOrder inOrder = inOrder(mockAdminModelJpaService);
        inOrder.verify(mockAdminModelJpaService).findOneModel(adminModelDTO.getIdx());
    }

    @Test
    @DisplayName("모델 수정 BDD 테스트")
    void 모델수정BDD테스트() {
        // given
        adminModelEntity = AdminModelEntity.builder()
                .idx(adminModelDTO.getIdx())
                .categoryCd(2)
                .categoryAge(3)
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
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        adminModelJpaService.updateModel(adminModelDTO.getIdx(), adminModelEntity);

        adminModelDTO = AdminModelEntity.toDto(adminModelEntity);

        // when
        given(mockAdminModelJpaService.findOneModel(adminModelDTO.getIdx())).willReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findOneModel(adminModelDTO.getIdx());

        // then
        assertThat(modelInfo.getModelKorFirstName()).isEqualTo("조");
        assertThat(modelInfo.getModelKorSecondName()).isEqualTo("찬희");

        // verify
        then(mockAdminModelJpaService).should(times(1)).findOneModel(adminModelDTO.getIdx());
        then(mockAdminModelJpaService).should(atLeastOnce()).findOneModel(adminModelDTO.getIdx());
        then(mockAdminModelJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 삭제 테스트")
    void 모델삭제테스트() {
        // then
        adminModelJpaService.deleteModel(adminModelDTO.getIdx());
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("모델 소속사 수정 Mockito 테스트")
    void 모델소속사수정Mockito테스트() {
        System.out.println("===agency===");
        System.out.println(adminAgencyDTO.getIdx());
        AdminModelDTO updateModel = adminModelJpaService.updateModelAgency(adminAgencyDTO.getIdx(), adminModelEntity);

        em.flush();
        em.clear();

        // when
        when(mockAdminModelJpaService.findOneModel(updateModel.getIdx())).thenReturn(updateModel);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findOneModel(updateModel.getIdx());

        // then
        assertThat(modelInfo.getModelAgency().getIdx()).isEqualTo(updateModel.getModelAgency().getIdx());

        // verify
        verify(mockAdminModelJpaService, times(1)).findOneModel(updateModel.getIdx());
        verify(mockAdminModelJpaService, atLeastOnce()).findOneModel(updateModel.getIdx());
        verifyNoMoreInteractions(mockAdminModelJpaService);

        InOrder inOrder = inOrder(mockAdminModelJpaService);
        inOrder.verify(mockAdminModelJpaService).findOneModel(updateModel.getIdx());
    }

    @Test
    @DisplayName("모델 소속사 수정 BDD 테스트")
    void 모델소속사수정BDD테스트() {
        // 소속사 등록
        AdminAgencyDTO newAgencyDTO = adminAgencyJpaService.insertAgency(adminAgencyEntity);
        Long agencyIdx = newAgencyDTO.getIdx();

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
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        // 모델 소속사 수정
        adminModelJpaService.insertModel(adminModelEntity).getIdx();

        AdminModelDTO updateModel = adminModelJpaService.updateModelAgency(agencyIdx, adminModelEntity);

        em.flush();
        em.clear();

        // when
        given(mockAdminModelJpaService.findOneModel(updateModel.getIdx())).willReturn(updateModel);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findOneModel(updateModel.getIdx());

        // then
        assertThat(modelInfo.getModelAgency().getIdx()).isEqualTo(updateModel.getModelAgency().getIdx());

        // verify
        then(mockAdminModelJpaService).should(times(1)).findOneModel(updateModel.getIdx());
        then(mockAdminModelJpaService).should(atLeastOnce()).findOneModel(updateModel.getIdx());
        then(mockAdminModelJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("어드민코멘트등록Mockito테스트")
    void 어드민코멘트등록Mockito테스트() {
        // when
        when(mockAdminCommentJpaService.findOneAdminComment(adminCommentDTO.getIdx())).thenReturn(adminCommentDTO);
        AdminCommentDTO commentInfo = mockAdminCommentJpaService.findOneAdminComment(adminCommentDTO.getIdx());

        // then
        assertThat(commentInfo.getComment()).isEqualTo("comment");
        assertThat(commentInfo.getCommentType()).isEqualTo("model");
        assertThat(commentInfo.getAdminModelDTO().getIdx()).isEqualTo(adminCommentDTO.getAdminModelDTO().getIdx());

        // verify
        then(mockAdminCommentJpaService).should(times(1)).findOneAdminComment(adminCommentDTO.getIdx());
        then(mockAdminCommentJpaService).should(atLeastOnce()).findOneAdminComment(adminCommentDTO.getIdx());
        then(mockAdminCommentJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("어드민코멘트등록BDD테스트")
    void 어드민코멘트등록BDD테스트() {
        // when
        given(mockAdminCommentJpaService.findOneAdminComment(adminCommentDTO.getIdx())).willReturn(adminCommentDTO);
        AdminCommentDTO commentInfo = mockAdminCommentJpaService.findOneAdminComment(adminCommentDTO.getIdx());

        // then
        assertThat(commentInfo.getComment()).isEqualTo("comment");
        assertThat(commentInfo.getCommentType()).isEqualTo("model");
        assertThat(commentInfo.getAdminModelDTO().getIdx()).isEqualTo(adminCommentDTO.getAdminModelDTO().getIdx());

        // verify
        then(mockAdminCommentJpaService).should(times(1)).findOneAdminComment(adminCommentDTO.getIdx());
        then(mockAdminCommentJpaService).should(atLeastOnce()).findOneAdminComment(adminCommentDTO.getIdx());
        then(mockAdminCommentJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 어드민 코멘트 조회 Mockito 테스트")
    void 모델어드민코멘트조회Mockito테스트() {
        List<AdminCommentDTO> adminCommentList = new ArrayList<>();
        adminCommentList.add(adminCommentDTO);

        when(mockAdminModelJpaService.findModelAdminComment(adminModelDTO.getIdx())).thenReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminModelJpaService.findModelAdminComment(adminModelDTO.getIdx());

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("model");
        assertThat(newAdminCommentList.get(0).getAdminModelDTO().getIdx()).isEqualTo(adminCommentDTO.getAdminModelDTO().getIdx());

        // verify
        verify(mockAdminModelJpaService, times(1)).findModelAdminComment(adminModelDTO.getIdx());
        verify(mockAdminModelJpaService, atLeastOnce()).findModelAdminComment(adminModelDTO.getIdx());
        verifyNoMoreInteractions(mockAdminModelJpaService);

        InOrder inOrder = inOrder(mockAdminModelJpaService);
        inOrder.verify(mockAdminModelJpaService).findModelAdminComment(adminModelDTO.getIdx());
    }

    @Test
    @DisplayName("모델 어드민 코멘트 조회 BDD 테스트")
    void 모델어드민코멘트조회BDD테스트() {
        List<AdminCommentDTO> adminCommentList = new ArrayList<>();
        adminCommentList.add(adminCommentDTO);

        given(mockAdminModelJpaService.findModelAdminComment(adminModelDTO.getIdx())).willReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminModelJpaService.findModelAdminComment(adminModelDTO.getIdx());

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("model");
        assertThat(newAdminCommentList.get(0).getAdminModelDTO().getIdx()).isEqualTo(adminCommentDTO.getAdminModelDTO().getIdx());

        // verify
        then(mockAdminModelJpaService).should(times(1)).findModelAdminComment(adminModelDTO.getIdx());
        then(mockAdminModelJpaService).should(atLeastOnce()).findModelAdminComment(adminModelDTO.getIdx());
        then(mockAdminModelJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("새로운 모델 설정 Mockito 테스트")
    void 새로운모델설정Mockito테스트() {
        AdminModelDTO newModel = adminModelJpaService.toggleModelNewYn(adminModelDTO.getIdx());

        em.flush();
        em.clear();

        // when
        when(mockAdminModelJpaService.findOneModel(newModel.getIdx())).thenReturn(newModel);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findOneModel(newModel.getIdx());

        assertThat(modelInfo.getNewYn()).isEqualTo("Y");

        // verify
        verify(mockAdminModelJpaService, times(1)).findOneModel(newModel.getIdx());
        verify(mockAdminModelJpaService, atLeastOnce()).findOneModel(newModel.getIdx());
        verifyNoMoreInteractions(mockAdminModelJpaService);

        InOrder inOrder = inOrder(mockAdminModelJpaService);
        inOrder.verify(mockAdminModelJpaService).findOneModel(newModel.getIdx());
    }

    @Test
    @DisplayName("새로운 모델 설정 BDD 테스트")
    void 새로운모델설정BDD테스트() {
        AdminModelDTO newModel = adminModelJpaService.toggleModelNewYn(adminModelDTO.getIdx());

        em.flush();
        em.clear();

        // when
        given(mockAdminModelJpaService.findOneModel(newModel.getIdx())).willReturn(newModel);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findOneModel(newModel.getIdx());

        assertThat(modelInfo.getNewYn()).isEqualTo("Y");

        // verify
        then(mockAdminModelJpaService).should(times(1)).findOneModel(newModel.getIdx());
        then(mockAdminModelJpaService).should(atLeastOnce()).findOneModel(newModel.getIdx());
        then(mockAdminModelJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 스케줄 Mockito 조회 테스트")
    void 모델스케줄Mockito조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        scheduleMap.put("jpaStartPage", 1);
        scheduleMap.put("size", 3);

        List<AdminScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(AdminScheduleDTO.builder().idx(1L).modelIdx(adminModelDTO.getIdx())
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());

        // when
        when(mockAdminModelJpaService.findOneModelSchedule(adminModelDTO.getIdx())).thenReturn(scheduleList);
        List<AdminScheduleDTO> newScheduleList = mockAdminModelJpaService.findOneModelSchedule(adminModelDTO.getIdx());

        // then
        assertThat(newScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(newScheduleList.get(0).getModelIdx()).isEqualTo(scheduleList.get(0).getModelIdx());
        assertThat(newScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());
        assertThat(newScheduleList.get(0).getModelScheduleTime()).isEqualTo(scheduleList.get(0).getModelScheduleTime());

        // verify
        verify(mockAdminModelJpaService, times(1)).findOneModelSchedule(adminModelDTO.getIdx());
        verify(mockAdminModelJpaService, atLeastOnce()).findOneModelSchedule(adminModelDTO.getIdx());
        verifyNoMoreInteractions(mockAdminModelJpaService);

        InOrder inOrder = inOrder(mockAdminModelJpaService);
        inOrder.verify(mockAdminModelJpaService).findOneModelSchedule(adminModelDTO.getIdx());
    }

    @Test
    @DisplayName("모델 스케줄 BDD 조회 테스트")
    void 모델스케줄BDD조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        scheduleMap.put("jpaStartPage", 1);
        scheduleMap.put("size", 3);

        List<AdminScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(AdminScheduleDTO.builder().idx(1L).modelIdx(adminModelDTO.getIdx())
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());

        // when
        given(mockAdminModelJpaService.findOneModelSchedule(adminModelDTO.getIdx())).willReturn(scheduleList);
        List<AdminScheduleDTO> newScheduleList = mockAdminModelJpaService.findOneModelSchedule(adminModelDTO.getIdx());

        // then
        assertThat(newScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(newScheduleList.get(0).getModelIdx()).isEqualTo(scheduleList.get(0).getModelIdx());
        assertThat(newScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());
        assertThat(newScheduleList.get(0).getModelScheduleTime()).isEqualTo(scheduleList.get(0).getModelScheduleTime());

        // verify
        then(mockAdminModelJpaService).should(times(1)).findOneModelSchedule(adminModelDTO.getIdx());
        then(mockAdminModelJpaService).should(atLeastOnce()).findOneModelSchedule(adminModelDTO.getIdx());
        then(mockAdminModelJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("추천 검색어 리스트 조회 테스트")
    void 추천검색어리스트조회테스트() {
        Map<String, Object> recommendMap = new HashMap<>();
        recommendMap.put("jpaStartPage", 0);
        recommendMap.put("size", 3);

        List<String> list = new ArrayList<>();
        list.add("모델1");
        list.add("모델2");

        AdminRecommendEntity adminRecommendEntity = AdminRecommendEntity.builder()
                .recommendKeyword(list)
                .build();

        adminModelJpaService.insertRecommend(adminRecommendEntity);

        assertThat(adminModelJpaService.findRecommendList(recommendMap)).isNotEmpty();
    }

    @Test
    @DisplayName("추천 검색어 상세 조회 테스트")
    void 추천검색어상세조회테스트() {
        List<String> list = new ArrayList<>();
        list.add("모델1");
        list.add("모델2");

        AdminRecommendEntity adminRecommendEntity = AdminRecommendEntity.builder()
                .recommendKeyword(list)
                .build();

        AdminRecommendDTO adminRecommendDTO = adminModelJpaService.insertRecommend(adminRecommendEntity);

        assertThat(adminModelJpaService.findOneRecommend(adminRecommendDTO.getIdx()).getRecommendKeyword()).isEqualTo(list);
    }


    @Test
    @DisplayName("추천 검색어 등록 테스트")
    void 추천검색어등록테스트() {
        List<String> list = new ArrayList<>();
        list.add("모델1");
        list.add("모델2");

        AdminRecommendEntity recommendEntity = AdminRecommendEntity.builder()
                .recommendKeyword(list)
                .build();

        AdminRecommendDTO adminRecommendDTO = adminModelJpaService.insertRecommend(recommendEntity);

        assertThat(adminRecommendDTO.getRecommendKeyword()).isEqualTo(list);
    }

    @Test
    @DisplayName("추천 검색어 수정 테스트")
    void 추천검색어수정테스트() {
        List<String> list = new ArrayList<>();
        list.add("모델1");
        list.add("모델2");

        AdminRecommendEntity recommendEntity = AdminRecommendEntity.builder()
                .recommendKeyword(list)
                .build();

        AdminRecommendDTO adminRecommendDTO = adminModelJpaService.insertRecommend(recommendEntity);

        list.add("모델3");
        recommendEntity = AdminRecommendEntity.builder()
                .idx(adminRecommendDTO.getIdx())
                .recommendKeyword(list)
                .build();

        em.flush();
        em.clear();

        AdminRecommendDTO updateRecommendDTO = adminModelJpaService.updateRecommend(recommendEntity);

        assertThat(updateRecommendDTO.getRecommendKeyword()).isEqualTo(list);
    }

    @Test
    @DisplayName("추천 검색어 삭제 테스트")
    void 추천검색어삭제테스트() {
        List<String> list = new ArrayList<>();
        list.add("모델1");
        list.add("모델2");

        AdminRecommendEntity recommendEntity = AdminRecommendEntity.builder()
                .recommendKeyword(list)
                .build();

        AdminRecommendDTO adminRecommendDTO = adminModelJpaService.insertRecommend(recommendEntity);

        Long deleteIdx = adminModelJpaService.deleteRecommend(adminRecommendDTO.getIdx());
        em.flush();
        em.clear();

        assertThat(deleteIdx).isEqualTo(adminRecommendDTO.getIdx());
    }
}

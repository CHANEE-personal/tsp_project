package com.tsp.api.model.service;

import com.tsp.api.comment.service.AdminCommentJpaRepository;
import com.tsp.api.comment.domain.AdminCommentDTO;
import com.tsp.api.comment.domain.AdminCommentEntity;
import com.tsp.api.common.domain.CommonImageDTO;
import com.tsp.api.model.domain.AdminModelDTO;
import com.tsp.api.model.domain.AdminModelEntity;
import com.tsp.api.model.domain.recommend.AdminRecommendDTO;
import com.tsp.api.model.domain.recommend.AdminRecommendEntity;
import com.tsp.api.model.domain.schedule.AdminScheduleDTO;
import com.tsp.api.model.domain.schedule.AdminScheduleEntity;
import com.tsp.api.model.service.agency.AdminAgencyJpaRepository;
import com.tsp.api.model.service.schedule.AdminScheduleJpaRepository;
import com.tsp.exception.TspException;
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
    @Mock private AdminModelJpaRepository adminModelJpaRepository;
    @Mock private AdminModelJpaQueryRepository adminModelJpaQueryRepository;
    @InjectMocks private AdminModelJpaServiceImpl mockAdminModelJpaService;

    @Mock private AdminAgencyJpaRepository adminAgencyJpaRepository;
    @Mock private AdminScheduleJpaRepository adminScheduleJpaRepository;
    @Mock private AdminCommentJpaRepository adminCommentJpaRepository;
    private final AdminModelJpaService adminModelJpaService;

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
        when(adminModelJpaQueryRepository.findModelList(modelMap, pageRequest)).thenReturn(resultModel);
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
        verify(adminModelJpaQueryRepository, times(1)).findModelList(modelMap, pageRequest);
        verify(adminModelJpaQueryRepository, atLeastOnce()).findModelList(modelMap, pageRequest);
        verifyNoMoreInteractions(adminModelJpaQueryRepository);

        InOrder inOrder = inOrder(adminModelJpaQueryRepository);
        inOrder.verify(adminModelJpaQueryRepository).findModelList(modelMap, pageRequest);
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
        given(adminModelJpaQueryRepository.findModelList(modelMap, pageRequest)).willReturn(resultModel);
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
        then(adminModelJpaQueryRepository).should(times(1)).findModelList(modelMap, pageRequest);
        then(adminModelJpaQueryRepository).should(atLeastOnce()).findModelList(modelMap, pageRequest);
        then(adminModelJpaQueryRepository).shouldHaveNoMoreInteractions();
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
        AdminModelDTO modelInfo = adminModelJpaService.findOneModel(adminModelEntity.getIdx());
        // then
        assertThat(modelInfo).isNotNull();
        assertThat(modelInfo.getIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(modelInfo.getCategoryCd()).isEqualTo(adminModelEntity.getCategoryCd());
        assertThat(modelInfo.getModelKorFirstName()).isEqualTo(adminModelEntity.getModelKorFirstName());
        assertThat(modelInfo.getModelKorSecondName()).isEqualTo(adminModelEntity.getModelKorSecondName());
    }

    @Test
    @DisplayName("모델 상세 조회 Mockito 테스트")
    void 모델상세조회Mockito테스트() {
        // when
        when(adminModelJpaRepository.findByIdx(adminModelEntity.getIdx())).thenReturn(Optional.ofNullable(adminModelEntity));
        AdminModelDTO modelInfo = mockAdminModelJpaService.findOneModel(adminModelEntity.getIdx());

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(modelInfo.getCategoryCd()).isEqualTo(adminModelEntity.getCategoryCd());
        assertThat(modelInfo.getModelKorName()).isEqualTo(adminModelEntity.getModelKorName());
        assertThat(modelInfo.getModelEngName()).isEqualTo(adminModelEntity.getModelEngName());

        // verify
        verify(adminModelJpaRepository, times(1)).findByIdx(adminModelEntity.getIdx());
        verify(adminModelJpaRepository, atLeastOnce()).findByIdx(adminModelEntity.getIdx());
        verifyNoMoreInteractions(adminModelJpaRepository);

        InOrder inOrder = inOrder(adminModelJpaRepository);
        inOrder.verify(adminModelJpaRepository).findByIdx(adminModelEntity.getIdx());
    }

    @Test
    @DisplayName("모델 상세 조회 BDD 테스트")
    void 모델상세조회BDD테스트() {
        // when
        given(adminModelJpaRepository.findByIdx(adminModelEntity.getIdx())).willReturn(Optional.ofNullable(adminModelEntity));
        AdminModelDTO modelInfo = mockAdminModelJpaService.findOneModel(adminModelEntity.getIdx());

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(modelInfo.getCategoryCd()).isEqualTo(adminModelEntity.getCategoryCd());
        assertThat(modelInfo.getModelKorName()).isEqualTo(adminModelEntity.getModelKorName());
        assertThat(modelInfo.getModelEngName()).isEqualTo(adminModelEntity.getModelEngName());

        // verify
        then(adminModelJpaRepository).should(times(1)).findByIdx(adminModelEntity.getIdx());
        then(adminModelJpaRepository).should(atLeastOnce()).findByIdx(adminModelEntity.getIdx());
        then(adminModelJpaRepository).shouldHaveNoMoreInteractions();
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
        when(adminModelJpaRepository.save(adminModelEntity)).thenReturn(adminModelEntity);
        AdminModelDTO modelInfo = mockAdminModelJpaService.insertModel(adminModelEntity);

        // then
        assertThat(modelInfo.getCategoryCd()).isEqualTo(adminModelEntity.getCategoryCd());
        assertThat(modelInfo.getModelKorFirstName()).isEqualTo(adminModelEntity.getModelKorFirstName());
        assertThat(modelInfo.getModelKorSecondName()).isEqualTo(adminModelEntity.getModelKorSecondName());

        // verify
        verify(adminModelJpaRepository, times(1)).save(adminModelEntity);
        verify(adminModelJpaRepository, atLeastOnce()).save(adminModelEntity);
        verifyNoMoreInteractions(adminModelJpaRepository);

        InOrder inOrder = inOrder(adminModelJpaRepository);
        inOrder.verify(adminModelJpaRepository).save(adminModelEntity);
    }

    @Test
    @DisplayName("모델 등록 BDD 테스트")
    void 모델등록BDD테스트() {
        // when
        given(adminModelJpaRepository.save(adminModelEntity)).willReturn(adminModelEntity);
        AdminModelDTO modelInfo = mockAdminModelJpaService.insertModel(adminModelEntity);

        // then
        assertThat(modelInfo.getCategoryCd()).isEqualTo(adminModelEntity.getCategoryCd());
        assertThat(modelInfo.getModelKorFirstName()).isEqualTo(adminModelEntity.getModelKorFirstName());
        assertThat(modelInfo.getModelKorSecondName()).isEqualTo(adminModelEntity.getModelKorSecondName());

        // verify
        then(adminModelJpaRepository).should(times(1)).save(adminModelEntity);
        then(adminModelJpaRepository).should(atLeastOnce()).save(adminModelEntity);
        then(adminModelJpaRepository).shouldHaveNoMoreInteractions();
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
        AdminModelEntity updateModelEntity = AdminModelEntity.builder()
                .idx(adminModelEntity.getIdx())
                .categoryCd(newCodeEntity.getCategoryCd())
                .categoryAge(2)
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .favoriteCount(1)
                .viewCount(1)
                .adminAgencyEntity(adminAgencyEntity)
                .commonImageEntityList(List.of(commonImageEntity))
                .modelMainYn("Y")
                .status("active")
                .newYn("N")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        // when
        when(adminModelJpaRepository.findById(updateModelEntity.getIdx())).thenReturn(Optional.of(updateModelEntity));
        when(adminModelJpaRepository.save(updateModelEntity)).thenReturn(updateModelEntity);
        AdminModelDTO modelInfo = mockAdminModelJpaService.updateModel(updateModelEntity.getIdx(), updateModelEntity);

        // then
        assertThat(modelInfo.getModelKorFirstName()).isEqualTo(updateModelEntity.getModelKorFirstName());
        assertThat(modelInfo.getModelKorSecondName()).isEqualTo(updateModelEntity.getModelKorSecondName());

        // verify
        verify(adminModelJpaRepository, times(1)).findById(updateModelEntity.getIdx());
        verify(adminModelJpaRepository, atLeastOnce()).findById(updateModelEntity.getIdx());
        verifyNoMoreInteractions(adminModelJpaRepository);

        InOrder inOrder = inOrder(adminModelJpaRepository);
        inOrder.verify(adminModelJpaRepository).findById(updateModelEntity.getIdx());
    }

    @Test
    @DisplayName("모델 수정 BDD 테스트")
    void 모델수정BDD테스트() {
        AdminModelEntity updateModelEntity = AdminModelEntity.builder()
                .idx(adminModelEntity.getIdx())
                .categoryCd(newCodeEntity.getCategoryCd())
                .categoryAge(2)
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .favoriteCount(1)
                .viewCount(1)
                .adminAgencyEntity(adminAgencyEntity)
                .commonImageEntityList(List.of(commonImageEntity))
                .modelMainYn("Y")
                .status("active")
                .newYn("N")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        // when
        given(adminModelJpaRepository.findById(updateModelEntity.getIdx())).willReturn(Optional.of(updateModelEntity));
        given(adminModelJpaRepository.save(updateModelEntity)).willReturn(updateModelEntity);
        AdminModelDTO modelInfo = mockAdminModelJpaService.updateModel(updateModelEntity.getIdx(), updateModelEntity);

        // then
        assertThat(modelInfo.getModelKorFirstName()).isEqualTo(updateModelEntity.getModelKorFirstName());
        assertThat(modelInfo.getModelKorSecondName()).isEqualTo(updateModelEntity.getModelKorSecondName());

        // verify
        then(adminModelJpaRepository).should(times(1)).findById(updateModelEntity.getIdx());
        then(adminModelJpaRepository).should(atLeastOnce()).findById(updateModelEntity.getIdx());
        then(adminModelJpaRepository).shouldHaveNoMoreInteractions();
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
        // when
        when(adminAgencyJpaRepository.findById(adminAgencyEntity.getIdx())).thenReturn(Optional.ofNullable(adminAgencyEntity));
        when(adminModelJpaRepository.save(adminModelEntity)).thenReturn(adminModelEntity);
        AdminModelDTO modelInfo = mockAdminModelJpaService.updateModelAgency(adminAgencyEntity.getIdx(), adminModelEntity);

        // then
        assertThat(modelInfo.getModelAgency().getIdx()).isEqualTo(adminModelEntity.getAdminAgencyEntity().getIdx());

        // verify
        verify(adminAgencyJpaRepository, times(1)).findById(adminAgencyEntity.getIdx());
        verify(adminAgencyJpaRepository, atLeastOnce()).findById(adminAgencyEntity.getIdx());
        verifyNoMoreInteractions(adminAgencyJpaRepository);

        InOrder inOrder = inOrder(adminAgencyJpaRepository);
        inOrder.verify(adminAgencyJpaRepository).findById(adminAgencyEntity.getIdx());
    }

    @Test
    @DisplayName("모델 소속사 수정 BDD 테스트")
    void 모델소속사수정BDD테스트() {
        // when
        given(adminAgencyJpaRepository.findById(adminAgencyEntity.getIdx())).willReturn(Optional.ofNullable(adminAgencyEntity));
        given(adminModelJpaRepository.save(adminModelEntity)).willReturn(adminModelEntity);
        AdminModelDTO modelInfo = mockAdminModelJpaService.updateModelAgency(adminAgencyEntity.getIdx(), adminModelEntity);

        // then
        assertThat(modelInfo.getModelAgency().getIdx()).isEqualTo(adminModelEntity.getAdminAgencyEntity().getIdx());

        // verify
        then(adminAgencyJpaRepository).should(times(1)).findById(adminAgencyEntity.getIdx());
        then(adminAgencyJpaRepository).should(atLeastOnce()).findById(adminAgencyEntity.getIdx());
        then(adminAgencyJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("어드민코멘트등록Mockito테스트")
    void 어드민코멘트등록Mockito테스트() {
        // when
        when(adminModelJpaRepository.findById(adminModelEntity.getIdx())).thenReturn(Optional.ofNullable(adminModelEntity));
        when(adminCommentJpaRepository.save(adminCommentEntity)).thenReturn(adminCommentEntity);
        AdminCommentDTO commentInfo = mockAdminModelJpaService.insertModelAdminComment(adminModelEntity.getIdx(), adminCommentEntity);

        // then
        assertThat(commentInfo.getComment()).isEqualTo(adminCommentEntity.getComment());
        assertThat(commentInfo.getCommentType()).isEqualTo(adminCommentEntity.getCommentType());
        assertThat(commentInfo.getAdminModelDTO().getIdx()).isEqualTo(adminCommentEntity.getAdminModelEntity().getIdx());

        // verify
        then(adminModelJpaRepository).should(times(1)).findById(adminModelEntity.getIdx());
        then(adminModelJpaRepository).should(atLeastOnce()).findById(adminModelEntity.getIdx());
        then(adminModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("어드민코멘트등록BDD테스트")
    void 어드민코멘트등록BDD테스트() {
        // when
        given(adminModelJpaRepository.findById(adminModelEntity.getIdx())).willReturn(Optional.ofNullable(adminModelEntity));
        given(adminCommentJpaRepository.save(adminCommentEntity)).willReturn(adminCommentEntity);
        AdminCommentDTO commentInfo = mockAdminModelJpaService.insertModelAdminComment(adminModelEntity.getIdx(), adminCommentEntity);

        // then
        assertThat(commentInfo.getComment()).isEqualTo(adminCommentEntity.getComment());
        assertThat(commentInfo.getCommentType()).isEqualTo(adminCommentEntity.getCommentType());
        assertThat(commentInfo.getAdminModelDTO().getIdx()).isEqualTo(adminCommentEntity.getAdminModelEntity().getIdx());

        // verify
        then(adminModelJpaRepository).should(times(1)).findById(adminModelEntity.getIdx());
        then(adminModelJpaRepository).should(atLeastOnce()).findById(adminModelEntity.getIdx());
        then(adminModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 어드민 코멘트 조회 Mockito 테스트")
    void 모델어드민코멘트조회Mockito테스트() {
        List<AdminCommentEntity> adminCommentList = new ArrayList<>();
        adminCommentList.add(adminCommentEntity);

        when(adminCommentJpaRepository.findByAdminModelEntityIdxAndCommentType(adminModelEntity.getIdx(), "model")).thenReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminModelJpaService.findModelAdminComment(adminModelEntity.getIdx());

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo(adminCommentEntity.getCommentType());
        assertThat(newAdminCommentList.get(0).getAdminModelDTO().getIdx()).isEqualTo(adminCommentEntity.getAdminModelEntity().getIdx());

        // verify
        verify(adminCommentJpaRepository, times(1)).findByAdminModelEntityIdxAndCommentType(adminModelEntity.getIdx(), "model");
        verify(adminCommentJpaRepository, atLeastOnce()).findByAdminModelEntityIdxAndCommentType(adminModelEntity.getIdx(), "model");
        verifyNoMoreInteractions(adminCommentJpaRepository);

        InOrder inOrder = inOrder(adminCommentJpaRepository);
        inOrder.verify(adminCommentJpaRepository).findByAdminModelEntityIdxAndCommentType(adminModelEntity.getIdx(), "model");
    }

    @Test
    @DisplayName("모델 어드민 코멘트 조회 BDD 테스트")
    void 모델어드민코멘트조회BDD테스트() {
        List<AdminCommentEntity> adminCommentList = new ArrayList<>();
        adminCommentList.add(adminCommentEntity);

        given(adminCommentJpaRepository.findByAdminModelEntityIdxAndCommentType(adminModelEntity.getIdx(), "model")).willReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminModelJpaService.findModelAdminComment(adminModelEntity.getIdx());

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo(adminCommentEntity.getCommentType());
        assertThat(newAdminCommentList.get(0).getAdminModelDTO().getIdx()).isEqualTo(adminCommentEntity.getAdminModelEntity().getIdx());

        // verify
        then(adminCommentJpaRepository).should(times(1)).findByAdminModelEntityIdxAndCommentType(adminModelEntity.getIdx(), "model");
        then(adminCommentJpaRepository).should(atLeastOnce()).findByAdminModelEntityIdxAndCommentType(adminModelEntity.getIdx(), "model");
        then(adminCommentJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("새로운 모델 설정 Mockito 테스트")
    void 새로운모델설정Mockito테스트() {
        AdminModelEntity updateModelEntity = AdminModelEntity.builder()
                .idx(adminModelEntity.getIdx())
                .categoryCd(newCodeEntity.getCategoryCd())
                .categoryAge(2)
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .favoriteCount(1)
                .viewCount(1)
                .adminAgencyEntity(adminAgencyEntity)
                .commonImageEntityList(List.of(commonImageEntity))
                .modelMainYn("Y")
                .status("active")
                .newYn("N")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        // when
        when(adminModelJpaRepository.findById(updateModelEntity.getIdx())).thenReturn(Optional.of(updateModelEntity));
        when(adminModelJpaRepository.save(updateModelEntity)).thenReturn(updateModelEntity);
        AdminModelDTO modelInfo = mockAdminModelJpaService.toggleModelNewYn(updateModelEntity.getIdx());

        assertThat(modelInfo.getNewYn()).isEqualTo("Y");

        // verify
        verify(adminModelJpaRepository, times(1)).findById(updateModelEntity.getIdx());
        verify(adminModelJpaRepository, atLeastOnce()).findById(updateModelEntity.getIdx());
        verifyNoMoreInteractions(adminModelJpaRepository);

        InOrder inOrder = inOrder(adminModelJpaRepository);
        inOrder.verify(adminModelJpaRepository).findById(updateModelEntity.getIdx());
    }

    @Test
    @DisplayName("새로운 모델 설정 BDD 테스트")
    void 새로운모델설정BDD테스트() {
        AdminModelEntity updateModelEntity = AdminModelEntity.builder()
                .idx(adminModelEntity.getIdx())
                .categoryCd(newCodeEntity.getCategoryCd())
                .categoryAge(2)
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .favoriteCount(1)
                .viewCount(1)
                .adminAgencyEntity(adminAgencyEntity)
                .commonImageEntityList(List.of(commonImageEntity))
                .modelMainYn("Y")
                .status("active")
                .newYn("N")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        // when
        given(adminModelJpaRepository.findById(updateModelEntity.getIdx())).willReturn(Optional.of(updateModelEntity));
        given(adminModelJpaRepository.save(updateModelEntity)).willReturn(updateModelEntity);
        AdminModelDTO modelInfo = mockAdminModelJpaService.toggleModelNewYn(updateModelEntity.getIdx());

        assertThat(modelInfo.getNewYn()).isEqualTo("Y");

        // verify
        then(adminModelJpaRepository).should(times(1)).findById(updateModelEntity.getIdx());
        then(adminModelJpaRepository).should(atLeastOnce()).findById(updateModelEntity.getIdx());
        then(adminModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 스케줄 Mockito 조회 테스트")
    void 모델스케줄Mockito조회테스트() {
        // given
        List<AdminScheduleEntity> scheduleList = new ArrayList<>();
        scheduleList.add(AdminScheduleEntity.builder().idx(1L).adminModelEntity(adminModelEntity)
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());

        // when
        when(adminScheduleJpaRepository.findAllById(adminModelEntity.getIdx())).thenReturn(scheduleList);
        List<AdminScheduleDTO> newScheduleList = mockAdminModelJpaService.findOneModelSchedule(adminModelEntity.getIdx());

        // then
        assertThat(newScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(newScheduleList.get(0).getModelIdx()).isEqualTo(scheduleList.get(0).getAdminModelEntity().getIdx());
        assertThat(newScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());
        assertThat(newScheduleList.get(0).getModelScheduleTime()).isEqualTo(scheduleList.get(0).getModelScheduleTime());

        // verify
        verify(adminScheduleJpaRepository, times(1)).findAllById(adminModelEntity.getIdx());
        verify(adminScheduleJpaRepository, atLeastOnce()).findAllById(adminModelEntity.getIdx());
        verifyNoMoreInteractions(adminScheduleJpaRepository);

        InOrder inOrder = inOrder(adminScheduleJpaRepository);
        inOrder.verify(adminScheduleJpaRepository).findAllById(adminModelEntity.getIdx());
    }

    @Test
    @DisplayName("모델 스케줄 BDD 조회 테스트")
    void 모델스케줄BDD조회테스트() {
        // given
        List<AdminScheduleEntity> scheduleList = new ArrayList<>();
        scheduleList.add(AdminScheduleEntity.builder().idx(1L).adminModelEntity(adminModelEntity)
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());

        // when
        given(adminScheduleJpaRepository.findAllById(adminModelEntity.getIdx())).willReturn(scheduleList);
        List<AdminScheduleDTO> newScheduleList = mockAdminModelJpaService.findOneModelSchedule(adminModelEntity.getIdx());

        // then
        assertThat(newScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(newScheduleList.get(0).getModelIdx()).isEqualTo(scheduleList.get(0).getAdminModelEntity().getIdx());
        assertThat(newScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());
        assertThat(newScheduleList.get(0).getModelScheduleTime()).isEqualTo(scheduleList.get(0).getModelScheduleTime());

        // verify
        then(adminScheduleJpaRepository).should(times(1)).findAllById(adminModelEntity.getIdx());
        then(adminScheduleJpaRepository).should(atLeastOnce()).findAllById(adminModelEntity.getIdx());
        then(adminScheduleJpaRepository).shouldHaveNoMoreInteractions();
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

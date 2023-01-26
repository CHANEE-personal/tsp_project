package com.tsp.api.model.service;

import com.tsp.api.common.EntityType;
import com.tsp.api.comment.domain.AdminCommentDTO;
import com.tsp.api.comment.domain.AdminCommentEntity;
import com.tsp.api.common.domain.CommonImageDTO;
import com.tsp.api.common.domain.CommonImageEntity;
import com.tsp.api.model.domain.AdminModelDTO;
import com.tsp.api.model.domain.AdminModelEntity;
import com.tsp.api.model.domain.CareerJson;
import com.tsp.api.model.domain.agency.AdminAgencyDTO;
import com.tsp.api.model.domain.agency.AdminAgencyEntity;
import com.tsp.api.model.domain.recommend.AdminRecommendEntity;
import com.tsp.api.model.domain.schedule.AdminScheduleDTO;
import com.tsp.api.user.domain.AdminUserEntity;
import com.tsp.api.user.service.repository.AdminUserJpaQueryRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("모델 Repository Test")
class AdminModelJpaQueryRepositoryTest {
    @Mock private AdminModelJpaQueryRepository mockAdminModelJpaQueryRepository;
    private final AdminModelJpaQueryRepository adminModelJpaQueryRepository;
    private final AdminUserJpaQueryRepository adminUserJpaQueryRepository;
    private final EntityManager em;

    private AdminModelEntity adminModelEntity;
    private AdminModelDTO adminModelDTO;
    private CommonImageEntity commonImageEntity;
    private CommonImageDTO commonImageDTO;
    private AdminAgencyEntity adminAgencyEntity;
    private AdminAgencyDTO adminAgencyDTO;
    private AdminCommentEntity adminCommentEntity;
    private AdminCommentDTO adminCommentDTO;

    public void createModelAndImage() {
        AdminUserEntity adminUserEntity = AdminUserEntity.builder()
                .userId("admin02")
                .password("pass1234")
                .name("test")
                .visible("Y")
                .build();

        adminAgencyEntity = AdminAgencyEntity.builder()
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

        adminAgencyDTO = AdminAgencyEntity.toDto(adminAgencyEntity);

        ArrayList<CareerJson> careerList = new ArrayList<>();
        careerList.add(new CareerJson("title","txt"));

        List<String> modelKeyword = new ArrayList<>();
        modelKeyword.add("테스트");

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
                .modelKeyword(modelKeyword)
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        adminModelDTO = AdminModelEntity.toDto(adminModelEntity);

        commonImageEntity = CommonImageEntity.builder()
                .idx(1L)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1L)
                .typeName(EntityType.MODEL)
                .build();

        commonImageDTO = CommonImageEntity.toDto(commonImageEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createModelAndImage();
    }

    @Test
    @DisplayName("모델 리스트 조회 테스트")
    void 모델리스트조회테스트() {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", 1);
        PageRequest pageRequest = PageRequest.of(1, 100);

        // then
        assertThat(adminModelJpaQueryRepository.findModelList(modelMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("모델 Mockito 조회 테스트")
    void 모델Mockito조회테스트() {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", 1);
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminModelDTO> modelList = new ArrayList<>();
        modelList.add(AdminModelDTO.builder().idx(3L).categoryCd(1).modelKorName("조찬희")
                .modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());
        Page<AdminModelDTO> resultModel = new PageImpl<>(modelList, pageRequest, modelList.size());

        // when
        when(mockAdminModelJpaQueryRepository.findModelList(modelMap, pageRequest)).thenReturn(resultModel);
        Page<AdminModelDTO> newModelList = mockAdminModelJpaQueryRepository.findModelList(modelMap, pageRequest);
        List<AdminModelDTO> findModelList = newModelList.stream().collect(Collectors.toList());

        // then
        assertThat(findModelList.get(0).getIdx()).isEqualTo(modelList.get(0).getIdx());
        assertThat(findModelList.get(0).getCategoryCd()).isEqualTo(modelList.get(0).getCategoryCd());
        assertThat(findModelList.get(0).getModelKorName()).isEqualTo(modelList.get(0).getModelKorName());
        assertThat(findModelList.get(0).getModelAgency().getAgencyName()).isEqualTo(modelList.get(0).getModelAgency().getAgencyName());
        assertThat(findModelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(modelList.get(0).getModelAgency().getAgencyDescription());

        // verify
        verify(mockAdminModelJpaQueryRepository, times(1)).findModelList(modelMap, pageRequest);
        verify(mockAdminModelJpaQueryRepository, atLeastOnce()).findModelList(modelMap, pageRequest);
        verifyNoMoreInteractions(mockAdminModelJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminModelJpaQueryRepository);
        inOrder.verify(mockAdminModelJpaQueryRepository).findModelList(modelMap, pageRequest);
    }

    @Test
    @DisplayName("모델 리스트 조회 BDD 테스트")
    void 모델리스트조회BDD테스트() {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", 1);
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminModelDTO> modelList = new ArrayList<>();
        modelList.add(AdminModelDTO.builder().idx(3L).categoryCd(1).modelKorName("조찬희")
                .modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());
        Page<AdminModelDTO> resultModel = new PageImpl<>(modelList, pageRequest, modelList.size());

        // when
        given(mockAdminModelJpaQueryRepository.findModelList(modelMap, pageRequest)).willReturn(resultModel);
        Page<AdminModelDTO> newModelList = mockAdminModelJpaQueryRepository.findModelList(modelMap, pageRequest);
        List<AdminModelDTO> findModelList = newModelList.stream().collect(Collectors.toList());

        // then
        assertThat(findModelList.get(0).getIdx()).isEqualTo(modelList.get(0).getIdx());
        assertThat(findModelList.get(0).getCategoryCd()).isEqualTo(modelList.get(0).getCategoryCd());
        assertThat(findModelList.get(0).getModelKorName()).isEqualTo(modelList.get(0).getModelKorName());
        assertThat(findModelList.get(0).getModelAgency().getAgencyName()).isEqualTo(modelList.get(0).getModelAgency().getAgencyName());
        assertThat(findModelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(modelList.get(0).getModelAgency().getAgencyDescription());

        // verify
        then(mockAdminModelJpaQueryRepository).should(times(1)).findModelList(modelMap, pageRequest);
        then(mockAdminModelJpaQueryRepository).should(atLeastOnce()).findModelList(modelMap, pageRequest);
        then(mockAdminModelJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 상세 조회 테스트")
    void 모델상세조회테스트() {
        // given
        adminModelEntity = AdminModelEntity.builder().idx(1L).categoryCd(2).build();

        // when
        adminModelDTO = adminModelJpaQueryRepository.findOneModel(adminModelEntity.getIdx());

        // then
        assertAll(() -> {
                    assertThat(adminModelDTO.getIdx()).isEqualTo(1);
                },
                () -> {
                    assertThat(adminModelDTO.getCategoryCd()).isEqualTo(2);
                    assertNotNull(adminModelDTO.getCategoryCd());
                },
                () -> {
                    assertThat(adminModelDTO.getCategoryAge()).isEqualTo(2);
                    assertNotNull(adminModelDTO.getCategoryAge());
                },
                () -> {
                    assertThat(adminModelDTO.getModelKorName()).isEqualTo("김예영");
                    assertNotNull(adminModelDTO.getModelKorName());
                },
                () -> {
                    assertThat(adminModelDTO.getModelEngName()).isEqualTo("kimye yeong");
                    assertNotNull(adminModelDTO.getModelEngName());
                },
                () -> {
                    assertThat(adminModelDTO.getHeight()).isEqualTo(173);
                    assertNotNull(adminModelDTO.getHeight());
                },
                () -> {
                    assertThat(adminModelDTO.getSize3()).isEqualTo("31-24-34");
                    assertNotNull(adminModelDTO.getSize3());
                },
                () -> {
                    assertThat(adminModelDTO.getShoes()).isEqualTo(240);
                    assertNotNull(adminModelDTO.getShoes());
                });
    }

    @Test
    @DisplayName("이전 or 다음 모델 상세 조회 테스트")
    void 이전or다음모델상세조회테스트() {
        // given
        adminModelEntity = AdminModelEntity.builder().idx(145L).categoryCd(2).build();

        // when
        adminModelDTO = adminModelJpaQueryRepository.findOneModel(adminModelEntity.getIdx());

        // 이전 모델
        assertThat(adminModelJpaQueryRepository.findPrevOneModel(adminModelEntity).getIdx()).isEqualTo(144);
        // 다음 모델
        assertThat(adminModelJpaQueryRepository.findNextOneModel(adminModelEntity).getIdx()).isEqualTo(147);
    }

    @Test
    @DisplayName("이전 모델 상세 조회 Mockito 테스트")
    void 이전모델상세조회Mockito테스트() {
        // given
        adminModelEntity = AdminModelEntity.builder().idx(145L).categoryCd(2).build();
        // when
        adminModelDTO = adminModelJpaQueryRepository.findPrevOneModel(adminModelEntity);

        when(mockAdminModelJpaQueryRepository.findPrevOneModel(adminModelEntity)).thenReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaQueryRepository.findPrevOneModel(adminModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(144);

        // verify
        verify(mockAdminModelJpaQueryRepository, times(1)).findPrevOneModel(adminModelEntity);
        verify(mockAdminModelJpaQueryRepository, atLeastOnce()).findPrevOneModel(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminModelJpaQueryRepository);
        inOrder.verify(mockAdminModelJpaQueryRepository).findPrevOneModel(adminModelEntity);
    }

    @Test
    @DisplayName("이전 모델 상세 조회 BDD 테스트")
    void 이전모델상세조회BDD테스트() {
        // given
        adminModelEntity = AdminModelEntity.builder().idx(145L).categoryCd(2).build();
        // when
        adminModelDTO = adminModelJpaQueryRepository.findPrevOneModel(adminModelEntity);

        given(mockAdminModelJpaQueryRepository.findPrevOneModel(adminModelEntity)).willReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaQueryRepository.findPrevOneModel(adminModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(144);

        // verify
        then(mockAdminModelJpaQueryRepository).should(times(1)).findPrevOneModel(adminModelEntity);
        then(mockAdminModelJpaQueryRepository).should(atLeastOnce()).findPrevOneModel(adminModelEntity);
        then(mockAdminModelJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 모델 상세 조회 Mockito 테스트")
    void 다음모델상세조회Mockito테스트() {
        // given
        adminModelEntity = AdminModelEntity.builder().idx(145L).categoryCd(2).build();
        // when
        adminModelDTO = adminModelJpaQueryRepository.findNextOneModel(adminModelEntity);

        when(mockAdminModelJpaQueryRepository.findNextOneModel(adminModelEntity)).thenReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaQueryRepository.findNextOneModel(adminModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(147);

        // verify
        verify(mockAdminModelJpaQueryRepository, times(1)).findNextOneModel(adminModelEntity);
        verify(mockAdminModelJpaQueryRepository, atLeastOnce()).findNextOneModel(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminModelJpaQueryRepository);
        inOrder.verify(mockAdminModelJpaQueryRepository).findNextOneModel(adminModelEntity);
    }

    @Test
    @DisplayName("다음 모델 상세 조회 BDD 테스트")
    void 다음모델상세조회BDD테스트() {
        // given
        adminModelEntity = AdminModelEntity.builder().idx(145L).categoryCd(2).build();
        // when
        adminModelDTO = adminModelJpaQueryRepository.findNextOneModel(adminModelEntity);

        given(mockAdminModelJpaQueryRepository.findNextOneModel(adminModelEntity)).willReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaQueryRepository.findNextOneModel(adminModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(147);

        // verify
        then(mockAdminModelJpaQueryRepository).should(times(1)).findNextOneModel(adminModelEntity);
        then(mockAdminModelJpaQueryRepository).should(atLeastOnce()).findNextOneModel(adminModelEntity);
        then(mockAdminModelJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 상세 조회 Mockito 테스트")
    void 모델상세조회Mockito테스트() {
        // given
        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        adminModelEntity = AdminModelEntity.builder().idx(1L).commonImageEntityList(commonImageEntityList).build();

        adminModelDTO = AdminModelDTO.builder()
                .idx(1L)
                .categoryCd(1)
                .categoryAge(2)
                .modelKorName("조찬희")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .modelAgency(AdminAgencyEntity.toDto(adminAgencyEntity))
                .modelImage(CommonImageEntity.toDtoList(commonImageEntityList))
                .build();

        // when
        when(mockAdminModelJpaQueryRepository.findOneModel(adminModelEntity.getIdx())).thenReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaQueryRepository.findOneModel(adminModelEntity.getIdx());

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(1);
        assertThat(modelInfo.getCategoryCd()).isEqualTo(1);
        assertThat(modelInfo.getCategoryAge()).isEqualTo(2);
        assertThat(modelInfo.getModelKorName()).isEqualTo("조찬희");
        assertThat(modelInfo.getModelEngName()).isEqualTo("CHOCHANHEE");
        assertThat(modelInfo.getModelDescription()).isEqualTo("chaneeCho");
        assertThat(modelInfo.getHeight()).isEqualTo(170);
        assertThat(modelInfo.getSize3()).isEqualTo("34-24-34");
        assertThat(modelInfo.getShoes()).isEqualTo(270);
        assertThat(modelInfo.getVisible()).isEqualTo("Y");
        assertThat(modelInfo.getModelAgency().getAgencyName()).isEqualTo("agency");
        assertThat(modelInfo.getModelAgency().getAgencyDescription()).isEqualTo("agency");
        assertThat(modelInfo.getModelImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(modelInfo.getModelImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(modelInfo.getModelImage().get(0).getFilePath()).isEqualTo("/test/test.jpg");
        assertThat(modelInfo.getModelImage().get(0).getImageType()).isEqualTo("main");
        assertThat(modelInfo.getModelImage().get(0).getTypeName()).isEqualTo(EntityType.MODEL);

        // verify
        verify(mockAdminModelJpaQueryRepository, times(1)).findOneModel(adminModelEntity.getIdx());
        verify(mockAdminModelJpaQueryRepository, atLeastOnce()).findOneModel(adminModelEntity.getIdx());
        verifyNoMoreInteractions(mockAdminModelJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminModelJpaQueryRepository);
        inOrder.verify(mockAdminModelJpaQueryRepository).findOneModel(adminModelEntity.getIdx());
    }

    @Test
    @DisplayName("모델 상세 조회 BDD 테스트")
    void 모델상세조회BDD테스트() {
        // given
        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        adminModelEntity = AdminModelEntity.builder().idx(1L).commonImageEntityList(commonImageEntityList).build();

        adminModelDTO = AdminModelDTO.builder()
                .idx(1L)
                .categoryCd(1)
                .categoryAge(2)
                .modelKorName("조찬희")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .modelAgency(AdminAgencyEntity.toDto(adminAgencyEntity))
                .modelImage(commonImageEntityList.stream().map(CommonImageEntity::toDto).collect(Collectors.toList()))
                .build();

        // when
        given(mockAdminModelJpaQueryRepository.findOneModel(adminModelEntity.getIdx())).willReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaQueryRepository.findOneModel(adminModelEntity.getIdx());

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(1);
        assertThat(modelInfo.getCategoryCd()).isEqualTo(1);
        assertThat(modelInfo.getCategoryAge()).isEqualTo(2);
        assertThat(modelInfo.getModelKorName()).isEqualTo("조찬희");
        assertThat(modelInfo.getModelEngName()).isEqualTo("CHOCHANHEE");
        assertThat(modelInfo.getModelDescription()).isEqualTo("chaneeCho");
        assertThat(modelInfo.getHeight()).isEqualTo(170);
        assertThat(modelInfo.getSize3()).isEqualTo("34-24-34");
        assertThat(modelInfo.getShoes()).isEqualTo(270);
        assertThat(modelInfo.getVisible()).isEqualTo("Y");
        assertThat(modelInfo.getModelAgency().getAgencyName()).isEqualTo("agency");
        assertThat(modelInfo.getModelAgency().getAgencyDescription()).isEqualTo("agency");
        assertThat(modelInfo.getModelImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(modelInfo.getModelImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(modelInfo.getModelImage().get(0).getFilePath()).isEqualTo("/test/test.jpg");
        assertThat(modelInfo.getModelImage().get(0).getImageType()).isEqualTo("main");
        assertThat(modelInfo.getModelImage().get(0).getTypeName()).isEqualTo(EntityType.MODEL);

        // verify
        then(mockAdminModelJpaQueryRepository).should(times(1)).findOneModel(adminModelEntity.getIdx());
        then(mockAdminModelJpaQueryRepository).should(atLeastOnce()).findOneModel(adminModelEntity.getIdx());
        then(mockAdminModelJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 등록 Mockito 테스트")
    void 모델등록Mockito테스트() {
        // given
        em.persist(adminModelEntity);
        adminModelDTO = AdminModelEntity.toDto(adminModelEntity);

        // when
        when(mockAdminModelJpaQueryRepository.findOneModel(adminModelEntity.getIdx())).thenReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaQueryRepository.findOneModel(adminModelEntity.getIdx());

        // then
        assertThat(modelInfo.getCategoryCd()).isEqualTo(1);
        assertThat(modelInfo.getCategoryAge()).isEqualTo(2);

        // verify
        verify(mockAdminModelJpaQueryRepository, times(1)).findOneModel(adminModelEntity.getIdx());
        verify(mockAdminModelJpaQueryRepository, atLeastOnce()).findOneModel(adminModelEntity.getIdx());
        verifyNoMoreInteractions(mockAdminModelJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminModelJpaQueryRepository);
        inOrder.verify(mockAdminModelJpaQueryRepository).findOneModel(adminModelEntity.getIdx());
    }

    @Test
    @DisplayName("모델 등록 BDD 테스트")
    void 모델등록BDD테스트() {
        // given
        em.persist(adminModelEntity);
        adminModelDTO = AdminModelEntity.toDto(adminModelEntity);

        // when
        given(mockAdminModelJpaQueryRepository.findOneModel(adminModelEntity.getIdx())).willReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaQueryRepository.findOneModel(adminModelEntity.getIdx());

        // then
        assertThat(modelInfo.getCategoryCd()).isEqualTo(1);
        assertThat(modelInfo.getCategoryAge()).isEqualTo(2);

        // verify
        then(mockAdminModelJpaQueryRepository).should(times(1)).findOneModel(adminModelEntity.getIdx());
        then(mockAdminModelJpaQueryRepository).should(atLeastOnce()).findOneModel(adminModelEntity.getIdx());
        then(mockAdminModelJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 등록 CreatedBy 테스트")
    void 모델등록CreatedBy테스트() {
        // given
        em.persist(adminModelEntity);
        adminModelDTO = AdminModelEntity.toDto(adminModelEntity);

        // when
        when(mockAdminModelJpaQueryRepository.findOneModel(adminModelEntity.getIdx())).thenReturn(adminModelDTO);
        AdminModelDTO newModelInfo = mockAdminModelJpaQueryRepository.findOneModel(adminModelEntity.getIdx());

        // then
//        assertThat(newModelInfo.getCreator()).isNotNull();
//        assertThat(newModelInfo.getCreateTime()).isNotNull();

        InOrder inOrder = inOrder(mockAdminModelJpaQueryRepository);
        inOrder.verify(mockAdminModelJpaQueryRepository).findOneModel(adminModelEntity.getIdx());
    }

    @Test
    @DisplayName("모델 어드민 코멘트 조회 Mockito 테스트")
    void 모델어드민코멘트조회Mockito테스트() {
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

        em.persist(adminModelEntity);
        adminModelDTO = AdminModelEntity.toDto(adminModelEntity);

        adminCommentEntity = AdminCommentEntity.builder()
                .comment("코멘트 테스트")
                .visible("Y")
                .build();

        List<AdminCommentDTO> adminCommentList = new ArrayList<>();
        adminCommentList.add(AdminCommentDTO.builder()
                .comment("코멘트 테스트")
                .commentType("model")
                .commentTypeIdx(adminModelDTO.getIdx())
                .visible("Y")
                .build());

        when(mockAdminModelJpaQueryRepository.findModelAdminComment(adminModelEntity.getIdx())).thenReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminModelJpaQueryRepository.findModelAdminComment(adminModelEntity.getIdx());

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("model");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminModelEntity.getIdx());

        // verify
        verify(mockAdminModelJpaQueryRepository, times(1)).findModelAdminComment(adminModelEntity.getIdx());
        verify(mockAdminModelJpaQueryRepository, atLeastOnce()).findModelAdminComment(adminModelEntity.getIdx());
        verifyNoMoreInteractions(mockAdminModelJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminModelJpaQueryRepository);
        inOrder.verify(mockAdminModelJpaQueryRepository).findModelAdminComment(adminModelEntity.getIdx());
    }

    @Test
    @DisplayName("모델 어드민 코멘트 조회 BDD 테스트")
    void 모델어드민코멘트조회BDD테스트() {
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

        em.persist(adminModelEntity);
        adminModelDTO = AdminModelEntity.toDto(adminModelEntity);

        adminCommentEntity = AdminCommentEntity.builder()
                .comment("코멘트 테스트")
                .visible("Y")
                .build();

        List<AdminCommentDTO> adminCommentList = new ArrayList<>();
        adminCommentList.add(AdminCommentDTO.builder()
                .comment("코멘트 테스트")
                .commentType("model")
                .commentTypeIdx(adminModelDTO.getIdx())
                .visible("Y")
                .build());

        given(mockAdminModelJpaQueryRepository.findModelAdminComment(adminModelEntity.getIdx())).willReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminModelJpaQueryRepository.findModelAdminComment(adminModelEntity.getIdx());

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("model");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminModelEntity.getIdx());

        // verify
        then(mockAdminModelJpaQueryRepository).should(times(1)).findModelAdminComment(adminModelEntity.getIdx());
        then(mockAdminModelJpaQueryRepository).should(atLeastOnce()).findModelAdminComment(adminModelEntity.getIdx());
        then(mockAdminModelJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 스케줄 Mockito 조회 테스트")
    void 모델스케줄Mockito조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        scheduleMap.put("jpaStartPage", 1);
        scheduleMap.put("size", 3);

        List<AdminScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(AdminScheduleDTO.builder().idx(1L).modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());

        // when
        when(mockAdminModelJpaQueryRepository.findOneModelSchedule(adminModelEntity.getIdx())).thenReturn(scheduleList);
        List<AdminScheduleDTO> newScheduleList = mockAdminModelJpaQueryRepository.findOneModelSchedule(adminModelEntity.getIdx());

        // then
        assertThat(newScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(newScheduleList.get(0).getModelIdx()).isEqualTo(scheduleList.get(0).getModelIdx());
        assertThat(newScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());
        assertThat(newScheduleList.get(0).getModelScheduleTime()).isEqualTo(scheduleList.get(0).getModelScheduleTime());

        // verify
        verify(mockAdminModelJpaQueryRepository, times(1)).findOneModelSchedule(adminModelEntity.getIdx());
        verify(mockAdminModelJpaQueryRepository, atLeastOnce()).findOneModelSchedule(adminModelEntity.getIdx());
        verifyNoMoreInteractions(mockAdminModelJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminModelJpaQueryRepository);
        inOrder.verify(mockAdminModelJpaQueryRepository).findOneModelSchedule(adminModelEntity.getIdx());
    }

    @Test
    @DisplayName("모델 스케줄 BDD 조회 테스트")
    void 모델스케줄BDD조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        scheduleMap.put("jpaStartPage", 1);
        scheduleMap.put("size", 3);

        List<AdminScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(AdminScheduleDTO.builder().idx(1L).modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());

        // when
        given(mockAdminModelJpaQueryRepository.findOneModelSchedule(adminModelEntity.getIdx())).willReturn(scheduleList);
        List<AdminScheduleDTO> newScheduleList = mockAdminModelJpaQueryRepository.findOneModelSchedule(adminModelEntity.getIdx());

        // then
        assertThat(newScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(newScheduleList.get(0).getModelIdx()).isEqualTo(scheduleList.get(0).getModelIdx());
        assertThat(newScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());
        assertThat(newScheduleList.get(0).getModelScheduleTime()).isEqualTo(scheduleList.get(0).getModelScheduleTime());

        // verify
        then(mockAdminModelJpaQueryRepository).should(times(1)).findOneModelSchedule(adminModelEntity.getIdx());
        then(mockAdminModelJpaQueryRepository).should(atLeastOnce()).findOneModelSchedule(adminModelEntity.getIdx());
        then(mockAdminModelJpaQueryRepository).shouldHaveNoMoreInteractions();
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

        em.persist(adminRecommendEntity);

        assertThat(adminModelJpaQueryRepository.findRecommendList(recommendMap)).isNotEmpty();
    }
}

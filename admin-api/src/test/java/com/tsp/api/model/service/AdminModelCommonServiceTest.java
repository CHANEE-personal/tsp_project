package com.tsp.api.model.service;

import com.tsp.api.comment.domain.AdminCommentDTO;
import com.tsp.api.comment.domain.AdminCommentEntity;
import com.tsp.api.comment.service.AdminCommentJpaRepository;
import com.tsp.api.common.EntityType;
import com.tsp.api.common.domain.*;
import com.tsp.api.common.image.AdminCommonImageJpaRepository;
import com.tsp.api.common.service.AdminCommonJpaRepository;
import com.tsp.api.model.domain.AdminModelDTO;
import com.tsp.api.model.domain.AdminModelEntity;
import com.tsp.api.model.domain.agency.AdminAgencyDTO;
import com.tsp.api.model.domain.agency.AdminAgencyEntity;
import com.tsp.api.model.domain.negotiation.AdminNegotiationDTO;
import com.tsp.api.model.domain.negotiation.AdminNegotiationEntity;
import com.tsp.api.model.service.agency.AdminAgencyJpaRepository;
import com.tsp.api.model.service.negotiation.AdminNegotiationJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;

import java.util.List;

import static java.time.LocalDateTime.now;

public abstract class AdminModelCommonServiceTest {

    @Autowired private AdminCommonJpaRepository adminCommonJpaRepository;
    @Autowired private AdminAgencyJpaRepository adminAgencyJpaRepository;
    @Autowired private AdminCommonImageJpaRepository adminCommonImageJpaRepository;
    @Autowired private AdminModelJpaRepository adminModelJpaRepository;
    @Autowired private AdminCommentJpaRepository adminCommentJpaRepository;
    @Autowired private AdminNegotiationJpaRepository adminNegotiationJpaRepository;
    protected NewCodeEntity newCodeEntity;
    protected NewCodeDTO newCodeDTO;

    protected AdminModelEntity adminModelEntity;
    protected AdminModelDTO adminModelDTO;
    protected CommonImageEntity commonImageEntity;
    protected CommonImageDTO commonImageDTO;
    protected AdminAgencyEntity adminAgencyEntity;
    protected AdminAgencyDTO adminAgencyDTO;
    protected AdminCommentEntity adminCommentEntity;
    protected AdminCommentDTO adminCommentDTO;
    protected AdminNegotiationEntity adminNegotiationEntity;
    protected AdminNegotiationDTO adminNegotiationDTO;

    @BeforeEach
    @EventListener(ApplicationContext.class)
    public void init() {
        saveModel();
    }

    public void saveModel() {
        // 공통 코드 등록
        newCodeEntity = adminCommonJpaRepository.save(
                NewCodeEntity.builder()
                        .categoryCd(1)
                        .categoryNm("men")
                        .visible("Y")
                        .cmmType("model")
                        .build());

        newCodeDTO = NewCodeEntity.toDto(newCodeEntity);

        // 모델 소속사 등록
        adminAgencyEntity = adminAgencyJpaRepository.save(
                AdminAgencyEntity.builder()
                        .agencyName("agency")
                        .agencyDescription("agency")
                        .visible("Y")
                        .build()
        );

        adminAgencyDTO = AdminAgencyEntity.toDto(adminAgencyEntity);

        // 모델 이미지 등록
        commonImageEntity = adminCommonImageJpaRepository.save(
                CommonImageEntity.builder()
                        .imageType("main")
                        .fileName("test.jpg")
                        .fileMask("test.jpg")
                        .filePath("/test/test.jpg")
                        .typeIdx(1L)
                        .typeName(EntityType.MODEL)
                        .build()
        );

        commonImageDTO = CommonImageEntity.toDto(commonImageEntity);

        // 모델 등록
        adminModelEntity = adminModelJpaRepository.save(
                AdminModelEntity.builder()
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
                        .build()
        );

        adminModelDTO = AdminModelEntity.toDto(adminModelEntity);

        // 모델 코멘트 등록
        adminCommentEntity = adminCommentJpaRepository.save(
                AdminCommentEntity.builder()
                        .adminModelEntity(adminModelEntity)
                        .comment("comment")
                        .commentType("model")
                        .visible("Y")
                        .build()
        );

        adminCommentDTO = AdminCommentEntity.toDto(adminCommentEntity);

        // 모델 섭외 등록
        adminNegotiationEntity = adminNegotiationJpaRepository.save(
                AdminNegotiationEntity.builder()
                        .adminModelEntity(adminModelEntity)
                        .modelKorName(adminModelEntity.getModelKorName())
                        .modelNegotiationDesc("영화 프로젝트 참여")
                        .modelNegotiationDate(now())
                        .name("조찬희")
                        .phone("010-1234-5678")
                        .email("test@gmail.com")
                        .visible("Y")
                        .build()
        );

        adminNegotiationDTO = AdminNegotiationEntity.toDto(adminNegotiationEntity);
    }
}

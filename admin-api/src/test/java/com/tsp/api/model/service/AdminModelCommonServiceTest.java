package com.tsp.api.model.service;

import com.tsp.api.comment.domain.AdminCommentDTO;
import com.tsp.api.comment.domain.AdminCommentEntity;
import com.tsp.api.comment.service.AdminCommentJpaRepository;
import com.tsp.api.common.EntityType;
import com.tsp.api.common.domain.*;
import com.tsp.api.common.image.AdminCommonImageJpaRepository;
import com.tsp.api.common.service.AdminCommonJpaRepository;
import com.tsp.api.faq.domain.AdminFaqDTO;
import com.tsp.api.faq.domain.AdminFaqEntity;
import com.tsp.api.faq.service.AdminFaqJpaRepository;
import com.tsp.api.festival.domain.AdminFestivalDTO;
import com.tsp.api.festival.domain.AdminFestivalEntity;
import com.tsp.api.festival.service.AdminFestivalJpaRepository;
import com.tsp.api.model.domain.AdminModelDTO;
import com.tsp.api.model.domain.AdminModelEntity;
import com.tsp.api.model.domain.agency.AdminAgencyDTO;
import com.tsp.api.model.domain.agency.AdminAgencyEntity;
import com.tsp.api.model.domain.negotiation.AdminNegotiationDTO;
import com.tsp.api.model.domain.negotiation.AdminNegotiationEntity;
import com.tsp.api.model.service.agency.AdminAgencyJpaRepository;
import com.tsp.api.model.service.negotiation.AdminNegotiationJpaRepository;
import com.tsp.api.notice.domain.AdminNoticeDTO;
import com.tsp.api.notice.domain.AdminNoticeEntity;
import com.tsp.api.notice.service.AdminNoticeJpaRepository;
import com.tsp.api.portfolio.domain.AdminPortFolioDTO;
import com.tsp.api.portfolio.domain.AdminPortFolioEntity;
import com.tsp.api.portfolio.service.AdminPortfolioJpaRepository;
import com.tsp.api.production.domain.AdminProductionDTO;
import com.tsp.api.production.domain.AdminProductionEntity;
import com.tsp.api.production.service.AdminProductionJpaRepository;
import com.tsp.api.support.domain.AdminSupportDTO;
import com.tsp.api.support.domain.AdminSupportEntity;
import com.tsp.api.support.domain.evaluation.EvaluationDTO;
import com.tsp.api.support.domain.evaluation.EvaluationEntity;
import com.tsp.api.support.evaluation.AdminEvaluationJpaRepository;
import com.tsp.api.support.service.AdminSupportJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;

public abstract class AdminModelCommonServiceTest {

    @Autowired private AdminCommonJpaRepository adminCommonJpaRepository;
    @Autowired private AdminAgencyJpaRepository adminAgencyJpaRepository;
    @Autowired private AdminCommonImageJpaRepository adminCommonImageJpaRepository;
    @Autowired private AdminModelJpaRepository adminModelJpaRepository;
    @Autowired private AdminCommentJpaRepository adminCommentJpaRepository;
    @Autowired private AdminNegotiationJpaRepository adminNegotiationJpaRepository;
    @Autowired private AdminFestivalJpaRepository adminFestivalJpaRepository;
    @Autowired private AdminFaqJpaRepository adminFaqJpaRepository;
    @Autowired private AdminNoticeJpaRepository adminNoticeJpaRepository;
    @Autowired private AdminProductionJpaRepository adminProductionJpaRepository;
    @Autowired private AdminSupportJpaRepository adminSupportJpaRepository;
    @Autowired private AdminEvaluationJpaRepository adminEvaluationJpaRepository;
    @Autowired private AdminPortfolioJpaRepository adminPortfolioJpaRepository;
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
    protected AdminFestivalEntity adminFestivalEntity;
    protected AdminFestivalDTO adminFestivalDTO;
    protected AdminFaqEntity adminFaqEntity;
    protected AdminFaqDTO adminFaqDTO;
    protected AdminNoticeEntity adminNoticeEntity;
    protected AdminNoticeDTO adminNoticeDTO;
    protected AdminProductionEntity adminProductionEntity;
    protected AdminProductionDTO adminProductionDTO;
    protected AdminSupportEntity adminSupportEntity;
    protected AdminSupportDTO adminSupportDTO;
    protected EvaluationEntity evaluationEntity;
    protected EvaluationDTO evaluationDTO;
    protected AdminPortFolioEntity adminPortFolioEntity;
    protected AdminPortFolioDTO adminPortFolioDTO;

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
                        .build());

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
                        .build());

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

        // 축제 등록
        adminFestivalEntity = adminFestivalJpaRepository.save(
                AdminFestivalEntity.builder()
                        .festivalTitle("축제 제목")
                        .festivalDescription("축제 내용")
                        .festivalMonth(LocalDateTime.now().getMonthValue())
                        .festivalDay(LocalDateTime.now().getDayOfMonth())
                        .festivalTime(LocalDateTime.now())
                        .build()
        );

        adminFestivalDTO = AdminFestivalEntity.toDto(adminFestivalEntity);

        // FAQ 등록
        adminFaqEntity = adminFaqJpaRepository.save(
                AdminFaqEntity.builder()
                        .title("FAQ 테스트")
                        .description("FAQ 테스트")
                        .visible("Y")
                        .build()
        );

        adminFaqDTO = AdminFaqEntity.toDto(adminFaqEntity);

        // 공지사항 등록
        adminNoticeEntity = adminNoticeJpaRepository.save(
                AdminNoticeEntity.builder()
                        .title("공지사항 테스트")
                        .description("공지사항 테스트")
                        .visible("Y")
                        .topFixed(false)
                        .build()
        );

        adminNoticeDTO = AdminNoticeEntity.toDto(adminNoticeEntity);

        // 프로덕션 등록
        adminProductionEntity = adminProductionJpaRepository.save(
                adminProductionEntity = AdminProductionEntity.builder()
                        .title("프로덕션 테스트")
                        .description("프로덕션 테스트")
                        .visible("Y")
                        .build()
        );

        adminProductionDTO = AdminProductionEntity.toDto(adminProductionEntity);

        // 지원모델 등록
        adminSupportEntity = adminSupportJpaRepository.save(
                AdminSupportEntity.builder()
                        .supportName("조찬희")
                        .supportHeight(170)
                        .supportMessage("조찬희")
                        .supportPhone("010-9466-2702")
                        .supportSize3("31-24-31")
                        .supportInstagram("https://instagram.com")
                        .supportTime(LocalDateTime.now())
                        .visible("Y")
                        .build()
        );

        adminSupportDTO = AdminSupportEntity.toDto(adminSupportEntity);

        // 지원모델 평가 등록
        evaluationEntity = adminEvaluationJpaRepository.save(
                EvaluationEntity.builder()
                        .idx(1L).adminSupportEntity(adminSupportEntity)
                        .evaluateComment("합격").visible("Y").build()
        );

        evaluationDTO = EvaluationEntity.toDto(evaluationEntity);

        // 포트폴리오 등록
        adminPortFolioEntity = adminPortfolioJpaRepository.save(
                AdminPortFolioEntity.builder()
                        .newPortFolioJpaDTO(newCodeEntity)
                        .title("포트폴리오 테스트")
                        .description("포트폴리오 테스트")
                        .hashTag("#test")
                        .videoUrl("https://youtube.com")
                        .visible("Y")
                        .build()
        );

        adminPortFolioDTO = AdminPortFolioEntity.toDto(adminPortFolioEntity);
    }
}

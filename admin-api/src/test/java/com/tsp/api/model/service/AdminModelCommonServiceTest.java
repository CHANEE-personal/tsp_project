package com.tsp.api.model.service;

import com.tsp.api.comment.domain.AdminCommentDto;
import com.tsp.api.comment.domain.AdminCommentEntity;
import com.tsp.api.comment.service.AdminCommentJpaRepository;
import com.tsp.api.common.EntityType;
import com.tsp.api.common.domain.*;
import com.tsp.api.common.image.AdminCommonImageJpaRepository;
import com.tsp.api.common.service.AdminCommonJpaRepository;
import com.tsp.api.faq.domain.AdminFaqDto;
import com.tsp.api.faq.domain.AdminFaqEntity;
import com.tsp.api.faq.service.AdminFaqJpaRepository;
import com.tsp.api.festival.domain.AdminFestivalDto;
import com.tsp.api.festival.domain.AdminFestivalEntity;
import com.tsp.api.festival.service.AdminFestivalJpaRepository;
import com.tsp.api.model.domain.AdminModelDto;
import com.tsp.api.model.domain.AdminModelEntity;
import com.tsp.api.model.domain.agency.AdminAgencyDto;
import com.tsp.api.model.domain.agency.AdminAgencyEntity;
import com.tsp.api.model.domain.negotiation.AdminNegotiationDto;
import com.tsp.api.model.domain.negotiation.AdminNegotiationEntity;
import com.tsp.api.model.domain.schedule.AdminScheduleDto;
import com.tsp.api.model.domain.schedule.AdminScheduleEntity;
import com.tsp.api.model.service.agency.AdminAgencyJpaRepository;
import com.tsp.api.model.service.negotiation.AdminNegotiationJpaRepository;
import com.tsp.api.model.service.schedule.AdminScheduleJpaRepository;
import com.tsp.api.notice.domain.AdminNoticeDto;
import com.tsp.api.notice.domain.AdminNoticeEntity;
import com.tsp.api.notice.service.AdminNoticeJpaRepository;
import com.tsp.api.portfolio.domain.AdminPortFolioDto;
import com.tsp.api.portfolio.domain.AdminPortFolioEntity;
import com.tsp.api.portfolio.service.AdminPortfolioJpaRepository;
import com.tsp.api.production.domain.AdminProductionDto;
import com.tsp.api.production.domain.AdminProductionEntity;
import com.tsp.api.production.service.AdminProductionJpaRepository;
import com.tsp.api.support.domain.AdminSupportDto;
import com.tsp.api.support.domain.AdminSupportEntity;
import com.tsp.api.support.domain.evaluation.EvaluationDto;
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
    @Autowired private AdminScheduleJpaRepository adminScheduleJpaRepository;

    protected NewCodeEntity newCodeEntity;
    protected NewCodeDto newCodeDTO;

    protected AdminModelEntity adminModelEntity;
    protected AdminModelDto adminModelDTO;
    protected CommonImageEntity commonImageEntity;
    protected CommonImageDto commonImageDTO;
    protected AdminAgencyEntity adminAgencyEntity;
    protected AdminAgencyDto adminAgencyDTO;
    protected AdminCommentEntity adminCommentEntity;
    protected AdminCommentDto adminCommentDTO;
    protected AdminNegotiationEntity adminNegotiationEntity;
    protected AdminNegotiationDto adminNegotiationDTO;
    protected AdminFestivalEntity adminFestivalEntity;
    protected AdminFestivalDto adminFestivalDTO;
    protected AdminFaqEntity adminFaqEntity;
    protected AdminFaqDto adminFaqDTO;
    protected AdminNoticeEntity adminNoticeEntity;
    protected AdminNoticeDto adminNoticeDTO;
    protected AdminProductionEntity adminProductionEntity;
    protected AdminProductionDto adminProductionDTO;
    protected AdminSupportEntity adminSupportEntity;
    protected AdminSupportDto adminSupportDTO;
    protected EvaluationEntity evaluationEntity;
    protected EvaluationDto evaluationDTO;
    protected AdminPortFolioEntity adminPortFolioEntity;
    protected AdminPortFolioDto adminPortFolioDTO;
    protected AdminScheduleEntity adminScheduleEntity;
    protected AdminScheduleDto adminScheduleDTO;

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
                        .typeIdx(1000L)
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
                        .adminSupportEntity(adminSupportEntity)
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

        // 모델 스케줄 등록
        adminScheduleEntity = adminScheduleJpaRepository.save(
                AdminScheduleEntity.builder()
                        .adminModelEntity(adminModelEntity)
                        .modelSchedule("모델 스케줄")
                        .modelScheduleTime(LocalDateTime.now())
                        .visible("Y")
                        .build());

        adminScheduleDTO = AdminScheduleEntity.toDto(adminScheduleEntity);
    }
}

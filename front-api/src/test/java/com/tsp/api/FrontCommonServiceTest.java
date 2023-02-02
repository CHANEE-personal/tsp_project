package com.tsp.api;

import com.tsp.api.common.domain.CommonImageDTO;
import com.tsp.api.common.domain.CommonImageEntity;
import com.tsp.api.common.domain.NewCodeEntity;
import com.tsp.api.common.service.FrontCommonImageJpaRepository;
import com.tsp.api.common.service.FrontCommonJpaRepository;
import com.tsp.api.festival.domain.FrontFestivalDTO;
import com.tsp.api.festival.domain.FrontFestivalEntity;
import com.tsp.api.festival.service.FrontFestivalJpaRepository;
import com.tsp.api.model.agency.service.FrontAgencyJpaRepository;
import com.tsp.api.model.domain.FrontModelDTO;
import com.tsp.api.model.domain.FrontModelEntity;
import com.tsp.api.model.domain.agency.FrontAgencyDTO;
import com.tsp.api.model.domain.agency.FrontAgencyEntity;
import com.tsp.api.model.domain.negotiation.FrontNegotiationDTO;
import com.tsp.api.model.domain.negotiation.FrontNegotiationEntity;
import com.tsp.api.model.negotiation.service.FrontNegotiationJpaRepository;
import com.tsp.api.model.service.FrontModelJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;

public abstract class FrontCommonServiceTest {

    @Autowired
    private FrontCommonJpaRepository frontCommonJpaRepository;
    @Autowired
    private FrontAgencyJpaRepository frontAgencyJpaRepository;
    @Autowired
    private FrontCommonImageJpaRepository frontCommonImageJpaRepository;
    @Autowired
    private FrontModelJpaRepository frontModelJpaRepository;
    @Autowired
    private FrontFestivalJpaRepository frontFestivalJpaRepository;
    @Autowired
    private FrontNegotiationJpaRepository frontNegotiationJpaRepository;
    protected NewCodeEntity newCodeEntity;
    protected FrontAgencyEntity frontAgencyEntity;
    protected FrontAgencyDTO frontAgencyDTO;
    protected CommonImageEntity commonImageEntity;
    protected CommonImageDTO commonImageDTO;
    protected FrontModelEntity frontModelEntity;
    protected FrontModelDTO frontModelDTO;
    protected FrontFestivalEntity frontFestivalEntity;
    protected FrontFestivalDTO frontFestivalDTO;
    protected FrontNegotiationEntity frontNegotiationEntity;
    protected FrontNegotiationDTO frontNegotiationDTO;

    @BeforeEach
    @EventListener(ApplicationContext.class)
    public void init() {
        saveData();
    }

    public void saveData() {
        // 공통 코드 등록
        newCodeEntity = frontCommonJpaRepository.save(
                NewCodeEntity.builder()
                        .categoryCd(1)
                        .categoryNm("men")
                        .visible("Y")
                        .cmmType("model")
                        .build());

        // 모델 소속사 등록
        frontAgencyEntity = frontAgencyJpaRepository.save(
                FrontAgencyEntity.builder()
                        .agencyName("agency")
                        .agencyDescription("agency")
                        .visible("Y")
                        .build());

        frontAgencyDTO = FrontAgencyEntity.toDto(frontAgencyEntity);

        // 모델 이미지 등록
        commonImageEntity = frontCommonImageJpaRepository.save(
                CommonImageEntity.builder()
                        .imageType("main")
                        .fileName("test.jpg")
                        .fileMask("test.jpg")
                        .filePath("/test/test.jpg")
                        .typeIdx(1L)
                        .typeName("MODEL")
                        .build());

        commonImageDTO = CommonImageEntity.toDto(commonImageEntity);

        // 모델 등록
        frontModelEntity = frontModelJpaRepository.save(
                FrontModelEntity.builder()
                        .newModelCodeJpaDTO(newCodeEntity)
                        .categoryAge(2)
                        .modelKorFirstName("조")
                        .modelKorSecondName("찬희")
                        .modelKorName("조찬희")
                        .modelFirstName("CHO")
                        .modelSecondName("CHANHEE")
                        .modelEngName("CHOCHANHEE")
                        .modelDescription("chaneeCho")
                        .modelFavoriteCount(1)
                        .modelViewCount(1)
                        .frontAgencyEntity(frontAgencyEntity)
                        .commonImageEntityList(List.of(commonImageEntity))
                        .modelMainYn("Y")
                        .newYn("N")
                        .height(170)
                        .size3("34-24-34")
                        .shoes(270)
                        .visible("Y")
                        .build());

        frontModelDTO = FrontModelEntity.toDto(frontModelEntity);

        // 축제 등록
        frontFestivalEntity = frontFestivalJpaRepository.save(
                FrontFestivalEntity.builder()
                        .festivalTitle("축제 제목")
                        .festivalDescription("축제 내용")
                        .festivalMonth(LocalDateTime.now().getMonthValue())
                        .festivalDay(LocalDateTime.now().getDayOfMonth())
                        .festivalTime(LocalDateTime.now())
                        .build());

        frontFestivalDTO = FrontFestivalEntity.toDto(frontFestivalEntity);

        // 모델 섭외 등록
        frontNegotiationEntity = frontNegotiationJpaRepository.save(
                FrontNegotiationEntity.builder()
                        .frontModelEntity(frontModelEntity)
                        .modelKorName(frontModelEntity.getModelKorName())
                        .modelNegotiationDesc("영화 프로젝트 참여")
                        .modelNegotiationDate(LocalDateTime.now())
                        .name("조찬희")
                        .phone("010-1234-5678")
                        .email("test@gmail.com")
                        .visible("Y")
                        .build());

        frontNegotiationDTO = FrontNegotiationEntity.toDto(frontNegotiationEntity);
    }
}

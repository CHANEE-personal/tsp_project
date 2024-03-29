package com.tsp.api.model.negotiation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.api.common.domain.NewCodeEntity;
import com.tsp.api.model.domain.FrontModelDTO;
import com.tsp.api.model.domain.FrontModelEntity;
import com.tsp.api.model.domain.agency.FrontAgencyEntity;
import com.tsp.api.model.domain.negotiation.FrontNegotiationEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.tsp.common.utils.StringUtil.getString;
import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.of;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("모델 섭외 Api Test")
class FrontNegotiationJpaApiControllerTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final WebApplicationContext wac;
    private final EntityManager em;
    private FrontModelEntity frontModelEntity;
    private FrontModelDTO frontModelDTO;

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void setup(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .alwaysDo(print())
                .build();

        createModel();
    }

    private void createModel() {
        FrontAgencyEntity frontAgencyEntity = FrontAgencyEntity.builder()
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
    }

    @Test
    @DisplayName("모델 섭외 조회 테스트")
    void 모델섭외조회Api테스트() throws Exception {
        LinkedMultiValueMap<String, String> negotiationMap = new LinkedMultiValueMap<>();
        mockMvc.perform(get("/api/negotiation").param("pageNum", "1").param("size", "3")
                        .queryParams(negotiationMap)
                        .queryParam("searchStartTime", of(now().getYear(), LocalDate.now().getMonth(), 1, 0, 0, 0, 0).format(ofPattern("yyyyMMdd")))
                        .queryParam("searchEndTime", of(now().getYear(), LocalDate.now().getMonth(), 30, 23, 59, 59).format(ofPattern("yyyyMMdd"))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @DisplayName("모델 섭외 상세 조회 테스트")
    void 모델섭외상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/negotiation/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("1"))
                .andExpect(jsonPath("$.modelIdx").value(1))
                .andExpect(jsonPath("$.modelNegotiationDesc").value("섭외 테스트"))
                .andExpect(jsonPath("$.visible").value("Y"));

    }

    @Test
    @DisplayName("모델 섭외 등록 테스트")
    void 모델섭외등록Api테스트() throws Exception {
        FrontNegotiationEntity frontNegotiationEntity = FrontNegotiationEntity.builder()
                .frontModelEntity(frontModelEntity)
                .modelKorName("조찬희")
                .modelNegotiationDesc("영화 프로젝트 참여")
                .modelNegotiationDate(now())
                .name("조찬희")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        mockMvc.perform(post("/api/model/{idx}/negotiation", frontModelEntity.getIdx())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(frontNegotiationEntity)))
                .andDo(print())
                .andDo(document("negotiation/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("modelNegotiationDesc").type(STRING).description("영화 프로젝트 참여")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("modelNegotiationDesc").type(STRING).description("영화 프로젝트 참여")
                        )))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.modelNegotiationDesc").value("영화 프로젝트 참여"));
    }

    @Test
    @DisplayName("모델 섭외 수정 테스트")
    void 모델섭외수정Api테스트() throws Exception {
        FrontNegotiationEntity frontNegotiationEntity = FrontNegotiationEntity.builder()
                .frontModelEntity(frontModelEntity)
                .modelKorName("조찬희")
                .modelNegotiationDesc("영화 프로젝트 참여")
                .modelNegotiationDate(now())
                .name("조찬희")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        em.persist(frontNegotiationEntity);

        FrontNegotiationEntity newFrontNegotiationEntity = FrontNegotiationEntity.builder()
                .idx(frontNegotiationEntity.getIdx())
                .frontModelEntity(frontModelEntity)
                .modelKorName("테스트")
                .modelNegotiationDesc("섭외 수정 테스트")
                .modelNegotiationDate(LocalDateTime.now())
                .name("테스트")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        mockMvc.perform(put("/api/negotiation/{idx}", newFrontNegotiationEntity.getIdx())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newFrontNegotiationEntity)))
                .andDo(print())
                .andDo(document("negotiation/put",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("modelNegotiationDesc").type(STRING).description("섭외 수정 테스트")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("modelNegotiationDesc").type(STRING).description("섭외 수정 테스트")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.modelNegotiationDesc").value("섭외 수정 테스트"));
    }

    @Test
    @DisplayName("모델 섭외 삭제 테스트")
    void 모델섭외삭제Api테스트() throws Exception {
        FrontNegotiationEntity frontNegotiationEntity = FrontNegotiationEntity.builder()
                .frontModelEntity(frontModelEntity)
                .modelKorName("조찬희")
                .modelNegotiationDesc("영화 프로젝트 참여")
                .modelNegotiationDate(now())
                .name("조찬희")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        em.persist(frontNegotiationEntity);

        mockMvc.perform(delete("/api/negotiation/{idx}", frontNegotiationEntity.getIdx()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(frontNegotiationEntity.getIdx())));
    }
}

package com.tsp.api.model.agency.controller;

import com.tsp.api.model.domain.agency.FrontAgencyEntity;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@TestPropertySource(locations = "classpath:application-local.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("Agency Api Test")
class FrontAgencyJpaApiControllerTest {
    private MockMvc mockMvc;
    private final WebApplicationContext wac;

    private FrontAgencyEntity frontAgencyEntity;
    private final EntityManager em;

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void setup(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .alwaysDo(print())
                .build();

        createFaq();
    }

    @DisplayName("테스트 FAQ 생성")
    void createFaq() {

        // faq 생성
        frontAgencyEntity = FrontAgencyEntity.builder()
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

        em.persist(frontAgencyEntity);
    }

    @Test
    @DisplayName("Agency 조회 테스트")
    void Agency조회테스트() throws Exception {
        mockMvc.perform(get("/api/agency").param("pageNum", "0").param("size", "100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    @DisplayName("Agency 상세 조회 테스트")
    void Agency상세조회테스트() throws Exception {
        // 사용
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/agency/{idx}", frontAgencyEntity.getIdx()))
                .andDo(print())
                .andDo(document("agency/get", pathParameters(
                        parameterWithName("idx").description("AGENCY IDX")
                )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value(frontAgencyEntity.getIdx()))
                .andExpect(jsonPath("$.agencyName").value(frontAgencyEntity.getAgencyName()))
                .andExpect(jsonPath("$.agencyDescription").value(frontAgencyEntity.getAgencyDescription()));

        // 예외
        mockMvc.perform(get("/api/agency/-1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.code").value("NOT_FOUND_AGENCY"))
                .andExpect(jsonPath("$.message").value("해당 Agency 없음"));
    }

    @Test
    @DisplayName("이전 Agency 상세 조회 테스트")
    void 이전Agency상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/agency/2/prev"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("1"));
    }

    @Test
    @DisplayName("다음 Agency 상세 조회 테스트")
    void 다음Agency상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/agency/2/next"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("3"));
    }

    @Test
    @Transactional
    @DisplayName("Agency 좋아요 테스트")
    void Agency좋아요테스트() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/agency/{idx}/like", frontAgencyEntity.getIdx()))
                .andDo(print())
                .andDo(document("agency/put/favorite", pathParameters(
                        parameterWithName("idx").description("AGENCY IDX")
                )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }
}

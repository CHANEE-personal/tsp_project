package com.tsp.new_tsp_front.api.model.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static com.tsp.new_tsp_front.common.utils.StringUtil.getString;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-local.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace= NONE)
@DisplayName("모델 Api Test")
class FrontModelJpaApiControllerTest {
    private MockMvc mockMvc;
    private final WebApplicationContext wac;

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void setup() {
        this.mockMvc = webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .alwaysExpect(status().isOk())
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("모델 조회 테스트")
    void 모델조회() throws Exception {
        mockMvc.perform(get("/api/model/lists/1").param("page", "1").param("size", "100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.modelList.length()", greaterThan(0)));
    }

    @Test
    @DisplayName("모델 조회 예외 테스트")
    void 모델조회예외테스트() throws Exception {
        mockMvc.perform(get("/api/model/lists/-1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString().equals("모델 categoryCd는 1~3 사이 값만 입력할 수 있습니다.");
    }

    @Test
    @DisplayName("모델 배너 조회 테스트")
    void 모델배너조회() throws Exception {
        mockMvc.perform(get("/api/model/lists/main"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.modelList.length()", greaterThan(0)));
    }

    @Test
    @DisplayName("남성 모델 상세 조회 테스트")
    void 남성모델상세조회() throws Exception {
        // 사용
        mockMvc.perform(get("/api/model/1/156"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idx").value("156"))
                .andExpect(jsonPath("$.categoryCd").value("1"))
                .andExpect(jsonPath("$.modelFirstName").value("Joo"))
                .andExpect(jsonPath("$.modelSecondName").value("seon woo"))
                .andExpect(jsonPath("$.modelKorFirstName").value("주"))
                .andExpect(jsonPath("$.modelKorSecondName").value("선우"))
                .andExpect(jsonPath("$.height").value("181"))
                .andExpect(jsonPath("$.size3").value("31-24-34"))
                .andExpect(jsonPath("$.shoes").value("275"));

        // 예외
        mockMvc.perform(get("/api/model/1/-1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.code").value("NOT_FOUND_MODEL"))
                .andExpect(jsonPath("$.message").value("해당 모델 없음"));
    }

    @Test
    @DisplayName("여성 모델 상세 조회 테스트")
    void 여성모델상세조회() throws Exception {
        // 사용
        mockMvc.perform(get("/api/model/2/143"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idx").value("143"))
                .andExpect(jsonPath("$.categoryCd").value("2"))
                .andExpect(jsonPath("$.modelFirstName").value("kim"))
                .andExpect(jsonPath("$.modelSecondName").value("ye yeong"))
                .andExpect(jsonPath("$.modelKorFirstName").value("김"))
                .andExpect(jsonPath("$.modelKorSecondName").value("예영"))
                .andExpect(jsonPath("$.height").value("173"))
                .andExpect(jsonPath("$.size3").value("31-24-34"))
                .andExpect(jsonPath("$.shoes").value("240"))
                .andExpect(jsonPath("$.modelMainYn").value("Y"));

        // 예외
        mockMvc.perform(get("/api/model/2/-1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.code").value("NOT_FOUND_MODEL"))
                .andExpect(jsonPath("$.message").value("해당 모델 없음"));
    }

    @Test
    @DisplayName("시니어 모델 상세 조회 테스트")
    void 시니어모델상세조회() throws Exception {
        // 사용
        mockMvc.perform(get("/api/model/3/12"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idx").value("12"))
                .andExpect(jsonPath("$.categoryCd").value("3"))
                .andExpect(jsonPath("$.modelFirstName").value("cho"))
                .andExpect(jsonPath("$.modelSecondName").value("chanhee"))
                .andExpect(jsonPath("$.modelKorFirstName").value("조"))
                .andExpect(jsonPath("$.modelKorSecondName").value("찬희"))
                .andExpect(jsonPath("$.height").value("170"))
                .andExpect(jsonPath("$.size3").value("31-24-31"))
                .andExpect(jsonPath("$.shoes").value("220"))
                .andExpect(jsonPath("$.modelMainYn").value("Y"));

        // 예외
        mockMvc.perform(get("/api/model/3/-1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.code").value("NOT_FOUND_MODEL"))
                .andExpect(jsonPath("$.message").value("해당 모델 없음"));
    }

    @Test
    @DisplayName("이전 모델 상세 조회 테스트")
    void 이전모델상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/model/2/145/prev"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("144"))
                .andExpect(jsonPath("$.categoryCd").value("2"));
    }

    @Test
    @DisplayName("다음 모델 상세 조회 테스트")
    void 다음모델상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/model/2/145/next"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("147"))
                .andExpect(jsonPath("$.categoryCd").value("2"));
    }

    @Test
    @Transactional
    @DisplayName("모델 좋아요 테스트")
    void 모델좋아요테스트() throws Exception {
        mockMvc.perform(put("/api/model/1/156/like"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(1)));
    }

    @Test
    @DisplayName("새로운 모델 조회 테스트")
    void 새로운모델조회() throws Exception {
        mockMvc.perform(get("/api/model/lists/new/1").param("page", "1").param("size", "100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.newModelList.length()", greaterThan(0)));
    }
}
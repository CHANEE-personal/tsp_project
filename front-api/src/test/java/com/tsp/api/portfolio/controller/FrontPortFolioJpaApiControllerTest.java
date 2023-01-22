package com.tsp.api.portfolio.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
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
@DisplayName("포트폴리오 Api Test")
class FrontPortFolioJpaApiControllerTest {
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
    @DisplayName("포트폴리오 조회 테스트")
    void 포트폴리오조회() throws Exception {
        mockMvc.perform(get("/api/portfolio/lists").param("pageNum", "1").param("size", "100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    @Disabled
    @DisplayName("포트폴리오 검색 조회 테스트")
    void 포트폴리오검색조회() throws Exception {
        LinkedMultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("searchType", "0");
        paramMap.add("searchKeyword", "하하");

        mockMvc.perform(get("/api/portfolio/lists").queryParams(paramMap).param("pageNum", "1").param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    @DisplayName("포트폴리오 상세 조회 테스트")
    void 포트폴리오상세조회() throws Exception {
        mockMvc.perform(get("/api/portfolio/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.title").value("포트폴리오 테스트"))
                .andExpect(jsonPath("$.description").value("포트폴리오 테스트"))
                .andExpect(jsonPath("$.hashTag").value("TEST"))
                .andExpect(jsonPath("$.videoUrl").value("http://youtube.com"));

        // 예외
        mockMvc.perform(get("/api/portfolio/-1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.code").value("NOT_FOUND_PORTFOLIO"))
                .andExpect(jsonPath("$.message").value("해당 포트폴리오 없음"));
    }

    @Test
    @DisplayName("이전 포트폴리오 상세 조회 테스트")
    void 이전포트폴리오상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/portfolio/2/prev"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("1"));
    }

    @Test
    @DisplayName("다음 포트폴리오 상세 조회 테스트")
    void 다음포트폴리오상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/portfolio/2/next"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("3"));
    }
}

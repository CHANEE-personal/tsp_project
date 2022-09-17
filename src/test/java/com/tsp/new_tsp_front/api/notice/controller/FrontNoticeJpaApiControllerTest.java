package com.tsp.new_tsp_front.api.notice.controller;

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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-local.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("공지사항 Api Test")
class FrontNoticeJpaApiControllerTest {
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
    @DisplayName("공지사항 조회 테스트")
    void 공지사항조회테스트() throws Exception {
        mockMvc.perform(get("/api/notice/lists").param("page", "1").param("size", "100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.noticeList.length()", equalTo(55)));
    }

    @Test
    @DisplayName("공지사항 검색 조회 테스트")
    void 공지사항검색조회테스트() throws Exception {
        LinkedMultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("jpaStartPage", "1");
        paramMap.add("size", "3");
        paramMap.add("searchType", "0");
        paramMap.add("searchKeyword", "하하");

        mockMvc.perform(get("/api/notice/lists").queryParams(paramMap))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.noticeList.length()", equalTo(1)));
    }

    @Test
    @DisplayName("공지사항 상세 조회 테스트")
    void 공지사항상세조회테스트() throws Exception {
        // 사용
        mockMvc.perform(get("/api/notice/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("1"))
                .andExpect(jsonPath("$.title").value("공지사항 테스트"))
                .andExpect(jsonPath("$.description").value("공지사항 테스트"));

        // 예외
        mockMvc.perform(get("/api/notice/-1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.code").value("NOT_FOUND_NOTICE"))
                .andExpect(jsonPath("$.message").value("해당 공지사항 없음"));
    }

    @Test
    @DisplayName("이전 공지사항 상세 조회 테스트")
    void 이전공지사항상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/notice/2/prev"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("1"));
    }

    @Test
    @DisplayName("다음 공지사항 상세 조회 테스트")
    void 다음공지사항상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/notice/2/next"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("3"));
    }
}
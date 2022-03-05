package com.tsp.new_tsp_front.api.portfolio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class FrontPortFolioJpaApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("포트폴리오 조회 테스트")
    public void 포트폴리오조회() throws Exception {
        mockMvc.perform(get("/api/portfolio/lists").param("page", "1").param("size", "100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.portFolioList.length()", equalTo(2)));
    }

    @Test
    @DisplayName("포트폴리오 상세 조회 테스트")
    public void 포트폴리오상세조회() throws Exception {
        mockMvc.perform(get("/api/portfolio/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.portFolioInfo.portFolioMap.title").value("포트폴리오 테스트"))
                .andExpect(jsonPath("$.portFolioInfo.portFolioMap.description").value("포트폴리오 테스트"))
                .andExpect(jsonPath("$.portFolioInfo.portFolioMap.hashTag").value("TEST"))
                .andExpect(jsonPath("$.portFolioInfo.portFolioMap.videoUrl").value("http://youtube.com"));

        // 미사용
        mockMvc.perform(get("/api/portfolio/-1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("NOT_FOUND_PORTFOLIO"));
    }
}
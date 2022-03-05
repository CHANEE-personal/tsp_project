package com.tsp.new_tsp_front.api.production.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioDTO;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionDTO;
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
class FrontProductionJpaApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("프로덕션 조회 테스트")
    public void 프로덕션조회() throws Exception {
        mockMvc.perform(get("/api/production/lists").param("page", "1").param("size", "100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productionList.length()", equalTo(55)));
    }

    @Test
    @DisplayName("프로덕션 상세 조회 테스트")
    public void 프로덕션상세조회() throws Exception {
        // 사용
        mockMvc.perform(get("/api/production/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productionInfo.productionInfo.idx").value("1"))
                .andExpect(jsonPath("$.productionInfo.productionInfo.title").value("프로덕션 테스트"))
                .andExpect(jsonPath("$.productionInfo.productionInfo.description").value("프로덕션 테스트"));

        // 미사용
        mockMvc.perform(get("/api/production/39"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("NOT_FOUND_PRODUCTION"));
    }
}
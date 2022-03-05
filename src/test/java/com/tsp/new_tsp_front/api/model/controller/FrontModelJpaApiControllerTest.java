package com.tsp.new_tsp_front.api.model.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class FrontModelJpaApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("모델 조회 테스트")
    public void 모델조회() throws Exception {
        mockMvc.perform(get("/api/model/lists/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelList.length()", equalTo(9)));
    }

    @Test
    @DisplayName("남성 모델 상세 조회 테스트")
    public void 남성모델상세조회() throws Exception {
        // 사용
        mockMvc.perform(get("/api/model/1/3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.idx").value("3"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.categoryCd").value("1"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.modelFirstName").value("CHO"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.modelSecondName").value("CHAN HEE"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.modelKorFirstName").value("조"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.modelKorSecondName").value("찬희"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.height").value("170"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.size3").value("34-24-34"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.shoes").value("270"));

        // 미사용
        mockMvc.perform(get("/api/model/1/-1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("NOT_FOUND_MODEL"));
    }

    @Test
    @DisplayName("여성 모델 상세 조회 테스트")
    public void 여성모델상세조회() throws Exception {
        // 사용
        mockMvc.perform(get("/api/model/2/6"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.idx").value("6"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.categoryCd").value("2"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.modelFirstName").value("KIM"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.modelSecondName").value("YE YOUNG"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.modelKorFirstName").value("김"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.modelKorSecondName").value("예영"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.height").value("170"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.size3").value("31-21-31"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.shoes").value("220"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.modelMainYn").value("Y"));

        // 미사용
        mockMvc.perform(get("/api/model/2/-1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("NOT_FOUND_MODEL"));
    }

    @Test
    @DisplayName("시니어 모델 상세 조회 테스트")
    public void 시니어모델상세조회() throws Exception {
        // 사용
        mockMvc.perform(get("/api/model/3/12"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.idx").value("12"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.categoryCd").value("3"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.modelFirstName").value("cho"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.modelSecondName").value("chanhee"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.modelKorFirstName").value("조"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.modelKorSecondName").value("찬희"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.height").value("170"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.size3").value("31-24-31"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.shoes").value("220"))
                .andExpect(jsonPath("$.modelInfoMap.modelInfo.modelMainYn").value("Y"));

        // 미사용
        mockMvc.perform(get("/api/model/3/-1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("NOT_FOUND_MODEL"));
    }
}
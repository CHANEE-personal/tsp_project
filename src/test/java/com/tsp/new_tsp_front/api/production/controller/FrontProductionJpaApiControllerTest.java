package com.tsp.new_tsp_front.api.production.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioDTO;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureRestDocs(
        outputDir = "target/snippets",
        uriScheme = "http",
        uriHost = "localhost",
        uriPort = 80
)
class FrontProductionJpaApiControllerTest {
    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    public void 프로덕션조회() throws Exception {
        FrontProductionDTO frontProductionDTO = new FrontProductionDTO();


        // given

        // when

        // then
    }
}
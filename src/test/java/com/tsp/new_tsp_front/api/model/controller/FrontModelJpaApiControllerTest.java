package com.tsp.new_tsp_front.api.model.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
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
class FrontModelJpaApiControllerTest {

    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    public void 모델조회() throws Exception {
        FrontModelDTO frontModelDTO = new FrontModelDTO();
        

        // given

        // when

        // then
    }
}
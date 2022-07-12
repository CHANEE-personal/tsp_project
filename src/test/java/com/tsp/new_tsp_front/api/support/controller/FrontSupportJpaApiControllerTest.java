package com.tsp.new_tsp_front.api.support.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_front.api.support.domain.FrontSupportEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace= NONE)
class FrontSupportJpaApiControllerTest {
	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;
	@Autowired private WebApplicationContext wac;

	@BeforeEach
	@EventListener(ApplicationReadyEvent.class)
	public void setup() {
		this.mockMvc = webAppContextSetup(wac)
				.addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
				.alwaysDo(print())
				.build();
	}

	@Test
	@DisplayName("모델 지원하기 테스트")
	void 모델지원Api테스트() throws Exception {
		FrontSupportEntity frontSupportEntity = FrontSupportEntity.builder()
				.supportName("조찬희")
				.supportMessage("조찬희")
				.supportHeight(170)
				.supportPhone("010-9466-2702")
				.supportInstagram("https://instagram.com")
				.supportSize3("31-24-31")
				.build();

		mockMvc.perform(post("/api/support")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(frontSupportEntity)))
                .andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.supportName").value("조찬희"))
				.andExpect(jsonPath("$.supportMessage").value("조찬희"))
				.andExpect(jsonPath("$.supportHeight").value(170))
				.andExpect(jsonPath("$.supportPhone").value("010-9466-2702"))
				.andExpect(jsonPath("$.supportInstagram").value("https://instagram.com"))
				.andExpect(jsonPath("$.supportSize3").value("31-24-31"));
	}
}
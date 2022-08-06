package com.tsp.new_tsp_front.api.support.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_front.api.support.domain.FrontSupportEntity;
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

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-local.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace= NONE)
@DisplayName("지원모델 Api Test")
class FrontSupportJpaApiControllerTest {
	private MockMvc mockMvc;
	private final ObjectMapper objectMapper;
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
	@DisplayName("모델 지원하기 테스트")
	void 모델지원Api테스트() throws Exception {
		FrontSupportEntity frontSupportEntity = FrontSupportEntity.builder()
				.supportName("조찬희")
				.supportMessage("조찬희")
				.supportHeight(170)
				.supportPhone("010-9466-2702")
				.supportInstagram("https://instagram.com")
				.supportSize3("31-24-31")
				.visible("Y")
				.build();

		mockMvc.perform(post("/api/support")
				.contentType(APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(frontSupportEntity)))
                .andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=utf-8"))
				.andExpect(jsonPath("$.supportName").value("조찬희"))
				.andExpect(jsonPath("$.supportMessage").value("조찬희"))
				.andExpect(jsonPath("$.supportHeight").value(170))
				.andExpect(jsonPath("$.supportPhone").value("010-9466-2702"))
				.andExpect(jsonPath("$.supportInstagram").value("https://instagram.com"))
				.andExpect(jsonPath("$.supportSize3").value("31-24-31"))
				.andExpect(jsonPath("$.visible").value("Y"));
	}
}
package com.tsp.api.festival;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.api.festival.domain.AdminFestivalEntity;
import com.tsp.api.user.domain.AdminUserEntity;
import com.tsp.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.tsp.api.user.domain.Role.ROLE_ADMIN;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("행사 Api Test")
class AdminFestivalJpaControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final WebApplicationContext wac;
    private final EntityManager em;
    private final JwtUtil jwtUtil;

    private AdminFestivalEntity adminFestivalEntity;
    private AdminUserEntity adminUserEntity;

    Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return authorities;
    }

    @DisplayName("테스트 유저 생성")
    void createUser() {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("admin04", "pass1234", getAuthorities());

        adminUserEntity = AdminUserEntity.builder()
                .userId("admin05")
                .password("pass1234")
                .name("test")
                .email("test@test.com")
                .role(ROLE_ADMIN)
                .userToken(jwtUtil.doGenerateToken(authenticationToken.getName()))
                .visible("Y")
                .build();

        em.persist(adminUserEntity);
    }

    @DisplayName("테스트 행사 생성")
    void createFestival() {
        // user 생성
        createUser();

        // festival 생성
        LocalDateTime dateTime = LocalDateTime.now();

        adminFestivalEntity = AdminFestivalEntity.builder()
                .festivalTitle("축제 제목")
                .festivalDescription("축제 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        em.persist(adminFestivalEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void setup(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .alwaysDo(print())
                .build();

        createFestival();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 행사 조회 테스트")
    void 행사조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/festival").param("pageNum", "1").param("size", "3")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 행사 상세 조회 테스트")
    void 행사상세조회Api테스트() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/festival/{idx}", adminFestivalEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andDo(document("festival/get", pathParameters(
                        parameterWithName("idx").description("축제 IDX")
                )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value(adminFestivalEntity.getIdx()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 행사 등록 테스트")
    void 행사등록Api테스트() throws Exception {
        LocalDateTime dateTime = LocalDateTime.now();

        AdminFestivalEntity insertFestivalEntity = AdminFestivalEntity.builder()
                .festivalTitle("축제 등록 제목")
                .festivalDescription("축제 등록 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/festival")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(insertFestivalEntity)))
                .andDo(print())
                .andDo(document("festival/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("festivalTitle").type(STRING).description("축제 제목"),
                                fieldWithPath("festivalDescription").type(STRING).description("축제 상세"),
                                fieldWithPath("festivalMonth").type(NUMBER).description("축제 월"),
                                fieldWithPath("festivalDay").type(NUMBER).description("축제 일"),
                                fieldWithPath("festivalTime").type(STRING).description("축제 일자")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("festivalTitle").type(STRING).description("축제 제목"),
                                fieldWithPath("festivalDescription").type(STRING).description("축제 상세"),
                                fieldWithPath("festivalMonth").type(NUMBER).description("축제 월"),
                                fieldWithPath("festivalDay").type(NUMBER).description("축제 일"),
                                fieldWithPath("festivalTime").type(STRING).description("축제 일자")
                        )))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.festivalTitle").value(insertFestivalEntity.getFestivalTitle()))
                .andExpect(jsonPath("$.festivalDescription").value(insertFestivalEntity.getFestivalDescription()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 행사 수정 테스트")
    void 행사수정Api테스트() throws Exception {
        LocalDateTime dateTime = LocalDateTime.now();

        AdminFestivalEntity updateFestivalEntity = AdminFestivalEntity.builder()
                .idx(adminFestivalEntity.getIdx())
                .festivalTitle("축제 수정 제목")
                .festivalDescription("축제 수정 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/festival/{idx}", adminFestivalEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateFestivalEntity)))
                .andDo(print())
                .andDo(document("festival/put",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("festivalTitle").type(STRING).description("축제 제목"),
                                fieldWithPath("festivalDescription").type(STRING).description("축제 상세"),
                                fieldWithPath("festivalMonth").type(NUMBER).description("축제 월"),
                                fieldWithPath("festivalDay").type(NUMBER).description("축제 일"),
                                fieldWithPath("festivalTime").type(STRING).description("축제 일자")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("festivalTitle").type(STRING).description("축제 제목"),
                                fieldWithPath("festivalDescription").type(STRING).description("축제 상세"),
                                fieldWithPath("festivalMonth").type(NUMBER).description("축제 월"),
                                fieldWithPath("festivalDay").type(NUMBER).description("축제 일"),
                                fieldWithPath("festivalTime").type(STRING).description("축제 일자")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.festivalTitle").value("축제 수정 제목"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 행사 삭제 테스트")
    void 행사삭제Api테스트() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/festival/{idx}", adminFestivalEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andDo(document("festival/delete", pathParameters(
                        parameterWithName("idx").description("축제 IDX")
                )))
                .andExpect(status().isNoContent());
    }
}

package com.tsp.api.portfolio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.api.domain.portfolio.AdminPortFolioEntity;
import com.tsp.api.domain.user.AdminUserEntity;
import com.tsp.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.tsp.api.domain.user.Role.ROLE_ADMIN;
import static com.tsp.common.StringUtil.getString;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;
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
@DisplayName("포트폴리오 Api Test")
class AdminPortfolioJpaControllerTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final WebApplicationContext wac;
    private final EntityManager em;
    private final JwtUtil jwtUtil;

    private AdminPortFolioEntity adminPortFolioEntity;
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
                .userId("admin04")
                .password("pass1234")
                .name("test")
                .email("test@test.com")
                .role(ROLE_ADMIN)
                .userToken(jwtUtil.doGenerateToken(authenticationToken.getName()))
                .visible("Y")
                .build();

        em.persist(adminUserEntity);
    }

    @DisplayName("테스트 포트폴리오 생성")
    void createPortfolio() {
        // user 생성
        createUser();

        // portfolio 생성
        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();
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

        createPortfolio();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 포트폴리오 조회 테스트")
    void 포트폴리오조회Api테스트() throws Exception {
        MultiValueMap<String, String> portfolioMap = new LinkedMultiValueMap<>();
        mockMvc.perform(get("/api/portfolio").params(portfolioMap).param("pageNum", "1").param("size", "3")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.portfolioList.length()", greaterThan(0)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 포트폴리오 검색조회 테스트")
    void 포트폴리오검색조회Api테스트() throws Exception {
        MultiValueMap<String, String> portfolioMap = new LinkedMultiValueMap<>();
        portfolioMap.add("searchType", "0");
        portfolioMap.add("searchKeyword", "하하");
        mockMvc.perform(get("/api/portfolio").queryParams(portfolioMap).param("pageNum", "1").param("size", "3")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.portfolioList.length()", greaterThan(0)));
    }

    @Test
    @Disabled
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 포트폴리오 조회 권한 테스트")
    void 포트폴리오조회Api권한테스트() throws Exception {
        MultiValueMap<String, String> portfolioMap = new LinkedMultiValueMap<>();

        mockMvc.perform(get("/api/portfolio").params(portfolioMap).param("pageNum", "1").param("size", "3")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 포트폴리오 상세 조회 테스트")
    void 포트폴리오상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/portfolio/1")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("1"));
    }

    @Test
    @Disabled
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 포트폴리오 상세 조회 권한 테스트")
    void 포트폴리오상세조회Api권한테스트() throws Exception {
        mockMvc.perform(get("/api/portfolio/1")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 이전 포트폴리오 상세 조회 테스트")
    void 이전포트폴리오상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/portfolio/2/prev")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("1"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 다음 포트폴리오 상세 조회 테스트")
    void 다음포트폴리오상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/portfolio/2/next")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("3"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 포트폴리오 등록 테스트")
    void 포트폴리오등록Api테스트() throws Exception {
        mockMvc.perform(post("/api/portfolio")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminPortFolioEntity)))
                .andDo(print())
                .andDo(document("portfolio/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("title").type(STRING).description("제목"),
                                fieldWithPath("description").type(STRING).description("상세"),
                                fieldWithPath("visible").type(STRING).description("노출 여부"),
                                fieldWithPath("hashTag").type(STRING).description("hashTag"),
                                fieldWithPath("videoUrl").type(STRING).description("videoUrl")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("title").type(STRING).description("제목"),
                                fieldWithPath("description").type(STRING).description("상세"),
                                fieldWithPath("visible").type(STRING).description("노출 여부"),
                                fieldWithPath("hashTag").type(STRING).description("hashTag"),
                                fieldWithPath("videoUrl").type(STRING).description("videoUrl")
                        )))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.categoryCd").value(1))
                .andExpect(jsonPath("$.title").value("포트폴리오 테스트"))
                .andExpect(jsonPath("$.description").value("포트폴리오 테스트"))
                .andExpect(jsonPath("$.hashTag").value("#test"))
                .andExpect(jsonPath("$.videoUrl").value("https://youtube.com"));
    }

    @Test
    @Disabled
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 포트폴리오 등록 권한 테스트")
    void 포트폴리오등록Api권한테스트() throws Exception {
        mockMvc.perform(post("/api/portfolio")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminPortFolioEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 포트폴리오 수정 테스트")
    void 포트폴리오수정Api테스트() throws Exception {
        em.persist(adminPortFolioEntity);

        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(adminPortFolioEntity.getIdx())
                .categoryCd(1)
                .title("포트폴리오 테스트1111")
                .description("포트폴리오 테스트1111")
                .hashTag("#test111")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        mockMvc.perform(put("/api/portfolio/{idx}", adminPortFolioEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminPortFolioEntity)))
                .andDo(print())
                .andDo(document("portfolio/put",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("title").type(STRING).description("제목"),
                                fieldWithPath("description").type(STRING).description("상세"),
                                fieldWithPath("visible").type(STRING).description("노출 여부"),
                                fieldWithPath("hashTag").type(STRING).description("hashTag"),
                                fieldWithPath("videoUrl").type(STRING).description("videoUrl")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("title").type(STRING).description("제목"),
                                fieldWithPath("description").type(STRING).description("상세"),
                                fieldWithPath("visible").type(STRING).description("노출 여부"),
                                fieldWithPath("hashTag").type(STRING).description("hashTag"),
                                fieldWithPath("videoUrl").type(STRING).description("videoUrl")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.categoryCd").value(1))
                .andExpect(jsonPath("$.title").value("포트폴리오 테스트1111"))
                .andExpect(jsonPath("$.description").value("포트폴리오 테스트1111"))
                .andExpect(jsonPath("$.hashTag").value("#test111"))
                .andExpect(jsonPath("$.videoUrl").value("https://youtube.com"));
    }

    @Test
    @Disabled
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 포트폴리오 수정 권한 테스트")
    void 포트폴리오수정Api권한테스트() throws Exception {
        em.persist(adminPortFolioEntity);

        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(adminPortFolioEntity.getIdx())
                .categoryCd(1)
                .title("포트폴리오 테스트1111")
                .description("포트폴리오 테스트1111")
                .hashTag("#test111")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        mockMvc.perform(put("/api/portfolio/{idx}", adminPortFolioEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminPortFolioEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 포트폴리오 삭제 테스트")
    void 포트폴리오삭제Api테스트() throws Exception {
        em.persist(adminPortFolioEntity);

        mockMvc.perform(delete("/api/portfolio/{idx}", adminPortFolioEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(adminPortFolioEntity.getIdx())));
    }

    @Test
    @Disabled
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 포트폴리오 삭제 권한 테스트")
    void 포트폴리오삭제Api권한테스트() throws Exception {
        em.persist(adminPortFolioEntity);

        mockMvc.perform(delete("/api/portfolio/{idx}", adminPortFolioEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}

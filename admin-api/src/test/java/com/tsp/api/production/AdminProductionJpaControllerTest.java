package com.tsp.api.production;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.api.production.domain.AdminProductionEntity;
import com.tsp.api.user.domain.AdminUserEntity;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.tsp.api.user.domain.Role.ROLE_ADMIN;
import static com.tsp.common.StringUtil.getString;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
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
@DisplayName("프로덕션 Api Test")
class AdminProductionJpaControllerTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final WebApplicationContext wac;
    private final EntityManager em;
    private final JwtUtil jwtUtil;

    private AdminProductionEntity adminProductionEntity;
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

    @DisplayName("테스트 프로덕션 생성")
    void createProduction() {
        // user 생성
        createUser();

        // production 생성
        adminProductionEntity = AdminProductionEntity.builder()
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
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

        createProduction();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 프로덕션 조회 테스트")
    void 프로덕션조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/production").param("pageNum", "1").param("size", "3")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 프로덕션 검색 조회 테스트")
    void 프로덕션검색조회Api테스트() throws Exception {
        // 검색 테스트
        LinkedMultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("searchType", "0");
        paramMap.add("searchKeyword", "하하");

        mockMvc.perform(get("/api/production").queryParams(paramMap).param("pageNum", "1").param("size", "3")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @Disabled
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 프로덕션 조회 권한 테스트")
    void 프로덕션조회Api권한테스트() throws Exception {
        mockMvc.perform(get("/api/production")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 프로덕션 상세 조회 테스트")
    void 프로덕션상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/production/1")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("1"))
                .andExpect(jsonPath("$.title").value("테스트1"))
                .andExpect(jsonPath("$.description").value("테스트1"));
    }

    @Test
    @Disabled
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 프로덕션 상세 조회 권한 테스트")
    void 프로덕션상세조회Api권한테스트() throws Exception {
        mockMvc.perform(get("/api/production/1")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 이전 프로덕션 상세 조회 테스트")
    void 이전프로덕션상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/production/118/prev")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("117"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 다음 프로덕션 상세 조회 테스트")
    void 다음프로덕션상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/production/118/next")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("119"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 프로덕션 등록 테스트")
    void 프로덕션등록Api테스트() throws Exception {
        mockMvc.perform(post("/api/production")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminProductionEntity)))
                .andDo(print())
                .andDo(document("production/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("title").type(STRING).description("제목"),
                                fieldWithPath("description").type(STRING).description("상세"),
                                fieldWithPath("visible").type(STRING).description("노출 여부")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("title").type(STRING).description("제목"),
                                fieldWithPath("description").type(STRING).description("상세"),
                                fieldWithPath("visible").type(STRING).description("노출 여부")
                        )))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.title").value("프로덕션 테스트"))
                .andExpect(jsonPath("$.description").value("프로덕션 테스트"));
    }

    @Test
    @Disabled
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 프로덕션 등록 권한 테스트")
    void 프로덕션등록Api권한테스트() throws Exception {
        mockMvc.perform(post("/api/production")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminProductionEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 프로덕션 수정 테스트")
    void 프로덕션수정Api테스트() throws Exception {
        em.persist(adminProductionEntity);

        adminProductionEntity = AdminProductionEntity.builder().idx(adminProductionEntity.getIdx()).title("테스트1").description("테스트1").visible("Y").build();

        mockMvc.perform(put("/api/production/{idx}", adminProductionEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminProductionEntity)))
                .andDo(print())
                .andDo(document("production/put",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("title").type(STRING).description("제목"),
                                fieldWithPath("description").type(STRING).description("상세"),
                                fieldWithPath("visible").type(STRING).description("노출 여부")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("title").type(STRING).description("제목"),
                                fieldWithPath("description").type(STRING).description("상세"),
                                fieldWithPath("visible").type(STRING).description("노출 여부")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.title").value("테스트1"))
                .andExpect(jsonPath("$.description").value("테스트1"));
    }

    @Test
    @Disabled
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 프로덕션 수정 권한 테스트")
    void 프로덕션수정Api권한테스트() throws Exception {
        em.persist(adminProductionEntity);

        adminProductionEntity = AdminProductionEntity.builder().idx(adminProductionEntity.getIdx()).title("테스트1").description("테스트1").build();

        mockMvc.perform(put("/api/production/{idx}", adminProductionEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminProductionEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 프로덕션 삭제 테스트")
    void 프로덕션삭제Api테스트() throws Exception {
        em.persist(adminProductionEntity);

        mockMvc.perform(delete("/api/production/{idx}", adminProductionEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(adminProductionEntity.getIdx())));
    }

    @Test
    @Disabled
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 프로덕션 삭제 권한 테스트")
    void 프로덕션삭제Api권한테스트() throws Exception {
        em.persist(adminProductionEntity);

        mockMvc.perform(delete("/api/production/{idx}", adminProductionEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}

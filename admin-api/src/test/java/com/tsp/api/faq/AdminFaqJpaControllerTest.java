package com.tsp.api.faq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.api.faq.domain.AdminFaqEntity;
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
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("FAQ Api Test")
class AdminFaqJpaControllerTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final WebApplicationContext wac;
    private final EntityManager em;
    private final JwtUtil jwtUtil;

    private AdminFaqEntity adminFaqEntity;
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

    @DisplayName("테스트 FAQ 생성")
    void createFaq() {
        // user 생성
        createUser();

        // faq 생성
        adminFaqEntity = AdminFaqEntity.builder()
                .title("FAQ 테스트")
                .description("FAQ 테스트")
                .visible("Y")
                .build();

        em.persist(adminFaqEntity);
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

        createFaq();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin FAQ 조회 테스트")
    void FAQ조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/faq").param("pageNum", "0").param("size", "3")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin FAQ 검색 조회 테스트")
    void FAQ검색조회Api테스트() throws Exception {
        // 검색 테스트
        LinkedMultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("searchType", "0");
        paramMap.add("searchKeyword", "하하");

        mockMvc.perform(get("/api/faq").queryParams(paramMap)
                        .param("pageNum", "0").param("size", "3")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin FAQ 상세 조회 테스트")
    void FAQ상세조회Api테스트() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/faq/{idx}", adminFaqEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andDo(document("faq/get", pathParameters(
                        parameterWithName("idx").description("FAQ IDX")
                )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value(adminFaqEntity.getIdx()))
                .andExpect(jsonPath("$.title").value(adminFaqEntity.getTitle()))
                .andExpect(jsonPath("$.description").value(adminFaqEntity.getDescription()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 이전 FAQ 상세 조회 테스트")
    void 이전FAQ상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/faq/2/prev"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("1"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 다음 FAQ 상세 조회 테스트")
    void 다음FAQ상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/faq/2/next"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("3"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin FAQ 등록 테스트")
    void FAQ등록Api테스트() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/faq")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminFaqEntity)))
                .andDo(print())
                .andDo(document("faq/post",
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
                .andExpect(jsonPath("$.title").value("FAQ 테스트"))
                .andExpect(jsonPath("$.description").value("FAQ 테스트"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin FAQ 수정 테스트")
    void FAQ수정Api테스트() throws Exception {
        adminFaqEntity = AdminFaqEntity.builder().idx(adminFaqEntity.getIdx()).title("테스트1").description("테스트1").visible("Y").build();

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/faq/{idx}", adminFaqEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminFaqEntity)))
                .andDo(print())
                .andDo(document("faq/put",
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
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin FAQ 삭제 테스트")
    void FAQ삭제Api테스트() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/faq/{idx}", adminFaqEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andDo(document("faq/delete", pathParameters(
                        parameterWithName("idx").description("FAQ IDX")
                )))
                .andExpect(status().isNoContent());
    }
}

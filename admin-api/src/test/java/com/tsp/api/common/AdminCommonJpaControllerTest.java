package com.tsp.api.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.api.common.domain.NewCodeEntity;
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
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("공통코드 Api Test")
class AdminCommonJpaControllerTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final WebApplicationContext wac;
    private final EntityManager em;
    private final JwtUtil jwtUtil;
    private AdminUserEntity adminUserEntity;
    private NewCodeEntity newCodeEntity;

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

    @DisplayName("테스트 공통코드 생성")
    void createCommonCode() {
        // user 생성
        createUser();

        // 공통코드 생성
        newCodeEntity = NewCodeEntity.builder()
                .categoryCd(1)
                .categoryNm("men")
                .cmmType("model")
                .visible("Y")
                .build();

        em.persist(newCodeEntity);
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

        createCommonCode();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 공통코드 조회 테스트")
    void 공통코드조회Api테스트() throws Exception {
        LinkedMultiValueMap<String, String> commonMap = new LinkedMultiValueMap<>();
        mockMvc.perform(get("/api/common").queryParams(commonMap).param("pageNum", "0").param("size", "3")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 공통코드 조회 권한 테스트")
    void 공통코드조회Api권한테스트() throws Exception {
        LinkedMultiValueMap<String, String> commonMap = new LinkedMultiValueMap<>();
        mockMvc.perform(get("/api/common").queryParams(commonMap).param("pageNum", "0").param("size", "3"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 공통코드 상세 조회 테스트")
    void 공통코드상세조회Api테스트() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/common/{idx}", newCodeEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andDo(document("common/get", pathParameters(
                        parameterWithName("idx").description("공통코드 IDX")
                )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value(newCodeEntity.getIdx()))
                .andExpect(jsonPath("$.categoryCd").value(newCodeEntity.getCategoryCd()))
                .andExpect(jsonPath("$.categoryNm").value(newCodeEntity.getCategoryNm()))
                .andExpect(jsonPath("$.cmmType").value(newCodeEntity.getCmmType()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 공통코드 등록 테스트")
    void 공통코드등록Api테스트() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/common")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newCodeEntity)))
                .andDo(print())
                .andDo(document("common/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("categoryCd").type(NUMBER).description("공통코드 카테고리"),
                                fieldWithPath("categoryNm").type(STRING).description("공통코드명"),
                                fieldWithPath("cmmType").type(STRING).description("공통코드 타입")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("categoryCd").type(NUMBER).description("공통코드 카테고리"),
                                fieldWithPath("categoryNm").type(STRING).description("공통코드명"),
                                fieldWithPath("cmmType").type(STRING).description("공통코드 타입")
                        )))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.categoryCd").value(1))
                .andExpect(jsonPath("$.categoryNm").value("men"))
                .andExpect(jsonPath("$.cmmType").value("model"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 공통코드 수정 테스트")
    void 공통코드수정Api테스트() throws Exception {
        newCodeEntity = NewCodeEntity.builder()
                .idx(newCodeEntity.getIdx())
                .categoryCd(1)
                .categoryNm("new men")
                .cmmType("model")
                .visible("Y")
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/common/{idx}", newCodeEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newCodeEntity)))
                .andDo(print())
                .andDo(document("common/put",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("categoryCd").type(NUMBER).description("공통코드 카테고리"),
                                fieldWithPath("categoryNm").type(STRING).description("공통코드명"),
                                fieldWithPath("cmmType").type(STRING).description("공통코드 타입")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("categoryCd").type(NUMBER).description("공통코드 카테고리"),
                                fieldWithPath("categoryNm").type(STRING).description("공통코드명"),
                                fieldWithPath("cmmType").type(STRING).description("공통코드 타입")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.categoryCd").value(1))
                .andExpect(jsonPath("$.categoryNm").value("new men"))
                .andExpect(jsonPath("$.cmmType").value("model"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 공통코드 삭제 테스트")
    void 공통코드삭제Api테스트() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/common/{idx}", newCodeEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andDo(document("common/delete", pathParameters(
                        parameterWithName("idx").description("공통코드 IDX")
                )))
                .andExpect(status().isNoContent());
    }
}

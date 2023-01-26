package com.tsp.api.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.api.comment.domain.AdminCommentEntity;
import com.tsp.api.model.domain.AdminModelEntity;
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
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;
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
@DisplayName("어드민 코멘트 Api Test")
class AdminCommentJpaControllerTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final WebApplicationContext wac;
    private final EntityManager em;
    private final JwtUtil jwtUtil;

    private AdminCommentEntity adminCommentEntity;
    private AdminModelEntity adminModelEntity;
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

    @DisplayName("테스트 어드민 코멘트 생성")
    void createAdminComment() {
        // user 생성
        createUser();

        adminModelEntity = AdminModelEntity.builder()
                .categoryCd(1)
                .categoryAge(2)
                .modelKorFirstName("test")
                .modelKorSecondName("test")
                .modelKorName("test")
                .modelFirstName("test")
                .modelSecondName("test")
                .modelEngName("test")
                .modelDescription("test")
                .modelMainYn("Y")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .status("active")
                .build();

        em.persist(adminModelEntity);

        // 어드민 코멘트 생성
        adminCommentEntity = AdminCommentEntity.builder()
                .comment("코멘트 테스트")
                .visible("Y")
                .build();

        em.persist(adminCommentEntity);
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

        createAdminComment();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 코멘트 조회 테스트")
    void 어드민코멘트조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/comment").param("pageNum", "1").param("size", "3")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 코멘트 검색 조회 테스트")
    void 어드민코멘트검색조회Api테스트() throws Exception {
        // 검색 테스트
        LinkedMultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("searchType", "0");
        paramMap.add("searchKeyword", "하하");

        mockMvc.perform(get("/api/comment").queryParams(paramMap).param("pageNum", "1").param("size", "3")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 코멘트 상세 조회 테스트")
    void 어드민코멘트상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/comment/1")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value(1))
                .andExpect(jsonPath("$.comment").value("테스트1"))
                .andExpect(jsonPath("$.commentType").value("model"))
                .andExpect(jsonPath("$.commentTypeIdx").value(adminModelEntity.getIdx()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 코멘트 등록 테스트")
    void 어드민코멘트등록Api테스트() throws Exception {
        mockMvc.perform(post("/api/comment")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminCommentEntity)))
                .andDo(print())
                .andDo(document("comment/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("comment").type(STRING).description("코멘트"),
                                fieldWithPath("commentType").type(STRING).description("코멘트 타입"),
                                fieldWithPath("commentTypeIdx").type(NUMBER).description("코멘트 타입 IDX"),
                                fieldWithPath("visible").type(STRING).description("코멘트 노출 여부")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("comment").type(STRING).description("코멘트"),
                                fieldWithPath("commentType").type(STRING).description("코멘트 타입"),
                                fieldWithPath("commentTypeIdx").type(NUMBER).description("코멘트 타입 IDX"),
                                fieldWithPath("visible").type(STRING).description("코멘트 노출 여부")
                        )))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.comment").value("코멘트 테스트"))
                .andExpect(jsonPath("$.commentType").value("model"))
                .andExpect(jsonPath("$.commentTypeIdx").value(adminModelEntity.getIdx()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 코멘트 수정 테스트")
    void 어드민코멘트수정Api테스트() throws Exception {
        em.persist(adminCommentEntity);

        adminCommentEntity = AdminCommentEntity.builder().idx(adminCommentEntity.getIdx()).comment("코멘트 테스트1").visible("Y").build();

        mockMvc.perform(put("/api/comment/{idx}", adminCommentEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminCommentEntity)))
                .andDo(print())
                .andDo(document("comment/put",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("comment").type(STRING).description("코멘트"),
                                fieldWithPath("commentType").type(STRING).description("코멘트 타입"),
                                fieldWithPath("commentTypeIdx").type(NUMBER).description("코멘트 타입 IDX"),
                                fieldWithPath("visible").type(STRING).description("코멘트 노출 여부")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("comment").type(STRING).description("코멘트"),
                                fieldWithPath("commentType").type(STRING).description("코멘트 타입"),
                                fieldWithPath("commentTypeIdx").type(NUMBER).description("코멘트 타입 IDX"),
                                fieldWithPath("visible").type(STRING).description("코멘트 노출 여부")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.comment").value("코멘트 테스트1"))
                .andExpect(jsonPath("$.commentType").value("model"))
                .andExpect(jsonPath("$.commentTypeIdx").value(adminModelEntity.getIdx()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 코멘트 삭제 테스트")
    void 어드민코멘트삭제Api테스트() throws Exception {
        em.persist(adminCommentEntity);

        mockMvc.perform(delete("/api/comment/{idx}", adminCommentEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(adminCommentEntity.getIdx())));
    }
}

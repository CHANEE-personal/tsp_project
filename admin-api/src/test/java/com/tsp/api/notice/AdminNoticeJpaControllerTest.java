package com.tsp.api.notice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.api.domain.notice.AdminNoticeEntity;
import com.tsp.api.domain.user.AdminUserEntity;
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

import static com.tsp.api.domain.user.Role.ROLE_ADMIN;
import static com.tsp.common.StringUtil.getString;
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
@DisplayName("공지사항 Api Test")
class AdminNoticeJpaControllerTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final WebApplicationContext wac;
    private final EntityManager em;
    private final JwtUtil jwtUtil;

    private AdminNoticeEntity adminNoticeEntity;
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

    @DisplayName("테스트 공지사항 생성")
    void createNotice() {
        // user 생성
        createUser();

        // notice 생성
        adminNoticeEntity = AdminNoticeEntity.builder()
                .title("공지사항 테스트")
                .description("공지사항 테스트")
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

        createNotice();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 공지사항 조회 테스트")
    void 공지사항조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/notice").param("pageNum", "1").param("size", "3")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 공지사항 검색 조회 테스트")
    void 공지사항검색조회Api테스트() throws Exception {
        // 검색 테스트
        LinkedMultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();

        paramMap.add("searchType", "0");
        paramMap.add("searchKeyword", "하하");

        mockMvc.perform(get("/api/notice").queryParams(paramMap).param("pageNum", "1").param("size", "3")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 공지사항 상세 조회 테스트")
    void 공지사항상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/notice/1")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("1"))
                .andExpect(jsonPath("$.title").value("테스트1"))
                .andExpect(jsonPath("$.description").value("테스트1"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 이전 공지사항 상세 조회 테스트")
    void 이전공지사항상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/notice/2/prev"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("1"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 다음 공지사항 상세 조회 테스트")
    void 다음공지사항상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/notice/2/next"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("3"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 공지사항 등록 테스트")
    void 공지사항등록Api테스트() throws Exception {
        mockMvc.perform(post("/api/notice")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminNoticeEntity)))
                .andDo(print())
                .andDo(document("notice/post",
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
                .andExpect(jsonPath("$.title").value("공지사항 테스트"))
                .andExpect(jsonPath("$.description").value("공지사항 테스트"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 공지사항 수정 테스트")
    void 공지사항수정Api테스트() throws Exception {
        em.persist(adminNoticeEntity);

        adminNoticeEntity = AdminNoticeEntity.builder().idx(adminNoticeEntity.getIdx()).title("테스트1").description("테스트1").visible("Y").build();

        mockMvc.perform(put("/api/notice/{idx}", adminNoticeEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminNoticeEntity)))
                .andDo(print())
                .andDo(document("notice/put",
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
    @DisplayName("Admin 공지사항 상단 고정 테스트")
    void 다음공지사항상단고정Api테스트() throws Exception {
        mockMvc.perform(get("/api/notice/2/fixed"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.topFixed").isBoolean());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 공지사항 삭제 테스트")
    void 공지사항삭제Api테스트() throws Exception {
        em.persist(adminNoticeEntity);

        mockMvc.perform(delete("/api/notice/{idx}", adminNoticeEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(adminNoticeEntity.getIdx())));
    }
}

package com.tsp.api.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.api.model.domain.negotiation.AdminNegotiationEntity;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.tsp.api.user.domain.Role.ROLE_ADMIN;
import static com.tsp.common.StringUtil.getString;
import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.of;
import static java.time.format.DateTimeFormatter.ofPattern;
import static javax.swing.text.html.parser.DTDConstants.NUMBER;
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
import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;
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
@DisplayName("모델 섭외 Api Test")
class AdminNegotiationJpaControllerTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final WebApplicationContext wac;
    private final EntityManager em;
    private final JwtUtil jwtUtil;

    private AdminNegotiationEntity adminNegotiationEntity;
    private AdminUserEntity adminUserEntity;

    Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return authorities;
    }

    @DisplayName("테스트 유저 생성")
    void createUser() {
        PasswordEncoder passwordEncoder = createDelegatingPasswordEncoder();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("admin04", passwordEncoder.encode("pass1234"), getAuthorities());
        String token = jwtUtil.doGenerateToken(authenticationToken.getName());

        adminUserEntity = AdminUserEntity.builder()
                .userId("admin04")
                .password(passwordEncoder.encode("pass1234"))
                .name("test")
                .email("test@test.com")
                .role(ROLE_ADMIN)
                .userToken(token)
                .visible("Y")
                .build();

        em.persist(adminUserEntity);
    }

    @DisplayName("테스트 모델 섭외 생성")
    void createModelNegotiation() {
        // user 생성
        createUser();

        adminNegotiationEntity = AdminNegotiationEntity.builder()
                .modelKorName("테스트")
                .modelNegotiationDesc("섭외 테스트")
                .modelNegotiationDate(now())
                .name("테스트")
                .phone("010-1234-5678")
                .email("test@gmail.com")
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

        createModelNegotiation();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 모델 섭외 조회 테스트")
    void 모델섭외조회Api테스트() throws Exception {
        LinkedMultiValueMap<String, String> negotiationMap = new LinkedMultiValueMap<>();
        mockMvc.perform(get("/api/negotiation").param("pageNum", "1").param("size", "3")
                        .queryParams(negotiationMap)
                        .queryParam("searchStartTime", of(now().getYear(), LocalDate.now().getMonth(), 1, 0, 0, 0, 0).format(ofPattern("yyyyMMdd")))
                        .queryParam("searchEndTime", of(now().getYear(), LocalDate.now().getMonth(), 30, 23, 59, 59).format(ofPattern("yyyyMMdd")))
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 모델 섭외 상세 조회 테스트")
    void 모델섭외상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/negotiation/1")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("1"))
                .andExpect(jsonPath("$.modelIdx").value(1))
                .andExpect(jsonPath("$.modelNegotiationDesc").value("섭외 테스트"))
                .andExpect(jsonPath("$.visible").value("Y"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 모델 섭외 이전 상세 조회 테스트")
    void 모델섭외이전상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/negotiation/2/prev")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("1"))
                .andExpect(jsonPath("$.modelIdx").value(1))
                .andExpect(jsonPath("$.modelNegotiationDesc").value("섭외 테스트"))
                .andExpect(jsonPath("$.visible").value("Y"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 모델 섭외 다음 상세 조회 테스트")
    void 모델섭외다음상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/negotiation/2/next")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("3"))
                .andExpect(jsonPath("$.modelIdx").value(1))
                .andExpect(jsonPath("$.modelNegotiationDesc").value("섭외 테스트"))
                .andExpect(jsonPath("$.visible").value("Y"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 모델 섭외 등록 테스트")
    void 모델섭외등록Api테스트() throws Exception {
        mockMvc.perform(post("/api/negotiation")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminNegotiationEntity)))
                .andDo(print())
                .andDo(document("negotiation/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("modelIdx").type(NUMBER).description(1),
                                fieldWithPath("modelNegotiationDesc").type(STRING).description("섭외 등록")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("modelIdx").type(NUMBER).description(1),
                                fieldWithPath("modelNegotiationDesc").type(STRING).description("섭외 등록")
                        )))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.modelIdx").value(1))
                .andExpect(jsonPath("$.modelNegotiationDesc").value("섭외 등록"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("CreatedBy, CreationTimestamp 테스트")
    void CreatedByAndCreationTimestamp테스트() throws Exception {
        mockMvc.perform(post("/api/schedule")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminNegotiationEntity)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.modelIdx").value(1))
                .andExpect(jsonPath("$.modelNegotiationDesc").value("섭외 등록"))
                .andExpect(jsonPath("$.creator").isNotEmpty())
                .andExpect(jsonPath("$.createTime").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 모델 섭외 수정 테스트")
    void 모델섭외수정Api테스트() throws Exception {
        em.persist(adminNegotiationEntity);

        adminNegotiationEntity = AdminNegotiationEntity.builder()
                .idx(adminNegotiationEntity.getIdx())
                .modelKorName("테스트")
                .modelNegotiationDesc("섭외 수정 테스트")
                .modelNegotiationDate(LocalDateTime.now())
                .name("테스트")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        mockMvc.perform(put("/api/negotiation/{idx}", adminNegotiationEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminNegotiationEntity)))
                .andDo(print())
                .andDo(document("negotiation/put",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("modelIdx").type(NUMBER).description(1),
                                fieldWithPath("modelNegotiationDesc").type(STRING).description("섭외 수정")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("modelIdx").type(NUMBER).description(1),
                                fieldWithPath("modelNegotiationDesc").type(STRING).description("섭외 수정")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.modelIdx").value(1))
                .andExpect(jsonPath("$.modelNegotiationDesc").value("섭외 수정 테스트"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("LastModifiedBy, UpdateTimestamp 테스트")
    void LastModifiedByAndUpdateTimestamp테스트() throws Exception {
        em.persist(adminNegotiationEntity);

        AdminNegotiationEntity newAdminNegotiationEntity = AdminNegotiationEntity.builder()
                .idx(adminNegotiationEntity.getIdx())
                .modelKorName("테스트")
                .modelNegotiationDesc("섭외 수정 테스트")
                .modelNegotiationDate(LocalDateTime.now())
                .name("테스트")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        mockMvc.perform(put("/api/negotiation/{idx}", newAdminNegotiationEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newAdminNegotiationEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.modelIdx").value(1))
                .andExpect(jsonPath("$.modelNegotiationDesc").value("섭외 수정 테스트"))
                .andExpect(jsonPath("$.updater").isNotEmpty())
                .andExpect(jsonPath("$.updateTime").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 모델 섭외 삭제 테스트")
    void 모델섭외삭제Api테스트() throws Exception {
        em.persist(adminNegotiationEntity);

        mockMvc.perform(delete("/api/negotiation/{idx}", adminNegotiationEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(adminNegotiationEntity.getIdx())));
    }
}

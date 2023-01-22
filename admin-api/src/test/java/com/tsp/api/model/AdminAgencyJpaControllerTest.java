package com.tsp.api.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.api.common.EntityType;
import com.tsp.api.domain.common.CommonImageEntity;
import com.tsp.api.domain.model.agency.AdminAgencyEntity;
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
import org.springframework.mock.web.MockMultipartFile;
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
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.tsp.api.domain.user.Role.ROLE_ADMIN;
import static com.tsp.common.StringUtil.getString;
import static java.util.List.of;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
@DisplayName("소속사 Api Test")
class AdminAgencyJpaControllerTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final WebApplicationContext wac;
    private final EntityManager em;
    private final JwtUtil jwtUtil;

    private AdminAgencyEntity adminAgencyEntity;
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

    @DisplayName("테스트 소속사 생성")
    void createAgency() {
        // user 생성
        createUser();

        adminAgencyEntity = AdminAgencyEntity.builder()
                .agencyName("agency")
                .agencyDescription("agency")
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

        createAgency();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 소속사 조회 테스트")
    void 소속사조회Api테스트() throws Exception {
        LinkedMultiValueMap<String, String> agencyMap = new LinkedMultiValueMap<>();

        mockMvc.perform(get("/api/agency/lists").queryParams(agencyMap).param("pageNum", "1").param("size", "3")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 소속사 상세 조회 테스트")
    void 소속사상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/agency/1")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("1"))
                .andExpect(jsonPath("$.agencyName").value("agency"))
                .andExpect(jsonPath("$.agencyDescription").value("agency"))
                .andExpect(jsonPath("$.visible").value("Y"));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 소속사 등록 테스트")
    void 소속사등록Api테스트() throws Exception {
        mockMvc.perform(post("/api/agency")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminAgencyEntity)))
                .andDo(print())
                .andDo(document("agency/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("agencyName").type(STRING).description("소속사명"),
                                fieldWithPath("agencyDescription").type(STRING).description("소속사 상세설명")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("agencyName").type(STRING).description("소속사명"),
                                fieldWithPath("agencyDescription").type(STRING).description("소속사 상세설명")
                        )))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.agencyName").value("agency"))
                .andExpect(jsonPath("$.agencyDescription").value("agency"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("CreatedBy, CreationTimestamp 테스트")
    void CreatedByAndCreationTimestamp테스트() throws Exception {
        mockMvc.perform(post("/api/agency")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminAgencyEntity)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.agencyName").value("agency"))
                .andExpect(jsonPath("$.agencyDescription").value("agency"))
                .andExpect(jsonPath("$.creator").isNotEmpty())
                .andExpect(jsonPath("$.createTime").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 모델 수정 테스트")
    void 소속사수정Api테스트() throws Exception {
        em.persist(adminAgencyEntity);

        adminAgencyEntity = AdminAgencyEntity.builder()
                .idx(adminAgencyEntity.getIdx())
                .agencyName("newAgency")
                .agencyDescription("newAgency")
                .visible("Y")
                .build();

        mockMvc.perform(put("/api/agency/{idx}", adminAgencyEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminAgencyEntity)))
                .andDo(print())
                .andDo(document("agency/put",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("agencyName").type(STRING).description("소속사명"),
                                fieldWithPath("agencyDescription").type(STRING).description("소속사 상세 설명")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("agencyName").type(STRING).description("소속사명"),
                                fieldWithPath("agencyDescription").type(STRING).description("소속사 상세 설명")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.agencyName").value("newAgency"))
                .andExpect(jsonPath("$.agencyDescription").value("newAgency"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("LastModifiedBy, UpdateTimestamp 테스트")
    void LastModifiedByAndUpdateTimestamp테스트() throws Exception {
        em.persist(adminAgencyEntity);

        AdminAgencyEntity newAdminAgencyEntity = AdminAgencyEntity.builder()
                .idx(adminAgencyEntity.getIdx())
                .agencyName("newAgency")
                .agencyDescription("newAgency")
                .visible("Y")
                .updater("1")
                .updateTime(LocalDateTime.now())
                .build();

        mockMvc.perform(put("/api/agency/{idx}", newAdminAgencyEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newAdminAgencyEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.agencyName").value("newAgency"))
                .andExpect(jsonPath("$.agencyDescription").value("newAgency"))
                .andExpect(jsonPath("$.updater").isNotEmpty())
                .andExpect(jsonPath("$.updateTime").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 소속사 삭제 테스트")
    void 소속사삭제Api테스트() throws Exception {
        em.persist(adminAgencyEntity);

        mockMvc.perform(delete("/api/agency/{idx}", adminAgencyEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(adminAgencyEntity.getIdx())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 소속사 이미지 등록 테스트")
    void 소속사이미지등록Api테스트() throws Exception {
        List<MultipartFile> imageFiles = of(
                new MockMultipartFile("0522045010647","0522045010647.png",
                        "image/png" , new FileInputStream("src/main/resources/static/images/0522045010647.png")),
                new MockMultipartFile("0522045010772","0522045010772.png" ,
                        "image/png" , new FileInputStream("src/main/resources/static/images/0522045010772.png"))
        );

        mockMvc.perform(multipart("/api/agency/1/images")
                        .file("images", imageFiles.get(0).getBytes())
                        .file("images", imageFiles.get(1).getBytes())
                        .contentType(MULTIPART_FORM_DATA_VALUE))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("Y"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 모델 이미지 삭제 테스트")
    void 모델이미지삭제Api테스트() throws Exception {
        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1L)
                .typeName(EntityType.AGENCY)
                .build();

        em.persist(commonImageEntity);

        mockMvc.perform(delete("/api/agency/{idx}/images", commonImageEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string(getString(commonImageEntity.getIdx(),"")));
    }
}

package com.tsp.api.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.api.common.EntityType;
import com.tsp.api.domain.common.CommonImageEntity;
import com.tsp.api.domain.model.AdminModelEntity;
import com.tsp.api.domain.model.CareerJson;
import com.tsp.api.domain.model.agency.AdminAgencyEntity;
import com.tsp.api.domain.model.recommend.AdminRecommendEntity;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.tsp.api.domain.user.Role.ROLE_ADMIN;
import static com.tsp.common.StringUtil.getString;
import static java.util.List.of;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.context.TestConstructor.AutowireMode.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
@DisplayName("모델 Api Test")
class AdminModelJpaControllerTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final WebApplicationContext wac;
    private final EntityManager em;
    private final JwtUtil jwtUtil;

    private AdminModelEntity adminModelEntity;
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
                .userId("admin06")
                .password(passwordEncoder.encode("pass1234"))
                .name("test")
                .email("test@test.com")
                .role(ROLE_ADMIN)
                .userToken(token)
                .visible("Y")
                .build();

        em.persist(adminUserEntity);
    }

    @DisplayName("테스트 모델 생성")
    void createModel() {
        // user 생성
        createUser();

        // model 생성
        ArrayList<CareerJson> careerList = new ArrayList<>();
        careerList.add(new CareerJson("title", "txt"));

        adminModelEntity = AdminModelEntity.builder()
                .categoryCd(1)
                .categoryAge(2)
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .status("draft")
                .careerList(careerList)
                .newYn("Y")
                .visible("Y")
                .build();
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void setup(RestDocumentationContextProvider restDocumentationContextProvider) throws Exception {
        this.mockMvc = webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .alwaysDo(print())
                .build();

        createModel();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 모델 조회 테스트")
    void 모델조회Api테스트() throws Exception {
        LinkedMultiValueMap<String, String> modelMap = new LinkedMultiValueMap<>();
        mockMvc.perform(get("/api/model/1").queryParams(modelMap).param("pageNum", "1").param("size", "3")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 모델 검색 조회 테스트")
    void 모델검색조회Api테스트() throws Exception {
        // 검색 테스트
        LinkedMultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("searchType", "0");
        paramMap.add("searchKeyword", "김민주");

        mockMvc.perform(get("/api/model/2").queryParams(paramMap).param("pageNum", "1").param("size", "3")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 모델 조회 예외 테스트")
    void 모델조회Api예외테스트() throws Exception {
        mockMvc.perform(get("/api/model/-1")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString().equals("모델 categoryCd는 1~3 사이 값만 입력할 수 있습니다.");
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 모델 조회 권한 테스트")
    void 모델조회Api권한테스트() throws Exception {
        mockMvc.perform(get("/api/model/1").param("pageNum", "1").param("size", "3"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 모델 상세 조회 테스트")
    void 모델상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/model/143")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("143"))
                .andExpect(jsonPath("$.categoryCd").value("2"))
                .andExpect(jsonPath("$.modelFirstName").value("kim"))
                .andExpect(jsonPath("$.modelSecondName").value("ye yeong"))
                .andExpect(jsonPath("$.modelKorFirstName").value("김"))
                .andExpect(jsonPath("$.modelKorSecondName").value("예영"))
                .andExpect(jsonPath("$.height").value("173"))
                .andExpect(jsonPath("$.size3").value("31-24-34"))
                .andExpect(jsonPath("$.shoes").value("240"));
    }

    @Test
    @DisplayName("Admin 모델 상세 조회 예외 테스트")
    void 모델상세조회Api예외테스트() throws Exception {
        mockMvc.perform(get("/api/model/-1")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString().equals("모델 categoryCd는 1~3 사이 값만 입력할 수 있습니다.");
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 모델 상세 조회 권한 테스트")
    void 모델상세조회Api권한테스트() throws Exception {
        mockMvc.perform(get("/api/model/143"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 이전 모델 상세 조회 테스트")
    void 이전모델상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/model/2/145/prev")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("144"))
                .andExpect(jsonPath("$.categoryCd").value("2"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 다음 모델 상세 조회 테스트")
    void 다음모델상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/model/2/145/next")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("147"))
                .andExpect(jsonPath("$.categoryCd").value("2"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 모델 등록 테스트")
    void 모델등록Api테스트() throws Exception {
        mockMvc.perform(post("/api/model")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminModelEntity)))
                .andDo(print())
                .andDo(document("model/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("categoryCd").type(NUMBER).description("모델 카테고리"),
                                fieldWithPath("categoryAge").type(NUMBER).description("모델 연령대"),
                                fieldWithPath("modelKorFirstName").type(STRING).description("모델 국문 성"),
                                fieldWithPath("modelKorSecondName").type(STRING).description("모델 국문 이름"),
                                fieldWithPath("modelKorName").type(STRING).description("모델 국문 이름"),
                                fieldWithPath("modelFirstName").type(STRING).description("모델 영문 성"),
                                fieldWithPath("modelSecondName").type(STRING).description("모델 영문 이름"),
                                fieldWithPath("modelDescription").type(STRING).description("모델 상세"),
                                fieldWithPath("height").type(NUMBER).description("모델 키"),
                                fieldWithPath("size3").type(STRING).description("모델 사이즈"),
                                fieldWithPath("shoes").type(NUMBER).description("모델 발 사이즈")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("categoryCd").type(NUMBER).description("모델 카테고리"),
                                fieldWithPath("categoryAge").type(NUMBER).description("모델 연령대"),
                                fieldWithPath("modelKorFirstName").type(STRING).description("모델 국문 성"),
                                fieldWithPath("modelKorSecondName").type(STRING).description("모델 국문 이름"),
                                fieldWithPath("modelKorName").type(STRING).description("모델 국문 이름"),
                                fieldWithPath("modelFirstName").type(STRING).description("모델 영문 성"),
                                fieldWithPath("modelSecondName").type(STRING).description("모델 영문 이름"),
                                fieldWithPath("modelDescription").type(STRING).description("모델 상세"),
                                fieldWithPath("height").type(NUMBER).description("모델 키"),
                                fieldWithPath("size3").type(STRING).description("모델 사이즈"),
                                fieldWithPath("shoes").type(NUMBER).description("모델 발 사이즈")
                        )))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.categoryCd").value(1))
                .andExpect(jsonPath("$.categoryAge").value(2))
                .andExpect(jsonPath("$.modelKorFirstName").value("조"))
                .andExpect(jsonPath("$.modelKorSecondName").value("찬희"))
                .andExpect(jsonPath("$.modelKorName").value("조찬희"))
                .andExpect(jsonPath("$.modelFirstName").value("CHO"))
                .andExpect(jsonPath("$.modelSecondName").value("CHANHEE"))
                .andExpect(jsonPath("$.modelDescription").value("chaneeCho"))
                .andExpect(jsonPath("$.height").value(170))
                .andExpect(jsonPath("$.size3").value("34-24-34"))
                .andExpect(jsonPath("$.shoes").value(270));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("CreatedBy, CreationTimestamp 테스트")
    void CreatedByAndCreationTimestamp테스트() throws Exception {
        mockMvc.perform(post("/api/model")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminModelEntity)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.categoryCd").value(1))
                .andExpect(jsonPath("$.categoryAge").value(2))
                .andExpect(jsonPath("$.modelKorFirstName").value("조"))
                .andExpect(jsonPath("$.modelKorSecondName").value("찬희"))
                .andExpect(jsonPath("$.modelKorName").value("조찬희"))
                .andExpect(jsonPath("$.modelFirstName").value("CHO"))
                .andExpect(jsonPath("$.modelSecondName").value("CHANHEE"))
                .andExpect(jsonPath("$.modelDescription").value("chaneeCho"))
                .andExpect(jsonPath("$.height").value(170))
                .andExpect(jsonPath("$.size3").value("34-24-34"))
                .andExpect(jsonPath("$.shoes").value(270))
                .andExpect(jsonPath("$.creator").isNotEmpty())
                .andExpect(jsonPath("$.createTime").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 모델 등록 예외 테스트")
    void 모델등록Api예외테스트() throws Exception {
        AdminModelEntity exAdminModelEntity = AdminModelEntity.builder()
                .categoryCd(-1)
                .categoryAge(2)
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .status("draft")
                .visible("Y")
                .build();

        mockMvc.perform(post("/api/model")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(exAdminModelEntity)))
                .andDo(print())
                .andExpect(status().isBadRequest());
//                .andExpect(jsonPath("$.code").value("ERROR_MODEL"))
//                .andExpect(jsonPath("$.status").value(500))
//                .andExpect(jsonPath("$.message").value("모델 등록 에러"));
    }

    @Test
    @Disabled
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 모델 등록 권한 테스트")
    void 모델등록Api권한테스트() throws Exception {
        mockMvc.perform(post("/api/model")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminModelEntity)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 모델 수정 테스트")
    void 모델수정Api테스트() throws Exception {
        em.persist(adminModelEntity);

        adminModelEntity = AdminModelEntity.builder()
                .idx(adminModelEntity.getIdx())
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
                .newYn("N")
                .status("active")
                .build();

        mockMvc.perform(put("/api/model/{idx}", adminModelEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminModelEntity)))
                .andDo(print())
                .andDo(document("model/put",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("categoryCd").type(NUMBER).description("모델 카테고리"),
                                fieldWithPath("categoryAge").type(NUMBER).description("모델 연령대"),
                                fieldWithPath("modelKorFirstName").type(STRING).description("모델 국문 성"),
                                fieldWithPath("modelKorSecondName").type(STRING).description("모델 국문 이름"),
                                fieldWithPath("modelKorName").type(STRING).description("모델 국문 이름"),
                                fieldWithPath("modelFirstName").type(STRING).description("모델 영문 성"),
                                fieldWithPath("modelSecondName").type(STRING).description("모델 영문 이름"),
                                fieldWithPath("modelDescription").type(STRING).description("모델 상세"),
                                fieldWithPath("height").type(NUMBER).description("모델 키"),
                                fieldWithPath("size3").type(STRING).description("모델 사이즈"),
                                fieldWithPath("shoes").type(NUMBER).description("모델 발 사이즈")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("categoryCd").type(NUMBER).description("모델 카테고리"),
                                fieldWithPath("categoryAge").type(NUMBER).description("모델 연령대"),
                                fieldWithPath("modelKorFirstName").type(STRING).description("모델 국문 성"),
                                fieldWithPath("modelKorSecondName").type(STRING).description("모델 국문 이름"),
                                fieldWithPath("modelKorName").type(STRING).description("모델 국문 이름"),
                                fieldWithPath("modelFirstName").type(STRING).description("모델 영문 성"),
                                fieldWithPath("modelSecondName").type(STRING).description("모델 영문 이름"),
                                fieldWithPath("modelDescription").type(STRING).description("모델 상세"),
                                fieldWithPath("height").type(NUMBER).description("모델 키"),
                                fieldWithPath("size3").type(STRING).description("모델 사이즈"),
                                fieldWithPath("shoes").type(NUMBER).description("모델 발 사이즈")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.modelKorFirstName").value("test"))
                .andExpect(jsonPath("$.modelKorSecondName").value("test"))
                .andExpect(jsonPath("$.modelKorName").value("test"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("LastModifiedBy, UpdateTimestamp 테스트")
    void LastModifiedByAndUpdateTimestamp테스트() throws Exception {
        em.persist(adminModelEntity);

        AdminModelEntity newAdminModelEntity = AdminModelEntity.builder()
                .idx(adminModelEntity.getIdx())
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

        mockMvc.perform(put("/api/model/{idx}", newAdminModelEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newAdminModelEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.modelKorFirstName").value("test"))
                .andExpect(jsonPath("$.modelKorSecondName").value("test"))
                .andExpect(jsonPath("$.modelKorName").value("test"))
                .andExpect(jsonPath("$.updater").isNotEmpty())
                .andExpect(jsonPath("$.updateTime").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 모델 수정 예외 테스트")
    void 모델수정Api예외테스트() throws Exception {
        em.persist(adminModelEntity);

        adminModelEntity = AdminModelEntity.builder()
                .idx(adminModelEntity.getIdx())
                .categoryCd(-1)
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

        mockMvc.perform(put("/api/model/{idx}", adminModelEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminModelEntity)))
                .andDo(print())
                .andExpect(status().isBadRequest());
//                .andExpect(jsonPath("$.code").value("ERROR_UPDATE_MODEL"))
//                .andExpect(jsonPath("$.status").value(500))
//                .andExpect(jsonPath("$.message").value("모델 수정 에러"));
    }

    @Test
    @Disabled
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 모델 수정 권한 테스트")
    void 모델수정Api권한테스트() throws Exception {
        em.persist(adminModelEntity);

        adminModelEntity = AdminModelEntity.builder()
                .idx(adminModelEntity.getIdx())
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

        mockMvc.perform(put("/api/model/{idx}", adminModelEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminModelEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 모델 삭제 테스트")
    void 모델삭제Api테스트() throws Exception {
        em.persist(adminModelEntity);

        mockMvc.perform(delete("/api/model/{idx}", adminModelEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(adminModelEntity.getIdx())));
    }

    @Test
    @Disabled
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 모델 삭제 권한 테스트")
    void 모델삭제Api권한테스트() throws Exception {
        em.persist(adminModelEntity);

        mockMvc.perform(delete("/api/model/{idx}", adminModelEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 모델 이미지 등록 테스트")
    void 모델이미지등록Api테스트() throws Exception {
        List<MultipartFile> imageFiles = of(
                new MockMultipartFile("0522045010647", "0522045010647.png",
                        "image/png", new FileInputStream("src/main/resources/static/images/0522045010647.png")),
                new MockMultipartFile("0522045010772", "0522045010772.png",
                        "image/png", new FileInputStream("src/main/resources/static/images/0522045010772.png"))
        );

        mockMvc.perform(multipart("/api/model/{idx}/images", adminModelEntity.getIdx())
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
                .typeName(EntityType.MODEL)
                .build();

        em.persist(commonImageEntity);

        mockMvc.perform(delete("/api/model/{idx}/images", commonImageEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string(getString(commonImageEntity.getIdx(), "")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 모델 소속사 수정 테스트")
    void 모델소속사수정Api테스트() throws Exception {
        // 소속사 등록
        AdminAgencyEntity adminAgencyEntity = AdminAgencyEntity.builder()
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

        em.persist(adminAgencyEntity);

        // 모델 등록
        em.persist(adminModelEntity);

        // 모델 소속사 수정
        adminModelEntity = AdminModelEntity.builder()
                .idx(adminModelEntity.getIdx())
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

        mockMvc.perform(put("/api/model/{idx}/agency", adminModelEntity.getIdx()).param("agencyIdx", String.valueOf(adminAgencyEntity.getIdx()))
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andDo(document("model/put/agency",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("agencyIdx").type(NUMBER).description("소속사 IDX")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("agencyIdx").type(NUMBER).description("소속사 IDX")
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.agencyIdx").value(adminAgencyEntity.getIdx()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 모델 어드민 코멘트 조회 테스트")
    void 모델어드민코멘트조회Api테스트() throws Exception {
        // 모델 등록
        em.persist(adminModelEntity);

        mockMvc.perform(get("/api/model/{idx}/admin-comment", adminModelEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.commentType").value("model"))
                .andExpect(jsonPath("$.commentTypeIdx").value(adminModelEntity.getIdx()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 새로운 모델 설정 테스트")
    void 새로운모델설정Api테스트() throws Exception {
        mockMvc.perform(put("/api/model/{idx}/toggle-new", adminModelEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.newYn").value("N"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 모델 스케줄 조회 테스트")
    void 모델스케줄조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/model/{idx}/schedule", adminModelEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.modelIdx").value(adminModelEntity.getIdx()));
    }

    @Test
    @DisplayName("추천 검색어 조회 테스트")
    void 추천검색어조회테스트() throws Exception {
        List<String> recommendList = new ArrayList<>();
        recommendList.add("모델1");
        recommendList.add("모델2");

        AdminRecommendEntity adminRecommendEntity = AdminRecommendEntity.builder()
                .recommendKeyword(recommendList)
                .build();

        em.persist(adminRecommendEntity);

        mockMvc.perform(get("/api/model/recommend").param("page", "1").param("size", "100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.recommendList.length()", greaterThan(0)));
    }

    @Test
    @DisplayName("추천 검색어 상세 조회 테스트")
    void 추천검색어상세조회테스트() throws Exception {
        List<String> recommendList = new ArrayList<>();
        recommendList.add("모델1");
        recommendList.add("모델2");

        AdminRecommendEntity adminRecommendEntity = AdminRecommendEntity.builder()
                .recommendKeyword(recommendList)
                .build();

        em.persist(adminRecommendEntity);
        mockMvc.perform(get("/api/model/recommend/{idx}", adminRecommendEntity.getIdx()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value(adminRecommendEntity.getIdx()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("추천 검색어 등록 테스트")
    void 추천검색어등록테스트() throws Exception {
        List<String> recommendList = new ArrayList<>();
        recommendList.add("모델1");
        recommendList.add("모델2");

        AdminRecommendEntity adminRecommendEntity = AdminRecommendEntity.builder()
                .recommendKeyword(recommendList)
                .build();

        mockMvc.perform(post("/api/model/recommend")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminRecommendEntity)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.recommendKeyword").value(recommendList));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("추천 검색어 수정 테스트")
    void 추천검색어수정테스트() throws Exception {
        List<String> recommendList = new ArrayList<>();
        recommendList.add("모델1");
        recommendList.add("모델2");

        AdminRecommendEntity adminRecommendEntity = AdminRecommendEntity.builder()
                .recommendKeyword(recommendList)
                .build();

        em.persist(adminRecommendEntity);

        recommendList.add("모델3");
        AdminRecommendEntity updateRecommendEntity = AdminRecommendEntity.builder()
                .idx(adminRecommendEntity.getIdx())
                .recommendKeyword(recommendList)
                .build();

        mockMvc.perform(put("/api/model/recommend/{idx}", updateRecommendEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminRecommendEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.recommendKeyword").value(recommendList));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("추천 검색어 삭제 테스트")
    void 추천검색어삭제테스트() throws Exception {
        List<String> recommendList = new ArrayList<>();
        recommendList.add("모델1");
        recommendList.add("모델2");

        AdminRecommendEntity adminRecommendEntity = AdminRecommendEntity.builder()
                .recommendKeyword(recommendList)
                .build();

        em.persist(adminRecommendEntity);
        mockMvc.perform(delete("/api/model/recommend/{idx}", adminRecommendEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}

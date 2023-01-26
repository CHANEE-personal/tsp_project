package com.tsp.api.user.service;

import com.tsp.api.domain.user.AdminUserDTO;
import com.tsp.api.domain.user.AdminUserEntity;
import com.tsp.api.domain.user.LoginRequest;
import com.tsp.api.domain.user.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.tsp.api.domain.user.Role.ROLE_ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("유저 Service Test")
class AdminUserJpaServiceTest {
    @Mock
    private AdminUserJpaService mockAdminUserJpaService;
    private final AdminUserJpaService adminUserJpaService;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager em;

    @Test
    @DisplayName("관리자 회원 리스트 조회 테스트")
    void 관리자회원리스트조회테스트() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 100);
        Page<AdminUserDTO> adminUserList = adminUserJpaService.findUserList(userMap, pageRequest);

        // then
        assertThat(adminUserList).isNotEmpty();
    }

    @Test
    @DisplayName("관리자 회원 리스트 조회 Mockito 테스트")
    void 관리자회원리스트조회Mockito테스트() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminUserDTO> returnUserList = new ArrayList<>();
        returnUserList.add(AdminUserDTO.builder()
                .idx(1L).userId("admin05").password("test1234").name("admin05").visible("Y").build());
        Page<AdminUserDTO> resultUser = new PageImpl<>(returnUserList, pageRequest, returnUserList.size());

        // when
        when(mockAdminUserJpaService.findUserList(userMap, pageRequest)).thenReturn(resultUser);
        Page<AdminUserDTO> userList = mockAdminUserJpaService.findUserList(userMap, pageRequest);
        List<AdminUserDTO> findUserList = userList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findUserList).isNotEmpty(),
                () -> assertThat(findUserList).hasSize(1)
        );

        assertThat(findUserList.get(0).getIdx()).isEqualTo(returnUserList.get(0).getIdx());
        assertThat(findUserList.get(0).getUserId()).isEqualTo(returnUserList.get(0).getUserId());
        assertThat(findUserList.get(0).getPassword()).isEqualTo(returnUserList.get(0).getPassword());
        assertThat(findUserList.get(0).getName()).isEqualTo(returnUserList.get(0).getName());
        assertThat(findUserList.get(0).getVisible()).isEqualTo(returnUserList.get(0).getVisible());

        // verify
        verify(mockAdminUserJpaService, times(1)).findUserList(userMap, pageRequest);
        verify(mockAdminUserJpaService, atLeastOnce()).findUserList(userMap, pageRequest);
        verifyNoMoreInteractions(mockAdminUserJpaService);

        InOrder inOrder = inOrder(mockAdminUserJpaService);
        inOrder.verify(mockAdminUserJpaService).findUserList(userMap, pageRequest);
    }

    @Test
    @DisplayName("관리자 회원 리스트 조회 BDD 테스트")
    void 관리자회원리스트조회BDD테스트() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminUserDTO> returnUserList = new ArrayList<>();
        returnUserList.add(AdminUserDTO.builder()
                .idx(1L).userId("admin05").password("test1234").name("admin05").visible("Y").build());
        Page<AdminUserDTO> resultUser = new PageImpl<>(returnUserList, pageRequest, returnUserList.size());

        // when
        given(mockAdminUserJpaService.findUserList(userMap, pageRequest)).willReturn(resultUser);
        Page<AdminUserDTO> userList = mockAdminUserJpaService.findUserList(userMap, pageRequest);
        List<AdminUserDTO> findUserList = userList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findUserList).isNotEmpty(),
                () -> assertThat(findUserList).hasSize(1)
        );

        assertThat(findUserList.get(0).getIdx()).isEqualTo(returnUserList.get(0).getIdx());
        assertThat(findUserList.get(0).getUserId()).isEqualTo(returnUserList.get(0).getUserId());
        assertThat(findUserList.get(0).getPassword()).isEqualTo(returnUserList.get(0).getPassword());
        assertThat(findUserList.get(0).getName()).isEqualTo(returnUserList.get(0).getName());
        assertThat(findUserList.get(0).getVisible()).isEqualTo(returnUserList.get(0).getVisible());

        // verify
        then(mockAdminUserJpaService).should(times(1)).findUserList(userMap, pageRequest);
        then(mockAdminUserJpaService).should(atLeastOnce());
        then(mockAdminUserJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("관리자 회원 상세 조회 테스트")
    void 관리자회원상세조회테스트() {
        // given
        AdminUserDTO adminUserEntity = adminUserJpaService.findOneUser("admin01");
        // then
        assertThat(adminUserEntity).isNotNull();
    }

    @Test
    @DisplayName("관리자 회원 상세 조회 Mockito 테스트")
    void 관리자회원상세조회Mockito테스트() {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        AdminUserDTO oneUser = adminUserJpaService.insertAdminUser(signUpRequest);

        // when
        when(mockAdminUserJpaService.findOneUser(oneUser.getUserId())).thenReturn(oneUser);
        AdminUserDTO userInfo = mockAdminUserJpaService.findOneUser(oneUser.getUserId());

        // then
        assertThat(userInfo.getIdx()).isEqualTo(oneUser.getIdx());
        assertThat(userInfo.getUserId()).isEqualTo(oneUser.getUserId());
        assertThat(userInfo.getPassword()).isEqualTo(oneUser.getPassword());
        assertThat(userInfo.getVisible()).isEqualTo(oneUser.getVisible());

        // verify
        verify(mockAdminUserJpaService, times(1)).findOneUser(oneUser.getUserId());
        verify(mockAdminUserJpaService, atLeastOnce()).findOneUser(oneUser.getUserId());
        verifyNoMoreInteractions(mockAdminUserJpaService);

        InOrder inOrder = inOrder(mockAdminUserJpaService);
        inOrder.verify(mockAdminUserJpaService).findOneUser(oneUser.getUserId());
    }

    @Test
    @DisplayName("관리자 회원 상세 조회 BDD 테스트")
    void 관리자회원상세조회BDD테스트() {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        AdminUserDTO oneUser = adminUserJpaService.insertAdminUser(signUpRequest);

        // when
        given(mockAdminUserJpaService.findOneUser(oneUser.getUserId())).willReturn(oneUser);
        AdminUserDTO userInfo = mockAdminUserJpaService.findOneUser(oneUser.getUserId());

        // then
        assertThat(userInfo.getIdx()).isEqualTo(oneUser.getIdx());
        assertThat(userInfo.getUserId()).isEqualTo(oneUser.getUserId());
        assertThat(userInfo.getPassword()).isEqualTo(oneUser.getPassword());
        assertThat(userInfo.getVisible()).isEqualTo(oneUser.getVisible());

        // verify
        then(mockAdminUserJpaService).should(times(1)).findOneUser(oneUser.getUserId());
        then(mockAdminUserJpaService).should(atLeastOnce()).findOneUser(oneUser.getUserId());
        then(mockAdminUserJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("관리자 로그인 처리 테스트")
    void 관리자로그인처리테스트() {
        // given
        AdminUserEntity adminUserEntity = AdminUserEntity.builder()
                .userId("admin05")
                .password(passwordEncoder.encode("pass1234"))
                .name("admin05")
                .email("admin05@admin.com")
                .visible("Y")
                .role(ROLE_ADMIN)
                .build();

        em.persist(adminUserEntity);

        LoginRequest loginRequest = LoginRequest.builder().userId(adminUserEntity.getUserId())
                .password("pass1234").build();

        // then
        adminUserJpaService.adminLogin(loginRequest);
    }

    @Test
    @DisplayName("관리자 회원가입 Mockito 테스트")
    void 관리자회원가입Mockito테스트() {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        AdminUserDTO oneUser = adminUserJpaService.insertAdminUser(signUpRequest);

        // when
        when(mockAdminUserJpaService.findOneUser(oneUser.getUserId())).thenReturn(oneUser);
        AdminUserDTO userInfo = mockAdminUserJpaService.findOneUser(oneUser.getUserId());

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test");
        assertThat(userInfo.getName()).isEqualTo("test");
        assertThat(userInfo.getEmail()).isEqualTo("test@test.com");

        // verify
        verify(mockAdminUserJpaService, times(1)).findOneUser(oneUser.getUserId());
        verify(mockAdminUserJpaService, atLeastOnce()).findOneUser(oneUser.getUserId());
        verifyNoMoreInteractions(mockAdminUserJpaService);

        InOrder inOrder = inOrder(mockAdminUserJpaService);
        inOrder.verify(mockAdminUserJpaService).findOneUser(oneUser.getUserId());
    }

    @Test
    @DisplayName("관리자 회원가입 BDD 테스트")
    void 관리자회원가입BDD테스트() {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        AdminUserDTO oneUser = adminUserJpaService.insertAdminUser(signUpRequest);

        // when
        given(mockAdminUserJpaService.findOneUser(oneUser.getUserId())).willReturn(oneUser);
        AdminUserDTO userInfo = mockAdminUserJpaService.findOneUser(oneUser.getUserId());

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test");
        assertThat(userInfo.getName()).isEqualTo("test");
        assertThat(userInfo.getEmail()).isEqualTo("test@test.com");

        // verify
        then(mockAdminUserJpaService).should(times(1)).findOneUser(oneUser.getUserId());
        then(mockAdminUserJpaService).should(atLeastOnce()).findOneUser(oneUser.getUserId());
        then(mockAdminUserJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("관리자 수정 Mockito 테스트")
    void 관리자수정Mockito테스트() {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        Long idx = adminUserJpaService.insertAdminUser(signUpRequest).getIdx();

        AdminUserEntity newAdminUserEntity = AdminUserEntity.builder()
                .idx(idx)
                .userId("test1")
                .password("test1")
                .name("test1")
                .email("test1@test.com")
                .role(ROLE_ADMIN)
                .visible("Y")
                .build();

        AdminUserDTO updateUser = adminUserJpaService.updateAdminUser(idx, newAdminUserEntity);

        // when
        when(mockAdminUserJpaService.findOneUser(newAdminUserEntity.getUserId())).thenReturn(updateUser);
        AdminUserDTO userInfo = mockAdminUserJpaService.findOneUser(newAdminUserEntity.getUserId());

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test1");
        assertThat(userInfo.getName()).isEqualTo("test1");
        assertThat(userInfo.getEmail()).isEqualTo("test1@test.com");

        // verify
        verify(mockAdminUserJpaService, times(1)).findOneUser(newAdminUserEntity.getUserId());
        verify(mockAdminUserJpaService, atLeastOnce()).findOneUser(newAdminUserEntity.getUserId());
        verifyNoMoreInteractions(mockAdminUserJpaService);

        InOrder inOrder = inOrder(mockAdminUserJpaService);
        inOrder.verify(mockAdminUserJpaService).findOneUser(newAdminUserEntity.getUserId());
    }

    @Test
    @DisplayName("관리자 수정 BDD 테스트")
    void 관리자수정BDD테스트() {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        Long idx = adminUserJpaService.insertAdminUser(signUpRequest).getIdx();

        AdminUserEntity newAdminUserEntity = AdminUserEntity.builder()
                .idx(idx)
                .userId("test1")
                .password("test1")
                .name("test1")
                .email("test1@test.com")
                .role(ROLE_ADMIN)
                .visible("Y")
                .build();

        AdminUserDTO updateUser = adminUserJpaService.updateAdminUser(idx, newAdminUserEntity);

        // when
        given(mockAdminUserJpaService.findOneUser(newAdminUserEntity.getUserId())).willReturn(updateUser);
        AdminUserDTO userInfo = mockAdminUserJpaService.findOneUser(newAdminUserEntity.getUserId());

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test1");
        assertThat(userInfo.getName()).isEqualTo("test1");
        assertThat(userInfo.getEmail()).isEqualTo("test1@test.com");

        // verify
        then(mockAdminUserJpaService).should(times(1)).findOneUser(newAdminUserEntity.getUserId());
        then(mockAdminUserJpaService).should(atLeastOnce()).findOneUser(newAdminUserEntity.getUserId());
        then(mockAdminUserJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("관리자 탈퇴 테스트")
    void 관리자탈퇴테스트() {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        AdminUserDTO adminUserDTO = adminUserJpaService.insertAdminUser(signUpRequest);
        AdminUserEntity adminUserEntity = AdminUserEntity.builder()
                .idx(adminUserDTO.getIdx())
                .userId(adminUserDTO.getUserId())
                .password(adminUserDTO.getPassword())
                .name(adminUserDTO.getName())
                .email(adminUserDTO.getEmail())
                .visible("Y")
                .build();

        AdminUserEntity nonadminUserEntity = AdminUserEntity.builder()
                .idx(999L)
                .userId(adminUserDTO.getUserId())
                .password(adminUserDTO.getPassword())
                .name(adminUserDTO.getName())
                .email(adminUserDTO.getEmail())
                .visible("Y")
                .build();

        // then
        adminUserJpaService.deleteAdminUser(nonadminUserEntity);
        em.flush();
        em.clear();
    }
}

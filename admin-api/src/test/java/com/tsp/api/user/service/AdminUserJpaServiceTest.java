package com.tsp.api.user.service;

import com.tsp.api.user.domain.AdminUserDTO;
import com.tsp.api.user.domain.AdminUserEntity;
import com.tsp.api.user.domain.LoginRequest;
import com.tsp.api.user.service.repository.AdminUserJpaQueryRepository;
import com.tsp.api.user.service.repository.AdminUserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
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

import static com.tsp.api.user.domain.Role.ROLE_ADMIN;
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
    @Mock private AdminUserJpaRepository adminUserJpaRepository;
    @Mock private AdminUserJpaQueryRepository adminUserJpaQueryRepository;
    @InjectMocks private AdminUserJpaServiceImpl mockAdminUserJpaService;
    private final AdminUserJpaService adminUserJpaService;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager em;

    @Test
    @DisplayName("관리자 회원 리스트 조회 테스트")
    void 관리자회원리스트조회테스트() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 100);
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
        when(adminUserJpaQueryRepository.findUserList(userMap, pageRequest)).thenReturn(resultUser);
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
        verify(adminUserJpaQueryRepository, times(1)).findUserList(userMap, pageRequest);
        verify(adminUserJpaQueryRepository, atLeastOnce()).findUserList(userMap, pageRequest);
        verifyNoMoreInteractions(adminUserJpaQueryRepository);

        InOrder inOrder = inOrder(adminUserJpaQueryRepository);
        inOrder.verify(adminUserJpaQueryRepository).findUserList(userMap, pageRequest);
    }

    @Test
    @DisplayName("관리자 회원 리스트 조회 BDD 테스트")
    void 관리자회원리스트조회BDD테스트() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<AdminUserDTO> returnUserList = new ArrayList<>();
        returnUserList.add(AdminUserDTO.builder()
                .idx(1L).userId("admin05").password("test1234").name("admin05").visible("Y").build());
        Page<AdminUserDTO> resultUser = new PageImpl<>(returnUserList, pageRequest, returnUserList.size());

        // when
        given(adminUserJpaQueryRepository.findUserList(userMap, pageRequest)).willReturn(resultUser);
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
        then(adminUserJpaQueryRepository).should(times(1)).findUserList(userMap, pageRequest);
        then(adminUserJpaQueryRepository).should(atLeastOnce());
        then(adminUserJpaQueryRepository).shouldHaveNoMoreInteractions();
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
        AdminUserEntity adminUserEntity = AdminUserEntity.builder()
                .userId("admin99")
                .password(passwordEncoder.encode("pass1234"))
                .name("admin99")
                .email("admin99@admin.com")
                .visible("Y")
                .role(ROLE_ADMIN)
                .build();

        em.persist(adminUserEntity);

        // when
        when(adminUserJpaRepository.findByUserId(adminUserEntity.getUserId())).thenReturn(Optional.of(adminUserEntity));
        AdminUserDTO userInfo = mockAdminUserJpaService.findOneUser(adminUserEntity.getUserId());

        // then
        assertThat(userInfo.getIdx()).isEqualTo(adminUserEntity.getIdx());
        assertThat(userInfo.getUserId()).isEqualTo(adminUserEntity.getUserId());
        assertThat(userInfo.getPassword()).isEqualTo(adminUserEntity.getPassword());
        assertThat(userInfo.getVisible()).isEqualTo(adminUserEntity.getVisible());

        // verify
        verify(adminUserJpaRepository, times(1)).findByUserId(adminUserEntity.getUserId());
        verify(adminUserJpaRepository, atLeastOnce()).findByUserId(adminUserEntity.getUserId());
        verifyNoMoreInteractions(adminUserJpaRepository);

        InOrder inOrder = inOrder(adminUserJpaRepository);
        inOrder.verify(adminUserJpaRepository).findByUserId(adminUserEntity.getUserId());
    }

    @Test
    @DisplayName("관리자 회원 상세 조회 BDD 테스트")
    void 관리자회원상세조회BDD테스트() {
        // given
        AdminUserEntity adminUserEntity = AdminUserEntity.builder()
                .userId("admin99")
                .password(passwordEncoder.encode("pass1234"))
                .name("admin99")
                .email("admin99@admin.com")
                .visible("Y")
                .role(ROLE_ADMIN)
                .build();

        em.persist(adminUserEntity);

        // when
        given(adminUserJpaRepository.findByUserId(adminUserEntity.getUserId())).willReturn(Optional.of(adminUserEntity));
        AdminUserDTO userInfo = mockAdminUserJpaService.findOneUser(adminUserEntity.getUserId());

        // then
        assertThat(userInfo.getIdx()).isEqualTo(adminUserEntity.getIdx());
        assertThat(userInfo.getUserId()).isEqualTo(adminUserEntity.getUserId());
        assertThat(userInfo.getPassword()).isEqualTo(adminUserEntity.getPassword());
        assertThat(userInfo.getVisible()).isEqualTo(adminUserEntity.getVisible());

        // verify
        then(adminUserJpaRepository).should(times(1)).findByUserId(adminUserEntity.getUserId());
        then(adminUserJpaRepository).should(atLeastOnce()).findByUserId(adminUserEntity.getUserId());
        then(adminUserJpaRepository).shouldHaveNoMoreInteractions();
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
        AdminUserEntity adminUserEntity = AdminUserEntity.builder()
                .userId("admin99")
                .password(passwordEncoder.encode("pass1234"))
                .name("admin99")
                .email("admin99@admin.com")
                .visible("Y")
                .role(ROLE_ADMIN)
                .build();

        // when
        when(adminUserJpaRepository.save(adminUserEntity)).thenReturn(adminUserEntity);
        AdminUserDTO userInfo = mockAdminUserJpaService.insertAdminUser(adminUserEntity);

        // then
        assertThat(userInfo.getUserId()).isEqualTo(adminUserEntity.getUserId());
        assertThat(userInfo.getName()).isEqualTo(adminUserEntity.getName());
        assertThat(userInfo.getEmail()).isEqualTo(adminUserEntity.getEmail());

        // verify
        verify(adminUserJpaRepository, times(1)).save(adminUserEntity);
        verify(adminUserJpaRepository, atLeastOnce()).save(adminUserEntity);

        InOrder inOrder = inOrder(adminUserJpaRepository);
        inOrder.verify(adminUserJpaRepository).save(adminUserEntity);
    }

    @Test
    @DisplayName("관리자 회원가입 BDD 테스트")
    void 관리자회원가입BDD테스트() {
        // given
        AdminUserEntity adminUserEntity = AdminUserEntity.builder()
                .userId("admin99")
                .password(passwordEncoder.encode("pass1234"))
                .name("admin99")
                .email("admin99@admin.com")
                .visible("Y")
                .role(ROLE_ADMIN)
                .build();

        // when
        given(adminUserJpaRepository.save(adminUserEntity)).willReturn(adminUserEntity);
        AdminUserDTO userInfo = mockAdminUserJpaService.insertAdminUser(adminUserEntity);

        // then
        assertThat(userInfo.getUserId()).isEqualTo(adminUserEntity.getUserId());
        assertThat(userInfo.getName()).isEqualTo(adminUserEntity.getName());
        assertThat(userInfo.getEmail()).isEqualTo(adminUserEntity.getEmail());

        // verify
        then(adminUserJpaRepository).should(times(1)).save(adminUserEntity);
        then(adminUserJpaRepository).should(atLeastOnce()).save(adminUserEntity);
    }

    @Test
    @DisplayName("관리자 수정 Mockito 테스트")
    void 관리자수정Mockito테스트() {
        // given
        AdminUserEntity adminUserEntity = AdminUserEntity.builder()
                .userId("admin99")
                .password(passwordEncoder.encode("pass1234"))
                .name("admin99")
                .email("admin99@admin.com")
                .visible("Y")
                .role(ROLE_ADMIN)
                .build();

        Long idx = adminUserJpaService.insertAdminUser(adminUserEntity).getIdx();

        AdminUserEntity newAdminUserEntity = AdminUserEntity.builder()
                .idx(idx)
                .userId("test1")
                .password("test1")
                .name("test1")
                .email("test1@test.com")
                .role(ROLE_ADMIN)
                .visible("Y")
                .build();

        // when
        when(adminUserJpaRepository.findById(newAdminUserEntity.getIdx())).thenReturn(Optional.of(newAdminUserEntity));
        when(adminUserJpaRepository.save(newAdminUserEntity)).thenReturn(newAdminUserEntity);
        AdminUserDTO userInfo = mockAdminUserJpaService.updateAdminUser(newAdminUserEntity.getIdx(), newAdminUserEntity);

        // then
        assertThat(userInfo.getUserId()).isEqualTo(newAdminUserEntity.getUserId());
        assertThat(userInfo.getName()).isEqualTo(newAdminUserEntity.getName());
        assertThat(userInfo.getEmail()).isEqualTo(newAdminUserEntity.getEmail());

        // verify
        verify(adminUserJpaRepository, times(1)).findById(newAdminUserEntity.getIdx());
        verify(adminUserJpaRepository, atLeastOnce()).findById(newAdminUserEntity.getIdx());
        verifyNoMoreInteractions(adminUserJpaRepository);

        InOrder inOrder = inOrder(adminUserJpaRepository);
        inOrder.verify(adminUserJpaRepository).findById(newAdminUserEntity.getIdx());
    }

    @Test
    @DisplayName("관리자 수정 BDD 테스트")
    void 관리자수정BDD테스트() {
        // given
        AdminUserEntity adminUserEntity = AdminUserEntity.builder()
                .userId("admin99")
                .password(passwordEncoder.encode("pass1234"))
                .name("admin99")
                .email("admin99@admin.com")
                .visible("Y")
                .role(ROLE_ADMIN)
                .build();

        Long idx = adminUserJpaService.insertAdminUser(adminUserEntity).getIdx();

        AdminUserEntity newAdminUserEntity = AdminUserEntity.builder()
                .idx(idx)
                .userId("test1")
                .password("test1")
                .name("test1")
                .email("test1@test.com")
                .role(ROLE_ADMIN)
                .visible("Y")
                .build();

        // when
        given(adminUserJpaRepository.findById(newAdminUserEntity.getIdx())).willReturn(Optional.of(newAdminUserEntity));
        given(adminUserJpaRepository.save(newAdminUserEntity)).willReturn(newAdminUserEntity);
        AdminUserDTO userInfo = mockAdminUserJpaService.updateAdminUser(newAdminUserEntity.getIdx(), newAdminUserEntity);

        // then
        assertThat(userInfo.getUserId()).isEqualTo(newAdminUserEntity.getUserId());
        assertThat(userInfo.getName()).isEqualTo(newAdminUserEntity.getName());
        assertThat(userInfo.getEmail()).isEqualTo(newAdminUserEntity.getEmail());

        // verify
        then(adminUserJpaRepository).should(times(1)).findById(newAdminUserEntity.getIdx());
        then(adminUserJpaRepository).should(atLeastOnce()).findById(newAdminUserEntity.getIdx());
        then(adminUserJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("관리자 탈퇴 테스트")
    void 관리자탈퇴테스트() {
        AdminUserEntity adminUserEntity = AdminUserEntity.builder()
                .userId("admin99")
                .password(passwordEncoder.encode("pass1234"))
                .name("admin99")
                .email("admin99@admin.com")
                .visible("Y")
                .role(ROLE_ADMIN)
                .build();

        em.persist(adminUserEntity);

        // then
        adminUserJpaService.deleteAdminUser(adminUserEntity);
        em.flush();
        em.clear();
    }
}

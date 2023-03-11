package com.tsp.api.user.service.repository;

import com.tsp.api.user.domain.AdminUserDto;
import com.tsp.api.user.domain.AdminUserEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("유저 Repository Test")
class AdminUserJpaQueryRepositoryTest {
    @Mock private AdminUserJpaQueryRepository mockAdminUserJpaQueryRepository;
    private final AdminUserJpaQueryRepository adminUserJpaQueryRepository;

    private AdminUserEntity adminUserEntity;
    private AdminUserDto adminUserDTO;

    void createUser() {
        adminUserEntity = AdminUserEntity.builder().idx(2L).userId("admin01").build();
        adminUserDTO = AdminUserEntity.toDto(adminUserEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createUser();
    }

    @Test
    @DisplayName("유저 조회 테스트")
    void 유저조회테스트() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 100);

        // then
        assertThat(adminUserJpaQueryRepository.findUserList(userMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("유저 Mockito 조회 테스트")
    void 유저Mockito조회테스트() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminUserDto> userList = new ArrayList<>();
        AdminUserDto adminUserDTO = AdminUserDto.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();
        userList.add(adminUserDTO);

        Page<AdminUserDto> resultUser = new PageImpl<>(userList, pageRequest, userList.size());

        // when
        when(mockAdminUserJpaQueryRepository.findUserList(userMap, pageRequest)).thenReturn(resultUser);
        Page<AdminUserDto> newUserList = mockAdminUserJpaQueryRepository.findUserList(userMap, pageRequest);
        List<AdminUserDto> findUserList = newUserList.stream().collect(Collectors.toList());

        // then
        assertThat(findUserList.get(0).getUserId()).isEqualTo(userList.get(0).getUserId());
        assertThat(findUserList.get(0).getPassword()).isEqualTo(userList.get(0).getPassword());
        assertThat(findUserList.get(0).getName()).isEqualTo(userList.get(0).getName());
        assertThat(findUserList.get(0).getEmail()).isEqualTo(userList.get(0).getEmail());

        // verify
        verify(mockAdminUserJpaQueryRepository, times(1)).findUserList(userMap, pageRequest);
        verify(mockAdminUserJpaQueryRepository, atLeastOnce()).findUserList(userMap, pageRequest);
        verifyNoMoreInteractions(mockAdminUserJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminUserJpaQueryRepository);
        inOrder.verify(mockAdminUserJpaQueryRepository).findUserList(userMap, pageRequest);
    }

    @Test
    @DisplayName("유저 BDD 조회 테스트")
    void 유저BDD조회테스트() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminUserDto> userList = new ArrayList<>();
        AdminUserDto adminUserDTO = AdminUserDto.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();
        userList.add(adminUserDTO);

        Page<AdminUserDto> resultUser = new PageImpl<>(userList, pageRequest, userList.size());

        // when
        given(mockAdminUserJpaQueryRepository.findUserList(userMap, pageRequest)).willReturn(resultUser);
        Page<AdminUserDto> newUserList = mockAdminUserJpaQueryRepository.findUserList(userMap, pageRequest);
        List<AdminUserDto> findUserList = newUserList.stream().collect(Collectors.toList());

        // then
        assertThat(findUserList.get(0).getUserId()).isEqualTo(userList.get(0).getUserId());
        assertThat(findUserList.get(0).getPassword()).isEqualTo(userList.get(0).getPassword());
        assertThat(findUserList.get(0).getName()).isEqualTo(userList.get(0).getName());
        assertThat(findUserList.get(0).getEmail()).isEqualTo(userList.get(0).getEmail());

        // verify
        then(mockAdminUserJpaQueryRepository).should(times(1)).findUserList(userMap, pageRequest);
        then(mockAdminUserJpaQueryRepository).should(atLeastOnce()).findUserList(userMap, pageRequest);
        then(mockAdminUserJpaQueryRepository).shouldHaveNoMoreInteractions();
    }
}

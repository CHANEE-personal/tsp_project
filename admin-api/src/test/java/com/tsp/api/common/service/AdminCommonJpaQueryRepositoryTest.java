package com.tsp.api.common.service;

import com.tsp.api.common.domain.NewCodeDTO;
import com.tsp.api.common.domain.NewCodeEntity;
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
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("공통코드 Repository Test")
class AdminCommonJpaQueryRepositoryTest {
    @Mock
    private AdminCommonJpaQueryRepository mockAdminCommonJpaQueryRepository;
    private final AdminCommonJpaQueryRepository adminCommonJpaQueryRepository;

    private NewCodeEntity commonCodeEntity;
    private NewCodeDTO commonCodeDTO;

    public void createModelAndImage() {
        AdminUserEntity adminUserEntity = AdminUserEntity.builder()
                .userId("admin02")
                .password("pass1234")
                .name("test")
                .visible("Y")
                .build();

        commonCodeEntity = NewCodeEntity.builder()
                .categoryCd(1)
                .categoryNm("공통코드")
                .cmmType("common")
                .visible("Y")
                .build();

        commonCodeDTO = NewCodeEntity.toDto(commonCodeEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createModelAndImage();
    }

    @Test
    @DisplayName("공통코드 리스트 조회 테스트")
    void 공통코드리스트조회테스트() {
        // given
        Map<String, Object> commonMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 100);

        // then
        assertThat(adminCommonJpaQueryRepository.findCommonCodeList(commonMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("공통코드 리스트 조회 Mockito 테스트")
    void 공통코드리스트조회Mockito테스트() {
        Map<String, Object> commonMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 100);

        List<NewCodeDTO> commonCodeList = new ArrayList<>();
        commonCodeList.add(NewCodeDTO.builder().idx(1L).categoryCd(1).categoryNm("men").cmmType("model").visible("Y").build());
        Page<NewCodeDTO> resultCommon = new PageImpl<>(commonCodeList, pageRequest, commonCodeList.size());
        // when
        when(mockAdminCommonJpaQueryRepository.findCommonCodeList(commonMap, pageRequest)).thenReturn(resultCommon);
        Page<NewCodeDTO> newCommonCodeList = mockAdminCommonJpaQueryRepository.findCommonCodeList(commonMap, pageRequest);
        List<NewCodeDTO> findCommonCodeList = newCommonCodeList.stream().collect(Collectors.toList());

        // then
        assertThat(findCommonCodeList.get(0).getIdx()).isEqualTo(commonCodeList.get(0).getIdx());
        assertThat(findCommonCodeList.get(0).getCategoryCd()).isEqualTo(commonCodeList.get(0).getCategoryCd());
        assertThat(findCommonCodeList.get(0).getCategoryNm()).isEqualTo(commonCodeList.get(0).getCategoryNm());
        assertThat(findCommonCodeList.get(0).getCmmType()).isEqualTo(commonCodeList.get(0).getCmmType());

        // verify
        verify(mockAdminCommonJpaQueryRepository, times(1)).findCommonCodeList(commonMap, pageRequest);
        verify(mockAdminCommonJpaQueryRepository, atLeastOnce()).findCommonCodeList(commonMap, pageRequest);
        verifyNoMoreInteractions(mockAdminCommonJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminCommonJpaQueryRepository);
        inOrder.verify(mockAdminCommonJpaQueryRepository).findCommonCodeList(commonMap, pageRequest);
    }

    @Test
    @DisplayName("공통코드 리스트 조회 BDD 테스트")
    void 공통코드리스트조회BDD테스트() {
        Map<String, Object> commonMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 100);

        List<NewCodeDTO> commonCodeList = new ArrayList<>();
        commonCodeList.add(NewCodeDTO.builder().idx(1L).categoryCd(1).categoryNm("men").cmmType("model").visible("Y").build());
        Page<NewCodeDTO> resultCommon = new PageImpl<>(commonCodeList, pageRequest, commonCodeList.size());
        // when
        when(mockAdminCommonJpaQueryRepository.findCommonCodeList(commonMap, pageRequest)).thenReturn(resultCommon);
        Page<NewCodeDTO> newCommonCodeList = mockAdminCommonJpaQueryRepository.findCommonCodeList(commonMap, pageRequest);
        List<NewCodeDTO> findCommonCodeList = newCommonCodeList.stream().collect(Collectors.toList());

        // then
        assertThat(findCommonCodeList.get(0).getIdx()).isEqualTo(commonCodeList.get(0).getIdx());
        assertThat(findCommonCodeList.get(0).getCategoryCd()).isEqualTo(commonCodeList.get(0).getCategoryCd());
        assertThat(findCommonCodeList.get(0).getCategoryNm()).isEqualTo(commonCodeList.get(0).getCategoryNm());
        assertThat(findCommonCodeList.get(0).getCmmType()).isEqualTo(commonCodeList.get(0).getCmmType());

        // verify
        then(mockAdminCommonJpaQueryRepository).should(times(1)).findCommonCodeList(commonMap, pageRequest);
        then(mockAdminCommonJpaQueryRepository).should(atLeastOnce()).findCommonCodeList(commonMap, pageRequest);
        then(mockAdminCommonJpaQueryRepository).shouldHaveNoMoreInteractions();
    }
}

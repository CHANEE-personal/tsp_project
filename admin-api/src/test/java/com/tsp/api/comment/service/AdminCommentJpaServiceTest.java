package com.tsp.api.comment.service;

import com.tsp.api.comment.domain.AdminCommentDTO;
import com.tsp.api.model.service.AdminModelCommonServiceTest;
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
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("어드민 코멘트 Service Test")
class AdminCommentJpaServiceTest extends AdminModelCommonServiceTest {
    @Mock private AdminCommentJpaQueryRepository adminCommentJpaQueryRepository;
    @InjectMocks private AdminCommentJpaServiceImpl mockAdminCommentJpaService;
    private final AdminCommentJpaService adminCommentJpaService;

    @Test
    @DisplayName("어드민 코멘트 리스트 조회 테스트")
    void 어드민코멘트리스트조회테스트() {
        // given
        Map<String, Object> commentMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        // then
        assertThat(adminCommentJpaService.findAdminCommentList(commentMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("어드민 코멘트 리스트 조회 Mockito 테스트")
    void 어드민코멘트리스트조회Mockito테스트() {
        // given
        Map<String, Object> commentMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<AdminCommentDTO> returnCommentList = new ArrayList<>();

        returnCommentList.add(AdminCommentDTO.builder().idx(1L).comment("코멘트테스트").commentType("model").commentTypeIdx(adminModelEntity.getIdx()).visible("Y").build());
        returnCommentList.add(AdminCommentDTO.builder().idx(2L).comment("코멘트테스트2").commentType("model").commentTypeIdx(adminModelEntity.getIdx()).visible("Y").build());

        Page<AdminCommentDTO> resultComment = new PageImpl<>(returnCommentList, pageRequest, returnCommentList.size());
        // when
        when(adminCommentJpaQueryRepository.findAdminCommentList(commentMap, pageRequest)).thenReturn(resultComment);
        Page<AdminCommentDTO> commentList = mockAdminCommentJpaService.findAdminCommentList(commentMap, pageRequest);
        List<AdminCommentDTO> findCommentList = commentList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findCommentList).isNotEmpty(),
                () -> assertThat(findCommentList).hasSize(2)
        );

        assertThat(findCommentList.get(0).getIdx()).isEqualTo(returnCommentList.get(0).getIdx());
        assertThat(findCommentList.get(0).getComment()).isEqualTo(returnCommentList.get(0).getComment());
        assertThat(findCommentList.get(0).getCommentType()).isEqualTo(returnCommentList.get(0).getCommentType());
        assertThat(findCommentList.get(0).getCommentTypeIdx()).isEqualTo(returnCommentList.get(0).getCommentTypeIdx());

        // verify
        verify(adminCommentJpaQueryRepository, times(1)).findAdminCommentList(commentMap, pageRequest);
        verify(adminCommentJpaQueryRepository, atLeastOnce()).findAdminCommentList(commentMap, pageRequest);
        verifyNoMoreInteractions(adminCommentJpaQueryRepository);

        InOrder inOrder = inOrder(adminCommentJpaQueryRepository);
        inOrder.verify(adminCommentJpaQueryRepository).findAdminCommentList(commentMap, pageRequest);
    }

    @Test
    @DisplayName("어드민 코멘트 리스트 조회 BDD 테스트")
    void 어드민코멘트리스트조회BDD테스트() {
        // given
        Map<String, Object> commentMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<AdminCommentDTO> returnCommentList = new ArrayList<>();

        returnCommentList.add(AdminCommentDTO.builder().idx(1L).comment("코멘트테스트").commentType("model").commentTypeIdx(adminModelEntity.getIdx()).visible("Y").build());
        returnCommentList.add(AdminCommentDTO.builder().idx(2L).comment("코멘트테스트2").commentType("model").commentTypeIdx(adminModelEntity.getIdx()).visible("Y").build());

        Page<AdminCommentDTO> resultComment = new PageImpl<>(returnCommentList, pageRequest, returnCommentList.size());
        // when
        given(adminCommentJpaQueryRepository.findAdminCommentList(commentMap, pageRequest)).willReturn(resultComment);
        Page<AdminCommentDTO> commentList = mockAdminCommentJpaService.findAdminCommentList(commentMap, pageRequest);
        List<AdminCommentDTO> findCommentList = commentList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findCommentList).isNotEmpty(),
                () -> assertThat(findCommentList).hasSize(2)
        );

        assertThat(findCommentList.get(0).getIdx()).isEqualTo(returnCommentList.get(0).getIdx());
        assertThat(findCommentList.get(0).getComment()).isEqualTo(returnCommentList.get(0).getComment());
        assertThat(findCommentList.get(0).getCommentType()).isEqualTo(returnCommentList.get(0).getCommentType());
        assertThat(findCommentList.get(0).getCommentTypeIdx()).isEqualTo(returnCommentList.get(0).getCommentTypeIdx());

        // verify
        then(adminCommentJpaQueryRepository).should(times(1)).findAdminCommentList(commentMap, pageRequest);
        then(adminCommentJpaQueryRepository).should(atLeastOnce()).findAdminCommentList(commentMap, pageRequest);
        then(adminCommentJpaQueryRepository).shouldHaveNoMoreInteractions();
    }
}

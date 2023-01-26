package com.tsp.api.comment.service;

import com.tsp.api.comment.domain.AdminCommentDTO;
import com.tsp.api.comment.domain.AdminCommentEntity;
import com.tsp.api.model.domain.AdminModelDTO;
import com.tsp.api.model.domain.AdminModelEntity;
import com.tsp.api.model.service.AdminModelJpaRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
class AdminCommentJpaServiceTest {
    @Mock AdminCommentJpaService mockAdminCommentJpaService;
    private final AdminCommentJpaService adminCommentJpaService;
    private final AdminModelJpaRepository adminModelJpaRepository;

    private AdminCommentEntity adminCommentEntity;
    private AdminCommentDTO adminCommentDTO;
    private AdminModelEntity adminModelEntity;
    private AdminModelDTO adminModelDTO;

    void createAdminComment() {
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
                .favoriteCount(1)
                .viewCount(1)
                .modelMainYn("Y")
                .newYn("N")
                .status("draft")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        adminModelDTO = AdminModelEntity.toDto(adminModelJpaRepository.save(adminModelEntity));

        adminCommentEntity = AdminCommentEntity.builder()
                .comment("코멘트 테스트")
                .visible("Y")
                .build();

        adminCommentDTO = AdminCommentEntity.toDto(adminCommentEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createAdminComment();
    }

    @Test
    @DisplayName("어드민 코멘트 리스트 조회 테스트")
    void 어드민코멘트리스트조회테스트() {
        // given
        Map<String, Object> commentMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        // then
        assertThat(adminCommentJpaService.findAdminCommentList(commentMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("어드민 코멘트 리스트 조회 Mockito 테스트")
    void 어드민코멘트리스트조회Mockito테스트() {
        // given
        Map<String, Object> commentMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminCommentDTO> returnCommentList = new ArrayList<>();

        returnCommentList.add(AdminCommentDTO.builder().idx(1L).comment("코멘트테스트").commentType("model").commentTypeIdx(adminModelEntity.getIdx()).visible("Y").build());
        returnCommentList.add(AdminCommentDTO.builder().idx(2L).comment("코멘트테스트2").commentType("model").commentTypeIdx(adminModelEntity.getIdx()).visible("Y").build());

        Page<AdminCommentDTO> resultComment = new PageImpl<>(returnCommentList, pageRequest, returnCommentList.size());
        // when
        when(mockAdminCommentJpaService.findAdminCommentList(commentMap, pageRequest)).thenReturn(resultComment);
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
        verify(mockAdminCommentJpaService, times(1)).findAdminCommentList(commentMap, pageRequest);
        verify(mockAdminCommentJpaService, atLeastOnce()).findAdminCommentList(commentMap, pageRequest);
        verifyNoMoreInteractions(mockAdminCommentJpaService);

        InOrder inOrder = inOrder(mockAdminCommentJpaService);
        inOrder.verify(mockAdminCommentJpaService).findAdminCommentList(commentMap, pageRequest);
    }

    @Test
    @DisplayName("어드민 코멘트 리스트 조회 BDD 테스트")
    void 어드민코멘트리스트조회BDD테스트() {
        // given
        Map<String, Object> commentMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminCommentDTO> returnCommentList = new ArrayList<>();

        returnCommentList.add(AdminCommentDTO.builder().idx(1L).comment("코멘트테스트").commentType("model").commentTypeIdx(adminModelEntity.getIdx()).visible("Y").build());
        returnCommentList.add(AdminCommentDTO.builder().idx(2L).comment("코멘트테스트2").commentType("model").commentTypeIdx(adminModelEntity.getIdx()).visible("Y").build());

        Page<AdminCommentDTO> resultComment = new PageImpl<>(returnCommentList, pageRequest, returnCommentList.size());
        // when
        given(mockAdminCommentJpaService.findAdminCommentList(commentMap, pageRequest)).willReturn(resultComment);
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
        then(mockAdminCommentJpaService).should(times(1)).findAdminCommentList(commentMap, pageRequest);
        then(mockAdminCommentJpaService).should(atLeastOnce()).findAdminCommentList(commentMap, pageRequest);
        then(mockAdminCommentJpaService).shouldHaveNoMoreInteractions();
    }
}

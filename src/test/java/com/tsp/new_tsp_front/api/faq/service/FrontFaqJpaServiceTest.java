package com.tsp.new_tsp_front.api.faq.service;

import com.tsp.new_tsp_front.api.faq.domain.FrontFaqDTO;
import com.tsp.new_tsp_front.api.faq.domain.FrontFaqEntity;
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
@DisplayName("FAQ Service Test")
class FrontFaqJpaServiceTest {
    @Mock private FrontFaqJpaService mockFrontFaqJpaService;
    private final FrontFaqJpaService frontFaqJpaService;

    @Test
    @DisplayName("FAQ리스트조회Mockito테스트")
    void FAQ리스트조회Mockito테스트() {
        // given
        Map<String, Object> faqMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontFaqDTO> faqList = new ArrayList<>();
        faqList.add(FrontFaqDTO.builder().idx(1L).title("FAQ").description("FAQ").visible("Y").build());
        Page<FrontFaqDTO> resultFaq = new PageImpl<>(faqList, pageRequest, faqList.size());

        // when
        when(mockFrontFaqJpaService.findFaqList(faqMap, pageRequest)).thenReturn(resultFaq);
        Page<FrontFaqDTO> newFaqList = mockFrontFaqJpaService.findFaqList(faqMap, pageRequest);
        List<FrontFaqDTO> findFaqList = newFaqList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findFaqList).isNotEmpty(),
                () -> assertThat(findFaqList).hasSize(2)
        );

        assertThat(findFaqList.get(0).getIdx()).isEqualTo(faqList.get(0).getIdx());
        assertThat(findFaqList.get(0).getTitle()).isEqualTo(faqList.get(0).getTitle());
        assertThat(findFaqList.get(0).getDescription()).isEqualTo(faqList.get(0).getDescription());
        assertThat(findFaqList.get(0).getVisible()).isEqualTo(faqList.get(0).getVisible());

        // verify
        verify(mockFrontFaqJpaService, times(1)).findFaqList(faqMap, pageRequest);
        verify(mockFrontFaqJpaService, atLeastOnce()).findFaqList(faqMap, pageRequest);
        verifyNoMoreInteractions(mockFrontFaqJpaService);

        InOrder inOrder = inOrder(mockFrontFaqJpaService);
        inOrder.verify(mockFrontFaqJpaService).findFaqList(faqMap, pageRequest);
    }

    @Test
    @DisplayName("FAQ리스트조회BDD테스트")
    void FAQ리스트조회BDD테스트() {
        // given
        Map<String, Object> faqMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontFaqDTO> faqList = new ArrayList<>();
        faqList.add(FrontFaqDTO.builder().idx(1L).title("FAQ").description("FAQ").visible("Y").build());
        Page<FrontFaqDTO> resultFaq = new PageImpl<>(faqList, pageRequest, faqList.size());

        // when
        given(mockFrontFaqJpaService.findFaqList(faqMap, pageRequest)).willReturn(resultFaq);
        Page<FrontFaqDTO> newFaqList = mockFrontFaqJpaService.findFaqList(faqMap, pageRequest);
        List<FrontFaqDTO> findFaqList = newFaqList.stream().collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findFaqList).isNotEmpty(),
                () -> assertThat(findFaqList).hasSize(2)
        );

        assertThat(findFaqList.get(0).getIdx()).isEqualTo(faqList.get(0).getIdx());
        assertThat(findFaqList.get(0).getTitle()).isEqualTo(faqList.get(0).getTitle());
        assertThat(findFaqList.get(0).getDescription()).isEqualTo(faqList.get(0).getDescription());
        assertThat(findFaqList.get(0).getVisible()).isEqualTo(faqList.get(0).getVisible());

        // verify
        then(mockFrontFaqJpaService).should(times(1)).findFaqList(faqMap, pageRequest);
        then(mockFrontFaqJpaService).should(atLeastOnce()).findFaqList(faqMap, pageRequest);
        then(mockFrontFaqJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("FAQ상세조회Mockito테스트")
    void FAQ상세조회Mockito테스트() {
        // given
        FrontFaqEntity frontFaqEntity = FrontFaqEntity.builder().idx(1L).title("FAQTest").description("FAQTest").build();
        FrontFaqDTO frontFaqDTO = FrontFaqEntity.toDto(frontFaqEntity);

        // when
        when(mockFrontFaqJpaService.findOneFaq(frontFaqEntity.getIdx())).thenReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaService.findOneFaq(frontFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(frontFaqDTO.getIdx());
        assertThat(faqInfo.getTitle()).isEqualTo(frontFaqDTO.getTitle());
        assertThat(faqInfo.getDescription()).isEqualTo(frontFaqDTO.getDescription());
        assertThat(faqInfo.getVisible()).isEqualTo(frontFaqDTO.getVisible());

        // verify
        verify(mockFrontFaqJpaService, times(1)).findOneFaq(frontFaqEntity.getIdx());
        verify(mockFrontFaqJpaService, atLeastOnce()).findOneFaq(frontFaqEntity.getIdx());
        verifyNoMoreInteractions(mockFrontFaqJpaService);

        InOrder inOrder = inOrder(mockFrontFaqJpaService);
        inOrder.verify(mockFrontFaqJpaService).findOneFaq(frontFaqEntity.getIdx());
    }

    @Test
    @DisplayName("FAQ상세조회BDD테스트")
    void FAQ상세조회BDD테스트() {
        // given
        FrontFaqEntity frontFaqEntity = FrontFaqEntity.builder().idx(1L).title("FAQTest").description("FAQTest").build();
        FrontFaqDTO frontFaqDTO = FrontFaqEntity.toDto(frontFaqEntity);

        // when
        when(mockFrontFaqJpaService.findOneFaq(frontFaqEntity.getIdx())).thenReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaService.findOneFaq(frontFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(frontFaqDTO.getIdx());
        assertThat(faqInfo.getTitle()).isEqualTo(frontFaqDTO.getTitle());
        assertThat(faqInfo.getDescription()).isEqualTo(frontFaqDTO.getDescription());
        assertThat(faqInfo.getVisible()).isEqualTo(frontFaqDTO.getVisible());

        // verify
        then(mockFrontFaqJpaService).should(times(1)).findOneFaq(frontFaqEntity.getIdx());
        then(mockFrontFaqJpaService).should(atLeastOnce()).findOneFaq(frontFaqEntity.getIdx());
        then(mockFrontFaqJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 FAQ 상세 조회 테스트")
    void 이전or다음FAQ상세조회테스트() {
        // given
        FrontFaqEntity frontFaqEntity = FrontFaqEntity.builder().idx(2L).build();

        // when
        FrontFaqDTO frontFaqDTO = frontFaqJpaService.findOneFaq(frontFaqEntity.getIdx());

        // 이전 소속사
        assertThat(frontFaqJpaService.findPrevOneFaq(frontFaqEntity.getIdx()).getIdx()).isEqualTo(1);
        // 다음 소속사
        assertThat(frontFaqJpaService.findNextOneFaq(frontFaqEntity.getIdx()).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 FAQ 상세 조회 Mockito 테스트")
    void 이전FAQ상세조회Mockito테스트() {
        // given
        FrontFaqEntity frontFaqEntity = FrontFaqEntity.builder().idx(2L).build();

        // when
        FrontFaqDTO frontFaqDTO = frontFaqJpaService.findPrevOneFaq(frontFaqEntity.getIdx());

        when(mockFrontFaqJpaService.findPrevOneFaq(frontFaqEntity.getIdx())).thenReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaService.findPrevOneFaq(frontFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockFrontFaqJpaService, times(1)).findPrevOneFaq(frontFaqEntity.getIdx());
        verify(mockFrontFaqJpaService, atLeastOnce()).findPrevOneFaq(frontFaqEntity.getIdx());
        verifyNoMoreInteractions(mockFrontFaqJpaService);

        InOrder inOrder = inOrder(mockFrontFaqJpaService);
        inOrder.verify(mockFrontFaqJpaService).findPrevOneFaq(frontFaqEntity.getIdx());
    }

    @Test
    @DisplayName("이전 FAQ 상세 조회 BDD 테스트")
    void 이전FAQ상세조회BDD테스트() {
        // given
        FrontFaqEntity frontFaqEntity = FrontFaqEntity.builder().idx(2L).build();

        // when
        FrontFaqDTO frontFaqDTO = frontFaqJpaService.findPrevOneFaq(frontFaqEntity.getIdx());

        given(mockFrontFaqJpaService.findPrevOneFaq(frontFaqEntity.getIdx())).willReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaService.findPrevOneFaq(frontFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockFrontFaqJpaService).should(times(1)).findPrevOneFaq(frontFaqEntity.getIdx());
        then(mockFrontFaqJpaService).should(atLeastOnce()).findPrevOneFaq(frontFaqEntity.getIdx());
        then(mockFrontFaqJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 FAQ 상세 조회 Mockito 테스트")
    void 다음FAQ상세조회Mockito테스트() {
        // given
        FrontFaqEntity frontFaqEntity = FrontFaqEntity.builder().idx(2L).build();

        // when
        FrontFaqDTO frontFaqDTO = frontFaqJpaService.findNextOneFaq(frontFaqEntity.getIdx());

        when(mockFrontFaqJpaService.findNextOneFaq(frontFaqEntity.getIdx())).thenReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaService.findNextOneFaq(frontFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockFrontFaqJpaService, times(1)).findNextOneFaq(frontFaqEntity.getIdx());
        verify(mockFrontFaqJpaService, atLeastOnce()).findNextOneFaq(frontFaqEntity.getIdx());
        verifyNoMoreInteractions(mockFrontFaqJpaService);

        InOrder inOrder = inOrder(mockFrontFaqJpaService);
        inOrder.verify(mockFrontFaqJpaService).findNextOneFaq(frontFaqEntity.getIdx());
    }

    @Test
    @DisplayName("다음 소속사 상세 조회 BDD 테스트")
    void 다음소속사상세조회BDD테스트() {
        // given
        FrontFaqEntity frontFaqEntity = FrontFaqEntity.builder().idx(2L).build();

        // when
        FrontFaqDTO frontFaqDTO = frontFaqJpaService.findNextOneFaq(frontFaqEntity.getIdx());

        when(mockFrontFaqJpaService.findNextOneFaq(frontFaqEntity.getIdx())).thenReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaService.findNextOneFaq(frontFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockFrontFaqJpaService).should(times(1)).findNextOneFaq(frontFaqEntity.getIdx());
        then(mockFrontFaqJpaService).should(atLeastOnce()).findNextOneFaq(frontFaqEntity.getIdx());
        then(mockFrontFaqJpaService).shouldHaveNoMoreInteractions();
    }
}
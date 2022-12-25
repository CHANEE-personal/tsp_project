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
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        faqMap.put("jpaStartPage", 1);
        faqMap.put("size", 3);

        List<FrontFaqDTO> returnFaqList = new ArrayList<>();

        returnFaqList.add(FrontFaqDTO.builder().idx(1L).title("FAQ테스트").description("FAQ테스트").visible("Y").build());
        returnFaqList.add(FrontFaqDTO.builder().idx(2L).title("FAQTest").description("FAQTest").visible("Y").build());

        // when
        when(mockFrontFaqJpaService.findFaqList(faqMap)).thenReturn(returnFaqList);
        List<FrontFaqDTO> faqList = mockFrontFaqJpaService.findFaqList(faqMap);

        // then
        assertAll(
                () -> assertThat(faqList).isNotEmpty(),
                () -> assertThat(faqList).hasSize(2)
        );

        assertThat(faqList.get(0).getIdx()).isEqualTo(returnFaqList.get(0).getIdx());
        assertThat(faqList.get(0).getTitle()).isEqualTo(returnFaqList.get(0).getTitle());
        assertThat(faqList.get(0).getDescription()).isEqualTo(returnFaqList.get(0).getDescription());
        assertThat(faqList.get(0).getVisible()).isEqualTo(returnFaqList.get(0).getVisible());

        // verify
        verify(mockFrontFaqJpaService, times(1)).findFaqList(faqMap);
        verify(mockFrontFaqJpaService, atLeastOnce()).findFaqList(faqMap);
        verifyNoMoreInteractions(mockFrontFaqJpaService);

        InOrder inOrder = inOrder(mockFrontFaqJpaService);
        inOrder.verify(mockFrontFaqJpaService).findFaqList(faqMap);
    }

    @Test
    @DisplayName("FAQ리스트조회BDD테스트")
    void FAQ리스트조회BDD테스트() {
        // given
        Map<String, Object> faqMap = new HashMap<>();
        faqMap.put("jpaStartPage", 1);
        faqMap.put("size", 3);

        List<FrontFaqDTO> returnFaqList = new ArrayList<>();

        returnFaqList.add(FrontFaqDTO.builder().idx(1L).title("FAQ테스트").description("FAQ테스트").visible("Y").build());
        returnFaqList.add(FrontFaqDTO.builder().idx(2L).title("FAQTest").description("FAQTest").visible("Y").build());

        // when
        given(mockFrontFaqJpaService.findFaqList(faqMap)).willReturn(returnFaqList);
        List<FrontFaqDTO> faqList = mockFrontFaqJpaService.findFaqList(faqMap);

        // then
        assertAll(
                () -> assertThat(faqList).isNotEmpty(),
                () -> assertThat(faqList).hasSize(2)
        );

        assertThat(faqList.get(0).getIdx()).isEqualTo(returnFaqList.get(0).getIdx());
        assertThat(faqList.get(0).getTitle()).isEqualTo(returnFaqList.get(0).getTitle());
        assertThat(faqList.get(0).getDescription()).isEqualTo(returnFaqList.get(0).getDescription());
        assertThat(faqList.get(0).getVisible()).isEqualTo(returnFaqList.get(0).getVisible());

        // verify
        then(mockFrontFaqJpaService).should(times(1)).findFaqList(faqMap);
        then(mockFrontFaqJpaService).should(atLeastOnce()).findFaqList(faqMap);
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
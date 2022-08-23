package com.tsp.new_tsp_front.api.faq.service;

import com.tsp.new_tsp_front.api.faq.domain.FrontFaqDTO;
import com.tsp.new_tsp_front.api.faq.domain.FrontFaqEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.api.faq.mapper.FaqMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("FAQ Service Test")
class FrontFaqJpaServiceTest {
    @Mock private FrontFaqJpaService mockFrontFaqJpaService;

    @Test
    @DisplayName("FAQ리스트조회Mockito테스트")
    void FAQ리스트조회Mockito테스트() {
        // given
        Map<String, Object> faqMap = new HashMap<>();
        faqMap.put("jpaStartPage", 1);
        faqMap.put("size", 3);

        List<FrontFaqDTO> returnFaqList = new ArrayList<>();

        returnFaqList.add(FrontFaqDTO.builder().idx(1).title("FAQ테스트").description("FAQ테스트").visible("Y").build());
        returnFaqList.add(FrontFaqDTO.builder().idx(2).title("FAQTest").description("FAQTest").visible("Y").build());

        // when
        when(mockFrontFaqJpaService.findFaqsList(faqMap)).thenReturn(returnFaqList);
        List<FrontFaqDTO> faqList = mockFrontFaqJpaService.findFaqsList(faqMap);

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
        verify(mockFrontFaqJpaService, times(1)).findFaqsList(faqMap);
        verify(mockFrontFaqJpaService, atLeastOnce()).findFaqsList(faqMap);
        verifyNoMoreInteractions(mockFrontFaqJpaService);

        InOrder inOrder = inOrder(mockFrontFaqJpaService);
        inOrder.verify(mockFrontFaqJpaService).findFaqsList(faqMap);
    }

    @Test
    @DisplayName("FAQ리스트조회BDD테스트")
    void FAQ리스트조회BDD테스트() {
        // given
        Map<String, Object> faqMap = new HashMap<>();
        faqMap.put("jpaStartPage", 1);
        faqMap.put("size", 3);

        List<FrontFaqDTO> returnFaqList = new ArrayList<>();

        returnFaqList.add(FrontFaqDTO.builder().idx(1).title("FAQ테스트").description("FAQ테스트").visible("Y").build());
        returnFaqList.add(FrontFaqDTO.builder().idx(2).title("FAQTest").description("FAQTest").visible("Y").build());

        // when
        given(mockFrontFaqJpaService.findFaqsList(faqMap)).willReturn(returnFaqList);
        List<FrontFaqDTO> faqList = mockFrontFaqJpaService.findFaqsList(faqMap);

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
        then(mockFrontFaqJpaService).should(times(1)).findFaqsList(faqMap);
        then(mockFrontFaqJpaService).should(atLeastOnce()).findFaqsList(faqMap);
        then(mockFrontFaqJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("FAQ상세조회Mockito테스트")
    void FAQ상세조회Mockito테스트() {
        // given
        FrontFaqEntity frontFaqEntity = FrontFaqEntity.builder().idx(1).title("FAQTest").description("FAQTest").build();
        FrontFaqDTO frontFaqDTO = INSTANCE.toDto(frontFaqEntity);

        // when
        when(mockFrontFaqJpaService.findOneFaq(frontFaqEntity)).thenReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaService.findOneFaq(frontFaqEntity);

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(frontFaqDTO.getIdx());
        assertThat(faqInfo.getTitle()).isEqualTo(frontFaqDTO.getTitle());
        assertThat(faqInfo.getDescription()).isEqualTo(frontFaqDTO.getDescription());
        assertThat(faqInfo.getVisible()).isEqualTo(frontFaqDTO.getVisible());

        // verify
        verify(mockFrontFaqJpaService, times(1)).findOneFaq(frontFaqEntity);
        verify(mockFrontFaqJpaService, atLeastOnce()).findOneFaq(frontFaqEntity);
        verifyNoMoreInteractions(mockFrontFaqJpaService);

        InOrder inOrder = inOrder(mockFrontFaqJpaService);
        inOrder.verify(mockFrontFaqJpaService).findOneFaq(frontFaqEntity);
    }

    @Test
    @DisplayName("FAQ상세조회BDD테스트")
    void FAQ상세조회BDD테스트() {
        // given
        FrontFaqEntity frontFaqEntity = FrontFaqEntity.builder().idx(1).title("FAQTest").description("FAQTest").build();
        FrontFaqDTO frontFaqDTO = INSTANCE.toDto(frontFaqEntity);

        // when
        when(mockFrontFaqJpaService.findOneFaq(frontFaqEntity)).thenReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaService.findOneFaq(frontFaqEntity);

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(frontFaqDTO.getIdx());
        assertThat(faqInfo.getTitle()).isEqualTo(frontFaqDTO.getTitle());
        assertThat(faqInfo.getDescription()).isEqualTo(frontFaqDTO.getDescription());
        assertThat(faqInfo.getVisible()).isEqualTo(frontFaqDTO.getVisible());

        // verify
        then(mockFrontFaqJpaService).should(times(1)).findOneFaq(frontFaqEntity);
        then(mockFrontFaqJpaService).should(atLeastOnce()).findOneFaq(frontFaqEntity);
        then(mockFrontFaqJpaService).shouldHaveNoMoreInteractions();
    }
}
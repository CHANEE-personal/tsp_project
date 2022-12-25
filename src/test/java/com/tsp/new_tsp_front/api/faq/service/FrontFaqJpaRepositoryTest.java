package com.tsp.new_tsp_front.api.faq.service;

import com.tsp.new_tsp_front.api.faq.domain.FrontFaqDTO;
import com.tsp.new_tsp_front.api.faq.domain.FrontFaqEntity;
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
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-local.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("FAQ Repository Test")
class FrontFaqJpaRepositoryTest {
    @Mock
    private final FrontFaqJpaRepository mockFrontFaqJpaRepository;
    private final FrontFaqJpaRepository frontFaqJpaRepository;
    private FrontFaqEntity frontFaqEntity;
    private FrontFaqDTO frontFaqDTO;

    private void createFaq() {
        frontFaqEntity = FrontFaqEntity.builder()
                .title("FAQ 테스트")
                .description("FAQ 테스트")
                .visible("Y")
                .viewCount(1)
                .build();

        frontFaqDTO = FrontFaqEntity.toDto(frontFaqEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createFaq();
    }

    @Test
    @DisplayName("FAQ 리스트 조회 테스트")
    void FAQ리스트조회테스트() {
        // given
        Map<String, Object> faqMap = new HashMap<>();
        faqMap.put("jpaStartPage", 0);
        faqMap.put("size", 3);
        faqMap.put("searchType", 0);
        faqMap.put("searchKeyword", "하하");

        // then
        assertThat(frontFaqJpaRepository.findFaqList(faqMap)).isNotEmpty();
    }

    @Test
    @DisplayName("FAQ 리스트 조회 Mockito 테스트")
    void FAQ리스트조회Mockito테스트() {
        // given
        Map<String, Object> faqMap = new HashMap<>();
        faqMap.put("jpaStartPage", 1);
        faqMap.put("size", 3);

        List<FrontFaqDTO> faqList = new ArrayList<>();
        faqList.add(FrontFaqDTO.builder().idx(1L).title("FAQ").description("FAQ").visible("Y").build());

        // when
        when(mockFrontFaqJpaRepository.findFaqList(faqMap)).thenReturn(faqList);
        List<FrontFaqDTO> newFaqList = mockFrontFaqJpaRepository.findFaqList(faqMap);

        // then
        assertThat(newFaqList.get(0).getIdx()).isEqualTo(faqList.get(0).getIdx());
        assertThat(newFaqList.get(0).getTitle()).isEqualTo(faqList.get(0).getTitle());
        assertThat(newFaqList.get(0).getDescription()).isEqualTo(faqList.get(0).getDescription());

        // verify
        verify(mockFrontFaqJpaRepository, times(1)).findFaqList(faqMap);
        verify(mockFrontFaqJpaRepository, atLeastOnce()).findFaqList(faqMap);
        verifyNoMoreInteractions(mockFrontFaqJpaRepository);

        InOrder inOrder = inOrder(mockFrontFaqJpaRepository);
        inOrder.verify(mockFrontFaqJpaRepository).findFaqList(faqMap);
    }

    @Test
    @DisplayName("FAQ 리스트 조회 BDD 테스트")
    void FAQ리스트조회BDD테스트() {
        // given
        Map<String, Object> faqMap = new HashMap<>();
        faqMap.put("jpaStartPage", 1);
        faqMap.put("size", 3);

        List<FrontFaqDTO> faqList = new ArrayList<>();
        faqList.add(FrontFaqDTO.builder().idx(1L).title("FAQ").description("FAQ").visible("Y").build());

        // when
        given(mockFrontFaqJpaRepository.findFaqList(faqMap)).willReturn(faqList);
        List<FrontFaqDTO> newFaqList = mockFrontFaqJpaRepository.findFaqList(faqMap);

        // then
        assertThat(newFaqList.get(0).getIdx()).isEqualTo(faqList.get(0).getIdx());
        assertThat(newFaqList.get(0).getTitle()).isEqualTo(faqList.get(0).getTitle());
        assertThat(newFaqList.get(0).getDescription()).isEqualTo(faqList.get(0).getDescription());

        // verify
        then(mockFrontFaqJpaRepository).should(times(1)).findFaqList(faqMap);
        then(mockFrontFaqJpaRepository).should(atLeastOnce()).findFaqList(faqMap);
        then(mockFrontFaqJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("FAQ상세Mockito조회테스트")
    void FAQ상세Mockito조회테스트() {
        // given
        FrontFaqEntity frontFaqEntity = FrontFaqEntity.builder()
                .idx(1L)
                .title("FAQ 테스트")
                .description("FAQ 테스트")
                .visible("Y")
                .build();

        frontFaqDTO = FrontFaqEntity.toDto(frontFaqEntity);

        // when
        when(mockFrontFaqJpaRepository.findOneFaq(frontFaqEntity.getIdx())).thenReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaRepository.findOneFaq(frontFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockFrontFaqJpaRepository, times(1)).findOneFaq(frontFaqEntity.getIdx());
        verify(mockFrontFaqJpaRepository, atLeastOnce()).findOneFaq(frontFaqEntity.getIdx());
        verifyNoMoreInteractions(mockFrontFaqJpaRepository);

        InOrder inOrder = inOrder(mockFrontFaqJpaRepository);
        inOrder.verify(mockFrontFaqJpaRepository).findOneFaq(frontFaqEntity.getIdx());
    }

    @Test
    @DisplayName("FAQ상세BDD조회테스트")
    void FAQ상세BDD조회테스트() {
        // given
        FrontFaqEntity frontFaqEntity = FrontFaqEntity.builder()
                .idx(1L)
                .title("FAQ 테스트")
                .description("FAQ 테스트")
                .visible("Y")
                .build();

        frontFaqDTO = FrontFaqEntity.toDto(frontFaqEntity);

        // when
        given(mockFrontFaqJpaRepository.findOneFaq(frontFaqEntity.getIdx())).willReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaRepository.findOneFaq(frontFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockFrontFaqJpaRepository).should(times(1)).findOneFaq(frontFaqEntity.getIdx());
        then(mockFrontFaqJpaRepository).should(atLeastOnce()).findOneFaq(frontFaqEntity.getIdx());
        then(mockFrontFaqJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 FAQ 상세 조회 테스트")
    void 이전or다음FAQ상세조회테스트() {
        // given
        frontFaqEntity = FrontFaqEntity.builder().idx(2L).build();

        // when
        frontFaqDTO = frontFaqJpaRepository.findOneFaq(frontFaqEntity.getIdx());

        // 이전 소속사
        assertThat(frontFaqJpaRepository.findPrevOneFaq(frontFaqEntity.getIdx()).getIdx()).isEqualTo(1);
        // 다음 소속사
        assertThat(frontFaqJpaRepository.findNextOneFaq(frontFaqEntity.getIdx()).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 FAQ 상세 조회 Mockito 테스트")
    void 이전FAQ상세조회Mockito테스트() {
        // given
        frontFaqEntity = FrontFaqEntity.builder().idx(2L).build();

        // when
        frontFaqDTO = frontFaqJpaRepository.findPrevOneFaq(frontFaqEntity.getIdx());

        when(mockFrontFaqJpaRepository.findPrevOneFaq(frontFaqEntity.getIdx())).thenReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaRepository.findPrevOneFaq(frontFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockFrontFaqJpaRepository, times(1)).findPrevOneFaq(frontFaqEntity.getIdx());
        verify(mockFrontFaqJpaRepository, atLeastOnce()).findPrevOneFaq(frontFaqEntity.getIdx());
        verifyNoMoreInteractions(mockFrontFaqJpaRepository);

        InOrder inOrder = inOrder(mockFrontFaqJpaRepository);
        inOrder.verify(mockFrontFaqJpaRepository).findPrevOneFaq(frontFaqEntity.getIdx());
    }

    @Test
    @DisplayName("이전 FAQ 상세 조회 BDD 테스트")
    void 이전FAQ상세조회BDD테스트() {
        // given
        frontFaqEntity = FrontFaqEntity.builder().idx(2L).build();

        // when
        frontFaqDTO = frontFaqJpaRepository.findPrevOneFaq(frontFaqEntity.getIdx());

        given(mockFrontFaqJpaRepository.findPrevOneFaq(frontFaqEntity.getIdx())).willReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaRepository.findPrevOneFaq(frontFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockFrontFaqJpaRepository).should(times(1)).findPrevOneFaq(frontFaqEntity.getIdx());
        then(mockFrontFaqJpaRepository).should(atLeastOnce()).findPrevOneFaq(frontFaqEntity.getIdx());
        then(mockFrontFaqJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 FAQ 상세 조회 Mockito 테스트")
    void 다음FAQ상세조회Mockito테스트() {
        // given
        frontFaqEntity = FrontFaqEntity.builder().idx(2L).build();

        // when
        frontFaqDTO = frontFaqJpaRepository.findNextOneFaq(frontFaqEntity.getIdx());

        when(mockFrontFaqJpaRepository.findNextOneFaq(frontFaqEntity.getIdx())).thenReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaRepository.findNextOneFaq(frontFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockFrontFaqJpaRepository, times(1)).findNextOneFaq(frontFaqEntity.getIdx());
        verify(mockFrontFaqJpaRepository, atLeastOnce()).findNextOneFaq(frontFaqEntity.getIdx());
        verifyNoMoreInteractions(mockFrontFaqJpaRepository);

        InOrder inOrder = inOrder(mockFrontFaqJpaRepository);
        inOrder.verify(mockFrontFaqJpaRepository).findNextOneFaq(frontFaqEntity.getIdx());
    }

    @Test
    @DisplayName("다음 소속사 상세 조회 BDD 테스트")
    void 다음소속사상세조회BDD테스트() {
        // given
        frontFaqEntity = FrontFaqEntity.builder().idx(2L).build();

        // when
        frontFaqDTO = frontFaqJpaRepository.findNextOneFaq(frontFaqEntity.getIdx());

        when(mockFrontFaqJpaRepository.findNextOneFaq(frontFaqEntity.getIdx())).thenReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaRepository.findNextOneFaq(frontFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockFrontFaqJpaRepository).should(times(1)).findNextOneFaq(frontFaqEntity.getIdx());
        then(mockFrontFaqJpaRepository).should(atLeastOnce()).findNextOneFaq(frontFaqEntity.getIdx());
        then(mockFrontFaqJpaRepository).shouldHaveNoMoreInteractions();
    }
}
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
class FrontFaqJpaQueryRepositoryTest {
    @Mock
    private final FrontFaqJpaQueryRepository mockFrontFaqJpaQueryRepository;
    private final FrontFaqJpaQueryRepository frontFaqJpaQueryRepository;
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
        faqMap.put("searchType", 0);
        faqMap.put("searchKeyword", "하하");
        PageRequest pageRequest = PageRequest.of(1, 3);

        // then
        assertThat(frontFaqJpaQueryRepository.findFaqList(faqMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("FAQ 리스트 조회 Mockito 테스트")
    void FAQ리스트조회Mockito테스트() {
        // given
        Map<String, Object> faqMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontFaqDTO> faqList = new ArrayList<>();
        faqList.add(FrontFaqDTO.builder().idx(1L).title("FAQ").description("FAQ").visible("Y").build());
        Page<FrontFaqDTO> resultFaq = new PageImpl<>(faqList, pageRequest, faqList.size());

        // when
        when(mockFrontFaqJpaQueryRepository.findFaqList(faqMap, pageRequest)).thenReturn(resultFaq);
        Page<FrontFaqDTO> newFaqList = mockFrontFaqJpaQueryRepository.findFaqList(faqMap, pageRequest);
        List<FrontFaqDTO> findFaqList = newFaqList.stream().collect(Collectors.toList());

        // then
        assertThat(findFaqList.get(0).getIdx()).isEqualTo(faqList.get(0).getIdx());
        assertThat(findFaqList.get(0).getTitle()).isEqualTo(faqList.get(0).getTitle());
        assertThat(findFaqList.get(0).getDescription()).isEqualTo(faqList.get(0).getDescription());

        // verify
        verify(mockFrontFaqJpaQueryRepository, times(1)).findFaqList(faqMap, pageRequest);
        verify(mockFrontFaqJpaQueryRepository, atLeastOnce()).findFaqList(faqMap, pageRequest);
        verifyNoMoreInteractions(mockFrontFaqJpaQueryRepository);

        InOrder inOrder = inOrder(mockFrontFaqJpaQueryRepository);
        inOrder.verify(mockFrontFaqJpaQueryRepository).findFaqList(faqMap, pageRequest);
    }

    @Test
    @DisplayName("FAQ 리스트 조회 BDD 테스트")
    void FAQ리스트조회BDD테스트() {
        // given
        Map<String, Object> faqMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<FrontFaqDTO> faqList = new ArrayList<>();
        faqList.add(FrontFaqDTO.builder().idx(1L).title("FAQ").description("FAQ").visible("Y").build());
        Page<FrontFaqDTO> resultFaq = new PageImpl<>(faqList, pageRequest, faqList.size());

        // when
        given(mockFrontFaqJpaQueryRepository.findFaqList(faqMap, pageRequest)).willReturn(resultFaq);
        Page<FrontFaqDTO> newFaqList = mockFrontFaqJpaQueryRepository.findFaqList(faqMap, pageRequest);
        List<FrontFaqDTO> findFaqList = newFaqList.stream().collect(Collectors.toList());

        // then
        assertThat(findFaqList.get(0).getIdx()).isEqualTo(faqList.get(0).getIdx());
        assertThat(findFaqList.get(0).getTitle()).isEqualTo(faqList.get(0).getTitle());
        assertThat(findFaqList.get(0).getDescription()).isEqualTo(faqList.get(0).getDescription());

        // verify
        then(mockFrontFaqJpaQueryRepository).should(times(1)).findFaqList(faqMap, pageRequest);
        then(mockFrontFaqJpaQueryRepository).should(atLeastOnce()).findFaqList(faqMap, pageRequest);
        then(mockFrontFaqJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 FAQ 상세 조회 Mockito 테스트")
    void 이전FAQ상세조회Mockito테스트() {
        // given
        frontFaqEntity = FrontFaqEntity.builder().idx(2L).build();

        // when
        frontFaqDTO = frontFaqJpaQueryRepository.findPrevOneFaq(frontFaqEntity.getIdx());

        when(mockFrontFaqJpaQueryRepository.findPrevOneFaq(frontFaqEntity.getIdx())).thenReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaQueryRepository.findPrevOneFaq(frontFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockFrontFaqJpaQueryRepository, times(1)).findPrevOneFaq(frontFaqEntity.getIdx());
        verify(mockFrontFaqJpaQueryRepository, atLeastOnce()).findPrevOneFaq(frontFaqEntity.getIdx());
        verifyNoMoreInteractions(mockFrontFaqJpaQueryRepository);

        InOrder inOrder = inOrder(mockFrontFaqJpaQueryRepository);
        inOrder.verify(mockFrontFaqJpaQueryRepository).findPrevOneFaq(frontFaqEntity.getIdx());
    }

    @Test
    @DisplayName("이전 FAQ 상세 조회 BDD 테스트")
    void 이전FAQ상세조회BDD테스트() {
        // given
        frontFaqEntity = FrontFaqEntity.builder().idx(2L).build();

        // when
        frontFaqDTO = frontFaqJpaQueryRepository.findPrevOneFaq(frontFaqEntity.getIdx());

        given(mockFrontFaqJpaQueryRepository.findPrevOneFaq(frontFaqEntity.getIdx())).willReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaQueryRepository.findPrevOneFaq(frontFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockFrontFaqJpaQueryRepository).should(times(1)).findPrevOneFaq(frontFaqEntity.getIdx());
        then(mockFrontFaqJpaQueryRepository).should(atLeastOnce()).findPrevOneFaq(frontFaqEntity.getIdx());
        then(mockFrontFaqJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 FAQ 상세 조회 Mockito 테스트")
    void 다음FAQ상세조회Mockito테스트() {
        // given
        frontFaqEntity = FrontFaqEntity.builder().idx(2L).build();

        // when
        frontFaqDTO = frontFaqJpaQueryRepository.findNextOneFaq(frontFaqEntity.getIdx());

        when(mockFrontFaqJpaQueryRepository.findNextOneFaq(frontFaqEntity.getIdx())).thenReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaQueryRepository.findNextOneFaq(frontFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockFrontFaqJpaQueryRepository, times(1)).findNextOneFaq(frontFaqEntity.getIdx());
        verify(mockFrontFaqJpaQueryRepository, atLeastOnce()).findNextOneFaq(frontFaqEntity.getIdx());
        verifyNoMoreInteractions(mockFrontFaqJpaQueryRepository);

        InOrder inOrder = inOrder(mockFrontFaqJpaQueryRepository);
        inOrder.verify(mockFrontFaqJpaQueryRepository).findNextOneFaq(frontFaqEntity.getIdx());
    }

    @Test
    @DisplayName("다음 소속사 상세 조회 BDD 테스트")
    void 다음소속사상세조회BDD테스트() {
        // given
        frontFaqEntity = FrontFaqEntity.builder().idx(2L).build();

        // when
        frontFaqDTO = frontFaqJpaQueryRepository.findNextOneFaq(frontFaqEntity.getIdx());

        when(mockFrontFaqJpaQueryRepository.findNextOneFaq(frontFaqEntity.getIdx())).thenReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaQueryRepository.findNextOneFaq(frontFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockFrontFaqJpaQueryRepository).should(times(1)).findNextOneFaq(frontFaqEntity.getIdx());
        then(mockFrontFaqJpaQueryRepository).should(atLeastOnce()).findNextOneFaq(frontFaqEntity.getIdx());
        then(mockFrontFaqJpaQueryRepository).shouldHaveNoMoreInteractions();
    }
}
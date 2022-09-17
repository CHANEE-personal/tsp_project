package com.tsp.new_tsp_front.api.faq.service;

import com.tsp.new_tsp_front.api.faq.domain.FrontFaqDTO;
import com.tsp.new_tsp_front.api.faq.domain.FrontFaqEntity;
import com.tsp.new_tsp_front.api.faq.mapper.FaqMapper;
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

        frontFaqDTO = FaqMapper.INSTANCE.toDto(frontFaqEntity);
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
        assertThat(frontFaqJpaRepository.findFaqsList(faqMap)).isNotEmpty();
    }

    @Test
    @DisplayName("FAQ 리스트 조회 Mockito 테스트")
    void FAQ리스트조회Mockito테스트() {
        // given
        Map<String, Object> faqMap = new HashMap<>();
        faqMap.put("jpaStartPage", 1);
        faqMap.put("size", 3);

        List<FrontFaqDTO> faqList = new ArrayList<>();
        faqList.add(FrontFaqDTO.builder().idx(1).title("FAQ").description("FAQ").visible("Y").build());

        // when
        when(mockFrontFaqJpaRepository.findFaqsList(faqMap)).thenReturn(faqList);
        List<FrontFaqDTO> newFaqList = mockFrontFaqJpaRepository.findFaqsList(faqMap);

        // then
        assertThat(newFaqList.get(0).getIdx()).isEqualTo(faqList.get(0).getIdx());
        assertThat(newFaqList.get(0).getTitle()).isEqualTo(faqList.get(0).getTitle());
        assertThat(newFaqList.get(0).getDescription()).isEqualTo(faqList.get(0).getDescription());

        // verify
        verify(mockFrontFaqJpaRepository, times(1)).findFaqsList(faqMap);
        verify(mockFrontFaqJpaRepository, atLeastOnce()).findFaqsList(faqMap);
        verifyNoMoreInteractions(mockFrontFaqJpaRepository);

        InOrder inOrder = inOrder(mockFrontFaqJpaRepository);
        inOrder.verify(mockFrontFaqJpaRepository).findFaqsList(faqMap);
    }

    @Test
    @DisplayName("FAQ 리스트 조회 BDD 테스트")
    void FAQ리스트조회BDD테스트() {
        // given
        Map<String, Object> faqMap = new HashMap<>();
        faqMap.put("jpaStartPage", 1);
        faqMap.put("size", 3);

        List<FrontFaqDTO> faqList = new ArrayList<>();
        faqList.add(FrontFaqDTO.builder().idx(1).title("FAQ").description("FAQ").visible("Y").build());

        // when
        given(mockFrontFaqJpaRepository.findFaqsList(faqMap)).willReturn(faqList);
        List<FrontFaqDTO> newFaqList = mockFrontFaqJpaRepository.findFaqsList(faqMap);

        // then
        assertThat(newFaqList.get(0).getIdx()).isEqualTo(faqList.get(0).getIdx());
        assertThat(newFaqList.get(0).getTitle()).isEqualTo(faqList.get(0).getTitle());
        assertThat(newFaqList.get(0).getDescription()).isEqualTo(faqList.get(0).getDescription());

        // verify
        then(mockFrontFaqJpaRepository).should(times(1)).findFaqsList(faqMap);
        then(mockFrontFaqJpaRepository).should(atLeastOnce()).findFaqsList(faqMap);
        then(mockFrontFaqJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("FAQ상세Mockito조회테스트")
    void FAQ상세Mockito조회테스트() {
        // given
        FrontFaqEntity frontFaqEntity = FrontFaqEntity.builder()
                .idx(1)
                .title("FAQ 테스트")
                .description("FAQ 테스트")
                .visible("Y")
                .build();

        frontFaqDTO = FaqMapper.INSTANCE.toDto(frontFaqEntity);

        // when
        when(mockFrontFaqJpaRepository.findOneFaq(frontFaqEntity)).thenReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaRepository.findOneFaq(frontFaqEntity);

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockFrontFaqJpaRepository, times(1)).findOneFaq(frontFaqEntity);
        verify(mockFrontFaqJpaRepository, atLeastOnce()).findOneFaq(frontFaqEntity);
        verifyNoMoreInteractions(mockFrontFaqJpaRepository);

        InOrder inOrder = inOrder(mockFrontFaqJpaRepository);
        inOrder.verify(mockFrontFaqJpaRepository).findOneFaq(frontFaqEntity);
    }

    @Test
    @DisplayName("FAQ상세BDD조회테스트")
    void FAQ상세BDD조회테스트() {
        // given
        FrontFaqEntity frontFaqEntity = FrontFaqEntity.builder()
                .idx(1)
                .title("FAQ 테스트")
                .description("FAQ 테스트")
                .visible("Y")
                .build();

        frontFaqDTO = FaqMapper.INSTANCE.toDto(frontFaqEntity);

        // when
        given(mockFrontFaqJpaRepository.findOneFaq(frontFaqEntity)).willReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaRepository.findOneFaq(frontFaqEntity);

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockFrontFaqJpaRepository).should(times(1)).findOneFaq(frontFaqEntity);
        then(mockFrontFaqJpaRepository).should(atLeastOnce()).findOneFaq(frontFaqEntity);
        then(mockFrontFaqJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 FAQ 상세 조회 테스트")
    void 이전or다음FAQ상세조회테스트() {
        // given
        frontFaqEntity = FrontFaqEntity.builder().idx(2).build();

        // when
        frontFaqDTO = frontFaqJpaRepository.findOneFaq(frontFaqEntity);

        // 이전 소속사
        assertThat(frontFaqJpaRepository.findPrevOneFaq(frontFaqEntity).getIdx()).isEqualTo(1);
        // 다음 소속사
        assertThat(frontFaqJpaRepository.findNextOneFaq(frontFaqEntity).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 FAQ 상세 조회 Mockito 테스트")
    void 이전FAQ상세조회Mockito테스트() {
        // given
        frontFaqEntity = FrontFaqEntity.builder().idx(2).build();

        // when
        frontFaqDTO = frontFaqJpaRepository.findPrevOneFaq(frontFaqEntity);

        when(mockFrontFaqJpaRepository.findPrevOneFaq(frontFaqEntity)).thenReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaRepository.findPrevOneFaq(frontFaqEntity);

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockFrontFaqJpaRepository, times(1)).findPrevOneFaq(frontFaqEntity);
        verify(mockFrontFaqJpaRepository, atLeastOnce()).findPrevOneFaq(frontFaqEntity);
        verifyNoMoreInteractions(mockFrontFaqJpaRepository);

        InOrder inOrder = inOrder(mockFrontFaqJpaRepository);
        inOrder.verify(mockFrontFaqJpaRepository).findPrevOneFaq(frontFaqEntity);
    }

    @Test
    @DisplayName("이전 FAQ 상세 조회 BDD 테스트")
    void 이전FAQ상세조회BDD테스트() {
        // given
        frontFaqEntity = FrontFaqEntity.builder().idx(2).build();

        // when
        frontFaqDTO = frontFaqJpaRepository.findPrevOneFaq(frontFaqEntity);

        given(mockFrontFaqJpaRepository.findPrevOneFaq(frontFaqEntity)).willReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaRepository.findPrevOneFaq(frontFaqEntity);

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockFrontFaqJpaRepository).should(times(1)).findPrevOneFaq(frontFaqEntity);
        then(mockFrontFaqJpaRepository).should(atLeastOnce()).findPrevOneFaq(frontFaqEntity);
        then(mockFrontFaqJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 FAQ 상세 조회 Mockito 테스트")
    void 다음FAQ상세조회Mockito테스트() {
        // given
        frontFaqEntity = FrontFaqEntity.builder().idx(2).build();

        // when
        frontFaqDTO = frontFaqJpaRepository.findNextOneFaq(frontFaqEntity);

        when(mockFrontFaqJpaRepository.findNextOneFaq(frontFaqEntity)).thenReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaRepository.findNextOneFaq(frontFaqEntity);

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockFrontFaqJpaRepository, times(1)).findNextOneFaq(frontFaqEntity);
        verify(mockFrontFaqJpaRepository, atLeastOnce()).findNextOneFaq(frontFaqEntity);
        verifyNoMoreInteractions(mockFrontFaqJpaRepository);

        InOrder inOrder = inOrder(mockFrontFaqJpaRepository);
        inOrder.verify(mockFrontFaqJpaRepository).findNextOneFaq(frontFaqEntity);
    }

    @Test
    @DisplayName("다음 소속사 상세 조회 BDD 테스트")
    void 다음소속사상세조회BDD테스트() {
        // given
        frontFaqEntity = FrontFaqEntity.builder().idx(2).build();

        // when
        frontFaqDTO = frontFaqJpaRepository.findNextOneFaq(frontFaqEntity);

        when(mockFrontFaqJpaRepository.findNextOneFaq(frontFaqEntity)).thenReturn(frontFaqDTO);
        FrontFaqDTO faqInfo = mockFrontFaqJpaRepository.findNextOneFaq(frontFaqEntity);

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockFrontFaqJpaRepository).should(times(1)).findNextOneFaq(frontFaqEntity);
        then(mockFrontFaqJpaRepository).should(atLeastOnce()).findNextOneFaq(frontFaqEntity);
        then(mockFrontFaqJpaRepository).shouldHaveNoMoreInteractions();
    }
}
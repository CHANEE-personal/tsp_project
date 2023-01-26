package com.tsp.common;

import com.tsp.api.model.domain.CareerJson;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.List;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
class CustomConverterTest {
    private final CustomConverter customConverter;

    @Test
    @DisplayName("convertToDataBaseColumnTest")
    void convertToDataBaseColumnTest() {
        List<CareerJson> careerList = of(new CareerJson("title", "text"));
        assertThat(customConverter.convertToDatabaseColumn(careerList))
                .isEqualTo("[{\"title\":\"title\",\"txt\":\"text\"}]");
    }

    @Test
    @DisplayName("convertToEntityAttributeTest")
    void convertToEntityAttributeTest() {
        String dbData = "[{\"title\":\"title\",\"txt\":\"txt\"}]";
        assertNotNull(customConverter.convertToEntityAttribute(dbData));
    }
}

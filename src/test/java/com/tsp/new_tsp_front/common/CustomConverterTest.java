package com.tsp.new_tsp_front.common;

import com.tsp.new_tsp_front.api.model.domain.CareerJson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomConverterTest {

    @Autowired
    private CustomConverter customConverter;

    @Test
    @DisplayName("convertToDataBaseColumnTest")
    public void convertToDataBaseColumnTest() {
        List<CareerJson> careerList = List.of(new CareerJson("title", "text"));
        assertThat(customConverter.convertToDatabaseColumn(careerList))
                .isEqualTo("[{\"title\":\"title\",\"txt\":\"text\"}]");
    }

    @Test
    @DisplayName("convertToEntityAttributeTest")
    public void convertToEntityAttributeTest() {
        String dbData = "[{\"title\":\"title\",\"txt\":\"txt\"}]";
        assertNotNull(customConverter.convertToEntityAttribute(dbData));
    }
}
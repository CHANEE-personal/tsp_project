package com.tsp.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.api.domain.model.CareerJson;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.util.Collections.emptyList;

@Converter
@Component
public class CustomConverter implements AttributeConverter<List<CareerJson>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(NON_NULL);

    @Override
    public String convertToDatabaseColumn(List<CareerJson> attribute) {
        if (attribute == null) return null;

        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CareerJson> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return emptyList();

        try {
            return objectMapper.readValue(dbData, List.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

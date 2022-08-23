package com.tsp.new_tsp_front.api.faq.mapper;

import com.tsp.new_tsp_front.api.faq.domain.FrontFaqDTO;
import com.tsp.new_tsp_front.api.faq.domain.FrontFaqEntity;
import com.tsp.new_tsp_front.common.mapStruct.StructMapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

public interface FaqMapper extends StructMapper<FrontFaqDTO, FrontFaqEntity> {
    FaqMapper INSTANCE = getMapper(FaqMapper.class);

    @Override
    FrontFaqDTO toDto(FrontFaqEntity entity);

    @Override
    FrontFaqEntity toEntity(FrontFaqDTO dto);

    @Override
    List<FrontFaqDTO> toDtoList(List<FrontFaqEntity> entityList);

    @Override
    List<FrontFaqEntity> toEntityList(List<FrontFaqDTO> dtoList);
}

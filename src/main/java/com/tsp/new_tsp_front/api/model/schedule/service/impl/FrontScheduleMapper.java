package com.tsp.new_tsp_front.api.model.schedule.service.impl;

import com.tsp.new_tsp_front.api.model.domain.schedule.FrontScheduleDTO;
import com.tsp.new_tsp_front.api.model.domain.schedule.FrontScheduleEntity;
import com.tsp.new_tsp_front.common.mapStruct.StructMapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

public interface FrontScheduleMapper extends StructMapper<FrontScheduleDTO, FrontScheduleEntity> {
    FrontScheduleMapper INSTANCE = getMapper(FrontScheduleMapper.class);

    @Override
    FrontScheduleDTO toDto(FrontScheduleEntity entity);

    @Override
    FrontScheduleEntity toEntity(FrontScheduleDTO dto);

    @Override
    List<FrontScheduleDTO> toDtoList(List<FrontScheduleEntity> entityList);

    @Override
    List<FrontScheduleEntity> toEntityList(List<FrontScheduleDTO> dtoList);
}

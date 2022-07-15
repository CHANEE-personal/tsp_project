package com.tsp.new_tsp_front.api.production.service.impl;

import com.tsp.new_tsp_front.api.production.domain.FrontProductionDTO;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity;
import com.tsp.new_tsp_front.common.mapStruct.StructMapper;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper
public interface ProductionMapper extends StructMapper<FrontProductionDTO, FrontProductionEntity> {
    ProductionMapper INSTANCE = getMapper(ProductionMapper.class);

    @Override
    FrontProductionDTO toDto(FrontProductionEntity entity);

    @Override
    FrontProductionEntity toEntity(FrontProductionDTO dto);

    @Override
    List<FrontProductionDTO> toDtoList(List<FrontProductionEntity> entityList);

    @Override
    List<FrontProductionEntity> toEntityList(List<FrontProductionDTO> dtoList);
}

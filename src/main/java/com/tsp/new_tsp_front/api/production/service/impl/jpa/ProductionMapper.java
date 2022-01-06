package com.tsp.new_tsp_front.api.production.service.impl.jpa;

import com.tsp.new_tsp_front.api.production.domain.FrontProductionDTO;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity;
import com.tsp.new_tsp_front.common.mapStruct.StructMapper;

import org.mapstruct.factory.Mappers;

import java.util.List;

public interface ProductionMapper extends StructMapper<FrontProductionDTO, FrontProductionEntity> {

	ProductionMapper INSTANCE = Mappers.getMapper(ProductionMapper.class);

	@Override
	FrontProductionDTO toDto(FrontProductionEntity entity);

	@Override
	FrontProductionEntity toEntity(FrontProductionDTO dto);

	@Override
	List<FrontProductionDTO> toDtoList(List<FrontProductionEntity> entityList);

	@Override
	List<FrontProductionEntity> toEntityList(List<FrontProductionDTO> dtoList);
}

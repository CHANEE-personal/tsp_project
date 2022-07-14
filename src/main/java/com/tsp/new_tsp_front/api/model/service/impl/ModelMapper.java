package com.tsp.new_tsp_front.api.model.service.impl;

import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import com.tsp.new_tsp_front.common.mapStruct.StructMapper;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper
public interface ModelMapper extends StructMapper<FrontModelDTO, FrontModelEntity> {
	ModelMapper INSTANCE = getMapper(ModelMapper.class);

	@Override
	FrontModelDTO toDto(FrontModelEntity entity);

	@Override
	FrontModelEntity toEntity(FrontModelDTO dto);

	@Override
	List<FrontModelDTO> toDtoList(List<FrontModelEntity> entityList);

	@Override
	List<FrontModelEntity> toEntityList(List<FrontModelDTO> dtoList);
}

package com.tsp.new_tsp_front.api.portfolio.service.impl;

import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioDTO;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioEntity;
import com.tsp.new_tsp_front.common.mapStruct.StructMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PortFolioMapper extends StructMapper<FrontPortFolioDTO, FrontPortFolioEntity> {

	PortFolioMapper INSTANCE = Mappers.getMapper(PortFolioMapper.class);

	@Override
	FrontPortFolioDTO toDto(FrontPortFolioEntity entity);

	@Override
	FrontPortFolioEntity toEntity(FrontPortFolioDTO dto);

	@Override
	List<FrontPortFolioDTO> toDtoList(List<FrontPortFolioEntity> entityList);

	@Override
	List<FrontPortFolioEntity> toEntityList(List<FrontPortFolioDTO> dtoList);
}

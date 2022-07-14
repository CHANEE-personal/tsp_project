package com.tsp.new_tsp_front.api.support.mapper;

import com.tsp.new_tsp_front.api.support.domain.FrontSupportDTO;
import com.tsp.new_tsp_front.api.support.domain.FrontSupportEntity;
import com.tsp.new_tsp_front.common.mapStruct.StructMapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

public interface SupportMapper extends StructMapper<FrontSupportDTO, FrontSupportEntity> {
	SupportMapper INSTANCE = getMapper(SupportMapper.class);

	@Override
	FrontSupportDTO toDto(FrontSupportEntity entity);

	@Override
	FrontSupportEntity toEntity(FrontSupportDTO dto);

	@Override
	List<FrontSupportDTO> toDtoList(List<FrontSupportEntity> entityList);

	@Override
	List<FrontSupportEntity> toEntityList(List<FrontSupportDTO> dtoList);
}

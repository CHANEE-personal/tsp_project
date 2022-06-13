package com.tsp.new_tsp_front.api.support.mapper;

import com.tsp.new_tsp_front.api.support.domain.FrontSupportDTO;
import com.tsp.new_tsp_front.api.support.domain.FrontSupportEntity;
import com.tsp.new_tsp_front.common.mapStruct.StructMapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface SupportMapper extends StructMapper<FrontSupportDTO, FrontSupportEntity> {

	SupportMapper INSTANCE = Mappers.getMapper(SupportMapper.class);

	@Override
	FrontSupportDTO toDto(FrontSupportEntity entity);

	@Override
	FrontSupportEntity toEntity(FrontSupportDTO dto);

	@Override
	List<FrontSupportDTO> toDtoList(List<FrontSupportEntity> entityList);

	@Override
	List<FrontSupportEntity> toEntityList(List<FrontSupportDTO> dtoList);
}

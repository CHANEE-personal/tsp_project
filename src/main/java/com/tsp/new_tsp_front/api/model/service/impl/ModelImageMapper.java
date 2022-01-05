package com.tsp.new_tsp_front.api.model.service.impl;

import com.tsp.new_tsp_front.api.common.domain.CommonImageDTO;
import com.tsp.new_tsp_front.api.common.domain.CommonImageEntity;
import com.tsp.new_tsp_front.common.mapStruct.StructMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ModelImageMapper extends StructMapper<CommonImageDTO, CommonImageEntity> {

	ModelImageMapper INSTANCE = Mappers.getMapper(ModelImageMapper.class);

	@Override
	CommonImageDTO toDto(CommonImageEntity entity);

	@Override
	CommonImageEntity toEntity(CommonImageDTO dto);

	@Override
	List<CommonImageDTO> toDtoList(List<CommonImageEntity> entityList);

	@Override
	List<CommonImageEntity> toEntityList(List<CommonImageDTO> dtoList);
}

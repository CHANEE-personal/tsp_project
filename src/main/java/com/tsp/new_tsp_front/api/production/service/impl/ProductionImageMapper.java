package com.tsp.new_tsp_front.api.production.service.impl;

import com.tsp.new_tsp_front.api.common.domain.CommonImageDTO;
import com.tsp.new_tsp_front.api.common.domain.CommonImageEntity;
import com.tsp.new_tsp_front.api.model.service.impl.ModelImageMapper;
import com.tsp.new_tsp_front.common.mapStruct.StructMapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface ProductionImageMapper extends StructMapper<CommonImageDTO, CommonImageEntity>  {
    ProductionImageMapper INSTANCE = Mappers.getMapper(ProductionImageMapper.class);

    @Override
    CommonImageDTO toDto(CommonImageEntity entity);

    @Override
    CommonImageEntity toEntity(CommonImageDTO dto);

    @Override
    List<CommonImageDTO> toDtoList(List<CommonImageEntity> entityList);

    @Override
    List<CommonImageEntity> toEntityList(List<CommonImageDTO> dtoList);
}

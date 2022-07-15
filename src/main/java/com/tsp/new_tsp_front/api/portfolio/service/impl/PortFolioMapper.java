package com.tsp.new_tsp_front.api.portfolio.service.impl;

import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioDTO;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioEntity;
import com.tsp.new_tsp_front.common.mapStruct.StructMapper;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper
public interface PortFolioMapper extends StructMapper<FrontPortFolioDTO, FrontPortFolioEntity> {
    PortFolioMapper INSTANCE = getMapper(PortFolioMapper.class);

    @Override
    FrontPortFolioDTO toDto(FrontPortFolioEntity entity);

    @Override
    FrontPortFolioEntity toEntity(FrontPortFolioDTO dto);

    @Override
    List<FrontPortFolioDTO> toDtoList(List<FrontPortFolioEntity> entityList);

    @Override
    List<FrontPortFolioEntity> toEntityList(List<FrontPortFolioDTO> dtoList);
}

package com.tsp.new_tsp_front.api.model.service.impl.Agency;

import com.tsp.new_tsp_front.api.model.domain.agency.FrontAgencyDTO;
import com.tsp.new_tsp_front.api.model.domain.agency.FrontAgencyEntity;
import com.tsp.new_tsp_front.common.mapStruct.StructMapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

public interface AgencyMapper extends StructMapper<FrontAgencyDTO, FrontAgencyEntity> {

    AgencyMapper INSTANCE = getMapper(AgencyMapper.class);

    @Override
    FrontAgencyDTO toDto(FrontAgencyEntity entity);

    @Override
    FrontAgencyEntity toEntity(FrontAgencyDTO dto);

    @Override
    List<FrontAgencyDTO> toDtoList(List<FrontAgencyEntity> entityList);

    @Override
    List<FrontAgencyEntity> toEntityList(List<FrontAgencyDTO> dtoList);
}
package com.tsp.new_tsp_front.api.model.negotiation.service.impl;

import com.tsp.new_tsp_front.api.model.domain.negotiation.FrontNegotiationDTO;
import com.tsp.new_tsp_front.api.model.domain.negotiation.FrontNegotiationEntity;
import com.tsp.new_tsp_front.api.model.schedule.service.impl.FrontScheduleMapper;
import com.tsp.new_tsp_front.common.mapStruct.StructMapper;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper
public interface FrontNegotiationMapper extends StructMapper<FrontNegotiationDTO, FrontNegotiationEntity> {

    FrontNegotiationMapper INSTANCE = getMapper(FrontNegotiationMapper.class);

    @Override
    FrontNegotiationDTO toDto(FrontNegotiationEntity entity);

    @Override
    FrontNegotiationEntity toEntity(FrontNegotiationDTO dto);

    @Override
    List<FrontNegotiationDTO> toDtoList(List<FrontNegotiationEntity> entityList);

    @Override
    List<FrontNegotiationEntity> toEntityList(List<FrontNegotiationDTO> dtoList);
}

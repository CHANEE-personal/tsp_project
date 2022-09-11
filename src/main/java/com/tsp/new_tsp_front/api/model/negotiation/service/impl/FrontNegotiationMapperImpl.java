package com.tsp.new_tsp_front.api.model.negotiation.service.impl;

import com.tsp.new_tsp_front.api.model.domain.negotiation.FrontNegotiationDTO;
import com.tsp.new_tsp_front.api.model.domain.negotiation.FrontNegotiationEntity;

import java.util.ArrayList;
import java.util.List;

public class FrontNegotiationMapperImpl implements FrontNegotiationMapper {

    @Override
    public FrontNegotiationDTO toDto(FrontNegotiationEntity entity) {
        if (entity == null) return null;

        return FrontNegotiationDTO.builder()
                .idx(entity.getIdx())
                .rnum(entity.getRnum())
                .modelIdx(entity.getModelIdx())
                .modelKorName(entity.getModelKorName())
                .modelNegotiationDesc(entity.getModelNegotiationDesc())
                .modelNegotiationDate(entity.getModelNegotiationDate())
                .name(entity.getName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    @Override
    public FrontNegotiationEntity toEntity(FrontNegotiationDTO dto) {
        if (dto == null) return null;

        return FrontNegotiationEntity.builder()
                .idx(dto.getIdx())
                .rnum(dto.getRnum())
                .modelIdx(dto.getModelIdx())
                .modelKorName(dto.getModelKorName())
                .modelNegotiationDesc(dto.getModelNegotiationDesc())
                .modelNegotiationDate(dto.getModelNegotiationDate())
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .visible(dto.getVisible())
                .creator(dto.getCreator())
                .createTime(dto.getCreateTime())
                .updater(dto.getUpdater())
                .updateTime(dto.getUpdateTime())
                .build();
    }

    @Override
    public List<FrontNegotiationDTO> toDtoList(List<FrontNegotiationEntity> entityList) {
        if (entityList == null) return null;

        List<FrontNegotiationDTO> list = new ArrayList<>(entityList.size());
        for (FrontNegotiationEntity frontNegotiationEntity : entityList) {
            list.add(toDto(frontNegotiationEntity));
        }

        return list;
    }

    @Override
    public List<FrontNegotiationEntity> toEntityList(List<FrontNegotiationDTO> dtoList) {
        if (dtoList == null) return null;

        List<FrontNegotiationEntity> list = new ArrayList<>(dtoList.size());
        for (FrontNegotiationDTO frontNegotiationDTO : dtoList) {
            list.add(toEntity(frontNegotiationDTO));
        }

        return list;
    }
}

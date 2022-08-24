package com.tsp.new_tsp_front.api.agency.service.impl;


import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyDTO;
import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyEntity;

import java.util.ArrayList;
import java.util.List;

public class AgencyMapperImpl implements AgencyMapper {

    @Override
    public FrontAgencyDTO toDto(FrontAgencyEntity entity) {
        if (entity == null) return null;

        return FrontAgencyDTO.builder()
                .idx(entity.getIdx())
                .rnum(entity.getRnum())
                .agencyName(entity.getAgencyName())
                .agencyDescription(entity.getAgencyDescription())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    @Override
    public FrontAgencyEntity toEntity(FrontAgencyDTO dto) {
        if (dto == null) return null;

        return FrontAgencyEntity.builder()
                .idx(dto.getIdx())
                .rnum(dto.getRnum())
                .agencyName(dto.getAgencyName())
                .agencyDescription(dto.getAgencyDescription())
                .visible(dto.getVisible())
                .creator(dto.getCreator())
                .createTime(dto.getCreateTime())
                .updater(dto.getUpdater())
                .updateTime(dto.getUpdateTime())
                .build();
    }

    @Override
    public List<FrontAgencyDTO> toDtoList(List<FrontAgencyEntity> entityList) {
        if (entityList == null) return null;

        List<FrontAgencyDTO> list = new ArrayList<>(entityList.size());
        for (FrontAgencyEntity frontAgencyEntity : entityList) {
            list.add(toDto(frontAgencyEntity));
        }

        return list;
    }

    @Override
    public List<FrontAgencyEntity> toEntityList(List<FrontAgencyDTO> dtoList) {
        if (dtoList == null) return null;

        List<FrontAgencyEntity> list = new ArrayList<>(dtoList.size());
        for (FrontAgencyDTO frontAgencyDTO : dtoList) {
            list.add(toEntity(frontAgencyDTO));
        }

        return list;
    }
}

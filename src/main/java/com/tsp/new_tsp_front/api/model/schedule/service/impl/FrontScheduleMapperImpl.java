package com.tsp.new_tsp_front.api.model.schedule.service.impl;

import com.tsp.new_tsp_front.api.model.domain.schedule.FrontScheduleDTO;
import com.tsp.new_tsp_front.api.model.domain.schedule.FrontScheduleEntity;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public class FrontScheduleMapperImpl implements FrontScheduleMapper {

    @Override
    public FrontScheduleDTO toDto(FrontScheduleEntity entity) {
        if (entity == null) return null;

        return FrontScheduleDTO.builder().idx(entity.getIdx())
                .rnum(entity.getRnum())
                .modelIdx(entity.getModelIdx())
                .modelSchedule(entity.getModelSchedule())
                .modelScheduleTime(entity.getModelScheduleTime())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    @Override
    public FrontScheduleEntity toEntity(FrontScheduleDTO dto) {
        if (dto == null) return null;

        return FrontScheduleEntity.builder()
                .rnum(dto.getRnum())
                .idx(dto.getIdx())
                .modelIdx(dto.getModelIdx())
                .modelSchedule(dto.getModelSchedule())
                .modelScheduleTime(dto.getModelScheduleTime())
                .visible(dto.getVisible())
                .creator(dto.getCreator())
                .createTime(dto.getCreateTime())
                .updater(dto.getUpdater())
                .updateTime(dto.getUpdateTime())
                .build();
    }

    @Override
    public List<FrontScheduleDTO> toDtoList(List<FrontScheduleEntity> entityList) {
        if (entityList == null) return null;

        List<FrontScheduleDTO> list = new ArrayList<>(entityList.size());
        for (FrontScheduleEntity frontScheduleEntity : entityList) {
            list.add(toDto(frontScheduleEntity));
        }

        return list;
    }

    @Override
    public List<FrontScheduleEntity> toEntityList(List<FrontScheduleDTO> dtoList) {
        if (dtoList == null) return null;

        List<FrontScheduleEntity> list = new ArrayList<>(dtoList.size());
        for (FrontScheduleDTO frontScheduleDTO : dtoList) {
            list.add(toEntity(frontScheduleDTO));
        }

        return list;
    }
}

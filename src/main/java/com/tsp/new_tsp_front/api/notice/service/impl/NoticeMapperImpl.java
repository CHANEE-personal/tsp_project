package com.tsp.new_tsp_front.api.notice.service.impl;

import com.tsp.new_tsp_front.api.notice.domain.FrontNoticeDTO;
import com.tsp.new_tsp_front.api.notice.domain.FrontNoticeEntity;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public class NoticeMapperImpl implements NoticeMapper {

    @Override
    public FrontNoticeDTO toDto(FrontNoticeEntity entity) {
        if (entity == null) return null;

        return FrontNoticeDTO.builder().idx(entity.getIdx())
                .rnum(entity.getRnum())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    @Override
    public FrontNoticeEntity toEntity(FrontNoticeDTO dto) {
        if (dto == null) return null;

        return FrontNoticeEntity.builder().idx(dto.getIdx())
                .rnum(dto.getRnum())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .visible(dto.getVisible())
                .creator(dto.getCreator())
                .createTime(dto.getCreateTime())
                .updater(dto.getUpdater())
                .updateTime(dto.getUpdateTime())
                .build();
    }

    @Override
    public List<FrontNoticeDTO> toDtoList(List<FrontNoticeEntity> entityList) {
        if (entityList == null) return null;

        List<FrontNoticeDTO> list = new ArrayList<>(entityList.size());
        for (FrontNoticeEntity frontNoticeEntity : entityList) {
            list.add(toDto(frontNoticeEntity));
        }

        return list;
    }

    @Override
    public List<FrontNoticeEntity> toEntityList(List<FrontNoticeDTO> dtoList) {
        if (dtoList == null) return null;

        List<FrontNoticeEntity> list = new ArrayList<>(dtoList.size());
        for (FrontNoticeDTO frontNoticeDTO : dtoList) {
            list.add(toEntity(frontNoticeDTO));
        }

        return list;
    }
}

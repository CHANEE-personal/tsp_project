package com.tsp.new_tsp_front.api.portfolio.service.impl;

import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioDTO;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioEntity;

import java.util.ArrayList;
import java.util.List;

public class PortFolioMapperImpl implements PortFolioMapper {

    @Override
    public FrontPortFolioDTO toDto(FrontPortFolioEntity entity) {
        if (entity == null) {
            return null;
        }

        return FrontPortFolioDTO.builder()
                .rnum(entity.getRnum())
                .idx(entity.getIdx())
                .categoryCd(entity.getCategoryCd())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .hashTag(entity.getHashTag())
                .videoUrl(entity.getVideoUrl())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .portfolioImage(PortFolioImageMapper.INSTANCE.toDtoList(entity.getCommonImageEntityList()))
                .build();
    }

    @Override
    public FrontPortFolioEntity toEntity(FrontPortFolioDTO dto) {

        if (dto == null) {
            return null;
        }

        return FrontPortFolioEntity.builder()
                .rnum(dto.getRnum())
                .idx(dto.getIdx())
                .categoryCd(dto.getCategoryCd())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .hashTag(dto.getHashTag())
                .videoUrl(dto.getVideoUrl())
                .visible(dto.getVisible())
                .creator(dto.getCreator())
                .createTime(dto.getCreateTime())
                .updater(dto.getUpdater())
                .updateTime(dto.getUpdateTime())
                .build();
    }

    @Override
    public List<FrontPortFolioDTO> toDtoList(List<FrontPortFolioEntity> entityList) {

        if (entityList == null) {
            return null;
        }

        List<FrontPortFolioDTO> list = new ArrayList<>(entityList.size());
        for (FrontPortFolioEntity frontPortFolioEntity : entityList) {
            list.add(toDto(frontPortFolioEntity));
        }

        return list;
    }

    @Override
    public List<FrontPortFolioEntity> toEntityList(List<FrontPortFolioDTO> dtoList) {

        if (dtoList == null) {
            return null;
        }

        List<FrontPortFolioEntity> list = new ArrayList<>(dtoList.size());
        for (FrontPortFolioDTO frontPortFolioDTO : dtoList) {
            list.add(toEntity(frontPortFolioDTO));
        }

        return list;
    }
}

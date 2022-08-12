package com.tsp.new_tsp_front.api.model.service.impl;

import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public class ModelMapperImpl implements ModelMapper {
    @Override
    public FrontModelDTO toDto(FrontModelEntity entity) {
        if (entity == null) return null;

        return FrontModelDTO.builder().idx(entity.getIdx())
                .rnum(entity.getRnum())
                .categoryCd(entity.getCategoryCd())
                .modelKorName(entity.getModelKorName())
                .modelEngName(entity.getModelEngName())
                .modelDescription(entity.getModelDescription())
                .visible(entity.getVisible())
                .height(entity.getHeight())
                .shoes(entity.getShoes())
                .size3(entity.getSize3())
                .categoryAge(entity.getCategoryAge())
                .modelMainYn(entity.getModelMainYn())
                .modelFirstName(entity.getModelFirstName())
                .modelSecondName(entity.getModelSecondName())
                .modelKorFirstName(entity.getModelKorFirstName())
                .modelKorSecondName(entity.getModelKorSecondName())
                .modelFavoriteCount(entity.getModelFavoriteCount())
                .careerList(entity.getCareerList())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .modelImage(ModelImageMapper.INSTANCE.toDtoList(entity.getCommonImageEntityList()))
                .build();
    }

    @Override
    public FrontModelEntity toEntity(FrontModelDTO dto) {
        if (dto == null) return null;

        return FrontModelEntity.builder()
                .rnum(dto.getRnum())
                .idx(dto.getIdx())
                .categoryCd(dto.getCategoryCd())
                .modelKorName(dto.getModelKorName())
                .modelEngName(dto.getModelEngName())
                .modelDescription(dto.getModelDescription())
                .visible(dto.getVisible())
                .height(dto.getHeight())
                .shoes(dto.getShoes())
                .size3(dto.getSize3())
                .categoryAge(dto.getCategoryAge())
                .modelMainYn(dto.getModelMainYn())
                .modelFirstName(dto.getModelFirstName())
                .modelSecondName(dto.getModelSecondName())
                .modelKorFirstName(dto.getModelKorFirstName())
                .modelKorSecondName(dto.getModelKorSecondName())
                .modelFavoriteCount(dto.getModelFavoriteCount())
                .careerList(dto.getCareerList())
                .creator(dto.getCreator())
                .createTime(dto.getCreateTime())
                .updater(dto.getUpdater())
                .updateTime(dto.getUpdateTime())
                .build();
    }

    @Override
    public List<FrontModelDTO> toDtoList(List<FrontModelEntity> entityList) {
        if (entityList == null) return null;

        List<FrontModelDTO> list = new ArrayList<>(entityList.size());
        for (FrontModelEntity frontModelEntity : entityList) {
            list.add(toDto(frontModelEntity));
        }

        return list;
    }

    @Override
    public List<FrontModelEntity> toEntityList(List<FrontModelDTO> dtoList) {
        if (dtoList == null) return null;

        List<FrontModelEntity> list = new ArrayList<>(dtoList.size());
        for (FrontModelDTO frontModelDTO : dtoList) {
            list.add(toEntity(frontModelDTO));
        }

        return list;
    }
}

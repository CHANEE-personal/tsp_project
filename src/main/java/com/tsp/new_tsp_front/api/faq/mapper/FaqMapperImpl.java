package com.tsp.new_tsp_front.api.faq.mapper;

import com.tsp.new_tsp_front.api.faq.domain.FrontFaqDTO;
import com.tsp.new_tsp_front.api.faq.domain.FrontFaqEntity;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public class FaqMapperImpl implements FaqMapper {

    @Override
    public FrontFaqDTO toDto(FrontFaqEntity entity) {
        if (entity == null) return null;

        return FrontFaqDTO.builder().idx(entity.getIdx())
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
    public FrontFaqEntity toEntity(FrontFaqDTO dto) {
        if (dto == null) return null;

        return FrontFaqEntity.builder().idx(dto.getIdx())
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
    public List<FrontFaqDTO> toDtoList(List<FrontFaqEntity> entityList) {
        if (entityList == null) return null;

        List<FrontFaqDTO> list = new ArrayList<>(entityList.size());
        for (FrontFaqEntity frontFaqEntity : entityList) {
            list.add(toDto(frontFaqEntity));
        }

        return list;
    }

    @Override
    public List<FrontFaqEntity> toEntityList(List<FrontFaqDTO> dtoList) {
        if (dtoList == null) return null;

        List<FrontFaqEntity> list = new ArrayList<>(dtoList.size());
        for (FrontFaqDTO frontFaqDTO : dtoList) {
            list.add(toEntity(frontFaqDTO));
        }

        return list;
    }
}

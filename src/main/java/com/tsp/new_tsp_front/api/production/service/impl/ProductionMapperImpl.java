package com.tsp.new_tsp_front.api.production.service.impl;

import com.tsp.new_tsp_front.api.production.domain.FrontProductionDTO;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity;

import java.util.ArrayList;
import java.util.List;

import static com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity.*;

public class ProductionMapperImpl implements ProductionMapper {

	@Override
	public FrontProductionDTO toDto(FrontProductionEntity entity) {
		if(entity == null) return null;

		return FrontProductionDTO.builder()
				.rnum(entity.getRnum())
				.idx(entity.getIdx())
				.title(entity.getTitle())
				.description(entity.getDescription())
				.visible(entity.getVisible())
				.creator(entity.getCreator())
				.createTime(entity.getCreateTime())
				.updater(entity.getUpdater())
				.updateTime(entity.getUpdateTime())
				.productionImage(ProductionImageMapperImpl.INSTANCE.toDtoList(entity.getCommonImageEntityList()))
				.build();
	}

	@Override
	public FrontProductionEntity toEntity(FrontProductionDTO dto) {
		if(dto == null) return null;

		return builder()
				.rnum(dto.getRnum())
				.idx(dto.getIdx())
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
	public List<FrontProductionDTO> toDtoList(List<FrontProductionEntity> entityList) {
		if(entityList == null) return null;

		List<FrontProductionDTO> list = new ArrayList<>(entityList.size());
		for(FrontProductionEntity frontProductionEntity : entityList) {
			list.add(toDto(frontProductionEntity));
		}

		return list;
	}

	@Override
	public List<FrontProductionEntity> toEntityList(List<FrontProductionDTO> dtoList) {
		if(dtoList == null) return null;

		List<FrontProductionEntity> list = new ArrayList<>(dtoList.size());
		for(FrontProductionDTO frontProductionDTO : dtoList) {
			list.add(toEntity(frontProductionDTO));
		}

		return list;
	}
}

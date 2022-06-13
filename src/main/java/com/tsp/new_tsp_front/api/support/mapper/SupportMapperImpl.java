package com.tsp.new_tsp_front.api.support.mapper;

import com.tsp.new_tsp_front.api.support.domain.FrontSupportDTO;
import com.tsp.new_tsp_front.api.support.domain.FrontSupportEntity;

import java.util.ArrayList;
import java.util.List;

public class SupportMapperImpl implements SupportMapper {

	@Override
	public FrontSupportDTO toDto(FrontSupportEntity entity) {
		if(entity == null) {
			return null;
		}

		return FrontSupportDTO.builder()
				.rnum(entity.getRnum())
				.idx(entity.getIdx())
				.supportName(entity.getSupportName())
				.supportHeight(entity.getSupportHeight())
				.supportSize3(entity.getSupportSize3())
				.supportInstagram(entity.getSupportInstagram())
				.supportPhone(entity.getSupportPhone())
				.supportMessage(entity.getSupportMessage())
				.visible(entity.getVisible())
				.supportTime(entity.getSupportTime())
				.build();
	}

	@Override
	public FrontSupportEntity toEntity(FrontSupportDTO dto) {
		if(dto == null) {
			return null;
		}

		return FrontSupportEntity.builder()
				.rnum(dto.getRnum())
				.idx(dto.getIdx())
				.supportName(dto.getSupportName())
				.supportHeight(dto.getSupportHeight())
				.supportSize3(dto.getSupportSize3())
				.supportInstagram(dto.getSupportInstagram())
				.supportPhone(dto.getSupportPhone())
				.supportMessage(dto.getSupportMessage())
				.supportTime(dto.getSupportTime())
				.visible(dto.getVisible())
				.build();
	}

	@Override
	public List<FrontSupportDTO> toDtoList(List<FrontSupportEntity> entityList) {
		if(entityList == null) {
			return null;
		}

		List<FrontSupportDTO> list = new ArrayList<>(entityList.size());
		for(FrontSupportEntity frontSupportEntity : entityList) {
			list.add(toDto(frontSupportEntity));
		}

		return list;
	}

	@Override
	public List<FrontSupportEntity> toEntityList(List<FrontSupportDTO> dtoList) {
		if(dtoList == null) {
			return null;
		}

		List<FrontSupportEntity> list = new ArrayList<>(dtoList.size());
		for(FrontSupportDTO frontSupportDTO : dtoList) {
			list.add(toEntity(frontSupportDTO));
		}

		return list;
	}
}

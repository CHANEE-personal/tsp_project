package com.tsp.new_tsp_front.api.notice.service.impl;

import com.tsp.new_tsp_front.api.notice.domain.FrontNoticeDTO;
import com.tsp.new_tsp_front.api.notice.domain.FrontNoticeEntity;
import com.tsp.new_tsp_front.common.mapStruct.StructMapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

public interface NoticeMapper extends StructMapper<FrontNoticeDTO, FrontNoticeEntity> {
    NoticeMapper INSTANCE = getMapper(NoticeMapper.class);

    @Override
    FrontNoticeDTO toDto(FrontNoticeEntity entity);

    @Override
    FrontNoticeEntity toEntity(FrontNoticeDTO dto);

    @Override
    List<FrontNoticeDTO> toDtoList(List<FrontNoticeEntity> entityList);

    @Override
    List<FrontNoticeEntity> toEntityList(List<FrontNoticeDTO> dtoList);
}

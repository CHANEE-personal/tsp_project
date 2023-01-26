package com.tsp.common.mapStruct;

import java.util.List;

public interface StructMapper<D, E> {
    D toDto(E entity);

    E toEntity(D dto);

    List<D> toDtoList(List<E> entityList);

    List<E> toEntityList(List<D> dtoList);
}

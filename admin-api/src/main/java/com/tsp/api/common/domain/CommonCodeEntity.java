package com.tsp.api.common.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "tsp_cmm_code")
public class CommonCodeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "category_cd")
    private Integer categoryCd;

    @Column(name = "category_nm")
    private String categoryNm;

    @Column(name = "visible")
    private String visible;

    @Column(name = "cmm_type")
    private String cmmType;

    public static CommonCodeDTO toDto(CommonCodeEntity entity) {
        if (entity == null) return null;
        return CommonCodeDTO.builder()
                .idx(entity.getIdx())
                .categoryCd(entity.getCategoryCd())
                .categoryNm(entity.getCategoryNm())
                .cmmType(entity.getCmmType())
                .visible(entity.getVisible())
                .build();
    }

    public static List<CommonCodeDTO> toDtoList(List<CommonCodeEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(CommonCodeEntity::toDto)
                .collect(Collectors.toList());
    }
}

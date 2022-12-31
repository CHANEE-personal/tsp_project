package com.tsp.new_tsp_front.api.production.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsp.new_tsp_front.api.common.domain.CommonImageEntity;
import com.tsp.new_tsp_front.api.common.domain.NewCommonMappedClass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tsp_production")
public class FrontProductionEntity extends NewCommonMappedClass {
    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "title")
    @NotEmpty(message = "프로덕션 제목 입력은 필수입니다.")
    private String title;

    @Column(name = "description")
    @Lob
    @NotEmpty(message = "프로덕션 상세내용 입력은 필수입니다.")
    private String description;

    @Column(name = "view_count")
    private int viewCount;

    @Column(name = "visible")
    @NotEmpty(message = "프로덕션 노출 여부 선택은 필수입니다.")
    private String visible;

    @JsonIgnore
    @OneToMany(mappedBy = "frontProductionEntity", fetch = LAZY)
    private List<CommonImageEntity> commonImageEntityList = new ArrayList<>();

    public static FrontProductionDTO toDto(FrontProductionEntity entity) {
        if (entity == null) return null;
        return FrontProductionDTO.builder()
                .rowNum(entity.getRowNum())
                .idx(entity.getIdx())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .viewCount(entity.getViewCount())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .productionImage(CommonImageEntity.toDtoList(entity.getCommonImageEntityList()))
                .build();
    }

    public static List<FrontProductionDTO> toDtoList(List<FrontProductionEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(FrontProductionEntity::toDto)
                .collect(Collectors.toList());
    }
}

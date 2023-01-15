package com.tsp.new_tsp_front.api.model.domain.search;

import com.tsp.new_tsp_front.api.common.domain.NewCommonMappedClass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "tsp_search")
public class FrontSearchEntity extends NewCommonMappedClass {
    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "search_keyword")
    private String searchKeyword;

    public static FrontSearchDTO toDto(FrontSearchEntity entity) {
        if (entity == null) return null;
        return FrontSearchDTO.builder()
                .rowNum(entity.getRowNum())
                .idx(entity.getIdx())
                .searchKeyword(entity.getSearchKeyword())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    public static List<FrontSearchDTO> toDtoList(List<FrontSearchEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(FrontSearchEntity::toDto)
                .collect(Collectors.toList());
    }
}

package com.tsp.new_tsp_front.api.model.domain.recommend;

import com.tsp.new_tsp_front.api.common.domain.NewCommonMappedClass;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Table(name = "tsp_recommend")
public class FrontRecommendEntity extends NewCommonMappedClass {
    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Type(type = "json")
    @Column(columnDefinition = "json", name = "recommend_keyword")
    private List<String> recommendKeyword = new ArrayList<>();

    public static FrontRecommendDTO toDto(FrontRecommendEntity entity) {
        if (entity == null) return null;
        return FrontRecommendDTO.builder()
                .rowNum(entity.getRowNum())
                .idx(entity.getIdx())
                .recommendKeyword(entity.getRecommendKeyword())
                .build();
    }

    public static List<FrontRecommendDTO> toDtoList(List<FrontRecommendEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(FrontRecommendEntity::toDto)
                .collect(Collectors.toList());
    }
}

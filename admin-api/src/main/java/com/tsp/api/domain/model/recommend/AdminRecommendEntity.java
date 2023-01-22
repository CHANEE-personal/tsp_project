package com.tsp.api.domain.model.recommend;

import com.tsp.api.domain.common.NewCommonMappedClass;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
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
@NoArgsConstructor
@TypeDef(name = "json", typeClass = JsonStringType.class)
@DynamicUpdate
@Table(name = "tsp_recommend")
public class AdminRecommendEntity extends NewCommonMappedClass {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Type(type = "json")
    @Column(columnDefinition = "json", name = "recommend_keyword")
    private List<String> recommendKeyword = new ArrayList<>();

    public void update(AdminRecommendEntity adminRecommendEntity) {
        this.recommendKeyword = adminRecommendEntity.recommendKeyword;
    }

    public static AdminRecommendDTO toDto(AdminRecommendEntity entity) {
        if (entity == null) return null;
        return AdminRecommendDTO.builder()
                .idx(entity.getIdx())
                .recommendKeyword(entity.getRecommendKeyword())
                .build();
    }

    public static List<AdminRecommendDTO> toDtoList(List<AdminRecommendEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(AdminRecommendEntity::toDto)
                .collect(Collectors.toList());
    }
}

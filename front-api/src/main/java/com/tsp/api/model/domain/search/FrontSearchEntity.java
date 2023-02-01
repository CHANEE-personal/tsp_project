package com.tsp.api.model.domain.search;

import com.tsp.api.common.domain.NewCommonMappedClass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tsp_search")
public class FrontSearchEntity extends NewCommonMappedClass {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "search_keyword")
    private String searchKeyword;

    public static FrontSearchDTO toDto(FrontSearchEntity entity) {
        if (entity == null) return null;
        return FrontSearchDTO.builder()
                .idx(entity.getIdx())
                .searchKeyword(entity.getSearchKeyword())
                .build();
    }

    public static List<FrontSearchDTO> toDtoList(List<FrontSearchEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(FrontSearchEntity::toDto)
                .collect(Collectors.toList());
    }
}

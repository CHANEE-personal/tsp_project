package com.tsp.new_tsp_front.api.agency.domain;

import com.tsp.new_tsp_front.api.common.domain.CommonImageEntity;
import com.tsp.new_tsp_front.api.common.domain.NewCommonMappedClass;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tsp_agency")
public class FrontAgencyEntity extends NewCommonMappedClass {
    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "agency_name")
    @NotEmpty(message = "소속사명 입력은 필수입니다.")
    private String agencyName;

    @Column(name = "agency_description")
    @Lob
    @NotEmpty(message = "소속사 상세내용 입력은 필수입니다.")
    private String agencyDescription;

    @Column(name = "favorite_count")
    private Integer favoriteCount;

    @Column(name = "visible")
    @NotEmpty(message = "소속사 노출 여부 선택은 필수입니다.")
    private String visible;

    @OneToMany(mappedBy = "frontAgencyEntity")
    private List<CommonImageEntity> commonImageEntityList = new ArrayList<>();

    @OneToOne(mappedBy = "frontAgencyEntity", cascade = ALL, fetch = LAZY)
    private FrontModelEntity frontModelEntity;

    public void updateFavoriteCount() {
        this.favoriteCount++;
    }

    public static FrontAgencyDTO toDto(FrontAgencyEntity entity) {
        if (entity == null) return null;
        return FrontAgencyDTO.builder()
                .idx(entity.getIdx())
                .rowNum(entity.getRowNum())
                .agencyName(entity.getAgencyName())
                .agencyDescription(entity.getAgencyDescription())
                .favoriteCount(entity.getFavoriteCount())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    public static List<FrontAgencyDTO> toDtoList(List<FrontAgencyEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(FrontAgencyEntity::toDto)
                .collect(Collectors.toList());
    }
}

package com.tsp.api.production.domain;

import com.tsp.api.common.domain.CommonImageEntity;
import com.tsp.api.common.domain.NewCommonMappedClass;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "tsp_production")
public class AdminProductionEntity extends NewCommonMappedClass {

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

    @BatchSize(size = 5)
    @Where(clause = "type_name = 'production'")
    @OneToMany(mappedBy = "adminProductionEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CommonImageEntity> commonImageEntityList = new ArrayList<>();

    public void update(AdminProductionEntity adminProductionEntity) {
        this.title = adminProductionEntity.title;
        this.description = adminProductionEntity.description;
        this.visible = adminProductionEntity.visible;
    }

    public void addImage(CommonImageEntity commonImageEntity) {
        commonImageEntity.setAdminProductionEntity(this);
        this.commonImageEntityList.add(commonImageEntity);
    }

    public static AdminProductionDto toDto(AdminProductionEntity entity) {
        if (entity == null) return null;
        return AdminProductionDto.builder()
                .idx(entity.idx)
                .title(entity.title)
                .description(entity.description)
                .viewCount(entity.viewCount)
                .visible(entity.visible)
                .productionImage(CommonImageEntity.toDtoList(entity.commonImageEntityList))
                .build();
    }

    public static List<AdminProductionDto> toDtoList(List<AdminProductionEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(AdminProductionEntity::toDto)
                .collect(Collectors.toList());
    }
}

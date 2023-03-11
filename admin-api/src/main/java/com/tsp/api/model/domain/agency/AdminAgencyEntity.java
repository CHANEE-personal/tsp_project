package com.tsp.api.model.domain.agency;

import com.tsp.api.common.domain.CommonImageEntity;
import com.tsp.api.common.domain.NewCommonMappedClass;
import com.tsp.api.model.domain.AdminModelEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@Table(name = "tsp_agency")
public class AdminAgencyEntity extends NewCommonMappedClass {

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
    private int favoriteCount;

    @Column(name = "visible")
    @NotEmpty(message = "소속사 노출 여부 선택은 필수입니다.")
    private String visible;

    @Builder.Default
    @Where(clause = "type_name = 'agency'")
    @OneToMany(mappedBy = "adminAgencyEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CommonImageEntity> commonImageEntityList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "adminAgencyEntity", fetch = LAZY)
    private List<AdminModelEntity> adminModelEntity = new ArrayList<>();

    public void update(AdminAgencyEntity adminAgency) {
        this.agencyName = adminAgency.agencyName;
        this.agencyDescription = adminAgency.agencyDescription;
        this.visible = adminAgency.visible;
    }

    public void addAgency(AdminModelEntity adminModelEntity) {
        adminModelEntity.setAdminAgencyEntity(this);
    }

    public void addImage(CommonImageEntity commonImageEntity) {
        commonImageEntity.setAdminAgencyEntity(this);
        this.commonImageEntityList.add(commonImageEntity);
    }

    public static AdminAgencyDto toDto(AdminAgencyEntity entity) {
        if (entity == null) return null;
        return AdminAgencyDto.builder()
                .idx(entity.idx)
                .agencyName(entity.agencyName)
                .agencyDescription(entity.agencyDescription)
                .favoriteCount(entity.favoriteCount)
                .visible(entity.visible)
                .agencyImage(CommonImageEntity.toDtoList(entity.commonImageEntityList))
                .build();
    }

    public static List<AdminAgencyDto> toDtoList(List<AdminAgencyEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(AdminAgencyEntity::toDto)
                .collect(Collectors.toList());
    }
}

package com.tsp.api.domain.model.agency;

import com.tsp.api.domain.common.CommonImageEntity;
import com.tsp.api.domain.common.NewCommonMappedClass;
import com.tsp.api.domain.model.AdminModelEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
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
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private Integer favoriteCount;

    @Column(name = "visible")
    @NotEmpty(message = "소속사 노출 여부 선택은 필수입니다.")
    private String visible;

    @Where(clause = "type_name = 'agency'")
    @OneToMany(mappedBy = "adminAgencyEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CommonImageEntity> commonImageEntityList = new ArrayList<>();

    @OneToOne(mappedBy = "adminAgencyEntity", fetch = LAZY)
    private AdminModelEntity adminModelEntity;

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

    public static AdminAgencyDTO toDto(AdminAgencyEntity entity) {
        if (entity == null) return null;
        return AdminAgencyDTO.builder()
                .idx(entity.getIdx())
                .agencyName(entity.getAgencyName())
                .agencyDescription(entity.getAgencyDescription())
                .favoriteCount(entity.getFavoriteCount())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .agencyImage(CommonImageEntity.toDtoList(entity.getCommonImageEntityList()))
                .build();
    }

    public static List<AdminAgencyDTO> toDtoList(List<AdminAgencyEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(AdminAgencyEntity::toDto)
                .collect(Collectors.toList());
    }
}

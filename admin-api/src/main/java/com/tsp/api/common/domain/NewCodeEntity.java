package com.tsp.api.common.domain;

import com.tsp.api.model.domain.AdminModelEntity;
import com.tsp.api.portfolio.domain.AdminPortFolioEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@Table(name = "tsp_cmm_code")
public class NewCodeEntity extends NewCommonMappedClass {

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

    @Builder.Default
    @Where(clause = "type_name = 'model'")
    @OneToMany(mappedBy = "newModelCodeJpaDTO", cascade = { PERSIST, MERGE }, fetch = LAZY)
    private List<AdminModelEntity> adminModelEntityList = new ArrayList<>();

    @Builder.Default
    @Where(clause = "type_name = 'portfolio'")
    @OneToMany(mappedBy = "newPortFolioJpaDTO", cascade = { PERSIST, MERGE }, fetch = LAZY)
    private List<AdminPortFolioEntity> adminPortFolioEntityList = new ArrayList<>();

    public void update(NewCodeEntity newCodeEntity) {
        this.categoryNm = newCodeEntity.categoryNm;
        this.visible = newCodeEntity.visible;
    }

    public void addModel(AdminModelEntity adminModelEntity) {
        adminModelEntity.setNewModelCodeJpaDTO(this);
        this.adminModelEntityList.add(adminModelEntity);
    }

    public void addPortfolio(AdminPortFolioEntity adminPortFolioEntity) {
        adminPortFolioEntity.setNewPortFolioJpaDTO(this);
        this.adminPortFolioEntityList.add(adminPortFolioEntity);
    }

    public static NewCodeDto toDto(NewCodeEntity entity) {
        if (entity == null) return null;
        return NewCodeDto.builder()
                .idx(entity.idx)
                .categoryCd(entity.categoryCd)
                .categoryNm(entity.categoryNm)
                .cmmType(entity.cmmType)
                .visible(entity.visible)
                .build();
    }

    public static List<NewCodeDto> toDtoList(List<NewCodeEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(NewCodeEntity::toDto)
                .collect(Collectors.toList());
    }
}

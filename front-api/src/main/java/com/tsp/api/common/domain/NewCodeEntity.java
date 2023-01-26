package com.tsp.api.common.domain;

import com.tsp.api.model.domain.FrontModelEntity;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.*;

@Getter
@Setter
@Entity
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    @OneToMany(mappedBy = "newModelCodeJpaDTO", cascade = MERGE, fetch = LAZY)
    private List<FrontModelEntity> frontModelEntityList = new ArrayList<>();
}

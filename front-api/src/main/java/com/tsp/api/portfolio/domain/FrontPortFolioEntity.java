package com.tsp.api.portfolio.domain;

import com.tsp.api.common.domain.CommonImageEntity;
import com.tsp.api.common.domain.NewCodeEntity;
import com.tsp.api.common.domain.NewCommonMappedClass;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "tsp_portfolio")
public class FrontPortFolioEntity extends NewCommonMappedClass {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "category_cd")
    @NotNull(message = "포트폴리오 카테고리 선택은 필수입니다.")
    private Integer categoryCd;

    @Column(name = "title")
    @NotEmpty(message = "포트폴리오 제목 입력은 필수입니다.")
    private String title;

    @Column(name = "description")
    @Lob
    @NotEmpty(message = "포트폴리오 상세내용 입력은 필수입니다.")
    private String description;

    @Column(name = "hash_tag")
    @NotEmpty(message = "포트폴리오 해시태그 입력은 필수입니다.")
    private String hashTag;

    @Column(name = "video_url")
    @NotEmpty(message = "포트폴리오 비디오 URL 입력은 필수입니다.")
    private String videoUrl;

    @Column(name = "view_count")
    private int viewCount;

    @Column(name = "visible")
    @NotEmpty(message = "포트폴리오 노출 여부 선택은 필수입니다.")
    private String visible;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_cd", insertable = false, updatable = false)
    private NewCodeEntity newPortFolioJpaDTO;

    @BatchSize(size = 5)
    @Where(clause = "type_name = 'portfolio'")
    @OneToMany(mappedBy = "frontPortFolioEntity", fetch = LAZY)
    private List<CommonImageEntity> commonImageEntityList = new ArrayList<>();

    public void updateViewCount() {
        this.viewCount++;
    }

    public static FrontPortFolioDTO toDto(FrontPortFolioEntity entity) {
        if (entity == null) return null;
        return FrontPortFolioDTO.builder()
                .idx(entity.getIdx())
                .categoryCd(entity.getCategoryCd())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .hashTag(entity.getHashTag())
                .videoUrl(entity.getVideoUrl())
                .viewCount(entity.getViewCount())
                .visible(entity.getVisible())
                .portfolioImage(CommonImageEntity.toDtoList(entity.getCommonImageEntityList()))
                .build();
    }

    public static List<FrontPortFolioDTO> toDtoList(List<FrontPortFolioEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(FrontPortFolioEntity::toDto)
                .collect(Collectors.toList());
    }
}

package com.tsp.new_tsp_front.api.faq.domain;

import com.tsp.new_tsp_front.api.common.domain.NewCommonMappedClass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "tsp_faq")
public class FrontFaqEntity extends NewCommonMappedClass {
    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "title")
    @NotEmpty(message = "제목 입력은 필수입니다.")
    private String title;

    @Column(name = "description")
    @Lob
    @NotEmpty(message = "FAQ 내용 입력은 필수입니다.")
    private String description;

    @Column(name = "view_count")
    private int viewCount;

    @Column(name = "visible")
    @NotEmpty(message = "FAQ 노출 여부 선택은 필수입니다.")
    private String visible;

    public static FrontFaqDTO toDto(FrontFaqEntity entity) {
        if (entity == null) return null;
        return FrontFaqDTO.builder().idx(entity.getIdx())
                .rowNum(entity.getRowNum())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    public static List<FrontFaqDTO> toDtoList(List<FrontFaqEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(FrontFaqEntity::toDto)
                .collect(Collectors.toList());
    }
}

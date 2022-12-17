package com.tsp.new_tsp_front.api.notice.domain;

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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tsp_notice")
public class FrontNoticeEntity extends NewCommonMappedClass {
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
    @NotEmpty(message = "공지사항 내용 입력은 필수입니다.")
    private String description;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "visible")
    @NotEmpty(message = "공지사항 노출 여부 선택은 필수입니다.")
    private String visible;

    @Column(name = "top_fixed")
    private String topFixed;

    public static FrontNoticeDTO toDto(FrontNoticeEntity entity) {
        if (entity == null) return null;
        return FrontNoticeDTO.builder().idx(entity.getIdx())
                .rowNum(entity.getRowNum())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .visible(entity.getVisible())
                .topFixed(entity.getTopFixed())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    public static List<FrontNoticeDTO> toDtoList(List<FrontNoticeEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(FrontNoticeEntity::toDto)
                .collect(Collectors.toList());
    }
}

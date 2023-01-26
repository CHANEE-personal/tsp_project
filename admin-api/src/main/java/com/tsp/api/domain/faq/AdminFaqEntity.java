package com.tsp.api.domain.faq;

import com.tsp.api.domain.common.NewCommonMappedClass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

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
@DynamicUpdate
@Table(name = "tsp_notice")
public class AdminFaqEntity extends NewCommonMappedClass {

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
    private Integer viewCount;

    @Column(name = "visible")
    @NotEmpty(message = "FAQ 노출 여부 선택은 필수입니다.")
    private String visible;

    public void update(AdminFaqEntity adminFaqEntity) {
        this.title = adminFaqEntity.title;
        this.description = adminFaqEntity.description;
        this.visible = adminFaqEntity.visible;
    }

    public static AdminFaqDTO toDto(AdminFaqEntity entity) {
        if (entity == null) return null;
        return AdminFaqDTO.builder().idx(entity.getIdx())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .viewCount(entity.getViewCount())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    public static List<AdminFaqDTO> toDtoList(List<AdminFaqEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(AdminFaqEntity::toDto)
                .collect(Collectors.toList());
    }
}

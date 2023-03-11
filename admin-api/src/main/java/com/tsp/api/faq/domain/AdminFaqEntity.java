package com.tsp.api.faq.domain;

import com.tsp.api.common.domain.NewCommonMappedClass;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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
    private int viewCount;

    @Column(name = "visible")
    @NotEmpty(message = "FAQ 노출 여부 선택은 필수입니다.")
    private String visible;

    public void update(AdminFaqEntity adminFaqEntity) {
        this.title = adminFaqEntity.title;
        this.description = adminFaqEntity.description;
        this.visible = adminFaqEntity.visible;
    }

    public static AdminFaqDto toDto(AdminFaqEntity entity) {
        if (entity == null) return null;
        return AdminFaqDto.builder()
                .idx(entity.idx)
                .title(entity.title)
                .description(entity.description)
                .viewCount(entity.viewCount)
                .visible(entity.visible)
                .build();
    }

    public static List<AdminFaqDto> toDtoList(List<AdminFaqEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(AdminFaqEntity::toDto)
                .collect(Collectors.toList());
    }
}

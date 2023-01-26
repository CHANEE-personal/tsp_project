package com.tsp.api.notice.domain;

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
public class AdminNoticeEntity extends NewCommonMappedClass {

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
    private int viewCount;

    @Column(name = "visible")
    @NotEmpty(message = "공지사항 노출 여부 선택은 필수입니다.")
    private String visible;

    @Column(name = "top_fixed")
    private Boolean topFixed;

    public void update(AdminNoticeEntity adminNoticeEntity) {
        this.title = adminNoticeEntity.title;
        this.description = adminNoticeEntity.description;
        this.visible = adminNoticeEntity.visible;
        this.topFixed = adminNoticeEntity.topFixed;
    }

    public void toggleTopFixed(Boolean topFixed) {
        this.topFixed = !topFixed;
    }

    public static AdminNoticeDTO toDto(AdminNoticeEntity entity) {
        if (entity == null) return null;
        return AdminNoticeDTO.builder()
                .idx(entity.getIdx())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .viewCount(entity.getViewCount())
                .topFixed(entity.getTopFixed())
                .visible(entity.getVisible())
                .build();
    }

    public static List<AdminNoticeDTO> toDtoList(List<AdminNoticeEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(AdminNoticeEntity::toDto)
                .collect(Collectors.toList());
    }
}

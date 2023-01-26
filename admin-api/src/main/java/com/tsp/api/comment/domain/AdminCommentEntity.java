package com.tsp.api.comment.domain;

import com.tsp.api.common.domain.NewCommonMappedClass;
import com.tsp.api.model.domain.AdminModelEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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
@Table(name = "tsp_admin_comment")
public class AdminCommentEntity extends NewCommonMappedClass {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "comment")
    @Lob
    @NotEmpty(message = "코멘트 입력은 필수입니다.")
    private String comment;

    @Column(name = "comment_type")
    private String commentType;

    @Column(name = "visible")
    @NotEmpty(message = "FAQ 노출 여부 선택은 필수입니다.")
    private String visible;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_type_idx", nullable = false)
    private AdminModelEntity adminModelEntity;

    public void update(AdminCommentEntity adminCommentEntity) {
        this.comment = adminCommentEntity.comment;
        this.visible = adminCommentEntity.visible;
    }

    public static AdminCommentDTO toDto(AdminCommentEntity entity) {
        if (entity == null) return null;
        return AdminCommentDTO.builder()
                .idx(entity.getIdx())
                .comment(entity.getComment())
                .commentType(entity.getCommentType())
                .adminModelDTO(AdminModelEntity.toDto(entity.getAdminModelEntity()))
                .visible(entity.getVisible())
                .build();
    }

    public static List<AdminCommentDTO> toDtoList(List<AdminCommentEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(AdminCommentEntity::toDto)
                .collect(Collectors.toList());
    }
}

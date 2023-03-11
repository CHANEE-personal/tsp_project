package com.tsp.api.support.domain.evaluation;

import com.tsp.api.common.domain.NewCommonMappedClass;
import com.tsp.api.support.domain.AdminSupportEntity;
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
@Table(name = "tsp_evaluate")
public class EvaluationEntity extends NewCommonMappedClass {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "visible")
    @NotEmpty(message = "지원모델 노출 여부 선택은 필수입니다.")
    private String visible;

    @Column(name = "evaluate_comment")
    @Lob
    @NotEmpty(message = "지원모델 평가 내용 입력은 필수입니다.")
    private String evaluateComment;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "support_idx", referencedColumnName = "idx")
    private AdminSupportEntity adminSupportEntity;

    public void update(EvaluationEntity evaluationEntity) {
        this.visible = evaluationEntity.visible;
        this.evaluateComment = evaluationEntity.evaluateComment;
    }

    public static EvaluationDto toDto(EvaluationEntity entity) {
        if (entity == null) return null;
        return EvaluationDto.builder()
                .idx(entity.getIdx())
                .adminSupportDTO(AdminSupportEntity.toDto(entity.getAdminSupportEntity()))
                .evaluateComment(entity.getEvaluateComment())
                .visible(entity.getVisible())
                .build();
    }

    public static List<EvaluationDto> toDtoList(List<EvaluationEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(EvaluationEntity::toDto)
                .collect(Collectors.toList());
    }
}

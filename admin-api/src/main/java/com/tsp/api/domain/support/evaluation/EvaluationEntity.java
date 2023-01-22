package com.tsp.api.domain.support.evaluation;

import com.tsp.api.domain.common.NewCommonMappedClass;
import com.tsp.api.domain.support.AdminSupportEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
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
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @JoinColumn(name = "support_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private AdminSupportEntity adminSupportEntity;

    public void update(EvaluationEntity evaluationEntity) {
        this.visible = evaluationEntity.visible;
        this.evaluateComment = evaluationEntity.evaluateComment;
    }

    public static EvaluationDTO toDto(EvaluationEntity entity) {
        if (entity == null) return null;
        return EvaluationDTO.builder()
                .idx(entity.getIdx())
                .supportIdx(entity.getAdminSupportEntity().getIdx())
                .evaluateComment(entity.getEvaluateComment())
                .visible(entity.getVisible())
                .build();
    }

    public static List<EvaluationDTO> toDtoList(List<EvaluationEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(EvaluationEntity::toDto)
                .collect(Collectors.toList());
    }
}

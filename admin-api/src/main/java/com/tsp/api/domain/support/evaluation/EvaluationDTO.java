package com.tsp.api.domain.support.evaluation;

import com.tsp.api.domain.common.NewCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor
@ApiModel(value = "지원모델 평가 관련 변수")
public class EvaluationDTO extends NewCommonDTO {

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @ApiModelProperty(required = true, value = "support_idx", example = "1")
    @NotNull(message = "지원모델 idx 입력은 필수입니다.")
    private Long supportIdx;

    @NotEmpty(message = "지원모델 평가 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "지원모델 평가내용")
    private String evaluateComment;

    @ApiModelProperty(required = true, value = "삭제 여부((ex)Y,N")
    @NotEmpty(message = "지원모델 평가 삭제 선택은 필수입니다.")
    private String visible;
}

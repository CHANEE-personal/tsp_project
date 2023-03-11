package com.tsp.api.support.domain.evaluation;

import com.tsp.api.common.domain.NewCommonDTO;
import com.tsp.api.support.domain.AdminSupportDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel(value = "지원모델 평가 관련 변수")
public class EvaluationDto extends NewCommonDTO {

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @ApiModelProperty(value = "adminSupportDTO", hidden = true)
    private AdminSupportDto adminSupportDTO;

    @NotEmpty(message = "지원모델 평가 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "지원모델 평가내용")
    private String evaluateComment;

    @ApiModelProperty(required = true, value = "삭제 여부((ex)Y,N")
    @NotEmpty(message = "지원모델 평가 삭제 선택은 필수입니다.")
    private String visible;
}

package com.tsp.api.support.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tsp.api.common.domain.NewCommonDTO;
import com.tsp.api.support.domain.evaluation.EvaluationDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel
public class AdminSupportDto extends NewCommonDTO {

    @ApiModelProperty(value = "지원모델 IDX", required = true, hidden = true, example = "1")
    private Long idx;

    @NotEmpty(message = "지원모델 이름 입력은 필수입니다.")
    @ApiModelProperty(value = "지원모델 이름", required = true)
    private String supportName;

    @NotEmpty(message = "지원모델 키 입력은 필수입니다.")
    @ApiModelProperty(value = "지원모델 Height", required = true, example = "180")
    private Integer supportHeight;

    @NotEmpty(message = "지원모델 사이즈 입력은 필수입니다.")
    @ApiModelProperty(value = "지원모델 3Size", required = true)
    private String supportSize3;

    @ApiModelProperty(value = "지원모델 instagram")
    private String supportInstagram;

    @NotEmpty(message = "지원모델 휴대폰번호 입력은 필수입니다.")
    @ApiModelProperty(value = "지원모델 휴대폰번호", required = true)
    private String supportPhone;

    @NotEmpty(message = "지원모델 상세 내용 입력은 필수입니다.")
    @ApiModelProperty(value = "지원모델 내용", required = true)
    private String supportMessage;

    @NotEmpty(message = "지원모델 노출 여부 선택은 필수입니다.")
    @ApiModelProperty(value = "노출여부", required = true)
    private String visible;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @ApiModelProperty(value = "지원 시각", required = true)
    private LocalDateTime supportTime;

    @ApiModelProperty(value = "합격여부", required = true)
    private String passYn;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @ApiModelProperty(value = "합격 시각", required = true)
    private LocalDateTime passTime;

    @ApiModelProperty(required = true, value = "evaluationList", hidden = true)
    private List<EvaluationDto> evaluationList = new ArrayList<>();
}

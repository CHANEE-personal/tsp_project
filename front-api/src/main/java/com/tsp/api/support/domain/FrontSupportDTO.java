package com.tsp.api.support.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tsp.api.common.domain.NewCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel
public class FrontSupportDTO extends NewCommonDTO {

    @ApiModelProperty(value = "지원모델 IDX", required = true, hidden = true)
    private Long idx;

    @ApiModelProperty(value = "지원모델 이름", required = true)
    private String supportName;

    @ApiModelProperty(value = "지원모델 Height", required = true)
    private Integer supportHeight;

    @ApiModelProperty(value = "지원모델 3Size", required = true)
    private String supportSize3;

    @ApiModelProperty(value = "지원모델 instagram")
    private String supportInstagram;

    @ApiModelProperty(value = "지원모델 휴대폰번호", required = true)
    private String supportPhone;

    @ApiModelProperty(value = "지원모델 내용", required = true)
    private String supportMessage;

    @ApiModelProperty(value = "노출여부", required = true)
    private String visible;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @ApiModelProperty(value = "지원 시각", required = true)
    private LocalDateTime supportTime;

}

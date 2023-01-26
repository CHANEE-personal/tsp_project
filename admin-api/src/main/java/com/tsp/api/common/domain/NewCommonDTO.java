package com.tsp.api.common.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel
public class NewCommonDTO {

    @ApiModelProperty(required = true, value = "등록자", hidden = true)
    private String createdBy;

    @ApiModelProperty(required = true, value = "수정자", hidden = true)
    private String modifiedBy;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @ApiModelProperty(required = true, value = "등록 일자", hidden = true)
    private LocalDateTime createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @ApiModelProperty(required = true, value = "수정 일자", hidden = true)
    private LocalDateTime modifiedAt;
}

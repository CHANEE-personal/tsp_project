package com.tsp.api.domain.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ApiModel
public class NewCommonDTO {
    @ApiModelProperty(required = true, value = "등록자", hidden = true)
    private String creator;

    @ApiModelProperty(required = true, value = "수정자", hidden = true)
    private String updater;

    @ApiModelProperty(required = true, value = "등록자 이름", hidden = true)
    private String adminName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @ApiModelProperty(required = true, value = "등록 일자", hidden = true)
    private LocalDateTime createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @ApiModelProperty(required = true, value = "수정 일자", hidden = true)
    private LocalDateTime updateTime;
}

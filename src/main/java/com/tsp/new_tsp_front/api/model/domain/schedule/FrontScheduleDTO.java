package com.tsp.new_tsp_front.api.model.domain.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tsp.new_tsp_front.api.common.domain.NewCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "모델 스케줄 관련 변수")
public class FrontScheduleDTO extends NewCommonDTO {
    @ApiModelProperty(required = true, value = "rnum", hidden = true, example = "1")
    private Integer rnum;

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Integer idx;

    @NotNull(message = "모델 IDX 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "modelIdx", example = "1")
    private Integer modelIdx;

    @NotEmpty(message = "모델 스케줄 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "modelSchedule")
    private String modelSchedule;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @ApiModelProperty(required = true, value = "모델 스케줄 일자", hidden = true)
    private Date modelScheduleTime;

    @NotEmpty(message = "모델 스케줄 노출 여부 선택은 필수입니다.")
    @ApiModelProperty(required = true, value = "visible")
    private String visible;
}

package com.tsp.api.domain.model.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tsp.api.domain.common.NewCommonDTO;
import com.tsp.api.domain.model.AdminModelDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ApiModel(value = "모델 스케줄 관련 변수")
public class AdminScheduleDTO extends NewCommonDTO {

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotNull(message = "모델 IDX 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "modelIdx", example = "1")
    private Long modelIdx;

    @NotEmpty(message = "모델 스케줄 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "modelSchedule")
    private String modelSchedule;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @ApiModelProperty(required = true, value = "모델 스케줄 일자", hidden = true)
    private LocalDateTime modelScheduleTime;

    @NotEmpty(message = "모델 스케줄 노출 여부 선택은 필수입니다.")
    @ApiModelProperty(required = true, value = "visible")
    private String visible;

    @ApiModelProperty(value = "adminModelDTO", hidden = true)
    private AdminModelDTO adminModelDTO;
}

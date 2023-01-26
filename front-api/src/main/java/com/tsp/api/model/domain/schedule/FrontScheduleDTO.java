package com.tsp.api.model.domain.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tsp.api.common.domain.NewCommonDTO;
import com.tsp.api.model.domain.FrontModelDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel(value = "모델 스케줄 관련 변수")
public class FrontScheduleDTO extends NewCommonDTO {

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotEmpty(message = "모델 스케줄 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "modelSchedule")
    private String modelSchedule;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @ApiModelProperty(required = true, value = "모델 스케줄 일자", hidden = true)
    private LocalDateTime modelScheduleTime;

    @NotEmpty(message = "모델 스케줄 노출 여부 선택은 필수입니다.")
    @ApiModelProperty(required = true, value = "visible")
    private String visible;

    @ApiModelProperty(value = "frontModelDTO", hidden = true)
    private FrontModelDTO frontModelDTO;
}

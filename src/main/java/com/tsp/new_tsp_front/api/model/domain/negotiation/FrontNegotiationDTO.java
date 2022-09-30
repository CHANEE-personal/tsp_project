package com.tsp.new_tsp_front.api.model.domain.negotiation;

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
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "모델 섭외 관련 변수")
public class FrontNegotiationDTO extends NewCommonDTO {
    @ApiModelProperty(required = true, value = "rnum", hidden = true, example = "1")
    private Integer rnum;

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotNull(message = "모델 IDX 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "modelIdx", example = "1")
    private Long modelIdx;

    @NotEmpty(message = "모델 국문 이름 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "model Kor Name", hidden = true)
    private String modelKorName;

    @NotEmpty(message = "모델 섭외 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "modelNegotiationDesc")
    private String modelNegotiationDesc;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @ApiModelProperty(required = true, value = "모델 섭외 일정", hidden = true)
    private LocalDateTime modelNegotiationDate;

    @NotEmpty(message = "모델 섭외자명 입력은 필수입니다.")
    @ApiModelProperty(value = "모델 섭외자명", required = true)
    private String name;

    @NotEmpty(message = "모델 섭외자 이메일 입력은 필수입니다.")
    @ApiModelProperty(value = "모델 섭외자 이메일", required = true)
    private String email;

    @NotEmpty(message = "모델 섭외자 휴대폰번호 입력은 필수입니다.")
    @ApiModelProperty(value = "모델 섭외자 휴대폰 번호", required = true)
    private String phone;

    @NotEmpty(message = "모델 섭외 노출 여부 선택은 필수입니다.")
    @ApiModelProperty(required = true, value = "visible")
    private String visible;
}

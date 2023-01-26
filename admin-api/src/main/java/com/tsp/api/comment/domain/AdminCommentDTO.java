package com.tsp.api.comment.domain;

import com.tsp.api.common.domain.NewCommonDTO;
import com.tsp.api.model.domain.AdminModelDTO;
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
@ApiModel(value = "코멘트 관련 변수")
public class AdminCommentDTO extends NewCommonDTO {

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotEmpty(message = "코멘트 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "comment")
    private String comment;

    @ApiModelProperty(required = true, value = "commentTypeIdx", example = "1")
    private Long commentTypeIdx;

    @ApiModelProperty(required = true, value = "commentType")
    private String commentType;

    @NotEmpty(message = "코멘트 노출 여부 선택은 필수입니다.")
    @ApiModelProperty(required = true, value = "visible")
    private String visible;

    @ApiModelProperty(value = "adminModelDTO", hidden = true)
    private AdminModelDTO adminModelDTO;
}

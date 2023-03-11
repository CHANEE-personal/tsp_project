package com.tsp.api.comment.domain;

import com.tsp.api.common.domain.NewCommonDTO;
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
public class AdminCommentDto extends NewCommonDTO {

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

    @ApiModelProperty(value = "modelIdx", hidden = true)
    private Long modelIdx;

    @ApiModelProperty(value = "categoryCd", hidden = true)
    private Integer categoryCd;

    @ApiModelProperty(value = "categoryAge", hidden = true)
    private Integer categoryAge;

    @ApiModelProperty(value = "modelKorName", hidden = true)
    private String modelKorName;

    @ApiModelProperty(value = "modelEngName", hidden = true)
    private String modelEngName;

    @ApiModelProperty(value = "modelDescription", hidden = true)
    private String modelDescription;

    @ApiModelProperty(value = "height", hidden = true)
    private Integer height;

    @ApiModelProperty(value = "size3", hidden = true)
    private String size3;

    @ApiModelProperty(value = "shoes", hidden = true)
    private Integer shoes;

    @ApiModelProperty(value = "modelMainYn", hidden = true)
    private String modelMainYn;

    @ApiModelProperty(value = "modelFirstName", hidden = true)
    private String modelFirstName;

    @ApiModelProperty(value = "modelSecondName", hidden = true)
    private String modelSecondName;

    @ApiModelProperty(value = "modelKorFirstName", hidden = true)
    private String modelKorFirstName;

    @ApiModelProperty(value = "modelKorSecondName", hidden = true)
    private String modelKorSecondName;
}

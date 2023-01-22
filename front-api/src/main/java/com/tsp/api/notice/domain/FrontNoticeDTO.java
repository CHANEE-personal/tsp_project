package com.tsp.api.notice.domain;

import com.tsp.api.common.domain.NewCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "공지사항 관련 변수")
public class FrontNoticeDTO extends NewCommonDTO {

    @ApiModelProperty(required = true, value = "idx", hidden = true)
    private Long idx;

    @NotEmpty(message = "제목 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "title")
    private String title;

    @NotEmpty(message = "공지사항 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "description")
    private String description;

    @ApiModelProperty(required = true, value = "viewCount")
    private int viewCount;

    @NotEmpty(message = "공지사항 노출 여부 선택은 필수입니다.")
    @ApiModelProperty(required = true, value = "visible")
    private String visible;

    @ApiModelProperty(value = "topFixed")
    private String topFixed;
}

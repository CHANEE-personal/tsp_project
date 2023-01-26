package com.tsp.api.portfolio.domain;

import com.tsp.api.common.domain.CommonImageDTO;
import com.tsp.api.common.domain.NewCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel
public class FrontPortFolioDTO extends NewCommonDTO {

    @ApiModelProperty(required = true, value = "idx", hidden = true)
    private Long idx;

    @NotEmpty(message = "제목은 필수입니다.")
    @ApiModelProperty(required = true, value = "title")
    private String title;

    @ApiModelProperty(required = true, value = "hashTag")
    private String hashTag;

    @NotEmpty(message = "카테고리 선택은 필수입니다.")
    @ApiModelProperty(required = true, value = "categoryCd")
    private Integer categoryCd;

    @ApiModelProperty(required = true, value = "videoUrl")
    private String videoUrl;

    @NotEmpty(message = "상세 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "description")
    private String description;

    @ApiModelProperty(value = "viewCount")
    private int viewCount;

    @ApiModelProperty(required = true, value = "visible")
    private String visible;

    @ApiModelProperty(required = true, value = "portfolioImageList", hidden = true)
    private List<CommonImageDTO> portfolioImage = new ArrayList<>();
}

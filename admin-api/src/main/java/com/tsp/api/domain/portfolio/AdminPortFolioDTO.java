package com.tsp.api.domain.portfolio;

import com.tsp.api.domain.common.CommonImageDTO;
import com.tsp.api.domain.common.NewCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor
@ApiModel
public class AdminPortFolioDTO extends NewCommonDTO {

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotEmpty(message = "제목은 필수입니다.")
    @ApiModelProperty(required = true, value = "title")
    private String title;

    @ApiModelProperty(required = true, value = "hashTag")
    private String hashTag;

    @NotEmpty(message = "카테고리 선택은 필수입니다.")
    @ApiModelProperty(required = true, value = "categoryCd", example = "1")
    private Integer categoryCd;

    @ApiModelProperty(required = true, value = "videoUrl")
    private String videoUrl;

    @NotEmpty(message = "상세 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "description")
    private String description;

    @ApiModelProperty(value = "viewCount", example = "1")
    private Integer viewCount;

    @NotEmpty(message = "포트폴리오 노출 여부 선택은 필수입니다.")
    @ApiModelProperty(required = true, value = "visible")
    private String visible;

    @ApiModelProperty(required = true, value = "portfolioImageList", hidden = true)
    private List<CommonImageDTO> portfolioImage = new ArrayList<>();
}

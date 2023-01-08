package com.tsp.new_tsp_front.api.production.domain;

import com.tsp.new_tsp_front.api.common.domain.CommonImageDTO;
import com.tsp.new_tsp_front.api.common.domain.NewCommonDTO;
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
@AllArgsConstructor
@ApiModel
public class FrontProductionDTO extends NewCommonDTO {
    @ApiModelProperty(required = true, value = "rowNum", hidden = true)
    private Integer rowNum;

    @ApiModelProperty(required = true, value = "idx", hidden = true)
    private Long idx;

    @NotEmpty(message = "제목 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "title")
    private String title;

    @NotEmpty(message = "상세 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "description")
    private String description;

    @ApiModelProperty(value = "viewCount")
    private int viewCount;

    @ApiModelProperty(required = true, value = "visible")
    private String visible;

    @ApiModelProperty(required = true, value = "productionImageList", hidden = true)
    private List<CommonImageDTO> productionImage = new ArrayList<>();
}

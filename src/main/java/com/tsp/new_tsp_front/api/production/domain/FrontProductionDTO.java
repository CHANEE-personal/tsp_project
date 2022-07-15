package com.tsp.new_tsp_front.api.production.domain;

import com.tsp.new_tsp_front.api.common.domain.CommonImageDTO;
import com.tsp.new_tsp_front.api.common.domain.NewCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class FrontProductionDTO extends NewCommonDTO {
    @ApiModelProperty(required = true, value = "rnum", hidden = true)
    Integer rnum;

    @ApiModelProperty(required = true, value = "idx", hidden = true)
    Integer idx;

    @NotNull(message = "제목 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "title")
    String title;

    @NotNull(message = "상세 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "description")
    String description;

    @ApiModelProperty(required = true, value = "visible")
    String visible;

    @ApiModelProperty(required = true, value = "productionImageList", hidden = true)
    private List<CommonImageDTO> productionImage = new ArrayList<>();
}

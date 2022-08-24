package com.tsp.new_tsp_front.api.agency.domain;

import com.tsp.new_tsp_front.api.common.domain.CommonImageDTO;
import com.tsp.new_tsp_front.api.common.domain.NewCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "소속사 관련 변수")
public class FrontAgencyDTO extends NewCommonDTO {
    @ApiModelProperty(required = true, value = "rnum", hidden = true)
    Integer rnum;

    @ApiModelProperty(required = true, value = "idx", hidden = true)
    Integer idx;

    @NotEmpty(message = "소속사명 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "agencyName")
    String agencyName;

    @NotEmpty(message = "소속사 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "agencyDescription")
    String agencyDescription;

    @ApiModelProperty(value = "favoriteCount")
    Integer favoriteCount;

    @NotEmpty(message = "소속사 노출 여부 선택은 필수입니다.")
    @ApiModelProperty(required = true, value = "visible")
    String visible;

    @ApiModelProperty(required = true, value = "agencyImageList", hidden = true)
    private List<CommonImageDTO> agencyImage = new ArrayList<>();
}

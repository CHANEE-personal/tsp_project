package com.tsp.new_tsp_front.api.agency.domain;

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
@ApiModel(value = "소속사 관련 변수")
public class FrontAgencyDTO extends NewCommonDTO {
    @ApiModelProperty(required = true, value = "rnum", hidden = true)
    private Integer rnum;

    @ApiModelProperty(required = true, value = "idx", hidden = true)
    private Long idx;

    @NotEmpty(message = "소속사명 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "agencyName")
    private String agencyName;

    @NotEmpty(message = "소속사 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "agencyDescription")
    private String agencyDescription;

    @ApiModelProperty(value = "favoriteCount")
    private Integer favoriteCount;

    @NotEmpty(message = "소속사 노출 여부 선택은 필수입니다.")
    @ApiModelProperty(required = true, value = "visible")
    private String visible;

    @ApiModelProperty(required = true, value = "agencyImageList", hidden = true)
    private List<CommonImageDTO> agencyImage = new ArrayList<>();
}

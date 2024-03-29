package com.tsp.api.model.domain.agency;

import com.tsp.api.common.domain.CommonImageDto;
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
@ApiModel(value = "소속사 관련 변수")
public class AdminAgencyDto extends NewCommonDTO {

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotEmpty(message = "소속사명 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "agencyName")
    private String agencyName;

    @NotEmpty(message = "소속사 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "agencyDescription")
    private String agencyDescription;

    @ApiModelProperty(value = "소속사 좋아요 수((ex)0)", example = "1")
    private int favoriteCount;

    @NotEmpty(message = "소속사 노출 여부 선택은 필수입니다.")
    @ApiModelProperty(required = true, value = "visible")
    private String visible;

    @ApiModelProperty(required = true, value = "agencyImageList", hidden = true)
    private List<CommonImageDto> agencyImage = new ArrayList<>();
}

package com.tsp.api.model.domain.recommend;

import com.tsp.api.common.domain.NewCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "추천 검색어 관련 변수")
public class AdminRecommendDTO extends NewCommonDTO {

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @Type(type = "json")
    @Nullable
    @ApiModelProperty(value = "recommend keyword")
    private List<String> recommendKeyword = new ArrayList<>();
}

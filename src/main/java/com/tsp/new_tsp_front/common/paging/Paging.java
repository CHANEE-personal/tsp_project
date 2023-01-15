package com.tsp.new_tsp_front.common.paging;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel
public class Paging {
    @ApiModelProperty(value = "페이지 번호(0..N)")
    private Integer pageNum;

    @ApiModelProperty(value = "페이지 크기", allowableValues = "range[0,100]")
    private Integer size;

    // 시작페이지
    @ApiModelProperty(value = "시작페이지", hidden = true)
    public Integer getStartPage() {
        return (pageNum - 1) * size;
    }
}

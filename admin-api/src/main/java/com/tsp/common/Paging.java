package com.tsp.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel
public class Paging {
    @ApiModelProperty(value = "페이지 번호(0..N)", example = "1")
    private Integer pageNum;

    @ApiModelProperty(value = "페이지 크기", allowableValues = "range[0,100]", example = "1")
    private Integer size;
}

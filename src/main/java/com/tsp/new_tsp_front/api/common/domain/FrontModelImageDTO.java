package com.tsp.new_tsp_front.api.common.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class FrontModelImageDTO {
    @ApiModelProperty(value = "파일 IDX", required = true, hidden = true)
    private Long idx;

    @ApiModelProperty(value = "분야 IDX", required = true, hidden = true)
    private Long typeIdx;

    @ApiModelProperty(value = "분야명", required = true, hidden = true)
    private String typeName;

    @ApiModelProperty(value = "파일 Number", required = true, hidden = true)
    private Integer fileNum;

    @ApiModelProperty(required = true, value = "파일명", hidden = true)
    private String fileName;

    @ApiModelProperty(value = "파일SIZE", hidden = true)
    private Long fileSize;

    @ApiModelProperty(value = "파일MASK", hidden = true)
    private String fileMask;

    @ApiModelProperty(value = "파일경로", hidden = true)
    private String filePath;

    @ApiModelProperty(value = "메인 이미지 구분", hidden = true)
    private String imageType;

    @ApiModelProperty(value = "이미지 사용 여부", hidden = true)
    private String visible;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @ApiModelProperty(value = "등록일자", hidden = true)
    private LocalDateTime regDate;
}

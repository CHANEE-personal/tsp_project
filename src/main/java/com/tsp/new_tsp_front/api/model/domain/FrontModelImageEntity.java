package com.tsp.new_tsp_front.api.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsp.new_tsp_front.api.common.domain.CommonImageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tsp_model_image")
public class FrontModelImageEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "type_idx")
    @ApiModelProperty(value = "분야 IDX", required = true, hidden = true)
    private Long typeIdx;

    @Column(name = "type_name")
    @ApiModelProperty(value = "분야명", required = true, hidden = true)
    private String typeName;

    @Column(name = "file_num")
    @ApiModelProperty(value = "파일 Number", required = true, hidden = true)
    private Integer fileNum;

    @Column(name = "file_name")
    @ApiModelProperty(required = true, value = "파일명", hidden = true)
    private String fileName;

    @Column(name = "file_size")
    @ApiModelProperty(value = "파일SIZE", hidden = true)
    private Long fileSize;

    @Column(name = "file_mask")
    @ApiModelProperty(value = "파일MASK", hidden = true)
    private String fileMask;

    @Column(name = "file_path")
    @ApiModelProperty(value = "파일경로", hidden = true)
    private String filePath;

    @Column(name = "image_type")
    @ApiModelProperty(value = "메인 이미지 구분", hidden = true)
    private String imageType;

    @Column(name = "visible")
    @ApiModelProperty(value = "사용 여부", hidden = true)
    private String visible;

    @Column(name = "reg_date", insertable = false, updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @ApiModelProperty(value = "등록일자", hidden = true)
    private LocalDateTime regDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "type_idx", insertable = false, updatable = false)
    private FrontModelEntity modelEntity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "idx", referencedColumnName = "type_idx", insertable = false, updatable = false)
    private CommonImageEntity imageEntity;

}

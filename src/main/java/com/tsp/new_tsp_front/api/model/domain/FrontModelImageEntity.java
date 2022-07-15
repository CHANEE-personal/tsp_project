package com.tsp.new_tsp_front.api.model.domain;

import com.tsp.new_tsp_front.api.common.domain.CommonImageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tsp_model_image")
public class FrontModelImageEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Integer idx;

    @Column(name = "type_idx")
    @ApiModelProperty(value = "분야 IDX", required = true, hidden = true)
    private Integer typeIdx;

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
    @ApiModelProperty(value = "등록일자", hidden = true)
    private String regDate;

    @ManyToOne
    @JoinColumn(name = "type_idx", insertable = false, updatable = false)
    private FrontModelEntity modelEntity;

    @ManyToOne
    @JoinColumn(name = "idx", referencedColumnName = "type_idx", insertable = false, updatable = false)
    private CommonImageEntity imageEntity;

}

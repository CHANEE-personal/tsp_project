package com.tsp.new_tsp_front.api.common.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyEntity;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioEntity;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "tsp_image")
public class CommonImageEntity implements Serializable {
    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue
    @Column(name = "idx")
    @ApiModelProperty(value = "파일 IDX", required = true, hidden = true)
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "type_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private FrontModelEntity frontModelEntity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "type_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private FrontProductionEntity frontProductionEntity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "type_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private FrontPortFolioEntity frontPortFolioEntity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "type_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private FrontAgencyEntity frontAgencyEntity;

    public static CommonImageDTO toDto(CommonImageEntity entity) {
        if (entity == null) return null;
        return CommonImageDTO.builder()
                .idx(entity.getIdx())
                .typeIdx(entity.getTypeIdx())
                .typeName(entity.getTypeName())
                .fileMask(entity.getFileMask())
                .fileSize(entity.getFileSize())
                .fileName(entity.getFileName())
                .fileNum(entity.getFileNum())
                .filePath(entity.getFilePath())
                .imageType(entity.getImageType())
                .visible(entity.getVisible())
                .regDate(entity.getRegDate())
                .build();
    }

    public static List<CommonImageDTO> toDtoList(List<CommonImageEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(CommonImageEntity::toDto)
                .collect(Collectors.toList());
    }
}

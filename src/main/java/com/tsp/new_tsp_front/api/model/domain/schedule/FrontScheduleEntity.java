package com.tsp.new_tsp_front.api.model.domain.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsp.new_tsp_front.api.common.domain.NewCommonMappedClass;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tsp_model_schedule")
public class FrontScheduleEntity extends NewCommonMappedClass {
    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "model_idx")
    @ApiModelProperty(value = "모델 idx", required = true)
    private Long modelIdx;

    @Column(name = "model_schedule")
    @Lob
    @NotEmpty(message = "모델 스케줄 입력은 필수입니다.")
    private String modelSchedule;

    @Column(name = "model_schedule_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "모델 스케줄 일정 입력은 필수입니다.")
    private LocalDateTime modelScheduleTime;

    @Column(name = "visible")
    @NotEmpty(message = "모델 스케줄 노출 여부 선택은 필수입니다.")
    private String visible;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "model_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private FrontModelEntity frontModelEntity;

    public static FrontScheduleDTO toDto(FrontScheduleEntity entity) {
        if (entity == null) return null;
        return FrontScheduleDTO.builder().idx(entity.getIdx())
                .rowNum(entity.getRowNum())
                .modelIdx(entity.getModelIdx())
                .modelSchedule(entity.getModelSchedule())
                .modelScheduleTime(entity.getModelScheduleTime())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    public static List<FrontScheduleDTO> toDtoList(List<FrontScheduleEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(FrontScheduleEntity::toDto)
                .collect(Collectors.toList());
    }
}

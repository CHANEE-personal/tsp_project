package com.tsp.new_tsp_front.api.model.domain.schedule;

import com.tsp.new_tsp_front.api.common.domain.NewCommonMappedClass;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tsp_model_schedule")
public class FrontScheduleEntity extends NewCommonMappedClass {
    @Transient
    private Integer rnum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Integer idx;

    @Column(name = "model_idx")
    @ApiModelProperty(value = "모델 idx", required = true)
    private Integer modelIdx;

    @Column(name = "model_schedule")
    @Lob
    @NotEmpty(message = "모델 스케줄 입력은 필수입니다.")
    private String modelSchedule;

    @Column(name = "model_schedule_time")
    @Temporal(value = TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "모델 스케줄 일정 입력은 필수입니다.")
    private Date modelScheduleTime;

    @Column(name = "visible")
    @NotEmpty(message = "모델 스케줄 노출 여부 선택은 필수입니다.")
    private String visible;
}

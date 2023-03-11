package com.tsp.api.model.domain.schedule;

import com.tsp.api.common.domain.NewCommonMappedClass;
import com.tsp.api.model.domain.FrontModelEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "tsp_model_schedule")
public class FrontScheduleEntity extends NewCommonMappedClass {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "model_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private FrontModelEntity frontModelEntity;

    public static FrontScheduleDTO toDto(FrontScheduleEntity entity) {
        if (entity == null) return null;
        return FrontScheduleDTO.builder()
                .idx(entity.idx)
                .categoryCd(entity.frontModelEntity.getCategoryCd())
                .categoryAge(entity.frontModelEntity.getCategoryAge())
                .modelKorName(entity.frontModelEntity.getModelKorName())
                .modelEngName(entity.frontModelEntity.getModelEngName())
                .modelKorFirstName(entity.frontModelEntity.getModelKorFirstName())
                .modelKorSecondName(entity.frontModelEntity.getModelKorSecondName())
                .modelFirstName(entity.frontModelEntity.getModelFirstName())
                .modelSecondName(entity.frontModelEntity.getModelSecondName())
                .height(entity.frontModelEntity.getHeight())
                .shoes(entity.frontModelEntity.getShoes())
                .modelSchedule(entity.modelSchedule)
                .modelScheduleTime(entity.modelScheduleTime)
                .visible(entity.visible)
                .build();
    }

    public static List<FrontScheduleDTO> toDtoList(List<FrontScheduleEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(FrontScheduleEntity::toDto)
                .collect(Collectors.toList());
    }
}

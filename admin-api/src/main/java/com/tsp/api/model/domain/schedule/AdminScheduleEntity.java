package com.tsp.api.model.domain.schedule;

import com.tsp.api.common.domain.NewCommonMappedClass;
import com.tsp.api.model.domain.AdminModelEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@Table(name = "tsp_model_schedule")
public class AdminScheduleEntity extends NewCommonMappedClass {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "model_schedule")
    @Lob
    @NotEmpty(message = "모델 스케줄 입력은 필수입니다.")
    private String modelSchedule;

    @Column(name = "model_schedule_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "모델 스케줄 일정 입력은 필수입니다.")
    private LocalDateTime modelScheduleTime;

    @Column(name = "visible")
    @NotEmpty(message = "모델 스케줄 노출 여부 선택은 필수입니다.")
    private String visible;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "model_idx", referencedColumnName = "idx", nullable = false)
    private AdminModelEntity adminModelEntity;

    public void update(AdminScheduleEntity adminScheduleEntity) {
        this.modelSchedule = adminScheduleEntity.modelSchedule;
        this.modelScheduleTime = adminScheduleEntity.modelScheduleTime;
        this.visible = adminScheduleEntity.visible;
    }

    public static AdminScheduleDto toDto(AdminScheduleEntity entity) {
        if (entity == null) return null;
        return AdminScheduleDto.builder()
                .idx(entity.idx)
                .categoryCd(entity.adminModelEntity.getCategoryCd())
                .categoryAge(entity.adminModelEntity.getCategoryAge())
                .modelKorName(entity.adminModelEntity.getModelKorName())
                .modelEngName(entity.adminModelEntity.getModelEngName())
                .modelKorFirstName(entity.adminModelEntity.getModelKorFirstName())
                .modelKorSecondName(entity.adminModelEntity.getModelKorSecondName())
                .modelFirstName(entity.adminModelEntity.getModelFirstName())
                .modelSecondName(entity.adminModelEntity.getModelSecondName())
                .height(entity.adminModelEntity.getHeight())
                .shoes(entity.adminModelEntity.getShoes())
                .modelSchedule(entity.modelSchedule)
                .modelScheduleTime(entity.modelScheduleTime)
                .visible(entity.visible)
                .build();
    }

    public static List<AdminScheduleDto> toDtoList(List<AdminScheduleEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(AdminScheduleEntity::toDto)
                .collect(Collectors.toList());
    }
}

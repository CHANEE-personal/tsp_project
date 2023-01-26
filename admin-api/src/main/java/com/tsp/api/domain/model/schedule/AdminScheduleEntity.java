package com.tsp.api.domain.model.schedule;

import com.tsp.api.domain.common.NewCommonMappedClass;
import com.tsp.api.domain.model.AdminModelEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
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

@SuperBuilder
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public static AdminScheduleDTO toDto(AdminScheduleEntity entity) {
        if (entity == null) return null;
        return AdminScheduleDTO.builder()
                .idx(entity.getIdx())
                .modelIdx(entity.getAdminModelEntity().getIdx())
                .adminModelDTO(AdminModelEntity.toDto(entity.getAdminModelEntity()))
                .modelSchedule(entity.getModelSchedule())
                .modelScheduleTime(entity.getModelScheduleTime())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    public static List<AdminScheduleDTO> toDtoList(List<AdminScheduleEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(AdminScheduleEntity::toDto)
                .collect(Collectors.toList());
    }
}

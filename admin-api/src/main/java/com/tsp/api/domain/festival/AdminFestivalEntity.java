package com.tsp.api.domain.festival;

import com.tsp.api.domain.common.NewCommonMappedClass;
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

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "tsp_festival")
public class AdminFestivalEntity extends NewCommonMappedClass {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "festival_title")
    @Lob
    @NotEmpty
    private String festivalTitle;

    @Column(name = "festival_description")
    @Lob
    @NotEmpty
    private String festivalDescription;

    @Column(name = "festival_month")
    @NotNull(message = "축제가 열리는 월 입력은 필수입니다.")
    private Integer festivalMonth;

    @Column(name = "festival_day")
    @NotNull(message = "축제가 열리는 일 입력은 필수입니다.")
    private Integer festivalDay;

    @Column(name = "festival_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "축제 일정 입력은 필수입니다.")
    private LocalDateTime festivalTime;

    public void update(AdminFestivalEntity adminFestival) {
        this.festivalTitle = adminFestival.festivalTitle;
        this.festivalDescription = adminFestival.festivalDescription;
        this.festivalTime = adminFestival.festivalTime;
        this.festivalMonth = adminFestival.festivalMonth;;
        this.festivalDay = adminFestival.festivalDay;
    }

    public static AdminFestivalDTO toDto(AdminFestivalEntity entity) {
        if (entity == null) return null;
        return AdminFestivalDTO.builder()
                .idx(entity.getIdx())
                .festivalTitle(entity.getFestivalTitle())
                .festivalDescription(entity.getFestivalDescription())
                .festivalMonth(entity.getFestivalMonth())
                .festivalDay(entity.getFestivalDay())
                .festivalTime(entity.getFestivalTime())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    public static List<AdminFestivalDTO> toDtoList(List<AdminFestivalEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(AdminFestivalEntity::toDto)
                .collect(Collectors.toList());
    }

}

package com.tsp.api.festival.domain;

import com.tsp.api.common.domain.NewCommonMappedClass;
import lombok.*;
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
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tsp_festival")
public class FrontFestivalEntity extends NewCommonMappedClass {

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

    public static FrontFestivalDTO toDto(FrontFestivalEntity entity) {
        if (entity == null) return null;
        return FrontFestivalDTO.builder()
                .idx(entity.getIdx())
                .festivalTitle(entity.getFestivalTitle())
                .festivalDescription(entity.getFestivalDescription())
                .festivalMonth(entity.getFestivalMonth())
                .festivalDay(entity.getFestivalDay())
                .festivalTime(entity.getFestivalTime())
                .build();
    }

    public static List<FrontFestivalDTO> toDtoList(List<FrontFestivalEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(FrontFestivalEntity::toDto)
                .collect(Collectors.toList());
    }
}

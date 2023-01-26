package com.tsp.api.support.domain;

import com.tsp.api.common.domain.NewCommonMappedClass;
import lombok.*;

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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "tsp_support")
public class FrontSupportEntity extends NewCommonMappedClass {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "support_name")
    @NotEmpty(message = "지원자 이름 입력은 필수입니다.")
    private String supportName;

    @Column(name = "support_height")
    @NotNull(message = "지원자 키 입력은 필수입니다.")
    private Integer supportHeight;

    @Column(name = "support_size3")
    @NotEmpty(message = "지원자 사이즈 입력은 필수입니다.")
    private String supportSize3;

    @Column(name = "support_instagram")
    private String supportInstagram;

    @Column(name = "support_phone")
    @NotEmpty(message = "지원자 휴대폰 번호 입력은 필수입니다.")
    private String supportPhone;

    @Column(name = "support_message")
    @NotEmpty(message = "지원자 상세 내용 입력은 필수입니다.")
    @Lob
    private String supportMessage;

    @Column(name = "visible")
    @NotEmpty(message = "지원모델 노출 여부 선택은 필수입니다.")
    private String visible;

    @Column(name = "support_time")
    private LocalDateTime supportTime;

    public static FrontSupportDTO toDto(FrontSupportEntity entity) {
        if (entity == null) return null;
        return FrontSupportDTO.builder()
                .idx(entity.getIdx())
                .supportName(entity.getSupportName())
                .supportHeight(entity.getSupportHeight())
                .supportSize3(entity.getSupportSize3())
                .supportInstagram(entity.getSupportInstagram())
                .supportPhone(entity.getSupportPhone())
                .supportMessage(entity.getSupportMessage())
                .visible(entity.getVisible())
                .supportTime(entity.getSupportTime())
                .build();
    }

    public static List<FrontSupportDTO> toDtoList(List<FrontSupportEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(FrontSupportEntity::toDto)
                .collect(Collectors.toList());
    }
}

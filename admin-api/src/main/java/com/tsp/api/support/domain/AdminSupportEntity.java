package com.tsp.api.support.domain;

import com.tsp.api.common.domain.NewCommonMappedClass;
import com.tsp.api.support.domain.evaluation.EvaluationEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@Table(name = "tsp_support")
public class AdminSupportEntity extends NewCommonMappedClass {

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime supportTime;

    @Column(name = "pass_yn")
    private String passYn;

    @Column(name = "pass_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime passTime;

    @Builder.Default
    @OneToMany(mappedBy = "adminSupportEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<EvaluationEntity> evaluationEntityList = new ArrayList<>();

    public void update(AdminSupportEntity adminSupportEntity) {
        this.supportName = adminSupportEntity.supportName;
        this.supportHeight = adminSupportEntity.supportHeight;
        this.supportSize3 = adminSupportEntity.supportSize3;
        this.supportInstagram = adminSupportEntity.supportInstagram;
        this.supportPhone = adminSupportEntity.supportPhone;
        this.supportMessage = adminSupportEntity.supportMessage;
        this.visible = adminSupportEntity.visible;
        this.passYn = adminSupportEntity.passYn;
        this.passTime = adminSupportEntity.passTime;
    }

    public void addSupport(EvaluationEntity evaluationEntity) {
        evaluationEntity.setAdminSupportEntity(this);
        this.evaluationEntityList.add(evaluationEntity);
    }

    public void togglePassYn(String passYn) {
        this.passYn = Objects.equals(passYn, "Y") ? "N" : "Y";
        this.passTime = LocalDateTime.now();
    }

    public static AdminSupportDTO toDto(AdminSupportEntity entity) {
        if (entity == null) return null;
        return AdminSupportDTO.builder()
                .idx(entity.getIdx())
                .supportName(entity.getSupportName())
                .supportHeight(entity.getSupportHeight())
                .supportSize3(entity.getSupportSize3())
                .supportInstagram(entity.getSupportInstagram())
                .supportPhone(entity.getSupportPhone())
                .supportMessage(entity.getSupportMessage())
                .supportTime(entity.getSupportTime())
                .passYn(entity.getPassYn())
                .passTime(entity.getPassTime())
                .visible(entity.getVisible())
                .build();
    }

    public static List<AdminSupportDTO> toDtoList(List<AdminSupportEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(AdminSupportEntity::toDto)
                .collect(Collectors.toList());
    }
}

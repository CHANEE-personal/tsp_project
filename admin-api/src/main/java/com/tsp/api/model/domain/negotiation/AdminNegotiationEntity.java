package com.tsp.api.model.domain.negotiation;

import com.tsp.api.common.domain.NewCommonMappedClass;
import com.tsp.api.model.domain.AdminModelEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
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
@DynamicUpdate
@Table(name = "tsp_model_negotiation")
public class AdminNegotiationEntity extends NewCommonMappedClass {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "model_kor_name")
    @NotEmpty(message = "모델 국문 이름 입력은 필수입니다.")
    private String modelKorName;

    @Column(name = "model_negotiation_desc")
    @Lob
    @NotEmpty(message = "모델 섭외 내용 입력은 필수입니다.")
    private String modelNegotiationDesc;

    @Column(name = "model_negotiation_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "모델 섭외 일정 입력은 필수입니다.")
    private LocalDateTime modelNegotiationDate;

    @Column(name = "name")
    @NotEmpty(message = "모델 섭외자명 입력은 필수입니다.")
    private String name;

    @Column(name = "email", unique = true)
    @Email
    @NotEmpty(message = "모델 섭외자 이메일 입력은 필수입니다.")
    private String email;

    @Column(name = "phone", unique = true)
    @NotEmpty(message = "모델 섭외자 휴대폰 번호 입력은 필수입니다.")
    private String phone;

    @Column(name = "visible")
    @NotEmpty(message = "모델 섭외 노출 여부 선택은 필수입니다.")
    private String visible;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "model_idx", referencedColumnName = "idx", nullable = false)
    private AdminModelEntity adminModelEntity;

    public void update(AdminNegotiationEntity adminNegotiationEntity) {
        this.modelKorName = adminNegotiationEntity.modelKorName;
        this.modelNegotiationDesc = adminNegotiationEntity.modelNegotiationDesc;
        this.modelNegotiationDate = adminNegotiationEntity.modelNegotiationDate;
        this.name = adminNegotiationEntity.name;
        this.email = adminNegotiationEntity.email;
        this.phone = adminNegotiationEntity.phone;
        this.visible = adminNegotiationEntity.visible;
    }

    public static AdminNegotiationDto toDto(AdminNegotiationEntity entity) {
        if (entity == null) return null;
        return AdminNegotiationDto.builder()
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
                .modelNegotiationDesc(entity.modelNegotiationDesc)
                .modelNegotiationDate(entity.modelNegotiationDate)
                .name(entity.name)
                .email(entity.email)
                .phone(entity.phone)
                .visible(entity.visible)
                .build();
    }

    public static List<AdminNegotiationDto> toDtoList(List<AdminNegotiationEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(AdminNegotiationEntity::toDto)
                .collect(Collectors.toList());
    }
}

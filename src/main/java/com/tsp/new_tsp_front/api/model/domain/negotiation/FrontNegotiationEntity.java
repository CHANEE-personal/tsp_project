package com.tsp.new_tsp_front.api.model.domain.negotiation;

import com.tsp.new_tsp_front.api.common.domain.NewCommonMappedClass;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tsp_model_negotiation")
public class FrontNegotiationEntity extends NewCommonMappedClass {
    @Transient
    private Integer rnum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Integer idx;

    @Column(name = "model_idx")
    @ApiModelProperty(value = "모델 idx", required = true)
    private Integer modelIdx;

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

    @Column(name = "email")
    @Email
    @NotEmpty(message = "모델 섭외자 이메일 입력은 필수입니다.")
    private String email;

    @Column(name = "phone")
    @NotEmpty(message = "모델 섭외자 휴대폰 번호 입력은 필수입니다.")
    private String phone;

    @Column(name = "visible")
    @NotEmpty(message = "모델 스케줄 노출 여부 선택은 필수입니다.")
    private String visible;
}
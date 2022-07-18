package com.tsp.new_tsp_front.api.support.domain;

import com.tsp.new_tsp_front.api.common.domain.NewCommonMappedClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tsp_support")
public class FrontSupportEntity extends NewCommonMappedClass {
    @Transient
    private Integer rnum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Integer idx;

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
    private Date supportTime;
}

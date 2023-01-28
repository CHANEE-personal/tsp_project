package com.tsp.api.model.domain;

import com.tsp.api.common.domain.CommonImageEntity;
import com.tsp.api.common.domain.NewCodeEntity;
import com.tsp.api.common.domain.NewCommonMappedClass;
import com.tsp.api.model.domain.agency.FrontAgencyEntity;
import com.tsp.api.model.domain.negotiation.FrontNegotiationEntity;
import com.tsp.api.model.domain.schedule.FrontScheduleEntity;
import com.tsp.common.CustomConverter;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@Table(name = "tsp_model")
public class FrontModelEntity extends NewCommonMappedClass {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "category_age")
    @Range(min = 2, max = 6, message = "모델 연령대 값은 2~6 사이 값만 입력할 수 있습니다")
    @NotNull(message = "모델 연령대 선택은 필수입니다.")
    private Integer categoryAge;

    @Column(name = "model_kor_name")
    @NotEmpty(message = "모델 국문 이름 입력은 필수입니다.")
    private String modelKorName;

    @Column(name = "model_eng_name")
    @NotEmpty(message = "모델 영문 이름 입력은 필수입니다.")
    private String modelEngName;

    @Column(name = "height")
//    @Range(min = 1, max = 4, message = "1자 이상 4자미만으로 작성해야 합니다.")
    @Digits(integer = 3, fraction = 0)
    @NotNull(message = "모델 키 입력은 필수입니다.")
    private Integer height;

    @Column(name = "size3")
//    @Pattern(regexp = "/^([0-9]{2})$/-?([0-9]{2})$/-?([0-9]{2})$/", message = "**-**-** 형식으로 입력바랍니다.")
    @NotEmpty(message = "모델 사이즈 입력은 필수입니다.")
    private String size3;

    @Column(name = "shoes")
//    @Range(min = 1, max = 4, message = "1자 이상 4자미만으로 작성해야 합니다.")
    @Digits(integer = 3, fraction = 0)
    @NotNull(message = "모델 발 사이즈 입력은 필수입니다.")
    private Integer shoes;

    @Column(name = "model_description")
    @Lob
    @NotEmpty(message = "모델 상세 내용 입력은 필수입니다.")
    private String modelDescription;

    @Column(name = "visible")
    @NotEmpty(message = "모델 노출 여부 선택은 필수입니다.")
    private String visible;

    @Column(name = "model_main_yn")
    @NotEmpty(message = "모델 메인 전시 여부는 필수입니다.")
    private String modelMainYn;

    @Column(name = "model_first_name")
    @NotEmpty(message = "모델 첫번째 이름 입력은 필수입니다.")
    private String modelFirstName;

    @Column(name = "model_second_name")
    @NotEmpty(message = "모델 두번째 이름 입력은 필수입니다.")
    private String modelSecondName;

    @Column(name = "model_kor_first_name")
    @NotEmpty(message = "모델 국문 첫번째 이름 입력은 필수입니다.")
    private String modelKorFirstName;

    @Column(name = "model_kor_second_name")
    @NotEmpty(message = "모델 국문 두번째 이름 입력은 필수입니다.")
    private String modelKorSecondName;

    @Column(name = "favorite_count")
    private Integer modelFavoriteCount;

    @Column(name = "view_count")
    private Integer modelViewCount;

    @Column(name = "new_yn")
    @NotEmpty(message = "새로운 모델 선택은 필수입니다.")
    private String newYn;

    @Column(name = "career_list")
    @Convert(converter = CustomConverter.class)
    private ArrayList<CareerJson> careerList;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_cd")
    private NewCodeEntity newModelCodeJpaDTO;

    @Builder.Default
    @BatchSize(size = 5)
    @Where(clause = "type_name = 'model'")
    @OneToMany(mappedBy = "frontModelEntity", fetch = LAZY, cascade = REMOVE)
    private List<CommonImageEntity> commonImageEntityList = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "agency_idx", referencedColumnName = "idx")
    private FrontAgencyEntity frontAgencyEntity;

    @Builder.Default
    @OneToMany(mappedBy = "frontModelEntity", fetch = LAZY)
    private List<FrontScheduleEntity> modelScheduleList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "frontModelEntity", fetch = LAZY)
    private List<FrontNegotiationEntity> modelNegotiationList = new ArrayList<>();

    public void updateViewCount() {
        this.modelViewCount++;
    }
    public void updateFavoriteCount() {
        this.modelFavoriteCount++;
    }

    public void addNegotiation(FrontNegotiationEntity frontNegotiationEntity) {
        frontNegotiationEntity.setFrontModelEntity(this);
        this.modelNegotiationList.add(frontNegotiationEntity);
    }

    public static FrontModelDTO toDto(FrontModelEntity entity) {
        if (entity == null) return null;
        return FrontModelDTO.builder()
                .idx(entity.getIdx())
                .categoryCd(entity.getNewModelCodeJpaDTO().getCategoryCd())
                .agencyIdx(entity.getFrontAgencyEntity().getIdx())
                .modelKorName(entity.getModelKorName())
                .modelEngName(entity.getModelEngName())
                .modelDescription(entity.getModelDescription())
                .visible(entity.getVisible())
                .height(entity.getHeight())
                .shoes(entity.getShoes())
                .size3(entity.getSize3())
                .categoryAge(entity.getCategoryAge())
                .modelMainYn(entity.getModelMainYn())
                .modelFirstName(entity.getModelFirstName())
                .modelSecondName(entity.getModelSecondName())
                .modelKorFirstName(entity.getModelKorFirstName())
                .modelKorSecondName(entity.getModelKorSecondName())
                .modelFavoriteCount(entity.getModelFavoriteCount())
                .modelViewCount(entity.getModelViewCount())
                .newYn(entity.getNewYn())
                .careerList(entity.getCareerList())
                .build();
    }

    public static List<FrontModelDTO> toDtoList(List<FrontModelEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(FrontModelEntity::toDto)
                .collect(Collectors.toList());
    }
}

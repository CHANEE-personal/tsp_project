package com.tsp.new_tsp_front.api.model.domain;

import com.tsp.new_tsp_front.api.common.domain.CommonImageEntity;
import com.tsp.new_tsp_front.api.common.domain.NewCodeEntity;
import com.tsp.new_tsp_front.api.common.domain.NewCommonMappedClass;
import com.tsp.new_tsp_front.common.CustomConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tsp_model")
public class FrontModelEntity extends NewCommonMappedClass {

	@Transient
	private Integer rnum;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idx")
	private Integer idx;

	@Column(name = "category_cd")
	@Range(min = 1, max = 3, message = "모델 카테고리 값은 1~3 사이 값만 입력할 수 있습니다")
	@NotNull(message = "모델 카테고리 선택은 필수입니다.")
	private Integer categoryCd;

	@Column(name = "category_age")
	@Range(min = 2, max = 6, message = "모델 연령대 값은 2~6 사이 값만 입력할 수 있습니다")
	@NotEmpty(message = "모델 연령대 선택은 필수입니다.")
	private String categoryAge;

	@Column(name = "model_kor_name")
	@NotEmpty(message = "모델 국문 이름 입력은 필수입니다.")
	private String modelKorName;

	@Column(name = "model_eng_name")
	@NotEmpty(message = "모델 영문 이름 입력은 필수입니다.")
	private String modelEngName;

	@Column(name = "height")
	@Range(min=1, max=4, message = "1자 이상 4자미만으로 작성해야 합니다.")
	@Pattern(regexp="\\\\d{1,3}", message = "숫자만 입력 가능합니다.")
	@NotEmpty(message = "모델 키 입력은 필수입니다.")
	private String height;

	@Column(name = "size3")
	@Range(min=1, max=4, message = "1자 이상 4자미만으로 작성해야 합니다.")
	@Pattern(regexp="/^([0-9]{2})$/-?([0-9]{2})$/-?([0-9]{2})$/", message = "**-**-** 형식으로 입력바랍니다.")
	@NotEmpty(message = "모델 사이즈 입력은 필수입니다.")
	private String size3;

	@Column(name = "shoes")
	@Range(min=1, max=4, message = "1자 이상 4자미만으로 작성해야 합니다.")
	@Pattern(regexp="\\\\d{1,3}", message = "숫자만 입력 가능합니다.")
	@NotEmpty(message = "모델 발 사이즈 입력은 필수입니다.")
	private String shoes;

	@Column(name = "model_description")
	@Lob
	@NotEmpty(message = "모델 상세 내용 입력은 필수입니다.")
	private String modelDescription;

	@NotEmpty
	@Column(name = "visible")
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

	@Column(name = "career_list")
	@Convert(converter = CustomConverter.class)
	private ArrayList<CareerJson> careerList;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "category_cd", insertable = false, updatable = false)
	private NewCodeEntity newModelCodeJpaDTO;

	@OneToMany(mappedBy = "frontModelEntity")
	private List<CommonImageEntity> commonImageEntityList = new ArrayList<>();

}

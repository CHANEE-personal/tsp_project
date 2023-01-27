package com.tsp.api.model.domain;

import com.tsp.api.common.domain.CommonImageDTO;
import com.tsp.api.common.domain.NewCommonDTO;
import com.tsp.api.model.domain.agency.AdminAgencyDTO;
import com.tsp.api.model.domain.negotiation.AdminNegotiationDTO;
import com.tsp.api.model.domain.schedule.AdminScheduleDTO;
import com.tsp.common.CustomConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.lang.Nullable;

import javax.persistence.Convert;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel(value = "모델 관련 변수")
public class AdminModelDTO extends NewCommonDTO {

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @Range(min = 1, max = 3, message = "모델 categoryCd는 1~3 사이 값만 입력할 수 있습니다.")
    @ApiModelProperty(position = 1, required = true, value = "남자,여자,시니어 모델 구분((ex)1,2,3)", example = "1")
    private Integer categoryCd;

    @Range(min = 2, max = 6, message = "모델 연령대 값은 2~6 사이 값만 입력할 수 있습니다.")
    @ApiModelProperty(position = 2, required = true, value = "모델 연령대((ex)2:20,3:30***)", example = "1")
    private Integer categoryAge;

    @NotEmpty(message = "모델 국문 이름 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "men Kor Name", hidden = true)
    private String modelKorName;

    @NotEmpty(message = "모델 영문 이름 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "men Eng Name", hidden = true)
    private String modelEngName;

    @NotEmpty(message = "모델 상세 내용 입력은 필수입니다.")
    @ApiModelProperty(position = 7, required = true, value = "모델 상세 설명")
    private String modelDescription;

    @ApiModelProperty(position = 8, required = true, value = "모델 키((ex)180)", example = "180")
    @NotNull(message = "모델 키 입력은 필수입니다.")
	@Pattern(regexp="\\\\d{1,3}", message = "숫자만 입력 가능합니다.")
	@Length(min=1, max=4, message = "1자 이상 4자미만으로 작성해야 합니다.")
    private Integer height;

    @NotEmpty(message = "모델 사이즈 입력은 필수입니다.")
	@Pattern(regexp="/^(\\d{2})$/-?(\\d{2})$/-?(\\d{2})$/", message = "**-**-** 형식으로 입력바랍니다.")
    @ApiModelProperty(position = 9, required = true, value = "모델 사이즈((ex)31-24-31)")
    private String size3;

    @ApiModelProperty(position = 10, required = true, value = "모델 신발 사이즈((ex)270)", example = "270")
    @NotNull(message = "모델 신발 사이즈 입력은 필수입니다.")
	@Pattern(regexp="\\d{3}", message = "숫자만 입력 가능합니다.")
	@Length(min=1, max=4, message = "1자 이상 4자미만으로 작성해야 합니다.")
    private Integer shoes;

    @ApiModelProperty(position = 12, required = true, value = "모델 노출 여부((ex)Y,N)")
    @NotEmpty(message = "모델 노출 여부 선택은 필수입니다.")
    private String visible;

    @ApiModelProperty(position = 11, required = true, value = "모델 메인 전시 여부((ex)Y,N)")
    @NotEmpty(message = "모델 메인 노출 여부 선택은 필수입니다.")
    private String modelMainYn;

    @ApiModelProperty(position = 3, required = true, value = "모델 영문 성((ex)CHO)")
    @NotEmpty(message = "모델 영문 성 입력은 필수입니다.")
    private String modelFirstName;

    @ApiModelProperty(position = 4, required = true, value = "모델 영문 이름((ex)CHAN HEE)")
    @NotEmpty(message = "모델 영문 이름 입력은 필수입니다.")
    private String modelSecondName;

    @ApiModelProperty(position = 5, required = true, value = "모델 국문 성((ex)조)")
    @NotEmpty(message = "모델 국문 성 입력은 필수입니다.")
    private String modelKorFirstName;

    @ApiModelProperty(position = 6, required = true, value = "모델 국문 이름((ex)찬희)")
    @NotEmpty(message = "모델 국문 이름 입력은 필수입니다.")
    private String modelKorSecondName;

    @ApiModelProperty(position = 7, required = true, value = "모델 좋아요 수((ex)0)")
    private int favoriteCount;

    @ApiModelProperty(position = 8, value = "모델 조회 수((ex)0", example = "1")
    private int viewCount;

    @ApiModelProperty(position = 9, value = "소속사 idx((ex)1", example = "1")
    private Long agencyIdx;

    @Convert(converter = CustomConverter.class)
    @ApiModelProperty(value = "model career")
    private List<CareerJson> careerList = new ArrayList<>();

    @NotEmpty(message = "모델 상태 선택은 필수입니다.")
    @ApiModelProperty(required = true, value = "model status")
    private String status;

    @NotEmpty(message = "새로운 모델 선택은 필수입니다.")
    @ApiModelProperty(required = true, value = "새로운 모델((ex)Y,N")
    private String newYn;

    @Type(type = "json")
    @Nullable
    @ApiModelProperty(value = "model keyword")
    private List<String> modelKeyword = new ArrayList<>();

    @ApiModelProperty(value = "modelImageList", hidden = true)
    private List<CommonImageDTO> modelImage = new ArrayList<>();

    @ApiModelProperty(value = "modelAgency", hidden = true)
    private AdminAgencyDTO modelAgency;

    @ApiModelProperty(value = "modelScheduleList", hidden = true)
    private List<AdminScheduleDTO> modelSchedule = new ArrayList<>();

    @ApiModelProperty(value = "modelNegotiationList", hidden = true)
    private List<AdminNegotiationDTO> modelNegotiation = new ArrayList<>();
}

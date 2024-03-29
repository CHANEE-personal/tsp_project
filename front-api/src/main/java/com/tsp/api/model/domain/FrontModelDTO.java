package com.tsp.api.model.domain;

import com.tsp.api.common.domain.CommonImageDTO;
import com.tsp.api.common.domain.NewCommonDTO;
import com.tsp.api.model.domain.agency.FrontAgencyDTO;
import com.tsp.api.model.domain.negotiation.FrontNegotiationDTO;
import com.tsp.api.model.domain.schedule.FrontScheduleDTO;
import com.tsp.common.CustomConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Convert;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel
public class FrontModelDTO extends NewCommonDTO {

    @ApiModelProperty(required = true, value = "idx", hidden = true)
    private Long idx;

    @ApiModelProperty(required = true, value = "category code")
    private Integer categoryCd;

    @ApiModelProperty(required = true, value = "category age")
    private Integer categoryAge;

    @NotEmpty(message = "모델 국문 이름 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "men Kor Name")
    private String modelKorName;

    @NotEmpty(message = "모델 영문 이름 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "men Eng Name")
    private String modelEngName;

    @NotEmpty(message = "모델 상세 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "model Description")
    private String modelDescription;

    @ApiModelProperty(required = true, value = "model height")
    @NotNull(message = "모델 키 입력은 필수입니다.")
    @Digits(integer = 3, fraction = 0)
//    @Range(min = 1, max = 4, message = "1자 이상 4자미만으로 작성해야 합니다.")
    private Integer height;

    @NotEmpty(message = "모델 사이즈 입력은 필수입니다.")
//    @Pattern(regexp = "/^(\\d{2})$/-?(\\d{2})$/-?(\\d{2})$/", message = "**-**-** 형식으로 입력바랍니다.")
    @ApiModelProperty(required = true, value = "model 3size")
    private String size3;

    @ApiModelProperty(required = true, value = "model shoes")
    @NotNull(message = "모델 신발 사이즈 입력은 필수입니다.")
    @Digits(integer = 3, fraction = 0)
//    @Range(min = 1, max = 4, message = "1자 이상 4자미만으로 작성해야 합니다.")
    private Integer shoes;

    @ApiModelProperty(required = true, value = "visible")
    private String visible;

    @ApiModelProperty(required = true, value = "model main YN")
    private String modelMainYn;

    @ApiModelProperty(required = true, value = "model first name")
    private String modelFirstName;

    @ApiModelProperty(required = true, value = "model second name")
    private String modelSecondName;

    @ApiModelProperty(required = true, value = "model kor first name")
    private String modelKorFirstName;

    @ApiModelProperty(required = true, value = "model kor second name")
    private String modelKorSecondName;

    @ApiModelProperty(value = "model favorite count")
    private Integer modelFavoriteCount;

    @ApiModelProperty(value = "model view count")
    private Integer modelViewCount;

    @ApiModelProperty(required = true, value = "새로운 모델 여부((ex)Y,N")
    private String newYn;

    @ApiModelProperty(value = "소속사 idx((ex)1")
    private Long agencyIdx;

    @ApiModelProperty(position = 10, value = "소속사명((ex)소속사", example = "소속사")
    private String agencyName;

    @ApiModelProperty(position = 10, value = "소속사 내용((ex)소속사", example = "소속사")
    private String agencyDescription;

    @Convert(converter = CustomConverter.class)
    @ApiModelProperty(value = "model career")
    private ArrayList<CareerJson> careerList;

    @ApiModelProperty(required = true, value = "modelImageList", hidden = true)
    private List<CommonImageDTO> modelImage = new ArrayList<>();

    @ApiModelProperty(required = true, value = "modelAgency", hidden = true)
    private FrontAgencyDTO modelAgency;

    @ApiModelProperty(value = "modelScheduleList", hidden = true)
    private List<FrontScheduleDTO> modelScheduleList = new ArrayList<>();

    @ApiModelProperty(value = "modelNegotiationList", hidden = true)
    private List<FrontNegotiationDTO> modelNegotiationList = new ArrayList<>();
}

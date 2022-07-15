package com.tsp.new_tsp_front.api.model.domain;

import com.tsp.new_tsp_front.api.common.domain.CommonImageDTO;
import com.tsp.new_tsp_front.api.common.domain.NewCommonDTO;
import com.tsp.new_tsp_front.common.CustomConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Convert;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class FrontModelDTO extends NewCommonDTO {
    @ApiModelProperty(required = true, value = "rnum", hidden = true)
    private Integer rnum;

    @ApiModelProperty(required = true, value = "idx", hidden = true)
    private Integer idx;

    @ApiModelProperty(required = true, value = "category code")
    private Integer categoryCd;

    @ApiModelProperty(required = true, value = "category age")
    private String categoryAge;

    @NotNull(message = "모델 국문 이름 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "men Kor Name")
    private String modelKorName;

    @NotNull(message = "모델 영문 이름 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "men Eng Name")
    private String modelEngName;

    @NotNull(message = "모델 상세 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "model Description")
    private String modelDescription;

    @ApiModelProperty(required = true, value = "model height")
    @NotNull(message = "모델 키 입력은 필수입니다.")
    @Pattern(regexp = "\\\\d{1,3}", message = "숫자만 입력 가능합니다.")
    @Range(min = 1, max = 4, message = "1자 이상 4자미만으로 작성해야 합니다.")
    private String height;

    @NotNull(message = "모델 사이즈 입력은 필수입니다.")
    @Pattern(regexp = "/^([0-9]{2})$/-?([0-9]{2})$/-?([0-9]{2})$/", message = "**-**-** 형식으로 입력바랍니다.")
    @ApiModelProperty(required = true, value = "model 3size")
    private String size3;

    @ApiModelProperty(required = true, value = "model shoes")
    @NotNull(message = "모델 신발 사이즈 입력은 필수입니다.")
    @Pattern(regexp = "[0-9]{3}", message = "숫자만 입력 가능합니다.")
    @Range(min = 1, max = 4, message = "1자 이상 4자미만으로 작성해야 합니다.")
    private String shoes;

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

    @Convert(converter = CustomConverter.class)
    @ApiModelProperty(required = false, value = "model career")
    private ArrayList<CareerJson> careerList;

    @ApiModelProperty(required = true, value = "modelImageList", hidden = true)
    private List<CommonImageDTO> modelImage = new ArrayList<>();
}

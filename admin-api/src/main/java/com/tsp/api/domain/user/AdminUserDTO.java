package com.tsp.api.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tsp.api.domain.common.NewCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel
public class AdminUserDTO extends NewCommonDTO {

    @ApiModelProperty(required = true, value = "user Seq", hidden = true, example = "1")
    private Long idx;

    @NotEmpty(message = "유저 아이디 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "user Id")
    private String userId;

    @NotEmpty(message = "유저 패스워드 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "user Password")
    private String password;

    @NotEmpty(message = "유저 이름 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "user Name", hidden = true)
    private String name;

    @NotEmpty(message = "유저 이메일 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "user email", hidden = true)
    private String email;

    @NotEmpty(message = "유저 사용여부 선택은 필수입니다.")
    @ApiModelProperty(required = true, value = "user visible", hidden = true)
    private String visible;

    @ApiModelProperty(value = "user Token", hidden = true)
    private String userToken;

    @ApiModelProperty(value = "user refresh Token", hidden = true)
    private String userRefreshToken;

    @ApiModelProperty(value = "role", hidden = true)
    private Role role;
}

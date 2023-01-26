package com.tsp.api.user.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequest {
    @ApiModelProperty(value = "아이디", example = "test123", required = true)
    @NotBlank(message = "아이디를 입력해주세요.")
    private String userId;

    @ApiModelProperty(value = "비밀번호", example = "password", required = true)
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 6, max = 14)
    private String password;

    @ApiModelProperty(value = "이름", example = "sh", required = true)
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @ApiModelProperty(required = true, value = "user email")
    @NotBlank(message = "이메일 입력은 필수입니다.")
    private String email;

    @ApiModelProperty(required = true, value = "user visible")
    @NotBlank(message = "유저 사용여부 선택은 필수입니다.")
    private String visible;

    @Builder(access = AccessLevel.PUBLIC)
    private SignUpRequest(String userId, String password, String name, String email, String visible) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.visible = visible;
    }
}

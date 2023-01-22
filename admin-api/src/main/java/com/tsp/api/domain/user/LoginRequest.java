package com.tsp.api.domain.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {

    @ApiModelProperty(value = "아이디", example = "test123", required = true)
    @NotBlank(message = "아이디를 입력해주세요.")
    private String userId;

    @ApiModelProperty(value = "비밀번호", example = "password", required = true)
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @Builder(access = AccessLevel.PUBLIC)
    private LoginRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}

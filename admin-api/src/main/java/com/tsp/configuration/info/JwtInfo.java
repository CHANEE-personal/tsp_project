package com.tsp.configuration.info;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "spring.jwt")
public class JwtInfo {

    @NotBlank
    private final String secretKey;

    @NotBlank
    private final String header;

    @NotBlank
    private final String authenticationPath;
}

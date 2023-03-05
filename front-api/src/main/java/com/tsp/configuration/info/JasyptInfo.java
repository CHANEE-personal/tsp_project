package com.tsp.configuration.info;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "jasypt.encryptor")
public class JasyptInfo {

    @NotBlank
    private final String bean;

    @NotBlank
    private final String password;
}

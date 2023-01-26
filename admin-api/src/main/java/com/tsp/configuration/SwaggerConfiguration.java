package com.tsp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static java.util.List.of;
import static springfox.documentation.builders.PathSelectors.ant;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;
import static springfox.documentation.spi.service.contexts.SecurityContext.builder;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    /**
     * <pre>
     * 1. MethodName : apiInfo
     * 2. ClassName  : swaggerConfiguration.java
     * 3. Comment    : swagger api 정보
     * 4. 작성자      : CHO
     * 5. 작성일      : 2021. 02. 09.
     * </pre>
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Tsp Admin")
                .description("Tsp Admin API")
                .build();
    }

    /**
     * <pre>
     * 1. MethodName : commonApi
     * 2. ClassName  : swaggerConfiguration.java
     * 3. Comment    : swagger api 경로 설정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2021. 02. 09.
     * </pre>
     */
    @Bean
    public Docket commonApi() {
        return new Docket(SWAGGER_2)
                .groupName("tsp")
                .apiInfo(this.apiInfo())
                .select()
                .apis(basePackage("com.tsp.api"))
                .paths(ant("/api/**"))
                .build()
                .securityContexts(of(securityContext()))
                .securitySchemes(of(apikey()));
    }

    /**
     * <pre>
     * 1. MethodName : apiKey
     * 2. ClassName  : swaggerConfiguration.java
     * 3. Comment    : swagger api key 설정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2021. 02. 09.
     * </pre>
     */
    private ApiKey apikey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return of(new SecurityReference("JWT", authorizationScopes));
    }
}

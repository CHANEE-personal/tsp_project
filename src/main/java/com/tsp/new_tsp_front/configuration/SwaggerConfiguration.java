package com.tsp.new_tsp_front.configuration;

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
import static springfox.documentation.builders.PathSelectors.*;
import static springfox.documentation.builders.RequestHandlerSelectors.*;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;
import static springfox.documentation.spi.service.contexts.SecurityContext.*;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	/**
	 * <pre>
	 * 1. MethodName : apiInfo
	 * 2. ClassName  : SwaggerConfiguration.java
	 * 3. Comment    : api Info
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 15.
	 * </pre>
	 *
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Tsp Model")
				.description("Tsp Model API")
				.build();
	}

	/**
	 * <pre>
	 * 1. MethodName : commonApi
	 * 2. ClassName  : SwaggerConfiguration.java
	 * 3. Comment    : Api path and info config
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 15.
	 * </pre>
	 *
	 */
	@Bean
	public Docket commonApi() {
		return new Docket(SWAGGER_2)
				.groupName("tsp")
				.apiInfo(this.apiInfo())
				.select()
				.apis(basePackage("com.tsp.new_tsp_front.api"))
				.paths(ant("/api/**"))
				.build()
				.securityContexts(of(securityContext()))
				.securitySchemes(of(apikey()));
	}

	/**
	 * <pre>
	 * 1. MethodName : apiKey
	 * 2. ClassName  : SwaggerConfiguration.java
	 * 3. Comment    : api key 설정
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 15.
	 * </pre>
	 *
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

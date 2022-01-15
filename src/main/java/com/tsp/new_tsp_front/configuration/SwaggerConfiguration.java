package com.tsp.new_tsp_front.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

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
	 * @throws Exception
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
	 * @throws Exception
	 */
	@Bean
	public Docket commonApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("tsp")
				.apiInfo(this.apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.tsp.new_tsp_front.api"))
				.paths(PathSelectors.ant("/api/**"))
				.build()
				.securityContexts(List.of(securityContext()))
				.securitySchemes(List.of(apikey()));
	}

	private ApiKey apikey() {
		return new ApiKey("JWT", "Authorization", "header");
	}

	private SecurityContext securityContext() {
		return springfox.documentation.spi.service.contexts.SecurityContext
				.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return List.of(new SecurityReference("JWT", authorizationScopes));
	}
}

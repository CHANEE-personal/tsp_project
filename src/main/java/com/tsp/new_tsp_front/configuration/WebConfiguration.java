package com.tsp.new_tsp_front.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

//	@Autowired
//	private HandlerInterceptor loginCheckInterceptor;

	private static final String[] CLASSPATH_RESOURCE_LOCATIONS =
			{"classpath:/static/",
					"classpath:/public/",
					"classpath:/",
					"classpath:/resources/",
					"classpath:/META-INF/resources/",
					"classpath:/META-INF/resources/webjars/"};

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
//				.allowedOrigins("http://ec2-13-124-84-154.ap-northeast-2.compute.amazonaws.com/")
				.allowedOrigins("http://localhost:8080/")
				.allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS")
				.allowedHeaders("*")

				.maxAge(3600);
	}

//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(loginCheckInterceptor)
//				.order(1)
//				.excludePathPatterns("/api/auth/admin-login")
//				.excludePathPatterns(
//						"/v2/api-docs",
//						"/swagger-resources/**",
//						"/swagger-ui.html",
//						"/webjars/**"
//				);
//	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
	}


//    @Bean
//    public CommonsMultipartResolver multipartResolver() {
//        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//        multipartResolver.setDefaultEncoding("UTF-8");  // 파일 인코딩 설정
//        multipartResolver.setMaxUploadSizePerFile(10 * 1024 * 1024);    // 파일당 업로드 크기 제한 (10MB)
//
//        return multipartResolver;
//    }
}

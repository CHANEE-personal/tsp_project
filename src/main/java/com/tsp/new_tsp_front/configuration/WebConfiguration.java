package com.tsp.new_tsp_front.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
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

	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		return new MethodValidationPostProcessor();
	}

	@Override
	public MessageCodesResolver getMessageCodesResolver() {
		DefaultMessageCodesResolver codesResolver = new DefaultMessageCodesResolver();
		codesResolver.setMessageCodeFormatter(DefaultMessageCodesResolver.Format.POSTFIX_ERROR_CODE);
		return codesResolver;
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource =
				new ReloadableResourceBundleMessageSource();
		messageSource.addBasenames("classpath:messages/modelMessage");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public LocalValidatorFactoryBean getValidator(MessageSource messageSource) {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource);
		return bean;
	}
}

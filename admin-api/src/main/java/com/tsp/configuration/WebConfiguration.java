package com.tsp.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.validation.DefaultMessageCodesResolver.Format.POSTFIX_ERROR_CODE;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS =
            {"classpath:/static/",
                    "classpath:/public/",
                    "classpath:/",
                    "classpath:/resources/",
                    "classpath:/META-INF/resources/",
                    "classpath:/META-INF/resources/webjars/"};

    /**
     * <pre>
     * 1. MethodName : addCorsMappings
     * 2. ClassName  : WebConfiguration.java
     * 3. Comment    : Cors 처리
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 15.
     * </pre>
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//				.allowedOrigins("http://ec2-13-124-84-154.ap-northeast-2.compute.amazonaws.com/")
                .allowedOrigins("http://localhost:8080/")
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }

    /**
     * <pre>
     * 1. MethodName : methodValidationPostProcessor
     * 2. ClassName  : WebConfiguration.java
     * 3. Comment    : method parameter or return validation check
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 15.
     * </pre>
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    /**
     * <pre>
     * 1. MethodName : getMessageCodesResolver
     * 2. ClassName  : WebConfiguration.java
     * 3. Comment    : DefaultMessageCodesResolver Format
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 15.
     * </pre>
     */
    @Override
    public MessageCodesResolver getMessageCodesResolver() {
        DefaultMessageCodesResolver codesResolver = new DefaultMessageCodesResolver();
        codesResolver.setMessageCodeFormatter(POSTFIX_ERROR_CODE);
        return codesResolver;
    }

    /**
     * <pre>
     * 1. MethodName : messageSource
     * 2. ClassName  : WebConfiguration.java
     * 3. Comment    : validation Message path 설정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 15.
     * </pre>
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.addBasenames("classpath:messages/modelMessage");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * <pre>
     * 1. MethodName : getValidator
     * 2. ClassName  : WebConfiguration.java
     * 3. Comment    : DTO or Entity validation Message expression
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 15.
     * </pre>
     */
    @Bean
    public LocalValidatorFactoryBean getValidator(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }
}

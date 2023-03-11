package com.tsp;

import com.tsp.configuration.info.JasyptInfo;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com")
@EnableConfigurationProperties({JasyptInfo.class})
@SpringBootApplication(scanBasePackages = "com")
@EnableCaching
public class TspFrontProject extends SpringBootServletInitializer {
    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.properties";

    public static void main(String[] args) {
        new SpringApplicationBuilder(TspFrontProject.class).properties(APPLICATION_LOCATIONS).run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(TspFrontProject.class);
    }
}

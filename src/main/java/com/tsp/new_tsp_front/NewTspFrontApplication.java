package com.tsp.new_tsp_front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@ComponentScans({
		@ComponentScan(basePackages = "com.tsp")
})
@SpringBootApplication(scanBasePackages = "com")
@EnableCaching
public class NewTspFrontApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(NewTspFrontApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(NewTspFrontApplication.class);
	}
}

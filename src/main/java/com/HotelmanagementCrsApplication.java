package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@ServletComponentScan
@EnableScheduling
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableCaching
public class HotelmanagementCrsApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(HotelmanagementCrsApplication.class, args);
	}
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(HotelmanagementCrsApplication.class);
	}

	@Autowired
	private RestTemplateBuilder builder;


	@Bean
	public RestTemplate restTemplate() {

		return builder.build();
	}
}

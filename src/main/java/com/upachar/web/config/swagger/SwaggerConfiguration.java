package com.upachar.web.config.swagger;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
	 @Bean
	 public Docket api() {
	 return new Docket(DocumentationType.SWAGGER_2)
	 .select()
	 .apis(RequestHandlerSelectors.basePackage("com.upachar.web"))
	 .paths(PathSelectors.any())
	 .build()
	 .apiInfo(apiInfo());
	 }


	private ApiInfo apiInfo() {
		return new ApiInfo("Upachar Pharmacy", "API documentation for Upachar Pharmacy", "version 1.0", "Terms of service",

				new Contact("Rokin Maharjan", "www.linkedin.com/in/rokin/", "rokinmaharjan@gmail.com"),
				"License of API", "API license URL", Collections.emptyList());

	}
}

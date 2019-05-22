package com.smi.drools.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.models.Contact;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket api() { 
		return new Docket(DocumentationType.SWAGGER_2)  
				.select()                                   
				.apis(RequestHandlerSelectors.basePackage("com.smi.drools.controller"))
				.paths(PathSelectors.ant("/test/createdocumentcontextrule"))                          
				.build()
				.apiInfo(apiInfo());                                           
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("RuleEngine API")
				.description("RuleEngine API reference for developers")
				.termsOfServiceUrl("http://com.smi.drools")
				.contact("docbot@mookambikainfo.com").license("RuleEngine License")
				.licenseUrl("docbot@mookambikainfo.com").version("1.0").build();
	}
}

package com.smi.drools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer
public class SpringBootDroolsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDroolsApplication.class, args);
	}

}

package com.lassulfi.app.photoapp.api.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import feign.Logger;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
public class PhotoAppApiUsersApplication {
	
	@Autowired
	Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(PhotoAppApiUsersApplication.class, args);
	}
	
	@Bean
	public BCryptPasswordEncoder createPasswordEncoderBean() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	@Profile("production")
	Logger.Level feignLoggerLevel() {
		return Logger.Level.NONE;
	}
	
	@Bean
	@Profile("!production")
	Logger.Level feignDefaultLoggerLevel() {
		return Logger.Level.FULL;
	}
	
	@Bean
	@Profile("production")
	public String createProductionBean() {
		System.out.println("Production bean created. myaplication.environment = " 
				+ environment.getProperty("myaplication.environment"));
		return "Production Bean";
	}
	
	@Bean
	@Profile("!production")
	public String createNotProductionBean() {
		System.out.println("Not production bean created. myaplication.environment = " 
				+ environment.getProperty("myaplication.environment"));
		return "Not Production Bean";
	}
	
	@Bean
	@Profile("default")
	public String createDevelopmentBean() {
		System.out.println("Development bean created. myaplication.environment =  " 
				+ environment.getProperty("myaplication.environment"));
		return "Development Bean";
	}
	
//	@Bean
//	public FeignErrorDecoder getFeignErrorDecoder() {
//		return new FeignErrorDecoder();
//	}
 
}

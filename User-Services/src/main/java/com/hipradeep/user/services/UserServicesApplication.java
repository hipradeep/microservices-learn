package com.hipradeep.user.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableFeignClients
public class UserServicesApplication {

	//SIMPLEWAY123
	@Bean
	@LoadBalanced  // use for SERVICE NAME like RATING-SERVICE
	RestTemplate restTemplate(){
		return new RestTemplate();
	}



	public static void main(String[] args) {
		SpringApplication.run(UserServicesApplication.class, args);
	}

}

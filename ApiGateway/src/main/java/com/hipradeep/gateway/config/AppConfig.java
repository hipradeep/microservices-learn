package com.hipradeep.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    public static String keyHeaderUsername="username";
    public static String keyHeaderEmail="email";
    public static String keyHeaderPassword="password";
    public static String keyHeaderName="name";

    @Bean
    public RestTemplate template(){
       return new RestTemplate();
    }

}

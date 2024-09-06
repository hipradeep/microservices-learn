package com.hipradeep.gateway.config;

import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableWebFluxSecurity //REMOVE-OCTA
public class SecurityConfig {
    //REMOVE-OCTA
/*
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {

        httpSecurity
                .authorizeExchange()
                .anyExchange()
                .authenticated()
                .and()
                .oauth2Client()
                .and()
                .oauth2ResourceServer()
                .jwt();


        return httpSecurity.build();


    }

*/
}

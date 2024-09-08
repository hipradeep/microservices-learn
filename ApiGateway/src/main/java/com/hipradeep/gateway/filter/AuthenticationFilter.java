package com.hipradeep.gateway.filter;

import com.hipradeep.gateway.config.AppConfig;
import com.hipradeep.gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

//    @Autowired
//    private RestTemplate restTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = null;
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("missing authorization header");
                }

                String authHeaderToken = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeaderToken != null && authHeaderToken.startsWith("Bearer ")) {
                    authHeaderToken = authHeaderToken.substring(7);
                }
                try {
                    //REST call to AUTH service
                    //Validate the authentication token using restTemplate
                    //template.getForObject("http://AUTH-SERVICE/validate?token" + authHeader, String.class);
                    jwtUtil.validateToken(authHeaderToken);

                    Claims claims = jwtUtil.extractAllClaims(authHeaderToken);

                    System.out.println(" Filter Username : " + jwtUtil.extractUsername(authHeaderToken));
                    System.out.println(" Filter Email : " + claims.get(AppConfig.keyHeaderEmail, String.class));
                    System.out.println(" Filter Claims : " + claims);

                    //get token PAYLOAD:DATA
                    request = exchange.getRequest()
                            .mutate()
                            .header(AppConfig.keyHeaderUsername, jwtUtil.extractUsername(authHeaderToken))
                            .header(AppConfig.keyHeaderEmail, claims.get(AppConfig.keyHeaderEmail, String.class))
                            .header(AppConfig.keyHeaderPassword, claims.get(AppConfig.keyHeaderPassword, String.class))
                            .header(AppConfig.keyHeaderName, claims.get(AppConfig.keyHeaderName, String.class))
                            .build();

                } catch (Exception e) {
                    System.out.println("invalid access...!");
                    throw new RuntimeException("un authorized access to application");
                }
            }

            assert request != null;
            return chain.filter(exchange.mutate().request(request).build());
        });
    }

    public static class Config {

    }
}

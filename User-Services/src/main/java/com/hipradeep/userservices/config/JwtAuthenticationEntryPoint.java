package com.hipradeep.userservices.config;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        // You can extract error from request attribute if set by JwtFilter
        final String errorMessage = (String) request.getAttribute("error");

        String jsonResponse = String.format(
                "{ \"timestamp\": \"%s\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"%s\", \"path\": \"%s\" }",
                new Date(),
                errorMessage != null ? errorMessage : authException.getMessage(),
                request.getRequestURI()
        );

        response.getWriter().write(jsonResponse);
    }
}
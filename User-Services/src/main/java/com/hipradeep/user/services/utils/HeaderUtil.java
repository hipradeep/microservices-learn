package com.hipradeep.user.services.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;



@Component
public class HeaderUtil {

    // Method to fetch header from the request
    public String getHeaderValue(HttpServletRequest request, String headerName) {
        return request.getHeader(headerName);
    }

    // Method to fetch header from the current request
    public String getHeaderValue(String headerName) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        return request.getHeader(headerName);
    }


}

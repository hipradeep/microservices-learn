package com.hipradeep.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse<T> {
    private Integer status;
    private String message;
    private String token;
    private T data;
}

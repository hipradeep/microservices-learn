package com.hipradeep.authservice.dto;

import com.hipradeep.authservice.entity.UserCredential;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentialDto {
    private String username;
    private String email;
    private String password;


}

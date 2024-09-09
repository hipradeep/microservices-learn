package com.hipradeep.authservice.service;

import com.hipradeep.authservice.entity.UserCredential;
import com.hipradeep.authservice.repository.UserCredentialRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserCredentialRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public String saveUser(UserCredential credential) {
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
        repository.save(credential);
        return "user added to the system";
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public String generateToken(String username, Map<String, Object> claims) {


        return jwtService.generateToken(username,claims);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }


}

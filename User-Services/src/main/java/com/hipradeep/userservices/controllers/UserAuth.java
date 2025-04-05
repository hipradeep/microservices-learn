package com.hipradeep.userservices.controllers;

import com.hipradeep.userservices.dto.AuthRequest;
import com.hipradeep.userservices.dto.AuthResponse;
import com.hipradeep.userservices.entities.User;
import com.hipradeep.userservices.services.UserDetailsServiceImpl;
import com.hipradeep.userservices.services.UserService;
import com.hipradeep.userservices.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/auth")
@Slf4j
public class UserAuth {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthRequest authRequest) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());

            return new ResponseEntity<>(new AuthResponse( userDetails.getUsername(), jwt), HttpStatus.OK);
        }catch (Exception e){
            log.error("Exception occurred while createAuthenticationToken ", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }
}

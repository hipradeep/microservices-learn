package com.hipradeep.authservice.controller;

import com.hipradeep.authservice.config.AppConfig;
import com.hipradeep.authservice.dto.*;
import com.hipradeep.authservice.entity.RefreshToken;
import com.hipradeep.authservice.entity.UserCredential;
import com.hipradeep.authservice.service.AuthService;
import com.hipradeep.authservice.service.RefreshTokenService;
import com.hipradeep.authservice.service.UserCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserCredentialService userCredentialService;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public String addNewUser(@RequestBody UserCredential user) {
        return service.saveUser(user);
    }

    @PostMapping("/token")
    public ResponseEntity<String> getToken(@RequestBody AuthRequest authRequest) {

        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authenticate.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.OK).body(service.generateToken(authRequest.getUsername()));
            } else {
                throw new RuntimeException("invalid access");
            }

        } catch (BadCredentialsException ex) {
            // Handle invalid credentials (wrong username or password) and return 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (Exception ex) {
            // Handle other exceptions and return 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during authentication");
        }

    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        service.validateToken(token);
        return "Token is valid";
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse<UserCredentialDto>> getLogin(@RequestBody AuthRequest authRequest) {

        try {
            // Authenticate the user
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            // If authentication is successful
            if (authenticate.isAuthenticated()) {
                // Load user details (e.g., from a database or in-memory)
                UserCredentialDto userDetails = userCredentialService.getUserCredential(authRequest.getUsername());

                // Create a map for user details to store in the JWT
                Map<String, Object> userClaims = new HashMap<>();
                userClaims.put(AppConfig.keyHeaderUsername, userDetails.getUsername());
                userClaims.put(AppConfig.keyHeaderEmail, userDetails.getEmail());
                userClaims.put(AppConfig.keyHeaderPassword, userDetails.getPassword());
                userClaims.put(AppConfig.keyHeaderName, userDetails.getUsername());

                // Generate JWT with user details
                String token = service.generateToken(authRequest.getUsername(), userClaims);

                RefreshToken refreshToken= refreshTokenService.generateRefreshToken(authRequest.getUsername());
                System.out.println(" RT token: " + refreshToken.getRefreshToken());

                 // Create the response object
                AuthResponse<UserCredentialDto> response = new AuthResponse<>(1, "Authentication successful", token, refreshToken.getRefreshToken(), userDetails);

                // Return the response wrapped in ResponseEntity with status 200 (OK)
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                // If authentication fails, return a response with status 401 (Unauthorized)
                return new ResponseEntity<>(new AuthResponse<>(0, "Invalid credentials", null,null, null), HttpStatus.UNAUTHORIZED);
            }
        } catch (BadCredentialsException ex) {
            // Handle invalid credentials (wrong username or password) and return 401 Unauthorized
            return new ResponseEntity<>(new AuthResponse<>(0, "Invalid username or password", null, null,null), HttpStatus.UNAUTHORIZED);

        } catch (Exception ex) {
            // Handle other exceptions and return 500 Internal Server Error
            return new ResponseEntity<>(new AuthResponse<>(0, "An error occurred during authentication", null,null, null), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshJwtToken(@RequestBody RefreshTokenRequest request) {
        // Verify the provided refresh token
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());

        // Retrieve the user associated with the refresh token
        String user = refreshToken.getUsername();

        // Load user details (e.g., from a database or in-memory)
        UserCredentialDto userDetails = userCredentialService.getUserCredential(user);

        // Create a map for user details to store in the JWT
        Map<String, Object> userClaims = new HashMap<>();
        userClaims.put(AppConfig.keyHeaderUsername, userDetails.getUsername());
        userClaims.put(AppConfig.keyHeaderEmail, userDetails.getEmail());
        userClaims.put(AppConfig.keyHeaderPassword, userDetails.getPassword());
        userClaims.put(AppConfig.keyHeaderName, userDetails.getUsername());

        // Generate a new JWT token for the user
        String newToken = this.service.generateToken(user, userClaims);


        // Return the response wrapped in ResponseEntity with status 200 (OK)
        return new ResponseEntity<>(new RefreshTokenResponse(newToken, user), HttpStatus.OK);
    }

}


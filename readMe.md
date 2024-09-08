![img.png](zebra/img_1.png)

# JWT Method Calling Flow

This document provides an overview of the method calling flow for handling JWT (JSON Web Tokens) in a Spring application.

## 1. **User Registration**

### 1.1. **`addNewUser()`**
- **Location**: `AuthController`
- **Description**: Registers a new user by saving user credentials.

## 2. **Token Generation**

### 2.1. **`getToken()`**
- **Location**: `AuthController`
- **Description**: Authenticates the user and generates a JWT token if authentication is successful.

### 2.2. **`generateToken()`**
- **Location**: `AuthService`
- **Description**: Generates a JWT token using the `JwtService`.

### 2.3. **`createToken()`**
- **Location**: `JwtService`
- **Description**: Creates a JWT token with specified claims and user details.

## 3. **Token Validation**

### 3.1. **`validateToken()`**
- **Location**: `AuthController`
- **Description**: Validates the JWT token by calling `AuthService`.

### 3.2. **`validateToken()`**
- **Location**: `AuthService`
- **Description**: Calls `JwtService` to validate the JWT token.

### 3.3. **`parseClaimsJws()`**
- **Location**: `JwtService`
- **Description**: Parses and validates the JWT token using the signing key.

## 4. **User Details Retrieval**

### 4.1. **`loadUserByUsername()`**
- **Location**: `CustomUserDetailsService`
- **Description**: Loads user details by username from `UserCredentialRepository`.

### 4.2. **`getUserCredential()`**
- **Location**: `UserCredentialService`
- **Description**: Retrieves user credentials by user ID.

## Summary

- **User Registration**: `addNewUser()` -> `saveUser()`
- **Token Generation**: `getToken()` -> `generateToken()` -> `createToken()`
- **Token Validation**: `validateToken()` (Controller) -> `validateToken()` (Service) -> `validateToken()` (JwtService)
- **Retrieve User Details**: `loadUserByUsername()` -> `getUserCredential()`
- **Security Configuration**: `securityFilterChain()`, `authenticationProvider()`, `authenticationManager()`
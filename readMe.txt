src/
└── main/
    └── java/
        └── com/hipradeep/userservice/

            ├── config/
            │   ├── SecurityConfig.java
            │   │   └── securityFilterChain(HttpSecurity http)
            │   │   └── authenticationManager(AuthenticationConfiguration config)
            │   │   └── passwordEncoder()
            │   └── JwtAuthenticationEntryPoint.java
            │       └── commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)

            ├── controller/
            │   └── AuthController.java
            │       └── login(AuthRequest request)
            │       └── createUser(User user)

            ├── dto/
            │   ├── AuthRequest.java
            │   │   └── username
            │   │   └── password
            │   └── AuthResponse.java
            │       └── token
            │       └── username

            ├── entity/
            │   └── User.java
            │       └── userId
            │       └── userName
            │       └── password
            │       └── (roles if needed as constant in service)

            ├── repository/
            │   └── UserRepository.java
            │       └── findByUserName(String username)

            ├── security/
            │   ├── JwtFilter.java
            │   │   └── doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            │   └── JwtUtil.java
            │       └── extractUsername(String token)
            │       └── generateToken(String username)
            │       └── validateToken(String token)

            ├── service/
            │   ├── UserDetailsServiceImpl.java
            │   │   └── loadUserByUsername(String username)
            │   └── UserService.java
            │       └── saveUser(User user)

            └── UserServicesApplication.java


-----------------------------------------------------------------------------

 🔐 Spring Security 6 JWT Implementation – Steps

1. Add Dependencies
   - spring-boot-starter-security
   - jjwt for JWT handling

2. Create `User` Entity
   - Include `username`, `password`, and other fields

3. Create `UserRepository`
   - `findByUserName(String username)`

4. Implement `UserDetailsServiceImpl`
   - Override `loadUserByUsername()`

5. Create `JwtUtil`
   - Methods: `generateToken()`, `extractUsername()`, `validateToken()`

6. Create `JwtFilter`
   - Intercept request, validate token, set `SecurityContext`

7. Create `JwtAuthenticationEntryPoint`
   - Handle unauthorized requests

8. Configure `SecurityConfig`
   - Use `SecurityFilterChain` bean
   - Add JWT filter & disable CSRF
   - Permit `/login`, secure others

9. Create `AuthController`
   - `/login` → Authenticate user, return JWT
   - `/users` → User registration (if needed)

10. DTOs
    - `AuthRequest` (username, password)
    - `AuthResponse` (JWT, etc.)


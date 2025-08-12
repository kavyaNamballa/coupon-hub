package com.demo.couponHub.controller;

import com.demo.couponHub.dtos.LoginResponse;
import com.demo.couponHub.dtos.RefreshTokenResponse;
import com.demo.couponHub.dtos.UserDTO;
import com.demo.couponHub.dtos.UserRequest;
import com.demo.couponHub.exceptions.NoSuchUserExists;
import com.demo.couponHub.exceptions.UserAlreadyExistsException;
import com.demo.couponHub.mappers.UserMapper;
import com.demo.couponHub.model.User;
import com.demo.couponHub.service.EmailService;
import com.demo.couponHub.service.JwtService;
import com.demo.couponHub.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Value("${security.jwt.refresh-token.cookie-name}")
    private String refreshTokenCookieName;

    @Value("${security.jwt.refresh-token.expiration-time}")
    private long refreshTokenExpiration;

    public UserController(
            UserService userService,
            EmailService emailService,
            UserMapper userMapper,
            JwtService jwtService,
            UserDetailsService userDetailsService) {
        this.userService = userService;
        this.emailService = emailService;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public UserDTO register(@RequestBody UserRequest request) throws UserAlreadyExistsException {
        User response = userService.registerUser(request);
//        try {
//            emailService.sendWelcomeEmail(request.getEmail(), request.getFirstName());
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
        return userMapper.mapDTO(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserRequest request, HttpServletResponse response) throws NoSuchUserExists {
        User authenticatedUser = userService.authenticate(request);

        // Generate access token and refresh token
        String accessToken = jwtService.generateToken(authenticatedUser);
        String refreshToken = jwtService.generateRefreshToken(authenticatedUser);

        // Set refresh token as HttpOnly cookie
        Cookie refreshTokenCookie = new Cookie(refreshTokenCookieName, refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // Set to true in production with HTTPS
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int) (refreshTokenExpiration / 1000));
        response.addCookie(refreshTokenCookie);

        LoginResponse loginResponse = new LoginResponse(accessToken, authenticatedUser);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Object> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (refreshTokenCookieName.equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token not found");
        }

        try {
            // Extract username from refresh token
            String userEmail = jwtService.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            // Validate refresh token
            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                // Generate new access token
                String newAccessToken = jwtService.generateToken(userDetails);

                // Optionally rotate refresh token
                String newRefreshToken = jwtService.generateRefreshToken(userDetails);

                // Set new refresh token as HttpOnly cookie
                Cookie newRefreshTokenCookie = new Cookie(refreshTokenCookieName, newRefreshToken);
                newRefreshTokenCookie.setHttpOnly(true);
                newRefreshTokenCookie.setSecure(false); // Set to true in production with HTTPS
                newRefreshTokenCookie.setPath("/");
                newRefreshTokenCookie.setMaxAge((int) (refreshTokenExpiration / 1000));
                response.addCookie(newRefreshTokenCookie);

                RefreshTokenResponse refreshResponse = new RefreshTokenResponse(newAccessToken);
                return ResponseEntity.ok(refreshResponse);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        // Clear refresh token cookie
        Cookie refreshTokenCookie = new Cookie(refreshTokenCookieName, "");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // Set to true in production with HTTPS
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok("Logout successful");
    }
}

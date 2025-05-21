package com.demo.couponHub.controller;


import com.demo.couponHub.dtos.UserDTO;
import com.demo.couponHub.dtos.UserRequest;
import com.demo.couponHub.exceptions.NoSuchUserExists;
import com.demo.couponHub.exceptions.UserAlreadyExistsException;
import com.demo.couponHub.mappers.UserMapper;
import com.demo.couponHub.model.User;
import com.demo.couponHub.service.EmailService;
import com.demo.couponHub.service.JwtService;
import com.demo.couponHub.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@CrossOrigin(origins = "http://dev.couponhub.com:9999")
public class UserController {
    private final UserService userService;
    private final EmailService emailService;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

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
    public ResponseEntity<String> login(@RequestBody UserRequest request) throws NoSuchUserExists {
        User authenticatedUser = userService.authenticate(request);
        String token = jwtService.generateToken(authenticatedUser);
        return ResponseEntity.ok(token);
    }
}

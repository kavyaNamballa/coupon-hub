package com.demo.couponHub.service;

import com.demo.couponHub.dtos.UserRequest;
import com.demo.couponHub.exceptions.NoSuchUserExists;
import com.demo.couponHub.exceptions.UserAlreadyExistsException;
import com.demo.couponHub.model.User;
import com.demo.couponHub.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User registerUser(UserRequest request) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.error("User already exists");
            throw new UserAlreadyExistsException("User already exists");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setMobileNumber(request.getMobileNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    public User authenticate(UserRequest input) throws NoSuchUserExists {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        return userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new NoSuchUserExists("No such user exists"));
    }
}


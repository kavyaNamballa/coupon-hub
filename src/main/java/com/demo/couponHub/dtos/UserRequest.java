package com.demo.couponHub.dtos;

import lombok.Data;

@Data
public class UserRequest {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String mobileNumber;
}

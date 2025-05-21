package com.demo.couponHub.dtos;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Timestamp createdDate;
    private Timestamp updatedDate;
}

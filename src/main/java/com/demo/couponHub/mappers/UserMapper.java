package com.demo.couponHub.mappers;


import com.demo.couponHub.dtos.UserDTO;
import com.demo.couponHub.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO mapDTO(User user);

    User mapUser(UserDTO userDTO);


}

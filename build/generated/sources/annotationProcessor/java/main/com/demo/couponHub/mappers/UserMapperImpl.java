package com.demo.couponHub.mappers;

import com.demo.couponHub.dtos.UserDTO;
import com.demo.couponHub.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-26T10:53:58+0530",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.12.1.jar, environment: Java 23.0.1 (Azul Systems, Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO mapDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( user.getId() );
        userDTO.setEmail( user.getEmail() );
        userDTO.setFirstName( user.getFirstName() );
        userDTO.setLastName( user.getLastName() );
        userDTO.setCreatedDate( user.getCreatedDate() );
        userDTO.setUpdatedDate( user.getUpdatedDate() );

        return userDTO;
    }

    @Override
    public User mapUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setCreatedDate( userDTO.getCreatedDate() );
        user.setUpdatedDate( userDTO.getUpdatedDate() );
        user.setId( userDTO.getId() );
        user.setFirstName( userDTO.getFirstName() );
        user.setLastName( userDTO.getLastName() );
        user.setEmail( userDTO.getEmail() );

        return user;
    }
}

package com.example.userapi.util;

import com.example.userapi.api.dto.UserDTO;
import com.example.userapi.model.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

public class UserUtil {
    public static User createNewUser() {

        return User.builder()
                .name("Franciele Ferreira")
                .cpf("897.408.970-02")
                .email("email@example.com")
                .age(25)
                .build();
    }

    public static UserDTO createNewUserDTO() {

        return UserDTO.builder()
                .name("Franciele Ferreira")
                .cpf("897.408.970-02")
                .email("email@example.com")
                .age(25)
                .build();
    }

    public static Example<User> createFilterUser(User filter) {

        return Example.of(filter,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
        );
    }
}

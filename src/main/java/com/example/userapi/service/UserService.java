package com.example.userapi.service;

import com.example.userapi.api.dto.UserDTO;
import com.example.userapi.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);

    Optional<User> findById(Long userId);

    void delete(User user);

    User update(UserDTO userDTO, User user);

    User replace(UserDTO userDTO, User user);

    List<User> find(User filter);
}

package com.mess.DTO.database;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDTO findUserByEmail(String email);
    List<UserDTO> findUserByEmails(String[] emails);
    Optional<UserDTO> findUserById(Long id);
    void saveUser(UserForm userForm);
    void saveAndFlush(UserDTO userDTO);
}
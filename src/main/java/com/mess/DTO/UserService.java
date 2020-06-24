package com.mess.DTO;

public interface UserService {
    UserDTO findUserByEmail(String email);
    void saveUser(UserForm userForm);
}
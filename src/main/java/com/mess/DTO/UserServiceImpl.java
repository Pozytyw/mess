package com.mess.DTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private Pbkdf2PasswordEncoder pbkdf2PasswordEncoder;

    public UserServiceImpl() {
    }

    @Override
    public UserDTO findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUser(UserForm userForm) {
        UserDTO user = new UserDTO();
        //new user set username
        user.setUsername(userForm.getUsername());
        //new user set email
        user.setEmail(userForm.getEmail());
        user.setPassword(pbkdf2PasswordEncoder.encode(userForm.getPassword()));
        //new user set active
        user.setActive(1);

        //set role
        RoleDTO userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<RoleDTO>(Arrays.asList(userRole)));

        //save user
        userRepository.save(user);
    }
}
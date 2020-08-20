package com.mess.DTO.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ConversationServiceImpl conversationService;

    @Autowired
    private Pbkdf2PasswordEncoder pbkdf2PasswordEncoder;

    public UserServiceImpl() {
    }

    @Override
    public UserDTO findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDTO> findUserByEmails(String[] emails) {
        return userRepository.findUserByEmails(emails);
    }

    @Override
    public Optional<UserDTO> findUserById(Long id){
        return userRepository.findById(id);
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

        //create self conversation
        ConversationDTO conversationDTO = new ConversationDTO();
        conversationDTO.setGroup(false);
        conversationDTO.setName("self");

        //set user
        HashSet<UserDTO> users = new HashSet<>();
        users.add(user);
        conversationDTO.setUsers(users);
        //save new conversation
        conversationService.saveAndFlush(conversationDTO);
    }

    @Override
    public void saveAndFlush(UserDTO userDTO) {
        userRepository.saveAndFlush(userDTO);
    }
}
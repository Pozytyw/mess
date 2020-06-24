package com.mess.controllers;

import com.mess.DTO.UserDTO;
import com.mess.DTO.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {
    @Autowired
    UserService userService;

    @GetMapping("/profile")
    public String showForm() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDTO user = userService.findUserByEmail(auth.getName());

        String returnString = "Username: " + user.getUsername();
        return returnString;
    }

}

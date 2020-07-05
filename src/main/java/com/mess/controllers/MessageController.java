package com.mess.controllers;

import com.mess.DTO.ConvRepository;
import com.mess.DTO.ConversationDTO;
import com.mess.DTO.UserDTO;
import com.mess.DTO.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class MessageController {
    @Autowired
    private ConvRepository convRepository;

    @Autowired
    private UserService userService;

    @RequestMapping(value={"/mess"}, method = RequestMethod.GET)
    public ModelAndView getMessenger() {
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDTO user = userService.findUserByEmail(auth.getName());

        List<ConversationDTO> conversationList = convRepository.getByUserID(user.getId());

        model.setViewName("message/message");
        model.addObject("conversations", conversationList);
        model.addObject("user", user);
        return model;
    }

}

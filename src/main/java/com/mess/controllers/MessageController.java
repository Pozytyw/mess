package com.mess.controllers;

import com.mess.DTO.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedList;
import java.util.List;

@RestController
public class MessageController {
    @Autowired
    private ConvRepository convRepository;

    @Autowired
    private UserService userService;

    @Autowired
    ReadMessageRepository readMessageRepository;

    @RequestMapping(value={"/"}, method = RequestMethod.GET)
    public ModelAndView getMessenger() {
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //get user ny auth name
        UserDTO user = userService.findUserByEmail(auth.getName());

        //get all user's conversations
        List<ConversationDTO> conversationList = convRepository.getByUserID(user.getId());

        List<List<ReadMessageDTO>> readList = new LinkedList<>();
        //for all conversation set last read message by users
        for(ConversationDTO conversationDTO : conversationList){
            readList.add(readMessageRepository.findByConv_id(conversationDTO.getId()));
        }

        //set view and put in to it conversation list
        model.addObject("conversations", conversationList);
        model.addObject("user", user);
        model.addObject("readList",readList);

        model.setViewName("message/message");
        return model;
    }

}

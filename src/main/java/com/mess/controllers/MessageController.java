package com.mess.controllers;

import com.mess.DTO.database.*;
import com.mess.storage_service.FileStorageService;
import com.mess.storage_service.FileStorageServiceImpl;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedList;
import java.util.List;

@RestController
public class MessageController {
    private static final int max_size = 1;//in mb

    @Autowired
    private ConvRepository convRepository;

    @Autowired
    private UserService userService;

    @Autowired
    ReadMessageRepository readMessageRepository;

    @Autowired
    FileStorageService fileStorageService;

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
            readList.add(readMessageRepository.findByConv_id(conversationDTO.getId(), user.getId()));
        }

        //set view and put in to it conversation list
        model.addObject("conversations", conversationList);
        model.addObject("user", user);
        model.addObject("readList",readList);

        model.setViewName("message/message");
        return model;
    }

    //update username, icon
    @RequestMapping(value = "/update_username", method = RequestMethod.POST)
    public ModelAndView updateUsername(@RequestParam("username") String username){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //get user by auth name
        UserDTO user = userService.findUserByEmail(auth.getName());
        //update username
        user.setUsername(username);
        userService.saveAndFlush(user);//save updated user

        return getMessenger();//return messenger side
    }

    @RequestMapping(value = "/update_icon", method = RequestMethod.POST)
    public ModelAndView updateIcon(@RequestParam("file") MultipartFile file){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //get user by auth name
        UserDTO user = userService.findUserByEmail(auth.getName());

        try {
            String newImg_src = fileStorageService.save(file, user.getUsername());
            user.setImg_src(newImg_src);
            userService.saveAndFlush(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return getMessenger();//return messenger side
    }
}

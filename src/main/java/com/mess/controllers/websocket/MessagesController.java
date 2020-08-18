package com.mess.controllers.websocket;

import com.mess.DTO.database.*;
import com.mess.DTO.ws.MessageWS;
import com.mess.websocket.MessageTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Calendar;
import java.util.List;

@Controller
public class MessagesController {
    @Autowired
    ConversationService conversationService;

    @Autowired
    UserService userService;

    @Autowired
    MessageTemplate messageTemplate;

    //send message to user
    @MessageMapping("/toUser")
    public void sendToUser(Principal principal, MessageWS messageWS) throws Exception{
        Long conversationId = Long.parseLong(messageWS.getConversationId());//get conversationId
        String name = principal.getName();//get name - users email
        UserDTO user = userService.findUserByEmail(name);

        //get all users from conversation
        List<String> emails = conversationService.getEmailsByConvId(conversationId);
        //get conversation
        ConversationDTO conversation = conversationService.findByID(conversationId).get();

        //when auth name doesn't include in conversation email list stop function
        if(!emails.contains(name))
            return;

        //get date
        Calendar date = Calendar.getInstance();

        //add message to database
        MessageDTO message= new MessageDTO();
        message.setMessage(messageWS.getMessage());
        message.setUser_id(user.getId());
        message.setSend_date(date);

        //set sender username in receive message
        messageWS.setSender(user.getEmail());

        //save message in conversation
        conversation = conversationService.saveNewMessage(conversation, message);

        //set message id
        messageWS.setMess_id(message.getId());
        messageTemplate.sendMessage(messageWS, emails);
    }
}

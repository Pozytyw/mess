package com.mess.controllers;

import com.mess.DTO.*;
import com.mess.websocket.Token;
import com.mess.websocket.UsersToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Calendar;
import java.util.List;

@Controller
public class SocketController {
    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Autowired
    ConvRepository convRepository;

    @Autowired
    UserService userService;

    @MessageMapping("/token")
    @SendToUser("/getter/token")
    public Token getToken(Principal principal) throws Exception {
        String name = principal.getName();//get name - users email
        String token = UsersToken.getToken(name);//get unique random token from UsersToken class
        UsersToken.usersToken.put(name, token);// put in map email -> token

        return new Token(name, token);//return token to create unique subscriptions
    }

    @MessageMapping("/toUser")
    public void sendToUser(Principal principal, MessageWS messageWS) throws Exception{
        Long conversationId = Long.parseLong(messageWS.getConversationId());//get conversationId
        String name = principal.getName();//get name - users email

        UserDTO user = userService.findUserByEmail(name);
        List<String> emails = convRepository.getEmailsByConvId(conversationId);
        //get conversation
        ConversationDTO conversation = convRepository.findById(conversationId).get();

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


        conversation.getMessages().add(message);
        convRepository.save(conversation);

        for(String email : emails) {
            if(email.equals(name))
                continue;//skip if email equal auth name
            String token = UsersToken.usersToken.get(email);
            if(token.isEmpty())
                continue;//ship if user isn't login
            String destination = "/getter/" + token + "/message";
            messagingTemplate.convertAndSend(destination, messageWS);
        }
    }

}
package com.mess.controllers;

import com.mess.DTO.database.*;
import com.mess.DTO.ws.MessageWS;
import com.mess.DTO.ws.Token;
import com.mess.DTO.ws.UserWS;
import com.mess.DTO.ws.UserWSRepository;
import com.mess.websocket.UsersToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class WebSocketController {
    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Autowired
    ConversationService conversationService;

    @Autowired
    UserService userService;

    @Autowired
    UserWSRepository userWSRepository;

    @MessageMapping("/token")
    @SendToUser("/getter/token")
    public Token getToken(Principal principal) throws Exception {
        String name = principal.getName();//get name - users email
        String token = UsersToken.getToken(name);//get unique random token from UsersToken class
        UsersToken.usersToken.put(name, token);// put in map email -> token

        return new Token(name, token);//return token to create unique subscriptions
    }

    @MessageMapping("/get_users")
    @SendToUser("/getter/get_users")
    public List<UserWS> getUsers(Principal principal, String queryParam) throws Exception {
        String name = principal.getName();//get name - users email
        UserDTO user = userService.findUserByEmail(name);

        List<UserWS> usersList = userWSRepository.findUsersByREGEX(queryParam, user.getId());
        return usersList;
    }

    @MessageMapping("/new_conv")
    public void newConv(Principal principal, Long user_id) throws Exception{
        //get names for new conversation
        String name = principal.getName();
        String name2 = userService.findUserById(user_id).get().getEmail();

        //create new conversation
        ConversationForm conversation = new ConversationForm();
        conversation.setName("talk");
        conversation.setGroup(false);

        conversationService.newConv(conversation, new String[]{name , name2});
    }

    @MessageMapping("/toUser")
    public void sendToUser(Principal principal, MessageWS messageWS) throws Exception{
        Long conversationId = Long.parseLong(messageWS.getConversationId());//get conversationId
        String name = principal.getName();//get name - users email

        UserDTO user = userService.findUserByEmail(name);
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
        messageWS.setSender(user.getUsername());

        conversation.getMessages().add(message);
        conversationService.save(conversation);

        for(String email : emails) {
            if(email.equals(name))
                continue;//skip if email equal auth name
            String token = UsersToken.usersToken.get(email);
            if(token == null)
                continue;//ship if user isn't login
            String destination = "/getter/message/" + token;
            messagingTemplate.convertAndSend(destination, messageWS);
        }
    }

}
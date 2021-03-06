package com.mess.websocket;

import com.mess.DTO.ws.MessageWS;
import com.mess.DTO.ws.ReadMessageWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

@Component("messageTemplate")
public class MessageTemplate {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public MessageTemplate() {
    }

    //send message to all users in list
    public void sendMessage(MessageWS messageWS, List<String> emails){
        //destination for sending message
        String destination = "/getter/message/";
        sendToUsersDestination(destination, emails, messageWS);
    }

    //send read message to users
    public void newLastRead(ReadMessageWS readMessageWS, List<String> emails){
        //destination for sending message
        String destination = "/getter/newLastRead/";
        sendToUsersDestination(destination, emails, readMessageWS);
    }

    //send to all users by destination
    private void sendToUsersDestination(String destination, List<String> emails, Object message){
        for(String email : emails) {
            //get users token
            String token = UsersToken.getToken(email);
            //ship if user isn't login
            if(token == null)
                continue;
            destination += token;
            messagingTemplate.convertAndSend(destination, message);
        }
    }
}



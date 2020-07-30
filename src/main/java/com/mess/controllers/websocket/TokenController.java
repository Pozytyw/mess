package com.mess.controllers.websocket;

import com.mess.DTO.ws.Token;
import com.mess.websocket.UsersToken;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class TokenController {

    //send token. Return response to user
    @MessageMapping("/token")
    @SendToUser("/getter/token")
    public Token getToken(Principal principal) throws Exception {
        //get name - users email
        String name = principal.getName();
        //get unique random token from UsersToken class
        //and put in map email -> token
        String token = UsersToken.newToken(name);
        //return token to create unique subscriptions
        return new Token(name, token);
    }
}

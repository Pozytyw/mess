package com.mess.controllers.websocket;

import com.mess.DTO.database.ConversationDTO;
import com.mess.DTO.database.ConversationService;
import com.mess.DTO.database.UserDTO;
import com.mess.DTO.database.UserService;
import com.mess.DTO.ws.FoundWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {
    @Autowired
    ConversationService conversationService;

    @Autowired
    UserService userService;

    //search for conversations. Return response to user
    @MessageMapping("/get_users")
    @SendToUser("/getter/get_users")
    public List<FoundWS> getUsers(Principal principal, String queryParam) throws Exception {
        //get name - users email
        String name = principal.getName();
        UserDTO user = userService.findUserByEmail(name);

        List<FoundWS> foundList = new ArrayList<>();

        List<ConversationDTO> conversationList = conversationService.findConversationByRegexp(queryParam, user.getId());
        conversationList.forEach(conversationDTO -> {
            // if conversation is not group
            if(!conversationDTO.isGroup()) {

                //conversations with name self is representation of users with we don't have talk conversation
                //we can use it for creating new conversation talk
                if(conversationDTO.getName().equals("self")){
                    UserDTO convUser = (UserDTO) conversationDTO.getUsers().toArray()[0];
                    foundList.add(new FoundWS(null, convUser.getId(), convUser.getUsername()));
                }
                //conversations with name talk. Conversation name is receiver username
                else{
                    foundList.add(new FoundWS(conversationDTO.getId(), null, conversationDTO.getName(user)));
                }
            //group conversations
            }else{
                foundList.add(new FoundWS(conversationDTO.getId(), null, conversationDTO.getName()));
            }
        });

        return foundList;
    }
}

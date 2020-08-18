package com.mess.controllers.websocket;

import com.mess.DTO.database.ConversationDTO;
import com.mess.DTO.database.ConversationService;
import com.mess.DTO.database.UserDTO;
import com.mess.DTO.database.UserService;
import com.mess.DTO.ws.GroupWS;
import com.mess.websocket.UsersToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
public class GroupController {
    @Autowired
    ConversationService conversationService;

    @Autowired
    UserService userService;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    //create new talk conversation
    @MessageMapping("/new_talk")
    public void newTalk(Principal principal, Long user_id) throws Exception{
        //get names for new conversation
        String email = principal.getName();
        String email2 = userService.findUserById(user_id).get().getEmail();
        UserDTO authUser = userService.findUserByEmail(email);
        UserDTO groupUser = userService.findUserByEmail(email2);

        //create new conversation
        ConversationDTO conversation = new ConversationDTO();
        conversation.setName("talk");
        conversation.setGroup(false);
        //set users
        Set<UserDTO> users = new HashSet<>();
        users.add(authUser);
        users.add(groupUser);
        conversation.setUsers(users);

        conversationService.saveAndFlush(conversation);

        for(UserDTO userDTO : conversation.getUsers()) {
            String token = UsersToken.getToken(userDTO.getEmail());
            //ship if user isn't login
            if(token == null)
                continue;
            String destination = "/getter/new_talk/" + token;

            //update name by user
            //if talk set name like caller username
            conversation.setName(conversation.getName(userDTO));

            messagingTemplate.convertAndSend(destination, conversation);
        }
    }

    //add user to existed group or if group doesn't exist create new group
    @MessageMapping("/add_group")
    public void newGroup(Principal principal, GroupWS groupWS) throws Exception{
        //get auth user from repository
        UserDTO authUser = userService.findUserByEmail(principal.getName());
        UserDTO groupUser;
        //conversation gave in message
        ConversationDTO conversationGroup = conversationService.findByID(groupWS.getConv_id()).get();

        //if conversation is group add new user to it
        if(conversationGroup.isGroup()){
            try {
                //get user from web socket message
                groupUser = userService.findUserById(groupWS.getUser_id()).get();
                //add new user to group
                conversationGroup.getUsers().add(groupUser);
                //save group
                conversationService.saveAndFlush(conversationGroup);

                //send to all users from group, if login, message add group with updated conversation
                for(UserDTO userDTO : conversationGroup.getUsers()) {
                    String token = UsersToken.getToken(userDTO.getEmail());
                    if(token == null)
                        continue;//ship if user isn't login
                    String destination = "/getter/add_group/" + token;
                    messagingTemplate.convertAndSend(destination, new GroupWS(conversationGroup.getId(), groupUser.getId(), conversationGroup.getName()));
                }
            }catch (NullPointerException e){}

        //create new group with authUser and groupUser
        }else{
            //if conversationGroup is self conversation return
            if(conversationGroup.getName().equals("self"))
                return;
            Long conv_id;
            ConversationDTO newGroupConversation = new ConversationDTO();
            //set name for group by conversation gave in message
            newGroupConversation.setName(conversationGroup.getUsers());
            //is now group
            newGroupConversation.setGroup(true);
            HashSet<UserDTO> users = new HashSet<>();
            for(UserDTO user : conversationGroup.getUsers()){
                users.add(user);
            }
            //set users
            newGroupConversation.setUsers(users);

            //save, create new conversation in repository
            conv_id = conversationService.saveAndFlush(newGroupConversation).getId();

            //send to all users from group, if login, message "add group" with conversation id
            for(UserDTO userDTO : newGroupConversation.getUsers()) {
                String token = UsersToken.getToken(userDTO.getEmail());
                //ship if user isn't login
                if(token == null)
                    continue;
                String destination = "/getter/add_group/" + token;
                messagingTemplate.convertAndSend(destination, new GroupWS(conv_id, null, newGroupConversation.getName()));
            }
        }
    }
}

package com.mess.controllers;

import com.mess.DTO.database.*;
import com.mess.DTO.ws.*;
import com.mess.websocket.UsersToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.*;

@Controller
public class WebSocketController {
    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Autowired
    ConversationService conversationService;

    @Autowired
    UserService userService;

    //send token
    @MessageMapping("/token")
    @SendToUser("/getter/token")
    public Token getToken(Principal principal) throws Exception {
        String name = principal.getName();//get name - users email
        String token = UsersToken.getToken(name);//get unique random token from UsersToken class
        UsersToken.usersToken.put(name, token);// put in map email -> token

        return new Token(name, token);//return token to create unique subscriptions
    }


    //search for conversations
    @MessageMapping("/get_users")
    @SendToUser("/getter/get_users")
    public List<FoundWS> getUsers(Principal principal, String queryParam) throws Exception {
        String name = principal.getName();//get name - users email
        UserDTO user = userService.findUserByEmail(name);

        List<FoundWS> foundList = new ArrayList<>();

        List<ConversationDTO> conversationList = conversationService.findConversationByRegexp(queryParam, user.getId());
        conversationList.forEach(conversationDTO -> {
            if(!conversationDTO.isGroup()) {// if conversation is not group

                //conversations with name self are used to create new talk conversation
                if(conversationDTO.getName().equals("self")){
                    UserDTO convUser = (UserDTO) conversationDTO.getUsers().toArray()[0];
                    foundList.add(new FoundWS(null, convUser.getId(), convUser.getUsername()));
                }
                else{//conversations with name talk. Only one for two users
                    foundList.add(new FoundWS(conversationDTO.getId(), null, conversationDTO.getName(user)));
                }

            }else{//group conversations
                foundList.add(new FoundWS(conversationDTO.getId(), null, conversationDTO.getName()));
            }
        });

        return foundList;
    }

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

        conversationService.save(conversation);

        for(UserDTO userDTO : conversation.getUsers()) {
            String token = UsersToken.usersToken.get(userDTO.getEmail());
            if(token == null)
                continue;//ship if user isn't login
            String destination = "/getter/new_talk/" + token;

            //update name by user
            conversation.setName(conversation.getName(userDTO));//if talk set name like caller username

            messagingTemplate.convertAndSend(destination, conversation);
        }
    }

    //add user to existed group or if group doesn't exist create new group
    @MessageMapping("/add_group")
    public void newGroup(Principal principal, GroupWS groupWS) throws Exception{
        UserDTO authUser = userService.findUserByEmail(principal.getName());//get auth user from repository
        UserDTO groupUser;

        ConversationDTO conversationGroup = conversationService.findByID(groupWS.getConv_id()).get();//conversation gave in message

        //if conversation is group add new user to it
        if(conversationGroup.isGroup()){
            try {
                groupUser = userService.findUserById(groupWS.getUser_id()).get();//get user from web socket message
                conversationGroup.getUsers().add(groupUser);//add new user to group
                conversationService.save(conversationGroup);//save group

                //send to all users from group, if login, message add group with updated conversation
                for(UserDTO userDTO : conversationGroup.getUsers()) {
                    String token = UsersToken.usersToken.get(userDTO.getEmail());
                    if(token == null)
                        continue;//ship if user isn't login
                    String destination = "/getter/add_group/" + token;
                    messagingTemplate.convertAndSend(destination, new GroupWS(conversationGroup.getId(), groupUser.getId(), conversationGroup.getName()));
                }
            }catch (NullPointerException e){}

        }else{//create new group with authUser and groupUser
            Long conv_id;
            ConversationDTO newGroupConversation = new ConversationDTO();
            newGroupConversation.setName(conversationGroup.getUsers());//set name for group by conversation gave in message
            newGroupConversation.setGroup(true);//is now group
            HashSet<UserDTO> users = new HashSet<>();
            for(UserDTO user : conversationGroup.getUsers()){
                users.add(user);
            }
            newGroupConversation.setUsers(users);//set users

            conv_id = conversationService.save(newGroupConversation);//save, create new conversation in repository

            //send to all users from group, if login, message "add group" with conversation id
            for(UserDTO userDTO : newGroupConversation.getUsers()) {
                String token = UsersToken.usersToken.get(userDTO.getEmail());
                if(token == null)
                    continue;//ship if user isn't login
                String destination = "/getter/add_group/" + token;
                messagingTemplate.convertAndSend(destination, new GroupWS(conv_id, null, newGroupConversation.getName()));
            }
        }

    }

    //send message to user
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
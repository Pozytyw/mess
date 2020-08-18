package com.mess.controllers.websocket;

import com.mess.DTO.database.*;
import com.mess.DTO.ws.ReadMessageWS;
import com.mess.websocket.MessageTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
public class ReadController {
    @Autowired
    ReadMessageRepository readMessageRepository;

    @Autowired
    ConversationService conversationService;

    @Autowired
    UserService userService;

    @Autowired
    MessageTemplate messageTemplate;

    @MessageMapping("/read")
    public void readByUser(Principal principal, ReadMessageWS readMessageWS) throws Exception {
        String name = principal.getName();//get name - users email
        UserDTO user = userService.findUserByEmail(name);

        //save new lastRead in database
        readMessageRepository.updateRead(readMessageWS.getMess_id(), readMessageWS.getReadDate(), readMessageWS.getConv_id(), user.getId());
        //get all emails from conversation
        List<String> emails = conversationService.getEmailsByConvId(readMessageWS.getConv_id());

        //remove reader from emails list
        emails.remove(name);

        //send message newLastRead to users in group/talk
        messageTemplate.newLastRead(readMessageWS, emails);
    }
}

package com.mess.controllers.websocket;

import com.mess.DTO.database.*;
import com.mess.DTO.ws.ReadMessageWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Optional;

@Controller
public class ReadController {
    @Autowired
    ReadMessageRepository readMessageRepository;

    @Autowired
    UserService userService;

    @MessageMapping("/read")
    public void readByUser(Principal principal, ReadMessageWS readMessageWS) throws Exception {
        String name = principal.getName();//get name - users email
        UserDTO user = userService.findUserByEmail(name);
        readMessageRepository.updateRead(readMessageWS.getMess_id(), readMessageWS.getReadDate(), readMessageWS.getConv_id(), user.getId());
//        ReadMessageDTO readMessageDTO = readMessageRepository.findById(readMessageWS.getConv_id(), user.getId());
//        readMessageDTO.setReadDate(readMessageWS.getReadDate());
//        readMessageDTO.setMess_id(readMessageWS.getMess_id());
//
//        readMessageRepository.saveAndFlush(readMessageDTO);
    }
}

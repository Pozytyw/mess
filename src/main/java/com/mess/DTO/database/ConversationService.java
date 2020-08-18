package com.mess.DTO.database;

import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationService {
    List<String> getEmailsByConvId(@Param("id") long id);
    List<ConversationDTO> getByUserID(@Param("id") long id);
    Optional<ConversationDTO> findByID(long id);
    ConversationDTO saveNewMessage(ConversationDTO conversationDTO, MessageDTO messageDTO);
    List<ConversationDTO> findConversationByRegexp(@Param("regexp") String regexp, @Param("user_id") Long user_id);
    ConversationDTO saveAndFlush(ConversationDTO conversationDTO);
    Optional<ConversationDTO> getTalk2Users(long id, long id1);
}

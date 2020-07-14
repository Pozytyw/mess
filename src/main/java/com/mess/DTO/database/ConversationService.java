package com.mess.DTO.database;

import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationService {
    List<String> getEmailsByConvId(@Param("id") long id);
    List<ConversationDTO> getByUserID(@Param("id") long id);
    Optional<ConversationDTO> findByID(long id);
    ConversationDTO newConv(ConversationForm conversationForm, String[] emails);
    Long save(ConversationDTO conversationDTO);
}

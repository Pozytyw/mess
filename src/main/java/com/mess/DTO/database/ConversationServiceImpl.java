package com.mess.DTO.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("conversationService")
public class ConversationServiceImpl implements ConversationService{
    @Autowired
    private ConvRepository convRepository;

    @Autowired UserRepository userRepository;

    @Autowired MessageRepository messageRepository;

    @Override
    public List<String> getEmailsByConvId(long id) {
        return convRepository.getEmailsByConvId(id);
    }

    @Override
    public List<ConversationDTO> getByUserID(long id) {
        return convRepository.getByUserID(id);
    }

    @Override
    public Optional<ConversationDTO> findByID(long id) {
        return convRepository.findById(id);
    }

    @Override
    public ConversationDTO saveNewMessage(ConversationDTO conversationDTO, MessageDTO messageDTO){
        //save message
        messageRepository.saveAndFlush(messageDTO);
        conversationDTO.getMessages().add(messageDTO);

        convRepository.saveAndFlush(conversationDTO);
        return conversationDTO;
    }

    @Override
    public List<ConversationDTO> findConversationByRegexp(@Param("regexp") String regexp, @Param("user_id") Long user_id){
        return convRepository.findConversationByRegexp(regexp, user_id);
    }

    @Override
    public ConversationDTO saveAndFlush(ConversationDTO conversationDTO) {
        return convRepository.saveAndFlush(conversationDTO);
    }

    @Override
    public Optional<ConversationDTO> getTalk2Users(long user_id1, long user_id2) {
        return convRepository.getTalk2Users(user_id1, user_id2);
    }
}

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
    public Long save(ConversationDTO conversationDTO){
        convRepository.save(conversationDTO);
        return conversationDTO.getId();
    }

    @Override
    public List<ConversationDTO> findConversationByRegexp(@Param("regexp") String regexp, @Param("user_id") Long user_id){
        return convRepository.findConversationByRegexp(regexp, user_id);
    }
}

package com.mess.DTO.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
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
    public void save(ConversationDTO conversationDTO){
        convRepository.save(conversationDTO);
    }

    @Override
    public ConversationDTO newConv(ConversationForm conversationForm, String[] emails) {

        ConversationDTO conversationDTO = new ConversationDTO();
        conversationDTO.setName(conversationForm.getName());
        conversationDTO.setGroup(conversationForm.isGroup());

        //set role
        List<UserDTO> users = userRepository.findUserByEmails(emails);
        conversationDTO.setUsers(new HashSet<UserDTO>(users));

        convRepository.saveAndFlush(conversationDTO);

        return conversationDTO;
    }
}

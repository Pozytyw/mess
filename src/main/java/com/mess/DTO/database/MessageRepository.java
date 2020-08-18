package com.mess.DTO.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("messageRepository")
public interface MessageRepository extends JpaRepository<MessageDTO, Long> {

}

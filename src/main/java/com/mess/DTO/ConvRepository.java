package com.mess.DTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("convRepository")
public interface ConvRepository extends JpaRepository<ConversationDTO, Long> {

    @Query(value = "select * from conversation inner join conv_user on conv_user.conv_id = conversation.id where conv_user.user_id = :id", nativeQuery = true)
    List<ConversationDTO> getByUserID(@Param("id") long id);

    @Query(value = "select users.email from users inner join conv_user on user_id = users.id " +
            "left join conversation on conv_user.conv_id = conversation.id where conv_user.conv_id = :id", nativeQuery = true)
    List<String> getEmailsByConvId(@Param("id") long id);

}

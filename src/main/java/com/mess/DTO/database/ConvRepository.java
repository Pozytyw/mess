package com.mess.DTO.database;

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

    @Query(value =
            "SELECT \n" +
            "    *\n" +
            "FROM\n" +
            "    conversation\n" +
            "        INNER JOIN\n" +
            "    conv_user ON conv_user.conv_id = conversation.id\n" +
            "        AND conv_user.conv_id IN (SELECT \n" +
            "            conversation.id\n" +
            "        FROM\n" +
            "            conversation\n" +
            "                INNER JOIN\n" +
            "            conv_user ON conv_user.conv_id = conversation.id\n" +
            "        WHERE\n" +
            "            conv_user.user_id = :id1)\n" +
            "WHERE\n" +
            "    conv_user.user_id = :id2 and conversation.is_group = 0;"
            , nativeQuery = true)
    List<ConversationDTO> getTalk2Users(@Param("id1") Long id1, @Param("id2") Long id2);

}
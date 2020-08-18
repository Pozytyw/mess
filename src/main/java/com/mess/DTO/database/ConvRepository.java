package com.mess.DTO.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    Optional<ConversationDTO> getTalk2Users(@Param("id1") Long id1, @Param("id2") Long id2);

    @Query(value =
            "SELECT \n" +
                    "    *\n" +
                    "FROM\n" +
                    "    conversation\n" +
                    "        LEFT JOIN\n" +
                    "    conv_user ON conv_user.conv_id = conversation.id\n" +
                    "        AND conv_user.conv_id IN (SELECT \n" +
                    "            id\n" +
                    "        FROM\n" +
                    "            conversation\n" +
                    "                INNER JOIN\n" +
                    "            conv_user ON conv_user.conv_id = conversation.id\n" +
                    "        WHERE\n" +
                    "            conv_user.user_id = :user_id\n" +
                    "                OR conversation.id IN (SELECT \n" +
                    "                    conversation.id\n" +
                    "                FROM\n" +
                    "                    conversation\n" +
                    "                        INNER JOIN\n" +
                    "                    conv_user ON conv_user.conv_id = conversation.id\n" +
                    "                        AND conv_user.user_id NOT IN (SELECT \n" +
                    "                            users.id\n" +
                    "                        FROM\n" +
                    "                            users\n" +
                    "                                INNER JOIN\n" +
                    "                            conv_user ON conv_user.user_id = users.id\n" +
                    "                                AND conv_user.conv_id IN (SELECT \n" +
                    "                                    conversation.id\n" +
                    "                                FROM\n" +
                    "                                    conversation\n" +
                    "                                        INNER JOIN\n" +
                    "                                    conv_user ON conv_user.conv_id = conversation.id\n" +
                    "                                WHERE\n" +
                    "                                    name LIKE 'talk' AND user_id = :user_id)\n" +
                    "                        WHERE\n" +
                    "                            users.id != :user_id)\n" +
                    "                WHERE\n" +
                    "                    name LIKE 'self'))\n" +
                    "        AND conv_user.user_id != :user_id\n" +
                    "        LEFT JOIN\n" +
                    "    users ON users.id = conv_user.user_id\n" +
                    "WHERE\n" +
                    "    CONCAT(conversation.name, users.username, email) REGEXP :regexp\n" +
                    "LIMIT 5;", nativeQuery = true)
    List<ConversationDTO> findConversationByRegexp(@Param("regexp") String regexp, @Param("user_id") Long user_id);

}
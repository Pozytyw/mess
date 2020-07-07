package com.mess.DTO.ws;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userWSRepository")
public interface UserWSRepository extends JpaRepository<UserWS, Long> {

    @Query(nativeQuery = true, value =
            "SELECT \n" +
            "    users.id AS id,\n" +
            "    users.username AS username,\n" +
            "    users.email,\n" +
            "    conversation.id AS conv_id\n" +
            "FROM\n" +
            "    users\n" +
            "        left JOIN\n" +
            "    conv_user ON conv_user.user_id = users.id\n" +
            "        AND conv_user.conv_id IN (SELECT \n" +
            "            id\n" +
            "        FROM\n" +
            "            conversation\n" +
            "                INNER JOIN\n" +
            "            conv_user ON conv_user.conv_id = conversation.id\n" +
            "        WHERE\n" +
            "            conv_user.user_id = ?2)\n" +
            "        LEFT JOIN\n" +
            "    conversation ON conversation.id = conv_user.conv_id\n" +
            "        AND conv_user.user_id != ?2\n" +
            "WHERE\n" +
            "    CONCAT(users.username, email) REGEXP ?1\n" +
            "        AND users.id != ?2\n" +
            "LIMIT 5;")
    List<UserWS> findUsersByREGEX(String regex, Long id);
}

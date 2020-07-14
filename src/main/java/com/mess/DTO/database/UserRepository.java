package com.mess.DTO.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<UserDTO, Long> {
    UserDTO findByEmail(String email);

    @Query(value = "select * from users where email in :emails", nativeQuery = true)
    List<UserDTO> findUserByEmails(@Param("emails") String[] emails);
}

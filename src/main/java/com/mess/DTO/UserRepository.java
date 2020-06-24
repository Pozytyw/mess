package com.mess.DTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<UserDTO, Long> {
    UserDTO findByEmail(String email);
}

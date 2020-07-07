package com.mess.DTO.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("roleRepository")
public interface RoleRepository extends JpaRepository<RoleDTO, Long> {
    RoleDTO findByRole(String role);
}

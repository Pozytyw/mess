package com.mess.DTO;

import javax.persistence.*;

@Entity
@Table(name="roles")
public class RoleDTO {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id")
    private long id;

    @Column(name="role")
    private String role;

    public RoleDTO() {}

    //getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

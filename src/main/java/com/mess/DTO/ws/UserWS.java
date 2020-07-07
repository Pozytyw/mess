package com.mess.DTO.ws;

import javax.persistence.*;

//This user DTO have conv_id common with auth user
@Entity
@Table(name = "users")
public class UserWS {

    @Id
    private long id;

    @Column(name="username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "conv_id")
    private Long conv_id;

    //constructors
    public UserWS() {}

    //getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getConv_id() {
        return conv_id;
    }

    public void setConv_id(Long conv_id) {
        this.conv_id = conv_id;
    }
}

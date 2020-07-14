package com.mess.DTO.ws;

public class FoundWS {
    private Long conv_id;
    private Long user_id;
    private String name;

    public FoundWS(Long conv_id, Long user_id, String name) {
        this.conv_id = conv_id;
        this.user_id = user_id;
        this.name = name;
    }

    public FoundWS() {
    }

    public Long getConv_id() {
        return conv_id;
    }

    public void setConv_id(Long conv_id) {
        this.conv_id = conv_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

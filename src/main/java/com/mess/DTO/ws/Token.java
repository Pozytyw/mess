package com.mess.DTO.ws;


public class Token {
    private String token;
    private String email;

    public Token() {

    }

    public Token(String email, String token) {
        this.token = token;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

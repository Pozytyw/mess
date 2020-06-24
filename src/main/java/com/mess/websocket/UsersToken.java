package com.mess.websocket;

import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.HashMap;
import java.util.Random;

public class UsersToken {
    public static HashMap<String, String> usersToken = new HashMap<>();
    private static Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder("Scap:Uku[d!l74UP", 185000, 255);

    public static String getToken(String name){
        String token = pbkdf2PasswordEncoder.encode(name + getRandomPepper());
        return token;
    }

    private static String getRandomPepper(){
        String charList = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@!,.<>*/-[]{}|:;~`";//char list
        Random random = new Random();
        String output = "";

        for (int i = 0; i < 15; i++) {
            output += charList.charAt(random.nextInt(charList.length())); //get random char from char list and add to output
        }

        return output;
    }
}

package com.mess.websocket;

import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import javax.validation.constraints.Null;
import java.util.HashMap;
import java.util.Random;

public class UsersToken {
    private static HashMap<String, Object[]> usersToken = new HashMap<>();
    private static Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder("Scap:Uku[d!l74UP", 2, 255);

    public static String newToken(String name){
        String newToken = pbkdf2PasswordEncoder.encode(name + getRandomPepper());
        Object[] token = new Object[2];
        //First elem in table is token string. Second is number of session with the same auth name
        //if second user login with the same auth name. Increment second element
        if(usersToken.containsKey(name)){
            token = usersToken.get(name);
            token[1] = (int)token[1] + 1;
        //if user is first with this auth name
        }else {
            token[1] = 1;
        }

        token[0] = newToken;
        usersToken.put(name, token);
        return newToken;
    }

    public static void removeToken(String name){
        Object[] token = usersToken.get(name);
        //if user is last with this auth name
        if((int)token[1] <= 1){
            usersToken.remove(name);
        //decrement second element of table
        }else{
            token[1] = (int)token[1] -1;
            usersToken.put(name,token);
        }

    }

    public static String getToken(String name){
        try {
            String token = (String) usersToken.get(name)[0];
            return token;
        }catch(NullPointerException e){
            return null;
        }
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

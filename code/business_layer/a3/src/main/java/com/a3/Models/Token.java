package com.a3.Models;

import com.a3.Interfaces.DBModel;

import java.security.SecureRandom;
import java.util.Base64;

public class Token implements DBModel {
    public String id;
    public Long userId;
    public String userType;

    public Token(){

    }

    public Token(String tokenId){
        id = tokenId;
    }

    public Token(Long uId, String uType){
        id = generateToken();
        userId = uId;
        userType = uType;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String generateToken(){
        SecureRandom secureRandom = new SecureRandom();
        Base64.Encoder base64Encoder = Base64.getUrlEncoder();

        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);

        return base64Encoder.encodeToString(randomBytes);
    }
}

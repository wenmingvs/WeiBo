package com.wenming.weiswift.app.common.entity;

/**
 * Created by wenmingvs on 16/5/18.
 */
public class Token {

    public String token;
    public String expiresIn;
    public String refresh_token;
    public String uid;

    public Token(String token, String expiresIn, String refresh_token, String uid) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.refresh_token = refresh_token;
        this.uid = uid;
    }
    
}

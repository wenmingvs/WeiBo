package com.wenming.weiswift.entity;

/**
 * Created by wenmingvs on 16/5/18.
 */
public class Token {

    String token;
    String expiresIn;
    String refresh_token;
    String uid;

    public Token(String token, String expiresIn, String refresh_token, String uid) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.refresh_token = refresh_token;
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


}

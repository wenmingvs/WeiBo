package com.wenming.weiswift.app.timeline.data.entity;

/**
 * Created by wenmingvs on 2017/8/21.
 */
public class ShortUrlEntity {
    String shortUrl;
    String longUrl;
    int type;
    String result;

    public ShortUrlEntity(String shortUrl, String longUrl, int type, String result) {
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
        this.type = type;
        this.result = result;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

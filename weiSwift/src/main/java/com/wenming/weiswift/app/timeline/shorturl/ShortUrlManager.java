package com.wenming.weiswift.app.timeline.shorturl;

import com.wenming.weiswift.app.timeline.data.entity.ShortUrlEntity;

import java.util.HashMap;

/**
 * Created by wenmingvs on 2017/8/21.
 */
public class ShortUrlManager {
    private static ShortUrlManager sInstance;
    HashMap<String, ShortUrlEntity> mHashMap = new HashMap<>();

    public static ShortUrlManager getInstance() {
        if (sInstance == null) {
            synchronized (ShortUrlManager.class) {
                if (sInstance == null) {
                    sInstance = new ShortUrlManager();
                }
            }
        }
        return sInstance;
    }

    public void addShortUrl(String shortURl, ShortUrlEntity shortUrlEntity) {
        mHashMap.put(shortURl, shortUrlEntity);
    }
}

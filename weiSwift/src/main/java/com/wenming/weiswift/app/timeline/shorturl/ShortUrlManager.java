package com.wenming.weiswift.app.timeline.shorturl;

import android.text.TextUtils;

import com.wenming.weiswift.app.timeline.data.entity.ShortUrlEntity;

import java.util.HashMap;

import static com.wenming.weiswift.app.timeline.shorturl.ShortUrlUtils.getShortUrl;

/**
 * Created by wenmingvs on 2017/8/21.
 */
public class ShortUrlManager {
    private static ShortUrlManager sInstance;
    private HashMap<String, ShortUrlEntity> mLongUrlHashMap = new HashMap<>();
    private HashMap<String, String> mVideoImgMap = new HashMap<>();

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
        mLongUrlHashMap.put(shortURl, shortUrlEntity);
    }

    public void addVideoImgUrl(String shortURl, String videoImgUrl) {
        mVideoImgMap.put(shortURl, videoImgUrl);
    }

    public ShortUrlEntity getLongUrlCache(String weiboContent) {
        ShortUrlEntity urlEntity = mLongUrlHashMap.get(getShortUrl(weiboContent));
        if (urlEntity != null) {
            return urlEntity;
        } else {
            return null;
        }
    }

    public String getVideoImgUrlCache(String shortUrl) {
        String videoImgUrl = mVideoImgMap.get(shortUrl);
        if (!TextUtils.isEmpty(videoImgUrl)) {
            return videoImgUrl;
        } else {
            return null;
        }
    }
}

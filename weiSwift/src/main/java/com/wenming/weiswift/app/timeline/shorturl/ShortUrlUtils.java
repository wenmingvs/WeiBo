package com.wenming.weiswift.app.timeline.shorturl;

import com.wenming.weiswift.app.timeline.constants.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wenmingvs on 2017/8/22.
 */

public class ShortUrlUtils {
    /**
     * 获取文本中的短链接
     *
     * @param weiboContent
     * @return
     */
    public static String getShortUrl(String weiboContent) {
        Pattern pattern = Pattern.compile(Constants.SHROT_URL);
        String content = null;
        Matcher matcher = pattern.matcher(weiboContent);
        if (matcher.find()) {
            content = matcher.group();
        }
        return content;
    }


    /**
     * 获取HTML中的视频图片地址
     *
     * @param htmlCode
     * @return
     */
    public static String getVideoImgUrl(String htmlCode) {
        Pattern pattern = Pattern.compile(Constants.HTML_VIDEO_IMG);
        String videoUrl = null;
        Matcher matcher = pattern.matcher(htmlCode);
        if (matcher.find()) {
            videoUrl = matcher.group();
        }
        return videoUrl;
    }
}

package com.wenming.weiswift.app.common;

import android.text.TextUtils;

import com.wenming.weiswift.app.common.entity.Comment;
import com.wenming.weiswift.app.common.entity.Status;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 在数据与UI控件绑定前，对数据进行进一步处理，使用正则表达式或者其他方式提取出里面的信息
 * Created by wenmingvs on 16/6/10.
 */
public class FillContentHelper {

    /**
     * 提取Status中的微博来源的关键字字段
     *
     * @param status
     */
    public static void setSource(Status status) {
        //如果字段是空的，就没必要在接着下去了。服务器确实有时候会返回空
        if (TextUtils.isEmpty(status.source)) {
            return;
        }
        //如果已经提取过关键字，就不需要再处理
        if (!status.source.contains("</a>")) {
            return;
        }

        Pattern mpattern = Pattern.compile("<(.*?)>(.*?)</a>");
        Matcher mmatcher = mpattern.matcher(status.source);

        if (mmatcher.find()) {
            String weibocomefrom = mmatcher.group(2);
            status.source = weibocomefrom;
        }
    }

    /**
     * 提取Status中的微博来源的关键字字段
     *
     * @param comment
     */
    public static void setSource(Comment comment) {
        //如果字段是空的，就没必要在接着下去了。服务器确实有时候会返回空
        if (TextUtils.isEmpty(comment.source)) {
            return;
        }
        //如果已经提取过关键字，就不需要再处理
        if (!comment.source.contains("</a>")) {
            return;
        }

        Pattern mpattern = Pattern.compile("<(.*?)>(.*?)</a>");
        Matcher mmatcher = mpattern.matcher(comment.source);

        if (mmatcher.find()) {
            String weibocomefrom = mmatcher.group(2);
            comment.source = weibocomefrom;
        }
        if (comment.status != null) {
            setSource(comment.status);
        }

    }

    /**
     * 设置三种类型图片的url地址
     *
     * @param status
     */
    public static void setImgUrl(Status status) {
        //如果微博存在图片
        if (status.pic_urls != null && status.pic_urls.size() > 0) {
            //如果本地私有字段已经被处理过了，就不需要再处理
            if (status.bmiddle_pic_urls.size() > 0) {
                return;
            }
            for (Status.PicUrlsBean picUrlsBean : status.pic_urls) {
                status.thumbnail_pic_urls.add(picUrlsBean.thumbnail_pic);
                status.bmiddle_pic_urls.add(picUrlsBean.thumbnail_pic.replace("thumbnail", "bmiddle"));
                if (!picUrlsBean.thumbnail_pic.endsWith(".gif")) {
                    status.origin_pic_urls.add(picUrlsBean.thumbnail_pic.replace("thumbnail", "large"));
                } else {
                    status.origin_pic_urls.add(picUrlsBean.thumbnail_pic.replace("thumbnail", "bmiddle"));
                }
            }
        }


    }

    /**
     * 如果这条微博只包含一张图，就给这张图片设置一个随机的尺寸，
     * 注意：
     * 这里使用status中的mlevel这个废弃的字段来作为变量
     *
     * @param status
     */
    public static void setSingleImgSizeType(Status status) {
        if (status.pic_urls != null && status.pic_urls.size() == 1) {
            //从本地加载缓存，如果已经被赋值,就不再继续取随机数
            if (!TextUtils.isEmpty(status.singleImgSizeType)) {
                return;
            }
            status.singleImgSizeType = String.valueOf(new Random().nextInt(3) + 1);
        }
    }




}



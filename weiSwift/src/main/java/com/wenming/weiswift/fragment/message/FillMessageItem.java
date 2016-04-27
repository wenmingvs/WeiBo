package com.wenming.weiswift.fragment.message;

import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.Status;
import com.wenming.weiswift.fragment.home.util.FillWeiBoItem;

/**
 * Created by wenmingvs on 16/4/27.
 */
public class FillMessageItem {

    public static void FillCenterContent(Status status, ImageView profile_img, TextView profile_name, TextView content) {
        if (status.origin_pic_urls == null || status.origin_pic_urls.size() == 0) {
            ImageLoader.getInstance().displayImage(status.user.avatar_hd, profile_img);
        } else {
            ImageLoader.getInstance().displayImage(status.origin_pic_urls.get(0), profile_img);
        }
        profile_name.setText(status.user.name);
        content.setText(status.text);
    }


    /**
     * 填充顶部微博用户信息数据
     *
     * @param comment
     * @param profile_img
     * @param profile_verified
     * @param profile_name
     * @param profile_time
     * @param weibo_comefrom
     */
    public static void fillTitleBar(Comment comment, ImageView profile_img, ImageView profile_verified, TextView profile_name, TextView profile_time, TextView weibo_comefrom) {
        FillWeiBoItem.fillProfileImg(comment.user, profile_img, profile_verified);
        profile_name.setText(comment.user.name);
        FillWeiBoItem.setWeiBoTime(profile_time, comment.created_at);
        FillWeiBoItem.setWeiBoComeFrom(weibo_comefrom, comment.source);
    }

}

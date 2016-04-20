package com.wenming.weiswift.fragment.home.util;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.sina.weibo.sdk.openapi.models.Status;
import com.wenming.weiswift.R;
import com.wenming.weiswift.common.util.DateUtils;
import com.wenming.weiswift.fragment.home.imagelist.ImageAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wenmingvs on 16/4/21.
 */
public class FillWeiBoItem {

    private static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.avator_default)
            .showImageForEmptyUri(R.drawable.avator_default)
            .showImageOnFail(R.drawable.avator_default)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .displayer(new CircleBitmapDisplayer(14671839, 1))
            .build();
    ;


    /**
     * 填充顶部微博用户信息数据
     *
     * @param status
     * @param profile_img
     * @param profile_name
     * @param profile_time
     * @param weibo_comefrom
     */
    public static void fillTitleBar(Status status, ImageView profile_img, TextView profile_name, TextView profile_time, TextView weibo_comefrom) {
        ImageLoader.getInstance().displayImage(status.user.avatar_hd, profile_img, options);
        profile_name.setText(status.user.name);
        setWeiBoTime(profile_time, status.created_at);
        setWeiBoComeFrom(weibo_comefrom, status.source);
    }

    private static void setWeiBoTime(TextView textView, String created_at) {
        Date data = DateUtils.parseDate(created_at, DateUtils.WeiBo_ITEM_DATE_FORMAT);
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");
        String time = df.format(data);
        textView.setText(time + "   ");
    }

    private static void setWeiBoComeFrom(TextView textView, String content) {
        if (content != null && content.length() > 0) {
            textView.setText("来自 " + content);
        } else {
            textView.setText("");
        }
    }

    /**
     * 填充微博转发，评论，赞的数量
     *
     * @param status
     * @param comment
     * @param redirect
     * @param feedlike
     */
    public static void fillButtonBar(Status status, TextView comment, TextView redirect, TextView feedlike) {
        comment.setText(status.comments_count + "");
        redirect.setText(status.reposts_count + "");
        feedlike.setText(status.attitudes_count + "");

    }

    /**
     * 填充微博文字内容
     */
    public static void fillWeiBoContent(Status status, Context context, TextView weibo_content) {
        weibo_content.setText(WeiBoContentTextUtil.getWeiBoContent(status.text, context, weibo_content));
    }

    /**
     * 填充微博图片列表,包括原创微博和转发微博中的图片都可以使用
     */
    public static void fillWeiBoImgList(Status status, Context context, RecyclerView imageList) {
        imageList.setVisibility(View.GONE);
        imageList.setVisibility(View.VISIBLE);
        ArrayList<String> imageDatas = status.origin_pic_urls;
        GridLayoutManager gridLayoutManager = initGridLayoutManager(imageDatas, context);
        ImageAdapter imageAdapter = new ImageAdapter(imageDatas, context);
        imageList.setHasFixedSize(true);
        imageList.setAdapter(imageAdapter);
        imageList.setLayoutManager(gridLayoutManager);
        imageAdapter.setData(imageDatas);
        if (imageDatas == null || imageDatas.size() == 0) {
            imageList.setVisibility(View.GONE);
        }
    }

    /**
     * 根据图片数量，初始化GridLayoutManager，并且设置列数，
     * 当图片 = 1 的时候，显示1列
     * 当图片<=4张的时候，显示2列
     * 当图片>4 张的时候，显示3列
     *
     * @return
     */
    private static GridLayoutManager initGridLayoutManager(ArrayList<String> imageDatas, Context context) {
        GridLayoutManager gridLayoutManager;
        if (imageDatas != null) {
            switch (imageDatas.size()) {
                case 1:
                    gridLayoutManager = new GridLayoutManager(context, 1);
                    break;
                case 2:
                    gridLayoutManager = new GridLayoutManager(context, 2);
                    break;
                case 3:
                    gridLayoutManager = new GridLayoutManager(context, 3);
                    break;
                case 4:
                    gridLayoutManager = new GridLayoutManager(context, 2);
                    break;
                default:
                    gridLayoutManager = new GridLayoutManager(context, 3);
                    break;
            }
        } else {
            gridLayoutManager = new GridLayoutManager(context, 3);
        }

        return gridLayoutManager;
    }

    /**
     * 转发的文字
     */
    public static void fillRetweetContent(Status status, Context context, TextView origin_nameAndcontent) {
        StringBuffer retweetcontent_buffer = new StringBuffer();
        retweetcontent_buffer.setLength(0);
        retweetcontent_buffer.append("@");
        retweetcontent_buffer.append(status.retweeted_status.user.name + " :  ");
        retweetcontent_buffer.append(status.retweeted_status.text);
        origin_nameAndcontent.setText(WeiBoContentTextUtil.getWeiBoContent(retweetcontent_buffer.toString(), context, origin_nameAndcontent));
    }

}

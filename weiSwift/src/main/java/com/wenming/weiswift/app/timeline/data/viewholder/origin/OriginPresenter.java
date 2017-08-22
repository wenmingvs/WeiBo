package com.wenming.weiswift.app.timeline.data.viewholder.origin;

import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.timeline.constants.Constants;
import com.wenming.weiswift.app.timeline.data.entity.ShortUrlEntity;
import com.wenming.weiswift.app.timeline.data.viewholder.base.BaseTimeLinePresenter;
import com.wenming.weiswift.app.timeline.net.TimeLineHttpHepler;
import com.wenming.weiswift.app.timeline.shorturl.ShortUrlManager;
import com.wenming.weiswift.app.timeline.shorturl.ShortUrlUtils;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 2017/7/25.
 */

public class OriginPresenter extends BaseTimeLinePresenter implements OriginContract.Presenter {
    private OriginContract.View mView;
    private Status mDataModel;

    public OriginPresenter(OriginContract.View view, Status dataModel) {
        super(view, dataModel);
        this.mView = view;
        this.mDataModel = dataModel;
        this.mView.setPresenter(this);
    }

    @Override
    protected void updateView() {
        super.updateView();
        setText();
        setImgList(mDataModel);
    }

    protected void setImgList(Status status) {
        ArrayList<String> imgList = status.bmiddle_pic_urls;
        final ShortUrlEntity urlEntity = ShortUrlManager.getInstance().getLongUrlByWeiBoContent(status.text);
        //存在图片
        if (imgList != null && imgList.size() > 0) {
            mView.setImgListVisible(true);
            mView.setImgListContent(mDataModel);
        }
        //1. 文本存在长链接缓存
        //2. 长连接是秒拍视频或者微博视频
        else if (urlEntity != null && (urlEntity.getLongUrl().contains(Constants.HTML_MIAOPAI_KEYWORD) || urlEntity.getLongUrl().contains(Constants.HTML_WEIBO_VIDEO_KEYWORD))) {
            //尝试获取此长连接对应的视频的图片文件
            String videoImgUrl = ShortUrlManager.getInstance().getVideoImgUrlCache(urlEntity.getShortUrl());
            if (!TextUtils.isEmpty(videoImgUrl)) {
                mView.setImgListVisible(true);
                mView.setVideoImg(videoImgUrl);
            }
            //无缓存，进行网络请求，获取此长连接对应的视频图片
            else {
                TimeLineHttpHepler.getHTMLCode(urlEntity.getLongUrl(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String htmlVideoImgUrl;
                        //使用秒拍的正则表达式
                        if (urlEntity.getLongUrl().contains(Constants.HTML_MIAOPAI_KEYWORD)) {
                            htmlVideoImgUrl = ShortUrlUtils.getVideoImgUrl(response, Constants.HTML_MIAOPAI_VIDEO_IMG);
                        }
                        //使用微博视频的正则表达式
                        else {
                            htmlVideoImgUrl = ShortUrlUtils.getVideoImgUrl(response, Constants.HTML_WEIBO_VIDEO_IMG);
                        }
                        ShortUrlManager.getInstance().addVideoImgUrl(urlEntity.getShortUrl(), htmlVideoImgUrl);
                        mView.setImgListVisible(true);
                        mView.setVideoImg(htmlVideoImgUrl);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
            }
        }
        //不存在图片
        else {
            mView.setImgListVisible(false);
        }
    }

    private void setText() {
        mView.setImgListContent(mDataModel.text);
    }
}

package com.wenming.weiswift.app.timeline.data.viewholder.origin;

import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wenming.weiswift.app.common.entity.Status;
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
        setImgList();
    }

    private void setImgList() {
        ArrayList<String> imgList = mDataModel.bmiddle_pic_urls;
        final ShortUrlEntity urlEntity = ShortUrlManager.getInstance().getLongUrlCache(mDataModel.text);
        //存在图片
        if (imgList != null && imgList.size() > 0) {
            mView.setImgListVisible(true);
            mView.setImgListContent(mDataModel);
        }
        //文本存在长连接而且长连接是秒拍视频
        else if (urlEntity != null && urlEntity.getLongUrl().contains("miaopai.com")) {
            //尝试获取此长连接对应的视频的图片文件
            String videoImgUrl = ShortUrlManager.getInstance().getVideoImgUrlCache(urlEntity.getShortUrl());
            if (!TextUtils.isEmpty(videoImgUrl)) {
                mView.setVideoImg(videoImgUrl);
            }
            //无缓存，进行网络请求，获取此长连接对应的视频图片
            else {
                TimeLineHttpHepler.getHTMLCode(urlEntity.getLongUrl(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String htmlVideoImgUrl = ShortUrlUtils.getVideoImgUrl(response);
                        mView.setVideoImg(htmlVideoImgUrl);
                        ShortUrlManager.getInstance().addVideoImgUrl(urlEntity.getShortUrl(), htmlVideoImgUrl);
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

package com.wenming.weiswift.mvp.model.imp;

import android.content.Context;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.wenming.weiswift.api.StatusesAPI;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.entity.list.StatusList;
import com.wenming.weiswift.mvp.model.HotWeiBoModel;
import com.wenming.weiswift.ui.common.NewFeature;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.common.login.Constants;
import com.wenming.weiswift.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/16.
 */
public class HotWeiBoModelImp implements HotWeiBoModel {

    private ArrayList<Status> mStatusList = new ArrayList<>();
    private static final int HOT_COMMENTS_DAILY = 0;
    private static final int HOT_COMMENTS_WEEKLY = 1;
    private static final int HOT_RETWEETS_DAILY = 2;
    private static final int HOT_RETWEETS_WEEKLY = 3;

    @Override
    public void getLatestComment(final Context context, final OnDataFinishedListener onDataFinishedListener) {

        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));

        if (mStatusList != null) {
            mStatusList.clear();
        }
        getPublicWeiBo(context, onDataFinishedListener);
    }


    @Override
    public void getNextPageComment(final Context context, final OnDataFinishedListener onDataFinishedListener) {
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        getPublicWeiBo(context, onDataFinishedListener);
    }

    private void getPublicWeiBo(final Context context, final OnDataFinishedListener onDataFinishedListener) {
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mStatusesAPI.publicTimeline(NewFeature.LOAD_PUBLICWEIBO_ITEM, 1, false, new RequestListener() {
            @Override
            public void onComplete(String response) {
                ArrayList<Status> temp = StatusList.parse(response).statusList;
                if (temp != null && temp.size() > 0) {
                    mStatusList.addAll(temp);
                    onDataFinishedListener.onDataFinish(mStatusList);
                } else {
                    ToastUtil.showShort(context, "没有更新的内容了");
                    onDataFinishedListener.noMoreDate();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(context, e.getMessage());
                onDataFinishedListener.onError(e.getMessage());
            }
        });
    }


}

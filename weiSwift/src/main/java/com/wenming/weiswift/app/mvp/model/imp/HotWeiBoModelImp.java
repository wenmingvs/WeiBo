package com.wenming.weiswift.app.mvp.model.imp;

import android.content.Context;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.wenming.weiswift.app.api.StatusesAPI;
import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.common.entity.list.StatusList;
import com.wenming.weiswift.app.mvp.model.HotWeiBoModel;
import com.wenming.weiswift.app.common.NewFeature;
import com.wenming.weiswift.app.login.AccessTokenKeeper;
import com.wenming.weiswift.app.login.Constants;
import com.wenming.weiswift.utils.ToastUtil;
import com.wenming.weiswift.widget.toast.LoadedToast;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/16.
 */
public class HotWeiBoModelImp implements HotWeiBoModel {

    private ArrayList<Status> mStatusList = new ArrayList<>();

    @Override
    public void getHotWeiBo(final Context context, final OnDataFinishedListener onDataFinishedListener) {
        getPublicWeiBo(context, onDataFinishedListener);
    }


    @Override
    public void getHotWeiBoNextPage(Context context, OnDataFinishedListener onDataFinishedListener) {
        getPublicWeiBo(context, onDataFinishedListener);
    }

    private void getPublicWeiBo(final Context context, final OnDataFinishedListener onDataFinishedListener) {
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mStatusesAPI.publicTimeline(NewFeature.LOAD_PUBLICWEIBO_ITEM, 1, false, new RequestListener() {
            @Override
            public void onComplete(String response) {
                ArrayList<Status> temp = StatusList.parse(response).statuses;
                LoadedToast.showToast(context, temp.size() + "条新微博");
                if (temp != null && temp.size() > 0) {
                    mStatusList.addAll(temp);
                    onDataFinishedListener.onDataFinish(mStatusList);
                } else {
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

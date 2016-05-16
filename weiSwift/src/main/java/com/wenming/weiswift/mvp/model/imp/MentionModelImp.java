package com.wenming.weiswift.mvp.model.imp;

import android.content.Context;
import android.text.TextUtils;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.wenming.weiswift.api.StatusesAPI;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.entity.list.StatusList;
import com.wenming.weiswift.mvp.model.MentionModel;
import com.wenming.weiswift.ui.common.NewFeature;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.common.login.Constants;
import com.wenming.weiswift.utils.SDCardUtil;
import com.wenming.weiswift.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/15.
 */
public class MentionModelImp implements MentionModel {

    private ArrayList<Status> mMentionList = new ArrayList<>();
    private boolean mRefrshAllData = true;

    @Override
    public void mentions(final Context context, final OnDataFinishedListener onDataFinishedListener) {
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        long sinceId = 0;
        if (mMentionList.size() > 0) {
            sinceId = Long.valueOf(mMentionList.get(0).id);
        }
        if (mRefrshAllData) {
            sinceId = 0;
        }
        mStatusesAPI.mentions(sinceId, 0, NewFeature.GET_MENTION_ITEM, 1, 0, 0, 0, true, new RequestListener() {
            @Override
            public void onComplete(String response) {
                ArrayList<Status> temp = StatusList.parse(response).statusList;
                if (temp != null && temp.size() > 0) {
                    if (mMentionList != null) {
                        mMentionList.clear();
                    }
                    mentionCacheSave(context, response);
                    mMentionList = temp;
                    onDataFinishedListener.onDataFinish(mMentionList);
                } else {
                    ToastUtil.showShort(context, "没有更新的内容了");
                    onDataFinishedListener.noMoreDate();
                }
                mRefrshAllData = false;
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(context, e.getMessage());
                mentionCacheLoad(context, onDataFinishedListener);
            }
        });
    }

    @Override
    public void mentionsNextPage(final Context context, final OnDataFinishedListener onDataFinishedListener) {
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        final String maxId = mMentionList.get(mMentionList.size() - 1).id;
        mStatusesAPI.mentions(0, Long.valueOf(maxId), NewFeature.LOADMORE_MENTION_ITEM, 1, 0, 0, 0, true, new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {
                    ArrayList<Status> temp = StatusList.parse(response).statusList;
                    if (temp.size() == 0 || (temp != null && temp.size() == 1 && temp.get(0).id.equals(String.valueOf(maxId)))) {
                        onDataFinishedListener.noMoreDate();
                    } else if (temp.size() > 1) {
                        temp.remove(0);
                        mMentionList.addAll(temp);
                        onDataFinishedListener.onDataFinish(mMentionList);
                    }
                } else {
                    ToastUtil.showShort(context, "内容已经加载完了");
                    onDataFinishedListener.noMoreDate();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                onDataFinishedListener.onError(e.getMessage());
            }
        });

    }

    @Override
    public void mentionCacheSave(Context context, String response) {
        if (NewFeature.CACHE_MESSAGE_MENTION) {
            SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "message_mention" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt", response);
        }
    }

    @Override
    public void mentionCacheLoad(Context context, OnDataFinishedListener onDataFinishedListener) {
        if (NewFeature.CACHE_MESSAGE_MENTION) {
            String response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "message_mention" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
            if (response != null) {
                mMentionList = StatusList.parse(response).statusList;
                onDataFinishedListener.onDataFinish(mMentionList);
            }
        }

    }

}

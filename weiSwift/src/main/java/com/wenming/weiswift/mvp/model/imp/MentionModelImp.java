package com.wenming.weiswift.mvp.model.imp;

import android.content.Context;
import android.text.TextUtils;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
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

    private StatusesAPI mStatusesAPI;
    private ArrayList<Status> mMentionList;
    private long mFirstWeiboId;
    private long mLastWeiboId;
    private boolean mRefrshAllData = true;
    private boolean mNoMoreData;


    @Override
    public void getLatestMention(final Context context, final OnDataFinishedListener onDataFinishedListener) {
        if (mStatusesAPI == null) {
            mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        }

        if (mRefrshAllData) {
            mFirstWeiboId = 0;
        }
        //ToastUtil.showShort(context, mFirstWeiboId + "");

        mStatusesAPI.mentions(mFirstWeiboId, 0, NewFeature.GET_MENTION_ITEM, 1, 0, 0, 0, true, new RequestListener() {
            @Override
            public void onComplete(String response) {
                ArrayList<Status> temp = StatusList.parse(response).statusList;
                if (temp != null && temp.size() > 0) {
                    if (mMentionList != null) {
                        mMentionList.clear();
                    }
                    saveMentionCache(context, response);
                    mMentionList = temp;
                    updateId();
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
                onDataFinishedListener.onDataFinish(getMentionCache(context));
            }
        });
    }

    @Override
    public void getNextPageMention(final Context context, final OnDataFinishedListener onDataFinishedListener) {
        //ToastUtil.showShort(context, mLastWeiboId + "");
        if (mStatusesAPI == null) {
            mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        }

        if (mNoMoreData == true) {
            return;
        }

        mStatusesAPI.mentions(0, mLastWeiboId, NewFeature.LOADMORE_MENTION_ITEM, 1, 0, 0, 0, true, new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {
                    ArrayList<Status> temp = StatusList.parse(response).statusList;
                    if (temp.size() == 0 || (temp != null && temp.size() == 1 && temp.get(0).id.equals(String.valueOf(mLastWeiboId)))) {
                        mNoMoreData = true;
                        onDataFinishedListener.noMoreDate();
                    } else if (temp.size() > 1) {
                        temp.remove(0);
                        mMentionList.addAll(temp);
                        updateId();
                        onDataFinishedListener.onDataFinish(mMentionList);
                    }
                } else {
                    ToastUtil.showShort(context, "内容已经加载完了");
                    onDataFinishedListener.noMoreDate();
                    mNoMoreData = true;
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                onDataFinishedListener.onError(e.getMessage());
            }
        });

    }

    @Override
    public void saveMentionCache(Context context, String response) {
        if (NewFeature.CACHE_MESSAGE_MENTION) {
            SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "message_mention" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt", response);
        }
    }

    @Override
    public ArrayList<Status> getMentionCache(Context context) {
        if (NewFeature.CACHE_MESSAGE_MENTION) {
            String response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "message_mention" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
            if (response != null) {
                mMentionList = StatusList.parse(response).statusList;
                updateId();
                return mMentionList;
            }
        }
        return null;
    }

    public void updateId() {
        mFirstWeiboId = Long.valueOf(mMentionList.get(0).id);
        mLastWeiboId = Long.valueOf(mMentionList.get(mMentionList.size() - 1).id);
    }


}

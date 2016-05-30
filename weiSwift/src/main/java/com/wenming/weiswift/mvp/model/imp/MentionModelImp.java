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
    private boolean mRefrshMentionList = true;
    private OnDataFinishedListener mOnDataFinishedListener;
    private Context mContext;
    private int mCurrentGroup;


    @Override
    public void mentions(int groupType, Context context, final OnDataFinishedListener onDataFinishedListener) {
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        long sinceId = checkout(groupType);
        mContext = context;
        mOnDataFinishedListener = onDataFinishedListener;
        switch (groupType) {
            case Constants.GROUP_RETWEET_TYPE_ALL:
                mStatusesAPI.mentions(sinceId, 0, NewFeature.GET_MENTION_ITEM, 1, StatusesAPI.AUTHOR_FILTER_ALL, 0, 0, true, pullToRefreshListener);
                break;
            case Constants.GROUP_RETWEET_TYPE_FRIENDS:
                mStatusesAPI.mentions(sinceId, 0, NewFeature.GET_MENTION_ITEM, 1, StatusesAPI.AUTHOR_FILTER_ATTENTIONS, 0, 0, true, pullToRefreshListener);
                break;
            case Constants.GROUP_RETWEET_TYPE_ORIGINWEIBO:
                mStatusesAPI.mentions(sinceId, 0, NewFeature.GET_MENTION_ITEM, 1, StatusesAPI.AUTHOR_FILTER_ALL, 0, StatusesAPI.TYPE_FILTER_ORIGAL, true, pullToRefreshListener);
                break;
            case Constants.GROUP_RETWEET_TYPE_ALLCOMMENT:
                break;
            case Constants.GROUP_RETWEET_TYPE_FRIEDNSCOMMENT:
                break;
        }

    }

    @Override
    public void mentionsNextPage(int groupType, Context context, final OnDataFinishedListener onDataFinishedListener) {
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        String maxId = mMentionList.get(mMentionList.size() - 1).id;
        mContext = context;
        mOnDataFinishedListener = onDataFinishedListener;
        switch (groupType) {
            case Constants.GROUP_RETWEET_TYPE_ALL:
                mStatusesAPI.mentions(0, Long.valueOf(maxId), NewFeature.LOADMORE_MENTION_ITEM, 1, StatusesAPI.AUTHOR_FILTER_ALL, 0, 0, true, nextPageListener);
                break;
            case Constants.GROUP_RETWEET_TYPE_FRIENDS:
                mStatusesAPI.mentions(0, Long.valueOf(maxId), NewFeature.LOADMORE_MENTION_ITEM, 1, StatusesAPI.AUTHOR_FILTER_ATTENTIONS, 0, 0, true, nextPageListener);
                break;
            case Constants.GROUP_RETWEET_TYPE_ORIGINWEIBO:
                mStatusesAPI.mentions(0, Long.valueOf(maxId), NewFeature.LOADMORE_MENTION_ITEM, 1, StatusesAPI.AUTHOR_FILTER_ALL, 0, StatusesAPI.TYPE_FILTER_ORIGAL, true, nextPageListener);
                break;
            case Constants.GROUP_RETWEET_TYPE_ALLCOMMENT:
                break;
            case Constants.GROUP_RETWEET_TYPE_FRIEDNSCOMMENT:
                break;
        }

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

    private long checkout(int groupType) {
        long sinceId = 0;
        if (mCurrentGroup != groupType) {
            mRefrshMentionList = true;
        }
        //如果是局部刷新，更新一下sinceId的值为第一条微博的id
        if (mMentionList.size() > 0 && mCurrentGroup == groupType && mRefrshMentionList == false) {
            sinceId = Long.valueOf(mMentionList.get(0).id);
        }
        //如果是全局刷新，把sinceId设置为0，去请求
        if (mRefrshMentionList) {
            sinceId = 0;
        }
        mCurrentGroup = groupType;
        return sinceId;
    }


    public RequestListener pullToRefreshListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            ArrayList<Status> temp = StatusList.parse(response).statusList;
            if (temp != null && temp.size() > 0) {
                if (mMentionList != null) {
                    mMentionList.clear();
                }
                mentionCacheSave(mContext, response);
                mMentionList = temp;
                mOnDataFinishedListener.onDataFinish(mMentionList);
            } else {
                ToastUtil.showShort(mContext, "没有更新的内容了");
                mOnDataFinishedListener.noMoreDate();
            }
            mRefrshMentionList = false;
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ToastUtil.showShort(mContext, e.getMessage());
            mentionCacheLoad(mContext, mOnDataFinishedListener);
        }
    };

    public RequestListener nextPageListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                ArrayList<Status> temp = StatusList.parse(response).statusList;
                if (temp.size() == 0 || (temp != null && temp.size() == 1 && temp.get(0).id.equals(String.valueOf(mMentionList.get(mMentionList.size() - 1).id)))) {
                    mOnDataFinishedListener.noMoreDate();
                } else if (temp.size() > 1) {
                    temp.remove(0);
                    mMentionList.addAll(temp);
                    mOnDataFinishedListener.onDataFinish(mMentionList);
                }
            } else {
                ToastUtil.showShort(mContext, "内容已经加载完了");
                mOnDataFinishedListener.noMoreDate();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            mOnDataFinishedListener.onError(e.getMessage());
        }
    };

}

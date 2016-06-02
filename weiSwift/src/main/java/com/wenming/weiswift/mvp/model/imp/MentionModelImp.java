package com.wenming.weiswift.mvp.model.imp;

import android.content.Context;
import android.text.TextUtils;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.wenming.weiswift.api.CommentsAPI;
import com.wenming.weiswift.api.StatusesAPI;
import com.wenming.weiswift.entity.Comment;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.entity.list.CommentList;
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
    private ArrayList<Comment> mCommentList = new ArrayList<>();
    private boolean mRefrshAll = true;
    private OnMentionFinishedListener mOnMentionFinishedListener;
    private OnCommentFinishedListener mOnCommentFinishedListener;
    private Context mContext;
    private int mCurrentGroup;

    @Override
    public void mentions(int groupType, Context context, OnMentionFinishedListener onMentionFinishedListener) {
        StatusesAPI statusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        long sinceId = checkout(groupType);
        mContext = context;
        mOnMentionFinishedListener = onMentionFinishedListener;
        switch (groupType) {
            case Constants.GROUP_RETWEET_TYPE_ALL:
                statusesAPI.mentions(sinceId, 0, NewFeature.GET_MENTION_ITEM, 1, StatusesAPI.AUTHOR_FILTER_ALL, 0, 0, true, pullToRefresh_Mention_Listener);
                break;
            case Constants.GROUP_RETWEET_TYPE_FRIENDS:
                statusesAPI.mentions(sinceId, 0, NewFeature.GET_MENTION_ITEM, 1, StatusesAPI.AUTHOR_FILTER_ATTENTIONS, 0, 0, true, pullToRefresh_Mention_Listener);
                break;
            case Constants.GROUP_RETWEET_TYPE_ORIGINWEIBO:
                statusesAPI.mentions(sinceId, 0, NewFeature.GET_MENTION_ITEM, 1, StatusesAPI.AUTHOR_FILTER_ALL, 0, StatusesAPI.TYPE_FILTER_ORIGAL, true, pullToRefresh_Mention_Listener);
                break;
        }
    }


    @Override
    public void commentMentions(int groupType, Context context, OnCommentFinishedListener onCommentFinishedListener) {
        CommentsAPI commentsAPI = new CommentsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        mOnCommentFinishedListener = onCommentFinishedListener;
        long sinceId = checkout(groupType);
        switch (groupType) {
            case Constants.GROUP_RETWEET_TYPE_ALLCOMMENT:
                commentsAPI.mentions(sinceId, 0, NewFeature.GET_MENTION_ITEM, 1, CommentsAPI.AUTHOR_FILTER_ALL, 0, pullToRefresh_Comment_Listener);
                break;
            case Constants.GROUP_RETWEET_TYPE_FRIEDNSCOMMENT:
                commentsAPI.mentions(sinceId, 0, NewFeature.GET_MENTION_ITEM, 1, CommentsAPI.AUTHOR_FILTER_ATTENTIONS, 0, pullToRefresh_Comment_Listener);
                break;
        }
    }

    @Override
    public void mentionsNextPage(int groupType, Context context, OnMentionFinishedListener onMentionFinishedListener) {
        StatusesAPI statusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        String maxId = mMentionList.get(mMentionList.size() - 1).id;
        mContext = context;
        mOnMentionFinishedListener = onMentionFinishedListener;
        switch (groupType) {
            case Constants.GROUP_RETWEET_TYPE_ALL:
                statusesAPI.mentions(0, Long.valueOf(maxId), NewFeature.LOADMORE_MENTION_ITEM, 1, StatusesAPI.AUTHOR_FILTER_ALL, 0, 0, true, nextPage_Mention_Listener);
                break;
            case Constants.GROUP_RETWEET_TYPE_FRIENDS:
                statusesAPI.mentions(0, Long.valueOf(maxId), NewFeature.LOADMORE_MENTION_ITEM, 1, StatusesAPI.AUTHOR_FILTER_ATTENTIONS, 0, 0, true, nextPage_Mention_Listener);
                break;
            case Constants.GROUP_RETWEET_TYPE_ORIGINWEIBO:
                statusesAPI.mentions(0, Long.valueOf(maxId), NewFeature.LOADMORE_MENTION_ITEM, 1, StatusesAPI.AUTHOR_FILTER_ALL, 0, StatusesAPI.TYPE_FILTER_ORIGAL, true, nextPage_Mention_Listener);
                break;
        }
    }

    @Override
    public void commentMentionsNextPage(int groupType, Context context, OnCommentFinishedListener onCommentFinishedListener) {
        CommentsAPI commentsAPI = new CommentsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        mOnCommentFinishedListener = onCommentFinishedListener;
        String maxId = mCommentList.get(mCommentList.size() - 1).id;
        switch (groupType) {
            case Constants.GROUP_RETWEET_TYPE_ALLCOMMENT:
                commentsAPI.mentions(0, Long.valueOf(maxId), NewFeature.LOADMORE_MENTION_ITEM, 1, CommentsAPI.AUTHOR_FILTER_ALL, 0, nextPage_Comment_Listener);
                break;
            case Constants.GROUP_RETWEET_TYPE_FRIEDNSCOMMENT:
                commentsAPI.mentions(0, Long.valueOf(maxId), NewFeature.LOADMORE_MENTION_ITEM, 1, CommentsAPI.AUTHOR_FILTER_ATTENTIONS, 0, nextPage_Comment_Listener);
                break;
        }
    }

    @Override
    public void cacheSave(int groupType, Context context, String response) {
        if (NewFeature.CACHE_MESSAGE_MENTION) {
            switch (groupType) {
                case Constants.GROUP_RETWEET_TYPE_ALL:
                    SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/message/mention", "所有微博" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt", response);
                    break;
                case Constants.GROUP_RETWEET_TYPE_FRIENDS:
                    SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/message/mention", "关注人的微博" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt", response);
                    break;
                case Constants.GROUP_RETWEET_TYPE_ORIGINWEIBO:
                    SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/message/mention", "原创微博" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt", response);
                    break;
                case Constants.GROUP_RETWEET_TYPE_ALLCOMMENT:
                    SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/message/mention", "所有评论" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt", response);
                    break;
                case Constants.GROUP_RETWEET_TYPE_FRIEDNSCOMMENT:
                    SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/message/mention", "关注人的评论" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt", response);
                    break;

            }

        }
    }

    @Override
    public void cacheLoad(int groupType, Context context, OnMentionFinishedListener onDataFinishedListener) {
        if (NewFeature.CACHE_MESSAGE_MENTION) {
            String response = null;
            switch (groupType) {
                case Constants.GROUP_RETWEET_TYPE_ALL:
                    response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/message/mention", "所有微博" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
                    break;
                case Constants.GROUP_RETWEET_TYPE_FRIENDS:
                    response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/message/mention", "关注人的微博" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
                    break;
                case Constants.GROUP_RETWEET_TYPE_ORIGINWEIBO:
                    response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/message/mention", "原创微博" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
                    break;
            }

            if (response != null) {
                mMentionList = StatusList.parse(response).statusList;
                onDataFinishedListener.onDataFinish(mMentionList);
            }
        }
    }

    @Override
    public void cacheLoad(int groupType, Context context, OnCommentFinishedListener onCommentFinishedListener) {
        if (NewFeature.CACHE_MESSAGE_MENTION) {
            String response = null;
            switch (groupType) {
                case Constants.GROUP_RETWEET_TYPE_ALLCOMMENT:
                    response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/message/mention", "所有评论" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
                    break;
                case Constants.GROUP_RETWEET_TYPE_FRIEDNSCOMMENT:
                    response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/message/mention", "关注人的评论" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
                    break;
            }

            if (response != null) {
                mCommentList = CommentList.parse(response).commentList;
                onCommentFinishedListener.onDataFinish(mCommentList);
            }
        }
    }


    private long checkout(int groupType) {
        long sinceId = 0;
        if (mCurrentGroup != groupType) {
            mRefrshAll = true;
        }

        if (groupType == Constants.GROUP_RETWEET_TYPE_ALL || groupType == Constants.GROUP_RETWEET_TYPE_FRIENDS || groupType == Constants.GROUP_RETWEET_TYPE_ORIGINWEIBO) {
            if (mMentionList.size() > 0 && mCurrentGroup == groupType && mRefrshAll == false) {
                sinceId = Long.valueOf(mMentionList.get(0).id);
            }
        } else {
            if (mCommentList.size() > 0 && mCurrentGroup == groupType && mRefrshAll == false) {
                sinceId = Long.valueOf(mCommentList.get(0).id);
            }
        }

        //如果是全局刷新，把sinceId设置为0，去请求
        if (mRefrshAll) {
            sinceId = 0;
        }
        mCurrentGroup = groupType;
        return sinceId;
    }

    public RequestListener pullToRefresh_Mention_Listener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            ArrayList<Status> temp = StatusList.parse(response).statusList;
            if (temp != null && temp.size() > 0) {
                if (mMentionList != null) {
                    mMentionList.clear();
                }
                cacheSave(mCurrentGroup, mContext, response);
                mMentionList = temp;
                mOnMentionFinishedListener.onDataFinish(mMentionList);
            } else {
                ToastUtil.showShort(mContext, "没有更新的内容了");
                mOnMentionFinishedListener.noMoreDate();
            }
            mRefrshAll = false;
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ToastUtil.showShort(mContext, e.getMessage());
            cacheLoad(mCurrentGroup, mContext, mOnMentionFinishedListener);
        }
    };

    private RequestListener pullToRefresh_Comment_Listener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            ArrayList<Comment> temp = CommentList.parse(response).commentList;
            if (temp != null && temp.size() > 0) {
                if (mCommentList != null) {
                    mCommentList.clear();
                }
                cacheSave(mCurrentGroup, mContext, response);
                mCommentList = temp;
                mOnCommentFinishedListener.onDataFinish(mCommentList);
            } else {
                ToastUtil.showShort(mContext, "没有更新的内容了");
                mOnCommentFinishedListener.noMoreDate();
            }
            mRefrshAll = false;
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ToastUtil.showShort(mContext, e.getMessage());
            cacheLoad(mCurrentGroup, mContext, mOnCommentFinishedListener);
        }
    };
    public RequestListener nextPage_Mention_Listener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                ArrayList<Status> temp = StatusList.parse(response).statusList;
                if (temp.size() == 0 || (temp != null && temp.size() == 1 && temp.get(0).id.equals(String.valueOf(mMentionList.get(mMentionList.size() - 1).id)))) {
                    mOnMentionFinishedListener.noMoreDate();
                } else if (temp.size() > 1) {
                    temp.remove(0);
                    mMentionList.addAll(temp);
                    mOnMentionFinishedListener.onDataFinish(mMentionList);
                }
            } else {
                ToastUtil.showShort(mContext, "内容已经加载完了");
                mOnMentionFinishedListener.noMoreDate();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            mOnMentionFinishedListener.onError(e.getMessage());
        }
    };
    public RequestListener nextPage_Comment_Listener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                ArrayList<Comment> temp = CommentList.parse(response).commentList;
                if (temp.size() == 1) {
                    mOnCommentFinishedListener.noMoreDate();
                } else if (temp.size() > 1) {
                    temp.remove(0);
                    mCommentList.addAll(temp);
                    mOnCommentFinishedListener.onDataFinish(mCommentList);
                } else {
                    ToastUtil.showShort(mContext, "数据异常");
                    mOnCommentFinishedListener.onError("数据异常");
                }
            } else {
                ToastUtil.showShort(mContext, "内容已经加载完了");
                mOnCommentFinishedListener.noMoreDate();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            mOnCommentFinishedListener.onError(e.getMessage());
        }
    };


}

package com.wenming.weiswift.mvp.model.imp;

import android.content.Context;
import android.text.TextUtils;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.wenming.weiswift.api.CommentsAPI;
import com.wenming.weiswift.entity.Comment;
import com.wenming.weiswift.entity.list.CommentList;
import com.wenming.weiswift.mvp.model.CommentModel;
import com.wenming.weiswift.ui.common.NewFeature;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.common.login.Constants;
import com.wenming.weiswift.utils.SDCardUtil;
import com.wenming.weiswift.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/15.
 */
public class CommentModelImp implements CommentModel {
    private ArrayList<Comment> mCommentList = new ArrayList<>();
    private boolean mRefrshCommentList = true;

    //统一接口
    private Context mContext;
    private OnDataFinishedListener mOnDataFinishedListener;

    /**
     * 用于标识当前分组
     */
    private int mCurrentGroup = Constants.GROUP_COMMENT_TYPE_ALL;


    @Override
    public void toMe(int groupType, Context context, OnDataFinishedListener onDataFinishedListener) {
        CommentsAPI commentsAPI = new CommentsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        mOnDataFinishedListener = onDataFinishedListener;
        long sinceId = 0;
        if (groupType == CommentsAPI.AUTHOR_FILTER_ALL) {
            sinceId = checkout(Constants.GROUP_COMMENT_TYPE_ALL);
        } else {
            sinceId = checkout(Constants.GROUP_COMMENT_TYPE_FRIENDS);
        }
        commentsAPI.toME(sinceId, 0, NewFeature.GET_COMMENT_ITEM, 1, groupType, 0, pullToRefreshListener);
    }

    @Override
    public void byMe(Context context, OnDataFinishedListener onDataFinishedListener) {
        CommentsAPI commentsAPI = new CommentsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        mOnDataFinishedListener = onDataFinishedListener;
        long sinceId = checkout(Constants.GROUP_COMMENT_TYPE_BYME);
        commentsAPI.byME(sinceId, 0, NewFeature.GET_COMMENT_ITEM, 1, 0, pullToRefreshListener);
    }

    @Override
    public void toMeNextPage(int groupType, Context context, OnDataFinishedListener onDataFinishedListener) {
        CommentsAPI commentsAPI = new CommentsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        String maxId = mCommentList.get(mCommentList.size() - 1).id;
        mOnDataFinishedListener = onDataFinishedListener;
        commentsAPI.toME(0, Long.valueOf(maxId), NewFeature.LOADMORE_COMMENT_ITEM, 1, groupType, 0, nextPageListener);
    }

    @Override
    public void byMeNextPage(Context context, OnDataFinishedListener onDataFinishedListener) {
        CommentsAPI commentsAPI = new CommentsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        mOnDataFinishedListener = onDataFinishedListener;
        String maxId = mCommentList.get(mCommentList.size() - 1).id;
        commentsAPI.byME(0, Long.valueOf(maxId), NewFeature.LOADMORE_MENTION_ITEM, 1, 0, nextPageListener);
    }


    @Override
    public void cacheSave(int groupType, Context context, String response) {
        if (NewFeature.CACHE_MESSAGE_MENTION) {
            switch (groupType) {
                case Constants.GROUP_COMMENT_TYPE_ALL:
                    SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/message/comment", "所有评论" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt", response);
                    break;
                case Constants.GROUP_COMMENT_TYPE_FRIENDS:
                    SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/message/comment", "关注的人" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt", response);
                    break;
                case Constants.GROUP_COMMENT_TYPE_BYME:
                    SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/message/comment", "我发出的" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt", response);
                    break;
            }
        }
    }

    @Override
    public void cacheLoad(int groupType, Context context, OnDataFinishedListener onDataFinishedListener) {
        if (NewFeature.CACHE_MESSAGE_MENTION) {
            String response = null;
            switch (groupType) {
                case Constants.GROUP_COMMENT_TYPE_ALL:
                    SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/message/comment", "所有评论" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
                    break;
                case Constants.GROUP_COMMENT_TYPE_FRIENDS:
                    SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/message/comment", "关注的人" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
                    break;
                case Constants.GROUP_COMMENT_TYPE_BYME:
                    SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/message/comment", "我发出的" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
                    break;
            }

            if (response != null) {
                mCurrentGroup = Constants.GROUP_COMMENT_TYPE_ALL;
                mCommentList = CommentList.parse(response).commentList;
                onDataFinishedListener.onDataFinish(mCommentList);
            }
        }
    }


    private long checkout(int authorType) {
        long sinceId = 0;
        if (mCurrentGroup != authorType) {
            mRefrshCommentList = true;
        }
        //如果是局部刷新，更新一下sinceId的值为第一条微博的id
        if (mCommentList.size() > 0 && mCurrentGroup == authorType && mRefrshCommentList == false) {
            sinceId = Long.valueOf(mCommentList.get(0).id);
        }
        //如果是全局刷新，把sinceId设置为0，去请求
        if (mRefrshCommentList) {
            sinceId = 0;
        }
        mCurrentGroup = authorType;
        return sinceId;
    }

    private RequestListener pullToRefreshListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            ArrayList<Comment> temp = CommentList.parse(response).commentList;
            if (temp != null && temp.size() > 0) {
                if (mCommentList != null) {
                    mCommentList.clear();
                }
                cacheSave(mCurrentGroup, mContext, response);
                mCommentList = temp;
                mOnDataFinishedListener.onDataFinish(mCommentList);
            } else {
                ToastUtil.showShort(mContext, "没有更新的内容了");
                mOnDataFinishedListener.noMoreDate();
            }
            mRefrshCommentList = false;
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ToastUtil.showShort(mContext, e.getMessage());
            cacheLoad(mCurrentGroup, mContext, mOnDataFinishedListener);
        }
    };

    public RequestListener nextPageListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                ArrayList<Comment> temp = CommentList.parse(response).commentList;
                if (temp.size() == 1) {
                    mOnDataFinishedListener.noMoreDate();
                } else if (temp.size() > 1) {
                    temp.remove(0);
                    mCommentList.addAll(temp);
                    mOnDataFinishedListener.onDataFinish(mCommentList);
                } else {
                    ToastUtil.showShort(mContext, "数据异常");
                    mOnDataFinishedListener.onError("数据异常");
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

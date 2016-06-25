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
import com.wenming.weiswift.entity.list.RetweetList;
import com.wenming.weiswift.mvp.model.StatusDetailModel;
import com.wenming.weiswift.ui.common.NewFeature;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.common.login.Constants;
import com.wenming.weiswift.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/6/25.
 */
public class StatusDetailModelImp implements StatusDetailModel {
    private ArrayList<Status> mRepostList = new ArrayList<>();
    private ArrayList<Comment> mCommentList = new ArrayList<>();
    private boolean mRefrshAll = true;
    private int mCurrentGroup;
    public static final int COMMENT_PAGE = 0x1;
    public static final int REPOST_PAGE = 0x2;

    @Override
    public void comment(int groupType, Status status, final Context context, final OnCommentCallBack onCommentCallBack) {
        CommentsAPI commentsAPI = new CommentsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        long sinceId = checkout(groupType);
        commentsAPI.show(Long.valueOf(status.id), sinceId, 0, NewFeature.GET_COMMENT_ITEM, 1, 0, new RequestListener() {
            @Override
            public void onComplete(String response) {
                ArrayList<Comment> temp = CommentList.parse(response).comments;
                if (temp != null && temp.size() > 0) {
                    if (mCommentList != null) {
                        mCommentList.clear();
                    }
                    mCommentList = temp;
                    onCommentCallBack.onDataFinish(mCommentList);
                } else {
                    ToastUtil.showShort(context, "没有更新的内容了");
                    onCommentCallBack.noMoreDate();
                }
                mRefrshAll = false;
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(context, e.getMessage());
                onCommentCallBack.onError(e.getMessage());
            }
        });
    }

    @Override
    public void commentNextPage(int groupType, Status status, final Context context, final OnCommentCallBack onCommentCallBack) {
        CommentsAPI commentsAPI = new CommentsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        String maxId = mCommentList.get(mCommentList.size() - 1).id;
        commentsAPI.show(Long.valueOf(status.id), 0, Long.valueOf(maxId), NewFeature.GET_COMMENT_ITEM, 1, 0, new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {
                    ArrayList<Comment> temp = CommentList.parse(response).comments;
                    if (temp.size() == 1) {
                        onCommentCallBack.noMoreDate();
                    } else if (temp.size() > 1) {
                        temp.remove(0);
                        mCommentList.addAll(temp);
                        onCommentCallBack.onDataFinish(mCommentList);
                    } else {
                        ToastUtil.showShort(context, "数据异常");
                        onCommentCallBack.onError("数据异常");
                    }
                } else {
                    ToastUtil.showShort(context, "内容已经加载完了");
                    onCommentCallBack.noMoreDate();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                onCommentCallBack.onError(e.getMessage());
            }
        });
    }

    @Override
    public void repost(int groupType, Status status, final Context context, final OnRepostCallBack onRepostCallBack) {
        StatusesAPI statusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        long sinceId = checkout(groupType);
        statusesAPI.repostTimeline(Long.valueOf(status.id), sinceId, 0, NewFeature.GET_COMMENT_ITEM, 1, 0, new RequestListener() {
            @Override
            public void onComplete(String response) {
                ArrayList<Status> temp = RetweetList.parse(response).reposts;
                if (temp != null && temp.size() > 0) {
                    if (mRepostList != null) {
                        mRepostList.clear();
                    }
                    mRepostList = temp;
                    onRepostCallBack.onDataFinish(mRepostList);
                } else {
                    ToastUtil.showShort(context, "没有更新的内容了");
                    onRepostCallBack.noMoreDate();
                }
                mRefrshAll = false;
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(context, e.getMessage());
                onRepostCallBack.onError(e.getMessage());
            }
        });
    }

    @Override
    public void repostNextPage(int groupType, Status status, final Context context, final OnRepostCallBack onRepostCallBack) {
        StatusesAPI statusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        String maxId = mRepostList.get(mRepostList.size() - 1).id;
        statusesAPI.repostTimeline(Long.valueOf(status.id), 0, Long.valueOf(maxId), NewFeature.GET_COMMENT_ITEM, 1, 0, new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {
                    ArrayList<Status> temp = RetweetList.parse(response).reposts;
                    if (temp.size() == 1) {
                        onRepostCallBack.noMoreDate();
                    } else if (temp.size() > 1) {
                        temp.remove(0);
                        mRepostList.addAll(temp);
                        onRepostCallBack.onDataFinish(mRepostList);
                    } else {
                        ToastUtil.showShort(context, "数据异常");
                        onRepostCallBack.onError("数据异常");
                    }
                } else {
                    ToastUtil.showShort(context, "内容已经加载完了");
                    onRepostCallBack.noMoreDate();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                onRepostCallBack.onError(e.getMessage());
            }
        });
    }

    private long checkout(int groupType) {
        long sinceId = 0;
        if (mCurrentGroup != groupType) {
            mRefrshAll = true;
        }

        if (groupType == REPOST_PAGE) {
            if (mRepostList.size() > 0 && mCurrentGroup == groupType && mRefrshAll == false) {
                sinceId = Long.valueOf(mRepostList.get(0).id);
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


}

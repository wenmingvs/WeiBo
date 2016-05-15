package com.wenming.weiswift.mvp.model.imp;

import android.content.Context;
import android.text.TextUtils;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
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
    private CommentsAPI mCommentsAPI;
    private ArrayList<Comment> mCommentList;
    private long mFirstWeiboId;
    private long mLastWeiboId;
    private boolean mRefrshAllData = true;
    private boolean mNoMoreData;


    @Override
    public void getLatestComment(final Context context, final OnDataFinishedListener onDataFinishedListener) {
        if (mCommentsAPI == null) {
            mCommentsAPI = new CommentsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        }

        if (mRefrshAllData) {
            mFirstWeiboId = 0;
        }
        mCommentsAPI.toME(mFirstWeiboId, 0, NewFeature.GET_COMMENT_ITEM, 1, 0, 0, new RequestListener() {
            @Override
            public void onComplete(String response) {
                ArrayList<Comment> temp = CommentList.parse(response).commentList;
                if (temp != null && temp.size() > 0) {
                    if (mCommentList != null) {
                        mCommentList.clear();
                    }
                    saveCommentCache(context, response);
                    mCommentList = temp;
                    updateId();
                    onDataFinishedListener.onDataFinish(mCommentList);
                } else {
                    ToastUtil.showShort(context, "没有更新的内容了");
                    onDataFinishedListener.noMoreDate();
                }
                mRefrshAllData = false;
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(context, e.getMessage());
                onDataFinishedListener.onDataFinish(getCommentCache(context));
            }
        });
    }

    @Override
    public void getNextPageComment(final Context context, final OnDataFinishedListener onDataFinishedListener) {
        if (mCommentsAPI == null) {
            mCommentsAPI = new CommentsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        }

        if (mNoMoreData == true) {
            return;

        }

        mCommentsAPI.mentions(0, mLastWeiboId, NewFeature.LOADMORE_MENTION_ITEM, 1, 0, 0, new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {
                    ArrayList<Comment> temp = CommentList.parse(response).commentList;
                    if (temp.size() == 0 || (temp != null && temp.size() == 1 && temp.get(0).id.equals(String.valueOf(mLastWeiboId)))) {
                        mNoMoreData = true;
                        onDataFinishedListener.noMoreDate();
                    } else if (temp.size() > 1) {
                        temp.remove(0);
                        mCommentList.addAll(temp);
                        updateId();
                        onDataFinishedListener.onDataFinish(mCommentList);
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
    public void saveCommentCache(Context context, String response) {
        if (NewFeature.CACHE_MESSAGE_MENTION) {
            SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "message_comment" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt", response);
        }
    }

    @Override
    public ArrayList<Comment> getCommentCache(Context context) {
        if (NewFeature.CACHE_MESSAGE_MENTION) {
            String response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "message_comment" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
            if (response != null) {
                mCommentList = CommentList.parse(response).commentList;
                updateId();
                return mCommentList;
            }
        }
        return null;
    }

    public void updateId() {
        mFirstWeiboId = Long.valueOf(mCommentList.get(0).id);
        mLastWeiboId = Long.valueOf(mCommentList.get(mCommentList.size() - 1).id);
    }

}

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


    @Override
    public void toMe(final Context context, final OnDataFinishedListener onDataFinishedListener) {
        CommentsAPI mCommentsAPI = new CommentsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        long sinceId = 0;
        if (mCommentList.size() > 0) {
            sinceId = Long.valueOf(mCommentList.get(0).id);
        }
        if (mRefrshCommentList) {
            sinceId = 0;
        }
        mCommentsAPI.toME(sinceId, 0, NewFeature.GET_COMMENT_ITEM, 1, 0, 0, new RequestListener() {
            @Override
            public void onComplete(String response) {
                ArrayList<Comment> temp = CommentList.parse(response).commentList;
                if (temp != null && temp.size() > 0) {
                    if (mCommentList != null) {
                        mCommentList.clear();
                    }
                    toMeCacheSave(context, response);
                    mCommentList = temp;
                    onDataFinishedListener.onDataFinish(mCommentList);
                } else {
                    ToastUtil.showShort(context, "没有更新的内容了");
                    onDataFinishedListener.noMoreDate();
                }
                mRefrshCommentList = false;
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(context, e.getMessage());
                ToMeCacheLoad(context, onDataFinishedListener);
            }
        });
    }

    @Override
    public void toMeNextPage(final Context context, final OnDataFinishedListener onDataFinishedListener) {
        CommentsAPI mCommentsAPI = new CommentsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        final String maxId = mCommentList.get(mCommentList.size() - 1).id;
        mCommentsAPI.mentions(0, Long.valueOf(maxId), NewFeature.LOADMORE_MENTION_ITEM, 1, 0, 0, new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {
                    ArrayList<Comment> temp = CommentList.parse(response).commentList;
                    if (temp.size() == 0 || (temp != null && temp.size() == 1 && temp.get(0).id.equals(maxId))) {

                        onDataFinishedListener.noMoreDate();
                    } else if (temp.size() > 1) {
                        temp.remove(0);
                        mCommentList.addAll(temp);
                        onDataFinishedListener.onDataFinish(mCommentList);
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
    public void toMeCacheSave(Context context, String response) {
        if (NewFeature.CACHE_MESSAGE_MENTION) {
            SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "message_comment" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt", response);
        }
    }

    @Override
    public void ToMeCacheLoad(Context context, OnDataFinishedListener onDataFinishedListener) {
        if (NewFeature.CACHE_MESSAGE_MENTION) {
            String response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "message_comment" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
            if (response != null) {
                mCommentList = CommentList.parse(response).commentList;
                onDataFinishedListener.onDataFinish(mCommentList);
            }
        }
    }
}

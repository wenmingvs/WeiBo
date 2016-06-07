package com.wenming.weiswift.mvp.model.imp;

import android.content.Context;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.wenming.weiswift.api.FavoritesAPI;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.mvp.model.FavoriteListModel;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.common.login.Constants;
import com.wenming.weiswift.utils.ToastUtil;

/**
 * Created by wenmingvs on 16/6/6.
 */
public class FavoriteListModelImp implements FavoriteListModel {
    private Context mContext;
    private OnRequestUIListener mOnRequestUIListener;

    @Override
    public void createFavorite(final Status status, Context context, OnRequestUIListener onRequestUIListener) {
        mContext = context;
        mOnRequestUIListener = onRequestUIListener;
        FavoritesAPI favoritesAPI = new FavoritesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        favoritesAPI.create(Long.valueOf(status.id), new RequestListener() {
            @Override
            public void onComplete(String s) {
                ToastUtil.showShort(mContext, "收藏成功");
                status.favorited = true;
                mOnRequestUIListener.onSuccess();
            }

            @Override
            public void onWeiboException(WeiboException e) {
                //如果是已经收藏的微博，需要纠正他
                if (e.getMessage().contains("20704")) {
                    ToastUtil.showShort(mContext, "请不要重复收藏");
                    status.favorited = true;
                } else {
                    ToastUtil.showShort(mContext, "收藏失败");
                    ToastUtil.showShort(mContext, e.getMessage());
                }
                mOnRequestUIListener.onError(e.getMessage());
            }
        });
    }

    @Override
    public void cancelFavorite(final Status status, Context context, OnRequestUIListener onRequestUIListener) {
        mContext = context;
        mOnRequestUIListener = onRequestUIListener;
        final FavoritesAPI favoritesAPI = new FavoritesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        favoritesAPI.destroy(Long.valueOf(status.id), new RequestListener() {
            @Override
            public void onComplete(String s) {
                ToastUtil.showShort(mContext, "取消收藏成功");
                status.favorited = false;
                mOnRequestUIListener.onSuccess();
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(mContext, "取消收藏失败");
                ToastUtil.showShort(mContext, e.getMessage());
                mOnRequestUIListener.onError(e.getMessage());
            }
        });
    }


}

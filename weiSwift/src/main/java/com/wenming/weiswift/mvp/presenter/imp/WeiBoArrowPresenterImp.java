package com.wenming.weiswift.mvp.presenter.imp;

import android.content.Context;

import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.entity.User;
import com.wenming.weiswift.mvp.model.FavoriteListModel;
import com.wenming.weiswift.mvp.model.FriendShipModel;
import com.wenming.weiswift.mvp.model.StatusListModel;
import com.wenming.weiswift.mvp.model.imp.FavoriteListModelImp;
import com.wenming.weiswift.mvp.model.imp.FriendShipModelImp;
import com.wenming.weiswift.mvp.model.imp.StatusListModelImp;
import com.wenming.weiswift.mvp.presenter.WeiBoArrowPresent;
import com.wenming.weiswift.mvp.view.WeiBoArrowView;
import com.wenming.weiswift.ui.login.fragment.home.weiboitem.ArrowPopupWindow;

/**
 * Created by xiangflight on 2016/4/22.
 */
public class WeiBoArrowPresenterImp implements WeiBoArrowPresent {
    private StatusListModel statusListModel;
    private FriendShipModel friendShipModel;
    private FavoriteListModel favoriteListModel;
    private WeiBoArrowView weiBoArrowView;
    private Context mContext;

    public WeiBoArrowPresenterImp(ArrowPopupWindow weiBoArrowPopupWindow) {
        statusListModel = new StatusListModelImp();
        friendShipModel = new FriendShipModelImp();
        favoriteListModel = new FavoriteListModelImp();
        this.weiBoArrowView = weiBoArrowPopupWindow;
    }

    /**
     * 删除一条微博
     *
     * @param id
     * @param context
     */
    public void weibo_destroy(long id, Context context) {
        mContext = context;
        weiBoArrowView.dismiss();
        statusListModel.weibo_destroy(id, context, new StatusListModel.OnRequestListener() {
            @Override
            public void onSuccess() {


            }

            @Override
            public void onError(String error) {

            }
        });
    }

    /**
     * 取消关注某一用户
     *
     * @param context
     */
    public void user_destroy(User user, Context context) {
        mContext = context;
        weiBoArrowView.dismiss();
        friendShipModel.user_destroy(user, context, new FriendShipModel.OnRequestListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public void user_create(User user, Context context) {
        mContext = context;
        weiBoArrowView.dismiss();
        friendShipModel.user_create(user, context, new FriendShipModel.OnRequestListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    /**
     * 收藏一条微博
     *
     * @param status
     * @param context
     */
    public void createFavorite(Status status, Context context) {
        mContext = context;
        weiBoArrowView.dismiss();
        favoriteListModel.createFavorite(status, context, new FavoriteListModel.OnRequestUIListener() {
            @Override
            public void onSuccess() {


            }

            @Override
            public void onError(String error) {

            }
        });
    }

    /**
     * 取消收藏一条微博
     *
     * @param status
     * @param context
     */
    @Override
    public void cancalFavorite(Status status, Context context) {
        mContext = context;
        weiBoArrowView.dismiss();
        favoriteListModel.cancelFavorite(status, context, new FavoriteListModel.OnRequestUIListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(String error) {

            }
        });
    }
}

package com.wenming.weiswift.mvp.presenter.imp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.PopupWindow;

import com.google.gson.Gson;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.entity.User;
import com.wenming.weiswift.entity.list.StatusList;
import com.wenming.weiswift.mvp.model.FavoriteListModel;
import com.wenming.weiswift.mvp.model.FriendShipModel;
import com.wenming.weiswift.mvp.model.StatusListModel;
import com.wenming.weiswift.mvp.model.imp.FavoriteListModelImp;
import com.wenming.weiswift.mvp.model.imp.FriendShipModelImp;
import com.wenming.weiswift.mvp.model.imp.StatusListModelImp;
import com.wenming.weiswift.mvp.presenter.WeiBoArrowPresent;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.utils.SDCardUtil;

/**
 * Created by xiangflight on 2016/4/22.
 */
public class WeiBoArrowPresenterImp implements WeiBoArrowPresent {
    private StatusListModel statusListModel;
    private FriendShipModel friendShipModel;
    private FavoriteListModel favoriteListModel;
    private PopupWindow mPopupWindows;
    private RecyclerView.Adapter mAdapter;
    private Context mContext;

    public WeiBoArrowPresenterImp(PopupWindow popupWindow) {
        statusListModel = new StatusListModelImp();
        friendShipModel = new FriendShipModelImp();
        favoriteListModel = new FavoriteListModelImp();
        this.mPopupWindows = popupWindow;
    }

    public WeiBoArrowPresenterImp(PopupWindow popupWindow, RecyclerView.Adapter adapter) {
        statusListModel = new StatusListModelImp();
        friendShipModel = new FriendShipModelImp();
        favoriteListModel = new FavoriteListModelImp();
        this.mPopupWindows = popupWindow;
        this.mAdapter = adapter;
    }


    /**
     * 删除一条微博
     *
     * @param id
     * @param context
     */
    public void weibo_destroy(long id, Context context, final int position) {
        mContext = context;
        mPopupWindows.dismiss();
        statusListModel.weibo_destroy(id, context, new StatusListModel.OnRequestListener() {
            @Override
            public void onSuccess() {
                //内存删除
                mAdapter.notifyItemRemoved(position);
                //((WeiboAdapter) mAdapter).removeDataItem(position);
                //TODO 本地删除
                String my_All_WeiBo = SDCardUtil.get(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/profile", "我的全部微博" + AccessTokenKeeper.readAccessToken(mContext).getUid() + ".txt");
                String my_Origin_WeiBo = SDCardUtil.get(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/profile", "我的原创微博" + AccessTokenKeeper.readAccessToken(mContext).getUid() + ".txt");
                String my_Pic_WeiBo = SDCardUtil.get(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/profile", "我的图片微博" + AccessTokenKeeper.readAccessToken(mContext).getUid() + ".txt");

                if (my_All_WeiBo != null) {
                    StatusList statusList = StatusList.parse(my_All_WeiBo);
                    statusList.statuses.remove(position);
                    Gson gson = new Gson();
                    gson.toJson(statusList);
                }


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
        mPopupWindows.dismiss();
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
        mPopupWindows.dismiss();
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
        mPopupWindows.dismiss();
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
        mPopupWindows.dismiss();
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

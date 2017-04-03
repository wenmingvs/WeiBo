package com.wenming.weiswift.app.mvp.presenter.imp;

import android.content.Context;

import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.mvp.model.FavoriteListModel;
import com.wenming.weiswift.app.mvp.model.imp.FavoriteListModelImp;
import com.wenming.weiswift.app.mvp.presenter.FavoritiesPresent;
import com.wenming.weiswift.app.mvp.view.FavoritiesActivityView;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/16.
 */
public class FavoritiesPresentImp implements FavoritiesPresent {

    private FavoriteListModel mFavoriteListModel;
    private FavoritiesActivityView mFavoritiesActivityView;

    public FavoritiesPresentImp(FavoritiesActivityView favoritiesActivityView) {
        this.mFavoritiesActivityView = favoritiesActivityView;
        this.mFavoriteListModel = new FavoriteListModelImp();
    }

    @Override
    public void pullToRefreshData(Context context) {
        mFavoritiesActivityView.showLoadingIcon();
        mFavoriteListModel.favorites(context, new FavoriteListModel.OnDataFinishedListener() {
            @Override
            public void noMoreDate() {
                mFavoritiesActivityView.hideLoadingIcon();
            }

            @Override
            public void onDataFinish(ArrayList<Status> list) {
                mFavoritiesActivityView.hideLoadingIcon();
                mFavoritiesActivityView.updateListView(list);
            }

            @Override
            public void onError(String error) {
                mFavoritiesActivityView.hideLoadingIcon();
                mFavoritiesActivityView.showErrorFooterView();
            }
        });
    }

    @Override
    public void requestMoreData(Context context) {
        mFavoriteListModel.favoritesNextPage(context, new FavoriteListModel.OnDataFinishedListener() {
            @Override
            public void noMoreDate() {
                mFavoritiesActivityView.showEndFooterView();
            }

            @Override
            public void onDataFinish(ArrayList<Status> list) {
                mFavoritiesActivityView.hideFooterView();
                mFavoritiesActivityView.updateListView(list);
            }

            @Override
            public void onError(String error) {
                mFavoritiesActivityView.showErrorFooterView();
            }
        });
    }
}

package com.wenming.weiswift.mvp.presenter.imp;

import android.content.Context;

import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.mvp.model.HotWeiBoModel;
import com.wenming.weiswift.mvp.model.imp.HotWeiBoModelImp;
import com.wenming.weiswift.mvp.presenter.HotWeiBoPresent;
import com.wenming.weiswift.mvp.view.HotWeiBoActivityView;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/16.
 */
public class HotWeiBoPresentImp implements HotWeiBoPresent {

    private HotWeiBoModel mHotWeiBoModel;
    private HotWeiBoActivityView mHotWeiBoActivityView;

    public HotWeiBoPresentImp(HotWeiBoActivityView hotWeiBoActivityView) {
        this.mHotWeiBoActivityView = hotWeiBoActivityView;
        mHotWeiBoModel = new HotWeiBoModelImp();
    }

    @Override
    public void pullToRefreshData(Context context) {
        mHotWeiBoActivityView.showLoadingIcon();
        mHotWeiBoModel.getLatestComment(context, new HotWeiBoModel.OnDataFinishedListener() {
            @Override
            public void noMoreDate() {
                mHotWeiBoActivityView.hideLoadingIcon();

            }

            @Override
            public void onDataFinish(ArrayList<Status> list) {
                mHotWeiBoActivityView.hideLoadingIcon();
                mHotWeiBoActivityView.updateListView(list);
            }

            @Override
            public void onError(String error) {
                mHotWeiBoActivityView.hideLoadingIcon();
                mHotWeiBoActivityView.showErrorFooterView();
            }
        });
    }

    @Override
    public void requestMoreData(Context context) {
        mHotWeiBoModel.getNextPageComment(context, new HotWeiBoModel.OnDataFinishedListener() {
            @Override
            public void noMoreDate() {
                mHotWeiBoActivityView.showEndFooterView();
            }

            @Override
            public void onDataFinish(ArrayList<Status> list) {
                mHotWeiBoActivityView.hideFooterView();
                mHotWeiBoActivityView.updateListView(list);
            }

            @Override
            public void onError(String error) {
                mHotWeiBoActivityView.showErrorFooterView();
            }
        });
    }
}

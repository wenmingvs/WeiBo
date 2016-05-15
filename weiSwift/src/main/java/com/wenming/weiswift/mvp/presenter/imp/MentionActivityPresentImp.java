package com.wenming.weiswift.mvp.presenter.imp;

import android.content.Context;

import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.mvp.model.MentionModel;
import com.wenming.weiswift.mvp.model.imp.MentionModelImp;
import com.wenming.weiswift.mvp.presenter.MentionActivityPresent;
import com.wenming.weiswift.mvp.view.MentionActivityView;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/15.
 */
public class MentionActivityPresentImp implements MentionActivityPresent {

    private MentionActivityView mMentionActivityView;
    private MentionModel mMentionModel;

    public MentionActivityPresentImp(MentionActivityView MentionActivityView) {
        this.mMentionActivityView = MentionActivityView;
        this.mMentionModel = new MentionModelImp();
    }

    @Override
    public void pullToRefreshData(Context context) {
        mMentionActivityView.showLoadingIcon();
        mMentionModel.getLatestMention(context, new MentionModel.OnDataFinishedListener() {
            @Override
            public void noMoreDate() {
                mMentionActivityView.hideLoadingIcon();

            }

            @Override
            public void onDataFinish(ArrayList<Status> statuslist) {
                mMentionActivityView.hideLoadingIcon();
                mMentionActivityView.updateListView(statuslist);
            }

            @Override
            public void onError(String error) {
                mMentionActivityView.hideLoadingIcon();
                mMentionActivityView.showErrorFooterView();
            }
        });
    }

    @Override
    public void requestMoreData(Context context) {
        mMentionModel.getNextPageMention(context, new MentionModel.OnDataFinishedListener() {
            @Override
            public void noMoreDate() {
                mMentionActivityView.showEndFooterView();
            }

            @Override
            public void onDataFinish(ArrayList<Status> statuslist) {
                mMentionActivityView.hideFooterView();
                mMentionActivityView.updateListView(statuslist);
            }

            @Override
            public void onError(String error) {
                mMentionActivityView.showErrorFooterView();
            }
        });
    }
}

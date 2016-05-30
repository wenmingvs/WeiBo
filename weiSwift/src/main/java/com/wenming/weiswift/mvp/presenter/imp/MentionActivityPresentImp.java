package com.wenming.weiswift.mvp.presenter.imp;

import android.content.Context;

import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.mvp.model.CommentModel;
import com.wenming.weiswift.mvp.model.MentionModel;
import com.wenming.weiswift.mvp.model.imp.CommentModelImp;
import com.wenming.weiswift.mvp.model.imp.MentionModelImp;
import com.wenming.weiswift.mvp.presenter.MentionActivityPresent;
import com.wenming.weiswift.mvp.view.MentionActivityView;
import com.wenming.weiswift.ui.common.login.Constants;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/15.
 */
public class MentionActivityPresentImp implements MentionActivityPresent {

    private MentionActivityView mMentionActivityView;
    private MentionModel mMentionModel;
    private CommentModel mCommentModel;

    public MentionActivityPresentImp(MentionActivityView MentionActivityView) {
        this.mMentionActivityView = MentionActivityView;
        this.mMentionModel = new MentionModelImp();
        this.mCommentModel = new CommentModelImp();
    }

    @Override
    public void pullToRefreshData(int groupId, Context context) {
        mMentionActivityView.showLoadingIcon();
        if (groupId == Constants.GROUP_RETWEET_TYPE_ALL || groupId == Constants.GROUP_RETWEET_TYPE_FRIENDS || groupId == Constants.GROUP_RETWEET_TYPE_ORIGINWEIBO) {
            mMentionModel.mentions(groupId, context, pullToRefreshListener);
        }
    }

    @Override
    public void requestMoreData(int groupId, Context context) {
        if (groupId == Constants.GROUP_RETWEET_TYPE_ALL || groupId == Constants.GROUP_RETWEET_TYPE_FRIENDS || groupId == Constants.GROUP_RETWEET_TYPE_ORIGINWEIBO) {
            mMentionModel.mentionsNextPage(groupId, context, requesetMoreDataListener);
        }
    }


    public MentionModel.OnDataFinishedListener pullToRefreshListener = new MentionModel.OnDataFinishedListener() {
        @Override
        public void noMoreDate() {
            mMentionActivityView.hideLoadingIcon();
        }

        @Override
        public void onDataFinish(ArrayList<Status> statuslist) {
            mMentionActivityView.hideLoadingIcon();
            mMentionActivityView.scrollToTop(false);
            mMentionActivityView.updateListView(statuslist);
        }

        @Override
        public void onError(String error) {
            mMentionActivityView.hideLoadingIcon();
            mMentionActivityView.showErrorFooterView();
        }
    };

    public MentionModel.OnDataFinishedListener requesetMoreDataListener = new MentionModel.OnDataFinishedListener() {
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
    };


}

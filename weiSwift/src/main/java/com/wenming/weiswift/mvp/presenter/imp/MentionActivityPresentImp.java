package com.wenming.weiswift.mvp.presenter.imp;

import android.content.Context;

import com.wenming.weiswift.entity.Comment;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.mvp.model.MentionModel;
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


    public MentionActivityPresentImp(MentionActivityView MentionActivityView) {
        this.mMentionActivityView = MentionActivityView;
        this.mMentionModel = new MentionModelImp();
    }

    @Override
    public void pullToRefreshData(int groupId, Context context) {
        mMentionActivityView.showLoadingIcon();
        if (groupId == Constants.GROUP_RETWEET_TYPE_ALL || groupId == Constants.GROUP_RETWEET_TYPE_FRIENDS || groupId == Constants.GROUP_RETWEET_TYPE_ORIGINWEIBO) {
            mMentionModel.mentions(groupId, context, pullToRefresh_Mention_Listener);
        } else if (groupId == Constants.GROUP_RETWEET_TYPE_ALLCOMMENT || groupId == Constants.GROUP_RETWEET_TYPE_FRIEDNSCOMMENT) {
            mMentionModel.commentMentions(groupId, context, pullToRefresh_Comment_Listener);
        }
    }

    @Override
    public void requestMoreData(int groupId, Context context) {
        if (groupId == Constants.GROUP_RETWEET_TYPE_ALL || groupId == Constants.GROUP_RETWEET_TYPE_FRIENDS || groupId == Constants.GROUP_RETWEET_TYPE_ORIGINWEIBO) {
            mMentionModel.mentionsNextPage(groupId, context, requesetMoreData_Mention_Listener);
        } else if (groupId == Constants.GROUP_RETWEET_TYPE_ALLCOMMENT || groupId == Constants.GROUP_RETWEET_TYPE_FRIEDNSCOMMENT) {
            mMentionModel.commentMentionsNextPage(groupId, context, requesetMoreData_Comment_Listener);
        }
    }


    public MentionModel.OnMentionFinishedListener pullToRefresh_Mention_Listener = new MentionModel.OnMentionFinishedListener() {
        @Override
        public void noMoreDate() {
            mMentionActivityView.hideLoadingIcon();
        }

        @Override
        public void onDataFinish(ArrayList<Status> mentionlist) {
            mMentionActivityView.hideLoadingIcon();
            mMentionActivityView.scrollToTop(false);
            mMentionActivityView.updateMentionListView(mentionlist, true);
        }

        @Override
        public void onError(String error) {
            mMentionActivityView.hideLoadingIcon();
            mMentionActivityView.showErrorFooterView();
        }
    };

    public MentionModel.OnMentionFinishedListener requesetMoreData_Mention_Listener = new MentionModel.OnMentionFinishedListener() {
        @Override
        public void noMoreDate() {
            mMentionActivityView.showEndFooterView();
        }

        @Override
        public void onDataFinish(ArrayList<Status> mentionlist) {
            mMentionActivityView.hideFooterView();
            mMentionActivityView.updateMentionListView(mentionlist, false);
        }

        @Override
        public void onError(String error) {
            mMentionActivityView.showErrorFooterView();
        }
    };

    public MentionModel.OnCommentFinishedListener pullToRefresh_Comment_Listener = new MentionModel.OnCommentFinishedListener() {
        @Override
        public void noMoreDate() {
            mMentionActivityView.hideLoadingIcon();
        }

        @Override
        public void onDataFinish(ArrayList<Comment> commentlist) {
            mMentionActivityView.hideLoadingIcon();
            mMentionActivityView.scrollToTop(false);
            mMentionActivityView.updateCommentListView(commentlist, true);
        }

        @Override
        public void onError(String error) {
            mMentionActivityView.hideLoadingIcon();
            mMentionActivityView.showErrorFooterView();
        }
    };

    public MentionModel.OnCommentFinishedListener requesetMoreData_Comment_Listener = new MentionModel.OnCommentFinishedListener() {
        @Override
        public void noMoreDate() {
            mMentionActivityView.showEndFooterView();
        }

        @Override
        public void onDataFinish(ArrayList<Comment> commentlist) {
            mMentionActivityView.hideFooterView();
            mMentionActivityView.updateCommentListView(commentlist, false);
        }

        @Override
        public void onError(String error) {
            mMentionActivityView.showErrorFooterView();
        }
    };


}

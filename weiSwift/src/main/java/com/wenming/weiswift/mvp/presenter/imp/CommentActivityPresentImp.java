package com.wenming.weiswift.mvp.presenter.imp;

import android.content.Context;

import com.wenming.weiswift.entity.Comment;
import com.wenming.weiswift.mvp.model.CommentModel;
import com.wenming.weiswift.mvp.model.imp.CommentModelImp;
import com.wenming.weiswift.mvp.presenter.CommentActivityPresent;
import com.wenming.weiswift.mvp.view.CommentActivityView;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/15.
 */
public class CommentActivityPresentImp implements CommentActivityPresent {

    private CommentModel mCommentModel;
    private CommentActivityView mCommentActivityView;


    public CommentActivityPresentImp(CommentActivityView commentActivityView) {
        this.mCommentActivityView = commentActivityView;
        this.mCommentModel = new CommentModelImp();
    }

    @Override
    public void pullToRefreshData(Context context) {
        mCommentActivityView.showLoadingIcon();
        mCommentModel.toMe(context, new CommentModel.OnDataFinishedListener() {
            @Override
            public void noMoreDate() {
                mCommentActivityView.hideLoadingIcon();

            }

            @Override
            public void onDataFinish(ArrayList<Comment> commentlist) {
                mCommentActivityView.hideLoadingIcon();
                mCommentActivityView.updateListView(commentlist);
            }

            @Override
            public void onError(String error) {
                mCommentActivityView.hideLoadingIcon();
                mCommentActivityView.showErrorFooterView();
            }
        });
    }

    @Override
    public void requestMoreData(Context context) {
        mCommentModel.toMeNextPage(context, new CommentModel.OnDataFinishedListener() {
            @Override
            public void noMoreDate() {
                mCommentActivityView.showEndFooterView();
            }

            @Override
            public void onDataFinish(ArrayList<Comment> commentlist) {
                mCommentActivityView.hideFooterView();
                mCommentActivityView.updateListView(commentlist);
            }

            @Override
            public void onError(String error) {
                mCommentActivityView.showErrorFooterView();
            }
        });
    }
}

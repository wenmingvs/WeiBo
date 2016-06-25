package com.wenming.weiswift.mvp.presenter.imp;

import android.content.Context;

import com.wenming.weiswift.entity.Comment;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.mvp.model.StatusDetailModel;
import com.wenming.weiswift.mvp.model.imp.StatusDetailModelImp;
import com.wenming.weiswift.mvp.presenter.DetailActivityPresent;
import com.wenming.weiswift.ui.login.fragment.home.weiboitemdetail.activity.BaseActivity;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/6/26.
 */
public class DetailActivityPresentImp implements DetailActivityPresent {

    private BaseActivity baseActivity;
    private StatusDetailModel statusDetailModel;

    public DetailActivityPresentImp(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
        this.statusDetailModel = new StatusDetailModelImp();
    }

    @Override
    public void pullToRefreshData(int groupId, Status status, Context context) {
        switch (groupId) {
            case StatusDetailModelImp.COMMENT_PAGE:
                statusDetailModel.comment(groupId, status, context, new StatusDetailModel.OnCommentCallBack() {
                    @Override
                    public void noMoreDate() {
                        baseActivity.hideLoadingIcon();
                    }

                    @Override
                    public void onDataFinish(ArrayList<Comment> commentlist) {
                        baseActivity.hideLoadingIcon();
                        //baseActivity.scrollToTop(false);
                        baseActivity.updateCommentListView(commentlist, true);
                    }

                    @Override
                    public void onError(String error) {
                        baseActivity.hideLoadingIcon();
                        baseActivity.showErrorFooterView();
                    }
                });
                break;
            case StatusDetailModelImp.REPOST_PAGE:
                statusDetailModel.repost(groupId, status, context, new StatusDetailModel.OnRepostCallBack() {
                    @Override
                    public void noMoreDate() {
                        baseActivity.hideLoadingIcon();
                    }

                    @Override
                    public void onDataFinish(ArrayList<Status> repostList) {
                        baseActivity.hideLoadingIcon();
                        //baseActivity.scrollToTop(false);
                        baseActivity.updateRepostListView(repostList, true);
                    }

                    @Override
                    public void onError(String error) {
                        baseActivity.hideLoadingIcon();
                        baseActivity.showErrorFooterView();
                    }
                });
                break;
        }
    }

    @Override
    public void requestMoreData(int groupId, Status status, Context context) {
        switch (groupId) {
            case StatusDetailModelImp.COMMENT_PAGE:
                statusDetailModel.commentNextPage(groupId, status, context, new StatusDetailModel.OnCommentCallBack() {
                    @Override
                    public void noMoreDate() {
                        baseActivity.hideFooterView();
                    }

                    @Override
                    public void onDataFinish(ArrayList<Comment> commentlist) {
                        baseActivity.hideFooterView();
                        baseActivity.updateCommentListView(commentlist, false);
                    }

                    @Override
                    public void onError(String error) {
                        baseActivity.showErrorFooterView();
                    }
                });
                break;
            case StatusDetailModelImp.REPOST_PAGE:
                statusDetailModel.repostNextPage(groupId, status, context, new StatusDetailModel.OnRepostCallBack() {

                    @Override
                    public void noMoreDate() {
                        baseActivity.hideFooterView();
                    }

                    @Override
                    public void onDataFinish(ArrayList<Status> repostList) {
                        baseActivity.hideFooterView();
                        baseActivity.updateRepostListView(repostList, false);
                    }

                    @Override
                    public void onError(String error) {
                        baseActivity.showErrorFooterView();
                    }
                });
                break;
        }
    }
}

package com.wenming.weiswift.app.timeline.data.viewholder.base;

import android.text.TextUtils;

import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.entity.Status;

/**
 * Created by wenmingvs on 2017/7/25.
 */

public abstract class BaseTimeLinePresenter implements BaseTimeLineContract.Presenter {
    private BaseTimeLineContract.View mView;
    private Status mDataModel;


    public BaseTimeLinePresenter(BaseTimeLineContract.View view, Status dataModel) {
        this.mView = view;
        this.mDataModel = dataModel;
        this.mView.setPresenter(this);
    }

    @Override
    public void start() {
        updateView();
    }

    private void updateView() {
        //设置头像
        setAvatar();
        //设置昵称与备注
        setTopBarNickName();
        //设置微博时间
        mView.setTopBarCreateTime(mDataModel.created_at);
        //设置微博来源
        setTopBarSourceFrom();
        //设置转发数
        setRetweetCount();
        //设置评论数
        setCommendCount();
        //设置点赞数
        setLikeCount();
    }

    private void setAvatar() {
        mView.setTopBarAvatar(mDataModel.user.avatar_hd);
        //设置头像认证
        boolean verifty = mDataModel.user.verified;
        int type = mDataModel.user.verified_type;
        if (verifty && type == 0) {
            mView.setTopBarIdentRes(R.drawable.avatar_vip);
        } else if (verifty && (type == 1 || type == 2 || type == 3)) {
            mView.setTopBarIdentRes(R.drawable.avatar_enterprise_vip);
        } else if (!verifty && type == 220) {
            mView.setTopBarIdentRes(R.drawable.avatar_grassroot);
        } else {
            mView.hideTopBarIden();
        }
    }

    /**
     * 设置昵称与备注
     */
    private void setTopBarNickName() {
        if (mDataModel.user == null) {
            return;
        }
        if (!TextUtils.isEmpty(mDataModel.user.remark)) {
            mView.setTopBarName(mDataModel.user.remark);
        } else {
            mView.setTopBarName(mDataModel.user.name);
        }
    }

    /**
     * 设置微博来源
     */
    private void setTopBarSourceFrom() {
        if (mDataModel == null || TextUtils.isEmpty(mDataModel.source)) {
            mView.hideTopBarSourceFrom();
        } else {
            mView.setTopBarSourceFrom(mDataModel.source);
        }
    }

    /**
     * 设置点赞数
     */
    private void setLikeCount() {
        if (mDataModel.attitudes_count != 0) {
            mView.setBottomBarLikeCount(mDataModel.attitudes_count);
        } else {
            mView.setDefaultLikeContent();
        }
    }

    /**
     * 设置评论数
     */
    private void setCommendCount() {
        if (mDataModel.comments_count != 0) {
            mView.setBottomBarCommentCount(mDataModel.comments_count);
        } else {
            mView.setDefaultCommentContent();
        }
    }

    /**
     * 设置转发数
     */
    private void setRetweetCount() {
        if (mDataModel.reposts_count != 0) {
            mView.setBottomBarRetweetCount(mDataModel.reposts_count);
        } else {
            mView.setDefaultRetweetContent();
        }
    }

    @Override
    public void goToStatusDetailActivity() {
        if (mDataModel.retweeted_status.user != null) {
            mView.goToStatusDetailActivity(mDataModel);
        }
    }
}

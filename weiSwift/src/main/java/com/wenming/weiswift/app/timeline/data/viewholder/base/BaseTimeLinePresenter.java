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
        mView.setTopBarAvatar(mDataModel.user.avatar_hd);
        //设置认证
        setTopBarIdentication();
        //设置名称
        setTopBarName();
        //设置时间
        mView.setTopBarCreateTime(mDataModel.created_at);
        //设置来源
        setTopBarSourceFrom();
    }

    private void setTopBarSourceFrom() {
        if (mDataModel == null || TextUtils.isEmpty(mDataModel.source)) {
            mView.hideTopBarSourceFrom();
        } else {
            mView.setTopBarSourceFrom(mDataModel.source);
        }
    }

    private void setTopBarIdentication() {
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

    private void setTopBarName() {
        if (mDataModel.user == null) {
            return;
        }
        if (!TextUtils.isEmpty(mDataModel.user.remark)) {
            mView.setTopBarName(mDataModel.user.remark);
        } else {
            mView.setTopBarName(mDataModel.user.name);
        }
    }
}

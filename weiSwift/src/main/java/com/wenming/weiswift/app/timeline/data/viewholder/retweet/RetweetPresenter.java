package com.wenming.weiswift.app.timeline.data.viewholder.retweet;

import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.timeline.data.viewholder.base.BaseTimeLinePresenter;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 2017/7/25.
 */

public class RetweetPresenter extends BaseTimeLinePresenter implements RetweetContract.Presenter {
    private RetweetContract.View mView;
    private Status mDataModel;

    public RetweetPresenter(RetweetContract.View view, Status dataModel) {
        super(view, dataModel);
        this.mView = view;
        this.mDataModel = dataModel;
        this.mView.setPresenter(this);
    }

    @Override
    protected void updateView() {
        super.updateView();
        setRetweetContent();
        setOriginContent();
        setOriginImgListContent();
    }

    private void setRetweetContent() {
        mView.setRetweetText(mDataModel.text);
    }

    private void setOriginContent() {
        if (mDataModel.retweeted_status.user != null) {
            StringBuffer buffer = new StringBuffer();
            buffer.setLength(0);
            buffer.append("@");
            buffer.append(mDataModel.retweeted_status.user.name + " :  ");
            buffer.append(mDataModel.retweeted_status.text);
            mView.setOriginText(buffer.toString());
        } else {
            mView.setDeleteOriginText();
        }
    }

    private void setOriginImgListContent() {
        ArrayList<String> imageDatas = mDataModel.retweeted_status.bmiddle_pic_urls;
        if (imageDatas == null || imageDatas.size() == 0) {
            mView.setImgListVisible(false);
            return;
        }
        mView.setImgListVisible(true);
        mView.setImgListContent(mDataModel.retweeted_status);
    }

    @Override
    public void goToRetweetDetailActivity() {
        mView.goToRetweetDetailActivity(mDataModel);
    }
}

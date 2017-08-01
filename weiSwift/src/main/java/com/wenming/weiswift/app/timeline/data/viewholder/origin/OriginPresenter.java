package com.wenming.weiswift.app.timeline.data.viewholder.origin;

import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.timeline.data.viewholder.base.BaseTimeLinePresenter;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 2017/7/25.
 */

public class OriginPresenter extends BaseTimeLinePresenter implements OriginContract.Presenter {
    private OriginContract.View mView;
    private Status mDataModel;

    public OriginPresenter(OriginContract.View view, Status dataModel) {
        super(view, dataModel);
        this.mView = view;
        this.mDataModel = dataModel;
        this.mView.setPresenter(this);
    }

    @Override
    protected void updateView() {
        super.updateView();
        setText();
        setImgList();
    }

    private void setImgList() {
        ArrayList<String> imageDatas = mDataModel.bmiddle_pic_urls;
        if (imageDatas == null || imageDatas.size() == 0) {
            mView.setImgListVisible(false);
            return;
        }
        if (!mView.isImgListVisble()) {
            mView.setImgListVisible(true);
        }
        mView.setImgListContent(mDataModel);
    }

    private void setText() {
        mView.setImgListContent(mDataModel.text);
    }
}

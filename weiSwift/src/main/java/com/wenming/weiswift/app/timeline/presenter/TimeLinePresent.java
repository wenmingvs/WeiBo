package com.wenming.weiswift.app.timeline.presenter;

import com.wenming.weiswift.app.timeline.contract.TimeLineContract;
import com.wenming.weiswift.app.timeline.data.TimeLineDataSource;

/**
 * Created by wenmingvs on 16/5/14.
 */
public class TimeLinePresent implements TimeLineContract.Presenter {

    private TimeLineContract.View mView;
    private TimeLineDataSource mDataModel;

    public TimeLinePresent(TimeLineContract.View view, TimeLineDataSource dataModel) {
        this.mView = view;
        this.mDataModel = dataModel;
        this.mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void requestTimeLine() {

    }

    @Override
    public void requestHeaderTimeLine() {

    }

    @Override
    public void requestLastTimeLine() {

    }
}

package com.wenming.weiswift.app.timeline.contract;

import com.wenming.weiswift.app.common.base.mvp.BasePresenter;
import com.wenming.weiswift.app.common.base.mvp.BaseView;
import com.wenming.weiswift.app.common.entity.Status;

import java.util.List;

/**
 * Created by wenmingvs on 2017/7/23.
 */

public interface TimeLineContract {
    interface View extends BaseView<Presenter> {
        void setTimeLineList(List<Status> timeLineList);

        void addHeaderTimeLine(List<Status> timeLineList);

        void addLastTimeLine(List<Status> timeLineList);
    }

    interface Presenter extends BasePresenter {
        void requestTimeLine();

        void requestHeaderTimeLine();

        void requestLastTimeLine();
    }
}

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

        void showLoading();

        void dismissLoading();

        void showServerMessage(String error);

        void showNetWorkNotConnected();

        void showNetWorkTimeOut();

        void loadMoreComplete();

        void loadMoreFail();

        void loadMoreEnd();

        void scrollToTop();

        void showNewWeiboCount(int num);
    }

    interface Presenter extends BasePresenter {
        /**
         * 首次请求
         */
        void refreshTimeLine(List<Status> timeLineList);

        /**
         * 加载更多
         */
        void loadMoreTimeLine(List<Status> timeLineList);
    }
}

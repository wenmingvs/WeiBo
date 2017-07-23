package com.wenming.weiswift.app.home.contract;

import com.wenming.weiswift.app.common.base.mvp.BasePresenter;
import com.wenming.weiswift.app.common.base.mvp.BaseView;
import com.wenming.weiswift.app.home.data.entity.Group;

import java.util.List;

/**
 * Created by wenmingvs on 2017/6/5.
 */

public interface HomeContract {
    interface View extends BaseView<Presenter> {
        void setGroupsList(List<Group> groups);

        void showServerMessage(String text);

        void showLoading();

        void dismissLoading();

        void showRetryBg();

        void hideRetryBg();

        void showNoneNetWork();
    }

    interface Presenter extends BasePresenter {
        void requestGroups();
    }
}

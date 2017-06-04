package com.wenming.weiswift.app.home.contract;

import com.wenming.weiswift.app.common.base.mvp.BasePresenter;
import com.wenming.weiswift.app.common.base.mvp.BaseView;

/**
 * Created by wenmingvs on 2017/6/5.
 */

public interface HomeContract {
    interface View extends BaseView<Presenter> {
        void initTabLayout();
    }

    interface Presenter extends BasePresenter {
        void requestGroups();
    }
}

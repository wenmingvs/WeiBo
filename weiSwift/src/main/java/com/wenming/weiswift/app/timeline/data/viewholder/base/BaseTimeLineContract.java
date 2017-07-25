package com.wenming.weiswift.app.timeline.data.viewholder.base;

import com.wenming.weiswift.app.common.base.mvp.BasePresenter;
import com.wenming.weiswift.app.common.base.mvp.BaseView;

/**
 * Created by wenmingvs on 2017/7/25.
 */

public interface BaseTimeLineContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}

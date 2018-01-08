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
        /**
         * 设置分组
         * @param groups
         */
        void setGroupsList(List<Group> groups);

        /**
         * 展示后台下发的信息
         * @param text
         */
        void showServerMessage(String text);

        /**
         * 展示loading
         */
        void showLoading();

        /**
         * 隐藏loading
         */
        void dismissLoading();

        /**
         * 显示无网络
         */
        void showNoneNetWork();

        void showTimeOut();
    }

    interface Presenter extends BasePresenter {
        void requestGroups();
    }
}

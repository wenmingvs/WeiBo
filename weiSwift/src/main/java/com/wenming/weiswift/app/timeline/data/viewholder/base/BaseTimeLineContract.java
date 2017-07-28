package com.wenming.weiswift.app.timeline.data.viewholder.base;

import com.wenming.weiswift.app.common.base.mvp.BasePresenter;
import com.wenming.weiswift.app.common.base.mvp.BaseView;

/**
 * Created by wenmingvs on 2017/7/25.
 */

public interface BaseTimeLineContract {
    interface View extends BaseView<Presenter> {
        void setTopBarAvatar(String url);

        void setTopBarIdentRes(int resId);

        void setTopBarName(String name);

        void setTopBarCreateTime(String createAt);

        void setTopBarSourceFrom(String source);

        void setBottomBarRetweetNum(int retweetNum);

        void setBottomCommentNum(int commentNum);

        void setBottomLikeNum(int likeNum);

        void hideTopBarSourceFrom();

        void showTopBarSourceFrom();

        void hideTopBarIden();

        void showTopBarIden();
    }

    interface Presenter extends BasePresenter {

    }
}

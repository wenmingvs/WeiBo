package com.wenming.weiswift.app.timeline.data.viewholder.base;

import com.wenming.weiswift.app.common.base.mvp.BasePresenter;
import com.wenming.weiswift.app.common.base.mvp.BaseView;
import com.wenming.weiswift.app.common.entity.Status;

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

        void hideTopBarSourceFrom();

        void showTopBarSourceFrom();

        void hideTopBarIden();

        void showTopBarIden();

        void setBottomBarRetweetCount(int count);

        void setDefaultRetweetContent();

        void setBottomBarCommentCount(int count);

        void setDefaultCommentContent();

        void setBottomBarLikeCount(int count);

        void setDefaultLikeContent();

        void goToStatusDetailActivity(Status status);

        Presenter getPresenter();

        void resetView();
    }

    interface Presenter extends BasePresenter {

        void goToStatusDetailActivity();
        
        void reset();
    }
}

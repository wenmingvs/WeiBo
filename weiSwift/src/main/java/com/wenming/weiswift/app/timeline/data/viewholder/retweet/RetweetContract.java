package com.wenming.weiswift.app.timeline.data.viewholder.retweet;

import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.timeline.data.viewholder.base.BaseTimeLineContract;

/**
 * Created by wenmingvs on 2017/7/25.
 */

public class RetweetContract {
    interface View extends BaseTimeLineContract.View {

        void setOriginText(String text);

        void setDeleteOriginText();

        void setRetweetText(String text);

        void goToRetweetDetailActivity(Status status);

        void setImgListVisible(boolean visible);

        void setImgListContent(Status status);

        void setVideoImg(String videoImg);

        boolean isImgListVisble();
    }

    interface Presenter extends BaseTimeLineContract.Presenter {

        void goToRetweetDetailActivity();
    }
}

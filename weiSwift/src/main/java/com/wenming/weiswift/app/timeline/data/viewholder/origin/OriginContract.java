package com.wenming.weiswift.app.timeline.data.viewholder.origin;

import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.timeline.data.viewholder.base.BaseTimeLineContract;

/**
 * Created by wenmingvs on 2017/7/25.
 */

public interface OriginContract {
    interface View extends BaseTimeLineContract.View {
        void setImgListContent(String text);

        void setImgListContent(Status status);

        void setVideoImg(String videoImg);

        void setImgListVisible(boolean visible);

        boolean isImgListVisble();
    }

    interface Presenter extends BaseTimeLineContract.Presenter {

    }
}

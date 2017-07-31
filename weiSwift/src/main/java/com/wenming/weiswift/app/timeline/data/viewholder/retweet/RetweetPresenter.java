package com.wenming.weiswift.app.timeline.data.viewholder.retweet;

import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.timeline.data.viewholder.base.BaseTimeLineContract;
import com.wenming.weiswift.app.timeline.data.viewholder.base.BaseTimeLinePresenter;

/**
 * Created by wenmingvs on 2017/7/25.
 */

public class RetweetPresenter extends BaseTimeLinePresenter implements RetweetContract.Presenter {

    public RetweetPresenter(BaseTimeLineContract.View view, Status dataModel) {
        super(view, dataModel);
    }

    @Override
    public void start() {

    }
}

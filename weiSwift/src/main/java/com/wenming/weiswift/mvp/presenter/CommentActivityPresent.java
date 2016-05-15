package com.wenming.weiswift.mvp.presenter;

import android.content.Context;

/**
 * Created by wenmingvs on 16/5/15.
 */
public interface CommentActivityPresent {
    public void pullToRefreshData(Context context);

    public void requestMoreData(Context context);
}

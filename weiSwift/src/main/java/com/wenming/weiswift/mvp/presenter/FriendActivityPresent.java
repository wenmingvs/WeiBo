package com.wenming.weiswift.mvp.presenter;

import android.content.Context;

/**
 * Created by wenmingvs on 16/5/16.
 */
public interface FriendActivityPresent {
    public void pullToRefreshData(long uid,Context context);

    public void requestMoreData(long uid,Context context);
}

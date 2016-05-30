package com.wenming.weiswift.mvp.presenter;

import android.content.Context;

/**
 * Created by wenmingvs on 16/5/16.
 */
public interface MyWeiBoActivityPresent {
    public void pullToRefreshData(long uid, int groupId, Context context);

    public void requestMoreData(long uid, int groupId, Context context);
}

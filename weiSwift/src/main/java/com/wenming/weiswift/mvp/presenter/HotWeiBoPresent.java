package com.wenming.weiswift.mvp.presenter;

import android.content.Context;

/**
 * Created by wenmingvs on 16/4/27.
 */
public interface HotWeiBoPresent {
    public void pullToRefreshData(Context context);

    public void requestMoreData(Context context);



}

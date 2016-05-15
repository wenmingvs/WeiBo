package com.wenming.weiswift.mvp.presenter;

import android.content.Context;

/**
 * Created by wenmingvs on 16/5/14.
 */
public interface HomeFragmentPresent {

    public void refreshUserName(Context context);

    public void pullToRefreshData(Context context);

    public void firstLoadData(Context context, boolean firstStart);

    public void requestMoreData(Context context);


}

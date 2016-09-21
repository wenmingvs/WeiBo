package com.wenming.weiswift.mvp.presenter;

import android.content.Context;

import com.wenming.weiswift.entity.Status;

/**
 * Created by wenmingvs on 16/6/26.
 */
public interface DetailActivityPresent {
    public void pullToRefreshData(int groupId, Status status, Context context);

    public void requestMoreData(int groupId, Status status, Context context);
}

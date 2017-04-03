package com.wenming.weiswift.app.mvp.presenter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenming.weiswift.app.common.entity.User;

/**
 * Created by wenmingvs on 16/5/16.
 */
public interface FriendActivityPresent {
    public void pullToRefreshData(long uid, Context context);

    public void requestMoreData(long uid, Context context);

    public void user_destroy(User user, Context context, ImageView friendIcon, TextView friendText);

    public void user_create(User user, Context context, ImageView friendIcon, TextView friendText);
}

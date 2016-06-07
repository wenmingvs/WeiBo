package com.wenming.weiswift.mvp.presenter;

import android.content.Context;

import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.entity.User;

/**
 * Created by xiangflight on 2016/4/22.
 */
public interface WeiBoArrowPresent {

    public void weibo_destroy(long id, Context context);

    public void user_destroy(User user, Context context);

    public void user_create(User user, Context context);

    public void createFavorite(Status status, Context context);

    public void cancalFavorite(Status status, Context context);

}

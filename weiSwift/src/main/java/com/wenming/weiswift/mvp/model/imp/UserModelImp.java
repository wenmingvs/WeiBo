package com.wenming.weiswift.mvp.model.imp;

import android.content.Context;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.wenming.weiswift.entity.User;
import com.wenming.weiswift.mvp.model.UserModel;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.common.login.Constants;

/**
 * Created by wenmingvs on 16/5/14.
 */
public class UserModelImp implements UserModel {


    @Override
    public void getUserName(Context context, final OnUserRequestFinish onUserRequestFinish) {
        long uid = Long.parseLong(AccessTokenKeeper.readAccessToken(context).getUid());
        UsersAPI mUsersAPI = new UsersAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mUsersAPI.show(uid, new RequestListener() {
            @Override
            public void onComplete(String response) {
                User user = User.parse(response);
                onUserRequestFinish.onComplete(user);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                onUserRequestFinish.onError(e.getMessage());
            }
        });
    }


}

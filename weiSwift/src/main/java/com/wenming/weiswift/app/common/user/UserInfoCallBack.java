package com.wenming.weiswift.app.common.user;

import com.wenming.weiswift.app.common.entity.User;

/**
 * Created by wenmingvs on 2017/8/12.
 */

public interface UserInfoCallBack {
    void onSuccess(User user);

    void onFail();
}

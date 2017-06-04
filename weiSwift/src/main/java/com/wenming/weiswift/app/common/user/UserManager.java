package com.wenming.weiswift.app.common.user;

import com.wenming.weiswift.app.common.ThreadHelper;
import com.wenming.weiswift.app.common.entity.User;

/**
 * Created by wenmingvs on 2017/4/3.
 */

public class UserManager {
    private static final String TAG = UserManager.class.getName();
    private static UserManager mInstance;
    private User mUser;

    private UserManager() {
    }

    public static UserManager getInstance() {
        if (mInstance == null) {
            synchronized (ThreadHelper.class) {
                if (mInstance == null) {
                    mInstance = new UserManager();
                }
            }
        }
        return mInstance;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }
}

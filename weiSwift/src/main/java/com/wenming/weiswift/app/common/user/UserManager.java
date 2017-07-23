package com.wenming.weiswift.app.common.user;

import com.wenming.weiswift.app.common.ThreadHelper;
import com.wenming.weiswift.app.common.entity.User;
import com.wenming.weiswift.app.common.oauth.AccessTokenManager;
import com.wenming.weiswift.app.common.preference.UserPrefences;
import com.wenming.weiswift.app.home.data.entity.Group;
import com.wenming.weiswift.app.home.data.entity.GroupList;

import java.util.List;

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

    public void setUserGroups(String jsonString) {
        UserPrefences.setUserGroups(AccessTokenManager.getInstance().getUid(), jsonString);
    }

    public List<Group> getUserGroups() {
        String jsonString = UserPrefences.getUserGroups(AccessTokenManager.getInstance().getUid());
        return GroupList.parse(jsonString).lists;
    }
}

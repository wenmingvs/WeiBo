package com.wenming.weiswift.app.common.user;

import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wenming.weiswift.app.common.ThreadHelper;
import com.wenming.weiswift.app.common.basenet.HttpManager;
import com.wenming.weiswift.app.common.constants.APIConstants;
import com.wenming.weiswift.app.common.entity.User;

import java.util.HashMap;
import java.util.Map;

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

    public void getUserInfo(String accessToken, String source, String uid, final UserInfoCallBack userInfoCallBack) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        if (!TextUtils.isEmpty(source) && !uid.equals("0")) {
            params.put("source", source);
        }
        if (!TextUtils.isEmpty(uid) && !uid.equals("0")) {
            params.put("uid", String.valueOf(uid));
        }
        HttpManager.getInstance().httpStringGetRequest(APIConstants.USER_SHOW, params, new Object(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mUser = User.parse(response);
                userInfoCallBack.onSuccess(mUser);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                userInfoCallBack.onFail();
            }
        });
    }
}

package com.wenming.weiswift.app.common.user;

import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wenming.weiswift.app.common.ThreadHelper;
import com.wenming.weiswift.app.common.basenet.HttpManager;
import com.wenming.weiswift.app.common.constants.APIConstants;
import com.wenming.weiswift.app.common.entity.User;
import com.wenming.weiswift.app.common.user.cache.UserInfoCacheConfig;
import com.wenming.weiswift.app.utils.TextSaveUtils;

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

    /**
     * 获取登录用户的信息
     *
     * @param accessToken
     * @param source
     * @param uid
     * @param userInfoCallBack
     */
    public void requestUserInfo(String accessToken, String source, final long uid, final UserInfoCallBack userInfoCallBack) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        if (!TextUtils.isEmpty(source)) {
            params.put("source", source);
        }
        if (uid != 0) {
            params.put("uid", String.valueOf(uid));
        }
        HttpManager.getInstance().httpStringGetRequest(APIConstants.USER_SHOW, params, new Object(), new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                mUser = User.parse(response);
                if (mUser != null) {
                    ThreadHelper.instance().runOnWorkThread(new ThreadHelper.Task() {
                        @Override
                        public void onRun() {
                            TextSaveUtils.write(UserInfoCacheConfig.getUserInfoDir(uid), UserInfoCacheConfig.FILE_USER_INFO, response);
                        }
                    });
                }
                userInfoCallBack.onSuccess(mUser);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                userInfoCallBack.onFail();
            }
        });
    }

    /**
     * 读取用户缓存信息
     */
    public void loadUserInfo(final long uid, final UserInfoCallBack callBack) {
        ThreadHelper.instance().runOnWorkThread(new ThreadHelper.Task() {
            @Override
            public void onRun() {
                String json = TextSaveUtils.read(UserInfoCacheConfig.getUserInfoDir(uid), UserInfoCacheConfig.FILE_USER_INFO);
                if (!TextUtils.isEmpty(json)) {
                    mUser = User.parse(json);
                    callBack.onSuccess(mUser);
                } else {
                    callBack.onFail();
                }
            }
        });
    }
}

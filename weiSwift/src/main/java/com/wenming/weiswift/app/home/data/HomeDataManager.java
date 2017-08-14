package com.wenming.weiswift.app.home.data;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wenming.weiswift.app.common.ThreadHelper;
import com.wenming.weiswift.app.home.cache.HomeCacheConfig;
import com.wenming.weiswift.app.home.data.entity.Group;
import com.wenming.weiswift.app.home.data.entity.GroupList;
import com.wenming.weiswift.app.home.net.HomeHttpHelper;
import com.wenming.weiswift.app.utils.TextSaveUtils;
import com.wenming.weiswift.utils.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by wenmingvs on 2017/6/5.
 */

public class HomeDataManager implements HomeDataSource {
    private Context mContext;
    private Object mRequestTag = new Object();

    public HomeDataManager(Context context) {
        this.mContext = context;
    }

    @Override
    public void loadGroupsCache(final long uid, final LoadCacheCallBack callBack) {
        ThreadHelper.instance().runOnWorkThread(new ThreadHelper.Task() {
            @Override
            public void onRun() {
                String json = TextSaveUtils.read(HomeCacheConfig.getGroupsDir(uid), HomeCacheConfig.FILE_GROUPS);
                if (!TextUtils.isEmpty(json)) {
                    callBack.onComplete(GroupList.parse(json).lists);
                } else {
                    callBack.onEmpty();
                }
            }
        });
    }

    @Override
    public void requestGroups(String accessToken, final long uid, final GroupCallBack callBack) {
        if (!NetUtil.isConnected(mContext)) {
            callBack.onNetWorkNotConnected();
            return;
        }
        HomeHttpHelper.getGroups(accessToken, mRequestTag, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.has("error")) {
                        List<Group> groups = GroupList.parse(response).lists;
                        //如果分组是有效的，缓存到本地
                        if (groups != null && groups.size() > 0) {
                            ThreadHelper.instance().runOnWorkThread(new ThreadHelper.Task() {
                                @Override
                                public void onRun() {
                                    TextSaveUtils.write(HomeCacheConfig.getGroupsDir(uid), HomeCacheConfig.FILE_GROUPS, response);
                                }
                            });
                        }
                        callBack.onSuccess(groups);
                    } else {
                        String errorString = jsonObject.optString("error");
                        callBack.onFail(errorString);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onFail(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    callBack.onTimeOut();
                }
                String body = null;
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                try {
                    body = new String(error.networkResponse.data,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                callBack.onFail(body);
            }
        });
    }
}

package com.wenming.weiswift.app.timeline.net;

import android.text.TextUtils;

import com.android.volley.Response;
import com.wenming.weiswift.app.common.ApplicationHelper;
import com.wenming.weiswift.app.common.basenet.HttpManager;
import com.wenming.weiswift.app.common.constants.APIConstants;
import com.wenming.weiswift.app.debug.DebugTool;
import com.wenming.weiswift.app.timeline.constants.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wenmingvs on 2017/7/24.
 */

public class TimeLineHttpHepler {

    /**
     * 获取当前登录用户及其所关注（授权）用户的最新微博
     *
     * @param token         采用OAuth授权方式为必填参数，OAuth授权后获得。
     * @param sinceId       若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
     * @param maxId         若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
     * @param count         单页返回的记录条数，最大不超过100，默认为20。
     * @param page          返回结果的页码，默认为1。
     * @param feature       过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
     * @param trimUser      返回值中user字段开关，0：返回完整user字段、1：user字段仅返回user_id，默认为0。
     * @param requesetTag   请求标示
     * @param listener      请求成功回调
     * @param errorListener 请求失败的回调
     */
    public static void getFriendsTimeLine(String token, long sinceId, long maxId, int count, int page, int feature, boolean trimUser,
                                          Object requesetTag, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        if (TextUtils.isEmpty(token)) {
            DebugTool.showToast(ApplicationHelper.getContext(), "token is empty");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("access_token", token);
        if (sinceId != 0) {
            params.put("since_id", String.valueOf(sinceId));
        }
        if (maxId != 0) {
            params.put("max_id", String.valueOf(maxId));
        }
        if (count != 0) {
            params.put("count", String.valueOf(count));
        }
        if (feature != 0) {
            params.put("feature", String.valueOf(feature));
        }
        if (trimUser) {
            params.put("trim_user", String.valueOf(1));
        } else {
            params.put("trim_user", String.valueOf(0));
        }
        HttpManager.getInstance().httpStringGetRequest(APIConstants.FRIENDS_TIMELINE, params, requesetTag, listener, errorListener);
    }

    /**
     * 获取当前登录用户某一好友分组的微博列表
     *
     * @param token         采用OAuth授权方式为必填参数，OAuth授权后获得。
     * @param sinceId       若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
     * @param maxId         若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
     * @param count         单页返回的记录条数，最大不超过100，默认为20。
     * @param page          返回结果的页码，默认为1。
     * @param feature       过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
     * @param trimUser      返回值中user字段开关，0：返回完整user字段、1：user字段仅返回user_id，默认为0。
     * @param requesetTag   请求标示
     * @param listener      请求成功回调
     * @param errorListener 请求失败的回调
     */
    public static void getGroupsTimeLine(String token, long groupId, long sinceId, long maxId, int count, int page, int feature, boolean trimUser,
                                         Object requesetTag, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        if (TextUtils.isEmpty(token) || groupId == 0) {
            DebugTool.showToast(ApplicationHelper.getContext(), "token is empty || gourpId == 0");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("access_token", token);
        if (groupId > 0) {
            params.put("list_id", String.valueOf(groupId));
        }
        if (sinceId != 0) {
            params.put("since_id", String.valueOf(sinceId));
        }
        if (maxId != 0) {
            params.put("max_id", String.valueOf(maxId));
        }
        if (count != 0) {
            params.put("count", String.valueOf(count));
        }
        if (feature != 0) {
            params.put("feature", String.valueOf(feature));
        }
        if (trimUser) {
            params.put("trim_user", String.valueOf(1));
        } else {
            params.put("trim_user", String.valueOf(0));
        }
        HttpManager.getInstance().httpStringGetRequest(APIConstants.GROUPS_TIMELINE, params, requesetTag, listener, errorListener);
    }

    /**
     * 将一个或多个短链接还原成原始的长链接
     *
     * @param token         采用OAuth授权方式为必填参数，OAuth授权后获得。
     * @param urlList       需要还原的短链接，需要URLencoded，最多不超过20个 。
     * @param requesetTag   请求标示
     * @param listener      请求成功回调
     * @param errorListener 请求失败的回调
     */
    public static void parseShortUrlList(String token, List<String> urlList, Object requesetTag, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", token);
        for (String url : urlList) {
            params.put("url_short", url);
        }
        HttpManager.getInstance().httpStringGetRequest(APIConstants.SHORT_URL_EXPAND, params, "url_short", urlList, requesetTag, listener, errorListener);
    }

    public static void getFriendsTimeLine(String token, Object requesetTag, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        getFriendsTimeLine(token, Constants.TIMELINE_DEFALUT_SINCE_ID, Constants.TIMELINE_DEFALUT_MAX_ID, Constants.TIMELINE_DEFALUT_COUNT,
                Constants.TIMELINE_DEFALUT_PAGE, Constants.TIMELINE_DEFALUT_FEATURE, Constants.TIMELINE_DEFALUT_TRIM_USER, requesetTag, listener, errorListener);
    }

    public static void getFriendsTimeLine(String token, long sinceId, long maxId, Object requesetTag, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        getFriendsTimeLine(token, sinceId, maxId, Constants.TIMELINE_DEFALUT_COUNT,
                Constants.TIMELINE_DEFALUT_PAGE, Constants.TIMELINE_DEFALUT_FEATURE, Constants.TIMELINE_DEFALUT_TRIM_USER, requesetTag, listener, errorListener);
    }

    public static void getGroupsTimeLine(String token, long groupId, Object requesetTag, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        getGroupsTimeLine(token, groupId, Constants.TIMELINE_DEFALUT_SINCE_ID, Constants.TIMELINE_DEFALUT_MAX_ID, Constants.TIMELINE_DEFALUT_COUNT,
                Constants.TIMELINE_DEFALUT_PAGE, Constants.TIMELINE_DEFALUT_FEATURE, Constants.TIMELINE_DEFALUT_TRIM_USER, requesetTag, listener, errorListener);
    }

    public static void getGroupsTimeLine(String token, long groupId, long sinceId, long maxId, Object requesetTag, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        getGroupsTimeLine(token, groupId, sinceId, maxId, Constants.TIMELINE_DEFALUT_COUNT,
                Constants.TIMELINE_DEFALUT_PAGE, Constants.TIMELINE_DEFALUT_FEATURE, Constants.TIMELINE_DEFALUT_TRIM_USER, requesetTag, listener, errorListener);
    }
}

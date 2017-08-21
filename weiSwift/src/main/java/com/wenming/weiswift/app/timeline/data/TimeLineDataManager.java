package com.wenming.weiswift.app.timeline.data;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wenming.weiswift.app.common.ApplicationHelper;
import com.wenming.weiswift.app.common.ThreadHelper;
import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.common.entity.list.StatusList;
import com.wenming.weiswift.app.debug.DebugTool;
import com.wenming.weiswift.app.timeline.cache.TimeLineCacheConfig;
import com.wenming.weiswift.app.timeline.constants.Constants;
import com.wenming.weiswift.app.timeline.data.entity.ShortUrlEntity;
import com.wenming.weiswift.app.timeline.net.TimeLineHttpHepler;
import com.wenming.weiswift.app.timeline.shorturl.ShortUrlManager;
import com.wenming.weiswift.app.utils.TextSaveUtils;
import com.wenming.weiswift.utils.NetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wenmingvs on 2017/7/23.
 */

public class TimeLineDataManager implements TimeLineDataSource {
    private Context mContext;
    private Object mRequestTag = new Object();

    public TimeLineDataManager(Context context) {
        this.mContext = context;
    }

    @Override
    public void loadFriendsTimeLineCache(final long uid, final LoadCacheCallBack callBack) {
        ThreadHelper.instance().runOnWorkThread(new ThreadHelper.Task() {
            @Override
            public void onRun() {
                String json = TextSaveUtils.read(TimeLineCacheConfig.getFriendsTimeLine(uid), TimeLineCacheConfig.FILE_FRIENDS_TIMELINE);
                if (!TextUtils.isEmpty(json)) {
                    callBack.onComplete(StatusList.parse(json).statuses);
                } else {
                    callBack.onEmpty();
                }
            }
        });
    }

    @Override
    public void loadGroupTimeLineCache(final long uid, final long groupId, final LoadCacheCallBack callBack) {
        ThreadHelper.instance().runOnWorkThread(new ThreadHelper.Task() {
            @Override
            public void onRun() {
                String json = TextSaveUtils.read(TimeLineCacheConfig.getGroupsTimeLineDir(uid),
                        TimeLineCacheConfig.FILE_GROUPS_TIMELINE_PRRFIX + String.valueOf(groupId) + TimeLineCacheConfig.FILE_GROUPS_TIMELINE_SUFFIX);
                if (!TextUtils.isEmpty(json)) {
                    callBack.onComplete(StatusList.parse(json).statuses);
                } else {
                    callBack.onEmpty();
                }
            }
        });
    }

    @Override
    public void refreshFriendsTimeLine(final long uid, final String accessToken, final RefreshTimeLineCallBack callBack) {
        if (!NetUtil.isConnected(mContext)) {
            callBack.onNetWorkNotConnected();
            return;
        }
        TimeLineHttpHepler.getFriendsTimeLine(accessToken, mRequestTag, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                StatusList statusList = StatusList.parse(response);
                final ArrayList<Status> timeLineList = statusList.statuses;
                if (timeLineList != null && timeLineList.size() > 0) {
                    ThreadHelper.instance().runOnWorkThread(new ThreadHelper.Task() {
                        @Override
                        public void onRun() {
                            TextSaveUtils.write(TimeLineCacheConfig.getFriendsTimeLine(uid), TimeLineCacheConfig.FILE_FRIENDS_TIMELINE, response);
                        }
                    });
                    parseShortUrl(accessToken, getShortUrlList(timeLineList), new ParseShortUrlCallBack() {
                        @Override
                        public void onSuccess() {
                            callBack.onSuccess(timeLineList);
                        }

                        @Override
                        public void onFail() {

                        }

                        @Override
                        public void onNetWorkNotConnected() {

                        }

                        @Override
                        public void onTimeOut() {

                        }
                    });
                } else {
                    callBack.onPullToRefreshEmpty();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (TextUtils.isEmpty(error.getCause().getMessage())) {
                    callBack.onTimeOut();
                }
            }
        });
    }

    @Override
    public void refreshFriendsTimeLine(final long uid, final String accessToken, long sinceId, final RefreshTimeLineCallBack callBack) {
        if (!NetUtil.isConnected(mContext)) {
            callBack.onNetWorkNotConnected();
            return;
        }
        TimeLineHttpHepler.getFriendsTimeLine(accessToken, Long.valueOf(sinceId), Constants.TIMELINE_DEFALUT_MAX_ID, mRequestTag, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                StatusList statusList = StatusList.parse(response);
                final ArrayList<Status> timeLineList = statusList.statuses;
                if (timeLineList != null && timeLineList.size() > 0) {
                    parseShortUrl(accessToken, getShortUrlList(timeLineList), new ParseShortUrlCallBack() {
                        @Override
                        public void onSuccess() {
                            callBack.onSuccess(timeLineList);
                        }

                        @Override
                        public void onFail() {

                        }

                        @Override
                        public void onNetWorkNotConnected() {

                        }

                        @Override
                        public void onTimeOut() {

                        }
                    });
                } else {
                    callBack.onPullToRefreshEmpty();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (TextUtils.isEmpty(error.getCause().getMessage())) {
                    callBack.onTimeOut();
                }
            }
        });
    }

    @Override
    public void loadMoreFriendsTimeLine(final String accessToken, long maxId, final LoadMoreTimeLineCallBack callBack) {
        if (!NetUtil.isConnected(mContext)) {
            callBack.onNetWorkNotConnected();
            return;
        }
        TimeLineHttpHepler.getFriendsTimeLine(accessToken, Constants.TIMELINE_DEFALUT_SINCE_ID, Long.valueOf(maxId), mRequestTag, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                handleLoadMoreResult(accessToken, response, callBack);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (TextUtils.isEmpty(error.getCause().getMessage())) {
                    callBack.onTimeOut();
                }
            }
        });
    }

    @Override
    public void refreshGroupTimeLine(final long uid, final String accessToken, final long groupId, final RefreshTimeLineCallBack callBack) {
        if (!NetUtil.isConnected(mContext)) {
            callBack.onNetWorkNotConnected();
            return;
        }
        TimeLineHttpHepler.getGroupsTimeLine(accessToken, groupId, mRequestTag, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                StatusList statusList = StatusList.parse(response);
                final ArrayList<Status> timeLineList = statusList.statuses;
                if (timeLineList != null && timeLineList.size() > 0) {
                    ThreadHelper.instance().runOnWorkThread(new ThreadHelper.Task() {
                        @Override
                        public void onRun() {
                            TextSaveUtils.write(TimeLineCacheConfig.getGroupsTimeLineDir(uid),
                                    TimeLineCacheConfig.FILE_GROUPS_TIMELINE_PRRFIX + String.valueOf(groupId) + TimeLineCacheConfig.FILE_GROUPS_TIMELINE_SUFFIX, response);
                        }
                    });
                    parseShortUrl(accessToken, getShortUrlList(timeLineList), new ParseShortUrlCallBack() {
                        @Override
                        public void onSuccess() {
                            callBack.onSuccess(timeLineList);
                        }

                        @Override
                        public void onFail() {

                        }

                        @Override
                        public void onNetWorkNotConnected() {

                        }

                        @Override
                        public void onTimeOut() {

                        }
                    });
                } else {
                    callBack.onPullToRefreshEmpty();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (TextUtils.isEmpty(error.getCause().getMessage())) {
                    callBack.onTimeOut();
                }
            }
        });
    }

    @Override
    public void refreshGroupTimeLine(final long uid, final String accessToken, final long groupId, long sinceId, final RefreshTimeLineCallBack callBack) {
        if (!NetUtil.isConnected(mContext)) {
            callBack.onNetWorkNotConnected();
            return;
        }
        TimeLineHttpHepler.getGroupsTimeLine(accessToken, groupId, sinceId, Constants.TIMELINE_DEFALUT_MAX_ID, mRequestTag, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                StatusList statusList = StatusList.parse(response);
                final ArrayList<Status> timeLineList = statusList.statuses;
                if (timeLineList != null && timeLineList.size() > 0) {
                    parseShortUrl(accessToken, getShortUrlList(timeLineList), new ParseShortUrlCallBack() {
                        @Override
                        public void onSuccess() {
                            callBack.onSuccess(timeLineList);
                        }

                        @Override
                        public void onFail() {

                        }

                        @Override
                        public void onNetWorkNotConnected() {

                        }

                        @Override
                        public void onTimeOut() {

                        }
                    });
                } else {
                    callBack.onPullToRefreshEmpty();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (TextUtils.isEmpty(error.getCause().getMessage())) {
                    callBack.onTimeOut();
                }
            }
        });
    }

    @Override
    public void loadMoreGroupTimeLine(final String accessToken, long groupId, long maxId, final LoadMoreTimeLineCallBack callBack) {
        if (!NetUtil.isConnected(mContext)) {
            callBack.onNetWorkNotConnected();
            return;
        }
        TimeLineHttpHepler.getGroupsTimeLine(accessToken, groupId, Constants.TIMELINE_DEFALUT_SINCE_ID, Long.valueOf(maxId), mRequestTag, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                handleLoadMoreResult(accessToken, response, callBack);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (TextUtils.isEmpty(error.getCause().getMessage())) {
                    callBack.onTimeOut();
                }
            }
        });
    }

    @Override
    public void parseShortUrl(String accessToken, List<String> urlList, final ParseShortUrlCallBack callBack) {
        if (!NetUtil.isConnected(mContext)) {
            callBack.onNetWorkNotConnected();
            return;
        }
        TimeLineHttpHepler.parseShortUrlList(accessToken, urlList, mRequestTag, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.has("urls")) {
                        DebugTool.showToast(ApplicationHelper.getContext(), "parseShortUrl error!!! not urls");
                        callBack.onFail();
                        return;
                    }
                    JSONArray urlArray = jsonObject.optJSONArray("urls");
                    String shortUrl, longUrl, result;
                    int type;
                    for (int i = 0; i < urlArray.length(); i++) {
                        JSONObject urlObject = urlArray.optJSONObject(i);
                        shortUrl = urlObject.optString("url_short");
                        longUrl = urlObject.optString("url_long");
                        type = urlObject.optInt("type");
                        result = urlObject.optString("result");
                        ShortUrlManager.getInstance().addShortUrl(shortUrl, new ShortUrlEntity(shortUrl, longUrl, type, result));
                    }
                    callBack.onSuccess();
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onFail();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callBack.onFail();
            }
        });
    }

    /**
     * 获取原创微博中的短链接
     *
     * @param statusList
     */
    private List<String> getShortUrlList(ArrayList<Status> statusList) {
        List<String> urlList = new ArrayList<>();
        Pattern pattern = Pattern.compile(Constants.SHROT_URL);
        String content = null;
        for (int i = 0; i < statusList.size(); i++) {
            //转发微博
            if (statusList.get(i).retweeted_status != null && statusList.get(i).retweeted_status.user != null) {
                content = statusList.get(i).retweeted_status.text;
            }
            //原创微博
            else if (statusList.get(i).user != null) {
                content = statusList.get(i).text;
            }
            Matcher matcher = pattern.matcher(content);
            while (matcher.find()) {
                urlList.add(matcher.group());
            }
        }
        return urlList;
    }

    private void handleLoadMoreResult(String accessToken, String response, final LoadMoreTimeLineCallBack callBack) {
        StatusList statusList = StatusList.parse(response);
        final ArrayList<Status> timeLineList = statusList.statuses;
        if (timeLineList != null && timeLineList.size() > 0) {
            //删掉第一条重复的微博
            timeLineList.remove(0);
            parseShortUrl(accessToken, getShortUrlList(timeLineList), new ParseShortUrlCallBack() {
                @Override
                public void onSuccess() {
                    callBack.onLoadMoreSuccess(timeLineList);
                }

                @Override
                public void onFail() {

                }

                @Override
                public void onNetWorkNotConnected() {

                }

                @Override
                public void onTimeOut() {

                }
            });
        } else {
            callBack.onLoadMoreEmpty();
        }
    }
}

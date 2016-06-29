package com.wenming.weiswift.mvp.model.imp;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.wenming.weiswift.api.FriendshipsAPI;
import com.wenming.weiswift.api.StatusesAPI;
import com.wenming.weiswift.api.UsersAPI;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.entity.Token;
import com.wenming.weiswift.entity.User;
import com.wenming.weiswift.entity.list.StatusList;
import com.wenming.weiswift.entity.list.TokenList;
import com.wenming.weiswift.entity.list.UserList;
import com.wenming.weiswift.mvp.model.UserModel;
import com.wenming.weiswift.ui.common.NewFeature;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.common.login.Constants;
import com.wenming.weiswift.utils.NetUtil;
import com.wenming.weiswift.utils.SDCardUtil;
import com.wenming.weiswift.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/14.
 */
public class UserModelImp implements UserModel {
    private ArrayList<Status> mStatusList = new ArrayList<>();
    private ArrayList<User> mUsersList = new ArrayList<>();
    private ArrayList<User> mUserArrayList;

    private OnStatusListFinishedListener mOnStatusListFinishedListener;
    private OnUserListRequestFinish mOnUserListRequestFinish;
    private OnUserDetailRequestFinish mOnUserDetailRequestFinish;
    private Context mContext;
    private int mCurrentGroup = Constants.GROUP_MYWEIBO_TYPE_ALL;
    private boolean mRefrshAll;

    private int mFollowersCursor;
    private int mFriendsCursor;
    private int mUserListType;
    private static final int FOLLOWERS_LISTS = 0x1;
    private static final int FRIENDS_LISTS = 0x2;

    /**
     * 根据用户ID获取用户信息。异步方法
     *
     * @param uid
     * @param context
     */
    @Override
    public void show(long uid, final Context context, final OnUserDetailRequestFinish onUserDetailRequestFinish) {
        UsersAPI mUsersAPI = new UsersAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        mOnUserDetailRequestFinish = onUserDetailRequestFinish;
        mUsersAPI.show(uid, user_PullToRefresh);
    }

    @Override
    public void show(String screenName, Context context, OnUserDetailRequestFinish onUserDetailRequestFinish) {
        UsersAPI mUsersAPI = new UsersAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        mOnUserDetailRequestFinish = onUserDetailRequestFinish;
        mUsersAPI.show(screenName, user_PullToRefresh);
    }

    /**
     * 根据用户ID获取用户信息。同步方法
     *
     * @param uid
     * @param context
     * @return
     */
    @Override
    public User showUserDetailSync(long uid, Context context) {
        UsersAPI mUsersAPI = new UsersAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        return User.parse(mUsersAPI.showSync(uid));
    }

    /**
     * 获取某个用户最新发表的微博列表。
     *
     * @param uid
     * @param groupId
     * @param context
     * @param onStatusFinishedListener
     */
    @Override
    public void userTimeline(long uid, int groupId, Context context, OnStatusListFinishedListener onStatusFinishedListener) {
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        mOnStatusListFinishedListener = onStatusFinishedListener;
        //long sinceId = checkout(groupId);
        mStatusesAPI.userTimeline(uid, 0, 0, NewFeature.GET_WEIBO_NUMS, 1, false, groupId, false, statuslist_PullToRefresh);
    }

    @Override
    public void userTimeline(String screenName, int groupId, Context context, OnStatusListFinishedListener onStatusFinishedListener) {
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        mOnStatusListFinishedListener = onStatusFinishedListener;
        //long sinceId = checkout(groupId);
        mStatusesAPI.userTimeline(screenName, 0, 0, NewFeature.GET_WEIBO_NUMS, 1, false, groupId, false, statuslist_PullToRefresh);
    }

    @Override
    public void userPhoto(String screenName, int groupId, Context context, OnStatusListFinishedListener onStatusFinishedListener) {
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        mOnStatusListFinishedListener = onStatusFinishedListener;
        //long sinceId = checkout(groupId);
        mStatusesAPI.userTimeline(screenName, 0, 0, 50, 1, false, StatusesAPI.FEATURE_PICTURE, false, statuslist_PullToRefresh);
    }

    /**
     * 获取用户的粉丝列表(最多返回5000条数据)。
     *
     * @param uid
     * @param context
     * @param onUserListRequestFinish
     */
    @Override
    public void followers(long uid, Context context, OnUserListRequestFinish onUserListRequestFinish) {
        FriendshipsAPI mFriendshipsAPI = new FriendshipsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mUserListType = FOLLOWERS_LISTS;
        mContext = context;
        mOnUserListRequestFinish = onUserListRequestFinish;
        mFriendshipsAPI.followers(uid, NewFeature.GET_FOLLOWER_NUM, 0, false, userlist_PullToRefresh);
    }

    /**
     * 获取用户的关注列表。
     *
     * @param uid
     * @param context
     * @param onUserListRequestFinish
     */
    @Override
    public void friends(long uid, Context context, OnUserListRequestFinish onUserListRequestFinish) {
        FriendshipsAPI mFriendshipsAPI = new FriendshipsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mUserListType = FRIENDS_LISTS;
        mContext = context;
        mOnUserListRequestFinish = onUserListRequestFinish;
        mFriendshipsAPI.friends(uid, NewFeature.GET_FRIENDS_NUM, 0, false, userlist_PullToRefresh);
    }

    @Override
    public void userTimelineNextPage(long uid, int groupId, Context context, OnStatusListFinishedListener onStatusFinishedListener) {
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        mOnStatusListFinishedListener = onStatusFinishedListener;
        mStatusesAPI.userTimeline(uid, 0, Long.valueOf(mStatusList.get(mStatusList.size() - 1).id), NewFeature.LOADMORE_WEIBO_ITEM, 1, false, groupId, false, statuslist_NextPage);
    }

    @Override
    public void userTimelineNextPage(String screenName, int groupId, Context context, OnStatusListFinishedListener onStatusFinishedListener) {
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        mOnStatusListFinishedListener = onStatusFinishedListener;
        mStatusesAPI.userTimeline(screenName, 0, Long.valueOf(mStatusList.get(mStatusList.size() - 1).id), NewFeature.LOADMORE_WEIBO_ITEM, 1, false, groupId, false, statuslist_NextPage);
    }

    @Override
    public void userPhotoNextPage(String screenName, int groupId, Context context, OnStatusListFinishedListener onStatusFinishedListener) {
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        mOnStatusListFinishedListener = onStatusFinishedListener;
        mStatusesAPI.userTimeline(screenName, 0, Long.valueOf(mStatusList.get(mStatusList.size() - 1).id), 50, 1, false, StatusesAPI.FEATURE_PICTURE, false, statuslist_NextPage);
    }


    @Override
    public void followersNextPage(long uid, Context context, OnUserListRequestFinish onUserListRequestFinish) {
        FriendshipsAPI mFriendshipsAPI = new FriendshipsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        mOnUserListRequestFinish = onUserListRequestFinish;
        mFriendshipsAPI.followers(uid, NewFeature.LOADMORE_FOLLOWER_NUM, mFollowersCursor, false, userlist_NextPage);
    }


    @Override
    public void friendsNextPage(long uid, Context context, OnUserListRequestFinish onUserListRequestFinish) {
        FriendshipsAPI mFriendshipsAPI = new FriendshipsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        mOnUserListRequestFinish = onUserListRequestFinish;
        mFriendshipsAPI.friends(uid, NewFeature.LOADMORE_FRIENDS_NUM, mFriendsCursor, false, userlist_NextPage);
    }

    /**
     * 获取登录用户的详细信息
     *
     * @param context
     * @param onUserListRequestFinish
     */
    @Override
    public void getUserDetailList(final Context context, final OnUserListRequestFinish onUserListRequestFinish) {
        String jsonstring = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift", "登录列表缓存.txt");
        if (jsonstring == null && AccessTokenKeeper.readAccessToken(context).isSessionValid()) {
            cacheCurrentOuthToken(context);
        }
        TokenList tokenList = TokenList.parse(SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift", "登录列表缓存.txt"));
        if (tokenList == null) {
            //如果本地没有缓存的话，就添加自己一个用户就好了，然后再保存到本地
            return;
        }
        final ArrayList<Token> tokenArrayList = tokenList.tokenList;
        if (tokenArrayList == null || tokenArrayList.size() == 0) {
            return;
        }
        if (!NetUtil.isConnected(context)) {
            onUserListRequestFinish.onError("无法连接网络");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                mUserArrayList = new ArrayList<User>();
                for (Token token : tokenArrayList) {
                    mUserArrayList.add(showUserDetailSync(Long.valueOf(token.uid), context));
                }
                onUserListRequestFinish.onDataFinish(mUserArrayList);
            }
        }).start();
    }

    @Override
    public void deleteUserByUid(long uid, Context context, OnUserDeleteListener onUserDeleteListener) {
        int i = 0;
        for (i = 0; i < mUserArrayList.size(); i++) {
            if (mUserArrayList.get(i).id.equals(String.valueOf(uid))) {
                mUserArrayList.remove(i);
                i--;
                break;
            }
        }
        if (mUserArrayList.size() == 0) {
            onUserDeleteListener.onEmpty();
            return;
        }
        if (i >= mUserArrayList.size()) {
            onUserDeleteListener.onError("没有找到对应的账户");
        } else {
            onUserDeleteListener.onSuccess(mUserArrayList);
        }
    }

    @Override
    public void cacheSave_statuslist(int groupType, Context context, String response) {
        mCurrentGroup = groupType;
        switch (groupType) {
            case Constants.GROUP_MYWEIBO_TYPE_ALL:
                SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/profile", "我的全部微博" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt", response);
                break;
            case Constants.GROUP_MYWEIBO_TYPE_ORIGIN:
                SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/profile", "我的原创微博" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt", response);
                break;
            case Constants.GROUP_MYWEIBO_TYPE_PICWEIBO:
                SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/profile", "我的图片微博" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt", response);
                break;
        }
    }

    @Override
    public void cacheLoad_statuslist(int groupType, Context context, OnStatusListFinishedListener onStatusListFinishedListener) {
        String response = null;
        mCurrentGroup = groupType;
        switch (groupType) {
            case Constants.GROUP_MYWEIBO_TYPE_ALL:
                response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/profile", "我的全部微博" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
                break;
            case Constants.GROUP_MYWEIBO_TYPE_ORIGIN:
                response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/profile", "我的原创微博" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
                break;
            case Constants.GROUP_MYWEIBO_TYPE_PICWEIBO:
                response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/profile", "我的图片微博" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
                break;
        }
        if (response != null) {
            mStatusList = StatusList.parse(response).statuses;
            onStatusListFinishedListener.onDataFinish(mStatusList);
        }
    }

    @Override
    public void cacheSave_user(Context context, String response) {
        SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/profile", "我的基本信息" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt", response);
    }

    @Override
    public void cacheLoad_user(Context context, OnUserDetailRequestFinish onUserDetailRequestFinish) {
        String response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/profile", "我的基本信息" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
        if (response != null) {
            onUserDetailRequestFinish.onComplete(User.parse(response));
        }
    }

    @Override
    public void cacheSave_userlist(int groupType, Context context, String response) {
        switch (groupType) {
            case FOLLOWERS_LISTS:
                SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/profile", "我的粉丝列表" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt", response);
                break;
            case FRIENDS_LISTS:
                SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/profile", "我的关注列表" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt", response);
                break;
        }
    }

    @Override
    public void cacheLoad_userlist(int groupType, Context context, OnUserListRequestFinish onUserListRequestFinish) {
        String response = null;
        mUserListType = groupType;
        switch (groupType) {
            case FOLLOWERS_LISTS:
                response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/profile", "我的粉丝列表" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
                break;
            case FRIENDS_LISTS:
                response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/profile", "我的关注列表" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
                break;
        }
        if (response != null) {
            mUsersList = UserList.parse(response).users;
            mFollowersCursor = Integer.valueOf(UserList.parse(response).next_cursor);
            mFriendsCursor = Integer.valueOf(UserList.parse(response).next_cursor);
            onUserListRequestFinish.onDataFinish(mUsersList);
        }
    }

    public void cacheCurrentOuthToken(Context context) {
        String tokenString = AccessTokenKeeper.readAccessToken(context).getToken();
        String expiresIn = String.valueOf(AccessTokenKeeper.readAccessToken(context).getExpiresTime());
        String refresh_token = AccessTokenKeeper.readAccessToken(context).getRefreshToken();
        String uid = AccessTokenKeeper.readAccessToken(context).getUid();
        Token token = new Token(tokenString, expiresIn, refresh_token, uid);
        TokenList tokenList = new TokenList();
        tokenList.tokenList.add(token);
        tokenList.current_uid = uid;
        tokenList.total_number = tokenList.tokenList.size();
        Gson gson = new Gson();
        SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "登录列表缓存.txt", gson.toJson(tokenList));
    }

    /**
     * 用于更新sinceId和maxId的值
     *
     * @param newGroupId
     * @return
     */
    private long checkout(int newGroupId) {
        long sinceId = 0;
        if (mCurrentGroup != newGroupId) {
            mRefrshAll = true;
        }
        if (mStatusList.size() > 0 && mCurrentGroup == newGroupId && mRefrshAll == false) {
            sinceId = Long.valueOf(mStatusList.get(0).id);
        }
        if (mRefrshAll) {
            sinceId = 0;
        }
        mCurrentGroup = newGroupId;
        return sinceId;
    }

    public RequestListener statuslist_NextPage = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                ArrayList<Status> temp = StatusList.parse(response).statuses;
                if (temp.size() == 0 || (temp != null && temp.size() == 1 && temp.get(0).id.equals(mStatusList.get(mStatusList.size() - 1).id))) {
                    mOnStatusListFinishedListener.noMoreDate();
                } else if (temp.size() > 1) {
                    temp.remove(0);
                    mStatusList.addAll(temp);
                    mOnStatusListFinishedListener.onDataFinish(mStatusList);
                }
            } else {
                mOnStatusListFinishedListener.noMoreDate();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ToastUtil.showShort(mContext, e.getMessage());
            mOnStatusListFinishedListener.onError(e.getMessage());
        }
    };
    public RequestListener userlist_PullToRefresh = new RequestListener() {
        @Override
        public void onComplete(String response) {
            ArrayList<User> temp = UserList.parse(response).users;
            if (temp != null && temp.size() > 0) {
                if (mUsersList != null) {
                    mUsersList.clear();
                }
                mUsersList = temp;
                cacheSave_userlist(mUserListType, mContext, response);
                mFollowersCursor = Integer.valueOf(UserList.parse(response).next_cursor);
                mFriendsCursor = Integer.valueOf(UserList.parse(response).next_cursor);
                mOnUserListRequestFinish.onDataFinish(mUsersList);
            } else {
                ToastUtil.showShort(mContext, "没有更新的内容了");
                mOnUserListRequestFinish.noMoreDate();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ToastUtil.showShort(mContext, e.getMessage());
            mOnUserListRequestFinish.onError(e.getMessage());
            cacheLoad_userlist(mUserListType, mContext, mOnUserListRequestFinish);
        }
    };
    public RequestListener userlist_NextPage = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                ArrayList<User> temp = UserList.parse(response).users;
                if (temp.size() == 0 || (temp != null && temp.size() == 1 && temp.get(0).id.equals(mUsersList.get(mUsersList.size() - 1).id))) {
                    mOnUserListRequestFinish.noMoreDate();
                } else if (temp.size() > 1) {
                    mUsersList.addAll(temp);
                    mFollowersCursor = Integer.valueOf(UserList.parse(response).next_cursor);
                    mFriendsCursor = Integer.valueOf(StatusList.parse(response).next_cursor);
                    mOnUserListRequestFinish.onDataFinish(mUsersList);
                }
            } else {
                ToastUtil.showShort(mContext, "内容已经加载完了");
                mOnUserListRequestFinish.noMoreDate();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ToastUtil.showShort(mContext, e.getMessage());
            mOnUserListRequestFinish.onError(e.getMessage());
        }
    };

    public RequestListener statuslist_PullToRefresh = new RequestListener() {
        @Override
        public void onComplete(String response) {
            ArrayList<Status> temp = StatusList.parse(response).statuses;
            if (temp != null && temp.size() > 0) {
                if (mStatusList != null) {
                    mStatusList.clear();
                }
                mStatusList = temp;
                cacheSave_statuslist(mCurrentGroup, mContext, response);
                mOnStatusListFinishedListener.onDataFinish(mStatusList);
            } else {
                ToastUtil.showShort(mContext, "没有更新的内容了");
                mOnStatusListFinishedListener.noMoreDate();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ToastUtil.showShort(mContext, e.getMessage());
            mOnStatusListFinishedListener.onError(e.getMessage());
            cacheLoad_statuslist(mCurrentGroup, mContext, mOnStatusListFinishedListener);
        }
    };

    public RequestListener user_PullToRefresh = new RequestListener() {
        @Override
        public void onComplete(String response) {
            User user = User.parse(response);
            if (user != null) {
                cacheSave_user(mContext, response);
                mOnUserDetailRequestFinish.onComplete(user);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ToastUtil.showShort(mContext, e.getMessage());
            mOnUserDetailRequestFinish.onError(e.getMessage());
            cacheLoad_user(mContext, mOnUserDetailRequestFinish);
        }
    };
}

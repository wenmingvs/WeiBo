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
    private int mFollowersCursor;
    private int mFriendsCursor;
    private ArrayList<User> mUserArrayList;
    private OnStatusListFinishedListener mOnStatusListFinishedListener;
    private OnUserListRequestFinish mOnUserListRequestFinish;
    private Context mContext;

    @Override
    public void showUserDetail(long uid, final Context context, final OnUserDetailRequestFinish onUserRequestFinish) {
        UsersAPI mUsersAPI = new UsersAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mUsersAPI.show(uid, new RequestListener() {
            @Override
            public void onComplete(String response) {
                SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "username_" + AccessTokenKeeper.readAccessToken(context).getUid(), response);
                User user = User.parse(response);
                onUserRequestFinish.onComplete(user);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(context, e.getMessage());
                onUserRequestFinish.onError(e.getMessage());
            }
        });
    }

    @Override
    public User showUserDetailSync(long uid, Context context) {
        UsersAPI mUsersAPI = new UsersAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        return User.parse(mUsersAPI.showSync(uid));
    }

    @Override
    public void userTimeline(long uid, int groupId, Context context, OnStatusListFinishedListener onStatusFinishedListener) {
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        mOnStatusListFinishedListener = onStatusFinishedListener;
        mStatusesAPI.userTimeline(uid, 0, 0, NewFeature.GET_WEIBO_NUMS, 1, false, groupId, false, statusPullToRefresh);
    }

    @Override
    public void userTimelineNextPage(long uid, int groupId, Context context, OnStatusListFinishedListener onStatusFinishedListener) {
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        mOnStatusListFinishedListener = onStatusFinishedListener;
        mStatusesAPI.userTimeline(uid, 0, Long.valueOf(mStatusList.get(mStatusList.size() - 1).id), NewFeature.LOADMORE_WEIBO_ITEM, 1, false, groupId, false, statusNextPage);
    }

    @Override
    public void followers(long uid, Context context, OnUserListRequestFinish onUserListRequestFinish) {
        FriendshipsAPI mFriendshipsAPI = new FriendshipsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        mOnUserListRequestFinish = onUserListRequestFinish;
        mFriendshipsAPI.followers(uid, NewFeature.GET_FOLLOWER_NUM, 0, false, userPullToRefresh);
    }

    @Override
    public void followersNextPage(long uid, Context context, OnUserListRequestFinish onUserListRequestFinish) {
        FriendshipsAPI mFriendshipsAPI = new FriendshipsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        mOnUserListRequestFinish = onUserListRequestFinish;
        mFriendshipsAPI.followers(uid, NewFeature.LOADMORE_FOLLOWER_NUM, mFollowersCursor, false, userNextPage);
    }

    @Override
    public void friends(long uid, Context context, OnUserListRequestFinish onUserListRequestFinish) {
        FriendshipsAPI mFriendshipsAPI = new FriendshipsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        mOnUserListRequestFinish = onUserListRequestFinish;
        mFriendshipsAPI.friends(uid, NewFeature.GET_FRIENDS_NUM, 0, false, userPullToRefresh);
    }

    @Override
    public void friendsNextPage(long uid, Context context, OnUserListRequestFinish onUserListRequestFinish) {
        FriendshipsAPI mFriendshipsAPI = new FriendshipsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        mOnUserListRequestFinish = onUserListRequestFinish;
        mFriendshipsAPI.friends(uid, NewFeature.LOADMORE_FRIENDS_NUM, mFriendsCursor, false, userNextPage);
    }

    @Override
    public void getUserDetailList(final Context context, final OnUserListRequestFinish onUserListRequestFinish) {
        String jsonstring = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "登录列表缓存.txt");
        if (jsonstring == null && AccessTokenKeeper.readAccessToken(context).isSessionValid()) {
            cacheCurrentOuthToken(context);
        }
        final ArrayList<Token> tokenList = TokenList.parse(SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "登录列表缓存.txt")).tokenList;
        if (tokenList == null || tokenList.size() == 0) {
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
                for (Token token : tokenList) {
                    mUserArrayList.add(showUserDetailSync(Long.valueOf(token.getUid()), context));
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

    public RequestListener statusPullToRefresh = new RequestListener() {
        @Override
        public void onComplete(String response) {
            ArrayList<Status> temp = StatusList.parse(response).statusList;
            if (temp != null && temp.size() > 0) {
                if (mStatusList != null) {
                    mStatusList.clear();
                }
                mStatusList = temp;
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
        }
    };

    public RequestListener statusNextPage = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                ArrayList<Status> temp = StatusList.parse(response).statusList;
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

    public RequestListener userPullToRefresh = new RequestListener() {
        @Override
        public void onComplete(String response) {
            ArrayList<User> temp = UserList.parse(response).usersList;
            if (temp != null && temp.size() > 0) {
                if (mUsersList != null) {
                    mUsersList.clear();
                }
                mUsersList = temp;
                mFollowersCursor = Integer.valueOf(StatusList.parse(response).next_cursor);
                mFriendsCursor = Integer.valueOf(StatusList.parse(response).next_cursor);
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
        }
    };

    public RequestListener userNextPage = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                ArrayList<User> temp = UserList.parse(response).usersList;
                if (temp.size() == 0 || (temp != null && temp.size() == 1 && temp.get(0).id.equals(mUsersList.get(mUsersList.size() - 1).id))) {
                    mOnUserListRequestFinish.noMoreDate();
                } else if (temp.size() > 1) {
                    //temp.remove(0);
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

}

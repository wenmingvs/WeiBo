package com.wenming.weiswift.common;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.wenming.weiswift.R;
import com.wenming.weiswift.common.endlessrecyclerview.IEndlessRecyclerView;
import com.wenming.weiswift.common.login.AccessTokenKeeper;
import com.wenming.weiswift.common.login.Constants;

/**
 * 主要用于完成一些网络请求的初始化操作，以及顶部bar的初始化
 * Created by wenmingvs on 16/4/26.
 */
public abstract class DetailActivity extends Activity implements IEndlessRecyclerView {
    public View mToolBar;
    public ImageView mBackIcon;
    public Context mContext;
    public AuthInfo mAuthInfo;
    public Oauth2AccessToken mAccessToken;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;
    public CommentsAPI mCommentsAPI;
    public StatusesAPI mStatusesAPI;
    public FriendshipsAPI mFriendshipsAPI;
    public SsoHandler mSsoHandler;
    public boolean mRefrshAllData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature();

        setContentView(R.layout.messagefragment_base_layout);
        mContext = this;
        initTitleBar();
        initAccessToken();
        initRefreshLayout();
        initRecyclerView();

    }

    public void requestWindowFeature() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
    }

    public abstract void initTitleBar();


    public void initAccessToken() {
        mAuthInfo = new AuthInfo(this, Constants.APP_KEY,
                Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(DetailActivity.this, mAuthInfo);
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        mCommentsAPI = new CommentsAPI(this, Constants.APP_KEY, mAccessToken);
        mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);
        mFriendshipsAPI = new FriendshipsAPI(this, Constants.APP_KEY, mAccessToken);
    }

    protected void initRefreshLayout() {
        mRefrshAllData = true;
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.base_swipe_refresh_widget);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefreshData();
            }
        });
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                pullToRefreshData();
            }
        });

    }


}
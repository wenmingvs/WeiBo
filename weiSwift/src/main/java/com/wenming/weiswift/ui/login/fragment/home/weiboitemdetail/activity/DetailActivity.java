package com.wenming.weiswift.ui.login.fragment.home.weiboitemdetail.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.wenming.weiswift.api.CommentsAPI;
import com.wenming.weiswift.api.FriendshipsAPI;
import com.wenming.weiswift.api.StatusesAPI;
import com.wenming.weiswift.R;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.common.login.Constants;
import com.wenming.weiswift.widget.endlessrecyclerview.IEndlessRecyclerView;

/**
 * 主要用于完成一些网络请求的初始化操作，以及顶部bar的初始化
 * Created by wenmingvs on 16/4/26.
 */
public abstract class DetailActivity extends Activity implements IEndlessRecyclerView {
    public Context mContext;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;
    public CommentsAPI mCommentsAPI;
    public StatusesAPI mStatusesAPI;
    public FriendshipsAPI mFriendshipsAPI;
    public boolean mRefrshAllData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagefragment_base_layout);
        mContext = this;
        initAccessToken();
        initRefreshLayout();
        initRecyclerView();
    }

    public void initAccessToken() {
        mCommentsAPI = new CommentsAPI(this, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(this));
        mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(this));
        mFriendshipsAPI = new FriendshipsAPI(this, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(this));
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
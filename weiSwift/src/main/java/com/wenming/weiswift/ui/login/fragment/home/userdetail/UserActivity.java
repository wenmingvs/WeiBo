package com.wenming.weiswift.ui.login.fragment.home.userdetail;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wenming.weiswift.api.CommentsAPI;
import com.wenming.weiswift.api.FriendshipsAPI;
import com.wenming.weiswift.api.StatusesAPI;
import com.wenming.weiswift.entity.User;
import com.wenming.weiswift.R;
import com.wenming.weiswift.widget.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.wenming.weiswift.widget.endlessrecyclerview.IEndlessRecyclerView;
import com.wenming.weiswift.widget.endlessrecyclerview.RecyclerViewUtils;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.common.login.Constants;
import com.wenming.weiswift.utils.DensityUtil;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/4/27.
 */
public class UserActivity extends Activity implements IEndlessRecyclerView {

    public HomePageAdapter mAdapter;
    public LinearLayoutManager mLayoutManager;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private ArrayList<String> mHomePageDatas;
    private boolean mNoMoreData;
    private User mUser;

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
        setContentView(R.layout.home_user_layout);
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

    @Override
    public void initRecyclerView() {
        mUser = getIntent().getParcelableExtra("user");
        mHomePageDatas = new ArrayList<>();
        mHomePageDatas.add(mUser.location);
        mHomePageDatas.add(mUser.description);
        mRecyclerView = (RecyclerView) findViewById(R.id.base_RecyclerView);
        mAdapter = new HomePageAdapter(mContext, mHomePageDatas);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        RecyclerViewUtils.setFooterView(mRecyclerView, new HomePageFooter(mContext));
        RecyclerViewUtils.setHeaderView(mRecyclerView, new UserHeadView(mContext, mUser));
        mRecyclerView.addItemDecoration(new HomePageItemSapce(DensityUtil.dp2px(mContext, 10)));
    }


    @Override
    public void pullToRefreshData() {

    }

    @Override
    public void requestMoreData() {

    }

    @Override
    public void loadMoreData(String string) {

    }

    @Override
    public void updateList() {

    }


}

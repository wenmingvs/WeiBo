package com.wenming.weiswift.ui.login.fragment.home.userdetail;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wenming.swipebacklayout.app.SwipeBackActivity;
import com.wenming.weiswift.R;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.entity.User;
import com.wenming.weiswift.mvp.presenter.UserActivityPresent;
import com.wenming.weiswift.mvp.presenter.imp.UserActivityPresentImp;
import com.wenming.weiswift.mvp.view.UserActivityView;
import com.wenming.weiswift.ui.login.fragment.home.userdetail.adapter.UserInfoAdapter;
import com.wenming.weiswift.ui.login.fragment.home.weiboitem.WeiboAdapter;
import com.wenming.weiswift.utils.DensityUtil;
import com.wenming.weiswift.widget.endlessrecyclerview.EndlessRecyclerOnScrollListener;
import com.wenming.weiswift.widget.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.wenming.weiswift.widget.endlessrecyclerview.RecyclerViewUtils;
import com.wenming.weiswift.widget.endlessrecyclerview.utils.RecyclerViewStateUtils;
import com.wenming.weiswift.widget.endlessrecyclerview.weight.LoadingFooter;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/4/27.
 */
public class UserActivity extends SwipeBackActivity implements UserActivityView {

    public static final String USER_ACTIVITY_USER_INFO = "用户信息";
    public static final String USER_ACTIVITY_USER_STATUS = "用户微博";
    public static final String USER_ACTIVITY__USER_PHOTO = "用户相册";

    public UserInfoAdapter mUserInfoAdapter;
    public WeiboAdapter mWeiboAdapter;
    public LinearLayoutManager mLayoutManager;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private ArrayList<String> mUserInfoDatas = new ArrayList<>();
    private ArrayList<Status> mMyWeiBoDatas = new ArrayList<>();
    private ArrayList<Status> mMyPhotoDatas = new ArrayList<>();
    private String mSourceScreeenName;
    private int mSourceId;
    public Context mContext;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;
    private UserActivityPresent mUserActivityPresent;
    private String mGroup = USER_ACTIVITY_USER_INFO;
    private int lastOffset;
    private int lastPosition;
    private boolean mNoMoreData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_user_layout);
        mUserActivityPresent = new UserActivityPresentImp(this);
        mSourceScreeenName = getIntent().getStringExtra("screenName");
        mSourceId = getIntent().getIntExtra("id", 0);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.base_swipe_refresh_widget);
        mContext = this;
        initRefreshLayout();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mUserActivityPresent.pullToRefreshData(mGroup, mSourceScreeenName, mContext);
            }
        });
    }

//    private void initRecyclerView(String group) {
//        switch (group) {
//            case USER_ACTIVITY_USER_INFO:
//                break;
//            case USER_ACTIVITY_USER_STATUS:
//                break;
//            case USER_ACTIVITY__USER_PHOTO:
//                break;
//        }
//    }


    protected void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mUserActivityPresent.pullToRefreshData(mGroup, mSourceScreeenName, mContext);
            }
        });

    }


    @Override
    public void updateStatusListView(ArrayList<Status> statuselist, boolean resetAdapter) {

    }

    @Override
    public void updateUserInfoListView(User user, boolean resetAdapter) {
        if (resetAdapter) {
            mUserInfoDatas = new ArrayList<>();
            mUserInfoDatas.add(user.location);
            mUserInfoDatas.add(user.description);
            mRecyclerView = (RecyclerView) findViewById(R.id.base_RecyclerView);
            mUserInfoAdapter = new UserInfoAdapter(mContext, mUserInfoDatas);
            mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mUserInfoAdapter);
            mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
            RecyclerViewUtils.setFooterView(mRecyclerView, new HomePageFooter(mContext));
            RecyclerViewUtils.setHeaderView(mRecyclerView, new UserHeadView(mContext, user));
            mRecyclerView.addItemDecoration(new HomePageItemSapce(DensityUtil.dp2px(mContext, 10)));
        }


    }

    @Override
    public void showLoadingIcon() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingIcon() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoadFooterView(String currentGroup) {
        if (currentGroup.equals(USER_ACTIVITY_USER_STATUS)) {
            RecyclerViewStateUtils.setFooterViewState(UserActivity.this, mRecyclerView, mWeiboAdapter.getItemCount(), LoadingFooter.State.Loading, null);
        } else if (currentGroup.equals(USER_ACTIVITY__USER_PHOTO)) {
            RecyclerViewStateUtils.setFooterViewState(UserActivity.this, mRecyclerView, mWeiboAdapter.getItemCount(), LoadingFooter.State.Loading, null);
        }
    }

    @Override
    public void hideFooterView() {
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
    }

    @Override
    public void showEndFooterView() {
        mNoMoreData = true;
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
    }

    @Override
    public void showErrorFooterView() {
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.NetWorkError);
    }

    @Override
    public void restoreScrollOffset(boolean refreshData) {
        ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(lastPosition, lastOffset);
        if (refreshData) {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mUserActivityPresent.pullToRefreshData(mGroup, mSourceScreeenName, mContext);
                }
            });
        }
    }


    public EndlessRecyclerOnScrollListener mScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            switch (mGroup) {
                case USER_ACTIVITY_USER_STATUS:
                    if (!mNoMoreData && mMyWeiBoDatas != null && mMyWeiBoDatas.size() > 0) {
                        showLoadFooterView(mGroup);
                        mUserActivityPresent.requestMoreData(mGroup, mSourceScreeenName, mContext);
                    }
                    break;
                case USER_ACTIVITY__USER_PHOTO:
                    if (!mNoMoreData && mMyPhotoDatas != null && mMyPhotoDatas.size() > 0) {
                        showLoadFooterView(mGroup);
                        mUserActivityPresent.requestMoreData(mGroup, mSourceScreeenName, mContext);
                    }
                    break;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            View topView = recyclerView.getLayoutManager().getChildAt(0);          //获取可视的第一个view
            lastOffset = topView.getTop();                                         //获取与该view的顶部的偏移量
            lastPosition = recyclerView.getLayoutManager().getPosition(topView);  //得到该View的数组位置

        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            View topView = recyclerView.getLayoutManager().getChildAt(0);         //获取可视的第一个view
            lastOffset = topView.getTop();                                        //获取与该view的顶部的偏移量
            lastPosition = recyclerView.getLayoutManager().getPosition(topView);  //得到该View的数组位置

        }
    };
}

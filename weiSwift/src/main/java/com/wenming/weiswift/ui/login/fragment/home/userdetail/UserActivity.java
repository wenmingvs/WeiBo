package com.wenming.weiswift.ui.login.fragment.home.userdetail;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.wenming.weiswift.R;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.entity.User;
import com.wenming.weiswift.mvp.presenter.UserActivityPresent;
import com.wenming.weiswift.mvp.presenter.imp.UserActivityPresentImp;
import com.wenming.weiswift.mvp.view.UserActivityView;
import com.wenming.weiswift.ui.common.BaseSwipeActivity;
import com.wenming.weiswift.ui.login.fragment.home.userdetail.adapter.UserInfoAdapter;
import com.wenming.weiswift.ui.login.fragment.home.userdetail.adapter.UserPhotoAdapter;
import com.wenming.weiswift.ui.login.fragment.home.weiboitem.TimelineArrowWindow;
import com.wenming.weiswift.ui.login.fragment.home.weiboitem.WeiboAdapter;
import com.wenming.weiswift.widget.endlessrecyclerview.EndlessRecyclerOnScrollListener;
import com.wenming.weiswift.widget.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.wenming.weiswift.widget.endlessrecyclerview.RecyclerViewUtils;
import com.wenming.weiswift.widget.endlessrecyclerview.utils.RecyclerViewStateUtils;
import com.wenming.weiswift.widget.endlessrecyclerview.weight.LoadingFooter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by wenmingvs on 16/4/27.
 */
public class UserActivity extends BaseSwipeActivity implements UserActivityView {

    public static final String USER_ACTIVITY_USER_INFO = "用户信息";
    public static final String USER_ACTIVITY_USER_STATUS = "用户微博";
    public static final String USER_ACTIVITY__USER_PHOTO = "用户相册";


    public LinearLayoutManager mLayoutManager;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;

    private String mSourceScreeenName;
    private int mSourceId;
    public Context mContext;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;
    private UserActivityPresent mUserActivityPresent;
    private String mGroup = USER_ACTIVITY_USER_STATUS;
    private int lastOffset;
    private int lastPosition;
    private boolean mNoMoreData;
    private WeiboAdapter mMyWeiBoAdapter;
    private UserInfoAdapter mUserInfoAdapter;
    private UserPhotoAdapter mImageAdapter;
    private ArrayList<String> mUserInfoDatas = new ArrayList<>();
    private ArrayList<Status> mMyWeiBoDatas = new ArrayList<>();
    private ArrayList<String> mMyPhotoDatas = new ArrayList<>();
    private User mUser;
    private UserHeadView mUserHeadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.home_user_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.base_RecyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.base_swipe_refresh_widget);
        mUserActivityPresent = new UserActivityPresentImp(this);
        mSourceScreeenName = getIntent().getStringExtra("screenName");
        mSourceId = getIntent().getIntExtra("id", 0);
        initRefreshLayout();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mUserActivityPresent.pullToRefreshData(mGroup, mSourceScreeenName, mContext);
            }
        });
    }

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
    public void updateStatusListView(final ArrayList<Status> statuselist, boolean resetAdapter) {
        mUser = statuselist.get(0).user;
        if (resetAdapter) {
            mNoMoreData = false;
            mMyWeiBoAdapter = new WeiboAdapter(statuselist, mContext) {
                @Override
                public void arrowClick(Status status, int position) {
                    TimelineArrowWindow popupWindow = new TimelineArrowWindow(mContext, statuselist.get(position), mMyWeiBoAdapter, position, "我的微博");
                    popupWindow.showAtLocation(mRecyclerView, Gravity.CENTER, 0, 0);
                }
            };
            mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mMyWeiBoAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        }
        mUserHeadView = new UserHeadView(mContext, mGroup, mUser) {
            @Override
            public void onIndicatorClick(String group) {
                mGroup = group;
                highlightIndicator(group);
                mUserActivityPresent.pullToRefreshData(mGroup, mSourceScreeenName, mContext);
            }
        };
        mRecyclerView.clearOnScrollListeners();
        mRecyclerView.addOnScrollListener(mScrollListener);
        mMyWeiBoDatas = statuselist;
        mMyWeiBoAdapter.setData(statuselist);
        RecyclerViewUtils.setHeaderView(mRecyclerView, mUserHeadView);
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void updatePhotoListView(ArrayList<Status> statuselist, boolean resetAdapter) {
        mUser = statuselist.get(0).user;
        mMyPhotoDatas.clear();
        mRecyclerView.clearOnScrollListeners();
        for (Status status : statuselist) {
            if (status.bmiddle_pic_urls.size() > 0) {
                mMyPhotoDatas.addAll(status.bmiddle_pic_urls);
            } else {
                if (status.retweeted_status != null) {
                    mMyPhotoDatas.addAll(status.retweeted_status.bmiddle_pic_urls);
                }
            }
        }
        removeDuplicateWithOrder(mMyPhotoDatas);
        if (resetAdapter) {
            mNoMoreData = false;
            mImageAdapter = new UserPhotoAdapter(mMyPhotoDatas, mContext);
            mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mImageAdapter);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    //heaedview
                    if (position == 0) {
                        return 3;
                    }
                    return 1;
                }
            });
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        }
        mUserHeadView = new UserHeadView(mContext, mGroup, mUser) {
            @Override
            public void onIndicatorClick(String group) {
                mGroup = group;
                highlightIndicator(group);
                mUserActivityPresent.pullToRefreshData(mGroup, mSourceScreeenName, mContext);
            }
        };
        mRecyclerView.addOnScrollListener(mScrollListener);
        mImageAdapter.setData(mMyPhotoDatas);
        RecyclerViewUtils.setHeaderView(mRecyclerView, mUserHeadView);
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateUserInfoListView(User user, boolean resetAdapter) {
        mUser = user;
        mUserHeadView = new UserHeadView(mContext, mGroup, mUser) {
            @Override
            public void onIndicatorClick(String group) {
                mGroup = group;
                highlightIndicator(group);
                mUserActivityPresent.pullToRefreshData(mGroup, mSourceScreeenName, mContext);
            }
        };
        if (resetAdapter) {
            mUserInfoDatas.clear();
            mUserInfoDatas.add(user.location);
            mUserInfoDatas.add(user.description);
            mRecyclerView.clearOnScrollListeners();
            mUserInfoAdapter = new UserInfoAdapter(mContext, mUserInfoDatas);
            mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mUserInfoAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
            RecyclerViewUtils.setHeaderView(mRecyclerView, mUserHeadView);
            RecyclerViewUtils.setFooterView(mRecyclerView, new UserInfoFooter(mContext));
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
            RecyclerViewStateUtils.setFooterViewState(UserActivity.this, mRecyclerView, mMyWeiBoAdapter.getItemCount(), LoadingFooter.State.Loading, null);
        } else if (currentGroup.equals(USER_ACTIVITY__USER_PHOTO)) {
            RecyclerViewStateUtils.setFooterViewState(UserActivity.this, mRecyclerView, mImageAdapter.getItemCount(), LoadingFooter.State.Loading, null);
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

    /**
     * 去重
     *
     * @param list
     */
    public static void removeDuplicateWithOrder(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Object element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        list.clear();
        list.addAll(newList);
        System.out.println(" remove duplicate " + list);
    }
}

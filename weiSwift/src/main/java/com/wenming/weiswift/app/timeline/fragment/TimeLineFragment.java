package com.wenming.weiswift.app.timeline.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.base.BaseFragment;
import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.timeline.adapter.TimeLineAdapter;
import com.wenming.weiswift.app.timeline.contract.TimeLineContract;
import com.wenming.weiswift.utils.ToastUtil;
import com.wenming.weiswift.widget.toast.LoadedToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenmingvs on 16/4/27.
 */
public class TimeLineFragment extends BaseFragment implements TimeLineContract.View {
    public RecyclerView mRecyclerView;
    public TimeLineAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TimeLineContract.Presenter mPresent;

    public TimeLineFragment() {
    }

    public static TimeLineFragment newInstance() {
        TimeLineFragment timeLineFragment = new TimeLineFragment();
        Bundle args = new Bundle();
        timeLineFragment.setArguments(args);
        return timeLineFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_timeline, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareView();
        initData();
        initView();
        initListener();
        mPresent.start();
    }

    private void prepareView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.timeline_refresh_srl);
        mRecyclerView = (RecyclerView) findViewById(R.id.timeline_rlv);
    }

    private void initData() {

    }

    private void initView() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light, R.color.holo_orange_light, R.color.holo_red_light);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new TimeLineAdapter(mContext, new ArrayList<Status>(0));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresent.refreshTimeLine(mAdapter.getData());
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresent.loadMoreTimeLine(mAdapter.getData());
            }
        },mRecyclerView);
    }

    @Override
    public void setPresenter(TimeLineContract.Presenter presenter) {
        mPresent = presenter;
    }

    @Override
    public void setTimeLineList(List<Status> timeLineList) {
        mAdapter.setNewData(timeLineList);
    }

    @Override
    public void addHeaderTimeLine(List<Status> timeLineList) {
        mAdapter.addData(0, timeLineList);
    }

    @Override
    public void addLastTimeLine(List<Status> timeLineList) {
        mAdapter.addData(timeLineList);
    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void dismissLoading() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void showPullToRefreshEmpty() {
        if (getActivity() != null && isAdded()) {
            LoadedToast.showToast(mContext, getString(R.string.timeline_count_zeor));
        }
    }

    @Override
    public void showServerMessage(String error) {
        ToastUtil.showShort(mContext, error);
    }

    @Override
    public void showNetWorkNotConnected() {
        ToastUtil.showShort(mContext, R.string.common_network_not_connected);
    }

    @Override
    public void showNetWorkTimeOut() {
        ToastUtil.showShort(mContext, R.string.common_network_time_out);
    }

    @Override
    public void loadMoreComplete() {
        mAdapter.loadMoreComplete();
    }

    @Override
    public void loadMoreFail() {
        mAdapter.loadMoreFail();
    }

    @Override
    public void loadMoreEnd() {
        mAdapter.loadMoreEnd();
    }

    @Override
    public void scrollToTop() {
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void showNewWeiboCount(final int num) {
        Animation animation = new AlphaAnimation(0.5f, 1.0f);
        animation.setDuration(2000);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mToastTv.setVisibility(View.VISIBLE);
                mToastTv.setText(num + "条新微博");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mToastTv.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mToastBg.startAnimation(animation);
    }
}

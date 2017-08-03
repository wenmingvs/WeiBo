package com.wenming.weiswift.app.timeline.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.base.BaseFragment;
import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.common.widget.CommonLoadMoreView;
import com.wenming.weiswift.app.timeline.adapter.TimeLineAdapter;
import com.wenming.weiswift.app.timeline.contract.TimeLineContract;
import com.wenming.weiswift.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenmingvs on 16/4/27.
 */
public class TimeLineFragment extends BaseFragment implements TimeLineContract.View {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mTimeLineRlv;
    public TimeLineAdapter mTimeLineAdapter;
    private LinearLayoutManager mTimeLineLlManager;
    private TextView mCountTv;
    private LinearLayout mCountLl;
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
        mTimeLineRlv = (RecyclerView) findViewById(R.id.timeline_rlv);
        mCountLl = (LinearLayout) findViewById(R.id.timeline_new_status_ll);
        mCountTv = (TextView) findViewById(R.id.timeline_new_status_tv);
    }

    private void initData() {

    }

    private void initView() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light, R.color.holo_orange_light, R.color.holo_red_light);
        mTimeLineLlManager = new LinearLayoutManager(mContext);
        mTimeLineRlv.setLayoutManager(mTimeLineLlManager);
        mTimeLineAdapter = new TimeLineAdapter(mContext, new ArrayList<Status>(0));
        mTimeLineAdapter.setLoadMoreView(new CommonLoadMoreView());
        mTimeLineRlv.setAdapter(mTimeLineAdapter);
    }

    private void initListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTimeLine();
            }
        });
        mTimeLineAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresent.loadMoreTimeLine(mTimeLineAdapter.getData());
            }
        }, mTimeLineRlv);
    }

    @Override
    public void setPresenter(TimeLineContract.Presenter presenter) {
        mPresent = presenter;
    }

    @Override
    public void setTimeLineList(List<Status> timeLineList) {
        mTimeLineAdapter.setNewData(timeLineList);
    }

    @Override
    public void addHeaderTimeLine(List<Status> timeLineList) {
        mTimeLineAdapter.addData(0, timeLineList);
    }

    @Override
    public void addLastTimeLine(List<Status> timeLineList) {
        mTimeLineAdapter.addData(timeLineList);
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
        mTimeLineAdapter.loadMoreComplete();
    }

    @Override
    public void loadMoreFail() {
        mTimeLineAdapter.loadMoreFail();
    }

    @Override
    public void loadMoreEnd() {
        mTimeLineAdapter.loadMoreEnd();
    }

    @Override
    public void scrollToTop() {
        mTimeLineRlv.smoothScrollToPosition(0);
    }

    public boolean isOnFirstCompletelyVisibleItemPosition() {
        return mTimeLineLlManager.findFirstCompletelyVisibleItemPosition() == 0;
    }

    public void refreshTimeLine() {
        showLoading();
        mPresent.refreshTimeLine(mTimeLineAdapter.getData());
    }

    @Override
    public void showNewWeiboCount(final int num) {
        if (getActivity() == null || !isAdded()) {
            return;
        }
        Animation animation = new AlphaAnimation(0.7f, 1.0f);
        animation.setDuration(2000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mCountTv.setVisibility(View.VISIBLE);
                mCountTv.setText(getString(R.string.timeline_count, String.valueOf(num)));
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mCountTv.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mCountTv.startAnimation(animation);
        mCountLl.startAnimation(animation);
    }
}

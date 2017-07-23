package com.wenming.weiswift.app.timeline.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.base.BaseFragment;
import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.timeline.adapter.TimeLineAdapter;
import com.wenming.weiswift.app.timeline.contract.TimeLineContract;

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
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new TimeLineAdapter(new ArrayList<Status>(0));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initListener() {

    }

    @Override
    public void setPresenter(TimeLineContract.Presenter presenter) {
        mPresent = presenter;
    }

    @Override
    public void setTimeLineList(List<Status> timeLineList) {

    }

    @Override
    public void addHeaderTimeLine(List<Status> timeLineList) {

    }

    @Override
    public void addLastTimeLine(List<Status> timeLineList) {

    }
}

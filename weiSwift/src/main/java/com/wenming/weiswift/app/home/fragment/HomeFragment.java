package com.wenming.weiswift.app.home.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.base.BaseFragment;
import com.wenming.weiswift.app.home.adapter.GroupPagerAdapter;
import com.wenming.weiswift.app.home.constant.Constants;
import com.wenming.weiswift.app.home.contract.HomeContract;
import com.wenming.weiswift.app.home.data.entity.Group;
import com.wenming.weiswift.app.timeline.data.TimeLineDataManager;
import com.wenming.weiswift.app.timeline.fragment.TimeLineFragment;
import com.wenming.weiswift.app.timeline.presenter.TimeLinePresent;
import com.wenming.weiswift.utils.ToastUtil;

import java.util.List;

/**
 * Created by wenmingvs on 16/4/27.
 */
public class HomeFragment extends BaseFragment implements HomeContract.View {
    private Toolbar mToolBar;
    private TabLayout mGourpTl;
    private ViewPager mGroupVp;
    private AppCompatActivity mActivity;
    private HomeContract.Presenter mPresenter;
    private GroupPagerAdapter mGroupAdapter;
    private boolean mRefreshAll;

    private static final String ARG_REFRESH_ALL = "arg_refresh_all";

    public HomeFragment() {
    }

    public static HomeFragment newInstance(boolean refreshAll) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_REFRESH_ALL, refreshAll);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareView();
        initData();
        initView();
        mPresenter.start();
    }

    private void prepareView() {
        mActivity = (AppCompatActivity) getActivity();
        mToolBar = (Toolbar) findViewById(R.id.main_toolbar);
        mGourpTl = (TabLayout) findViewById(R.id.main_tablayout);
        mGroupVp = (ViewPager) findViewById(R.id.main_groups_vp);
    }

    private void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mRefreshAll = arguments.getBoolean(ARG_REFRESH_ALL);
        }
    }

    private void initView() {
        mActivity.setSupportActionBar(mToolBar);
        initDefaultFragment();
    }

    private void initDefaultFragment() {
        mGroupAdapter = new GroupPagerAdapter(mActivity.getSupportFragmentManager());
        TimeLineFragment defaultFragment = initTimeLineFragment(Constants.GROUP_ALL);
        mGroupAdapter.addFragment(defaultFragment, getString(R.string.groups_default));
        mGroupVp.setAdapter(mGroupAdapter);
        mGourpTl.setupWithViewPager(mGroupVp);
        mGroupVp.setCurrentItem(0, false);
        mGourpTl.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setGroupsList(List<Group> groups) {
        if (groups != null && groups.size() > 0) {
            for (int i = 0; i < groups.size(); i++) {
                TimeLineFragment groupFragment = initTimeLineFragment(Long.valueOf(groups.get(i).id));
                mGroupAdapter.addFragment(groupFragment, groups.get(i).name);
            }
            mGroupAdapter.notifyDataSetChanged();
        }
    }

    private TimeLineFragment initTimeLineFragment(long gourpId) {
        TimeLineFragment timeLineFragment = TimeLineFragment.newInstance();
        new TimeLinePresent(timeLineFragment, new TimeLineDataManager(mContext.getApplicationContext()), mRefreshAll, gourpId);
        return timeLineFragment;
    }

    @Override
    public void showServerMessage(String text) {
        ToastUtil.showShort(mContext, text);
    }

    @Override
    public void showLoading() {
        showLoadingDialog(R.string.groups_loading, false);
    }

    @Override
    public void dismissLoading() {
        dissLoadingDialog();
    }

    @Override
    public void showNoneNetWork() {
        ToastUtil.showShort(mContext, R.string.common_network_not_connected);
    }

    public void scrollToTop() {
        TimeLineFragment currentFragment = mGroupAdapter.getCurrentFragment();
        if (currentFragment == null){
            return;
        }
        if (currentFragment.isOnFirstCompletelyVisibleItemPosition()) {
            currentFragment.refreshTimeLine();
        }else {
            currentFragment.scrollToTop();
        }
    }
}

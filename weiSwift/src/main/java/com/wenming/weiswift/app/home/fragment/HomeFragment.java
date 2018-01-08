package com.wenming.weiswift.app.home.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
    private static final String ARG_REFRESH_ALL = "arg_refresh_all";
    /**
     * 缓存的viewpager数
     */
    private static final int CACHE_FRAGMENT_NUM = 3;
    private TabLayout mGroupTl;
    private ViewPager mGroupVp;
    private HomeContract.Presenter mPresenter;
    private GroupPagerAdapter mGroupAdapter;
    private boolean mRefreshAll;

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
        mGroupVp = (ViewPager) findViewById(R.id.home_groups_vp);
    }

    private void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mRefreshAll = arguments.getBoolean(ARG_REFRESH_ALL);
        }
    }

    private void initView() {
        //初始化分组列表
        mGroupAdapter = new GroupPagerAdapter(getActivity().getSupportFragmentManager());
        //创建【全部】分组
        TimeLineFragment defaultFragment = createTimeLineFragment(Constants.GROUP_ALL);
        mGroupAdapter.addFragment(defaultFragment, getString(R.string.groups_default));
        mGroupVp.setAdapter(mGroupAdapter);
        //标签栏与viewpager绑定
        mGroupTl.setupWithViewPager(mGroupVp);
        mGroupVp.setCurrentItem(0, false);
        //设置标签为可滑动
        mGroupTl.setTabMode(TabLayout.MODE_SCROLLABLE);
        //设置提前加载的分组
        mGroupVp.setOffscreenPageLimit(CACHE_FRAGMENT_NUM);
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void setTabLayout(TabLayout tabLayout) {
        mGroupTl = tabLayout;
    }

    /**
     * 每次更新分组，都会重新创建adapter，达到清除上次数据的效果，更新分组不会很频繁所以不用担心性能问题
     *
     * @param groups
     */
    @Override
    public void setGroupsList(List<Group> groups) {
        if (groups == null || groups.size() == 0) {
            return;
        }
        //创建【全部】分组
        mGroupAdapter = new GroupPagerAdapter(getActivity().getSupportFragmentManager());
        TimeLineFragment defaultFragment = createTimeLineFragment(Constants.GROUP_ALL);
        mGroupAdapter.addFragment(defaultFragment, getString(R.string.groups_default));
        mGroupVp.setAdapter(mGroupAdapter);
        //加载全新的tab
        for (int i = 0; i < groups.size(); i++) {
            TimeLineFragment groupFragment = createTimeLineFragment(Long.valueOf(groups.get(i).id));
            mGroupAdapter.addFragment(groupFragment, groups.get(i).name);
        }
        mGroupAdapter.notifyDataSetChanged();
    }

    private TimeLineFragment createTimeLineFragment(long groupId) {
        TimeLineFragment timeLineFragment = TimeLineFragment.newInstance();
        new TimeLinePresent(timeLineFragment, new TimeLineDataManager(mContext.getApplicationContext()), mRefreshAll, groupId);
        return timeLineFragment;
    }

    @Override
    public void showServerMessage(final String text) {
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

    @Override
    public void showTimeOut() {
        ToastUtil.showShort(mContext, R.string.common_network_time_out);
    }

    public void scrollToTop() {
        TimeLineFragment currentFragment = mGroupAdapter.getCurrentFragment();
        if (currentFragment == null) {
            return;
        }
        if (currentFragment.isOnFirstCompletelyVisibleItemPosition()) {
            currentFragment.refreshTimeLine();
        } else {
            currentFragment.scrollToTop();
        }
    }
}

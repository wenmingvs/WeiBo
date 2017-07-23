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
import com.wenming.weiswift.app.home.contract.HomeContract;
import com.wenming.weiswift.app.home.data.entity.Group;
import com.wenming.weiswift.app.home.timeline.fragment.TimeLineFragment;
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

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareView();
        initView();
        mPresenter.start();
    }

    private void prepareView() {
        mActivity = (AppCompatActivity) getActivity();
        mToolBar = (Toolbar) findViewById(R.id.main_toolbar);
        mGourpTl = (TabLayout) findViewById(R.id.main_tablayout);
        mGroupVp = (ViewPager) findViewById(R.id.main_groups_vp);
    }

    private void initView() {
        mActivity.setSupportActionBar(mToolBar);
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setGroupsList(List<Group> groups) {
        mGroupAdapter = new GroupPagerAdapter(mActivity.getSupportFragmentManager());
        TimeLineFragment defaultFragment = initTimeLineFragment();
        mGroupAdapter.addFragment(defaultFragment, getString(R.string.groups_default));
        if (groups != null && groups.size() > 0) {
            for (int i = 0; i < groups.size(); i++) {
                TimeLineFragment groupFragment = initTimeLineFragment();
                mGroupAdapter.addFragment(groupFragment, groups.get(i).name);
            }
        }
        mGroupVp.setAdapter(mGroupAdapter);
        mGourpTl.setupWithViewPager(mGroupVp);
        mGroupVp.setCurrentItem(0, false);
    }

    private TimeLineFragment initTimeLineFragment() {
        TimeLineFragment timeLineFragment = TimeLineFragment.newInstance();
        //TODO 初始化presenter
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
    public void showRetryBg() {
        //TODO 获取不到微博数据
    }

    @Override
    public void hideRetryBg() {
        //TODO 获取不到微博数据
    }

    @Override
    public void showNoneNetWork() {
        ToastUtil.showShort(mContext, R.string.common_none_network);
    }
}

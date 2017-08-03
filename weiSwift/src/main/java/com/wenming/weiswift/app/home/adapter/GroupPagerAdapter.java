package com.wenming.weiswift.app.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.wenming.weiswift.app.timeline.fragment.TimeLineFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenmingvs on 2017/6/3.
 */

public class GroupPagerAdapter extends FragmentPagerAdapter {
    private final List<TimeLineFragment> mFragmentList = new ArrayList<>();
    private final List<String> mGroupNameList = new ArrayList<>();
    private TimeLineFragment mCurrentFragment;

    public GroupPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(TimeLineFragment fragment, String title) {
        mFragmentList.add(fragment);
        mGroupNameList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mGroupNameList.get(position);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        mCurrentFragment = (TimeLineFragment) object;
    }

    public TimeLineFragment getCurrentFragment() {
        return mCurrentFragment;
    }
}

package com.wenming.weiswift.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.wenming.weiswift.R;

/**
 * Created by wenmingvs on 15/12/26.
 */
public class DiscoverFragment extends Fragment {
    private Activity mActivity;
    private View mToolBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.toolbar_discover);
        mToolBar = mActivity.findViewById(R.id.toolbar_discover);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        TextView textView = new TextView(getActivity());
        textView.setText("发现");
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(30);
        return textView;

    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            hideToolBar();
        } else {
            showToolBar();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mToolBar.setVisibility(View.GONE);
    }

    public void hideToolBar() {
        mToolBar.setVisibility(View.GONE);
    }

    public void showToolBar() {
        mToolBar.setVisibility(View.VISIBLE);
    }

}

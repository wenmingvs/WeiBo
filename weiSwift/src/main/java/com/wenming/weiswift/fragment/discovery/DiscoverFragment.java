package com.wenming.weiswift.fragment.discovery;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import com.wenming.weiswift.R;
import com.wenming.weiswift.fragment.discovery.popularweibo.PopularWeiBoActivity;

/**
 * Created by wenmingvs on 15/12/26.
 */
public class DiscoverFragment extends Fragment {
    private Activity mActivity;
    private Context mContext;
    private View mToolBar;
    private View mView;
    private RelativeLayout mPublicWeibo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.toolbar_discover);
        mToolBar = mActivity.findViewById(R.id.toolbar_discover);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.discoverfragment_layout, container, false);
        mPublicWeibo = (RelativeLayout) mView.findViewById(R.id.publicweibo_layout);
        mPublicWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, PopularWeiBoActivity.class);
                mContext.startActivity(intent);
            }
        });
        return mView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            //hideToolBar();
        } else {
            //showToolBar();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //mToolBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //mToolBar.setVisibility(View.GONE);
    }

    public void hideToolBar() {
        mToolBar.setVisibility(View.GONE);
    }

    public void showToolBar() {
        mToolBar.setVisibility(View.VISIBLE);
    }

}

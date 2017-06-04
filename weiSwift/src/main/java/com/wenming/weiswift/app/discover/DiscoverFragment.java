package com.wenming.weiswift.app.discover;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.base.BaseFragment;
import com.wenming.weiswift.app.discover.hotweibo.HotWeiBoSwipeActivity;

/**
 * Created by wenmingvs on 15/12/26.
 */
public class DiscoverFragment extends BaseFragment {
    private RelativeLayout mPublicWeibo;

    public DiscoverFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.discoverfragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareView();
        initListener();
    }

    private void prepareView() {
        mPublicWeibo = (RelativeLayout) findViewById(R.id.publicweibo_layout);

    }

    private void initListener() {
        mPublicWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HotWeiBoSwipeActivity.class);
                getContext().startActivity(intent);
            }
        });
    }
}

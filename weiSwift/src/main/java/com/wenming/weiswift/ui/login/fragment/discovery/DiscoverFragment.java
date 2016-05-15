package com.wenming.weiswift.ui.login.fragment.discovery;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.wenming.weiswift.R;
import com.wenming.weiswift.ui.login.fragment.discovery.hotweibo.HotWeiBoActivity;

/**
 * Created by wenmingvs on 15/12/26.
 */
public class DiscoverFragment extends Fragment {
    private View mView;
    private RelativeLayout mPublicWeibo;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.discoverfragment_layout, container, false);
        mPublicWeibo = (RelativeLayout) mView.findViewById(R.id.publicweibo_layout);
        mPublicWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HotWeiBoActivity.class);
                getContext().startActivity(intent);
            }
        });
        return mView;
    }


}

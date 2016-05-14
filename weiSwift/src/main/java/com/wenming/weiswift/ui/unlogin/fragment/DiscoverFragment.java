package com.wenming.weiswift.ui.unlogin.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wenming.weiswift.R;

/**
 * Created by wenmingvs on 15/12/26.
 */
public class DiscoverFragment extends BaseFragment {
    private View mView;
    public TextView mLogin;
    public TextView mRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.unlogin_discoverfragment_layout, container, false);
        mLogin = (TextView) mView.findViewById(R.id.login);
        mRegister = (TextView) mView.findViewById(R.id.register);
        initAccessToken();
        setUpListener();
        return mView;
    }

    public void setUpListener() {
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginWebView();
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginWebView();
            }
        });
    }
}

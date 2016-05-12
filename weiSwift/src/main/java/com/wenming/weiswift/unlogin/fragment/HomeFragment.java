package com.wenming.weiswift.unlogin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenming.weiswift.R;

/**
 * Created by wenmingvs on 16/5/9.
 */
public class HomeFragment extends BaseFragment {
    private View mView;
    public TextView mLogin;
    public TextView mRegister;
    private ImageView mCircleView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.unlogin_mainfragment_layout, container, false);
        mLogin = (TextView) mView.findViewById(R.id.login);
        mRegister = (TextView) mView.findViewById(R.id.register);
        mCircleView = (ImageView) mView.findViewById(R.id.circleView);
        initAccessToken();
        setUpListener();
        return mView;
    }

    private void setUpListener() {
        Animation rotateAnim = AnimationUtils.loadAnimation(getContext(), R.anim.endlessrotate);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnim.setInterpolator(lin);
        mCircleView.setAnimation(rotateAnim);
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

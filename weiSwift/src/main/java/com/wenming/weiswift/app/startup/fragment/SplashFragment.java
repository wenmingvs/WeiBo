package com.wenming.weiswift.app.startup.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.base.BaseFragment;
import com.wenming.weiswift.app.home.activity.MainActivity;
import com.wenming.weiswift.app.startup.contract.SplashContract;

/**
 * Created by wenmingvs on 2017/5/30.
 */

public class SplashFragment extends BaseFragment implements SplashContract.View {
    private Button mWebAuthBt;
    private Button mSSOAuthBt;
    private LinearLayout mAuthContainerLl;
    private TextView mAuthHintTv;
    private SplashContract.Presenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareViews();
        initListener();
        mPresenter.start();
    }

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    private void prepareViews() {
        mAuthContainerLl = (LinearLayout) findViewById(R.id.login_container_ll);
        mAuthHintTv = (TextView) findViewById(R.id.login_text_tv);
        mSSOAuthBt = (Button) findViewById(R.id.login_sso_auth_bt);
        mWebAuthBt = (Button) findViewById(R.id.login_web_auth_bt);
    }

    private void initListener() {
        mSSOAuthBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.ssoAuth(getActivity());
            }
        });
        mWebAuthBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.webAuth(mContext);
            }
        });
    }

    public void onBackPress() {
        getActivity().finish();
    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void goToMainActivity() {
        Intent intent = new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public void finishActivity() {
        getActivity().finish();
    }

    @Override
    public void showAuth() {
        mAuthContainerLl.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.login_bt_animation);
        mAuthContainerLl.setAnimation(animation);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.ssoAuthorizeCallBack(requestCode, resultCode, data);
    }
}

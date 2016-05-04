package com.wenming.weiswift.fragment.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;
import com.wenming.weiswift.NewFeature;
import com.wenming.weiswift.R;
import com.wenming.weiswift.common.login.AccessTokenKeeper;
import com.wenming.weiswift.common.login.Constants;
import com.wenming.weiswift.common.util.LogUtil;
import com.wenming.weiswift.common.util.ToastUtil;
import com.wenming.weiswift.fragment.home.weiboitem.IWeiboListRecyclerView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wenmingvs on 15/12/26.
 */
public abstract class MainFragment extends Fragment implements IWeiboListRecyclerView {

    public AuthInfo mAuthInfo;
    public Oauth2AccessToken mAccessToken;
    public StatusesAPI mStatusesAPI;
    public UsersAPI mUsersAPI;
    public SsoHandler mSsoHandler;
    public Context mContext;
    public Activity mActivity;
    public View mToolBar;
    public TextView mLogin;
    public TextView mRegister;
    public TextView mUserName;
    public View mView;
    public boolean mFirstLoad;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public boolean mRefrshAllData;
    private Timer mTimer;
    private TimerTask mTimeTask;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtil.d("onAttach");
        mFirstLoad = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("onCreateView");
        mActivity = getActivity();
        mContext = getActivity();
        initAccessToken();
        if (NewFeature.LOGIN == true) {
            mView = inflater.inflate(R.layout.mainfragment_layout, container, false);
            initTimeTask();
            initLoginStateTitleBar();
            initRecyclerView();
            initRefreshLayout();
            return mView;
        } else {
            mView = inflater.inflate(R.layout.mainfragment_unlogin_layout, container, false);
            initunLoginStateTitleBar();
            return mView;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LogUtil.d("onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        if (NewFeature.LOGIN == true) {
            mToolBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d("onResume in MainFragment");
    }

    @Override
    public void onDestroyView() {
        LogUtil.d("onDestroyView");
        super.onDestroyView();
        if (NewFeature.LOGIN == true) {
            mToolBar.setVisibility(View.GONE);
            mFirstLoad = false;
        }
    }


    public void hideToolBar() {
        mToolBar.setVisibility(View.GONE);
    }

    public void showToolBar() {
        mToolBar.setVisibility(View.VISIBLE);
    }

    /**
     * 发起 SSO 登陆的 Activity 必须重写 onActivityResults
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.d("onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {

        super.onHiddenChanged(hidden);
        if (hidden) {
            hideToolBar();
            if(mSwipeRefreshLayout != null){
                mSwipeRefreshLayout.setRefreshing(false);
            }
        } else {
            showToolBar();
        }
    }

    @Override
    public void initTimeTask() {
        mTimeTask = new TimerTask() {
            @Override
            public void run() {
                mRefrshAllData = true;
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTimeTask, 0, 15 * 60 * 1000);
    }

    private void initAccessToken() {
        mAuthInfo = new AuthInfo(mContext, Constants.APP_KEY,
                Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(mActivity, mAuthInfo);
        mAccessToken = AccessTokenKeeper.readAccessToken(mContext);
        mStatusesAPI = new StatusesAPI(mContext, Constants.APP_KEY, mAccessToken);
        mUsersAPI = new UsersAPI(mContext, Constants.APP_KEY, mAccessToken);
    }

    private void initLoginStateTitleBar() {
        mActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.toolbar_home_login);
        mToolBar = mActivity.findViewById(R.id.toolbar_home_login);
        mUserName = (TextView) mToolBar.findViewById(R.id.toolbar_username);
        refreshUserName();
    }

    private void refreshUserName() {
        long uid = Long.parseLong(mAccessToken.getUid());
        mUsersAPI.show(uid, new RequestListener() {
            @Override
            public void onComplete(String response) {
                // 调用 User#parse 将JSON串解析成User对象
                User user = User.parse(response);
                if (user != null) {
                    mUserName.setText(user.name);
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ErrorInfo info = ErrorInfo.parse(e.getMessage());
                ToastUtil.showShort(mContext, info.toString());
            }
        });
    }


    private void initunLoginStateTitleBar() {
        mActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.toolbar_home_unlogin);
        mToolBar = mActivity.findViewById(R.id.toolbar_home_unlogin);
        mLogin = (TextView) mToolBar.findViewById(R.id.login);
        mRegister = (TextView) mToolBar.findViewById(R.id.register);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSsoHandler.authorizeWeb(new AuthListener());
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            /**
             * @param view
             */
            @Override
            public void onClick(View view) {
                mSsoHandler.registerOrLoginByMobile("验证码登陆", new AuthListener());
            }
        });
    }

    /**
     * 初始化下拉刷新控件
     * 1. 设置下拉刷新执行的逻辑
     * 2. 第一次进来就自动下拉刷新
     */
    private void initRefreshLayout() {
        mRefrshAllData = true;
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefreshData(mRefrshAllData);
            }
        });
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                firstLoadData();
            }
        });

    }

    class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);// 从 Bundle 中解析 Token
            if (mAccessToken.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(mContext,
                        mAccessToken);//保存Token
                Toast.makeText(mContext, "授权成功,请重启App", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(mContext, "授权失败", Toast.LENGTH_SHORT)
                        .show();
            }

        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(mContext,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(mContext, "取消授权",
                    Toast.LENGTH_LONG).show();
        }
    }
}

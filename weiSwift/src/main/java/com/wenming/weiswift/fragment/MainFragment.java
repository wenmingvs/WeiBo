package com.wenming.weiswift.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
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
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.wenming.weiswift.R;
import com.wenming.weiswift.weiboAccess.AccessTokenKeeper;
import com.wenming.weiswift.weiboAccess.Constants;

/**
 * Created by wenmingvs on 15/12/26.
 */
public class MainFragment extends Fragment {
    private Activity mActivity;
    private View mToolBar;
    private TextView mLogUtilinTextView;
    private Context mContext;
    private static String TAG = "MainFragment";
    private AuthInfo mAuthInfo;
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;
    private StatusesAPI mStatusesAPI;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar();
    }

    private void initToolBar() {
        mActivity = getActivity();
        mContext = mActivity;
        mActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.toolbar_home);
        mToolBar = mActivity.findViewById(R.id.toolbar_home);
        mLogUtilinTextView = (TextView) mToolBar.findViewById(R.id.login);
        initWeiBoAccess();
        mLogUtilinTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSsoHandler.authorize(new AuthListener());
            }
        });
    }

    /**
     * 创建微博授权类对象，将应用的信息保存
     */
    private void initWeiBoAccess() {
        mAuthInfo = new AuthInfo(mContext, Constants.APP_KEY,
                Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(mActivity, mAuthInfo);
    }

    /**
     * 发起 SSO 登陆的 Activity 必须重写 onActivityResults
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);// 从 Bundle 中解析 Token
            if (mAccessToken.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(mContext,
                        mAccessToken);//保存Token
                Toast.makeText(mContext, "授权成功", Toast.LENGTH_SHORT)
                        .show();
            }else {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText("首页");
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(30);
        return textView;
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
}

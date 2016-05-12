package com.wenming.weiswift.unlogin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.wenming.weiswift.common.login.AccessTokenKeeper;
import com.wenming.weiswift.common.login.Constants;
import com.wenming.weiswift.login.activity.MainActivity;
import com.wenming.weiswift.unlogin.activity.WebViewActivity;

/**
 * Created by wenmingvs on 16/5/10.
 */
public class BaseFragment extends Fragment {
    public SsoHandler mSsoHandler;
    public Oauth2AccessToken mAccessToken;
    public AuthInfo mAuthInfo;


    public void initAccessToken() {
        mAuthInfo = new AuthInfo(getContext(), Constants.APP_KEY,
                Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(getActivity(), mAuthInfo);
        mAccessToken = AccessTokenKeeper.readAccessToken(getContext());
    }


    class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);// 从 Bundle 中解析 Token
            if (mAccessToken.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(getContext(), mAccessToken);//保存Token
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("fisrtstart", true);
                startActivity(intent);
                getActivity().finish();
            } else {
                Toast.makeText(getContext(), "授权失败", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(getContext(), "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getContext(), "取消授权", Toast.LENGTH_LONG).show();
        }
    }

    public void openLoginWebView() {
        String authurl = "https://open.weibo.cn/oauth2/authorize" + "?" + "client_id=" + Constants.APP_KEY
                + "&response_type=token&redirect_uri=" + Constants.REDIRECT_URL
                + "&key_hash=" + Constants.AppSecret + (TextUtils.isEmpty(Constants.PackageName) ? "" : "&packagename=" + Constants.PackageName)
                + "&display=mobile" + "&scope=" + Constants.SCOPE;

        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra("url", authurl);
        startActivity(intent);
        getActivity().finish();
    }


}

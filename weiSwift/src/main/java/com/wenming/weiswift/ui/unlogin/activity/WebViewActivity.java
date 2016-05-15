package com.wenming.weiswift.ui.unlogin.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.wenming.weiswift.R;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.common.login.Constants;
import com.wenming.weiswift.ui.login.activity.MainActivity;
import com.wenming.weiswift.utils.LogUtil;

/**
 * Created by wenmingvs on 16/5/12.
 */

public class WebViewActivity extends Activity {

    private String sRedirectUri;
    private WebView mWeb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);
        String url = getIntent().getStringExtra("url");
        sRedirectUri = Constants.REDIRECT_URL;
        mWeb = (WebView) findViewById(R.id.webview);
        WebSettings settings = mWeb.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSaveFormData(false);
        settings.setSavePassword(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWeb.setWebViewClient(new MyWebViewClient());
        mWeb.loadUrl(url);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (isUrlRedirected(url)) {
                view.stopLoading();
                handleRedirectedUrl(url);
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (!url.equals("about:blank") && isUrlRedirected(url)) {
                view.stopLoading();
                handleRedirectedUrl(url);
                return;
            }
            super.onPageStarted(view, url, favicon);
        }
    }

    public boolean isUrlRedirected(String url) {
        return url.startsWith(sRedirectUri);
    }

    private void handleRedirectedUrl(String url) {
        if (!url.contains("error")) {
            int tokenIndex = url.indexOf("access_token=");
            int expiresIndex = url.indexOf("expires_in=");
            int refresh_token_Index = url.indexOf("refresh_token=");
            int uid_Index = url.indexOf("uid=");

            String token = url.substring(tokenIndex + 13, url.indexOf("&", tokenIndex));
            String expiresIn = url.substring(expiresIndex + 11, url.indexOf("&", expiresIndex));
            String refresh_token = url.substring(refresh_token_Index + 14, url.indexOf("&", refresh_token_Index));
            String uid = url.substring(uid_Index + 4);


            LogUtil.d("url = " + url);
            LogUtil.d("token = " + token);
            LogUtil.d("expires_in = " + expiresIn);
            LogUtil.d("refresh_token = " + refresh_token);
            LogUtil.d("uid = " + uid);

            Oauth2AccessToken mAccessToken = new Oauth2AccessToken();
            mAccessToken.setToken(token);
            mAccessToken.setExpiresIn(expiresIn);
            mAccessToken.setRefreshToken(refresh_token);
            mAccessToken.setUid(uid);
            AccessTokenKeeper.writeAccessToken(WebViewActivity.this, mAccessToken);
            Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
            intent.putExtra("fisrtstart", true);
            startActivity(intent);
            finish();

        }
    }

}

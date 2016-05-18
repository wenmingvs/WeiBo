package com.wenming.weiswift.mvp.presenter.imp;

import android.content.Context;

import com.wenming.weiswift.mvp.model.TokenListModel;
import com.wenming.weiswift.mvp.model.imp.TokenListModelImp;
import com.wenming.weiswift.mvp.presenter.WebViewActivityPresent;
import com.wenming.weiswift.mvp.view.WebViewActivityView;
import com.wenming.weiswift.utils.LogUtil;

/**
 * Created by wenmingvs on 16/5/18.
 */
public class WebViewActivityPresentImp implements WebViewActivityPresent {

    private WebViewActivityView mWebViewActivityView;
    private TokenListModel mTokenListModel;

    public WebViewActivityPresentImp(WebViewActivityView webViewActivityView) {
        this.mWebViewActivityView = webViewActivityView;
        mTokenListModel = new TokenListModelImp();
    }

    public void handleRedirectedUrl(Context context, String url) {
        if (!url.contains("error")) {
            int tokenIndex = url.indexOf("access_token=");
            int expiresIndex = url.indexOf("expires_in=");
            int refresh_token_Index = url.indexOf("refresh_token=");
            int uid_Index = url.indexOf("uid=");

            String token = url.substring(tokenIndex + 13, url.indexOf("&", tokenIndex));
            String expiresIn = url.substring(expiresIndex + 11, url.indexOf("&", expiresIndex));
            String refresh_token = url.substring(refresh_token_Index + 14, url.indexOf("&", refresh_token_Index));
            String uid = new String();
            if (url.contains("scope=")) {
                uid = url.substring(uid_Index + 4, url.indexOf("&", uid_Index));
            } else {
                uid = url.substring(uid_Index + 4);
            }

            LogUtil.d("uid= " + uid);

            mTokenListModel.addToken(context, token, expiresIn, refresh_token, uid);
            mWebViewActivityView.startMainActivity();
        }
    }


}

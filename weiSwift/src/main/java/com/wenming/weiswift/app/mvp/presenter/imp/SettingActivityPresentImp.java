package com.wenming.weiswift.app.mvp.presenter.imp;

import android.content.Context;
import android.content.Intent;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wenming.weiswift.app.common.oauth.AccessTokenManager;
import com.wenming.weiswift.app.main.activity.MainActivity;
import com.wenming.weiswift.app.mvp.model.TokenListModel;
import com.wenming.weiswift.app.mvp.model.imp.TokenListModelImp;
import com.wenming.weiswift.app.mvp.presenter.SettingActivityPresent;
import com.wenming.weiswift.app.mvp.view.SettingActivityView;
import com.wenming.weiswift.app.unlogin.activity.UnLoginActivity;
import com.wenming.weiswift.utils.ToastUtil;

/**
 * Created by wenmingvs on 16/5/18.
 */
public class SettingActivityPresentImp implements SettingActivityPresent {

    private SettingActivityView settingActivityView;
    private TokenListModel tokenListModel;

    public SettingActivityPresentImp(SettingActivityView settingActivityView) {
        this.settingActivityView = settingActivityView;
        tokenListModel = new TokenListModelImp();
    }

    @Override
    public void logout(final Context context) {
        tokenListModel.deleteToken(context, AccessTokenManager.getInstance().getOAuthToken().getUid());
        AccessTokenManager.getInstance().clearAccessToken();
        tokenListModel.switchToken(context, 0, new TokenListModel.OnTokenSwitchListener() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }

            @Override
            public void onError(String error) {
                Intent intent = new Intent(context, UnLoginActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public void clearCache(Context context) {
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().clearMemoryCache();
        ToastUtil.showShort(context, "缓存清理成功！");
    }
}

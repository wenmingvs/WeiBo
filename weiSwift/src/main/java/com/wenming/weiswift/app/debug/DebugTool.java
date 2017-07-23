package com.wenming.weiswift.app.debug;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.wenming.weiswift.app.common.ThreadHelper;
import com.wenming.weiswift.app.config.AppConfig;
import com.wenming.weiswift.app.utils.ToastUtils;

/**
 * Debug 辅助工具接口,只在 Debug 状态下启用
 */
public class DebugTool {
    private final static String TAG = "DebugTool";

    public static void showEnvironment(Context context) {
        if (AppConfig.isDebug()) {
            showToast(context, "您处于测试环境");
        }
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getResources().getText(resId));
    }

    public static void showToast(final Context context, final CharSequence text) {
        if (AppConfig.isDebug()) {
            ThreadHelper.instance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Context finalContext = context.getApplicationContext();
                    ToastUtils.showGreen(finalContext, text);
                }
            });
        }
    }

    public static void installLeakCanary(Application application) {
        if (AppConfig.openLeakCanary()) {
            LeakCanary.install(application);
        }
    }
}

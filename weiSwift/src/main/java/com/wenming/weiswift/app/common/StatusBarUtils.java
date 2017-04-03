package com.wenming.weiswift.app.common;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wenming.weiswift.R;
import com.wenming.weiswift.utils.LogUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 用于设置状态栏的颜色，包括背景色和文字的颜色
 * Created by wenmingvs on 16/8/12.
 */
public final class StatusBarUtils {

    /**
     * 是否处理stausbar的文字为深色
     */
    private boolean lightStatusBar = true;
    /**
     * 是否把状态栏设置为透明的
     */
    private boolean transparentStatusBar = false;
    /**
     * 是否把导航栏设置为透明的
     */
    private boolean transparentNavigationbar = false;
    /**
     * 窗口，外出传入
     */
    private final Window window;
    /**
     * 导航栏的view，外部传入，可以返回导航栏的高度和偏移位置
     */
    private final View actionBarView;
    /**
     * 需要把statusBar设置为什么颜色
     */
    private final int statusBarColor;
    /**
     * 当前的sdk版本
     */
    private final int current = Build.VERSION.SDK_INT;

    /**
     * 支持状态栏文字变色的机型
     */
    private ArrayList<String> mChangeTextMobilelist = new ArrayList<>(Arrays.asList("Meizu", "Xiaomi"));


    private StatusBarUtils(Window window, boolean lightStatusBar, boolean transparentStatusBar, boolean transparentNavigationbar, View actionBarView, int statusBarColor) {
        this.lightStatusBar = lightStatusBar;
        this.transparentStatusBar = transparentStatusBar;
        this.window = window;
        this.transparentNavigationbar = transparentNavigationbar;
        this.actionBarView = actionBarView;
        this.statusBarColor = statusBarColor;
    }

    /**
     * 配置完成后，调用此方法开始根据配置对标题栏做一系列处理
     *
     * @param activity
     */
    private void process(Activity activity) {
        //处理状态栏透明度
        if (current >= Build.VERSION_CODES.KITKAT && current < Build.VERSION_CODES.M) {
            processKitkat(activity);
        } else if (current == Build.VERSION_CODES.LOLLIPOP) {
            //处理状态栏透明度的同时，还需要且设置statusbar的文字变色
            processM();
        } else if (current >= Build.VERSION_CODES.M) {
            //处理状态栏透明度的同时，还需要且设置statusbar的文字变色
            processM();
        }
        //处理android4.4.4到android5.0的状态栏的文字变色
        processPrivateAPI();
    }

    /**
     * 调用私有API处理颜色
     */
    private void processPrivateAPI() {
        try {
            processFlyMe(lightStatusBar);
        } catch (Exception e) {
            try {
                processMIUI(lightStatusBar);
            } catch (Exception e2) {

            }
        }
    }


    /**
     * 处理4.4~5.0沉浸,把状态栏变成透明的，然后叠一层纯色的view上去
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void processKitkat(Activity activity) {
        WindowManager.LayoutParams winParams = window.getAttributes();
        //只有支持状态栏文字变色的机型，才修改状态栏为全透明
        LogUtil.d("Build.MANUFACTURER = " + Build.MANUFACTURER);
        if (mChangeTextMobilelist.contains(Build.MANUFACTURER) && transparentStatusBar) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        window.setAttributes(winParams);
        if (mChangeTextMobilelist.contains(Build.MANUFACTURER)) {
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            // 使用颜色资源
            tintManager.setStatusBarTintResource(R.color.home_status_text);
        }
    }

    /**
     * 改变小米的状态栏字体颜色为黑色, 要求MIUI6以上
     * Tested on: MIUIV7 5.0 Redmi-Note3
     */
    private void processMIUI(boolean lightStatusBar) throws Exception {
        Class<? extends Window> clazz = window.getClass();
        int darkModeFlag;
        Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
        Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
        darkModeFlag = field.getInt(layoutParams);
        Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
        extraFlagField.invoke(window, lightStatusBar ? darkModeFlag : 0, darkModeFlag);
    }

    /**
     * 改变魅族的状态栏字体为黑色，要求FlyMe4以上
     */
    private void processFlyMe(boolean isLightStatusBar) throws Exception {
        WindowManager.LayoutParams lp = window.getAttributes();
        Class<?> instance = Class.forName("android.view.WindowManager$LayoutParams");
        int value = instance.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON").getInt(lp);
        Field field = instance.getDeclaredField("meizuFlags");
        field.setAccessible(true);
        int origin = field.getInt(lp);
        if (isLightStatusBar) {
            field.set(lp, origin | value);
        } else {
            field.set(lp, (~value) & origin);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void processM() {
        if (current < Build.VERSION_CODES.M) {
            return;
        }
        int flag = window.getDecorView().getSystemUiVisibility();
        if (lightStatusBar) {
            //改变字体颜色
            flag |= (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(statusBarColor);
        }
        if (transparentStatusBar) {
            //flag |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            flag |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.setStatusBarColor(statusBarColor);
        }
        if (transparentNavigationbar) {
            flag |= (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            window.setNavigationBarColor(statusBarColor);
        }
        window.getDecorView().setSystemUiVisibility(flag);
    }

    public static Builder from(Activity activity) {
        return new Builder().setWindow(activity);
    }

    public static Builder from(Dialog dialog) {
        return new Builder().setWindow(dialog);
    }

    public static Builder from(Window window) {
        return new Builder().setWindow(window);
    }

    /**
     * 配置类
     */
    final public static class Builder {
        private Window window;
        private boolean lightStatusBar = false;
        private boolean transparentStatusbar = false;
        private boolean transparentNavigationbar = false;
        private int statusBarColor;
        private View actionBarView;

        public Builder setActionbarView(@Nullable View actionbarView) {
            this.actionBarView = actionbarView;
            return this;
        }

        private Builder setWindow(@NonNull Window Window) {
            this.window = Window;
            return this;
        }

        private Builder setWindow(@NonNull Activity activity) {
            this.window = activity.getWindow();
            return this;
        }

        private Builder setWindow(@NonNull Dialog dialog) {
            this.window = dialog.getWindow();
            return this;
        }

        public Builder setLightStatusBar(boolean lightStatusBar) {
            this.lightStatusBar = lightStatusBar;
            return this;
        }

        public Builder setTransparentStatusbar(boolean transparentStatusbar) {
            this.transparentStatusbar = transparentStatusbar;
            return this;
        }

        public Builder setTransparentNavigationbar(boolean transparentNavigationbar) {
            this.transparentNavigationbar = transparentNavigationbar;
            return this;
        }

        public Builder setStatusBarColor(int statusBarColor) {
            this.statusBarColor = statusBarColor;
            return this;
        }

        public void process(Activity activity) {
            new StatusBarUtils(window, lightStatusBar, transparentStatusbar, transparentNavigationbar,
                    actionBarView, statusBarColor).process(activity);
        }
    }
}
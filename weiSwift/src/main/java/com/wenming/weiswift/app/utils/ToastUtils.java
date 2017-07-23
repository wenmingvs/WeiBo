package com.wenming.weiswift.app.utils;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.support.annotation.MainThread;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wenming.weiswift.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * 自定义Toast:
 * <p>
 * 一种红色背景
 * <p>
 * 一种普通灰色背景
 * <p>
 * 一种普通灰色背景带left icon
 * <p>
 * Created by wenming on 2017/7/23 18:08
 */
public final class ToastUtils {
    private static final int TOAST_OFFSET_Y = 60;
    private static Toast sToast;
    private static OppoToast sOppoToast;
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    @MainThread
    public static void show(Context context, int resId) {
        show(context, context.getResources().getText(resId));
    }

    @MainThread
    public static void show(Context context, CharSequence text) {
        configureAndShow(context, text, R.layout.toast_gray);
    }

    @MainThread
    public static void showRed(Context context, int resId) {
        showRed(context, context.getResources().getText(resId));
    }

    @MainThread
    public static void showRed(Context context, CharSequence text) {
        configureAndShow(context, text, R.layout.toast_red);
    }

    @MainThread
    public static void showGreen(Context context, CharSequence text) {
        configureAndShow(context, text, R.layout.toast_green);
    }

    private static void configureAndShow(Context context, CharSequence text, int resource) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(resource, null);
        TextView showTextTv = (TextView) layout.findViewById(R.id.toast_show_text_tv);
        showTextTv.setText(text);
        showToast(context, layout);
    }

    /**
     * 显示Toast
     * 备注：Oppo默认关闭通知权限，会导致Toast无法显示，目前使用自定义的OppoToast来显示
     *
     * @param context   上下文
     * @param toastView 自定义的View
     */
    private static void showToast(Context context, View toastView) {
        boolean isOppo = Build.BRAND.equalsIgnoreCase("oppo");
        //非Oppo的手机,使用系统Toast
        if (!isOppo) {
            showSystemToast(context, toastView);
        } else {
            //Oppo手机默认是关闭的，所以要针对Oppo手机做一下检查，开启的话使用系统的toast，否则使用OppoToast
            boolean isNotificationEnabled = isNotificationEnabled(context);
            if (isNotificationEnabled) {
                showSystemToast(context, toastView);
            } else {
                showOppoToast(context, toastView);
            }
        }
    }

    public static void showSystemToast(Context context, View toastView) {
        if (sToast == null) {
            sToast = new Toast(context);
            sToast.setDuration(Toast.LENGTH_SHORT);
            int offset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, TOAST_OFFSET_Y, context.getResources().getDisplayMetrics());
            sToast.setGravity(Gravity.TOP, 0, offset);
        }
        sToast.setView(toastView);
        sToast.show();
        sOppoToast = null;
    }

    public static void showOppoToast(Context context, View toastView) {
        try {
            if (sOppoToast == null) {
                sOppoToast = new OppoToast(context);
                sOppoToast.setDuration(Toast.LENGTH_SHORT);
                int offset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, TOAST_OFFSET_Y, context.getResources().getDisplayMetrics());
                sOppoToast.setGravity(Gravity.TOP, 0, offset);
            }
            sOppoToast.setView(toastView);
            sOppoToast.show();
            sToast = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过反射的方式，读取AppOpsManager类中的变量值，来确定是否具有通知权限。只有API大于或者等于19，才能使用此方法
     *
     * @param context 上下文
     * @return true：给予了通知权限 false：没有给予通知权限  默认返回true
     */
    private static boolean isNotificationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;
            Class appOpsClass = null; /* Context.APP_OPS_MANAGER */
            try {
                appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
                int value = (int) opPostNotificationValue.get(Integer.class);
                return ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static void release() {
        sToast = null;
        sOppoToast = null;
    }
}

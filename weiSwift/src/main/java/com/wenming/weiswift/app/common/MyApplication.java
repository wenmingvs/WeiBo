/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.wenming.weiswift.app.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.wenming.library.LogReport;
import com.wenming.library.save.imp.CrashWriter;
import com.wenming.library.upload.email.EmailReporter;
import com.wenming.weiswift.app.common.basenet.HttpManager;
import com.wenming.weiswift.app.login.activity.BackgroundActivity;
import com.wenming.weiswift.utils.LogUtil;
import com.wenming.weiswift.utils.SharedPreferencesUtil;

import java.util.LinkedList;
import java.util.List;


public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private static Context sContext;
    private List<Activity> mActivityList = new LinkedList<Activity>();

    public static void initImageLoader(Context context) {
        DisplayImageOptions.Builder disBuilder = new DisplayImageOptions.Builder();
        disBuilder.cacheInMemory(true);
        disBuilder.cacheOnDisk(true);
        disBuilder.considerExifParams(true);
        DisplayImageOptions displayImageOptions = disBuilder.build();
        int memoryCacheSize = (int) Runtime.getRuntime().maxMemory();
        if (memoryCacheSize >= 128 * 1024 * 1024) {
            memoryCacheSize /= 16;
        } else {
            memoryCacheSize /= 20;
        }
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheSize(memoryCacheSize)
                .diskCacheSize(200 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(displayImageOptions);
        ImageLoader.getInstance().init(config.build());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //LeakCanary.install(this);
        initImageLoader(getApplicationContext());
        registerActivityLifecycleCallbacks(this);
        HttpManager.getInstance().init(this);
        initCrashReport();
        //使用亮色(light)主题，不使用夜间模式
        boolean setNightMode = (boolean) SharedPreferencesUtil.get(this, "setNightMode", false);
        LogUtil.d("setNightMode = " + setNightMode);
        if (setNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        sContext = getApplicationContext();
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    private void initCrashReport() {
        initEmailReporter();
        LogReport.getInstance()
                .setCacheSize(30 * 1024 * 1024)//支持设置缓存大小，超出后清空
                .setLogDir(getApplicationContext(), "sdcard/" + this.getString(this.getApplicationInfo().labelRes) + "/")//定义路径为：sdcard/[app name]/
                .setWifiOnly(true)//设置只在Wifi状态下上传，设置为false为Wifi和移动网络都上传
                .setLogSaver(new CrashWriter(getApplicationContext()))//支持自定义保存崩溃信息的样式
                //.setEncryption(new AESEncode()) //支持日志到AES加密或者DES加密，默认不开启
                .init(getApplicationContext());
    }


    public void finishAll() {
        for (Activity activity : mActivityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public void recreateForNightMode() {
        for (Activity activity : mActivityList) {
            if (activity instanceof BackgroundActivity) {
                return;
            }
            if (!activity.isFinishing()) {
                activity.recreate();
            }
        }
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        LogUtil.d("OnCreate = " + activity.getLocalClassName());
        mActivityList.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        LogUtil.d("OnDestroyed = " + activity.getLocalClassName());
        mActivityList.remove(activity);
    }


    /**
     * 使用EMAIL发送日志
     */
    private void initEmailReporter() {
        EmailReporter email = new EmailReporter(this);
        email.setReceiver("wenmingvs@gmail.com");//收件人
        email.setSender("wenmingvs@163.com");//发送人邮箱
        email.setSendPassword("apptest1234");//用于登录第三方的邮件授权码
        email.setSMTPHost("smtp.163.com");//SMTP地址
        email.setPort("465");//SMTP 端口
        LogReport.getInstance().setUploadType(email);
    }

    public static void setContext(Application context) {
        sContext = context;
    }

    public static Context getContext() {
        return sContext;
    }
}
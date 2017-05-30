package com.wenming.weiswift.app.common;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Thread 工具类，维护 app 线程池，用于异步和UI操作
 * <p>
 * {@link #runOnUiThread(Runnable)}
 * <p>
 * {@link #runOnUiThread(Runnable, long)}
 * <p>
 * {@link #runOnWorkThread(Runnable)}
 * <p>
 * Created by wenming on 2017/30/5.
 */
public class ThreadHelper {
    private static volatile ThreadHelper mInstance;

    private ThreadHelper() {
    }

    public static ThreadHelper instance() {
        if (mInstance == null) {
            synchronized (ThreadHelper.class) {
                if (mInstance == null) {
                    mInstance = new ThreadHelper();
                }
            }
        }
        return mInstance;
    }


    private ExecutorService mThreadPool;
    private Handler mHandler;

    /**
     * lazy-loading
     */
    private synchronized void initThreadHelper() {
        if (mThreadPool == null) {
            mThreadPool = Executors.newCachedThreadPool();
        }
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
    }

    public void runOnWorkThread(Runnable action) {
        initThreadHelper();
        mThreadPool.execute(action);
    }

    public void runOnUiThread(Runnable action) {
        initThreadHelper();
        mHandler.post(action);
    }

    public void runOnUiThread(Runnable action, long delay) {
        initThreadHelper();
        mHandler.postDelayed(action, delay);
    }

    public void removeDelayedAction(Runnable action) {
        initThreadHelper();
        mHandler.removeCallbacks(action);
    }
}

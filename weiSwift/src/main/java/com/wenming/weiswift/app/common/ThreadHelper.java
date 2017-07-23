package com.wenming.weiswift.app.common;

import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread 工具类，维护 app 线程池，用于异步和UI操作
 * <p>
 * {@link #runOnUiThread(Runnable)}
 * <p>
 * {@link #runOnUiThread(Runnable, long)}
 * <p>
 * {@link #runOnWorkThread(Task)}
 * <p>
 * {@link #runOnWorkThread(Task, long)}
 * <p>
 * Created by Jay on 2016/11/8.
 */
public class ThreadHelper {
    private static ThreadHelper sInstance;

    private ExecutorService mThreadPool;
    private Handler mHandler;

    private ThreadHelper() {
        mThreadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                30L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new DefaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * The default thread factory
     */
    static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "ThreadHelperPool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            return t;
        }
    }

    public static ThreadHelper instance() {
        if (sInstance == null) {
            synchronized (ThreadHelper.class) {
                if (sInstance == null) {
                    sInstance = new ThreadHelper();
                }
            }
        }
        return sInstance;
    }

    /**
     * @return ThreadHelper的线程池
     */
    public Executor getThreadPoolExecutor() {
        return mThreadPool;
    }

    /**
     * use {@link ThreadHelper#runOnWorkThread(Task)} instead.
     */
    @Deprecated
    public void runOnWorkThread(final Runnable action) {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND + Process.THREAD_PRIORITY_MORE_FAVORABLE);
                action.run();
            }
        });
    }

    /**
     *  use {@link ThreadHelper#runOnWorkThread(Task, long)} instead.
     */
    @Deprecated
    public void runOnWorkThread(final Runnable action, long delayMillis) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                        action.run();
                    }
                });
            }
        }, delayMillis);
    }

    public void runOnWorkThread(Task task) {
        mThreadPool.execute(task);
    }

    public void runOnWorkThread(final Task task, long delayMillis) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mThreadPool.execute(task);
            }
        }, delayMillis);
    }

    public void runOnUiThread(Runnable action) {
        mHandler.post(action);
    }

    public void runOnUiThread(Runnable action, long delayMillis) {
        mHandler.postDelayed(action, delayMillis);
    }

    public void removeDelayedAction(Runnable action) {
        mHandler.removeCallbacks(action);
    }

    public static abstract class Task implements Runnable {
        private int priority = Process.THREAD_PRIORITY_BACKGROUND;

        public Task() {
        }

        /**
         * 传递当前任务运行的线程优先级
         *
         * @param priority {@link Process#THREAD_PRIORITY_BACKGROUND}
         */
        public Task(int priority) {
            this.priority = priority;
        }

        @Override
        public void run() {
            Process.setThreadPriority(priority);
            onRun();
        }

        public abstract void onRun();
    }
}

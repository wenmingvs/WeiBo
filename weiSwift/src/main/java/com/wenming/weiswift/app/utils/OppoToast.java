package com.wenming.weiswift.app.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by wenmingvs on 2016/7/5.
 */
public class OppoToast {

    private static Handler mHandler = new Handler();

    private WindowManager mWindowManager;

    /**
     * how long to show the view for
     */
    private long mDurationMillis;

    private View mView;

    private WindowManager.LayoutParams mParams;

    /**
     * The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     */
    private Context mContext;

    /**
     * The location at which the notification should appear on the screen.
     * @see Gravity
     * @see #getGravity
     */
    private int mGravity;

    /**
     * Show the view or text notification for a long period of time.  This time
     * could be user-definable.
     * @see #setDuration
     */
    private final static int DURATION_SHORT = 2000;

    /**
     * Show the view or text notification for a short period of time.  This time
     * could be user-definable.  This is the default.
     * @see #setDuration
     */
    private final static int DURATION_LONG = 3500;


    public static OppoToast makeText(Context context, String text, long duration){
        return new OppoToast(context)
                .setText(text)
                .setDuration(duration)
                .setGravity(Gravity.BOTTOM, 0, 0);
    }

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     */
    public OppoToast(Context context){
        mContext = context;
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.windowAnimations = android.R.style.Animation_Toast;
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mParams.setTitle("Toast");
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mGravity = Gravity.BOTTOM;
    }

    /**
     * Set the location at which the notification should appear on the screen.
     * @see Gravity
     * @see #getGravity
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public OppoToast setGravity(int gravity, int xOffset, int yOffset) {
        mGravity = gravity;
        mParams.y = yOffset;
        mParams.x = xOffset;
        return this;
    }

    /**
     * Get the location at which the notification should appear on the screen.
     * @see Gravity
     * @see #getGravity
     */
    public int getGravity() {
        return mGravity;
    }

    /**
     * Set how long to show the view for.
     */
    public OppoToast setDuration(long durationMillis) {
        if (durationMillis < 0) {
            mDurationMillis = 0;
        }
        if (durationMillis == Toast.LENGTH_SHORT) {
            mDurationMillis = DURATION_SHORT;
        } else if (durationMillis == Toast.LENGTH_LONG) {
            mDurationMillis = DURATION_LONG;
        } else {
            mDurationMillis = durationMillis;
        }
        return this;
    }

    /**
     * Set the view to show.
     * @see #getView
     */
    public OppoToast setView(View view) {
        if (mView != view){
            handleHide();
        }
        mView = view;
        return this;
    }

    /**
     * Return the view.
     * @see #setView
     */
    public View getView() {
        return mView;
    }

    /**
     * Update the text in a Toast that was previously created using one of the makeText() methods.
     * @param text The new text for the Toast.
     */
    public OppoToast setText(String text) {
        View view = Toast.makeText(mContext, text, Toast.LENGTH_SHORT).getView();
        if (view != null){
            TextView tv = (TextView) view.findViewById(android.R.id.message);
            tv.setText(text);
            setView(view);
        }
        return this;
    }

    /**
     * show the toast,push it into handler queue
     */
    public void show() {
        if (mHandler != null){
            mHandler.post(mShow);
            mHandler.postDelayed(mHide,mDurationMillis);
        }
    }

    /**
     * cancel all toast tasks in queue
     */
    public void cancel() {
        if (mHandler != null){
            mHandler.removeCallbacks(null);
            mHandler.post(mHide);
        }
    }

    /**
     * config the taost view layout and add the view to windows
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void handleShow() {
        if (mView != null) {
            final int finalGravity;
            if (Build.VERSION.SDK_INT >= 14){
                final Configuration config = mView.getContext().getResources().getConfiguration();
                finalGravity = Gravity.getAbsoluteGravity(mGravity, config.getLayoutDirection());
            }else {
                finalGravity = mGravity;
            }
            mParams.gravity = finalGravity;
            if ((finalGravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.FILL_HORIZONTAL) {
                mParams.horizontalWeight = 1.0f;
            }
            if ((finalGravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.FILL_VERTICAL) {
                mParams.verticalWeight = 1.0f;
            }
            if (mView.getParent() != null) {
                mWindowManager.removeView(mView);
            }
            mWindowManager.addView(mView, mParams);
        }
    }

    /**
     * remove view from windows
     */
    private void handleHide() {
        if (mView != null) {
            // note: checking parent() just to make sure the view has
            // been added...  i have seen cases where we get here when
            // the view isn't yet added, so let's try not to crash.
            if (mView.getParent() != null) {
                mWindowManager.removeView(mView);
            }
            mView = null;
        }
    }

    private final Runnable mShow = new Runnable() {
        @Override
        public void run() {
            handleShow();
        }
    };

    private final Runnable mHide = new Runnable() {
        @Override
        public void run() {
            handleHide();
        }
    };

}

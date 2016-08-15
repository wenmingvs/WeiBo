package com.wenming.weiswift.widget.toast;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wenming.weiswift.R;

/**
 * Created on 16-7-4.
 * Usage:
 * LoadedToast.showToast(Context context, String msg)
 */
public class LoadedToast extends Toast {

    private static final int DEFAULT_OFFSET_X = 0;
    private int DEFAULT_OFFSET_Y;

    private static LoadedToast mLoadedToast;
    private static TextView mTvMsg;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    private LoadedToast(Context context, String msg) {
        super(context);
        View mToastView = LayoutInflater.from(context).inflate(R.layout.loaded_toast, null);
        mTvMsg = (TextView) mToastView.findViewById(R.id.toast_msg);
        mTvMsg.setText(msg);
        // toast settings
        DEFAULT_OFFSET_Y = (int) context.getResources().getDimension(R.dimen.dp_45);
        setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, DEFAULT_OFFSET_X, DEFAULT_OFFSET_Y);
        setDuration(Toast.LENGTH_SHORT);
        setView(mToastView);
    }


    public static void showToast(Context context, String msg) {
        if (mLoadedToast == null) {
            mLoadedToast = new LoadedToast(context.getApplicationContext(), msg);
        } else {
            mTvMsg.setText(msg);
        }
        mLoadedToast.show();
    }
}

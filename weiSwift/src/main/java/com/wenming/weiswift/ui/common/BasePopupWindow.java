package com.wenming.weiswift.ui.common;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by wenmingvs on 16/6/5.
 */
public class BasePopupWindow extends PopupWindow {


    private Activity mActivity;

    public BasePopupWindow(Context context, Activity activity) {
        super(context);
        mActivity = activity;
    }

    /**
     * 调整窗口的透明度
     *
     * @param from
     * @param to
     */
    private void setOutBackground(float from, float to) {
        final WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setDuration(600);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp.alpha = (float) animation.getAnimatedValue();
                mActivity.getWindow().setAttributes(lp);
            }
        });
        valueAnimator.start();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        setOutBackground(1.0f, 0.5f);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        setOutBackground(0.5f, 1.0f);
    }

}

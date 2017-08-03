package com.wenming.weiswift.app.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * func: 截断传递给RecyclerView的事件
 * <p>
 * Created by Li WenTao on 2016/8/5 10:27
 */
public class FixClickListenerRelativeLayout extends RelativeLayout {

    public FixClickListenerRelativeLayout(Context context) {
        super(context);
    }

    public FixClickListenerRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixClickListenerRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return ev.getAction() == MotionEvent.ACTION_MOVE;
    }
}

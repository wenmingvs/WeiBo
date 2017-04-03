package com.wenming.weiswift.app.home.weiboitem;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.wenming.weiswift.R;

/**
 * Created by wenmingvs on 16/4/27.
 */
public class SeachHeadView extends RelativeLayout {

    public SeachHeadView(Context context) {
        super(context);
        init(context);
    }

    public SeachHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SeachHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SeachHeadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);

    }

    public void init(Context context) {
        inflate(context, R.layout.headsearchview, this);
    }
}

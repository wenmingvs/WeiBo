package com.wenming.weiswift.home.util;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by wenmingvs on 16/4/15.
 */
public class WeiBoContentClickableSpan extends ClickableSpan {
    @Override
    public void onClick(View widget) {
    }
    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(Color.argb(255, 123, 154, 190));
        ds.setUnderlineText(false);
    }
}

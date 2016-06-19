package com.wenming.weiswift.widget.emojitextview;

import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import android.view.View;

public abstract class ClickableImageSpan extends ImageSpan {
    public ClickableImageSpan(Drawable b, int verticalAlignment) {
        super(b, verticalAlignment);
    }

    public abstract void onClick(View view);
}
package com.wenming.weiswift.ui.login.fragment.message;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by wenmingvs on 16/4/18.
 */
public class ItemSapce extends RecyclerView.ItemDecoration {
    private int space;

    public ItemSapce(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, space, 0, 0);
    }
}

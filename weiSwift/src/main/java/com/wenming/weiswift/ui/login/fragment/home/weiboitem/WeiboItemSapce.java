package com.wenming.weiswift.ui.login.fragment.home.weiboitem;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by wenmingvs on 16/4/18.
 */
public class WeiboItemSapce extends RecyclerView.ItemDecoration {
    private int space;

    public WeiboItemSapce(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) != 0) {
            outRect.set(0, 0, 0, space);
        }
    }
}

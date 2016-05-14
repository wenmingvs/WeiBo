package com.wenming.weiswift.ui.login.fragment.post.idea.imagelist;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by wenmingvs on 16/5/7.
 */
public class ItemSpace extends RecyclerView.ItemDecoration {

    private int space;

    public ItemSpace(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        if ((parent.getChildLayoutPosition(view) + 1) % 2 == 0) {
//            outRect.right = space;
//        }
//
//        outRect.top = space;

        outRect.left = space;
        outRect.top = space;


    }
}

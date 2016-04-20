package com.wenming.weiswift.fragment.home.imagelist;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by wenmingvs on 16/4/17.
 */
public class ImageItemSapce extends RecyclerView.ItemDecoration {
    private int space;

    public ImageItemSapce(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        switch (parent.getAdapter().getItemCount()) {
            case 1:
                break;
            case 2:
                if ((parent.getChildAdapterPosition(view) + 1) == 2) {
                    outRect.left = space / 2;
                }
                break;
            case 3:
                if ((parent.getChildAdapterPosition(view) + 1) == 2 || (parent.getChildAdapterPosition(view) + 1) == 4) {
                    outRect.left = space / 2;
                }
                break;
            case 4:
                if ((parent.getChildAdapterPosition(view) + 1) == 2 || (parent.getChildAdapterPosition(view) + 1) == 4) {
                    outRect.left = space / 2;
                }
                break;
            case 5:
                if ((parent.getChildAdapterPosition(view) + 1) % 3 != 0) {
                    outRect.right = space;
                }
                break;
            case 6:
                if ((parent.getChildAdapterPosition(view) + 1) % 3 != 0) {
                    outRect.right = space;
                }
                break;
            case 7:
                if ((parent.getChildAdapterPosition(view) + 1) % 3 != 0) {
                    outRect.right = space;
                }
                break;
            case 8:
                if ((parent.getChildAdapterPosition(view) + 1) % 3 != 0) {
                    outRect.right = space;
                }
                break;
            case 9:
                if ((parent.getChildAdapterPosition(view) + 1) % 3 != 0) {
                    outRect.right = space;
                }
                break;

        }
        if ((parent.getChildAdapterPosition(view) + 1) >= 3 && parent.getAdapter().getItemCount() <= 4) {
            outRect.top = space;
        }

        if ((parent.getChildAdapterPosition(view) + 1) > 3 && parent.getAdapter().getItemCount() > 4) {
            outRect.top = space;
        }


    }


}


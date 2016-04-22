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

    /**
     * 1张图，不增加间距
     * 2张图，增加2的左间距
     * 3张图，增加2，3左间距
     * 4张图，增加2，4的左间距，增加3，4的上间距
     * 5张图，增加1，2，4的右间距，增加4，5的上间距
     * 6张图，增加1，2，4，5的右间距，增加4，5，6的上间距
     * 7张图。。。
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view) + 1;//从1开始计数

        switch (parent.getAdapter().getItemCount()) {
            case 1:
                break;
            case 2:
                if (position == 2) {
                    outRect.left = space / 2;
                }
                break;
            case 3:
                if ((position) == 2 || (position) == 3) {
                    outRect.left = space / 2;
                }

                break;
            case 4:
                if ((position) == 2 || (position) == 4) {
                    outRect.left = space / 2;
                }
                if (position == 3 || position == 4) {
                    outRect.top = space;
                }
                break;
            case 5:
                if ((position) % 3 != 0) {
                    outRect.right = space;
                }
                if ((position) > 3) {
                    outRect.top = space;
                }
                break;
            case 6:
                if ((position) % 3 != 0) {
                    outRect.right = space;
                }
                if ((position) > 3) {
                    outRect.top = space;
                }
                break;
            case 7:
                if ((position) % 3 != 0) {
                    outRect.right = space;
                }
                if ((position) > 3) {
                    outRect.top = space;
                }
                break;
            case 8:
                if ((position) % 3 != 0) {
                    outRect.right = space;
                }
                if ((position) > 3) {
                    outRect.top = space;
                }
                break;
            case 9:
                if ((position) % 3 != 0) {
                    outRect.right = space;
                }
                if ((position) > 3) {
                    outRect.top = space;
                }
                break;

        }


    }


}


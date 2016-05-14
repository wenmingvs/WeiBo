package com.wenming.weiswift.ui.login.fragment.home.imagedetaillist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wenming.weiswift.R;

/**
 * Created by xiangflight on 2016/4/22.
 */
public class ImageDetailTopBar extends RelativeLayout {

    private View mView;
    private TextView mPageNum;
    private ImageView mMoreOptions;
    private OnMoreOptionsListener mMoreOptionsListener;

    /**
     * @param context 上下文
     * @param attrs   用户获取自定义view的属性
     */
    public ImageDetailTopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mView = LayoutInflater.from(context).inflate(R.layout.home_image_detail_list_top_bar, this);
        mPageNum = (TextView) mView.findViewById(R.id.pageTextId);
        mMoreOptions = (ImageView) mView.findViewById(R.id.more_options);
        setUpMoreOptionsEvent();
    }

    /**
     * 设置了More Options的点击事件，内部调用了监听接口
     */
    private void setUpMoreOptionsEvent() {
        mMoreOptions.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMoreOptionsListener != null) {
                    mMoreOptionsListener.onClick(v);
                }
            }
        });
    }


    /**
     * 设置当前图片的页数
     *
     * @param pageNum
     */
    public void setPageNum(String pageNum) {
        mPageNum.setText(pageNum);
    }

    /**
     * 设置是否显示当前页数，为1时不需要显示
     *
     * @param isVisible
     */
    public void setPageNumVisible(int isVisible) {
        mPageNum.setVisibility(isVisible);
    }

    /**
     * 定义了一个监听接口，用于监听点击More Options事件
     */
    interface OnMoreOptionsListener {
        void onClick(View view);
    }

    /**
     * 传入一个监听接口
     *
     * @param onMoreOptionsListener
     */
    public void setOnMoreOptionsListener(OnMoreOptionsListener onMoreOptionsListener) {
        this.mMoreOptionsListener = onMoreOptionsListener;
    }

}

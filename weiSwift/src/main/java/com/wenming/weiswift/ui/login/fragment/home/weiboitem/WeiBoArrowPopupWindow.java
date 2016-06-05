package com.wenming.weiswift.ui.login.fragment.home.weiboitem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.ui.common.BasePopupWindow;
import com.wenming.weiswift.utils.ScreenUtil;
import com.wenming.weiswift.utils.ToastUtil;

/**
 * Created by xiangflight on 2016/4/22.
 */
public class WeiBoArrowPopupWindow extends BasePopupWindow {

    private View mView;
    private TextView mDeleteTextView;
    private TextView mCollectTextView;
    private TextView mDisfollowTextView;

    public WeiBoArrowPopupWindow(Context context, Status status) {
        super(context, (Activity) context);
        initPopWindow(context);
        mView = LayoutInflater.from(context).inflate(R.layout.home_weiboitem_arrow_popwindow, null);
        this.setContentView(mView);
        initOnClickListener(context);
    }

    private void initPopWindow(Context context) {
        int width = ScreenUtil.getScreenWidth(context) - 320;
        this.setWindowLayoutMode(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(width);
        this.setHeight(1);
        // 设置弹出窗口可点击
        this.setFocusable(true);
        // 设置窗口外部可点击
        this.setOutsideTouchable(true);
        // 设置drawable，必须得设置
        this.setBackgroundDrawable(context.getDrawable(R.drawable.home_weiboitem_arrow_pop_corner_bg));
        // 设置点击外部隐藏window
        this.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    private void initOnClickListener(final Context context) {
        mDeleteTextView = (TextView) mView.findViewById(R.id.pop_deleteweibo);
        mCollectTextView = (TextView) mView.findViewById(R.id.pop_collectweibo);
        mDisfollowTextView = (TextView) mView.findViewById(R.id.pop_disfollow);

        mDeleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(context, "删除一条微博");
            }
        });
        mCollectTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(context, "收藏一条微博");
            }
        });

        mDisfollowTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(context, "取消关注此人");
            }
        });
    }
}

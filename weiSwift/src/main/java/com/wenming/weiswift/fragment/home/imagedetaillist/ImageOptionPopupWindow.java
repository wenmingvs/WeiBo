package com.wenming.weiswift.fragment.home.imagedetaillist;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.wenming.weiswift.R;
import com.wenming.weiswift.common.util.ScreenUtil;

/**
 * Created by xiangflight on 2016/4/22.
 */
public class ImageOptionPopupWindow extends PopupWindow {

    private View mView;

    /**
     * 使用单例模式创建ImageOPtionPopupWindow
     */
    private static volatile ImageOptionPopupWindow mImageOptionPopupWindow;

    public static ImageOptionPopupWindow getInstance(Context context) {
        if (mImageOptionPopupWindow == null) {
            synchronized (ImageOptionPopupWindow.class) {
                if (mImageOptionPopupWindow == null) {
                    mImageOptionPopupWindow = new ImageOptionPopupWindow(context.getApplicationContext());
                }
            }
        }
        return mImageOptionPopupWindow;
    }

    /**
     * 创建一个ImageOptionPopupWindow
     * @param context
     */
    private ImageOptionPopupWindow(Context context) {
        super(context);
        // 设置popupWindow的外部属性
        // 设置宽高
        this.setWidth(ScreenUtil.getScreenWidth(context));
        this.setHeight(ScreenUtil.getScreenHeight(context) * 22 / 100);
        // 设置弹出窗口可点击
        this.setFocusable(true);
        // 设置窗口外部可点击
        this.setOutsideTouchable(true);
        // 设置drawable，必须得设置
        this.setBackgroundDrawable(new BitmapDrawable());
        // 设置弹出window的动画效果，从底部弹出
        this.setAnimationStyle(R.style.image_option_pop_window_anim_style);
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
        // 设置popupwindow的布局
        mView = LayoutInflater.from(context).inflate(R.layout.home_image_detail_list_pop_window, null);
        this.setContentView(mView);
    }

}

package com.wenming.weiswift.ui.login.fragment.home.imagedetaillist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wenming.weiswift.R;
import com.wenming.weiswift.ui.common.BasePopupWindow;
import com.wenming.weiswift.utils.SaveImgUtil;
import com.wenming.weiswift.utils.ScreenUtil;
import com.wenming.weiswift.utils.ToastUtil;

/**
 * Created by xiangflight on 2016/4/22.
 */
public class ImageOptionPopupWindow extends BasePopupWindow {

    private View mView;
    private TextView mCancalTextView;
    private TextView mSavePicTextView;
    private TextView mRetweetTextView;
    private Context mContext;
    private String mImgURL;

    /**
     * 用于加载微博列表图片的配置，进行安全压缩，尽可能的展示图片细节
     */
    private static DisplayImageOptions ImageOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.message_image_default)
            .showImageForEmptyUri(R.drawable.message_image_default)
            .showImageOnFail(R.drawable.timeline_image_failure)
            .bitmapConfig(Bitmap.Config.ARGB_8888)
            .imageScaleType(ImageScaleType.NONE)
            .considerExifParams(true)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();


    public ImageOptionPopupWindow(String url, Context context) {
        super(context, (Activity) context, 300);
        mContext = context;
        mImgURL = url;
        initPopWindow(mContext);
        mView = LayoutInflater.from(mContext).inflate(R.layout.home_image_detail_list_pop_window, null);
        this.setContentView(mView);
        initOnClickListener(mContext);
    }

    private void initPopWindow(Context context) {
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
    }

    private void initOnClickListener(final Context context) {
        mCancalTextView = (TextView) mView.findViewById(R.id.pop_cancal);
        mSavePicTextView = (TextView) mView.findViewById(R.id.pop_savcpic);
        mRetweetTextView = (TextView) mView.findViewById(R.id.pop_retweet);

        mCancalTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mSavePicTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                ImageLoader.getInstance().loadImage(mImgURL, ImageOptions, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        SaveImgUtil.create(mContext).saveImage(loadedImage);
                    }
                });
            }
        });

        mRetweetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                ToastUtil.showShort(context, "转发微博");
            }
        });
    }

}

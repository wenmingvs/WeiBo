package com.wenming.weiswift.ui.login.fragment.home.imagedetaillist;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.wenming.weiswift.R;
import com.wenming.weiswift.utils.SaveImgUtil;
import com.wenming.weiswift.utils.ToastUtil;

import java.io.File;

/**
 * Created by xiangflight on 2016/4/22.
 */
public class ImageDetailDialog extends Dialog {

    private TextView mCancalTextView;
    private TextView mSavePicTextView;
    private TextView mRetweetTextView;
    private Context mContext;
    private String mImgURL;

    /**
     * 用于加载微博列表图片的配置，进行安全压缩，尽可能的展示图片细节
     */
    private static DisplayImageOptions mImageOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.message_image_default)
            .showImageForEmptyUri(R.drawable.message_image_default)
            .showImageOnFail(R.drawable.timeline_image_failure)
            .bitmapConfig(Bitmap.Config.ARGB_8888)
            .imageScaleType(ImageScaleType.NONE)
            .considerExifParams(true)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();
    ;


    public ImageDetailDialog(String url, Context context) {
        super(context, R.style.ImageSaveDialog);
        mContext = context;
        mImgURL = url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_image_detail_list_pop_window);
        mCancalTextView = (TextView) findViewById(R.id.pop_cancal);
        mSavePicTextView = (TextView) findViewById(R.id.pop_savcpic);
        mRetweetTextView = (TextView) findViewById(R.id.pop_retweet);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        setUpListener(mContext);
    }


    private void setUpListener(final Context context) {
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
                ImageLoader.getInstance().loadImage(mImgURL, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        File imgFile = DiskCacheUtils.findInCache(mImgURL, ImageLoader.getInstance().getDiskCache());
                        SaveImgUtil.create(mContext).saveImage(imgFile, loadedImage);
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

package com.wenming.weiswift.fragment.home.imagedetaillist;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wenming.weiswift.common.photoview.PhotoView;
import com.wenming.weiswift.common.photoview.PhotoViewAttacher;
import com.wenming.weiswift.fragment.home.util.FillWeiBoItem;

import java.util.ArrayList;


/**
 * Created by wenmingvs on 16/4/19.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private ArrayList<String> mDatas;
    private Context mContext;
    private DisplayImageOptions options;
    public OnSingleTagListener onSingleTagListener;

    public interface OnSingleTagListener {
        public void onTag();
    }

    public void setOnSingleTagListener(OnSingleTagListener onSingleTagListener) {
        this.onSingleTagListener = onSingleTagListener;
    }


    public ViewPagerAdapter(ArrayList<String> datas, Context context) {
        this.mDatas = datas;
        this.mContext = context;


        options = new DisplayImageOptions.Builder()
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.NONE)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .build();

    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final PhotoView photoView = new PhotoView(container.getContext());
        photoView.setMaximumScale(12f);

        ImageLoader.getInstance().displayImage(mDatas.get(position), photoView, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if (FillWeiBoItem.returnImageType(mContext, bitmap) == FillWeiBoItem.IMAGE_TYPE_LONG_TEXT) {
                    //Toast.makeText(mContext, "onLoadingComplete", Toast.LENGTH_SHORT).show();
                    photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    photoView.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
                        @Override
                        public boolean onSingleTapConfirmed(MotionEvent e) {
                            onSingleTagListener.onTag();
                            // Toast.makeText(mContext, "onSingleTapConfirmed", Toast.LENGTH_SHORT).show();
                            return true;
                        }

                        @Override
                        public boolean onDoubleTap(MotionEvent e) {
                            //photoView.setScale(9.65f, true);
                            return false;
                        }

                        @Override
                        public boolean onDoubleTapEvent(MotionEvent e) {
                            //photoView.setScale(9.65f, true);
                            return false;
                        }
                    });
                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                onSingleTagListener.onTag();
            }

            @Override
            public void onOutsidePhotoTap() {
                onSingleTagListener.onTag();
            }
        });
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

package com.wenming.weiswift.fragment.home.imagedetaillist;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.wenming.weiswift.R;
import com.wenming.weiswift.common.FillContent;
import com.wenming.weiswift.common.photoview.PhotoView;
import com.wenming.weiswift.common.photoview.PhotoViewAttacher;
import com.wenming.weiswift.common.util.LogUtil;

import java.util.ArrayList;


/**
 * Created by wenmingvs on 16/4/19.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private ArrayList<String> mDatas;
    private Context mContext;
    private DisplayImageOptions options;
    public OnSingleTagListener onSingleTagListener;
    private View mView;
    private PhotoView mPhotoView;
    private SimpleDraweeView mSimpleDraweeView;

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
                .showImageOnLoading(R.drawable.bga_refresh_loading01)
                .cacheInMemory(true)
                .cacheOnDisk(true)
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
        mView = LayoutInflater.from(mContext).inflate(R.layout.home_weiboitem_imagedetails_item, null);
        mPhotoView = (PhotoView) mView.findViewById(R.id.PhotoViewID);
        mSimpleDraweeView = (SimpleDraweeView) mView.findViewById(R.id.frescoView);

        if (mDatas.get(position).endsWith(".gif")) {

            String gifURL = getGifUrl(mDatas.get(position));
            LogUtil.d(gifURL);
            mPhotoView.setVisibility(View.INVISIBLE);
            mSimpleDraweeView.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(gifURL);
            mSimpleDraweeView.setImageURI(uri);
            DraweeController draweeController =
                    Fresco.newDraweeControllerBuilder()
                            .setUri(uri)
                            .setAutoPlayAnimations(true)
                            .build();
            mSimpleDraweeView.setController(draweeController);
            mSimpleDraweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSingleTagListener.onTag();
                }
            });
        } else {
            mPhotoView.setVisibility(View.VISIBLE);
            mSimpleDraweeView.setVisibility(View.INVISIBLE);
            ImageLoader.getInstance().displayImage(mDatas.get(position), mPhotoView, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    if (FillContent.returnImageType(mContext, bitmap) == FillContent.IMAGE_TYPE_LONG_TEXT) {
                        //Toast.makeText(mContext, "onLoadingComplete", Toast.LENGTH_SHORT).show();
                        mPhotoView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                        mPhotoView.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
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
            }, new ImageLoadingProgressListener() {
                @Override
                public void onProgressUpdate(String s, View view, int current, int total) {
                    LogUtil.d("onProgressUpdate = " + 100.0f * current / total + "%");
                }
            });


            mPhotoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float v, float v1) {
                    onSingleTagListener.onTag();
                }

                @Override
                public void onOutsidePhotoTap() {
                    onSingleTagListener.onTag();
                }
            });
        }

        container.addView(mView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return mView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    private static String getGifUrl(String thumbnail_url) {
        StringBuffer buffer = new StringBuffer(thumbnail_url);
        buffer.replace(22, 29, "mw690");
        return buffer.toString();
    }
}

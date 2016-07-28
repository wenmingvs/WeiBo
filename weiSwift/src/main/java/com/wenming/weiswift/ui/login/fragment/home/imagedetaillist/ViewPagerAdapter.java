package com.wenming.weiswift.ui.login.fragment.home.imagedetaillist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.wenming.weiswift.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import uk.co.senab.photoview.PhotoView;


/**
 * Created by wenmingvs on 16/4/19.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private ArrayList<String> mDatas;
    private Context mContext;
    private DisplayImageOptions options;
    public OnSingleTagListener onSingleTagListener;
    private View mView;


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
        mView = LayoutInflater.from(container.getContext()).inflate(R.layout.home_weiboitem_imagedetails_item, null);
        final RelativeLayout bgLayout = (RelativeLayout) mView.findViewById(R.id.ImageViewItemLayout);
        final DonutProgress donutProgress = (DonutProgress) mView.findViewById(R.id.donut_progress);

        final SubsamplingScaleImageView longImg = (SubsamplingScaleImageView) mView.findViewById(R.id.longImg);
        final GifImageView gifImageView = (GifImageView) mView.findViewById(R.id.gifView);
        final uk.co.senab.photoview.PhotoView norImgView = (uk.co.senab.photoview.PhotoView) mView.findViewById(R.id.norImg);
        setOnClickListener(bgLayout, longImg, gifImageView, norImgView);
        setOnLongClickListener(bgLayout, longImg, gifImageView, norImgView, position);

        ImageLoader.getInstance().loadImage(mDatas.get(position), null, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                if (DiskCacheUtils.findInCache(mDatas.get(position), ImageLoader.getInstance().getDiskCache()) == null) {
                    donutProgress.setVisibility(View.VISIBLE);
                } else {
                    donutProgress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                donutProgress.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String s, final View view, Bitmap bitmap) {
                File file = DiskCacheUtils.findInCache(mDatas.get(position), ImageLoader.getInstance().getDiskCache());
                if (mDatas.get(position).endsWith(".gif")) {
                    gifImageView.setVisibility(View.VISIBLE);
                    longImg.setVisibility(View.INVISIBLE);
                    norImgView.setVisibility(View.INVISIBLE);
                    displayGif(file, gifImageView);
                } else if (bitmap.getHeight() > bitmap.getWidth() * 3) {
                    longImg.setVisibility(View.VISIBLE);
                    gifImageView.setVisibility(View.INVISIBLE);
                    norImgView.setVisibility(View.INVISIBLE);
                    displayLongPic(file, bitmap, longImg);
                } else {
                    norImgView.setVisibility(View.VISIBLE);
                    gifImageView.setVisibility(View.INVISIBLE);
                    longImg.setVisibility(View.INVISIBLE);
                    displayNormalImg(file, bitmap, norImgView);
                }
                donutProgress.setProgress(100);
                donutProgress.setVisibility(View.GONE);
            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String s, View view, int current, int total) {
                donutProgress.setProgress((int) 100.0f * current / total);
            }
        });
        container.addView(mView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return mView;
    }

    private void setOnLongClickListener(final RelativeLayout bgLayout, SubsamplingScaleImageView longImg, GifImageView gifImageView, uk.co.senab.photoview.PhotoView photoView, final int position) {
        longImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopWindow(bgLayout, position);
                return false;
            }
        });
        gifImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopWindow(bgLayout, position);
                return false;
            }
        });
        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopWindow(bgLayout, position);
                return false;
            }
        });
    }

    private void setOnClickListener(RelativeLayout relativeLayout, SubsamplingScaleImageView longImg, GifImageView gifImageView, uk.co.senab.photoview.PhotoView photoView) {
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSingleTagListener.onTag();
            }
        });
        longImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSingleTagListener.onTag();
            }
        });
        gifImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSingleTagListener.onTag();
            }
        });
        photoView.setOnPhotoTapListener(new uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener() {
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

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void displayGif(File file, GifImageView gifImageView) {
        try {
            GifDrawable gifDrawable = new GifDrawable(file);
            gifImageView.setImageDrawable(gifDrawable);
        } catch (IOException e) {
            Log.e("wenming", e.getMessage());
            e.printStackTrace();
        }
    }

    private void displayNormalImg(File file, Bitmap bitmap, PhotoView photoView) {
        photoView.setImageBitmap(bitmap);
    }

    private void displayLongPic(File file, Bitmap bitmap, SubsamplingScaleImageView longImg) {
        longImg.setQuickScaleEnabled(true);
        longImg.setZoomEnabled(true);
        longImg.setPanEnabled(true);
        longImg.setDoubleTapZoomDuration(100);
        longImg.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
        longImg.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
        longImg.setImage(ImageSource.uri(file.getAbsolutePath()), new ImageViewState(0, new PointF(0, 0), 0));
    }

    private void showPopWindow(View parent, int position) {
        ImageOptionPopupWindow mPopupWindow = new ImageOptionPopupWindow(mDatas.get(position), mContext);
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        } else {
            mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        }
    }


}

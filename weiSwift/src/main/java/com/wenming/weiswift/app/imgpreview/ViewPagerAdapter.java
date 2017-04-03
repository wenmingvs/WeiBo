package com.wenming.weiswift.app.imgpreview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.daimajia.numberprogressbar.NumberProgressBar;
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
import com.wenming.weiswift.app.common.ImageUtil;
import com.wenming.weiswift.app.common.NewFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by wenmingvs on 16/4/19.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private ArrayList<String> mDatas;
    private Context mContext;
    private DisplayImageOptions options;
    public OnSingleTagListener onSingleTagListener;

    public interface OnSingleTagListener {
        void onTag();
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
        View mView = LayoutInflater.from(container.getContext()).inflate(R.layout.home_weiboitem_imagedetails_item, null);

        final FrameLayout bgLayout = (FrameLayout) mView.findViewById(R.id.bgLayout);
        //预览缩略图的控件
        final PhotoView preNorImg = (PhotoView) mView.findViewById(R.id.previewImg);
        final SubsamplingScaleImageView preLongImg = (SubsamplingScaleImageView) mView.findViewById(R.id.previewLongImg);
        //显示原图的控件
        final SubsamplingScaleImageView longImg = (SubsamplingScaleImageView) mView.findViewById(R.id.longImg);
        final GifImageView gifImg = (GifImageView) mView.findViewById(R.id.gifView);
        final PhotoView norImgView = (PhotoView) mView.findViewById(R.id.norImg);
        //底部的进度条
        final NumberProgressBar progressBar = (NumberProgressBar) mView.findViewById(R.id.donut_progress);

        setOnClickListener(preNorImg, preLongImg, bgLayout, longImg, gifImg, norImgView);
        setOnLongClickListener(preNorImg, preLongImg, bgLayout, longImg, gifImg, norImgView, position);

        ImageLoader.getInstance().loadImage(mDatas.get(position), null, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                progressBar.setVisibility(View.VISIBLE);
                showPreviewImg(mDatas.get(position), preNorImg, preLongImg);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String s, final View view, Bitmap bitmap) {
                File file = DiskCacheUtils.findInCache(mDatas.get(position), ImageLoader.getInstance().getDiskCache());

                if (mDatas.get(position).endsWith(".gif")) {
                    gifImg.setVisibility(View.VISIBLE);
                    longImg.setVisibility(View.GONE);
                    norImgView.setVisibility(View.GONE);
                    if (file == null) {
                        return;
                    }
                    displayGif(file, gifImg);
                } else if (ImageUtil.isLongImg(file, bitmap)) {
                    longImg.setVisibility(View.VISIBLE);
                    gifImg.setVisibility(View.GONE);
                    norImgView.setVisibility(View.GONE);

                    if (file == null) {
                        return;
                    }
                    displayLongPic(file, longImg);
                    longImg.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hidePreviewImg(preNorImg, preLongImg);
                            progressBar.setProgress(100);
                            progressBar.setVisibility(View.GONE);
                        }
                    }, 500);
                } else {
                    norImgView.setVisibility(View.VISIBLE);
                    gifImg.setVisibility(View.GONE);
                    longImg.setVisibility(View.GONE);
                    displayNormalImg(bitmap, norImgView);
                }
                hidePreviewImg(preNorImg, preLongImg);
                progressBar.setProgress(100);
                progressBar.setVisibility(View.GONE);
            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String s, View view, int current, int total) {
                progressBar.setProgress((int) 100.0f * current / total);
            }
        });
        container.addView(mView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return mView;
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

    private void displayNormalImg(Bitmap bitmap, PhotoView photoView) {
        photoView.setImageBitmap(bitmap);
    }

    private void displayLongPic(File file, SubsamplingScaleImageView longImg) {
        longImg.setQuickScaleEnabled(true);
        longImg.setZoomEnabled(true);
        longImg.setPanEnabled(true);
        longImg.setDoubleTapZoomDuration(100);
        longImg.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
        longImg.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
        longImg.setImage(ImageSource.uri(file.getAbsolutePath()), new ImageViewState(0, new PointF(0, 0), 0));
    }

    private void showPopWindow(View parent, int position) {
        SaveImageDialog.showDialog(mDatas.get(position), mContext);
    }

    private void showPreviewImg(final String url, final PhotoView preNorImg, final SubsamplingScaleImageView preLongImg) {
        //直接显示Gif
        if (url.endsWith("gif")) {
            return;
        }
        //存在原图，直接显示原图
        File orgFile = DiskCacheUtils.findInCache(url, ImageLoader.getInstance().getDiskCache());
        if (orgFile != null) {
            hidePreviewImg(preNorImg, preLongImg);
            return;
        }
        String replacement = "";
        if (NewFeature.timeline_img_quality == NewFeature.bmiddle_quality) {
            replacement = "bmiddle";
        } else if (NewFeature.timeline_img_quality == NewFeature.thumbnail_quality) {
            replacement = "thumbnail";
        } else {
            replacement = "large";
        }
        final String bmiddleUrl = url.replace("large", replacement);
        //加载图片
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .build();


        ImageLoader.getInstance().loadImage(bmiddleUrl, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                File bimiddleImg = DiskCacheUtils.findInCache(bmiddleUrl, ImageLoader.getInstance().getDiskCache());
                //存储卡空间不足
                if (bimiddleImg == null) {
                    //ToastUtil.showShort(mContext, "缩略图不存在");
                    return;
                }
                //如果是长图，显示长图
                if (ImageUtil.isLongImg(bimiddleImg, loadedImage)) {
                    //ToastUtil.showShort(mContext, "显示长图预览");
                    preNorImg.setVisibility(View.GONE);
                    preLongImg.setVisibility(View.VISIBLE);
                    displayLongPic(bimiddleImg, preLongImg);
                } else {
                    //ToastUtil.showShort(mContext, "显示普通图片预览");
                    preNorImg.setVisibility(View.VISIBLE);
                    preLongImg.setVisibility(View.GONE);
                    displayNormalImg(loadedImage, preNorImg);
                }
            }
        });
    }

    private void hidePreviewImg(PhotoView norImg, final SubsamplingScaleImageView longImg) {
        if (norImg.getVisibility() == View.VISIBLE){
            norImg.setVisibility(View.GONE);
        }
        if (longImg.getVisibility() == View.VISIBLE){
            longImg.setVisibility(View.GONE);
        }
    }

    private void setOnLongClickListener(PhotoView preNorImg, SubsamplingScaleImageView preLongImg, final FrameLayout bgLayout, SubsamplingScaleImageView longImg, GifImageView gifImageView, PhotoView photoView, final int position) {
        preNorImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopWindow(bgLayout, position);
                return false;
            }
        });

        preLongImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopWindow(bgLayout, position);
                return false;
            }
        });

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

    private void setOnClickListener(PhotoView preNorImg, SubsamplingScaleImageView preLongImg, FrameLayout bgLayout, SubsamplingScaleImageView longImg, GifImageView gifImageView, PhotoView normalImg) {
        bgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSingleTagListener.onTag();
            }
        });
        preNorImg.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                onSingleTagListener.onTag();
            }

            @Override
            public void onOutsidePhotoTap() {
                onSingleTagListener.onTag();
            }
        });
        preLongImg.setOnClickListener(new View.OnClickListener() {
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
        normalImg.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
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
}

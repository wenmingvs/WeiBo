package com.wenming.weiswift.ui.login.fragment.home.imagedetaillist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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
import com.wenming.weiswift.ui.common.ImageUtil;
import com.wenming.weiswift.utils.ToastUtil;

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
        final NumberProgressBar progressBar = (NumberProgressBar) mView.findViewById(R.id.donut_progress);

        final PhotoView preImageView = (PhotoView) mView.findViewById(R.id.previewImg);
        final SubsamplingScaleImageView previewLongImg = (SubsamplingScaleImageView) mView.findViewById(R.id.previewLongImg);

        final SubsamplingScaleImageView longImg = (SubsamplingScaleImageView) mView.findViewById(R.id.longImg);
        final GifImageView gifImageView = (GifImageView) mView.findViewById(R.id.gifView);
        final PhotoView norImgView = (PhotoView) mView.findViewById(R.id.norImg);


        setOnClickListener(preImageView,previewLongImg, bgLayout, longImg, gifImageView, norImgView);
        setOnLongClickListener(preImageView,previewLongImg, bgLayout, longImg, gifImageView, norImgView, position);

        ImageLoader.getInstance().loadImage(mDatas.get(position), null, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                progressBar.setVisibility(View.VISIBLE);
                showPreviewImg(mDatas.get(position).replace("large", "bmiddle"), preImageView, previewLongImg);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                progressBar.setVisibility(View.INVISIBLE);
                showPreviewImg(mDatas.get(position).replace("large", "bmiddle"), preImageView, previewLongImg);
            }

            @Override
            public void onLoadingComplete(String s, final View view, Bitmap bitmap) {
                hidePreviewImg(preImageView, previewLongImg);
                if (mDatas.get(position).endsWith(".gif")) {
                    gifImageView.setVisibility(View.VISIBLE);
                    longImg.setVisibility(View.INVISIBLE);
                    norImgView.setVisibility(View.INVISIBLE);
                    File file = DiskCacheUtils.findInCache(mDatas.get(position), ImageLoader.getInstance().getDiskCache());
                    if (file == null) {
                        return;
                    }
                    displayGif(file, gifImageView);
                } else if (bitmap.getHeight() > bitmap.getWidth() * 3) {
                    longImg.setVisibility(View.VISIBLE);
                    gifImageView.setVisibility(View.INVISIBLE);
                    norImgView.setVisibility(View.INVISIBLE);
                    File file = DiskCacheUtils.findInCache(mDatas.get(position), ImageLoader.getInstance().getDiskCache());
                    if (file == null) {
                        return;
                    }
                    displayLongPic(file, longImg);
                } else {
                    norImgView.setVisibility(View.VISIBLE);
                    gifImageView.setVisibility(View.INVISIBLE);
                    longImg.setVisibility(View.INVISIBLE);
                    displayNormalImg(bitmap, norImgView);
                }
                progressBar.setProgress(100);
                progressBar.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
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

    private void setOnLongClickListener(PhotoView preImageView, SubsamplingScaleImageView previewLongImg, final RelativeLayout bgLayout, SubsamplingScaleImageView longImg, GifImageView gifImageView, PhotoView photoView, final int position) {
        preImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopWindow(bgLayout, position);
                return false;
            }
        });
        previewLongImg.setOnLongClickListener(new View.OnLongClickListener() {
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

    private void setOnClickListener(PhotoView preImageView, SubsamplingScaleImageView previewLongImg, RelativeLayout relativeLayout, SubsamplingScaleImageView longImg, GifImageView gifImageView, PhotoView normalImg) {
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSingleTagListener.onTag();
            }
        });
        preImageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                onSingleTagListener.onTag();
            }

            @Override
            public void onOutsidePhotoTap() {
                onSingleTagListener.onTag();
            }
        });
        previewLongImg.setOnClickListener(new View.OnClickListener() {
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
        if (url.endsWith("gif")) {
            return;
        }
        //加载图片
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .build();
        ImageLoader.getInstance().displayImage(url, preNorImg, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                File bimiddleImg = DiskCacheUtils.findInCache(url, ImageLoader.getInstance().getDiskCache());
                //存储卡空间不足
                if (bimiddleImg == null) {
                    ToastUtil.showShort(mContext, "存储卡空间不足，显示失败");
                    return;
                }
                //如果是长图，显示长图
                if (ImageUtil.isLongImg(bimiddleImg, loadedImage)) {
                    preNorImg.setVisibility(View.INVISIBLE);
                    preLongImg.setVisibility(View.VISIBLE);
                    displayLongPic(bimiddleImg, preLongImg);
                } else {
                    preNorImg.setVisibility(View.VISIBLE);
                    preLongImg.setVisibility(View.INVISIBLE);
                    displayNormalImg(loadedImage, preNorImg);
                }
            }
        });
    }

    private void hidePreviewImg(PhotoView norImg, SubsamplingScaleImageView longImg) {
        norImg.setVisibility(View.GONE);
        longImg.setVisibility(View.GONE);
    }

}

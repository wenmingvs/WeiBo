package com.wenming.weiswift.ui.login.fragment.home.imagedetaillist;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.photoviewlib.PhotoView;
import com.example.photoviewlib.PhotoViewAttacher;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.wenming.weiswift.R;
import com.wenming.weiswift.ui.common.FillContent;
import com.wenming.weiswift.utils.LogUtil;

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
        final RelativeLayout imageViewItemLayout = (RelativeLayout) mView.findViewById(R.id.ImageViewItemLayout);
        final PhotoView photoView = (PhotoView) mView.findViewById(R.id.PhotoViewID);
        final SimpleDraweeView simpleDraweeView = (SimpleDraweeView) mView.findViewById(R.id.frescoView);
        final DonutProgress donutProgress = (DonutProgress) mView.findViewById(R.id.donut_progress);

        imageViewItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSingleTagListener.onTag();
            }
        });

        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopWindow(imageViewItemLayout, position);
                return false;
            }
        });

        simpleDraweeView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopWindow(imageViewItemLayout, position);
                return false;
            }
        });

        if (mDatas.get(position).endsWith(".gif")) {
            donutProgress.setVisibility(View.GONE);
            String gifURL = mDatas.get(position);
            LogUtil.d(gifURL);
            photoView.setVisibility(View.INVISIBLE);
            simpleDraweeView.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(gifURL);
            simpleDraweeView.setImageURI(uri);
            DraweeController draweeController =
                    Fresco.newDraweeControllerBuilder()
                            .setUri(uri)
                            .setAutoPlayAnimations(true)
                            .build();
            simpleDraweeView.setController(draweeController);
            simpleDraweeView.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
            simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSingleTagListener.onTag();
                }
            });
       simpleDraweeView.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {
               return false;
           }
       });

        } else {
            photoView.setVisibility(View.VISIBLE);
            simpleDraweeView.setVisibility(View.INVISIBLE);
            //后台下载高质量的图片
            ImageLoader.getInstance().displayImage(mDatas.get(position), photoView, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    donutProgress.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    donutProgress.setVisibility(View.GONE);
                }

                /**
                 * 点击图片后，识别是否是长微博，如果是，则使用PhotoView进行加载
                 * @param s
                 * @param view
                 * @param bitmap
                 */
                @Override
                public void onLoadingComplete(String s, final View view, Bitmap bitmap) {
                    donutProgress.setProgress(100);
                    if (FillContent.returnImageType(mContext, bitmap) == FillContent.IMAGE_TYPE_LONG_TEXT) {
                        ((PhotoView) view).setScaleType(ImageView.ScaleType.CENTER_CROP);
                        ((PhotoView) view).setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
                            @Override
                            public boolean onSingleTapConfirmed(MotionEvent e) {
                                onSingleTagListener.onTag();
                                return true;
                            }

                            @Override
                            public boolean onDoubleTap(MotionEvent e) {
                                ((PhotoView) view).setScaleType(ImageView.ScaleType.CENTER_CROP);
                                return false;
                            }

                            @Override
                            public boolean onDoubleTapEvent(MotionEvent e) {
                                return false;
                            }
                        });
                    }
                    donutProgress.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                }
            }, new ImageLoadingProgressListener() {
                @Override
                public void onProgressUpdate(String s, View view, int current, int total) {
                    donutProgress.setProgress((int) 100.0f * current / total);
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
        }

        container.addView(mView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return mView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
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

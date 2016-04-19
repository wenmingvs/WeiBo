package com.wenming.weiswift.home.imagedetails;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by wenmingvs on 16/4/19.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private ArrayList<String> mDatas;
    private PhotoViewAttacher.OnPhotoTapListener mPhotoTapListener;
    private Context mContext;


    public ViewPagerAdapter(ArrayList<String> datas, Context context, PhotoViewAttacher.OnPhotoTapListener onPhotoTapListener) {
        this.mDatas = datas;
        this.mContext = context;
        this.mPhotoTapListener = onPhotoTapListener;
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
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        ImageLoader.getInstance().displayImage(mDatas.get(position), photoView);
        photoView.setOnPhotoTapListener(mPhotoTapListener);
        photoView.setMaximumScale(10f);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

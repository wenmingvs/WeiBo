package com.wenming.weiswift.app.imgpreview;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;

import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.StatusBarUtils;
import com.wenming.weiswift.app.imgpreview.animation.ZoomOutPageTransformer;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by wenmingvs on 16/4/19.
 */
public class ImageDetailsActivity extends Activity implements ViewPagerAdapter.OnSingleTagListener {

    private ImageDetailViewPager mViewPager;
    private ImageDetailTopBar mImageDetailTopBar;
    private ArrayList<String> mDatas;
    private int mPosition;
    private int mImgNum;
    private ViewPagerAdapter mAdapter;
    private Context mContext;
    private PhotoViewAttacher.OnPhotoTapListener mPhotoTapListener = new PhotoViewAttacher.OnPhotoTapListener() {
        @Override
        public void onPhotoTap(View view, float v, float v1) {
            finish();
        }

        @Override
        public void onOutsidePhotoTap() {
            finish();
        }
    };

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_weiboitem_imagedetails);
        mContext = ImageDetailsActivity.this;
        mDatas = this.getIntent().getStringArrayListExtra("imagelist_url");
        mPosition = getIntent().getIntExtra("image_position", 0);
        mImgNum = mDatas.size();

        mViewPager = (ImageDetailViewPager) findViewById(R.id.viewpagerId);
        mImageDetailTopBar = (ImageDetailTopBar) findViewById(R.id.imageTopBar);
        mAdapter = new ViewPagerAdapter(mDatas, this);
        mAdapter.setOnSingleTagListener(this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        if (mImgNum == 1) {
            mImageDetailTopBar.setPageNumVisible(View.GONE);
        } else {
            mImageDetailTopBar.setPageNum((mPosition + 1) + "/" + mImgNum);
        }
        mViewPager.setCurrentItem(mPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 每当页数发生改变时重新设定一遍当前的页数和总页数
                mImageDetailTopBar.setPageNum((position + 1) + "/" + mImgNum);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mImageDetailTopBar.setOnMoreOptionsListener(new ImageDetailTopBar.OnMoreOptionsListener() {
            @Override
            public void onClick(View view) {
                SaveImageDialog.showDialog(mDatas.get(mViewPager.getCurrentItem()), mContext);
            }
        });

        //只有小米，或者魅族手机，或者6.0的手机，才适配状态栏
        if (Build.MANUFACTURER.equalsIgnoreCase("xiaomi") || Build.MANUFACTURER.equalsIgnoreCase("meizu") || Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            StatusBarUtils.from(this)
                    .setTransparentStatusbar(true)
                    .setStatusBarColor(getResources().getColor(R.color.black))
                    .process(this);
        }
        

    }


    @Override
    public void onTag() {
        finish();
    }

}

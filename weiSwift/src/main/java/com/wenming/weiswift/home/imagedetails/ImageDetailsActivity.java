package com.wenming.weiswift.home.imagedetails;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wenming.weiswift.R;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by wenmingvs on 16/4/19.
 */
public class ImageDetailsActivity extends Activity implements ViewPagerAdapter.OnSingleTagListener {

    private ImageDetailViewPager mViewPager;
    private TextView mPageText;
    private ImageView mImageView;
    private ArrayList<String> mDatas;
    private int mPosition;
    private int mImgNum;
    private ViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_weiboitem_imagedetails);
        mDatas = this.getIntent().getStringArrayListExtra("imagelist_url");
        mPosition = getIntent().getIntExtra("image_position", 0);
        mImgNum = mDatas.size();

        mViewPager = (ImageDetailViewPager) findViewById(R.id.viewpagerId);
        mImageView = (ImageView) findViewById(R.id.image_moreId);
        mPageText = (TextView) findViewById(R.id.pageTextId);
        mAdapter = new ViewPagerAdapter(mDatas, this);
        mAdapter.setOnSingleTagListener(this);
        mViewPager.setAdapter(mAdapter);

        mPageText.setText((mPosition + 1) + "/" + mImgNum);

        mViewPager.setCurrentItem(mPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 每当页数发生改变时重新设定一遍当前的页数和总页数
                mPageText.setText((position + 1) + "/" + mImgNum);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ImageDetailsActivity.this, "等待完成的功能", Toast.LENGTH_SHORT).show();
            }
        });

    }


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


    @Override
    public void onTag() {
        finish();
    }
}

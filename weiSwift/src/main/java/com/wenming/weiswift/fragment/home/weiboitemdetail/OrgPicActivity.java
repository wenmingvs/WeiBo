package com.wenming.weiswift.fragment.home.weiboitemdetail;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.wenming.weiswift.R;
import com.wenming.weiswift.common.emojitextview.EmojiTextView;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/4/21.
 */
public class OrgPicActivity extends BaseActivity {
    private DisplayImageOptions options;
    private ArrayList<String> mImageDatas;

    public LinearLayout origin_weibo_layout;
    public ImageView profile_img;
    public TextView profile_name;
    public TextView profile_time;
    public TextView weibo_comeform;
    public EmojiTextView weibo_Content;
    public TextView redirect;
    public TextView comment;
    public TextView feedlike;
    public RecyclerView imageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void setContntView() {
        setContentView(R.layout.home_weiboitem_detail_orginal_pictext);
        origin_weibo_layout = (LinearLayout) findViewById(R.id.origin_weibo_layout);
        profile_img = (ImageView) findViewById(R.id.profile_img);
        profile_name = (TextView) findViewById(R.id.profile_name);
        profile_time = (TextView) findViewById(R.id.profile_time);
        weibo_Content = (EmojiTextView) findViewById(R.id.weibo_Content);
        weibo_comeform = (TextView) findViewById(R.id.weiboComeFrom);
        redirect = (TextView) findViewById(R.id.redirect);
        comment = (TextView) findViewById(R.id.comment);
        feedlike = (TextView) findViewById(R.id.feedlike);
        imageList = (RecyclerView) findViewById(R.id.weibo_image);
    }
}

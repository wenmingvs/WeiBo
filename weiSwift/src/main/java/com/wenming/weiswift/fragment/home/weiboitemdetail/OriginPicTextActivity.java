package com.wenming.weiswift.fragment.home.weiboitemdetail;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.common.emojitextview.EmojiTextView;
import com.wenming.weiswift.fragment.home.util.FillWeiBoItem;

/**
 * Created by wenmingvs on 16/4/21.
 */
public class OriginPicTextActivity extends BaseActivity {
    private Context mContext;
    private LinearLayout origin_weibo_layout;
    private ImageView profile_img;
    private TextView profile_name;
    private TextView profile_time;
    private TextView weibo_comefrom;
    private EmojiTextView weibo_content;
    private TextView redirect;
    private TextView comment;
    private TextView feedlike;
    private RecyclerView imageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mWeiboItem = getIntent().getParcelableExtra("weiboitem");
        FillWeiBoItem.fillTitleBar(mWeiboItem, profile_img, profile_name, profile_time, weibo_comefrom);
        FillWeiBoItem.fillWeiBoContent(mWeiboItem, mContext, weibo_content);
        FillWeiBoItem.fillButtonBar(mWeiboItem, comment, redirect, feedlike);
        FillWeiBoItem.fillWeiBoImgList(mWeiboItem, mContext, imageList);

    }

    @Override
    public void setContntView() {
        setContentView(R.layout.home_weiboitem_detail_orginal_pictext);
        origin_weibo_layout = (LinearLayout) findViewById(R.id.origin_weibo_layout);
        profile_img = (ImageView) findViewById(R.id.profile_img);
        profile_name = (TextView) findViewById(R.id.profile_name);
        profile_time = (TextView) findViewById(R.id.profile_time);
        weibo_content = (EmojiTextView) findViewById(R.id.weibo_Content);
        weibo_comefrom = (TextView) findViewById(R.id.weiboComeFrom);
        redirect = (TextView) findViewById(R.id.redirect);
        comment = (TextView) findViewById(R.id.comment);
        feedlike = (TextView) findViewById(R.id.feedlike);
        imageList = (RecyclerView) findViewById(R.id.weibo_image);
    }
}

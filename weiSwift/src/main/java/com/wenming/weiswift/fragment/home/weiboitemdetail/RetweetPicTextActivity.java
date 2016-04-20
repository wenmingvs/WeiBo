package com.wenming.weiswift.fragment.home.weiboitemdetail;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.fragment.home.util.FillWeiBoItem;

/**
 * Created by wenmingvs on 16/4/21.
 */
public class RetweetPicTextActivity extends BaseActivity {
    private Context mContext;
    private LinearLayout retweet_weibo_layout;
    private ImageView profile_img;
    private TextView profile_name;
    private TextView profile_time;
    private TextView weibo_comefrom;
    private TextView retweet_content;
    private TextView redirect;
    private TextView comment;
    private TextView feedlike;
    private TextView origin_nameAndcontent;
    private RecyclerView retweet_imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mWeiboItem = getIntent().getParcelableExtra("weiboitem");
        FillWeiBoItem.fillTitleBar(mWeiboItem, profile_img, profile_name, profile_time, weibo_comefrom);
        FillWeiBoItem.fillRetweetContent(mWeiboItem, mContext, origin_nameAndcontent);
        FillWeiBoItem.fillWeiBoContent(mWeiboItem, mContext, retweet_content);
        FillWeiBoItem.fillButtonBar(mWeiboItem, comment, redirect, feedlike);
        FillWeiBoItem.fillWeiBoImgList(mWeiboItem.retweeted_status, mContext, retweet_imageList);

    }

    @Override
    public void setContntView() {
        setContentView(R.layout.home_weiboitem_detail_retweet_pictext);
        retweet_weibo_layout = (LinearLayout) findViewById(R.id.retweet_weibo_layout);
        profile_img = (ImageView) findViewById(R.id.profile_img);
        profile_name = (TextView) findViewById(R.id.profile_name);
        profile_time = (TextView) findViewById(R.id.profile_time);
        retweet_content = (TextView) findViewById(R.id.retweet_content);
        weibo_comefrom = (TextView) findViewById(R.id.weiboComeFrom);
        redirect = (TextView) findViewById(R.id.redirect);
        comment = (TextView) findViewById(R.id.comment);
        feedlike = (TextView) findViewById(R.id.feedlike);
        origin_nameAndcontent = (TextView) findViewById(R.id.origin_nameAndcontent);
        retweet_imageList = (RecyclerView) findViewById(R.id.origin_imageList);
    }
}

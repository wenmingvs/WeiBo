package com.wenming.weiswift.fragment.home.weiboitemdetail.headview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sina.weibo.sdk.openapi.models.Status;
import com.wenming.weiswift.R;
import com.wenming.weiswift.common.emojitextview.EmojiTextView;
import com.wenming.weiswift.fragment.home.imagelist.ImageItemSapce;
import com.wenming.weiswift.fragment.home.util.FillWeiBoItem;
import com.wenming.weiswift.fragment.home.weiboitemdetail.util.FillCommentDetail;

/**
 * Created by wenmingvs on 16/4/26.
 */
public class RetweetPicTextHeaderView extends LinearLayout {

    private View mView;
    private LinearLayout retweet_weibo_layout;
    private ImageView profile_img;
    private ImageView profile_verified;
    private TextView profile_name;
    private TextView profile_time;
    private TextView weibo_comefrom;
    private EmojiTextView retweet_content;
    private LinearLayout bottombar_layout;
    private EmojiTextView origin_nameAndcontent;
    private RecyclerView retweet_imageList;
    private TextView commentView;
    private TextView retweetView;
    private TextView likeView;

    public RetweetPicTextHeaderView(Context context, AttributeSet attrs, Status status) {
        super(context, attrs);
        init(context, status);
    }

    public RetweetPicTextHeaderView(Context context, AttributeSet attrs, int defStyleAttr, Status status) {
        super(context, attrs, defStyleAttr);
        init(context, status);
    }

    public RetweetPicTextHeaderView(Context context, Status status) {
        super(context);
        init(context, status);
    }

    private void initWeiBoContent(Context context, Status status) {
        retweet_imageList.addItemDecoration(new ImageItemSapce((int) context.getResources().getDimension(R.dimen.home_weiboitem_imagelist_space)));
        FillWeiBoItem.fillTitleBar(status, profile_img, profile_verified, profile_name, profile_time, weibo_comefrom);
        FillWeiBoItem.fillWeiBoContent(status.text, context, retweet_content);
        FillWeiBoItem.fillRetweetContent(status, context, origin_nameAndcontent);
        FillWeiBoItem.fillWeiBoImgList(status.retweeted_status, context, retweet_imageList);
        FillWeiBoItem.showButtonBar(View.GONE, bottombar_layout);
        FillCommentDetail.FillDetailBar(status, commentView, retweetView, likeView);
    }


    public void init(Context context, Status status) {
        mView = inflate(context, R.layout.mainfragment_weiboitem_detail_commentbar_retweet_pictext_headview, this);
        retweet_weibo_layout = (LinearLayout) findViewById(R.id.retweet_weibo_layout);
        profile_img = (ImageView) findViewById(R.id.profile_img);
        profile_verified = (ImageView) findViewById(R.id.profile_verified);
        profile_name = (TextView) findViewById(R.id.profile_name);
        profile_time = (TextView) findViewById(R.id.profile_time);
        retweet_content = (EmojiTextView) findViewById(R.id.retweet_content);
        weibo_comefrom = (TextView) findViewById(R.id.weiboComeFrom);
        bottombar_layout = (LinearLayout) findViewById(R.id.bottombar_layout);
        origin_nameAndcontent = (EmojiTextView) findViewById(R.id.origin_nameAndcontent);
        retweet_imageList = (RecyclerView) findViewById(R.id.origin_imageList);
        commentView = (TextView) findViewById(R.id.commentBar_comment);
        retweetView = (TextView) findViewById(R.id.commentBar_retweet);
        likeView = (TextView) findViewById(R.id.commentBar_like);

        initWeiBoContent(context, status);
    }

    public void refreshDetailBar(int comments_count, int reposts_count, int attitudes_count) {
        FillCommentDetail.FillDetailBar(comments_count, reposts_count, attitudes_count, commentView, retweetView, likeView);
    }
}

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
 * Created by wenmingvs on 16/4/25.
 */
public class OriginPicTextHeaderView extends LinearLayout {
    private View mView;
    private LinearLayout origin_weibo_layout;
    private ImageView profile_img;
    private ImageView profile_verified;
    private TextView profile_name;
    private TextView profile_time;
    private TextView weibo_comefrom;
    private EmojiTextView weibo_content;
    private LinearLayout bottombar_layout;
    private RecyclerView imageList;
    private TextView retweetView;
    private TextView commentView;
    private TextView likeView;

    public OriginPicTextHeaderView(Context context, Status status) {
        super(context);
        init(context, status);
    }

    public OriginPicTextHeaderView(Context context, AttributeSet attrs, Status status) {
        super(context, attrs);
        init(context, status);
    }

    public OriginPicTextHeaderView(Context context, AttributeSet attrs, int defStyleAttr, Status status) {
        super(context, attrs, defStyleAttr);
        init(context, status);
    }

    public void init(Context context, Status status) {
        mView = inflate(context, R.layout.mainfragment_weiboitem_detail_commentbar_origin_pictext_headview, this);
        origin_weibo_layout = (LinearLayout) mView.findViewById(R.id.origin_weibo_layout);
        profile_img = (ImageView) mView.findViewById(R.id.profile_img);
        profile_verified = (ImageView) mView.findViewById(R.id.profile_verified);
        profile_name = (TextView) mView.findViewById(R.id.profile_name);
        profile_time = (TextView) mView.findViewById(R.id.profile_time);
        weibo_content = (EmojiTextView) mView.findViewById(R.id.weibo_Content);
        weibo_comefrom = (TextView) mView.findViewById(R.id.weiboComeFrom);
        bottombar_layout = (LinearLayout) mView.findViewById(R.id.bottombar_layout);
        imageList = (RecyclerView) mView.findViewById(R.id.weibo_image);
        commentView = (TextView) mView.findViewById(R.id.commentBar_comment);
        retweetView = (TextView) mView.findViewById(R.id.commentBar_retweet);
        likeView = (TextView) mView.findViewById(R.id.commentBar_like);

        initWeiBoContent(context, status);
    }

    private void initWeiBoContent(Context context, Status status) {
        imageList.addItemDecoration(new ImageItemSapce((int) context.getResources().getDimension(R.dimen.home_weiboitem_imagelist_space)));
        FillWeiBoItem.fillTitleBar(status, profile_img, profile_verified, profile_name, profile_time, weibo_comefrom);
        FillWeiBoItem.fillWeiBoContent(status.text, context, weibo_content);
        FillWeiBoItem.fillWeiBoImgList(status, context, imageList);
        FillWeiBoItem.showButtonBar(View.GONE, bottombar_layout);
        FillCommentDetail.FillDetailBar(status, commentView, retweetView, likeView);
    }

    public void refreshDetailBar(int comments_count, int reposts_count, int attitudes_count) {
        FillCommentDetail.FillDetailBar(comments_count, reposts_count, attitudes_count, commentView, retweetView, likeView);
    }

}

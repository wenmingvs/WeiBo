package com.wenming.weiswift.ui.login.fragment.home.weiboitemdetail.headview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.mvp.model.imp.StatusDetailModelImp;
import com.wenming.weiswift.ui.common.FillContent;
import com.wenming.weiswift.widget.emojitextview.EmojiTextView;

import static com.wenming.weiswift.R.id.noneLayout;

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
    private RelativeLayout mNoneView;
    private Context mContext;
    private ImageView mCommentIndicator;
    private ImageView mRetweetIndicator;
    private ImageView mPopover_arrow;
    private OnDetailButtonClickListener onDetailButtonClickListener;
    private int mType = StatusDetailModelImp.COMMENT_PAGE;

    public OriginPicTextHeaderView(Context context, Status status, int type) {
        super(context);
        mType = type;
        init(context, status);
        switch (mType) {
            case StatusDetailModelImp.COMMENT_PAGE:
                commentHighlight();
                break;
            case StatusDetailModelImp.REPOST_PAGE:
                repostHighlight();
                break;
        }
    }

    public void setOnDetailButtonClickListener(OnDetailButtonClickListener onDetailButtonClickListener) {
        this.onDetailButtonClickListener = onDetailButtonClickListener;
    }

    public void init(Context context, Status status) {
        mContext = context;
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
        mPopover_arrow = (ImageView) mView.findViewById(R.id.popover_arrow);
        commentView = (TextView) mView.findViewById(R.id.commentBar_comment);
        retweetView = (TextView) mView.findViewById(R.id.commentBar_retweet);
        likeView = (TextView) mView.findViewById(R.id.commentBar_like);
        mNoneView = (RelativeLayout) findViewById(noneLayout);
        mCommentIndicator = (ImageView) findViewById(R.id.comment_indicator);
        mRetweetIndicator = (ImageView) findViewById(R.id.retweet_indicator);
        initWeiBoContent(context, status);
    }

    private void initWeiBoContent(Context context, final Status status) {
        FillContent.fillTitleBar(mContext, status, profile_img, profile_verified, profile_name, profile_time, weibo_comefrom);
        FillContent.fillWeiBoContent(status.text, context, weibo_content);
        FillContent.fillWeiBoImgList(status, context, imageList);
        FillContent.showButtonBar(View.GONE, bottombar_layout);
        FillContent.fillDetailBar(status.comments_count, status.reposts_count, status.reposts_count, commentView, retweetView, likeView);
        FillContent.refreshNoneView(mContext, mType, status.reposts_count, status.comments_count, mNoneView);

        mPopover_arrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailWeiBoArrowWindow detailWeiBoArrowWindow = new DetailWeiBoArrowWindow(mContext, status);
                detailWeiBoArrowWindow.showAtLocation(mView, Gravity.CENTER, 0, 0);
            }
        });

        retweetView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                repostHighlight();
                onDetailButtonClickListener.OnRetweet();
                //FillContent.refreshNoneView(mContext, mType, status.reposts_count, status.comments_count, mNoneView);
            }
        });

        commentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                commentHighlight();
                onDetailButtonClickListener.OnComment();
                //FillContent.refreshNoneView(mContext, mType, status.reposts_count, status.comments_count, mNoneView);
            }
        });

    }

    public void refreshDetailBar(int comments_count, int reposts_count, int attitudes_count) {
        FillContent.fillDetailBar(comments_count, reposts_count, attitudes_count, commentView, retweetView, likeView);
        FillContent.refreshNoneView(mContext, mType, reposts_count, comments_count, mNoneView);
    }

    public void commentHighlight() {
        commentView.setTextColor(Color.parseColor("#000000"));
        mCommentIndicator.setVisibility(View.VISIBLE);
        retweetView.setTextColor(Color.parseColor("#828282"));
        mRetweetIndicator.setVisibility(View.INVISIBLE);
    }

    public void repostHighlight() {
        retweetView.setTextColor(Color.parseColor("#000000"));
        mRetweetIndicator.setVisibility(View.VISIBLE);
        commentView.setTextColor(Color.parseColor("#828282"));
        mCommentIndicator.setVisibility(View.INVISIBLE);

    }

}

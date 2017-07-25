package com.wenming.weiswift.app.timeline.data.viewholder.retweet;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.app.timeline.data.viewholder.base.BaseTimeLineContract;
import com.wenming.weiswift.app.timeline.data.viewholder.base.BaseTimeLineViewHolder;
import com.wenming.weiswift.widget.emojitextview.EmojiTextView;

public class RetweetViewHolder extends BaseTimeLineViewHolder implements RetweetContract.View {
    public LinearLayout retweet_weibo_layout;
    public ImageView profile_img;
    public ImageView profile_verified;
    public ImageView popover_arrow;
    public TextView profile_name;
    public TextView profile_time;
    public TextView weibo_comefrom;
    public EmojiTextView retweet_content;
    public TextView redirect;
    public TextView comment;
    public TextView feedlike;
    public EmojiTextView origin_nameAndcontent;
    public RecyclerView retweet_imageList;
    public LinearLayout bottombar_layout;
    public LinearLayout bottombar_retweet;
    public LinearLayout bottombar_comment;
    public LinearLayout bottombar_attitude;
    public LinearLayout retweetStatus_layout;
    private RetweetContract.Presenter mPresenter;

    public RetweetViewHolder(View v) {
        super(v);
        retweet_weibo_layout = (LinearLayout) v.findViewById(R.id.retweet_weibo_layout);
        profile_img = (ImageView) v.findViewById(R.id.profile_img);
        profile_verified = (ImageView) v.findViewById(R.id.profile_verified);
        popover_arrow = (ImageView) v.findViewById(R.id.popover_arrow);
        profile_name = (TextView) v.findViewById(R.id.profile_name);
        profile_time = (TextView) v.findViewById(R.id.profile_time);
        retweet_content = (EmojiTextView) v.findViewById(R.id.retweet_content);
        weibo_comefrom = (TextView) v.findViewById(R.id.weiboComeFrom);
        redirect = (TextView) v.findViewById(R.id.redirect);
        comment = (TextView) v.findViewById(R.id.comment);
        feedlike = (TextView) v.findViewById(R.id.feedlike);
        origin_nameAndcontent = (EmojiTextView) v.findViewById(R.id.origin_nameAndcontent);
        retweet_imageList = (RecyclerView) v.findViewById(R.id.origin_imageList);
        bottombar_layout = (LinearLayout) v.findViewById(R.id.bottombar_layout);
        bottombar_retweet = (LinearLayout) v.findViewById(R.id.bottombar_retweet);
        bottombar_comment = (LinearLayout) v.findViewById(R.id.bottombar_comment);
        bottombar_attitude = (LinearLayout) v.findViewById(R.id.bottombar_attitude);
        retweetStatus_layout = (LinearLayout) v.findViewById(R.id.retweetStatus_layout);
    }

    @Override
    public void setPresenter(BaseTimeLineContract.Presenter presenter) {
        mPresenter = (RetweetContract.Presenter) presenter;
    }
}
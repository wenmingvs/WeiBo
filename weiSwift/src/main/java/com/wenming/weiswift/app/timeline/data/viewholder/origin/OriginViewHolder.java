package com.wenming.weiswift.app.timeline.data.viewholder.origin;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.app.timeline.data.viewholder.base.BaseTimeLineContract;
import com.wenming.weiswift.app.timeline.data.viewholder.base.BaseTimeLineViewHolder;
import com.wenming.weiswift.widget.emojitextview.EmojiTextView;

public class OriginViewHolder extends BaseTimeLineViewHolder implements OriginContract.View {
    public LinearLayout origin_weibo_layout;
    public LinearLayout titlebar_layout;
    public ImageView profile_img;
    public ImageView profile_verified;
    public ImageView popover_arrow;
    public TextView profile_name;
    public TextView profile_time;
    public TextView weibo_comefrom;
    public EmojiTextView weibo_content;
    public TextView redirect;
    public TextView comment;
    public TextView feedlike;
    public RecyclerView imageList;
    public TextView favoritedelete;
    public ImageView splitLine;
    public LinearLayout bottombar_layout;
    public LinearLayout bottombar_retweet;
    public LinearLayout bottombar_comment;
    public LinearLayout bottombar_attitude;
    private OriginContract.Presenter mPresenter;

    public OriginViewHolder(View v) {
        super(v);
        origin_weibo_layout = (LinearLayout) v.findViewById(R.id.origin_weibo_layout);
        titlebar_layout = (LinearLayout) v.findViewById(R.id.titlebar_layout);
        profile_img = (ImageView) v.findViewById(R.id.profile_img);
        profile_verified = (ImageView) v.findViewById(R.id.profile_verified);
        popover_arrow = (ImageView) v.findViewById(R.id.popover_arrow);
        profile_name = (TextView) v.findViewById(R.id.profile_name);
        profile_time = (TextView) v.findViewById(R.id.profile_time);
        weibo_content = (EmojiTextView) v.findViewById(R.id.weibo_Content);
        weibo_comefrom = (TextView) v.findViewById(R.id.weiboComeFrom);
        redirect = (TextView) v.findViewById(R.id.redirect);
        comment = (TextView) v.findViewById(R.id.comment);
        feedlike = (TextView) v.findViewById(R.id.feedlike);
        splitLine = (ImageView) v.findViewById(R.id.splitLine);
        imageList = (RecyclerView) v.findViewById(R.id.weibo_image);
        favoritedelete = (TextView) v.findViewById(R.id.favorities_delete);
        bottombar_layout = (LinearLayout) v.findViewById(R.id.bottombar_layout);
        bottombar_retweet = (LinearLayout) v.findViewById(R.id.bottombar_retweet);
        bottombar_comment = (LinearLayout) v.findViewById(R.id.bottombar_comment);
        bottombar_attitude = (LinearLayout) v.findViewById(R.id.bottombar_attitude);
    }

    @Override
    public void setPresenter(BaseTimeLineContract.Presenter presenter) {
        mPresenter = (OriginContract.Presenter) presenter;
    }
}
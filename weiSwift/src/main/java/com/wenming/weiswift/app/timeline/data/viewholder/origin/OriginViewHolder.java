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
    public TextView weibo_comefrom;
    public EmojiTextView weibo_content;
    public TextView redirect;
    public TextView comment;
    public TextView feedlike;
    public RecyclerView imageList;
    public TextView favoritedelete;
    public ImageView splitLine;
    private OriginContract.Presenter mPresenter;

    public OriginViewHolder(View v) {
        super(v);
        origin_weibo_layout = (LinearLayout) v.findViewById(R.id.origin_weibo_layout);
        weibo_content = (EmojiTextView) v.findViewById(R.id.weibo_Content);
        weibo_comefrom = (TextView) v.findViewById(R.id.common_status_source_tv);
        redirect = (TextView) v.findViewById(R.id.common_bottombar_retweet_tv);
        comment = (TextView) v.findViewById(R.id.common_bottombar_comment_tv);
        feedlike = (TextView) v.findViewById(R.id.common_bottombar_like_tv);
        splitLine = (ImageView) v.findViewById(R.id.splitLine);
        imageList = (RecyclerView) v.findViewById(R.id.weibo_image);
        favoritedelete = (TextView) v.findViewById(R.id.favorities_delete);
    }

    @Override
    public void setPresenter(BaseTimeLineContract.Presenter presenter) {
        mPresenter = (OriginContract.Presenter) presenter;
    }
}
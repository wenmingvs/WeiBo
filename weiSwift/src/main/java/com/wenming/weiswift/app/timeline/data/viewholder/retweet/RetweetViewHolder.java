package com.wenming.weiswift.app.timeline.data.viewholder.retweet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.app.timeline.data.viewholder.base.BaseTimeLineContract;
import com.wenming.weiswift.app.timeline.data.viewholder.base.BaseTimeLineViewHolder;
import com.wenming.weiswift.widget.emojitextview.EmojiTextView;

public class RetweetViewHolder extends BaseTimeLineViewHolder implements RetweetContract.View {
    public LinearLayout retweet_weibo_layout;
    public EmojiTextView retweet_content;
    public TextView redirect;
    public TextView comment;
    public TextView feedlike;
    public EmojiTextView origin_nameAndcontent;
    public RecyclerView retweet_imageList;
    public LinearLayout retweetStatus_layout;
    private RetweetContract.Presenter mPresenter;

    public RetweetViewHolder(Context context, View v) {
        super(context, v);
        prepareView();
        initListener();
    }

    @Override
    protected void prepareView() {
        super.prepareView();
        retweet_weibo_layout = (LinearLayout) findViewById(R.id.retweet_weibo_layout);
        retweet_content = (EmojiTextView) findViewById(R.id.retweet_content);
        redirect = (TextView) findViewById(R.id.common_bottombar_retweet_tv);
        comment = (TextView) findViewById(R.id.common_bottombar_comment_tv);
        feedlike = (TextView) findViewById(R.id.common_bottombar_like_tv);
        origin_nameAndcontent = (EmojiTextView) findViewById(R.id.origin_nameAndcontent);
        retweet_imageList = (RecyclerView) findViewById(R.id.origin_imageList);
        retweetStatus_layout = (LinearLayout) findViewById(R.id.retweetStatus_layout);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    public void setPresenter(BaseTimeLineContract.Presenter presenter) {
        mPresenter = (RetweetContract.Presenter) presenter;
    }
}
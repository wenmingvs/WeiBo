package com.wenming.weiswift.app.timeline.data.viewholder.origin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.app.timeline.data.viewholder.base.BaseTimeLineContract;
import com.wenming.weiswift.app.timeline.data.viewholder.base.BaseTimeLineViewHolder;
import com.wenming.weiswift.app.weibodetail.activity.OriginPicTextCommentDetailSwipeActivity;
import com.wenming.weiswift.widget.emojitextview.EmojiTextView;

public class OriginViewHolder extends BaseTimeLineViewHolder implements OriginContract.View {
    public LinearLayout origin_weibo_layout;
    public EmojiTextView weibo_content;
    public RecyclerView imageList;
    public TextView favoritedelete;
    public ImageView splitLine;
    private OriginContract.Presenter mPresenter;

    public OriginViewHolder(Context context, View v) {
        super(context, v);
        prepareView();
        initListener();
    }

    @Override
    protected void prepareView() {
        super.prepareView();
        origin_weibo_layout = (LinearLayout) findViewById(R.id.origin_weibo_layout);
        weibo_content = (EmojiTextView) findViewById(R.id.weibo_Content);
        splitLine = (ImageView) findViewById(R.id.splitLine);
        imageList = (RecyclerView) findViewById(R.id.weibo_image);
        favoritedelete = (TextView) findViewById(R.id.favorities_delete);
    }

    @Override
    protected void initListener() {
        super.initListener();
        //微博背景的点击事件
        origin_weibo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.goToStatusDetailActivity();
            }
        });
    }

    @Override
    public void setPresenter(BaseTimeLineContract.Presenter presenter) {
        mPresenter = (OriginContract.Presenter) presenter;
    }
}
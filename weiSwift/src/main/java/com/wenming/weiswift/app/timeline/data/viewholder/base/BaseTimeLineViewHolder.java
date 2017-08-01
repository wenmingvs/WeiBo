package com.wenming.weiswift.app.timeline.data.viewholder.base;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.weibodetail.activity.OriginPicTextCommentDetailSwipeActivity;
import com.wenming.weiswift.utils.DateUtils;
import com.wenming.weiswift.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wenmingvs on 2017/7/25.
 */

public abstract class BaseTimeLineViewHolder extends BaseViewHolder implements BaseTimeLineContract.View {
    private View mRootView;
    //TopBar
    private ImageView mTopBarAvatarIv;
    private ImageView mTopBarAvatarIdenIv;
    private TextView mTopBarNickNameTv;
    private TextView mTopBarTimeTv;
    private TextView mTopBarSourceFrom;
    private ImageView mTopBarStatusMoreIv;
    //BottomBar
    private LinearLayout mBottomBarRetweetLl;
    private LinearLayout mBottomBarCommentLl;
    private LinearLayout mBottomBarLikeLl;
    private TextView mBottomBarRetweetTv;
    private TextView mBottomBarCommentTv;
    private TextView mBottomBarLikeTv;
    protected Context mContext;
    private BaseTimeLineContract.Presenter mPresenter;

    private static DisplayImageOptions mAvatorOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.avator_default)
            .showImageForEmptyUri(R.drawable.avator_default)
            .showImageOnFail(R.drawable.avator_default)
            .imageScaleType(ImageScaleType.EXACTLY)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .displayer(new CircleBitmapDisplayer(14671839, 1))
            .build();

    public BaseTimeLineViewHolder(Context context, View view) {
        super(view);
        this.mRootView = view;
        this.mContext = context;
        prepareView();
        initView();
        initListener();
    }

    protected void initView() {

    }

    protected View findViewById(int paramInt) {
        if (mRootView != null) {
            return mRootView.findViewById(paramInt);
        }
        return null;
    }

    protected void prepareView() {
        mTopBarAvatarIv = (ImageView) findViewById(R.id.common_avatar_iv);
        mTopBarAvatarIdenIv = (ImageView) findViewById(R.id.common_avatar_identification_iv);
        mTopBarStatusMoreIv = (ImageView) findViewById(R.id.common_status_more_iv);
        mTopBarNickNameTv = (TextView) findViewById(R.id.common_status_nickname_tv);
        mTopBarTimeTv = (TextView) findViewById(R.id.common_status_time_tv);
        mTopBarSourceFrom = (TextView) findViewById(R.id.common_status_source_tv);
        mBottomBarRetweetLl = (LinearLayout) findViewById(R.id.common_bottombar_retweet_ll);
        mBottomBarCommentLl = (LinearLayout) findViewById(R.id.common_bottombar_comment_ll);
        mBottomBarLikeLl = (LinearLayout) findViewById(R.id.common_bottombar_like_ll);
        mBottomBarRetweetTv = (TextView) findViewById(R.id.common_bottombar_retweet_tv);
        mBottomBarCommentTv = (TextView) findViewById(R.id.common_bottombar_comment_tv);
        mBottomBarLikeTv = (TextView) findViewById(R.id.common_bottombar_like_tv);
    }

    protected void initListener() {
        //点击转发的内容
        mBottomBarRetweetTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.goToStatusDetailActivity();
            }
        });
    }

    @Override
    public void goToStatusDetailActivity(Status status) {
        Intent intent = new Intent(mContext, OriginPicTextCommentDetailSwipeActivity.class);
        intent.putExtra("weiboitem", status.retweeted_status);
        mContext.startActivity(intent);
    }

    @Override
    public void setTopBarAvatar(String url) {
        ImageLoader.getInstance().displayImage(url, mTopBarAvatarIv, mAvatorOptions);
    }

    @Override
    public void setTopBarIdentRes(int resId) {
        mTopBarAvatarIdenIv.setImageResource(resId);
    }

    @Override
    public void hideTopBarIden() {
        mTopBarAvatarIdenIv.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showTopBarIden() {
        mTopBarAvatarIdenIv.setVisibility(View.VISIBLE);
    }

    @Override
    public void setTopBarName(String name) {
        mTopBarNickNameTv.setText(name);
    }

    @Override
    public void setTopBarCreateTime(String createAt) {
        Date data = DateUtils.parseDate(createAt, DateUtils.WeiBo_ITEM_DATE_FORMAT);
        TimeUtils timeUtils = TimeUtils.instance(mContext);
        mTopBarTimeTv.setText(timeUtils.buildTimeString(data.getTime()));
    }

    @Override
    public void setTopBarSourceFrom(String source) {
        mTopBarSourceFrom.setText(String.format(mContext.getString(R.string.timeline_source_from_suffix), source));
    }

    @Override
    public void hideTopBarSourceFrom() {
        mTopBarSourceFrom.setVisibility(View.GONE);
    }

    @Override
    public void showTopBarSourceFrom() {
        mTopBarSourceFrom.setVisibility(View.VISIBLE);
    }

    @Override
    public void setBottomBarRetweetCount(int count) {
        mBottomBarRetweetTv.setText(String.valueOf(count));
    }

    @Override
    public void setDefaultRetweetContent() {
        if (mContext == null) {
            return;
        }
        mBottomBarRetweetTv.setText(mContext.getString(R.string.timeline_retweet));
    }

    @Override
    public void setBottomBarCommentCount(int count) {
        mBottomBarCommentTv.setText(String.valueOf(count));
    }

    @Override
    public void setDefaultCommentContent() {
        if (mContext == null) {
            return;
        }
        mBottomBarCommentTv.setText(mContext.getString(R.string.timeline_comment));
    }

    @Override
    public void setBottomBarLikeCount(int count) {
        mBottomBarLikeTv.setText(String.valueOf(count));
    }

    @Override
    public void setDefaultLikeContent() {
        if (mContext == null) {
            return;
        }
        mBottomBarLikeTv.setText(mContext.getString(R.string.timeline_like));
    }

    /**
     * 根据图片数量，初始化GridLayoutManager，并且设置列数，
     * 当图片 = 1 的时候，显示1列
     * 当图片<=4张的时候，显示2列
     * 当图片>4 张的时候，显示3列
     */
    protected GridLayoutManager initGridLayoutManager(ArrayList<String> imageDatas) {
        GridLayoutManager gridLayoutManager;
        if (imageDatas != null) {
            switch (imageDatas.size()) {
                case 1:
                    gridLayoutManager = new GridLayoutManager(mContext, 1);
                    break;
                case 2:
                    gridLayoutManager = new GridLayoutManager(mContext, 2);
                    break;
                case 3:
                    gridLayoutManager = new GridLayoutManager(mContext, 3);
                    break;
                case 4:
                    gridLayoutManager = new GridLayoutManager(mContext, 2);
                    break;
                default:
                    gridLayoutManager = new GridLayoutManager(mContext, 3);
                    break;
            }
        } else {
            gridLayoutManager = new GridLayoutManager(mContext, 3);
        }
        return gridLayoutManager;
    }

    @Override
    public void resetView() {

    }

    @Override
    public BaseTimeLineContract.Presenter getPresenter() {
        return mPresenter;
    }
}

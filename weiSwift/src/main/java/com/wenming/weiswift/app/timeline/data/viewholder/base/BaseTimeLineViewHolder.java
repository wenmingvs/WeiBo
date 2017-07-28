package com.wenming.weiswift.app.timeline.data.viewholder.base;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.wenming.weiswift.utils.DateUtils;
import com.wenming.weiswift.utils.TimeUtils;

import java.util.Date;

/**
 * Created by wenmingvs on 2017/7/25.
 */

public abstract class BaseTimeLineViewHolder extends BaseViewHolder implements BaseTimeLineContract.View {
    private View mRootView;
    //TopBar
    private LinearLayout mTopBarContainerLl;
    private ImageView mTopBarAvatarIv;
    private ImageView mTopBarAvatarIdenIv;
    private TextView mTopBarNickNameTv;
    private TextView mTopBarTimeTv;
    public TextView mTopBarSourceFrom;
    private ImageView mTopBarStatusMoreIv;
    //BottomBar
    public LinearLayout mBottomBarContainerLl;
    public LinearLayout mBottomBarRetweetLl;
    public LinearLayout mBottomBarCommentLl;
    public LinearLayout mBottomBarLikeLl;
    private Context mContext;
    private BaseTimeLineContract.Presenter mPresenter;

    private static DisplayImageOptions mAvatorOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.avator_default)
            .showImageForEmptyUri(R.drawable.avator_default)
            .showImageOnFail(R.drawable.avator_default)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .displayer(new CircleBitmapDisplayer(14671839, 1))
            .build();

    public BaseTimeLineViewHolder(Context context, View view) {
        super(view);
        this.mRootView = view;
        this.mContext = context;
        prepareView();
        initListener();
    }

    protected View findViewById(int paramInt) {
        if (mRootView != null) {
            return mRootView.findViewById(paramInt);
        }
        return null;
    }

    private void prepareView() {
        mTopBarContainerLl = (LinearLayout) mRootView.findViewById(R.id.common_status_topbar_ll);
        mTopBarAvatarIv = (ImageView) mRootView.findViewById(R.id.common_avatar_iv);
        mTopBarAvatarIdenIv = (ImageView) mRootView.findViewById(R.id.common_avatar_identification_iv);
        mTopBarStatusMoreIv = (ImageView) mRootView.findViewById(R.id.common_status_more_iv);
        mTopBarNickNameTv = (TextView) mRootView.findViewById(R.id.common_status_nickname_tv);
        mTopBarTimeTv = (TextView) mRootView.findViewById(R.id.common_status_time_tv);
        mBottomBarContainerLl = (LinearLayout) findViewById(R.id.common_bottombar_container_ll);
        mBottomBarRetweetLl = (LinearLayout) findViewById(R.id.common_bottombar_retweet_ll);
        mBottomBarCommentLl = (LinearLayout) findViewById(R.id.common_bottombar_comment_ll);
        mBottomBarLikeLl = (LinearLayout) findViewById(R.id.common_bottombar_like_ll);
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
    public void setBottomBarRetweetNum(int retweetNum) {

    }

    @Override
    public void setBottomCommentNum(int commentNum) {

    }

    @Override
    public void setBottomLikeNum(int likeNum) {

    }

    @Override
    public void hideTopBarSourceFrom() {
        mTopBarSourceFrom.setVisibility(View.GONE);
    }

    @Override
    public void showTopBarSourceFrom() {
        mTopBarSourceFrom.setVisibility(View.VISIBLE);
    }

    private void initListener() {

    }
}

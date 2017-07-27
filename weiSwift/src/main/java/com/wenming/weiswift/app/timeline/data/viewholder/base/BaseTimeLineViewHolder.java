package com.wenming.weiswift.app.timeline.data.viewholder.base;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.wenming.weiswift.R;

/**
 * Created by wenmingvs on 2017/7/25.
 */

public abstract class BaseTimeLineViewHolder extends BaseViewHolder implements BaseTimeLineContract.View {
    private View mRootView;
    private LinearLayout mTopBarContainerLl;
    private ImageView mTopBarAvatarIv;
    private ImageView mTopBarAvatarIdenIv;
    private ImageView mTopBarStatusMoreIv;
    private TextView mTopBarNickNameTv;
    private TextView mTopBarTimeTv;
    public LinearLayout mBottomBarContainerLl;
    public LinearLayout mBottomBarRetweetLl;
    public LinearLayout mBottomBarCommentLl;
    public LinearLayout mBottomBarLikeLl;
    private BaseTimeLineContract.Presenter mPresenter;

    public BaseTimeLineViewHolder(View view) {
        super(view);
        this.mRootView = view;
        initView();
    }

    private void initView() {
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

    protected View findViewById(int paramInt) {
        if (mRootView != null) {
            return mRootView.findViewById(paramInt);
        }
        return null;
    }
}

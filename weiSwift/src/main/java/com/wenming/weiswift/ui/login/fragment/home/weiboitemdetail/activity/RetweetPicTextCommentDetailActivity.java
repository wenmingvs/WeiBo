package com.wenming.weiswift.ui.login.fragment.home.weiboitemdetail.activity;

import android.widget.LinearLayout;

import com.wenming.weiswift.ui.login.fragment.home.weiboitemdetail.headview.RetweetPicTextHeaderView;
import com.wenming.weiswift.widget.endlessrecyclerview.RecyclerViewUtils;

/**
 * Created by wenmingvs on 16/4/21.
 */
public class RetweetPicTextCommentDetailActivity extends BaseDetailActivity {
    public LinearLayout mHeaderView;

    @Override
    protected void addHeaderView(int type) {
        mHeaderView = new RetweetPicTextHeaderView(mContext, mStatus, type);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mHeaderView.setLayoutParams(layoutParams);
        ((RetweetPicTextHeaderView) mHeaderView).setOnDetailButtonClickListener(onDetailButtonClickListener);
        RecyclerViewUtils.setHeaderView(mRecyclerView, mHeaderView);
    }

    @Override
    protected int getHeaderViewHeight() {
        return mHeaderView.getHeight();
    }

    @Override
    protected void refreshDetailBar(int comments, int reposts, int attitudes) {
        ((RetweetPicTextHeaderView) mHeaderView).refreshDetailBar(comments, reposts, attitudes);
    }

}
package com.wenming.weiswift.ui.login.fragment.home.weiboitemdetail.activity;

import android.widget.LinearLayout;

import com.wenming.weiswift.widget.endlessrecyclerview.RecyclerViewUtils;
import com.wenming.weiswift.ui.login.fragment.home.weiboitemdetail.headview.RetweetPicTextHeaderView;

/**
 * Created by wenmingvs on 16/4/21.
 */
public class RetweetPicTextCommentActivity extends BaseActivity {

    @Override
    protected void addHeaderView(int num) {
        mHeaderView = new RetweetPicTextHeaderView(mContext, mWeiboItem);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mHeaderView.setLayoutParams(layoutParams);
        ((RetweetPicTextHeaderView) mHeaderView).setOnDetailButtonClickListener(onDetailButtonClickListener);
        RecyclerViewUtils.setHeaderView(mRecyclerView, mHeaderView);
    }

    @Override
    protected void refreshDetailBar(int comments, int reposts, int attitudes) {
        ((RetweetPicTextHeaderView) mHeaderView).refreshDetailBar(comments, reposts, attitudes);
    }

}
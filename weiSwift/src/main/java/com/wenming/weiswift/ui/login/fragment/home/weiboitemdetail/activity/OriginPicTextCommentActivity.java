package com.wenming.weiswift.ui.login.fragment.home.weiboitemdetail.activity;

import android.widget.LinearLayout;

import com.wenming.weiswift.widget.endlessrecyclerview.RecyclerViewUtils;
import com.wenming.weiswift.ui.login.fragment.home.weiboitemdetail.headview.OriginPicTextHeaderView;

/**
 * Created by wenmingvs on 16/4/21.
 */
public class OriginPicTextCommentActivity extends BaseActivity {


    @Override
    protected void addHeaderView(int num) {
        mHeaderView = new OriginPicTextHeaderView(mContext, mWeiboItem);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mHeaderView.setLayoutParams(layoutParams);
        ((OriginPicTextHeaderView) mHeaderView).setOnDetailButtonClickListener(onDetailButtonClickListener);
        RecyclerViewUtils.setHeaderView(mRecyclerView, mHeaderView);
    }

    @Override
    protected void refreshDetailBar(int comments, int reposts, int attitudes) {
        ((OriginPicTextHeaderView) mHeaderView).refreshDetailBar(comments, reposts, attitudes);
    }


}

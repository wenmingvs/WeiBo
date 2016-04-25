package com.wenming.weiswift.fragment.home.weiboitemdetail.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.wenming.weiswift.R;
import com.wenming.weiswift.common.endlessrecyclerview.RecyclerViewUtils;
import com.wenming.weiswift.common.util.DensityUtil;
import com.wenming.weiswift.fragment.home.weiboitemdetail.headview.OriginPicTextHeaderView;

/**
 * Created by wenmingvs on 16/4/21.
 */
public class OriginPicTextCommentActivity extends DetailActivity {


    private View mNoneView;
    private boolean addNoneView;

    @Override
    protected void addHeaderView(int num) {
        if (num == 0 && !addNoneView) {
            mHeaderView = new OriginPicTextHeaderView(mContext, mWeiboItem);
            mNoneView = LayoutInflater.from(this).inflate(R.layout.mainfragment_weiboitem_detail_commentbar_comment_item_none, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(this, 210f));
            mNoneView.setLayoutParams(layoutParams);
           // mHeaderView.addView(mNoneView);
            addNoneView = true;
        }
        RecyclerViewUtils.setHeaderView(mRecyclerView, mHeaderView);
    }

    @Override
    protected void refreshDetailBar(int comments, int reposts, int attitudes) {
        ((OriginPicTextHeaderView) mHeaderView).refreshDetailBar(comments, reposts, attitudes);
    }


}

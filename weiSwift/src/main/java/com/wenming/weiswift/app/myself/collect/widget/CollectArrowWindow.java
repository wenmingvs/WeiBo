package com.wenming.weiswift.app.myself.collect.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.mvp.view.WeiBoArrowView;
import com.wenming.weiswift.app.common.widget.ArrowDialog;
import com.wenming.weiswift.app.home.adapter.WeiboAdapter;

/**
 * Created by xiangflight on 2016/4/22.
 */
public class CollectArrowWindow extends ArrowDialog implements WeiBoArrowView {


    public CollectArrowWindow(Context context, Status status, WeiboAdapter weiboAdapter, int position, String groupName) {
        super(context, status, weiboAdapter, position, groupName);
    }

    /**
     * 设置收藏的TextView的内容，如果收藏了此微博，则显示取消收藏，如果没有收藏，则显示收藏
     */
    @Override
    public void setFavoriteTextContext(final Status status, TextView textView) {
        textView.setText("取消收藏");
        textView.setBackgroundResource(R.drawable.home_weiboitem_arrow_pop_corner_single_highlight_bg_auto);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeiBoArrowPresent.cancalFavorite(mItemPosition, status, mContext, true);
            }
        });
    }

    /**
     * 设置朋友的关系内容，如果已经关注，则显示取消关注，如果没有关注，则显示关注
     */
    @Override
    public void setFriendShipContext(final Status status, TextView textView) {
        mFollerLayout.setVisibility(View.GONE);
    }

    /**
     * 设置是否显示删除按钮，如果不是自己的微博，要隐藏他
     */
    @Override
    public void setDeleteViewContent(final Status status, final TextView textView) {
        mDeleteLayout.setVisibility(View.GONE);
    }


}

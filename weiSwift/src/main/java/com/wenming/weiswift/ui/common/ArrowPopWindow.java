package com.wenming.weiswift.ui.common;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.mvp.presenter.WeiBoArrowPresent;
import com.wenming.weiswift.mvp.presenter.imp.WeiBoArrowPresenterImp;
import com.wenming.weiswift.ui.login.fragment.home.weiboitem.WeiboAdapter;
import com.wenming.weiswift.utils.DensityUtil;
import com.wenming.weiswift.utils.ScreenUtil;

/**
 * 主要负责popwindow的一系列初始化，包括背景，阴影动画，还有其中的textview 的初始化
 * Created by wenmingvs on 16/6/11.
 */
public abstract class ArrowPopWindow extends BasePopupWindow {
    public View mView;
    public Context mContext;
    public TextView mDeleteTextView;
    public TextView mFavoriteTextView;
    public TextView mFriendShipTextView;
    public LinearLayout mDeleteLayout;
    public LinearLayout mFollerLayout;
    public WeiBoArrowPresent mWeiBoArrowPresent;
    public Status mStatus;
    public WeiboAdapter mWeiboAdapter;
    public int mItemPosition;
    public String mGroupName;

    public ArrowPopWindow(Context context, Status status, WeiboAdapter weiboAdapter, int position, String groupName) {
        super(context, (Activity) context, 300);
        mContext = context;
        mStatus = status;
        mWeiboAdapter = weiboAdapter;
        mItemPosition = position;
        mGroupName = groupName;
        mWeiBoArrowPresent = new WeiBoArrowPresenterImp(this);
        initPopWindow(context);
        mView = LayoutInflater.from(context).inflate(R.layout.home_weiboitem_arrow_popwindow, null);
        this.setContentView(mView);
        setUpListener();
    }

    public ArrowPopWindow(Context context, Status status) {
        super(context, (Activity) context, 300);
        mContext = context;
        mStatus = status;
        mWeiBoArrowPresent = new WeiBoArrowPresenterImp(this);
        initPopWindow(context);
        mView = LayoutInflater.from(context).inflate(R.layout.home_weiboitem_arrow_popwindow, null);
        this.setContentView(mView);
        setUpListener();
    }


    private void initPopWindow(Context context) {
        int width = ScreenUtil.getScreenWidth(context) - DensityUtil.dp2px(mContext, 80);
        this.setWindowLayoutMode(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(width);
        this.setHeight(1);
        // 设置弹出窗口可点击
        this.setFocusable(true);
        // 设置窗口外部可点击
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.weiboitem_arrow_popwup_style);
        // 设置drawable，必须得设置
        this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.home_weiboitem_arrow_pop_corner_bg));
        // 设置点击外部隐藏window
        this.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    private void setUpListener() {
        mDeleteTextView = (TextView) mView.findViewById(R.id.pop_deleteweibo);
        mFavoriteTextView = (TextView) mView.findViewById(R.id.pop_collectweibo);
        mFriendShipTextView = (TextView) mView.findViewById(R.id.pop_disfollow);
        mDeleteLayout = (LinearLayout) mView.findViewById(R.id.deleteLayout);
        mFollerLayout = (LinearLayout) mView.findViewById(R.id.followLayout);
        setFavoriteTextContext(mStatus, mFavoriteTextView);
        setFriendShipContext(mStatus, mFriendShipTextView);
        setDeleteViewContent(mStatus, mDeleteTextView);
    }

    public abstract void setDeleteViewContent(Status mStatus, TextView mDeleteTextView);

    public abstract void setFriendShipContext(Status mStatus, TextView mFriendShipTextView);

    public abstract void setFavoriteTextContext(Status mStatus, TextView mFavoriteTextView);


}

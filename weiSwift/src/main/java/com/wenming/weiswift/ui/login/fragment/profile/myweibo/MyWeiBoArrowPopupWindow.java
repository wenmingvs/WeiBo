package com.wenming.weiswift.ui.login.fragment.profile.myweibo;

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
import com.wenming.weiswift.mvp.view.WeiBoArrowView;
import com.wenming.weiswift.ui.common.BasePopupWindow;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.utils.DensityUtil;
import com.wenming.weiswift.utils.ScreenUtil;

/**
 * Created by xiangflight on 2016/4/22.
 */
public class MyWeiBoArrowPopupWindow extends BasePopupWindow implements WeiBoArrowView {
    private View mView;
    private Context mContext;
    private TextView mDeleteTextView;
    private TextView mFavoriteTextView;
    private TextView mFriendShipTextView;
    private LinearLayout mDeleteLayout;
    private OnDeleteItemListener mOnDeleteItemListener;
    private WeiBoArrowPresent mWeiBoArrowPresent;
    private Status mStatus;
    private int mStatusPosition;
    private String mCurrentUserId;

    public MyWeiBoArrowPopupWindow(Context context, Status status, int position) {
        super(context, (Activity) context, 300);
        mContext = context;
        mStatus = status;
        mStatusPosition = position;
        mWeiBoArrowPresent = new WeiBoArrowPresenterImp(this);
        mCurrentUserId = AccessTokenKeeper.readAccessToken(mContext).getUid();
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
        this.setBackgroundDrawable(context.getDrawable(R.drawable.home_weiboitem_arrow_pop_corner_bg));
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
        setFavoriteTextContext(mStatus, mFavoriteTextView);
        setFriendShipContext(mStatus, mFriendShipTextView);
        setDeleteViewContent(mStatus, mDeleteTextView);
    }

    /**
     * 不需要收藏自己的微博
     */
    private void setFavoriteTextContext(final Status status, TextView textView) {
        if (status.favorited) {
            textView.setText("取消收藏");
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mWeiBoArrowPresent.cancalFavorite(status, mContext);
                }
            });
        } else {
            textView.setText("收藏");
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mWeiBoArrowPresent.createFavorite(status, mContext);
                }
            });
        }
    }

    /**
     * 不能关注自己！
     */
    private void setFriendShipContext(Status status, TextView textView) {
        textView.setVisibility(View.GONE);
    }

    /**
     * 设置是否显示删除按钮，如果不是自己的微博，要隐藏他
     */
    private void setDeleteViewContent(final Status status, TextView textView) {
        if (status.user.id.equals(mCurrentUserId)) {
            textView.setVisibility(View.VISIBLE);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnDeleteItemListener.OnItemDelete(mStatusPosition, status);
                }
            });
        } else {
            mDeleteLayout.setVisibility(View.GONE);
        }
    }

    public void setOnDeleteItemListener(OnDeleteItemListener onDeleteItemListener) {
        this.mOnDeleteItemListener = onDeleteItemListener;
    }

    public interface OnDeleteItemListener {
        public void OnItemDelete(int position, Status status);
    }

}

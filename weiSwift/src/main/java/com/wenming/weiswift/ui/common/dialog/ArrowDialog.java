package com.wenming.weiswift.ui.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.mvp.presenter.WeiBoArrowPresent2;
import com.wenming.weiswift.mvp.presenter.imp.WeiBoArrowPresenterImp2;
import com.wenming.weiswift.ui.login.fragment.home.weiboitem.WeiboAdapter;

/**
 * Created by wenmingvs on 2016/8/22.
 */
public abstract class ArrowDialog extends Dialog {

    public Context mContext;
    public TextView mDeleteTextView;
    public TextView mFavoriteTextView;
    public TextView mFriendShipTextView;
    public LinearLayout mDeleteLayout;
    public LinearLayout mFollerLayout;
    public WeiBoArrowPresent2 mWeiBoArrowPresent;
    public Status mStatus;
    public WeiboAdapter mWeiboAdapter;
    public int mItemPosition;
    public String mGroupName;
    public onDialogButtonClick mOnDialogButtonClick;

    public ArrowDialog(Context context, Status status, WeiboAdapter weiboAdapter, int position, String groupName) {
        super(context, R.style.ArrowDialog);
        mContext = context;
        mStatus = status;
        mWeiboAdapter = weiboAdapter;
        mItemPosition = position;
        mGroupName = groupName;
    }

    public ArrowDialog(Context context, Status status) {
        super(context);
        mContext = context;
        mStatus = status;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_weiboitem_arrow_popwindow);
        if(mWeiboAdapter == null){
            mWeiBoArrowPresent = new WeiBoArrowPresenterImp2(this);
        }else {
        }  mWeiBoArrowPresent = new WeiBoArrowPresenterImp2(this,mWeiboAdapter);

        mDeleteTextView = (TextView) findViewById(R.id.pop_deleteweibo);
        mFavoriteTextView = (TextView) findViewById(R.id.pop_collectweibo);
        mFriendShipTextView = (TextView) findViewById(R.id.pop_disfollow);
        mDeleteLayout = (LinearLayout) findViewById(R.id.deleteLayout);
        mFollerLayout = (LinearLayout) findViewById(R.id.followLayout);
        initContent();
        setUpListener();
        setCanceledOnTouchOutside(true);
        setCancelable(true);
    }

    @Override
    public void show() {
        super.show();
    }

    public void initContent() {
        setFavoriteTextContext(mStatus, mFavoriteTextView);
        setFriendShipContext(mStatus, mFriendShipTextView);
        setDeleteViewContent(mStatus, mDeleteTextView);
    }


    public void setUpListener() {

    }

    /**
     * 设置收藏的TextView的内容，如果收藏了此微博，则显示取消收藏，如果没有收藏，则显示收藏
     */
    public abstract  void setFavoriteTextContext(final Status status, TextView textView);

    /**
     * 设置朋友的关系内容，如果已经关注，则显示取消关注，如果没有关注，则显示关注
     */
    public abstract void setFriendShipContext(final Status status, TextView textView);

    /**
     * 设置是否显示删除按钮，如果不是自己的微博，要隐藏他
     */
    public abstract void setDeleteViewContent(final Status status, final TextView textView);

    public interface onDialogButtonClick {
        void onDeleteButtonClick(Status status, int position, WeiBoArrowPresent2 weiBoArrowPresent2, TextView deleteTextView);

        void onFriendShipButtonClick(Status status, int position, WeiBoArrowPresent2 weiBoArrowPresent2, TextView friendShipTextView);

        void onFavoriteButtonClick(Status status, int position, WeiBoArrowPresent2 weiBoArrowPresent2, TextView favoriteTextView);
    }

    public void setOnDialogButtonClick(onDialogButtonClick onDialogButtonClick) {
        this.mOnDialogButtonClick = onDialogButtonClick;
    }


//    public static class Builder {
//        private ArrowDialog mDialog;
//
//        public Builder() {
//        }
//
//        public Builder(Context context, Status status) {
//            mDialog = new ArrowDialog(context, status);
//        }
//
//        public Builder(Context context, Status status, WeiboAdapter weiboAdapter, int position, String groupName) {
//            mDialog = new ArrowDialog(context, status, weiboAdapter, position, groupName);
//        }
//
//
//        public Builder setCancelable(boolean cancelable) {
//            mDialog.setCancelable(cancelable);
//            return this;
//        }
//
//        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
//            mDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
//            return this;
//        }
//
//        public ArrowDialog create() {
//            return mDialog;
//        }
//    }
}

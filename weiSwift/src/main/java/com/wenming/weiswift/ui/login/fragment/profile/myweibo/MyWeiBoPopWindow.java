package com.wenming.weiswift.ui.login.fragment.profile.myweibo;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.ui.common.login.Constants;
import com.wenming.weiswift.ui.login.fragment.home.imagedetaillist.ImageOptionPopupWindow;
import com.wenming.weiswift.ui.login.fragment.message.IGroupItemClick;

/**
 * Created by wenmingvs on 16/5/12.
 */
public class MyWeiBoPopWindow extends PopupWindow {

    private Context mContext;
    private View mView;
    private int mWidth;
    private int mHeight;
    private IGroupItemClick mIGroupItemClick;
    private int mSelectIndex = 0;
    private TextView mAllWeibo;
    private TextView mOriginWeibo;
    private TextView mImgWeibo;


    /**
     * 使用单例模式创建ImageOPtionPopupWindow
     */
    private static MyWeiBoPopWindow mGroupPopWindow;

    public static MyWeiBoPopWindow getInstance(Context context, int width, int height) {
        if (mGroupPopWindow == null) {
            synchronized (ImageOptionPopupWindow.class) {
                if (mGroupPopWindow == null) {
                    mGroupPopWindow = new MyWeiBoPopWindow(context.getApplicationContext(), width, height);
                }
            }
        }
        return mGroupPopWindow;
    }

    private MyWeiBoPopWindow(Context context, int width, int height) {
        super(context);
        this.mContext = context;
        this.mWidth = width;
        this.mHeight = height;
        mView = LayoutInflater.from(context).inflate(R.layout.profilefragment_myweibo__grouplist_pop, null);
        setContentView(mView);
        mAllWeibo = (TextView) mView.findViewById(R.id.allweibo);
        mOriginWeibo = (TextView) mView.findViewById(R.id.originweibo);
        mImgWeibo = (TextView) mView.findViewById(R.id.imgweibo);
        initPopWindow();
        setUpListener();
    }

    private void initPopWindow() {
        this.setWindowLayoutMode(mWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(mWidth);
        this.setHeight(mHeight);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
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
        mAllWeibo.setSelected(true);
        mAllWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disSelectedAll();
                mAllWeibo.setSelected(true);
                mIGroupItemClick.onGroupItemClick(Constants.GROUP_MYWEIBO_TYPE_ALL, "全部微博");
            }
        });
        mOriginWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disSelectedAll();
                mOriginWeibo.setSelected(true);
                mIGroupItemClick.onGroupItemClick(Constants.GROUP_MYWEIBO_TYPE_ORIGIN, "原创微博");
            }
        });
        mImgWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disSelectedAll();
                mImgWeibo.setSelected(true);
                mIGroupItemClick.onGroupItemClick(Constants.GROUP_MYWEIBO_TYPE_PICWEIBO, "图片微博");
            }
        });
    }


    public void setOnGroupItemClickListener(IGroupItemClick groupItemClickListener) {
        this.mIGroupItemClick = groupItemClickListener;
    }

    public void onDestory() {
        if (mGroupPopWindow != null) {
            mGroupPopWindow = null;
        }
    }

    private void disSelectedAll() {
        mAllWeibo.setSelected(false);
        mImgWeibo.setSelected(false);
        mOriginWeibo.setSelected(false);
    }

}

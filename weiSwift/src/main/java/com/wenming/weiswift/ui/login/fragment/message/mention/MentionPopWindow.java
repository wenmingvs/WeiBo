package com.wenming.weiswift.ui.login.fragment.message.mention;

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
public class MentionPopWindow extends PopupWindow {

    /**
     * 使用单例模式创建ImageOPtionPopupWindow
     */
    private static MentionPopWindow mGroupPopWindow;
    private Context mContext;
    private View mView;
    private int mWidth;
    private int mHeight;
    private IGroupItemClick mIGroupItemClick;
    private int mSelectIndex = 0;
    private TextView mAllWeiBo;
    private TextView mFriends_Weibo;
    private TextView mOrigin_Weibo;
    private TextView mAll_Comment;
    private TextView mFriends_Comment;

    private MentionPopWindow(Context context, int width, int height) {
        super(context);
        this.mContext = context;
        this.mWidth = width;
        this.mHeight = height;
        mView = LayoutInflater.from(context).inflate(R.layout.message_mention_grouplist_pop, null);
        setContentView(mView);
        mAllWeiBo = (TextView) mView.findViewById(R.id.allweibo);
        mFriends_Weibo = (TextView) mView.findViewById(R.id.friends_weibo);
        mOrigin_Weibo = (TextView) mView.findViewById(R.id.origin_weibo);
        mAll_Comment = (TextView) mView.findViewById(R.id.all_comment);
        mFriends_Comment = (TextView) mView.findViewById(R.id.friends_comment);
        initPopWindow();
        setUpListener();
    }

    public static MentionPopWindow getInstance(Context context, int width, int height) {
        if (mGroupPopWindow == null) {
            synchronized (ImageOptionPopupWindow.class) {
                if (mGroupPopWindow == null) {
                    mGroupPopWindow = new MentionPopWindow(context.getApplicationContext(), width, height);
                }
            }
        }
        return mGroupPopWindow;
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
        mAllWeiBo.setSelected(true);
        mAllWeiBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disSelectedAll();
                mAllWeiBo.setSelected(true);
                mIGroupItemClick.onGroupItemClick(Constants.GROUP_RETWEET_TYPE_ALL, "所有微博");
            }
        });
        mFriends_Weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disSelectedAll();
                mFriends_Weibo.setSelected(true);
                mIGroupItemClick.onGroupItemClick(Constants.GROUP_RETWEET_TYPE_FRIENDS, "关注人的微博");
            }
        });
        mOrigin_Weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disSelectedAll();
                mOrigin_Weibo.setSelected(true);
                mIGroupItemClick.onGroupItemClick(Constants.GROUP_RETWEET_TYPE_ORIGINWEIBO, "原创微博");
            }
        });

        mAll_Comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disSelectedAll();
                mAll_Comment.setSelected(true);
                mIGroupItemClick.onGroupItemClick(Constants.GROUP_RETWEET_TYPE_ALLCOMMENT, "所有评论");
            }
        });
        mFriends_Comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disSelectedAll();
                mFriends_Comment.setSelected(true);
                mIGroupItemClick.onGroupItemClick(Constants.GROUP_RETWEET_TYPE_FRIEDNSCOMMENT, "关注人的评论");
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
        mAllWeiBo.setSelected(false);
        mFriends_Weibo.setSelected(false);
        mOrigin_Weibo.setSelected(false);
        mAll_Comment.setSelected(false);
        mFriends_Comment.setSelected(false);
    }

}

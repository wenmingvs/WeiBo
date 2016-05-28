package com.wenming.weiswift.ui.login.fragment.message.comment;

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
public class CommentPopWindow extends PopupWindow {

    private Context mContext;
    private View mView;
    private int mWidth;
    private int mHeight;
    private IGroupItemClick mIGroupItemClick;
    private int mSelectIndex = 0;
    private TextView mAllComment;
    private TextView mFriendsComment;
    private TextView mCommentToSend;


    /**
     * 使用单例模式创建ImageOPtionPopupWindow
     */
    private static CommentPopWindow mGroupPopWindow;

    public static CommentPopWindow getInstance(Context context, int width, int height) {
        if (mGroupPopWindow == null) {
            synchronized (ImageOptionPopupWindow.class) {
                if (mGroupPopWindow == null) {
                    mGroupPopWindow = new CommentPopWindow(context.getApplicationContext(), width, height);
                }
            }
        }
        return mGroupPopWindow;
    }

    private CommentPopWindow(Context context, int width, int height) {
        super(context);
        this.mContext = context;
        this.mWidth = width;
        this.mHeight = height;
        mView = LayoutInflater.from(context).inflate(R.layout.message_commment_grouplist_pop, null);
        setContentView(mView);
        mAllComment = (TextView) mView.findViewById(R.id.allcomment);
        mFriendsComment = (TextView) mView.findViewById(R.id.friendscomment);
        mCommentToSend = (TextView) mView.findViewById(R.id.commenttosend);
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
        mAllComment.setSelected(true);
        mAllComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disSelectedAll();
                mAllComment.setSelected(true);
                mIGroupItemClick.onGroupItemClick(Constants.GROUP_COMMENT_TYPE_ALL, "全部评论");
            }
        });
        mFriendsComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disSelectedAll();
                mFriendsComment.setSelected(true);
                mIGroupItemClick.onGroupItemClick(Constants.GROUP_COMMENT_TYPE_FRIENDS, "关注的人");
            }
        });
        mCommentToSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disSelectedAll();
                mCommentToSend.setSelected(true);
                mIGroupItemClick.onGroupItemClick(Constants.GROUP_COMMENT_TYPE_BYME, "我发出的");
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
        mAllComment.setSelected(false);
        mCommentToSend.setSelected(false);
        mFriendsComment.setSelected(false);
    }

}

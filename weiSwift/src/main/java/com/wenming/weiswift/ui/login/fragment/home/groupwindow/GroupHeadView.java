package com.wenming.weiswift.ui.login.fragment.home.groupwindow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.utils.ToastUtil;

/**
 * Created by wenmingvs on 16/4/27.
 */
public class GroupHeadView extends RelativeLayout {

    private View mView;
    private TextView mHome;
    private LinearLayout mBastFriend;
    private Context mContext;

    public GroupHeadView(Context context) {
        super(context);
        init(context);
    }

    public GroupHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GroupHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        mContext = context;
        mView = inflate(context, R.layout.home_grouplist_pop_headview, this);
        mHome = (TextView) mView.findViewById(R.id.allweibo);
        mBastFriend = (LinearLayout) mView.findViewById(R.id.bestfriend);
        mHome.setSelected(true);
        mHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "...");
            }
        });

        mBastFriend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ToastUtil.showShort(mContext, "...");
            }
        });


    }
}

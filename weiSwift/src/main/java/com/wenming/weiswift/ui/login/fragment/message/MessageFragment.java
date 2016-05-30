package com.wenming.weiswift.ui.login.fragment.message;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.wenming.weiswift.R;
import com.wenming.weiswift.utils.ToastUtil;
import com.wenming.weiswift.ui.login.fragment.message.comment.CommentActivity;
import com.wenming.weiswift.ui.login.fragment.message.mention.MentionActivity;

/**
 * Created by wenmingvs on 15/12/26.
 */
public class MessageFragment extends Fragment {
    private Activity mActivity;
    private View mToolBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mView;
    private Context mContext;

    private RelativeLayout mMention_layout;
    private RelativeLayout mComment_layout;
    private RelativeLayout mAttitude_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.messagefragment_layout, container, false);
        mActivity = getActivity();
        mContext = getContext();
        initRefreshLayout();
        setUpListener();
        return mView;
    }

    private void setUpListener() {
        mMention_layout = (RelativeLayout) mView.findViewById(R.id.mention_layout);
        mComment_layout = (RelativeLayout) mView.findViewById(R.id.comment_layout);
        mAttitude_layout = (RelativeLayout) mView.findViewById(R.id.attitude_layout);

        mMention_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MentionActivity.class);
                mContext.startActivity(intent);
            }
        });
        mComment_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                mContext.startActivity(intent);
            }
        });
        mAttitude_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "API暂未支持");
            }
        });


    }


    private void initRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.message_pulltorefresh);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }


}

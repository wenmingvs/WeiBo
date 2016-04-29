package com.wenming.weiswift.fragment.message;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import com.wenming.weiswift.NewFeature;
import com.wenming.weiswift.R;
import com.wenming.weiswift.common.util.ToastUtil;
import com.wenming.weiswift.fragment.message.comment.CommentActivity;
import com.wenming.weiswift.fragment.message.mention.MentionActivity;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (NewFeature.LOGIN_STATUS == true) {
            mView = inflater.inflate(R.layout.messagefragment_layout, container, false);
            initToolBar();
            initRefreshLayout();
            setUpListener();
            return mView;

        } else {
            mView = inflater.inflate(R.layout.messagefragment_unlogin_layout, container, false);
            initToolBar();
            return mView;
        }
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

    private void initToolBar() {
        if (NewFeature.LOGIN_STATUS == true) {
            initLoginState();
        } else {
            initUnLoginState();
        }
    }

    private void initUnLoginState() {
        mActivity = getActivity();
        mContext = mActivity;
        mActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.toolbar_message_unlogin);
        mToolBar = mActivity.findViewById(R.id.toolbar_message_unlogin);
    }

    private void initLoginState() {
        mActivity = getActivity();
        mContext = mActivity;
        mActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.toolbar_message_login);
        mToolBar = mActivity.findViewById(R.id.toolbar_message_login);
    }


    private void initRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.message_pulltorefresh);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            hideToolBar();
        } else {
            showToolBar();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mToolBar.setVisibility(View.GONE);
    }

    public void hideToolBar() {
        mToolBar.setVisibility(View.GONE);
    }

    public void showToolBar() {
        mToolBar.setVisibility(View.VISIBLE);
    }

}

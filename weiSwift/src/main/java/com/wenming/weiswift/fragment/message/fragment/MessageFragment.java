package com.wenming.weiswift.fragment.message.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.wenming.weiswift.NewFeature;
import com.wenming.weiswift.R;
import com.wenming.weiswift.fragment.message.MessageAdapter;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 15/12/26.
 */
public class MessageFragment extends Fragment {
    private Activity mActivity;
    private View mToolBar;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MessageAdapter messageAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private View mView;
    private Context mContext;
    private ArrayList<Integer> mData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (NewFeature.LOGIN_STATUS == true) {
            mView = inflater.inflate(R.layout.messagefragment_layout, container, false);
            initToolBar();
            mData = new ArrayList<Integer>();
            mData.add(R.drawable.messagescenter_at);
            mData.add(R.drawable.messagescenter_comments);
            mData.add(R.drawable.messagescenter_good);
            mData.add(R.drawable.messagescenter_subscription);
            mData.add(R.drawable.messagescenter_messagebox);
            initRecyclerView();
            initRefreshLayout();
            return mView;

        } else {
            mView = inflater.inflate(R.layout.messagefragment_unlogin_layout, container, false);
            initToolBar();
            return mView;
        }
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
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 5000);
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.messageList);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        messageAdapter = new MessageAdapter(mContext, mData);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(messageAdapter);
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

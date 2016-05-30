package com.wenming.weiswift.ui.login.fragment.message.mention;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.mvp.presenter.MentionActivityPresent;
import com.wenming.weiswift.mvp.presenter.imp.MentionActivityPresentImp;
import com.wenming.weiswift.mvp.view.MentionActivityView;
import com.wenming.weiswift.ui.common.login.Constants;
import com.wenming.weiswift.ui.login.fragment.message.IGroupItemClick;
import com.wenming.weiswift.ui.login.fragment.message.ItemSapce;
import com.wenming.weiswift.utils.DensityUtil;
import com.wenming.weiswift.utils.ScreenUtil;
import com.wenming.weiswift.widget.endlessrecyclerview.EndlessRecyclerOnScrollListener;
import com.wenming.weiswift.widget.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.wenming.weiswift.widget.endlessrecyclerview.utils.RecyclerViewStateUtils;
import com.wenming.weiswift.widget.endlessrecyclerview.weight.LoadingFooter;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/4/26.
 */
public class MentionActivity extends Activity implements MentionActivityView {
    private ArrayList<Status> mDatas;
    private MentionAdapter mAdapter;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private MentionActivityPresent mMentionPresent;
    private LinearLayout mGroup;
    private TextView mGroupName;
    private MentionPopWindow mMentionPopWindow;
    private int mCurrentGroup = Constants.GROUP_RETWEET_TYPE_ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.messagefragment_mention_layout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.base_swipe_refresh_widget);
        mRecyclerView = (RecyclerView) findViewById(R.id.base_RecyclerView);
        mGroup = (LinearLayout) findViewById(R.id.mention_group);
        mGroupName = (TextView) findViewById(R.id.mention_name);
        mMentionPresent = new MentionActivityPresentImp(this);
        initRefreshLayout();
        initRecyclerView();
        initGroupWindows();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mMentionPresent.pullToRefreshData(mCurrentGroup, mContext);
            }
        });
    }

    private void initGroupWindows() {
        mGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                int statusBarHeight = rect.top;
                mMentionPopWindow = MentionPopWindow.getInstance(mContext, ScreenUtil.getScreenWidth(mContext) * 3 / 5, ScreenUtil.getScreenHeight(mContext) * 2 / 3);
                mMentionPopWindow.showAtLocation(mGroupName, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, mGroupName.getHeight() + statusBarHeight + DensityUtil.dp2px(mContext, 8));
                mMentionPopWindow.setOnGroupItemClickListener(new IGroupItemClick() {
                    @Override
                    public void onGroupItemClick(long groupId, String groupName) {
                        mCurrentGroup = (int) groupId;
                        setGroupName(groupName);
                        mMentionPopWindow.dismiss();
                        mMentionPresent.pullToRefreshData(mCurrentGroup, mContext);
                    }
                });
            }
        });
    }

    public void setGroupName(String groupName) {
        mGroupName.setText(groupName);
    }

    private void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMentionPresent.pullToRefreshData(mCurrentGroup, mContext);
            }
        });
    }


    private void initRecyclerView() {
        mAdapter = new MentionAdapter(mContext, mDatas);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        mRecyclerView.addItemDecoration(new ItemSapce(DensityUtil.dp2px(mContext, 14)));
    }


    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (mDatas != null && mDatas.size() > 0) {
                showLoadFooterView();
                mMentionPresent.requestMoreData(mCurrentGroup, mContext);
            }
        }
    };


    public void onArrorClick(View view) {
        finish();
    }


    @Override
    public void updateListView(ArrayList<Status> statuselist) {
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mDatas = statuselist;
        mAdapter.setData(statuselist);
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }


    @Override
    public void showLoadingIcon() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingIcon() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoadFooterView() {
        RecyclerViewStateUtils.setFooterViewState(MentionActivity.this, mRecyclerView, mDatas.size(), LoadingFooter.State.Loading, null);
    }

    @Override
    public void hideFooterView() {
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
    }

    @Override
    public void showEndFooterView() {
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.TheEnd);
    }

    @Override
    public void showErrorFooterView() {
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.NetWorkError);
    }

    @Override
    public void scrollToTop(boolean refreshData) {
        mRecyclerView.scrollToPosition(0);
        if (refreshData) {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mMentionPresent.pullToRefreshData(mCurrentGroup, mContext);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        if (mMentionPopWindow != null) {
            mMentionPopWindow.onDestory();
        }
        super.onDestroy();
    }

}
